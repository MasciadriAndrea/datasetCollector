package dataModel;

public class Location {
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
	public Integer getUniqueLocationId() {
		return uniqueLocationId;
	}
	public void setUniqueLocationId(Integer uniqueLocationId) {
		this.uniqueLocationId = uniqueLocationId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((uniqueLocationId == null) ? 0 : uniqueLocationId.hashCode());
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
		Location other = (Location) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (uniqueLocationId == null) {
			if (other.uniqueLocationId != null)
				return false;
		} else if (!uniqueLocationId.equals(other.uniqueLocationId))
			return false;
		return true;
	}
}
