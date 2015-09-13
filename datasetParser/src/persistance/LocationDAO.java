package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.Location;

public interface LocationDAO {
	public List<Location> getLocationByHouse(Integer id) throws SQLException;
	public void updateLocation(Location st);
	public void deleteLocation(Integer id);
}
