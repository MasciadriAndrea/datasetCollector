package dataModel;

import java.util.ArrayList;
import java.util.List;

public class Dataset {

	
	private Integer id;
	private String name;
	private List<House> houses;
	
	
	public Dataset(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.houses = new ArrayList<House>();
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
	public List<House> getHouses() {
		return houses;
	}
	public void setHouses(List<House> houses) {
		this.houses = houses;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((houses == null) ? 0 : houses.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Dataset other = (Dataset) obj;
		if (houses == null) {
			if (other.houses != null)
				return false;
		} else if (!houses.equals(other.houses))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
