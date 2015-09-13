package dataModel;

import java.util.ArrayList;
import java.util.List;

public class Day {
	private Integer id;
	private String day;
	private String month;
	private String year;
	private List<DayHasActivity> dailyActivities;
	private List<Sensorset> sensorsets;
	
	public Day(Integer id, String day, String month, String year) {
		super();
		this.id = id;
		this.day = day;
		this.month = month;
		this.year = year;
		this.dailyActivities = new ArrayList<DayHasActivity>();
		this.sensorsets = new ArrayList<Sensorset>();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public List<DayHasActivity> getDailyActivities() {
		return dailyActivities;
	}
	public void setDailyActivities(List<DayHasActivity> dailyActivities) {
		this.dailyActivities = dailyActivities;
	}
	public List<Sensorset> getSensorsets() {
		return sensorsets;
	}
	public void setSensorsets(List<Sensorset> sensorsets) {
		this.sensorsets = sensorsets;
	}
	
}
