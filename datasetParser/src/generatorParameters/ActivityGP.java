package generatorParameters;

import java.util.ArrayList;
import java.util.List;

import dataModel.Activity;

public class ActivityGP extends Activity {

	private List<Activity> subactivitiesID;
	private List<ActivityPerformance> patterns;
	private List<ActivityPerformance> performances;
	
	public ActivityGP(Integer id, Integer uid, String name, List<Activity> subactivitiesID) {
		super(id, uid, name);
		this.subactivitiesID = subactivitiesID;
		this.patterns = new ArrayList<ActivityPerformance>();
		this.performances = new ArrayList<ActivityPerformance>();
	}

	public List<Activity> getSubactivitiesID() {
		return subactivitiesID;
	}

	public void setSubactivitiesID(List<Activity> subactivitiesID) {
		this.subactivitiesID = subactivitiesID;
	}

	public List<ActivityPerformance> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<ActivityPerformance> patterns) {
		this.patterns = patterns;
	}
	

}
