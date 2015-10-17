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
		List<DayGP> dayGP=new ArrayList<DayGP>();
		
		for(Resident res:this.parameters.getResidents()){
			//for each resident import its days
			for(Day realDay:houseDays){
				System.out.println("Uploading day "+realDay.getIncrementalDay()+" resident "+res.getUniqueResidentId());
				//I should create a dayGP for this resident
				DayGP daygp=new DayGP(0,realDay.getIncrementalDay(),realDay.getDay(),realDay.getMonth(),realDay.getYear(),realDay.getSecondIdSS(),res);
				List<DayHasActivity> dhaRes=new ArrayList<DayHasActivity>();
				String[] ids=daygp.getSSid();
				Integer[] idsInt=new Integer[86400];
				for(DayHasActivity dha:realDay.getDailyActivities()){
					if(dha.getResident().getUniqueResidentId()==res.getUniqueResidentId()){
						
						Integer performedSubActivityId=dha.getActivity().getUniqueActivityId();
						ActivityGP agp=this.getParameters().getActivityGpBySubActId(performedSubActivityId);
						if(agp==null){
							agp=this.parameters.getActivityGpByIdGP(0);
						}
						DayHasActivity dhaNew= new DayHasActivity(0,dha.getStartSec(),dha.getEndSec(),agp,dha.getResident());
						dhaRes.add(dhaNew);			
						Integer startSec=dha.getStartSec();
						Integer endSec=dha.getEndSec();
						Integer okSec=0;
						Integer koSec=0;
						for(Integer sec=startSec;sec<=endSec;sec++){
							//filtering data
							if(agp.getUniqueActivityId()!=0){
								Integer previousSSId=Integer.parseInt(ids[sec-1]);
								HSensorset previousSS=house.getSensorsetByUniqueId(previousSSId);
								List<Integer> filteredSIds=previousSS.getActivatedSensorsId();
								filteredSIds.retainAll(agp.getAllowedSensorsId());
								Integer newSSId=this.manageSS(filteredSIds);
								idsInt[sec-1]=newSSId;
								okSec++;
								//System.out.println("Changing ss "+previousSSId+" in "+newSSId+" for day "+daygp.getIncrementalDay());
							}else{
								idsInt[sec-1]=0;koSec++;
							}
						}
						
						
						//TODO do this not by activity but by day!!!
						System.out.println("Total seconds parsed: "+(okSec+koSec)+" -> not accepted seconds: "+koSec);
					}
				}
				
				//hereeeee
				
				
				daygp.setSSid(idsInt);
				daygp.setDailyActivities(dhaRes);
				dayGP.add(daygp);
			}
		}
		this.parameters.setDays(dayGP);
				
	}
	
	public Integer manageSS(List<Integer> incomingList){
		Integer numberOfSS=0;
		for(HSensorset ss:this.parameters.getSensorsets()){
			if(ss.getUniqueSensorsetId()!=0){
				numberOfSS++;
				Integer matchingSensor=0;
				List<Integer> actSensorss=ss.getActivatedSensorsId();
				for(Integer ids:actSensorss){
					if(incomingList.contains(ids)){
						matchingSensor++;
					}
				}
				if((matchingSensor==actSensorss.size())&&(actSensorss.size()==incomingList.size())){
					//is the same ss
					return ss.getUniqueSensorsetId();
				}
			}
		}
		//otherwise create SS
		List<SensorTime> lst=new ArrayList<SensorTime>();
		for(Integer sens=1;sens<=this.house.getSensors().size();sens++){
			HSensor sensor=this.house.getSensorByUniqueId(sens);
			String value="0";
			if(incomingList.contains(sens)){
				value="1";
			}
        	SensorTime st=new SensorTime(0,sensor,value);
        	lst.add(st);
		}
		HSensorset newss=new HSensorset(0,numberOfSS+1,lst);
		this.parameters.getSensorsets().add(newss);
		return numberOfSS+1;
	}

}



