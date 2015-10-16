package generatorParameters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import common.DbManager;
import dataModel.Activity;
import dataModel.Dataset;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.HSensor;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Resident;
import dataModel.SensorTime;
import persistance.DatasetDAOSql;
import specificParser.ArasParser;

public class ParametersHandler {
	private static String confFileName = "fileName";

	private static ParametersHandler instance;
	private Parameters parameters;
	private House house;


	private ParametersHandler(){
		super();
		this.parameters = new Parameters();
		this.house = null;
	}

	public static ParametersHandler getInstance(){
		if(instance==null){
			instance=new ParametersHandler();
		}
		return instance;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public void processChain(House house){
		this.house = house;
		this.setActivitiesWithConfigurations();
		this.setDay();
	}

	private void setActivitiesWithConfigurations(){

		//		TODO parser for configuration file
		// this.confFileName;	
		
//		 take use residents
		List<Integer> residentsId = new ArrayList<Integer>(); residentsId.add(1); residentsId.add(2);	
		List<Resident> residents = new ArrayList<Resident>();
		for (Integer id: residentsId){
			residents.add(this.house.getResidentByUniqueId(id));
		}
//		take used sensors
		List<Integer> sensorsId = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20); 	
		List<HSensor> sensorsAll = new ArrayList<HSensor>();
		for (Integer id: sensorsId){
			sensorsAll.add(this.house.getSensorByUniqueId(id));
		}

		List<String> activityNames = new ArrayList<String>();
		List<List<Integer>> subActInd = new ArrayList<List<Integer>>();
		List<List<Integer>> allowedSensors = new ArrayList<List<Integer>>();

		activityNames.add("Cook");
		subActInd.add(Arrays.asList(3, 5, 7));
		allowedSensors.add(Arrays.asList(8, 9, 15, 16, 19));

		activityNames.add("Eat");
		subActInd.add(Arrays.asList(4, 6, 8, 10));
		allowedSensors.add(Arrays.asList(4, 5, 6, 7, 8, 9, 15, 16));

		activityNames.add("Sleep");
		subActInd.add(Arrays.asList(11, 16));
		allowedSensors.add(Arrays.asList(2, 4, 5, 20));

		activityNames.add("WatchTV");
		subActInd.add(Arrays.asList(12));
		allowedSensors.add(Arrays.asList(3, 4, 5, 6, 7));

		activityNames.add("ReadBook");
		subActInd.add(Arrays.asList(18));
		allowedSensors.add(Arrays.asList(4, 5, 6, 7));

		activityNames.add("TakeShower");
		subActInd.add(Arrays.asList(14));
		allowedSensors.add(Arrays.asList(11, 13, 14, 17));

		activityNames.add("WashUp");
		subActInd.add(Arrays.asList(20, 21));
		allowedSensors.add(Arrays.asList(11, 13, 17));

		activityNames.add("Toilet");
		subActInd.add(Arrays.asList(15));
		allowedSensors.add(Arrays.asList(13, 14, 18));

		List<ActivityGP> activities = new ArrayList<ActivityGP>();	
		int uid = 0;
		for (int i = 0; i < activityNames.size(); i++){
			List<Activity> subactivities = new ArrayList<Activity>();
			for (Integer subactivityId: subActInd.get(i)){
				subactivities.add(this.house.getActivityByUniqueId(subactivityId));
			}
			List<HSensor> sensors = new ArrayList<HSensor>();
			for (Integer sensorId: allowedSensors.get(i)){
				sensors.add(this.house.getSensorByUniqueId(sensorId));
			}
			ActivityGP activity = new ActivityGP(0, uid, activityNames.get(i), subactivities, sensors);
			activities.add(activity);
			uid++;
		}
		this.parameters.setActivities(activities);
		this.parameters.setResidents(residents);
		this.parameters.setSensors(sensorsAll);
	}

	private void setDay(){
		List<Day> houseDays = house.getDays();

		for (ActivityGP activity:parameters.getActivities()){
			for(Day day : houseDays){

				//		set activity labels according to the configurations

				System.out.println("set activity labels according to the configurations "+ day.getIncrementalDay());

				List<DayHasActivity> dayActivities =new ArrayList<DayHasActivity>();			
				List<DayHasActivity> houseDaySubactivities = day.getDailyActivities();

				/*				for (DayHasActivity hds : houseDaySubactivities){

					if (isContainResident(hds.getResident())){

						for (Activity subactivity: activity.getSubactivities()){
							if (hds.getActivity().getUniqueActivityId() == subactivity.getUniqueActivityId()){
								System.out.println(hds.getActivity().getName() + " is found for " + activity.getName() + " with  " + hds.getResident().getUniqueResidentId());
								Integer startSec = hds.getStartSec();
								Integer endSec = hds.getEndSec();
								DayHasActivity da = new DayHasActivity(0, startSec, endSec, activity, hds.getResident());
								dayActivities.add(da);
							}
						}
					}		
				}
				 */
				//		change secondIdSS for every day
				String secondSSId = "";

				for( String houseSSidString : day.getSecondIdSS().split(",")){
					Integer houseSSid = Integer.valueOf(houseSSidString);
					List<HSensor> presentSensors = this.house.getActivatedSensorsByUniqueSSid(houseSSid);
					List<HSensor> filteredSensors = new ArrayList<HSensor>();

					// find which allowed sensors are present
					for(HSensor presentSensor:presentSensors){
						if (isSensorIsAllowedInActivity(activity, presentSensor)){
							filteredSensors.add(presentSensor);
						}
					}



				}

				//					SensorTime st = new SensorTime();
				//							HSensorset newSS = new HSensorset(houseSSid, houseSSid, null);

			}


		}
	}

	public List<HSensor> getActivatedSensorsBySensorTimeList(List<SensorTime> sensorsTime){
		List<HSensor> activatedSensors = new ArrayList<HSensor>();
		for(SensorTime sensorTime:sensorsTime){
			if (sensorTime.getValue().equals("1")){
				activatedSensors.add(sensorTime.getSensor());
			}
		}
		return activatedSensors;
	}

	public boolean isContainResident(Resident r){

		for (Resident resident: parameters.getResidents()){
			if (resident.getUniqueResidentId() == r.getUniqueResidentId()){
				return true;
			}
		}
		return false;
	}

	public boolean isSensorIsAllowedInActivity(ActivityGP activity, HSensor s){
		for (HSensor sensor : activity.getAllowedSensors()){
			if (sensor.getUniqueSensorId() == s.getUniqueSensorId()){
				return true;
			}
		}
		return false;
	}

	public List<SensorTime> sensorsTimeFromActivatedSensors(List<HSensor> activatedSensors){

		List<SensorTime> sensorsTime = new ArrayList<SensorTime>();
		
		for (HSensor sensor:parameters.getSensors()){
			SensorTime st = new SensorTime(0, sensor, "0");
			for (HSensor actSensor:activatedSensors){
				if (sensor.getUniqueSensorId() == actSensor.getUniqueSensorId()){
					st.setValue("1");
				} 
			}
			sensorsTime.add(st);
		}

		return sensorsTime;
	}

}



