package dataModel;

import java.util.List;

public class Sensorset {
	private Integer id;
	private List<SensorTime> sensors;
	private Integer second;
	
	public Sensorset(Integer id, List<SensorTime> sensors, Integer second) {
		super();
		this.id = id;
		this.sensors = sensors;
		this.second = second;
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
	public Integer getSecond() {
		return second;
	}
	public void setSecond(Integer second) {
		this.second = second;
	}
	
	
}
