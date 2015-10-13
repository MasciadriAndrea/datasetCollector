package generatorParameters;

import java.util.ArrayList;
import java.util.List;

import dataModel.Activity;
import dataModel.HSensor;

public class ActivityGP extends Activity {

	private List<Activity> subactivities;
	private List<ActivityPerformance> patterns;
	private List<ActivityPerformance> performances;
	private List<HSensor> allowedSensors;
	
	public ActivityGP(Integer id, Integer uid, String name, List<Activity> subactivities, List<HSensor> allowedSensors) {
		super(id, uid, name);
		this.subactivities = subactivities;
		this.allowedSensors = allowedSensors;
		this.patterns = new ArrayList<ActivityPerformance>();
		this.performances = new ArrayList<ActivityPerformance>();
		
	}

	public List<ActivityPerformance> getPerformances() {
		return performances;
	}

	public void setPerformances(List<ActivityPerformance> performances) {
		this.performances = performances;
	}

	public List<HSensor> getAllowedSensors() {
		return allowedSensors;
	}

	public void setAllowedSensors(List<HSensor> allowedSensors) {
		this.allowedSensors = allowedSensors;
	}

	public List<Activity> getSubactivities() {
		return subactivities;
	}

	public void setSubactivitiesID(List<Activity> subactivitiesID) {
		this.subactivities = subactivitiesID;
	}

	public List<ActivityPerformance> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<ActivityPerformance> patterns) {
		this.patterns = patterns;
	}
	

}
