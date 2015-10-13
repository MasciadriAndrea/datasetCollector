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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((uniqueSensorTypeId == null) ? 0 : uniqueSensorTypeId
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
		SensorType other = (SensorType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (uniqueSensorTypeId == null) {
			if (other.uniqueSensorTypeId != null)
				return false;
		} else if (!uniqueSensorTypeId.equals(other.uniqueSensorTypeId))
			return false;
		return true;
	}
}
