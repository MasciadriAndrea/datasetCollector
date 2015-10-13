package generatorParameters;

import java.util.ArrayList;
import java.util.List;

import dataModel.Day;

public class DayActivity {
	private Integer uniqueActivityId;
	private List<Day> days;
	
	public DayActivity(int uniqueActivityId){
		this.uniqueActivityId = uniqueActivityId;
		this.days = new ArrayList<Day>();
	}

	public Integer getUniqueActivityId() {
		return uniqueActivityId;
	}

	public List<Day> getDays() {
		return days;
	}

	public void setDays(List<Day> days) {
		this.days = days;
	}
	
	
}
