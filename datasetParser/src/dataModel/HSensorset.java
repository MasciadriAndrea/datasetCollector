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
}
