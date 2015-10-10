package common;

import java.util.ArrayList;
import java.util.List;

import specificParser.ArasParser;
import dataModel.Activity;
import dataModel.Dataset;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.House;
import dataModel.Location;
import dataModel.Resident;
import dataModel.SecondHasSensorset;
import dataModel.HSensor;
import dataModel.SensorTime;
import dataModel.SensorType;
import dataModel.HSensorset;

public class DSParser {

	public static void main(String[] args) {
		
		//instantiate simple
		/*Resident resident=new Resident(0,1,23);
		ArrayList<Resident> listResidents=new ArrayList<Resident>();
		listResidents.add(resident);
		
		Activity activity1=new Activity(0,1,"Sleep");
		Activity activity2=new Activity(0,2,"Eat");
		ArrayList<Activity> listActivities=new ArrayList<Activity>();
		listActivities.add(activity1);
		listActivities.add(activity2);
		
		Location location=new Location(0,1,"Kitchen");
		ArrayList<Location> listLocations=new ArrayList<Location>();
		listLocations.add(location);
		
		SensorType sensType=new SensorType(0,1,"Temperature");
		ArrayList<SensorType> listSensorTypes=new ArrayList<SensorType>();
		listSensorTypes.add(sensType);
		
		HSensor sens1=new HSensor(0,1,"s1","100","200",sensType,location);
		HSensor sens2=new HSensor(0,2,"s2","50","50",sensType,location);
		ArrayList<HSensor> listSensors=new ArrayList<HSensor>();
		listSensors.add(sens1);
		listSensors.add(sens2);
		
		DayHasActivity dha1=new DayHasActivity(0,1,50000,activity1);
		DayHasActivity dha2=new DayHasActivity(0,50001,86400,activity2);
		ArrayList<DayHasActivity> listDha=new ArrayList<DayHasActivity>();
		listDha.add(dha1);
		listDha.add(dha2);
		
		SensorTime s1ss1=new SensorTime(0,sens1,"0");
		SensorTime s2ss1=new SensorTime(0,sens2,"1");
		SensorTime s1ss2=new SensorTime(0,sens1,"1");
		SensorTime s2ss2=new SensorTime(0,sens2,"0");
		List<SensorTime> lst1=new ArrayList<SensorTime>();
		lst1.add(s1ss1);
		lst1.add(s2ss1);
		List<SensorTime> lst2=new ArrayList<SensorTime>();
		lst2.add(s1ss2);
		lst2.add(s2ss2);
		
		HSensorset ss1= new HSensorset(0,1,lst1);
		HSensorset ss2= new HSensorset(0,2,lst2);
		ArrayList<HSensorset> listSS=new ArrayList<HSensorset>();
		listSS.add(ss1);
		listSS.add(ss2);
		
		SecondHasSensorset shs1=new SecondHasSensorset(0,1,ss1);
		SecondHasSensorset shs2=new SecondHasSensorset(0,2,ss2);
		ArrayList<SecondHasSensorset> listShS=new ArrayList<SecondHasSensorset>();
		listShS.add(shs1);
		listShS.add(shs2);
		
		Day day1=new Day(0,0,"1","1","2015");
		day1.setDailyActivities(listDha);
		day1.setSecondHasSensorsets(listShS);
		ArrayList<Day> listDays=new ArrayList<Day>();
		listDays.add(day1);
		
		House house1=new House(0,"House A");
		house1.setResidents(listResidents);
		house1.setLocations(listLocations);
		house1.setActivities(listActivities);
		house1.setSensorTypes(listSensorTypes);
		house1.setSensors(listSensors);
		house1.setDays(listDays);
		house1.setSensorsets(listSS);
		ArrayList<House> listHouses=new ArrayList<House>();
		listHouses.add(house1);
		
		Dataset dataset1=new Dataset(0,"FooDataset");
		dataset1.setHouses(listHouses);*/
		
		
		ArasParser ap=ArasParser.getInstance();
		ap.updateHouseData("ArasDS", "House1");
		ap.saveDataset();
	}

}
