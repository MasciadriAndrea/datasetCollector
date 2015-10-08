package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.Sensorset;

public interface SensorsetDAO {
	public List<Sensorset> getSensorsetByHouse(Integer id) throws SQLException;
	public Sensorset getSensorsetById(Integer id) throws SQLException;
	public Sensorset updateSensorset(Sensorset st,Integer idHouse) throws SQLException;
	public void deleteSensorset(Integer id);
}
