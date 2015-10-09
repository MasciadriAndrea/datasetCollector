package dataModel;

public class SensorType {
	private Integer id;
	private String name;
	private Integer uniqueSensorTypeId;
	
	public SensorType(Integer id, Integer usi, String name) {
		super();
		this.id = id;
		this.name = name;
		this.uniqueSensorTypeId = usi;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getUniqueSensorTypeId() {
		return uniqueSensorTypeId;
	}
	public void setUniqueSensorTypeId(Integer uniqueSensorTypeId) {
		this.uniqueSensorTypeId = uniqueSensorTypeId;
	}
}
