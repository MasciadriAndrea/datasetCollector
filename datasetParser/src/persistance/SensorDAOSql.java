package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Location;
import dataModel.HSensor;
import dataModel.SensorType;

public class SensorDAOSql implements SensorDAO {
	private static SensorDAOSql instance;
	
	private SensorDAOSql(){
		super();
	}
	
	public static SensorDAOSql getInstance(){
		if(instance==null){
			instance=new SensorDAOSql();
		}
		return instance;
	}
	@Override
	public List<HSensor> getSensorByHouse(Integer id) throws SQLException{
		List<HSensor> st=new ArrayList<HSensor>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,name,x,y,Location_id,SensorType_id,uniqueSensorId FROM Sensor WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		Integer ids=0;
		Integer idLoc=0;
		Integer idType=0;
		String names= "";
		String xs= "";
		String ys= "";
		Integer usi=0;
		while (rs.next()) {
			names = rs.getString("name");
			xs = rs.getString("x");
			ys = rs.getString("y");
			ids = Integer.parseInt(rs.getString("id"));
			usi = Integer.parseInt(rs.getString("uniqueSensorId"));
			idLoc = Integer.parseInt(rs.getString("Location_id"));
			idType = Integer.parseInt(rs.getString("SensorType_id"));
			
			LocationDAOSql locationDao=LocationDAOSql.getInstance();
			Location loc=locationDao.getLocationById(idLoc);
			SensorTypeDAOSql stDao=SensorTypeDAOSql.getInstance();
			SensorType stype=stDao.getSensorTypeById(idType);
			st.add(new HSensor(ids, usi, names, xs, ys, stype,loc));
		}
		return st;
	}
	
	@Override
	public HSensor getSensorById(Integer sid) throws SQLException{
		HSensor s=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,name,x,y,Location_id,SensorType_id,uniqueSensorId FROM Sensor WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, sid);
		ResultSet rs = preparedStatement.executeQuery();
		String names= "";
		String xs= "";
		String ys= "";
		Integer usi=0;
		while (rs.next()) {
			names = rs.getString("name");
			xs = rs.getString("x");
			ys = rs.getString("y");
			
			Integer idLoc = Integer.parseInt(rs.getString("Location_id"));
			usi = Integer.parseInt(rs.getString("uniqueSensorId"));
			Integer idType = Integer.parseInt(rs.getString("SensorType_id"));
			
			LocationDAOSql locationDao=LocationDAOSql.getInstance();
			Location loc=locationDao.getLocationById(idLoc);
			SensorTypeDAOSql stDao=SensorTypeDAOSql.getInstance();
			SensorType stype=stDao.getSensorTypeById(idType);
			s=new HSensor(sid,usi, names, xs, ys, stype, loc);
		}
		return s;
	}
	
	private Integer insertSensor(String name,String x,String y,Integer type,Integer location,Integer idHouse,Integer usi) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Sensor (name,x,y,House_id,SensorType_id,Location_id,uniqueSensorId) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, x);
		preparedStatement.setString(3, y);
		preparedStatement.setInt(4, idHouse);
		preparedStatement.setInt(5, type);
		preparedStatement.setInt(6, location);
		preparedStatement.setInt(7, usi);
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
	public HSensor updateSensor(HSensor s, Integer idHouse) throws SQLException{
		Integer idS=s.getId();
		
		// check if exist -> if the exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Sensor WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idS);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			deleteSensor(idS);
		}
		
		//insert in the database
		Integer newIdSensor=0;
		newIdSensor= insertSensor(s.getName(),s.getX(),s.getY(),s.getType().getId(),s.getLocation().getId(),idHouse,s.getUniqueSensorId());
				
		//upload the id
		s.setId(newIdSensor);
		return s;
	}
	
	@Override
	public void deleteSensor(Integer id){
		//delete actual
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "DELETE FROM Sensor WHERE id = ?";
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
