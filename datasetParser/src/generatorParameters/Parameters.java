package generatorParameters;

import java.util.ArrayList;
import java.util.List;

import specificParser.ArasParser;
import dataModel.HSensor;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Resident;
import dataModel.SensorType;

public class Parameters {
	
	private List<Resident> residents;
	private List<HSensor> sensors;
	private List<DayActivity> activityDays;
	private List<SensorType> sensorTypes;
	private List<ActivityGP> activities;
	private List<HSensorsetGP> sensorsets;
	
	public Parameters(){
		this.residents = new ArrayList<Resident>();
		this.sensors = new ArrayList<HSensor>();
		this.activityDays = new ArrayList<DayActivity>();
		this.sensorTypes = new ArrayList<SensorType>();
		this.activities = new ArrayList<ActivityGP>();
		this.sensorsets = new ArrayList<HSensorsetGP>();
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

	public List<DayActivity> getActivityDays() {
		return activityDays;
	}

	public void setActivityDays(List<DayActivity> activityDays) {
		this.activityDays = activityDays;
	}

	public List<SensorType> getSensorTypes() {
		return sensorTypes;
	}

	public void setSensorTypes(List<SensorType> sensorTypes) {
		this.sensorTypes = sensorTypes;
	}

	public List<ActivityGP> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityGP> activities) {
		this.activities = activities;
	}

	public List<HSensorsetGP> getSensorsets() {
		return sensorsets;
	}

	public void setSensorsets(List<HSensorsetGP> sensorsets) {
		this.sensorsets = sensorsets;
	}
	
	public void addSensorset(HSensorsetGP ss){
		this.getSensorsets().add(ss);
	}
	
}
