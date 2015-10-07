package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	@Override
	public void updateSensorType(SensorType st){
		//TODO
	}
	
	@Override
	public void deleteSensorType(Integer id){
		//TODO
	}
}
