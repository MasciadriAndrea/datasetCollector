package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.DayHasActivity;
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
	public Sensorset getSensorsetById(Integer id) throws SQLException{
		Sensorset ss=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT second FROM Sensorset WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer ids=0; 
		Integer seconds= 0;
		while (rs.next()) {
			seconds= Integer.parseInt(rs.getString("second"));
			
			//retrieve all the sensortime
			 SensorTimeDAOSql sensorTimeDao=new SensorTimeDAOSql();
			 List<SensorTime> s=sensorTimeDao.getSensorTimeBySensorsetId(ids);
			
			ss= new Sensorset(id, s, seconds);
		}
		return ss;
	}
	
	private Integer insertSensorset(String sec,Integer idDay) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Sensorset (second,Day_id) VALUES (?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, sec);
		preparedStatement.setInt(2, idDay);
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
	public Sensorset updateSensorset(Sensorset ss,Integer idDay) throws SQLException{
		Integer idSS=ss.getId();
		
		// check if SS exist -> if the SS exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Sensorset WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idSS);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		while (rs.next()) {
			deleteSensorset(idSS);
		}
		
		//insert in the database
		Integer newIdSS=0;
		newIdSS= insertSensorset(ss.getSecond().toString(),idDay);
				
		//upload the id
		ss.setId(newIdSS);
		
		//insert contained Objects
		
				//retrieve all the SensorTime
				 SensorTimeDAOSql stDao=new SensorTimeDAOSql();
				 for(SensorTime sta: ss.getSensors()){
					sta=stDao.updateSensorTime(sta,newIdSS);
				 }
				 
		return ss;
	}
	
	@Override
	public void deleteSensorset(Integer id){
		//delete everything contained
				try{  
						//retrieve all sensorTime
						 SensorTimeDAOSql sensortimeDAO=new SensorTimeDAOSql();
						 List<SensorTime> st=sensortimeDAO.getSensorTimeBySensorsetId(id);
						 for(SensorTime si: st){
							 sensortimeDAO.deleteSensorTime(si.getId());
						}
						 
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
				//delete actual
				Connection dbConnection=DbManager.getInstance().getConnection();
				String selectSQL = "DELETE FROM Sensorset WHERE id = ?";
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
