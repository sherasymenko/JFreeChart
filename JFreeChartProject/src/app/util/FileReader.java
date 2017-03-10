package app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReader {
	private final Logger LOGGER = Logger.getLogger(FileReader.class.getName());
	private final String filePath;
	private int headerLineIndex;
	private int headerNumber;
	private int dataNumber;

	public FileReader(String filePath) {
		super();
		this.filePath = filePath;
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
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (currentLineIndex > headerLineIndex) {
				String lineData[] = line.split(";");
				for (int i = 0; i < headerNumber; i++) {
					data[currentDataIndex][i] = new String(lineData[i]);
				}
				currentDataIndex++;
			}
			currentLineIndex++;
		}
		dataNumber = currentDataIndex;
		fis.close();
		return data;
	}

	public int getHeaderLineIndex() {
		return headerLineIndex;
	}

	public void setHeaderLineIndex(int headerLineIndex) {
		this.headerLineIndex = headerLineIndex;
	}

	public int getHeaderNumber() {
		return headerNumber;
	}

	public void setHeaderNumber(int headerNumber) {
		this.headerNumber = headerNumber;
	}

	public int getDataNumber() {
		return dataNumber;
	}

	public void setDataNumber(int dataNumber) {
		this.dataNumber = dataNumber;
	}
}