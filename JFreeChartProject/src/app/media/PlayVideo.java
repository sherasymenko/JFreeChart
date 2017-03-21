package app.media;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.MediaException;

public class PlayVideo {
	private SceneGenerator sg;
	private JFXPanel fxPanel;
	private JFrame frame;
	public void initAndShowGUI() {
		frame = new JFrame("FX");
		fxPanel = new JFXPanel();
		frame.add(fxPanel);
		frame.setBounds(200, 100, 800, 600);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void startButton(String videoPath, boolean toClean, double speed) {
		if (toClean)
			sg.getMediaView().getMediaPlayer().stop();
		sg.getMediaView().getMediaPlayer().setRate(speed);
		sg.getMediaView().getMediaPlayer().play();
	}

	public void pauseButton(String videoPath) {
		sg.getMediaView().getMediaPlayer().pause();
	}
	
	public void resetPlayer(){
		frame.getContentPane().removeAll();
		fxPanel = new JFXPanel();
		frame.add(fxPanel);
		frame.setBounds(200, 100, 800, 600);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.revalidate();
		frame.repaint();
	}

	public void selectVideo(String videoPath, double startTime) throws MediaException {
		sg = new SceneGenerator(startTime);
		Scene scene = sg.createScene(videoPath);
		fxPanel.setScene(scene);
	}
}