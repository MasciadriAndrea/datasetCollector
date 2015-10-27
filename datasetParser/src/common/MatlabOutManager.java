package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.NotDirectoryException;
import java.util.List;
import java.util.TreeMap;

import dataModel.Activity;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Resident;

public class MatlabOutManager {
	private static MatlabOutManager instance;
	private static String urlData = "dataOut/matlab";
	private static String fileSad = "/sadResident";
	private static String fileSecDay = "/secondDay";
	private static String fileSS = "/uniqueSS";

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
		computeSensorsets(h);
		System.out.println("Finish exporting Matlab matrices");
	}

	private void computeSensorsets(House h) {
		System.out.println("Computing uniqueSS");
		try {
			File folder = new File(urlData);
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}
			File currentFile = new File(urlData+fileSS+".txt");
			FileOutputStream fos = new FileOutputStream(currentFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for (HSensorset ss : h.getSensorsets()) {
				String line=ss.getUniqueSensorsetId().toString();
				line+=","+ss.getStringSS();
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void computeSadMatrix(House h) {
		try {
			File folder = new File(urlData);
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}
		for(Resident resident:h.getResidents()){
			System.out.println("Computing sad matrix for resident "+resident.getUniqueResidentId()+" (wait: long procedure)");
				File currentFile = new File(urlData+fileSad+resident.getUniqueResidentId().toString()+".txt");
				FileOutputStream fos = new FileOutputStream(currentFile);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				for (Day d : h.getDays()) {
					int[] actSeconds=new int[86400];
					String line="";
					TreeMap<Integer, DayHasActivity> orderedAct = new TreeMap<Integer, DayHasActivity>();
					for(DayHasActivity a:d.getDailyActivities()){
						if(a.getResident().getUniqueResidentId().equals(resident.getUniqueResidentId())){
							orderedAct.put(a.getStartSec(), a);
						}
					}
					for (Integer key: orderedAct.keySet()) {
						  DayHasActivity dha=orderedAct.get(key);
						  Integer idA=dha.getActivity().getUniqueActivityId();
						  for(Integer sec=dha.getStartSec();sec<=dha.getEndSec();sec++){
							  actSeconds[sec]=idA;
						  }  
					}
					for(int i=0;i<86400;i++){
						line+= actSeconds[i]+ ",";
					}
					line=line.substring(0,line.length()-1);
					bw.write(line);
					bw.newLine();
				}
				bw.close();
			
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void computeSecondDayMatrix(House h) {
		System.out.println("Computing second day matrix");
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
			e.printStackTrace();
		}

	}
}
