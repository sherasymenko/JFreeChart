package app.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReader {
	private final Logger LOGGER = Logger.getLogger(FileReader.class.getName());
	private final String filePath;
	private int headerLineIndex;
	private int headerNumber;
	private Map<String, String[]> allData = new HashMap<String, String[]>();
	private double xMax;
	private double xMin;
	private double yMax;
	private double yMin;
	private double period;
	private double startTime;

	public FileReader(String filePath, double startTime) throws IOException {
		super();
		this.filePath = filePath;
this.startTime = startTime;
		read();
		this.period = 75.0;
		this.xMax = 1 / period * (getRowsNumber(filePath) - 1);
		this.xMin = new Double(0);
		this.yMax = getMaxValue(allData);
		this.yMin = getMinValue(allData);
	
		System.out.println("yMax " + yMax);
		System.out.println("yMin " + yMin);
	}

	private void read() throws IOException {
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		double period = new Double(75);
		
		int numberOfLineToPass = (int) Math.ceil(startTime /1000 / (1 / period));
		System.out.println("numberOfLineToPass  " + numberOfLineToPass);
		int rowsNumber = getRowsNumber(filePath) - numberOfLineToPass - 1;
		System.out.println("rowsNumber " + rowsNumber);
		int currentRowIndex = 0;
		int currentPassRowIndex = 0;
		String[] headers = getHeaders();
		String row;

		for (int h = 0; h < headers.length; h++) {
			allData.put(headers[h], new String[rowsNumber]);
		}

		while ((row = br.readLine()) != null) {
			if (currentPassRowIndex > numberOfLineToPass) {
				row = row.trim();
				String rowData[] = row.split(";");
				for (int i = 0; i < rowData.length; i++) {
					String[] dataTest = (String[]) allData.get(headers[i]);
					dataTest[currentRowIndex] = new String(rowData[i]);
				}
				currentRowIndex++;
			}
			currentPassRowIndex++;
		}
		br.close();
	}

	public double getMaxValue(Map data) {
		String[] graphs = { "Acc_X", "Acc_Y", "Acc_Z" };
		String[] values = (String[]) data.get(graphs[0]);
		System.out.println(" values.length " + values.length);
		double maxValue = new Double(values[0]);
		for (int i = 0; i < graphs.length; i++) {
			values = (String[]) data.get(graphs[i]);
			for (int j = 0; j < values.length; j++) {
				System.out.println("j " + j + "  new Double(values[j]) " + new Double(values[j]));
				maxValue = Double.max(maxValue, new Double(values[j]));
			}
		}
		System.out.println("max value " + maxValue);
		return maxValue;
	}

	public double getMinValue(Map data) {
		String[] graphs = { "Acc_X", "Acc_Y", "Acc_Z" };
		String[] values = (String[]) data.get(graphs[0]);
		double minValue = new Double(values[0]);
		for (int i = 0; i < graphs.length; i++) {
			values = (String[]) data.get(graphs[i]);
			for (int j = 0; j < values.length; j++) {
				minValue = Double.min(minValue, new Double(values[j]));
			}
		}
		System.out.println("min value " + minValue);
		return minValue;
	}

	public String[] getHeaders() throws FileNotFoundException {
		String[] headers = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;
			headerLineIndex = 0;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.contains("PacketCounter")) {
					headers = line.split(";");
					break;
				}
				headerLineIndex++;
			}
			headerNumber = headers.length;
			fis.close();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, e.toString());
		}
		return headers;
	}

	public int getRowsNumber(String filePath) throws IOException {
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		int lines = 0;
		while (br.readLine() != null)
			lines++;
		br.close();
		return lines;
	}
/*
	public String[][] getData() throws IOException {
		String[][] data = null;
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		int dataLineNumber = getRowsNumber(filePath) - (headerLineIndex + 1);
		data = new String[dataLineNumber][headerNumber];
		String line;
		int currentLineIndex = 0;
		int currentDataIndex = 0;
		double period = new Double(75);
		double startTime = new Double(3);
		int numberOfLineToPass = (int) Math.ceil(startTime / (1 / period));
		System.out.println("numberOfLineToPass  " + numberOfLineToPass);
		while ((line = br.readLine()) != null) {
			line = line.trim();

			String lineData[] = line.split(";");
			if (currentLineIndex > headerLineIndex) {
				if (currentLineIndex > numberOfLineToPass) {
					for (int i = 0; i < headerNumber; i++) {
						data[currentDataIndex][i] = new String(lineData[i]);
					}
					currentDataIndex++;
				}
			}
			currentLineIndex++;
		}
		fis.close();
		return data;
	}*/

	public Map<String, String[]> getAllData() {
		return allData;
	}

	public void setAllData(Map<String, String[]> allData) {
		this.allData = allData;
	}

	public int getHeaderLineIndex() {
		return headerLineIndex;
	}

	public void setHeaderLineIndex(int headerLineIndex) {
		this.headerLineIndex = headerLineIndex;
	}

	public double getxMax() {
		return xMax;
	}

	public void setxMax(double xMax) {
		this.xMax = xMax;
	}

	public double getxMin() {
		return xMin;
	}

	public void setxMin(double xMin) {
		this.xMin = xMin;
	}

	public double getyMax() {
		return yMax;
	}

	public void setyMax(double yMax) {
		this.yMax = yMax;
	}

	public double getyMin() {
		return yMin;
	}

	public void setyMin(double yMin) {
		this.yMin = yMin;
	}

	public double getPeriod() {
		return period;
	}

	public void setPeriod(double period) {
		this.period = period;
	}

}