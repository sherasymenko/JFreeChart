package app.media;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

@SuppressWarnings("deprecation")
public class SceneGenerator {
	private final StackPane layout = new StackPane();
	private final Label currentlyPlaying = new Label();
	private final ProgressBar progress = new ProgressBar();
	private ChangeListener<Duration> progressChangeListener;
	private MediaPlayer player;
	private MediaView mediaView;

	public MediaView getMediaView() {
		return mediaView;
	}

	public Scene createScene(String videoPath) throws MediaException {
		final File dir = new File(videoPath);
		if (!dir.exists() || dir.isDirectory()) {
			System.out.println("Cannot find video file: " + dir);
		}
		player = createPlayer(("file:///" + videoPath).replace("\\", "/").replaceAll(" ", "%20"));
		mediaView = new MediaView(player);
		mediaView.mediaPlayerProperty().addListener(new ChangeListener<MediaPlayer>() {
			@Override
			public void changed(ObservableValue<? extends MediaPlayer> observableValue, MediaPlayer oldPlayer,
					MediaPlayer newPlayer) {
				setCurrentlyPlaying(newPlayer);
			}
		});
		mediaView.setMediaPlayer(player);
		setCurrentlyPlaying(mediaView.getMediaPlayer());
		layout.setStyle("-fx-background-color: cornsilk; -fx-font-size: 20; -fx-padding: 20; -fx-alignment: center;");
		layout.getChildren()
				.addAll(VBoxBuilder.create().spacing(10).alignment(Pos.CENTER)
						.children(currentlyPlaying, mediaView,
								HBoxBuilder.create().spacing(10).alignment(Pos.CENTER).children(progress).build())
						.build());
		progress.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(progress, Priority.ALWAYS);
		return new Scene(layout, 800, 600);
	}

	private void setCurrentlyPlaying(final MediaPlayer newPlayer) {
		progress.setProgress(0);
		progressChangeListener = new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue,
					Duration newValue) {
				progress.setProgress(
						1.0 * newPlayer.getCurrentTime().toMillis() / newPlayer.getTotalDuration().toMillis());
			}
		};
		newPlayer.currentTimeProperty().addListener(progressChangeListener);
		String source = newPlayer.getMedia().getSource();
		source = source.substring(0, source.length() - ".mp4".length());
		source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");
		currentlyPlaying.setText("Now Playing: " + source);
	}

	public MediaPlayer createPlayer(String aMediaSrc) throws MediaException {
		final MediaPlayer player = new MediaPlayer(new Media(aMediaSrc));
		player.setOnError(new Runnable() {
			@Override
			public void run() {
				System.out.println("Media error occurred: " + player.getError());
			}
		});
		return player;
	}
}