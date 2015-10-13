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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime
				* result
				+ ((uniqueResidentId == null) ? 0 : uniqueResidentId.hashCode());
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
		Resident other = (Resident) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (uniqueResidentId == null) {
			if (other.uniqueResidentId != null)
				return false;
		} else if (!uniqueResidentId.equals(other.uniqueResidentId))
			return false;
		return true;
	}
}
