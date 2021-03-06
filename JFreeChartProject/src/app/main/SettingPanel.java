package app.main;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import app.graph.OpenSheet;
import app.media.PlayVideo;
import javafx.scene.media.MediaException;

@SuppressWarnings("serial")
public class SettingPanel extends JFrame {
	private static JButton start = new JButton("Start");
	private JButton resetSetting = new JButton("Wyczy�� ustawienia");
	private JButton refreshSelectedFile = new JButton("O");
	private JButton selectFile = new JButton("wybierz plik...");
	private JButton refreshSelectedVideo = new JButton("O");
	private JButton selectVideo = new JButton("wybierz filmik...");
	private JLabel errorMessage = new JLabel("Znaleziono b��dy: ");
	private JLabel videoStartTimeLabel = new JLabel("Czas rozpocz�cia wideo");
	private JLabel graphStartTimeLabel = new JLabel("Czas rozpocz�cia wykresu");
	private JLabel speedLabel = new JLabel("Szybko��");
	private JTextField selectedFilePath = new JTextField("C:\\Users\\sherasymenko\\Downloads\\wykres\\Svieta_badanie1_p_s16_l17_u18_2-000_00342816.txt", 50);
	private JTextField selectedVideoPath = new JTextField("C:\\Users\\sherasymenko\\Downloads\\wykres\\Svieta_badanie1_p_s16_l17_u18_2.mp4", 50);
	private JTextField videoStartTime = new JTextField("00:00:00.000", 12);
	private JTextField graphStartTime = new JTextField("00:00:00.000", 12);
	private JPanel newPanel = new JPanel();
	private JPanel line1 = new JPanel();
	private JPanel line2 = new JPanel();
	private JPanel line3 = new JPanel();
	private JPanel line4 = new JPanel();
	private JPanel line5 = new JPanel();
	private JRadioButton speed1 = new JRadioButton("0.1");
	private JRadioButton speed2 = new JRadioButton("0.25");
	private JRadioButton speed3 = new JRadioButton("0.5");
	private JRadioButton speed4 = new JRadioButton("1");
	private GridBagConstraints constraints = new GridBagConstraints();
	private OpenSheet chart = null;
	private PlayVideo player = null;

	public SettingPanel(OpenSheet chart, PlayVideo player) {
		super("Panel sterowania");
		this.chart = chart;
		this.player = player;
		elementsSetting();
		addElementsToPanel();
		panelSetting();
	}

	public static void setStartButton() {
		start.setText("Start");

	}

	private void elementsSetting() {
		errorMessage.setVisible(false);
		errorMessage.setForeground(Color.red);
		resetSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chart.resetChart();
				player.resetPlayer();
				//selectedFilePath.setText("");
				//selectedVideoPath.setText("");
				speed1.setSelected(false);
				speed2.setSelected(false);
				speed3.setSelected(false);
				speed4.setSelected(true);
				videoStartTime.setText("00:00:00.000");
				graphStartTime.setText("00:00:00.000");
			}
		});
		refreshSelectedFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					chart.addData(selectedFilePath.getText(), getTimeInMilis(graphStartTime.getText()));
				} catch (IOException e1) {
				}
			}
		});
		selectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(newPanel);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					selectedFilePath.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		refreshSelectedVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					player.selectVideo(selectedVideoPath.getText(), getTimeInMilis(videoStartTime.getText()));
				} catch (MediaException ex) {
				}
			}
		});
		selectVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(newPanel);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					selectedVideoPath.setText(selectedFile.getAbsolutePath());
				}
			}
		});
			start.addActionListener(new ActionListener() {
				List<JRadioButton> radioList = Arrays.asList(speed1, speed2, speed3, speed4);
				public void actionPerformed(ActionEvent e) {
					System.out.println("videoStartTime.getText() " + videoStartTime.getText());
					double test = getTimeInMilis(videoStartTime.getText());
					if (start.getText().equals("Start")) {
						chart.startDraw(selectedFilePath.getText(), false, getSpeed(radioList));
						player.startButton(selectedVideoPath.getText(), false, getSpeed(radioList));
						start.setText("Pauza");
					} else if (start.getText().equals("Pauza")) {
						player.pauseButton(selectedVideoPath.getText());
						chart.pauseDraw(selectedFilePath.getText());
						start.setText("Start");
					} else if (start.getText().equals("Reset")) {
						chart.startDraw(selectedFilePath.getText(), true, getSpeed(radioList));
						player.startButton(selectedVideoPath.getText(), true, getSpeed(radioList));
						start.setText("Pauza");
					}
				}
			});
		
		speed1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (speed1.isSelected()) {
					speed2.setSelected(false);
					speed3.setSelected(false);
					speed4.setSelected(false);
				}
			}
		});
		speed2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (speed2.isSelected()) {
					speed1.setSelected(false);
					speed3.setSelected(false);
					speed4.setSelected(false);
				}
			}
		});
		speed3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (speed3.isSelected()) {
					speed1.setSelected(false);
					speed2.setSelected(false);
					speed4.setSelected(false);
				}
			}
		});
		
		speed4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (speed4.isSelected()) {
					speed1.setSelected(false);
					speed2.setSelected(false);
					speed3.setSelected(false);
				}
			}
		});
	}

	public double getSpeed(List<JRadioButton> radio) {
		radio = radio.stream().filter(s -> s.isSelected()).collect(Collectors.toList());
		return new Double(radio.get(0).getName());
	}
	
	public double getTimeInMilis(String time)  {
		String[] parts = time.split(":");
		System.out.println(" parts 0   " + parts[0]);
		System.out.println(" parts 1   " + parts[1]);
		System.out.println(" parts 2   " + parts[2]);
		double hours = new Double(parts[0]);
		hours = hours*3600000;
		System.out.println("hours  " + hours);
		double minutes = new Double(parts[1]);
		minutes = minutes*60000;
		System.out.println("minutes " + minutes);
		double second = new Double(parts[2]);
		second = second*1000;
		System.out.println("second " + second);
		double sum = hours + minutes + second;
		System.out.println("hours + minutes + second " + sum);
		return sum;
	}

	private void addElementsToPanel() {
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
		speed1.setName("0.1");
		speed2.setName("0.25");
		speed3.setName("0.5");
		speed4.setName("1");
		speed4.setSelected(true);
		line1.add(errorMessage);
		line2.add(refreshSelectedFile);
		line2.add(selectedFilePath);
		line2.add(selectFile);
		line3.add(refreshSelectedVideo);
		line3.add(selectedVideoPath);
		line3.add(selectVideo);
		line4.add(videoStartTimeLabel);
		line4.add(videoStartTime);
		line4.add(graphStartTimeLabel);
		line4.add(graphStartTime);
		line4.add(speedLabel);
		line4.add(speed1);
		line4.add(speed2);
		line4.add(speed3);
		line4.add(speed4);
		line5.add(start);
		line5.add(resetSetting);
		newPanel.add(line1);
		newPanel.add(line2);
		newPanel.add(line3);
		newPanel.add(line4);
		newPanel.add(line5);
	}

	private void panelSetting() {
		setSize(600, 700);
		setVisible(true);
		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ustawienia"));
		add(newPanel);
		pack();
		setLocationRelativeTo(null);
	}
}