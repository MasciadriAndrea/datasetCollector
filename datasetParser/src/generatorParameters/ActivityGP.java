package generatorParameters;

import java.util.ArrayList;
import java.util.List;

import dataModel.Activity;
import dataModel.HSensor;

public class ActivityGP extends Activity {

	private List<Activity> subactivities;
	private List<HSensor> allowedSensors;
	
	public ActivityGP(Integer id, Integer uid, String name, List<Activity> subactivities, List<HSensor> allowedSensors) {
		super(id, uid, name);
		this.subactivities = subactivities;
		this.allowedSensors = allowedSensors;
		
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

}
