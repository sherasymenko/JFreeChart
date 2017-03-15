package app.util;

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

	public FileReader(String filePath) throws IOException {
		super();
		this.filePath = filePath;

		read();
		this.period = 75.0;
		this.xMax = 1 / period * (getLineNumber(filePath)-1);
		this.xMin = new Double(0);
		this.yMax = getMaxValue(allData);
		this.yMin = getMinValue(allData);
	}

	
	private void read() throws IOException{
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		int linesNumber = getLineNumber(filePath);
		//String[] dataTest = new String[linesNumber];
		Map<String, String[]> m = new HashMap<String, String[]>();
		String[] headers =  getHeaders();
		int currentLineIndex = 0;
		String line;
		
		while ((line = br.readLine()) != null) {
			line = line.trim();
			String lineData[] = line.split(";");
			for (int i = 0; i < lineData.length; i++) {
				if(currentLineIndex==0){
					m.put(lineData[i], new String[linesNumber]);
				}else{
					String[] dataTest = (String[]) m.get(headers[i]);
					dataTest[currentLineIndex] = new String(lineData[i]);
				}
			}
			currentLineIndex++;
		}
		allData = m;
		br.close();
	}
	
	public double getMaxValue(Map data) {
		String[] graphs = {"Acc_X","Acc_Y","Acc_Z"};
		String[] values = (String[]) data.get(graphs[0]);
		double maxValue = new Double(values[0]);
		for (int i = 0; i < graphs.length; i++) {
			values = (String[]) data.get(graphs[i]);
			for (int j = 0; j < values.length; j++) {
					maxValue = Double.max(maxValue, new Double(values[j]));
			}
		}
		return maxValue;
	}

	public double getMinValue(Map data) {
		String[] graphs = {"Acc_X","Acc_Y","Acc_Z"};
		String[] values = (String[]) data.get(graphs[0]);
		double minValue = new Double(values[0]);
		for (int i = 0; i < graphs.length; i++) {
			values = (String[]) data.get(graphs[i]);
			for (int j = 0; j < values.length; j++) {
				minValue = Double.min(minValue, new Double(values[j]));
			}
		}
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

	public int getLineNumber(String filePath) throws IOException {
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		int lines = 0;
		while (br.readLine() != null)
			lines++;
		br.close();
		return lines;
	}

	
	public String[][] getData() throws IOException {
		String[][] data = null;
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		int dataLineNumber = getLineNumber(filePath) - (headerLineIndex + 1);
		data = new String[dataLineNumber][headerNumber];
		String line;
		int currentLineIndex = 0;
		int currentDataIndex = 0;
		double period = new Double(75);
		double startTime = new Double(3);
		int numberOfLineToPass = (int) Math.ceil(startTime/(1/period));
		System.out.println("numberOfLineToPass  " + numberOfLineToPass);
		while ((line = br.readLine()) != null) {
			line = line.trim();
			
				String lineData[] = line.split(";");if (currentLineIndex > headerLineIndex) {
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
	}
	
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