package dataModel;

public class Activity {
	public Integer getUniqueActivityId() {
		return uniqueActivityId;
	}
	public void setUniqueActivityId(Integer uniqueActivityId) {
		this.uniqueActivityId = uniqueActivityId;
	}
	private Integer id;
	private Integer uniqueActivityId;
	private String name;
	
	public Activity(Integer id, Integer uid, String name) {
		super();
		this.id = id;
		this.uniqueActivityId = uid;
		this.name = name;
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
