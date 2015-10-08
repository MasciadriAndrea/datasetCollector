package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.Location;

public interface LocationDAO {
	public List<Location> getLocationByHouse(Integer id) throws SQLException;
	public Location getLocationById(Integer id) throws SQLException;
	public Location updateLocation(Location st,Integer idHouse) throws SQLException;
	public void deleteLocation(Integer id);
}
