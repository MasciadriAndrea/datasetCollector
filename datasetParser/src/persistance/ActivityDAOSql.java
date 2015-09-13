package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Activity;

public class ActivityDAOSql implements ActivityDAO {
	@Override
	public List<Activity> getActivityByHouse(Integer id) throws SQLException{
		List<Activity> st=new ArrayList<Activity>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT Activity.name as name, Activity.id as id FROM Activity,House_has_Activity WHERE House_has_Activity.idActivity=Activity.id and House_has_Activity.idHouse = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String namest=""; 
		Integer idst = 0;
		while (rs.next()) {
			namest = rs.getString("name");
			idst = Integer.parseInt(rs.getString("id"));
			st.add(new Activity(idst,namest));
		}
		return st;
	}
	
	@Override
	public Activity getActivityById(Integer aid) throws SQLException{
		Activity a=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT name FROM Activity WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, aid);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String namest=""; 
		while (rs.next()) {
			namest = rs.getString("name");
			a= new Activity(aid,namest);
		}
		return a;
	}
	
	@Override
	public void updateActivity(Activity st){
		//TODO
	}
	
	@Override
	public void deleteActivity(Integer id){
		//TODO
	}
}
