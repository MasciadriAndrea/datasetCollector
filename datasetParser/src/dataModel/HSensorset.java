package dataModel;

import java.util.List;

public class HSensorset {
	private Integer id;
	private List<SensorTime> sensors;
	
	public HSensorset(Integer id, List<SensorTime> sensors) {
		super();
		this.id = id;
		this.sensors = sensors;
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
	
	
}
