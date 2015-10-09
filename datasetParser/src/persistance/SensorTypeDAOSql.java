package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.SensorType;

public class SensorTypeDAOSql implements SensorTypeDAO {
	@Override
	public List<SensorType> getSensorTypeByHouse(Integer id) throws SQLException{
		List<SensorType> st=new ArrayList<SensorType>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,name,uniqueSensorTypeId FROM SensorType WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String namest=""; 
		Integer idst = 0;
		Integer usi=0;
		while (rs.next()) {
			namest = rs.getString("name");
			idst = Integer.parseInt(rs.getString("id"));
			usi = Integer.parseInt(rs.getString("uniqueSensorTypeId"));
			st.add(new SensorType(idst,usi,namest));
		}
		return st;
	}
	
	@Override
	public SensorType getSensorTypeById(Integer id) throws SQLException{
		SensorType st=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT name,uniqueSensorTypeId FROM SensorType WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String namest=""; 
		Integer usi=0;
		while (rs.next()) {
			namest = rs.getString("name");
			usi = Integer.parseInt(rs.getString("uniqueSensorTypeId"));
			st=new SensorType(id,usi,namest);
		}
		return st;
	}
	
	private Integer insertSensorType(String name,Integer idHouse,Integer usi) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO SensorType (name,House_id,uniqueSensorTypeId) VALUES (?,?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, name);
		preparedStatement.setInt(2, idHouse);
		preparedStatement.setInt(3, usi);
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
	public SensorType updateSensorType(SensorType st,Integer idHouse) throws SQLException{
		Integer idSt=st.getId();
		
		// check if exist -> if exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM SensorType WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idSt);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		while (rs.next()) {
			deleteSensorType(idSt);
		}
		
		//insert in the database
		Integer newIdActivity=0;
		newIdActivity= insertSensorType(st.getName(),idHouse,st.getUniqueSensorTypeId());
				
		//upload the id
		st.setId(newIdActivity);
		return st;
	}
	
	@Override
	public void deleteSensorType(Integer id){
		//delete actual
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "DELETE FROM SensorType WHERE id = ?";
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
