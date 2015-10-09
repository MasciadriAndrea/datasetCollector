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
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.Location;
import dataModel.Resident;
import dataModel.SecondHasSensorset;
import dataModel.HSensor;
import dataModel.SensorType;
import dataModel.HSensorset;

public class DayDAOSql implements DayDAO {
	@Override
	public List<Day> getDayByHouse(Integer id) throws SQLException{
		List<Day> st=new ArrayList<Day>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,day,month,year,incrementalDay FROM Day WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer idd=0; 
		String dayd= "";
		String monthd= "";
		String yeard= "";
		Integer incDay=0;
		while (rs.next()) {
			dayd = rs.getString("day");
			monthd = rs.getString("month");
			yeard = rs.getString("year");
			idd = Integer.parseInt(rs.getString("id"));
			incDay = Integer.parseInt(rs.getString("incrementalDay"));
			Day d=new Day(idd,incDay,dayd,monthd,yeard);
			
			//retrieve all dailyActivities	
			 DayHasActivityDAOSql dayHasActivityDAO=new DayHasActivityDAOSql();
			 List<DayHasActivity> da=dayHasActivityDAO.getDayHasActivityByDay(idd);
			 d.setDailyActivities(da);
			
			//retrieve all secondHasSensorset
			 SecondHasSensorsetDAOSql secondHasSensorsetDAO=new SecondHasSensorsetDAOSql();
			 List<SecondHasSensorset> ss=secondHasSensorsetDAO.getSecondHasSensorsetByDay(idd);
			 d.setSecondHasSensorsets(ss);
			
			st.add(d);
		}
		return st;
	}
	
	public Day getDayById(Integer id) throws SQLException{
		Day d=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT day,month,year,incrementalDay FROM Day WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer idd=0; 
		String dayd= "";
		String monthd= "";
		String yeard= "";
		Integer incDay=0;
		while (rs.next()) {
			dayd = rs.getString("day");
			monthd = rs.getString("month");
			yeard = rs.getString("year");
			incDay=rs.getInt("incrementalDay");
			d=new Day(id,incDay,dayd,monthd,yeard);
			
			//retrieve all dailyActivities	
			 DayHasActivityDAOSql dayHasActivityDAO=new DayHasActivityDAOSql();
			 List<DayHasActivity> da=dayHasActivityDAO.getDayHasActivityByDay(idd);
			 d.setDailyActivities(da);
			 
			//retrieve all secondHasSensorset
			 SecondHasSensorsetDAOSql secondHasSensorsetDAO=new SecondHasSensorsetDAOSql();
			 List<SecondHasSensorset> ss=secondHasSensorsetDAO.getSecondHasSensorsetByDay(idd);
			 d.setSecondHasSensorsets(ss);
			
		}
		return d;
	}
	
	public Integer insertDay(Integer incDay,String day,String month,String year, Integer idHouse) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Day (day,month,year,House_id,incrementalDay) VALUES (?,?,?,?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, day);
		preparedStatement.setString(2, month);
		preparedStatement.setString(3, year);
		preparedStatement.setInt(4, idHouse);
		preparedStatement.setInt(5, incDay);
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
	public Day updateDay(Day d,Integer idHouse) throws SQLException{
		Integer idD=d.getId();
		
		// check if exist -> if exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Day WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idD);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		while (rs.next()) {
			deleteDay(idD);
		}
		
		//insert in the database
		Integer newIdDay=0;
		newIdDay= insertDay(d.getIncrementalDay(),d.getDay(),d.getMonth(),d.getYear(), idHouse);
				
		//upload the id
		d.setId(newIdDay);
		
		//insert contained Objects
		
				//retrieve all the DailyActivities
				 DayHasActivityDAOSql dhaDao=new DayHasActivityDAOSql();
				 for(DayHasActivity dha: d.getDailyActivities()){
					dha=dhaDao.updateDayHasActivity(dha,newIdDay);
				 }
				 
				//retrieve all secondHasSensorset
				 SecondHasSensorsetDAOSql secondHasSensorsetDAO=new SecondHasSensorsetDAOSql();
				 for(SecondHasSensorset ss: d.getSecondHasSensorsets()){
						ss=secondHasSensorsetDAO.updateSecondHasSensorset(ss,newIdDay);
					 }
		return d;
	}
	
	@Override
	public void deleteDay(Integer id){
		//delete everything contained
		try{  
			  	//retrieve all dailyActivities	
				 DayHasActivityDAOSql dayHasActivityDAO=new DayHasActivityDAOSql();
				 List<DayHasActivity> das=dayHasActivityDAO.getDayHasActivityByDay(id);
				 for(DayHasActivity da: das){
					 dayHasActivityDAO.deleteDayHasActivity(da.getId());
				}
				 
					//retrieve all secondHasSensorset
				 SecondHasSensorsetDAOSql secondHasSensorsetDAO=new SecondHasSensorsetDAOSql();
				 List<SecondHasSensorset> sechss=secondHasSensorsetDAO.getSecondHasSensorsetByDay(id);
				 for(SecondHasSensorset ssa: sechss){
					 secondHasSensorsetDAO.deleteSecondHasSensorset(ssa.getId());
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
