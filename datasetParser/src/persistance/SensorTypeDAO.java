package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.SensorType;

public interface SensorTypeDAO {
	public List<SensorType> getSensorTypeByHouse(Integer id) throws SQLException;
	public SensorType getSensorTypeById(Integer id) throws SQLException;
	public SensorType updateSensorType(SensorType st, Integer idHouse) throws SQLException;
	public void deleteSensorType(Integer id);
}
