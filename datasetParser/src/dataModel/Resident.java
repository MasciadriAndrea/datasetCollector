package dataModel;

public class Resident {
	private Integer id;
	private Integer age;
	
	public Resident(Integer id, Integer age) {
		super();
		this.id = id;
		this.age = age;
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
	
	
}
