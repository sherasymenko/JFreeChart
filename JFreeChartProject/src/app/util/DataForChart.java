package app.util;

public class DataForChart {
	private String chartName;
	private String[] headers;
	private String[][] data;
	private double period;
	private double xMax;
	private double xMin;
	private double yMax;
	private double yMin;

	public DataForChart(String chartName, String[] headers, String[][] data) {
		super();
		this.chartName = chartName;
		this.headers = headers;
		this.data = data;
		this.period = 75.0;
		this.xMax = 1 / period * data.length;
		this.xMin = new Double(0);
		this.yMax = getMaxValue(data);
		this.yMin = getMinValue(data);
	}

	public double getMaxValue(String[][] data) {
		double maxValue = new Double(data[0][1]);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				if (j != 0)
					maxValue = Double.max(maxValue, new Double(data[i][j]));
			}
		}
		return maxValue;
	}

	public double getMinValue(String[][] data) {
		double minValue = new Double(data[0][1]);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				if (j != 0)
					minValue = Double.min(minValue, new Double(data[i][j]));
			}
		}
		return minValue;
	}

	public String getChartName() {
		return chartName;
	}

	public String[] getHeaders() {
		return headers;
	}

	public String[][] getData() {
		return data;
	}

	public double getxMax() {
		return xMax;
	}

	public double getxMin() {
		return xMin;
	}

	public double getyMax() {
		return yMax;
	}

	public double getyMin() {
		return yMin;
	}

	public double getPeriod() {
		return period;
	}
}