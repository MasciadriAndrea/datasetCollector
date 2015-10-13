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
		List<Integer> residentsId = new ArrayList<Integer>(); residentsId.add(1); residentsId.add(2);	
		List<Resident> residents = new ArrayList<Resident>();
		for (Integer id: residentsId){
			residents.add(this.house.getResidentByUniqueId(id));
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
	}

	private void setDay(){
		List<Resident> residents = parameters.getResidents();

		List<ActivityGP> activities = new ArrayList<ActivityGP>();
		activities.add(parameters.getActivities().get(1));

		List<HSensorset> uniqueSensorsets = new ArrayList<HSensorset>();

		List<Day> houseDays = house.getDays();
		
		for (ActivityGP activity:activities){
			for(Day day : houseDays){

				//		set activity labels according to the configurations

				System.out.println("set activity labels according to the configurations "+ day.getIncrementalDay());
				
				List<DayHasActivity> dayActivities =new ArrayList<DayHasActivity>();			
				List<DayHasActivity> houseDaySubactivities = day.getDailyActivities();

				for (DayHasActivity hds : houseDaySubactivities){
					for (Resident resident: residents){
						if (hds.getResident().getUniqueResidentId() == resident.getUniqueResidentId()){

							for (Activity subactivity: activity.getSubactivities()){
								if (hds.getActivity().getUniqueActivityId() == subactivity.getUniqueActivityId()){
									System.out.println(hds.getActivity().getName() + " is found for " + activity.getName());
									Integer startSec = hds.getStartSec();
									Integer endSec = hds.getEndSec();
									DayHasActivity da = new DayHasActivity(0, startSec, endSec, activity, resident);
									dayActivities.add(da);
								}
							}
						}
					}
				}

				//		change secondIdSS for every day
				String secondSSId = "";
				for( String houseSSidString : day.getSecondIdSS().split(",")){
					Integer houseSSid = Integer.valueOf(houseSSidString);
					List<HSensor> presentSensors = this.house.getActivatedSensorsByUniqueSSid(houseSSid);
					List<HSensor> filteredSensors = new ArrayList<HSensor>();


					// find which allowed sensors are present
					for (HSensor allowedSensor : activity.getAllowedSensors()){
						for(HSensor presentSensor:presentSensors){
							if (presentSensor == allowedSensor){
								filteredSensors.add(presentSensor);
							}
						}
					}
					//					create activated sensors list
					List<SensorTime> sensorsTime = new ArrayList<SensorTime>();
					for (HSensor sensor:parameters.getSensors()){
						SensorTime st = new SensorTime(0, sensor, "");
						for (HSensor fsensor:filteredSensors){
							if (sensor == fsensor){
								st.setValue("1");
							} else {
								st.setValue("0");
							}
						}
						sensorsTime.add(st);
					}				

					HSensorset ss = getSensorsetByActivatedSensors(sensorsTime);
					if (ss == null){
						ss = new HSensorset(0, parameters.getSensorsets().size(),  sensorsTime);
					}

				secondSSId = secondSSId+"," + Integer.toString(ss.getUniqueSensorsetId());
				}

				//					SensorTime st = new SensorTime();
				//							HSensorset newSS = new HSensorset(houseSSid, houseSSid, null);

			}


		}
	}

	public HSensorset getSensorsetByActivatedSensors(List<SensorTime> currentSensorTime){
		List<HSensor> currentActivatedSensors = getActivatedSensorsBySensorTimeList(currentSensorTime);
		for (HSensorset ss: parameters.getSensorsets()){
			List<HSensor> activatedSensors = getActivatedSensorsBySensorTimeList(ss.getSensors());
			Integer score = 0;
			for (HSensor currentActivatedSensor:currentActivatedSensors){
				for (HSensor activatedSensor: activatedSensors){
					if (currentActivatedSensor == activatedSensor){
						score++;
					}
				}
				if (score.equals(currentActivatedSensors.size())){
					return ss;
				}
			}

		}
		return null;

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

}



