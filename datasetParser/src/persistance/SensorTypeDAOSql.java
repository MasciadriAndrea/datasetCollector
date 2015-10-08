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
		String selectSQL = "SELECT SensorType.name as name, SensorType.id as id FROM SensorType,House_has_SensorType WHERE House_has_SensorType.idSensorType=SensorType.id and House_has_SensorType.idHouse = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String namest=""; 
		Integer idst = 0;
		while (rs.next()) {
			namest = rs.getString("name");
			idst = Integer.parseInt(rs.getString("id"));
			st.add(new SensorType(idst,namest));
		}
		return st;
	}
	
	@Override
	public SensorType getSensorTypeById(Integer id) throws SQLException{
		SensorType st=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT name FROM SensorType WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String namest=""; 
		while (rs.next()) {
			namest = rs.getString("name");
			st=new SensorType(id,namest);
		}
		return st;
	}
	
	private Integer insertSensorType(String name,Integer idHouse) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO SensorType (name,House_id) VALUES (?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, name);
		preparedStatement.setInt(2, idHouse);
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
		newIdActivity= insertSensorType(st.getName(),idHouse);
				
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
