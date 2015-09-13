package dataModel;

import java.util.ArrayList;
import java.util.List;

public class House {
	private Integer Id;
	private String Name;
	private List<Resident> residents;
	private List<Location> locations;
	private List<Sensor> sensors;
	private List<Day> days;
	private List<SensorType> sensorTypes;
	private List<Activity> activities;
	
	public House(Integer id, String name) {
		super();
		Id = id;
		Name = name;
		this.residents = new ArrayList<Resident>();
		this.locations = new ArrayList<Location>();
		this.sensors = new ArrayList<Sensor>();
		this.days = new ArrayList<Day>();
		this.sensorTypes = new ArrayList<SensorType>();
		this.activities = new ArrayList<Activity>();
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
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
	public List<Sensor> getSensors() {
		return sensors;
	}
	public void setSensors(List<Sensor> sensors) {
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
	public void addSensors(Sensor s){
		sensors.add(s);
	}
	public void addLocations(Location l){
		locations.add(l);
	}
	public void addDays(Day d){
		days.add(d);
	}
}