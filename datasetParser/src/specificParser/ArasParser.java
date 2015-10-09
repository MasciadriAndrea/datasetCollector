package specificParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;

import dataModel.Activity;
import dataModel.Dataset;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.HSensor;
import dataModel.HSensorset;
import dataModel.Location;
import dataModel.Resident;
import dataModel.SecondHasSensorset;
import dataModel.SensorTime;

public class ArasParser extends GenericParser {
	
	private static String urlData="dataIn/aras";
	private static String folderConf="/confFile";
	private static String fileSensor="/sensors.conf";
	private static String fileActivity="/activities.conf";
	private static String fileLocation="/locations.conf";
	private static String fileResident="/residents.conf";
	private static String filePRE="DAY_";
	private static Integer startingDay=0;
	
	private ArasParser(){
		super();
	}

	@Override
	public GenericParser getInstance(){
		if(instance==null){
			instance=new ArasParser();
		}
		return instance;
	}

	@Override
	public List<Activity> getActivityList()  {
		List<Activity> al=new ArrayList<Activity>();
		try{
			File folder = new File(urlData+folderConf);
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}
			
			//read the conf file
			File currentFile = new File(urlData+folderConf+fileActivity);
			BufferedReader reader;
			if (!currentFile.exists()) {
				throw new FileNotFoundException(null);
			}
			ArrayList<String> configLines = new ArrayList<String>();
			reader = new BufferedReader(new FileReader(currentFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				configLines.add(line);
			}
			reader.close();
			
			//scan the line to get parameters
			for (String pattern : configLines) {
				String[] chunks = pattern.split(",");
				//TODO al.add(new Activity());
			}
		
		}catch(IOException e){
			e.printStackTrace();
		}
		return al;
	}

	@Override
	public List<Resident> getResidentList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Location> getLocationList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HSensor> getSensorList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HSensorset> getSensorsetList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SensorTime> getSensorTimeList(Integer uniqueSensorsetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Day> getDayList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DayHasActivity> getDayHasActivityList(Integer incrementalDay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SecondHasSensorset> getSecondHasSensorset(Integer incrementalDay) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
