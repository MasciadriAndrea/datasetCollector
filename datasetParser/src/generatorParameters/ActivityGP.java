package generatorParameters;

import java.util.ArrayList;
import java.util.List;

import dataModel.Activity;
import dataModel.HSensor;

public class ActivityGP extends Activity {
	public Integer getDhaInActivity() {
		return dhaInActivity;
	}

	public void setDhaInActivity(Integer dhaInActivity) {
		this.dhaInActivity = dhaInActivity;
	}

	private List<Activity> subactivities;
	private List<HSensor> allowedSensors;
	private List<Pattern> patterns;
	private Integer dhaInActivity;
	
	public ActivityGP(Integer id, Integer uid, String name, List<Activity> subactivities, List<HSensor> allowedSensors) {
		super(id, uid, name);
		this.subactivities = subactivities;
		this.allowedSensors = allowedSensors;
		this.patterns=new ArrayList<Pattern>();
		this.dhaInActivity=0;
	}
	
	public List<Integer> getAllowedSensorsId() {
		List<Integer> ids=new ArrayList<Integer>();
		for(HSensor s:this.allowedSensors){
			ids.add(s.getUniqueSensorId());
		}
		return ids;
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
	
	
	public List<Pattern> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<Pattern> patterns) {
		this.patterns = patterns;
	}
	
}
