package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.SensorType;

public interface SensorTypeDAO {
	public List<SensorType> getSensorTypeByHouse(Integer id) throws SQLException;
	public void updateSensorType(SensorType st);
	public void deleteSensorType(Integer id);
}
