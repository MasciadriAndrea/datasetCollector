package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.NotDirectoryException;

import dataModel.Day;
import dataModel.House;

public class MatlabOutManager {
	private static MatlabOutManager instance;
	private static String urlData = "dataOut";
	private static String fileSad = "/sad";
	private static String fileSecDay = "/secondDayR";

	private MatlabOutManager() {
		super();
	}

	public static MatlabOutManager getInstance() {
		if (instance == null) {
			instance = new MatlabOutManager();
		}
		return instance;
	}

	public void createMatrices(House h) {
		computeSadMatrix(h);
		computeSecondDayMatrix(h);
	}

	public void computeSadMatrix(House h) {
		// one for every resident
	}

	public void computeSecondDayMatrix(House h) {

		try {
			File folder = new File(urlData);
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}
			File currentFile = new File(urlData+fileSecDay+".txt");
			FileOutputStream fos = new FileOutputStream(currentFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for (Day d : h.getDays()) {
				bw.write(d.getSecondIdSS());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
