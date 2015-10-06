package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.SensorTime;
import dataModel.Sensorset;

public class SensorsetDAOSql implements SensorsetDAO {
	@Override
	public List<Sensorset> getSensorsetByDay(Integer id) throws SQLException{
		List<Sensorset> st=new ArrayList<Sensorset>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,second FROM Sensorset WHERE Day_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer ids=0; 
		Integer seconds= 0;
		while (rs.next()) {
			seconds= Integer.parseInt(rs.getString("second"));
			ids = Integer.parseInt(rs.getString("id"));
			
			//retrieve all the sensortime
			 SensorTimeDAOSql sensorTimeDao=new SensorTimeDAOSql();
			 List<SensorTime> s=sensorTimeDao.getSensorTimeBySensorsetId(ids);
			
			st.add(new Sensorset(ids, s, seconds));
		}
		return st;
	}
	
	@Override
	public void updateSensorset(Sensorset st){
		//TODO
	}
	
	@Override
	public void deleteSensorset(Integer id){
		
		
		 
	}
}
