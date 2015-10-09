package dataModel;

public class Location {
	public Integer getUniqueLocationId() {
		return uniqueLocationId;
	}
	public void setUniqueLocationId(Integer uniqueLocationId) {
		this.uniqueLocationId = uniqueLocationId;
	}
	private Integer id;
	private Integer uniqueLocationId;
	private String name;
	
	public Location(Integer id, Integer uli, String name) {
		super();
		this.id = id;
		this.name = name;
		this.uniqueLocationId = uli;
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
	
	
}
