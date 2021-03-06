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
import dataModel.HSensorset;

public class SensorsetDAOSql implements SensorsetDAO {
	private static SensorsetDAOSql instance;
	
	private SensorsetDAOSql(){
		super();
	}
	
	public static SensorsetDAOSql getInstance(){
		if(instance==null){
			instance=new SensorsetDAOSql();
		}
		return instance;
	}
	
	@Override
	public List<HSensorset> getSensorsetByHouse(Integer id) throws SQLException{
		List<HSensorset> st=new ArrayList<HSensorset>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,uniqueSensorsetId FROM Sensorset WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		Integer ids=0;
		Integer uniqueId=0; 
		while (rs.next()) {
			ids = Integer.parseInt(rs.getString("id"));
			uniqueId=Integer.parseInt(rs.getString("uniqueSensorsetId"));
			//retrieve all the sensortime
			 SensorTimeDAOSql sensorTimeDao=SensorTimeDAOSql.getInstance();
			 List<SensorTime> s=sensorTimeDao.getSensorTimeBySensorsetId(ids);
			
			st.add(new HSensorset(ids,uniqueId, s));
		}
		return st;
	}
	
	@Override
	public HSensorset getSensorsetById(Integer id) throws SQLException{
		HSensorset ss=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Sensorset WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		Integer ids=0; 
		Integer uniqueId=0;
		while (rs.next()) {
			uniqueId=Integer.parseInt(rs.getString("uniqueSensorsetId"));
			
			//retrieve all the sensortime
			 SensorTimeDAOSql sensorTimeDao=SensorTimeDAOSql.getInstance();
			 List<SensorTime> s=sensorTimeDao.getSensorTimeBySensorsetId(ids);
			
			ss= new HSensorset(id,uniqueId, s);
		}
		return ss;
	}
	
	private Integer insertSensorset(Integer idHouse,Integer uniqueSsId) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Sensorset (House_id,uniqueSensorsetId) VALUES (?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setInt(1, idHouse);
		preparedStatement.setInt(2, uniqueSsId);
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
	public HSensorset updateSensorset(HSensorset ss,Integer idHouse) throws SQLException{
		Integer idSS=ss.getId();
		
		// check if SS exist -> if the SS exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Sensorset WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idSS);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			deleteSensorset(idSS);
		}
		
		//insert in the database
		Integer newIdSS=0;
		newIdSS= insertSensorset(idHouse,ss.getUniqueSensorsetId());
				
		//upload the id
		ss.setId(newIdSS);
		
		//insert contained Objects
		
				//retrieve all the SensorTime
				 SensorTimeDAOSql stDao=SensorTimeDAOSql.getInstance();
				 for(SensorTime sta: ss.getSensors()){
					sta=stDao.updateSensorTime(sta,newIdSS,idHouse);
				 }
				 
		return ss;
	}
	
	@Override
	public void deleteSensorset(Integer id){
		//delete everything contained
				try{  
						//retrieve all sensorTime
						 SensorTimeDAOSql sensortimeDAO=SensorTimeDAOSql.getInstance();
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
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	}
}
