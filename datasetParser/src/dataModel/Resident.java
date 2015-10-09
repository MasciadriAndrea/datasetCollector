package dataModel;

public class Resident {
	private Integer id;
	private Integer uniqueResidentId;
	private Integer age;
	
	public Resident(Integer id, Integer age,Integer uri) {
		super();
		this.id = id;
		this.age = age;
		this.uniqueResidentId=uri;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getUniqueResidentId() {
		return uniqueResidentId;
	}
	public void setUniqueResidentId(Integer uniqueResidentId) {
		this.uniqueResidentId = uniqueResidentId;
	}
}
