package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.House;

public interface HouseDAO {
	public List<House> getHouseByDS(Integer id) throws SQLException;
	public House getHouseById(Integer id);
	public House getHouseByName(String name);
	public void updateHouse(House ds);
	public void deleteHouse(Integer id);
}
