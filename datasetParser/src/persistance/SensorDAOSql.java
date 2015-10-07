package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Location;
import dataModel.Sensor;
import dataModel.SensorType;

public class SensorDAOSql implements SensorDAO {
	@Override
	public List<Sensor> getSensorByHouse(Integer id) throws SQLException{
		List<Sensor> st=new ArrayList<Sensor>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,name,x,y,Location_id,SensorType_id FROM Sensor WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer ids=0;
		Integer idLoc=0;
		Integer idType=0;
		String names= "";
		String xs= "";
		String ys= "";
		while (rs.next()) {
			names = rs.getString("name");
			xs = rs.getString("x");
			ys = rs.getString("y");
			ids = Integer.parseInt(rs.getString("id"));
			idLoc = Integer.parseInt(rs.getString("Location_id"));
			idType = Integer.parseInt(rs.getString("SensorType_id"));
			
			LocationDAOSql locationDao=new LocationDAOSql();
			Location loc=locationDao.getLocationById(idLoc);
			SensorTypeDAOSql stDao=new SensorTypeDAOSql();
			SensorType stype=stDao.getSensorTypeById(idType);
			
			st.add(new Sensor(ids, names, xs, ys, stype,loc));
		}
		return st;
	}
	
	@Override
	public Sensor getSensorById(Integer sid) throws SQLException{
		Sensor s=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,name,x,y,Location_id,SensorType_id FROM Sensor WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, sid);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String names= "";
		String xs= "";
		String ys= "";
		while (rs.next()) {
			names = rs.getString("name");
			xs = rs.getString("x");
			ys = rs.getString("y");
			
			Integer idLoc = Integer.parseInt(rs.getString("Location_id"));
			Integer idType = Integer.parseInt(rs.getString("SensorType_id"));
			
			LocationDAOSql locationDao=new LocationDAOSql();
			Location loc=locationDao.getLocationById(idLoc);
			SensorTypeDAOSql stDao=new SensorTypeDAOSql();
			SensorType stype=stDao.getSensorTypeById(idType);
			s=new Sensor(sid, names, xs, ys, stype, loc);
		}
		return s;
	}
	
	@Override
	public void updateSensor(Sensor st){
		//TODO
	}
	
	@Override
	public void deleteSensor(Integer id){
		//TODO
	}
}
