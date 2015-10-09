package dataModel;

import java.util.ArrayList;
import java.util.List;

public class Day {
	private Integer id;
	private String day;
	private String month;
	private String year;
	private Integer incrementalDay;
	private List<DayHasActivity> dailyActivities;
	private List<SecondHasSensorset> secondHasSensorsets;
	
	public Day(Integer id, Integer incrementalDay, String day, String month, String year) {
		super();
		this.id = id;
		this.incrementalDay=incrementalDay;
		this.day = day;
		this.month = month;
		this.year = year;
		this.dailyActivities = new ArrayList<DayHasActivity>();
		this.secondHasSensorsets = new ArrayList<SecondHasSensorset>();
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
	public List<SecondHasSensorset> getSecondHasSensorsets() {
		return secondHasSensorsets;
	}
	public void setSecondHasSensorsets(List<SecondHasSensorset> secondHasSensorsets) {
		this.secondHasSensorsets = secondHasSensorsets;
	}
	public Integer getIncrementalDay() {
		return incrementalDay;
	}
	public void setIncrementalDay(Integer incrementalDay) {
		this.incrementalDay = incrementalDay;
	}
	
	
}
