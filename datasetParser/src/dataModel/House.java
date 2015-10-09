package dataModel;

import java.util.ArrayList;
import java.util.List;

public class House {
	private Integer id;
	private String name;
	private List<Resident> residents;
	private List<Location> locations;
	private List<HSensor> sensors;
	private List<Day> days;
	private List<SensorType> sensorTypes;
	private List<Activity> activities;
	private List<HSensorset> sensorsets;
	
	public House(Integer id, String name) {
		super();
		id = id;
		name = name;
		this.residents = new ArrayList<Resident>();
		this.locations = new ArrayList<Location>();
		this.sensors = new ArrayList<HSensor>();
		this.days = new ArrayList<Day>();
		this.sensorTypes = new ArrayList<SensorType>();
		this.activities = new ArrayList<Activity>();
		this.sensorsets=new ArrayList<HSensorset>();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		name = name;
	}
	public List<Resident> getResidents() {
		return residents;
	}
	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	public List<HSensor> getSensors() {
		return sensors;
	}
	public void setSensors(List<HSensor> sensors) {
		this.sensors = sensors;
	}
	public List<Day> getDays() {
		return days;
	}
	public void setDays(List<Day> days) {
		this.days = days;
	}
	public List<SensorType> getSensorTypes() {
		return sensorTypes;
	}
	public void setSensorTypes(List<SensorType> sensorTypes) {
		this.sensorTypes = sensorTypes;
	}
	public List<Activity> getActivities() {
		return activities;
	}
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	public void addSensorType(SensorType st){
		sensorTypes.add(st);
	}
	public void addActivities(Activity a){
		activities.add(a);
	}
	public void addResidents(Resident r){
		residents.add(r);
	}
	public void addSensors(HSensor s){
		sensors.add(s);
	}
	public void addLocations(Location l){
		locations.add(l);
	}
	public void addDays(Day d){
		days.add(d);
	}
	public List<HSensorset> getSensorsets() {
		return sensorsets;
	}
	public void setSensorsets(List<HSensorset> sensorsets) {
		this.sensorsets = sensorsets;
	}
	
	
}
