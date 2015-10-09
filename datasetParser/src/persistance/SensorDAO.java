package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.HSensor;

public interface SensorDAO {
	public List<HSensor> getSensorByHouse(Integer id) throws SQLException;
	public HSensor getSensorById(Integer sid) throws SQLException;
	public HSensor updateSensor(HSensor st, Integer houseId) throws SQLException;
	public void deleteSensor(Integer id);
}
