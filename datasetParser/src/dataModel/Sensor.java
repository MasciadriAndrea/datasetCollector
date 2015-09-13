package dataModel;

public class Sensor {
	private Integer id;
	private String name;
	private String x;
	private String y;
	
	public Sensor(Integer id, String name, String x, String y) {
		super();
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
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
	
	
}
