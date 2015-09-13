package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.SensorTime;

public interface SensorTimeDAO {
	public List<SensorTime> getSensorTimeBySensorsetId(Integer id) throws SQLException;
	public void updateSensorTime(SensorTime st);
	public void deleteSensorTime(Integer id);
}
