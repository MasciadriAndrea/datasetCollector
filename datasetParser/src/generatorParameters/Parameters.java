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
	private List<Resident> residents;
	private List<HSensor> sensors;
	private List<ActivityGP> activities;
	private List<HSensorset> sensorsets;
	private List<DayGP> days;
	private int[][] overallTransitionSS;
	private float[][] overallProbSS;
	
	public Parameters(){
		this.residents = new ArrayList<Resident>();
		this.sensors = new ArrayList<HSensor>();
		this.activities = new ArrayList<ActivityGP>();
		this.sensorsets = new ArrayList<HSensorset>();
		HSensorset h1=new HSensorset();
		sensorsets.add(h1);
		this.days=new ArrayList<DayGP>();
		overallTransitionSS=null;
		overallProbSS=null;
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
		List<ActivityGP> agpList=new ArrayList<ActivityGP>();
		for(ActivityGP agp:this.getActivities()){
			for(Activity subact:agp.getSubactivities()){
				if(subact.getUniqueActivityId()==id){
					return agp;
				}
			}
		}
		return null;
	}
	
	public int[][] getOverallTransitionSS() {
		return overallTransitionSS;
	}

	public void setOverallTransitionSS(int[][] overallTransitionSS) {
		this.overallTransitionSS = overallTransitionSS;
	}

	public List<DayGP> getDays() {
		return days;
	}

	public void setDays(List<DayGP> days) {
		this.days = days;
	}
	
	public float[][] getOverallProbSS() {
		return overallProbSS;
	}

	public void setOverallProbSS(float[][] overallProbSS) {
		this.overallProbSS = overallProbSS;
	}

	public void addResident(Resident newResident){
		this.residents.add(newResident);
	}
	public void addActivity(ActivityGP newActivity){
		this.getActivities().add(newActivity);
	}
	public HSensorset getSensorsetByUniqueId(Integer id){
		for(HSensorset ss:this.sensorsets){
			if(ss.getUniqueSensorsetId().equals(id)){
				return ss;
			}
		}return null;

	}
}
