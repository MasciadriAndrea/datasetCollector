package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.Sensor;

public interface SensorDAO {
	public List<Sensor> getSensorByHouse(Integer id) throws SQLException;
	public Sensor getSensorById(Integer sid) throws SQLException;
	public Sensor updateSensor(Sensor st, Integer houseId) throws SQLException;
	public void deleteSensor(Integer id);
}
