package app.graph;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

import app.main.SettingPanel;

@SuppressWarnings("serial")
public class OpenSheet extends ApplicationFrame {
	private XYSeries[] graphs;
	/*private XYSeries roll = new XYSeries("Roll");
	private XYSeries pitch = new XYSeries("Pitch");
	private XYSeries yaw = new XYSeries("Yaw");*/
	private JPanel content = new JPanel(new BorderLayout());
	private XYSeriesCollection dataset = new XYSeriesCollection();
	private Timer timer;
	private FileReader reader;
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

	public void addData(String filePath, double startTime) throws IOException {
		reader = new FileReader(filePath, startTime);
		graphs = new XYSeries[reader.getHeaders().length];
		for(int i = 0;i < graphs.length; i++){
			graphs[i] = new XYSeries(reader.getHeaders()[i]);
		}	
		
		xylineChart = ChartFactory.createXYLineChart(chartTitle, "Time", "Value", createDataset(),
				PlotOrientation.VERTICAL, true, true, false);
		xylineChart.getXYPlot().getDomainAxis().setRange(reader.getxMin(), reader.getxMax());
		xylineChart.getXYPlot().getRangeAxis().setRange(reader.getyMin(), reader.getyMax());
		chartPanel.setChart(xylineChart);
		chartPanel.setMouseZoomable(true);
	}
	
	public void resetChart(){
		content.removeAll();
		content.revalidate();
		content.repaint();
			for(int i = 0;i < graphs.length; i++){
				graphs[i].clear();
			}		
			index = 0;
			timeTest = 0;
		dataset = new XYSeriesCollection();
		index = 0;
		timer.cancel();
		xylineChart = ChartFactory.createXYLineChart(chartTitle, "", "", new XYSeriesCollection());
		chartPanel = new ChartPanel(xylineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		chartPanel.setMouseZoomable(false);
		content.add(chartPanel, BorderLayout.CENTER);
		setContentPane(content);
	}

	public void startDraw(String filePath, boolean toClean, double speed) {
		if (toClean) {
			for(int i = 0;i < graphs.length; i++){
				graphs[i].clear();
			}		
			index = 0;
			timeTest = 0;
		}
		timer = new Timer();
		draw = new Draw(reader, timer, index, timeTest, isClean, filePath);
		long period = (long) ((1 / reader.getPeriod() * 1000) / speed);
		timer.schedule(draw, 0, period);
	}

	public void pauseDraw(String filePath) {
		index = draw.getI();
		timeTest = draw.getTime();
		timer.cancel();
	}

	private XYDataset createDataset() {
		for(int i = 0;i < graphs.length; i++){
			dataset.addSeries(graphs[i]);
		}
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
		private int i = 0;
		private double time = 0;
		private String filePath = "";
		private FileReader reader = null;
		private Map<String, String[]> allData = new HashMap<String, String[]>();

		public Draw(FileReader reader, Timer timer, int index, double time, boolean isClean, String filePath) {
			this.reader = reader;
			i = index;
			this.time = time;
			this.filePath = filePath;
			this.allData = reader.getAllData();
		}

		public Draw() {
		}

		public void run() {
			try {
				if ((reader.getRowsNumber(filePath) - 1) != i) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! " + (reader.getRowsNumber(filePath) - 1));
					for(int j = 0;j < graphs.length; j++){
						graphs[j].add(time, new Double(allData.get(reader.getHeaders()[j])[i]));
					}
					time = time + 1 / reader.getPeriod();
					i++;
				} else {
					SettingPanel.setStartButton();
					timer.cancel();
				}
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
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