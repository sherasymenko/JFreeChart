package app.main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.ui.RefineryUtilities;
import app.graph.OpenSheet;
import app.media.PlayVideo;
import app.ui.SettingPanel;

public class Main {
	private static OpenSheet sheet = new OpenSheet("", "");
	private static PlayVideo player = new PlayVideo();

	public static void main(String[] args) {
		openSheetGUI();
		openMediaPlayerGUI();
		openSettingsGUI();
	}

	private static void openSheetGUI() {
		RefineryUtilities.centerFrameOnScreen(sheet);
		sheet.setVisible(true);
		sheet.pack();
	}

	private static void openMediaPlayerGUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				player.initAndShowGUI();
			}
		});
	}

	private static void openSettingsGUI() {
		SettingPanel settingPanel = new SettingPanel(sheet, player);
		settingPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}