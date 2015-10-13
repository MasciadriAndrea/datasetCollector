package dataModel;

public class HSensor {
	private Integer id;
	private Integer uniqueSensorId;
	private String name;
	private String x;
	private String y;
	private SensorType type;//add this everywhere
	private Location location;//add this everywhere
	
	public HSensor(Integer id, Integer uniqueSensorId, String name, String x, String y, SensorType type, Location location) {
		super();
		this.id = id;
		this.uniqueSensorId = uniqueSensorId;
		this.name = name;
		this.x = x;
		this.y = y;
		this.type=type;
		this.location=location;
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
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}

	public SensorType getType() {
		return type;
	}

	public void setType(SensorType type) {
		this.type = type;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	public Integer getUniqueSensorId() {
		return uniqueSensorId;
	}

	public void setUniqueSensorId(Integer uniqueSensorId) {
		this.uniqueSensorId = uniqueSensorId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((uniqueSensorId == null) ? 0 : uniqueSensorId.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
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
		HSensor other = (HSensor) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (uniqueSensorId == null) {
			if (other.uniqueSensorId != null)
				return false;
		} else if (!uniqueSensorId.equals(other.uniqueSensorId))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}

}
