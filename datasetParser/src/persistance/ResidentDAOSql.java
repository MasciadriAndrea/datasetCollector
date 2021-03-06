package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Resident;

public class ResidentDAOSql implements ResidentDAO {
	private static ResidentDAOSql instance;
	
	private ResidentDAOSql(){
		super();
	}
	
	public static ResidentDAOSql getInstance(){
		if(instance==null){
			instance=new ResidentDAOSql();
		}
		return instance;
	}
	@Override
	public List<Resident> getResidentByHouse(Integer id) throws SQLException{
		List<Resident> st=new ArrayList<Resident>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,age,uniqueResidentId FROM Resident WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		Integer ager=0; 
		Integer uri=0;
		Integer idr = 0;
		while (rs.next()) {
			ager = Integer.parseInt(rs.getString("age"));
			idr = Integer.parseInt(rs.getString("id"));
			uri=Integer.parseInt(rs.getString("uniqueResidentId"));
			st.add(new Resident(idr,ager,uri));
		}
		return st;
	}
	
	@Override
	public Resident getResidentById(Integer id) throws SQLException{
		Resident r=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT age,uniqueResidentId FROM Resident WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		Integer ager=0; 
		Integer uri=0;
		while (rs.next()) {
			ager = Integer.parseInt(rs.getString("age"));
			uri=Integer.parseInt(rs.getString("uniqueResidentId"));
			r=new Resident(id,ager,uri);
		}
		return r;
	}
	
	private Integer insertResident(String age,Integer idHouse,Integer uri) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Resident (age,House_id,uniqueResidentId) VALUES (?,?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, age);
		preparedStatement.setInt(2, idHouse);
		preparedStatement.setInt(3, uri);
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
	public Resident updateResident(Resident r,Integer idHouse) throws SQLException{
		Integer idR=r.getId();
		
		// check if exist -> if the exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Resident WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idR);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			deleteResident(idR);
		}
		
		//insert in the database
		Integer newIdResident=0;
		newIdResident= insertResident(r.getAge().toString(),idHouse,r.getUniqueResidentId());
				
		//upload the id
		r.setId(newIdResident);
		return r;
	}
	
	@Override
	public void deleteResident(Integer id){
		//delete actual
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "DELETE FROM Resident WHERE id = ?";
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
