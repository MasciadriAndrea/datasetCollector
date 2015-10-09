package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.HSensorset;

public interface SensorsetDAO {
	public List<HSensorset> getSensorsetByHouse(Integer id) throws SQLException;
	public HSensorset getSensorsetById(Integer id) throws SQLException;
	public HSensorset updateSensorset(HSensorset st,Integer idHouse) throws SQLException;
	public void deleteSensorset(Integer id);
}
