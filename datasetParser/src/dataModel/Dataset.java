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

}
