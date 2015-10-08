package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.House;

public interface HouseDAO {
	public List<House> getHouseByDS(Integer id) throws SQLException;
	public House getHouseById(Integer id) throws SQLException;
	public House updateHouse(House ds,Integer idDs) throws SQLException;
	public void deleteHouse(Integer id);
}
