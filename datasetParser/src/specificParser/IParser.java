package specificParser;


import java.util.List;

import dataModel.Activity;
import dataModel.Dataset;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.HSensor;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Location;
import dataModel.Resident;
import dataModel.SecondHasSensorset;
import dataModel.SensorTime;
import dataModel.SensorType;

public interface IParser {
	public Dataset getDataset();
	public void createDataset(Integer id, String name);
	public void createHouse(String name);
	
	public List<Activity> getActivityList();
	public List<Resident> getResidentList();
	public List<Location> getLocationList();
	public List<HSensor> getSensorList();
	public List<SensorType> getSensorTypeList();
	public List<HSensorset> getSensorsetList();
	public List<SensorTime> getSensorTimeList(Integer uniqueSensorsetId);
	public List<Day> getDayList();
	public List<DayHasActivity> getDayHasActivityList(Integer incrementalDay); 
	public List<SecondHasSensorset> getSecondHasSensorset(Integer incrementalDay);
}
