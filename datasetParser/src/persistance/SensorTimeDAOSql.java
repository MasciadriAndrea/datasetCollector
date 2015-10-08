package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		String selectSQL = "SELECT id,Sensor_id,value FROM Sensorset_has_Sensor WHERE Sensorset_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer ids = 0; 
		Integer idst = 0;
		String status="";
		while (rs.next()) {
			idst = Integer.parseInt(rs.getString("id"));
			ids = Integer.parseInt(rs.getString("Sensor_id"));
			status= rs.getString("value");
			
			//get sensor
			 SensorDAOSql sensorDao=new SensorDAOSql();
			 Sensor s=sensorDao.getSensorById(ids);
			 
			st.add(new SensorTime(idst,s,status));
		}
		return st;
	}
	
	@Override
	public SensorTime getSensorTimeById(Integer id) throws SQLException{
		SensorTime st=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT Sensor_id,value FROM Sensorset_has_Sensor WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer ids = 0; 
		String status="";
		while (rs.next()) {
			ids = Integer.parseInt(rs.getString("Sensor_id"));
			status= rs.getString("value");
			
			//get sensor
			 SensorDAOSql sensorDao=new SensorDAOSql();
			 Sensor s=sensorDao.getSensorById(ids);
			 
			st=new SensorTime(id,s,status);
		}
		return st;
	}
	
	private Integer insertSensorTime(String value,Integer idSensor,Integer idSS) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Sensorset_has_Sensor (value,Sensorset_id,Sensor_id) VALUES (?,?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, value);
		preparedStatement.setInt(2, idSS);
		preparedStatement.setInt(3, idSensor);
		int affectedRows = preparedStatement.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return (int) generatedKeys.getLong(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
	}
	
	@Override
	public SensorTime updateSensorTime(SensorTime st,Integer idSS) throws SQLException{
		Integer idST=st.getId();
		
		// check if exist -> if exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Sensorset_has_Sensor WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idST);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		while (rs.next()) {
			deleteSensorTime(idST);
		}
		
		//insert in the database
		Integer newIdST=0;
		newIdST= insertSensorTime(st.getValue(),st.getSensor().getId(),idSS);
				
		//upload the id
		st.setId(newIdST);
				 
		return st;
	}
	
	@Override
	public void deleteSensorTime(Integer id){
			
		//delete actual
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "DELETE FROM Sensorset_has_Sensor WHERE id = ?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);
			preparedStatement.executeQuery(selectSQL );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
