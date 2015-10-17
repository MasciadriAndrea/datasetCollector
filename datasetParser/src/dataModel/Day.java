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
	private String secondIdSS;
	
	public Day(Integer id, Integer incrementalDay, String day, String month, String year,String idSSs) {
		super();
		this.id = id;
		this.incrementalDay=incrementalDay;
		this.day = day;
		this.month = month;
		this.year = year;
		this.dailyActivities = new ArrayList<DayHasActivity>();
		this.secondIdSS= idSSs;
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
	
	public String[] getSSid(){
		return secondIdSS.split(",");
	}
	
	public void setSSid(Integer[] ids){
		String str="";
		for(Integer i=0;i<ids.length;i++){
			str+=String.valueOf(ids[i])+",";
		}
		str=str.substring(0,str.length()-1);
		this.secondIdSS=str;
	}

	public Integer getIncrementalDay() {
		return incrementalDay;
	}
	public void setIncrementalDay(Integer incrementalDay) {
		this.incrementalDay = incrementalDay;
	}
	
	public Integer getSSidBySecond(Integer sec){
		String[] chunks=secondIdSS.split(",");
		if(chunks.length!=86400){
			System.out.println("Not correct number of seconds");
		}
		if(sec<=chunks.length){
			return Integer.valueOf(chunks[sec-1]);
		}else{
			return 0;
			//Throw exception
		}
	}
	
	public String getSecondIdSS() {
		return secondIdSS;
	}
	public void setSecondIdSS(String secondIdSS) {
		this.secondIdSS = secondIdSS;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dailyActivities == null) ? 0 : dailyActivities.hashCode());
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result
				+ ((incrementalDay == null) ? 0 : incrementalDay.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result
				+ ((secondIdSS == null) ? 0 : secondIdSS.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		Day other = (Day) obj;
		if (dailyActivities == null) {
			if (other.dailyActivities != null)
				return false;
		} else if (!dailyActivities.equals(other.dailyActivities))
			return false;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		if (incrementalDay == null) {
			if (other.incrementalDay != null)
				return false;
		} else if (!incrementalDay.equals(other.incrementalDay))
			return false;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (secondIdSS == null) {
			if (other.secondIdSS != null)
				return false;
		} else if (!secondIdSS.equals(other.secondIdSS))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}
	
	public void setSSidBySecond(Integer sec, Integer value) {
		String[] chunks=secondIdSS.split(",");
		if(chunks.length!=86400){
			System.out.println("Not correct number of seconds");
		}
		if(sec<=chunks.length){
			chunks[sec-1]=String.valueOf(value);
		}
		String str="";
		for(Integer i=0;i<chunks.length;i++){
			str+=chunks[i]+",";
		}
		str=str.substring(0,str.length()-1);
		this.secondIdSS=str;
	}
}
