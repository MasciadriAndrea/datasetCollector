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
	private List<ActivityGP> activities;
	private List<HSensorset> sensorsets;
	
	public Parameters(){
		this.residents = new ArrayList<Resident>();
		this.sensors = new ArrayList<HSensor>();
		this.activities = new ArrayList<ActivityGP>();
		this.sensorsets = new ArrayList<HSensorset>();
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
	
}
