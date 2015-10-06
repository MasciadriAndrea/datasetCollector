package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.Sensorset;

public class DayDAOSql implements DayDAO {
	@Override
	public List<Day> getDayByHouse(Integer id) throws SQLException{
		List<Day> st=new ArrayList<Day>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,day,month,year FROM Day WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer idd=0; 
		String dayd= "";
		String monthd= "";
		String yeard= "";
		while (rs.next()) {
			dayd = rs.getString("day");
			monthd = rs.getString("month");
			yeard = rs.getString("year");
			idd = Integer.parseInt(rs.getString("id"));
			Day d=new Day(idd,dayd,monthd,yeard);
			
			//retrieve all dailyActivities	
			 DayHasActivityDAOSql dayHasActivityDAO=new DayHasActivityDAOSql();
			 List<DayHasActivity> da=dayHasActivityDAO.getDayHasActivityByDay(idd);
			 d.setDailyActivities(da);
			
			//retrieve all sensorsets
			 SensorsetDAOSql sensorsetDAO=new SensorsetDAOSql();
			 List<Sensorset> ss=sensorsetDAO.getSensorsetByDay(idd);
			 d.setSensorsets(ss);
			
			st.add(d);
		}
		return st;
	}
	
	@Override
	public void updateDay(Day st){
		//TODO
	}
	
	@Override
	public void deleteDay(Integer id){
		//delete everything contained
		try{  
			  	//retrieve all dailyActivities	
				 DayHasActivityDAOSql dayHasActivityDAO=new DayHasActivityDAOSql();
				 List<DayHasActivity> das=dayHasActivityDAO.getDayHasActivityByDay(id);
				 for(DayHasActivity da: das){
					 //TODO
					 dayHasActivityDAO.deleteDayHasActivity(da.getId());
				}
				
				//retrieve all sensorsets
				 SensorsetDAOSql sensorsetDAO=new SensorsetDAOSql();
				 List<Sensorset> ss=sensorsetDAO.getSensorsetByDay(id);
				 for(Sensorset s: ss){
					 //TODO
					 sensorsetDAO.deleteSensorset(s.getId());
				}
				 
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		//delete actual
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "DELETE FROM Day WHERE id = ?";
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
