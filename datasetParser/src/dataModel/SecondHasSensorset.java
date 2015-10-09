package dataModel;

public class SecondHasSensorset {
	private Integer id;
	private Integer second;
	private HSensorset sensorset;
	
	public SecondHasSensorset(Integer id, Integer second, HSensorset sensorset) {
		super();
		this.id = id;
		this.second = second;
		this.sensorset = sensorset;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSecond() {
		return second;
	}
	public void setSecond(Integer second) {
		this.second = second;
	}
	public HSensorset getSensorset() {
		return sensorset;
	}
	public void setSensorset(HSensorset sensorset) {
		this.sensorset = sensorset;
	}
	
	
}
