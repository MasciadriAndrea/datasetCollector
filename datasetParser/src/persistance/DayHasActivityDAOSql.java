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
import dataModel.Resident;

public class DayHasActivityDAOSql implements DayHasActivityDAO {
private static DayHasActivityDAOSql instance;
	
	private DayHasActivityDAOSql(){
		super();
	}
	
	public static DayHasActivityDAOSql getInstance(){
		if(instance==null){
			instance=new DayHasActivityDAOSql();
		}
		return instance;
	}
	@Override
	public List<DayHasActivity> getDayHasActivityByDay(Integer id) throws SQLException{
		List<DayHasActivity> st=new ArrayList<DayHasActivity>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,startSec,endSec,Activity_id,Resident_id FROM Day_has_Activity WHERE Day_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		Integer idd=0; 
		Integer startd= 0;
		Integer endd= 0;
		Integer aid=0;
		Integer rid=0;
		while (rs.next()) {
			startd = Integer.parseInt(rs.getString("startSec"));
			endd = Integer.parseInt(rs.getString("endSec"));
			idd = Integer.parseInt(rs.getString("id"));
			aid = Integer.parseInt(rs.getString("Activity_id"));
			rid = Integer.parseInt(rs.getString("Resident_id"));
			
			//retrieve the associated activity	
			 ActivityDAOSql activityDao=ActivityDAOSql.getInstance();
			 Activity a=activityDao.getActivityById(aid);
			 
			//retrieve the associated resident	
			 ResidentDAOSql residentDao=ResidentDAOSql.getInstance();
			 Resident r=residentDao.getResidentById(rid);
			
			st.add(new DayHasActivity(idd, startd, endd, a, r));
		}
		return st;
	}
	
	@Override
	public DayHasActivity getDayHasActivityById(Integer id) throws SQLException{
		DayHasActivity dha=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT startSec,endSec,Activity_id,Resident_id FROM Day_has_Activity WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		Integer startd= 0;
		Integer endd= 0;
		Integer aid=0;
		Integer rid=0;
		while (rs.next()) {
			startd = Integer.parseInt(rs.getString("startSec"));
			endd = Integer.parseInt(rs.getString("endSec"));
			aid = Integer.parseInt(rs.getString("Activity_id"));
			rid = Integer.parseInt(rs.getString("Resident_id"));
			//retrieve the associated activity	
			 ActivityDAOSql activityDao=ActivityDAOSql.getInstance();
			 Activity a=activityDao.getActivityById(aid);
			//retrieve the associated resident	
			 ResidentDAOSql residentDao=ResidentDAOSql.getInstance();
			 Resident r=residentDao.getResidentById(rid);
			
			 dha=new DayHasActivity(id, startd, endd, a,r);
		}
		return dha;
	}
	
	public Integer insertDayHasActivity(String startSec,String endSec,Integer idAct,Integer idDay,Integer resId) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Day_has_Activity (startSec,endSec,Day_id,Activity_id,Resident_id) VALUES (?,?,?,?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, startSec);
		preparedStatement.setString(2, endSec);
		preparedStatement.setInt(3, idDay);
		preparedStatement.setInt(4, idAct);
		preparedStatement.setInt(5, resId);
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
		//System.out.println("inserting activity "+dha.getActivity().getName());
		Integer idDha=dha.getId();
		
		// check if exist -> if exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Day_has_Activity WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idDha);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			deleteDayHasActivity(idDha);
		}
		
		//insert in the database
		Integer newIdDha=0;
		//System.out.println(dha.getStartSec().toString());
		//System.out.println(dha.getEndSec().toString());
		//System.out.println(dha.getActivity().getId().toString());
		//System.out.println(dha.getResident().getId().toString());
		newIdDha= insertDayHasActivity(dha.getStartSec().toString(),dha.getEndSec().toString(),dha.getActivity().getId(),idDay,dha.getResident().getId());
				
		//upload the id
		dha.setId(newIdDha);
		
		//TODO return dha;
		return null;
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
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	}
}
