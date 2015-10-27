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
		this.id = id;
		this.name = name;
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
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public Activity getActivityByUniqueId(Integer id){
		for(Activity e:this.activities){
			if(e.getUniqueActivityId().equals(id)){
				return e;
			}		
		}
		return null;
	}
	public Resident getResidentByUniqueId(Integer id){
		for(Resident e:this.residents){
			if(e.getUniqueResidentId().equals(id)){
				return e;
			}		
		}
		return null;
	}
	public Location getLocationByUniqueId(Integer id){
		for(Location e:this.locations){
			if(e.getUniqueLocationId().equals(id)){
				return e;
			}		
		}
		return null;
	}
	public HSensor getSensorByUniqueId(Integer id){
		for(HSensor e:this.sensors){
			if(e.getUniqueSensorId().equals(id)){
				return e;
			}		
		}
		return null;
	}
	public HSensorset getSensorsetByUniqueId(Integer id){
		for(HSensorset e:this.sensorsets){
			if(e.getUniqueSensorsetId().equals(id)){
				return e;
			}		
		}
		return null;
	}

	
	public List<HSensor> getActivatedSensorsByDaySecond(Day day, Integer second){
		List<HSensor> activatedSensors = new ArrayList<HSensor>();
		Integer sensorsetId = day.getSSidBySecond(second);
		for(SensorTime sensorTime:this.getSensorsetByUniqueId(sensorsetId).getSensors()){
			if (sensorTime.getValue().equals("1")){
				activatedSensors.add(sensorTime.getSensor());
			}
		}
		return activatedSensors;
	}
	
	public List<HSensor> getActivatedSensorsByUniqueSSid(Integer sensorsetId){
		List<HSensor> activatedSensors = new ArrayList<HSensor>();
		for(SensorTime sensorTime:this.getSensorsetByUniqueId(sensorsetId).getSensors()){
			if (sensorTime.getValue().equals("1")){
				activatedSensors.add(sensorTime.getSensor());
			}
		}
		return activatedSensors;
	}
	
	
	public SensorType getSensorTypeByUniqueId(Integer id){
		for(SensorType e:this.sensorTypes){
			if(e.getUniqueSensorTypeId().equals(id)){
				return e;
			}		
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activities == null) ? 0 : activities.hashCode());
		result = prime * result + ((days == null) ? 0 : days.hashCode());
		result = prime * result
				+ ((locations == null) ? 0 : locations.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((residents == null) ? 0 : residents.hashCode());
		result = prime * result
				+ ((sensorTypes == null) ? 0 : sensorTypes.hashCode());
		result = prime * result + ((sensors == null) ? 0 : sensors.hashCode());
		result = prime * result
				+ ((sensorsets == null) ? 0 : sensorsets.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		House other = (House) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (days == null) {
			if (other.days != null)
				return false;
		} else if (!days.equals(other.days))
			return false;
		if (locations == null) {
			if (other.locations != null)
				return false;
		} else if (!locations.equals(other.locations))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (residents == null) {
			if (other.residents != null)
				return false;
		} else if (!residents.equals(other.residents))
			return false;
		if (sensorTypes == null) {
			if (other.sensorTypes != null)
				return false;
		} else if (!sensorTypes.equals(other.sensorTypes))
			return false;
		if (sensors == null) {
			if (other.sensors != null)
				return false;
		} else if (!sensors.equals(other.sensors))
			return false;
		if (sensorsets == null) {
			if (other.sensorsets != null)
				return false;
		} else if (!sensorsets.equals(other.sensorsets))
			return false;
		return true;
	}
	
	public Location getLocationByName(String name){
		int p=0;
		for(Location l:this.locations){
			if(l.getName().equals(name)){
				return l;
			}
			p++;
		}
		p++;
		Location newp=new Location(0,p,name);
		this.locations.add(newp);
		return newp;
	}
	
	public Integer getIncrementalDayIdByDate(String day,String month,String year){
		for(Day d:this.days){
			if((d.getDay().equals(day))&&(d.getMonth().equals(month))&&(d.getYear().equals(year))){
				return d.getIncrementalDay();
			}
		}
		return 0;//0 means day not found
	}
	
	public Day getDayByIncremental(Integer v){
		for(Day d:this.days){
			if(d.getIncrementalDay().equals(v)){
				return d;
			}
		}
		return null;
	}
}
