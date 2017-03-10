package app.graph;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import app.ui.SettingPanel;
import app.util.DataForChart;
import app.util.FileReader;

@SuppressWarnings("serial")
public class OpenSheet extends ApplicationFrame {
	private XYSeries roll = new XYSeries("Roll");
	private XYSeries pitch = new XYSeries("Pitch");
	private XYSeries yaw = new XYSeries("Yaw");

	private String[][] data;
	private JPanel content = new JPanel(new BorderLayout());
	private XYSeriesCollection dataset = new XYSeriesCollection();
	private Timer timer;
	private FileReader reader;
	private String[] headers;
	private DataForChart dataForChart;
	private String chartTitle;
	private ChartPanel chartPanel;
	private JFreeChart xylineChart;
	private Draw draw = new Draw();
	private int index = 0;
	private double timeTest = 0;
	private boolean isClean = false;

	public OpenSheet(String applicationTitle, String chartTitle) {
		super(applicationTitle);
		this.chartTitle = chartTitle;
		xylineChart = ChartFactory.createXYLineChart(chartTitle, "", "", new XYSeriesCollection());
		chartPanel = new ChartPanel(xylineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		chartPanel.setMouseZoomable(false);
		content.add(chartPanel, BorderLayout.CENTER);
		setContentPane(content);
	}

	public void addData(String filePath) throws IOException {
		reader = new FileReader(filePath);
		headers = reader.getHeaders();
		data = reader.getData();
		dataForChart = new DataForChart(chartTitle, headers, data);
		xylineChart = ChartFactory.createXYLineChart(chartTitle, "Time", "Value", createDataset(),
				PlotOrientation.VERTICAL, true, true, false);
		xylineChart.getXYPlot().getDomainAxis().setRange(dataForChart.getxMin(), dataForChart.getxMax());
		xylineChart.getXYPlot().getRangeAxis().setRange(dataForChart.getyMin(), dataForChart.getyMax());
		chartPanel.setChart(xylineChart);
		chartPanel.setMouseZoomable(true);
	}

	public void startDraw(String filePath, boolean toClean, double speed) {
		if (toClean) {
			roll.clear();
			pitch.clear();
			yaw.clear();
			index = 0;
			timeTest = 0;
		}
		timer = new Timer();
		draw = new Draw(dataForChart, timer, index, timeTest, isClean);
		long period = (long) ((1 / dataForChart.getPeriod() * 1000) / speed);
		timer.schedule(draw, 0, period);
	}

	public void pauseDraw(String filePath) {
		index = draw.getI();
		timeTest = draw.getTime();
		timer.cancel();
	}

	private XYDataset createDataset() {
		int dataNumber = reader.getDataNumber();
		int headerNumber = reader.getHeaderNumber();
		for (int i = 0; i < dataNumber; i++) {
			for (int j = 0; j < headerNumber; j++) {
			}
		}
		dataset.addSeries(roll);
		dataset.addSeries(pitch);
		dataset.addSeries(yaw);
		return dataset;
	}

	public static void main(String[] args) {
		OpenSheet chart = new OpenSheet("", "");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}

	public boolean isClean() {
		return isClean;
	}

	public void setClean(boolean isClean) {
		this.isClean = isClean;
	}

	private class Draw extends TimerTask {
		private DataForChart dataForChart;
		private int i = 0;
		private double time = 0;

		public Draw(DataForChart dataForChart, Timer timer, int index, double time, boolean isClean) {
			this.dataForChart = dataForChart;
			i = index;
			this.time = time;
		}

		public Draw() {
		}

		public void run() {

			if (data.length != i) {
				String[][] dataTest = dataForChart.getData();
				double rollValue = new Double(dataTest[i][1]);
				double pitchValue = new Double(dataTest[i][2]);
				double yawValue = new Double(dataTest[i][3]);
				roll.add(time, rollValue);
				pitch.add(time, pitchValue);
				yaw.add(time, yawValue);
				time = time + 1 / dataForChart.getPeriod();
				i++;
			} else {
				SettingPanel.setStartButtonOnReset();
				timer.cancel();
			}
		}

		public double getTime() {
			return time;
		}

		public int getI() {
			return i;
		}
	}
}