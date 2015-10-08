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
	public DayHasActivity getDayHasActivityById(Integer id) throws SQLException{
		DayHasActivity dha=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT startSec,endSec,Activity_id FROM Day_has_Activity WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer startd= 0;
		Integer endd= 0;
		Integer aid=0;
		while (rs.next()) {
			startd = Integer.parseInt(rs.getString("startSec"));
			endd = Integer.parseInt(rs.getString("endSec"));
			aid = Integer.parseInt(rs.getString("Activity_id"));
			
			//retrieve the associated activity	
			 ActivityDAOSql activityDao=new ActivityDAOSql();
			 Activity a=activityDao.getActivityById(aid);
			 dha=new DayHasActivity(id, startd, endd, a);
		}
		return dha;
	}
	
	public Integer insertDayHasActivity(String startSec,String endSec,Integer idAct,Integer idDay) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Day_has_Activity (startSec,endSec,Day_id,Activity_id) VALUES (?,?,?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, startSec);
		preparedStatement.setString(2, endSec);
		preparedStatement.setInt(3, idDay);
		preparedStatement.setInt(2, idAct);
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
	public DayHasActivity updateDayHasActivity(DayHasActivity dha,Integer idDay) throws SQLException{
		Integer idDha=dha.getId();
		
		// check if exist -> if exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Day_has_Activity WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idDha);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		while (rs.next()) {
			deleteDayHasActivity(idDha);
		}
		
		//insert in the database
		Integer newIdDha=0;
		newIdDha= insertDayHasActivity(dha.getStartSec().toString(),dha.getEndSec().toString(),dha.getActivity().getId(),idDay);
				
		//upload the id
		dha.setId(newIdDha);
		
		return dha;
	}
	
	@Override
	public void deleteDayHasActivity(Integer id){
				//delete actual
				Connection dbConnection=DbManager.getInstance().getConnection();
				String selectSQL = "DELETE FROM Day_has_Activity WHERE id = ?";
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
