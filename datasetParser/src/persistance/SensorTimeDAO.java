package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.SensorTime;

public interface SensorTimeDAO {
	public List<SensorTime> getSensorTimeBySensorsetId(Integer id) throws SQLException;
	public SensorTime getSensorTimeById(Integer id) throws SQLException;
	public SensorTime updateSensorTime(SensorTime st,Integer idSS) throws SQLException;
	public void deleteSensorTime(Integer id);
}
