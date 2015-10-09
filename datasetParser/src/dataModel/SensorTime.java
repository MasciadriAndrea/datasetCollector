package dataModel;

public class SensorTime {
	private Integer id;
	private HSensor sensor;
	private String value;
	
	public SensorTime(Integer id, HSensor sensor, String value) {
		super();
		this.id = id;
		this.sensor = sensor;
		this.value = value;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public HSensor getSensor() {
		return sensor;
	}
	public void setSensor(HSensor sensor) {
		this.sensor = sensor;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
