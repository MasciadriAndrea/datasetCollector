package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Sensor;
import dataModel.SensorTime;

public class SensorTimeDAOSql implements SensorTimeDAO {
	@Override
	public List<SensorTime> getSensorTimeBySensorsetId(Integer id) throws SQLException{
		List<SensorTime> st=new ArrayList<SensorTime>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,Sensor_id,status FROM Sensorset_has_Sensor WHERE Sensorset_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer ids = 0; 
		Integer idst = 0;
		String status="";
		while (rs.next()) {
			idst = Integer.parseInt(rs.getString("id"));
			ids = Integer.parseInt(rs.getString("Sensor_id"));
			status= rs.getString("status");
			
			//get sensor
			 SensorDAOSql sensorDao=new SensorDAOSql();
			 Sensor s=sensorDao.getSensorById(ids);
			 
			st.add(new SensorTime(idst,s,status));
		}
		return st;
	}
	
	@Override
	public void updateSensorTime(SensorTime st){
		//TODO
	}
	
	@Override
	public void deleteSensorTime(Integer id){
		//TODO
	}
}
