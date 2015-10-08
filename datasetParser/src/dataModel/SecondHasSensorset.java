package dataModel;

public class SecondHasSensorset {
	private Integer id;
	private Integer second;
	private Sensorset sensorset;
	
	public SecondHasSensorset(Integer id, Integer second, Sensorset sensorset) {
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
	public Sensorset getSensorset() {
		return sensorset;
	}
	public void setSensorset(Sensorset sensorset) {
		this.sensorset = sensorset;
	}
	
	
}
