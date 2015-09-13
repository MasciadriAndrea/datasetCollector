package dataModel;

public class SensorTime {
	private Integer id;
	private Sensor sensor;
	private String value;
	
	public SensorTime(Integer id, Sensor sensor, String value) {
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
	public Sensor getSensor() {
		return sensor;
	}
	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
