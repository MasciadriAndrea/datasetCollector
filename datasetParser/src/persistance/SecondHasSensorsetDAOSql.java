package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Activity;
import dataModel.DayHasActivity;
import dataModel.SecondHasSensorset;
import dataModel.HSensorset;

public class SecondHasSensorsetDAOSql implements SecondHasSensorsetDAO {
	@Override
	public List<SecondHasSensorset> getSecondHasSensorsetByDay(Integer id) throws SQLException{
		List<SecondHasSensorset> st=new ArrayList<SecondHasSensorset>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,Sensorset_id,second FROM Second_has_Sensorset WHERE Day_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer idd=0; 
		Integer second= 0;
		Integer idss= 0;
		while (rs.next()) {
			second = Integer.parseInt(rs.getString("second"));
			idss = Integer.parseInt(rs.getString("Sensorset_id"));
			idd = Integer.parseInt(rs.getString("id"));
			
			//retrieve the associated activity	
			 SensorsetDAOSql ssDao=new SensorsetDAOSql();
			 HSensorset sa=ssDao.getSensorsetById(idss);
			
			st.add(new SecondHasSensorset(idd,second,sa));
		}
		return null;
	}
	
	@Override
	public SecondHasSensorset getSecondHasSensorsetById(Integer id) throws SQLException{
		SecondHasSensorset shs=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT Day_id,Sensorset_id,second FROM Second_has_Sensorset WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer idd=0; 
		Integer second= 0;
		Integer idss= 0;
		while (rs.next()) {
			second = Integer.parseInt(rs.getString("second"));
			idss = Integer.parseInt(rs.getString("Sensorset_id"));
			idd = Integer.parseInt(rs.getString("Day_id"));
			
			
			//retrieve the associated activity	
			 SensorsetDAOSql ssDao=new SensorsetDAOSql();
			 HSensorset sa=ssDao.getSensorsetById(idss);
			
			 shs=new SecondHasSensorset(id,second,sa);
		}
		return shs;
	}
	
	public Integer insertSecondHasSensorset(Integer idDay,Integer idSS,Integer second) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Second_has_Sensorset (Day_id,Sensorset_id,second) VALUES (?,?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setInt(1, idDay);
		preparedStatement.setInt(2, idSS);
		preparedStatement.setInt(3, second);
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
	public SecondHasSensorset updateSecondHasSensorset(SecondHasSensorset shs,Integer idDay) throws SQLException{
		Integer idShs=shs.getId();
		
		// check if exist -> if exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Second_has_Sensorset WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idShs);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		while (rs.next()) {
			deleteSecondHasSensorset(idShs);
		}
		
		//insert in the database
		Integer newIdShs=0;
		newIdShs= insertSecondHasSensorset(idDay,shs.getSensorset().getId(),shs.getSecond());
				
		//upload the id
		shs.setId(newIdShs);
		
		return shs;
	}
	
	@Override
	public void deleteSecondHasSensorset(Integer id){
				//delete actual
				Connection dbConnection=DbManager.getInstance().getConnection();
				String selectSQL = "DELETE FROM Day_has_Activity WHERE id = ?";
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
