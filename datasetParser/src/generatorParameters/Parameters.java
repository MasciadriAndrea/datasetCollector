package generatorParameters;

import java.util.ArrayList;
import java.util.List;

import specificParser.ArasParser;
import dataModel.Activity;
import dataModel.HSensor;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Resident;
import dataModel.SensorType;

public class Parameters {
	
	public List<DayGP> getDays() {
		return days;
	}

	public void setDays(List<DayGP> days) {
		this.days = days;
	}

	private List<Resident> residents;
	private List<HSensor> sensors;
	private List<ActivityGP> activities;
	private List<HSensorset> sensorsets;
	private List<DayGP> days;
	
	public Parameters(){
		this.residents = new ArrayList<Resident>();
		this.sensors = new ArrayList<HSensor>();
		this.activities = new ArrayList<ActivityGP>();
		ActivityGP a=new ActivityGP(0,0,"DO NOT CONSIDER",new ArrayList<Activity>(),new ArrayList<HSensor>());
		activities.add(a);
		this.sensorsets = new ArrayList<HSensorset>();
		HSensorset h1=new HSensorset();
		sensorsets.add(h1);
		this.days=new ArrayList<DayGP>();
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public List<HSensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<HSensor> sensors) {
		this.sensors = sensors;
	}

	public List<ActivityGP> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityGP> activities) {
		this.activities = activities;
	}

	public List<HSensorset> getSensorsets() {
		return sensorsets;
	}

	public void setSensorsets(List<HSensorset> sensorsets) {
		this.sensorsets = sensorsets;
	}
	
	public void addSensorset(HSensorset ss){
		this.getSensorsets().add(ss);
	}
	
	public ActivityGP getActivityGpByIdGP(Integer id){
		for(ActivityGP agp:this.getActivities()){
			if(agp.getUniqueActivityId()==id){
				return agp;
			}
		}
		return null;
	}
	
	public ActivityGP getActivityGpBySubActId(Integer id){
		for(ActivityGP agp:this.getActivities()){
			for(Activity subact:agp.getSubactivities()){
				if(subact.getUniqueActivityId()==id){
					return agp;
				}
			}
		}
		//System.out.println("Error of null activity");
		return null;
	}
	
}
