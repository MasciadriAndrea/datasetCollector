package dataModel;

import java.util.List;

public class HSensorset {
	private Integer id;
	private Integer uniqueSensorsetId;
	private List<SensorTime> sensors;
	
	public HSensorset(Integer id, Integer uniqueId, List<SensorTime> sensors) {
		super();
		this.id = id;
		this.sensors = sensors;
		this.uniqueSensorsetId=uniqueId;
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
}
