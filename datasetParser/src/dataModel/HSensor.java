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
	
}
