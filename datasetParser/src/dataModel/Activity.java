package dataModel;

public class Activity {
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
	public Integer getUniqueActivityId() {
		return uniqueActivityId;
	}
	public void setUniqueActivityId(Integer uniqueActivityId) {
		this.uniqueActivityId = uniqueActivityId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((uniqueActivityId == null) ? 0 : uniqueActivityId.hashCode());
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
		Activity other = (Activity) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (uniqueActivityId == null) {
			if (other.uniqueActivityId != null)
				return false;
		} else if (!uniqueActivityId.equals(other.uniqueActivityId))
			return false;
		return true;
	}
}
