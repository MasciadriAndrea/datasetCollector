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

public class ActivityDAOSql implements ActivityDAO {
	@Override
	public List<Activity> getActivityByHouse(Integer id) throws SQLException{
		List<Activity> st=new ArrayList<Activity>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id, name, uniqueActivityId FROM Activity WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String namest=""; 
		Integer idst = 0;
		Integer uid = 0;
		while (rs.next()) {
			namest = rs.getString("name");
			idst = Integer.parseInt(rs.getString("id"));
			uid = Integer.parseInt(rs.getString("uniqueActivityId"));
			st.add(new Activity(idst,uid,namest));
		}
		return st;
	}
	
	@Override
	public Activity getActivityById(Integer aid) throws SQLException{
		Activity a=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT name,uniqueActivityId FROM Activity WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, aid);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String namest="";
		Integer uid = 0;
		while (rs.next()) {
			namest = rs.getString("name");
			uid = Integer.parseInt(rs.getString("uniqueActivityId"));
			a= new Activity(aid,uid,namest);
		}
		return a;
	}
	
	private Integer insertActivity(String name,Integer idHouse,Integer uid) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Activity (name,House_id,uniqueActivityId) VALUES (?,?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, name);
		preparedStatement.setInt(2, idHouse);
		preparedStatement.setInt(3, uid);
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
	public Activity updateActivity(Activity att,Integer idHouse) throws SQLException{
		Integer idA=att.getId();
		
		// check if exist -> if exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Activity WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idA);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		while (rs.next()) {
			deleteActivity(idA);
		}
		
		//insert in the database
		Integer newIdActivity=0;
		newIdActivity= insertActivity(att.getName(),idHouse,att.getUniqueActivityId());
				
		//upload the id
		att.setId(newIdActivity);
		return att;
	}
	
	@Override
	public void deleteActivity(Integer id){
		//delete actual
				Connection dbConnection=DbManager.getInstance().getConnection();
				String selectSQL = "DELETE FROM Activity WHERE id = ?";
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
