package dataModel;

import java.util.ArrayList;
import java.util.List;

public class HSensorset {
	private Integer id;
	private Integer uniqueSensorsetId;
	private List<SensorTime> sensors;
	private List<Integer> durations;
	private float[] timeDistr;
	
	public HSensorset(Integer id, Integer uniqueId, List<SensorTime> sensors) {
		super();
		this.id = id;
		this.sensors = sensors;
		this.uniqueSensorsetId=uniqueId;
		this.durations=new ArrayList<Integer>();
		this.timeDistr=null;
	}
	
	public HSensorset(){
		super();
		this.id=0;
		this.uniqueSensorsetId=0;
		List<SensorTime> lst=new ArrayList<SensorTime>();
		this.sensors=new ArrayList<SensorTime>();
		this.durations=new ArrayList<Integer>();
		this.timeDistr=null;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<SensorTime> getSensors() {
		return sensors;
	}
	public void setSensors(List<SensorTime> sensors) {
		this.sensors = sensors;
	}
	public Integer getUniqueSensorsetId() {
		return uniqueSensorsetId;
	}
	public void setUniqueSensorsetId(Integer uniqueSensorsetId) {
		this.uniqueSensorsetId = uniqueSensorsetId;
	}
	
	public List<Integer> getActivatedSensorsId(){
		List<Integer> activeSensors=new ArrayList<Integer>();
		for(SensorTime st:this.getSensors()){
			if(st.getValue().equals("1")){
				activeSensors.add(st.getSensor().getUniqueSensorId());
			}
		}
		return activeSensors;
	}
	
	public String getStringSS(){
		Integer prevSensId=0;
		String line="";
		for(SensorTime st:this.sensors){
			if(prevSensId>st.getSensor().getUniqueSensorId()){
				System.out.println("Error in sensor order!");
				return null;
			}
			line+=st.getValue()+",";
			prevSensId=st.getSensor().getUniqueSensorId();
		}
		line=line.substring(0,line.length()-1);
		return line;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sensors == null) ? 0 : sensors.hashCode());
		result = prime
				* result
				+ ((uniqueSensorsetId == null) ? 0 : uniqueSensorsetId
						.hashCode());
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
		HSensorset other = (HSensorset) obj;
		if (sensors == null) {
			if (other.sensors != null)
				return false;
		} else if (!sensors.equals(other.sensors))
			return false;
		if (uniqueSensorsetId == null) {
			if (other.uniqueSensorsetId != null)
				return false;
		} else if (!uniqueSensorsetId.equals(other.uniqueSensorsetId))
			return false;
		return true;
	}
	
	public List<Integer> getDurations() {
		return durations;
	}

	public void setDurations(List<Integer> durations) {
		this.durations = durations;
	}
	
	public void addDuration(Integer num){
		this.durations.add(num);
	}
	public float[] getTimeDistr() {
		return timeDistr;
	}

	public void setTimeDistr(float[] timeDistr) {
		this.timeDistr = timeDistr;
	}

}
