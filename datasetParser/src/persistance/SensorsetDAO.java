package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.Sensorset;

public interface SensorsetDAO {
	public List<Sensorset> getSensorsetByDay(Integer id) throws SQLException;
	public void updateSensorset(Sensorset st);
	public void deleteSensorset(Integer id);
}
