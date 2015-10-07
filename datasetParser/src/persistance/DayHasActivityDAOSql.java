package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Activity;
import dataModel.DayHasActivity;

public class DayHasActivityDAOSql implements DayHasActivityDAO {
	@Override
	public List<DayHasActivity> getDayHasActivityByDay(Integer id) throws SQLException{
		List<DayHasActivity> st=new ArrayList<DayHasActivity>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,startSec,endSec,Activity_id FROM Day_has_Activity WHERE Day_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer idd=0; 
		Integer startd= 0;
		Integer endd= 0;
		Integer aid=0;
		while (rs.next()) {
			startd = Integer.parseInt(rs.getString("startSec"));
			endd = Integer.parseInt(rs.getString("endSec"));
			idd = Integer.parseInt(rs.getString("id"));
			aid = Integer.parseInt(rs.getString("Activity_id"));
			
			//retrieve the associated activity	
			 ActivityDAOSql activityDao=new ActivityDAOSql();
			 Activity a=activityDao.getActivityById(aid);
			
			st.add(new DayHasActivity(idd, startd, endd, a));
		}
		return st;
	}
	
	@Override
	public void updateDayHasActivity(DayHasActivity st){
		//TODO
	}
	
	@Override
	public void deleteDayHasActivity(Integer id){
		//TODO
	}
}
