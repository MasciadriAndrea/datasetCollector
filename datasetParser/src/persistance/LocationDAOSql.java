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

public class LocationDAOSql implements LocationDAO {
	@Override
	public List<Location> getLocationByHouse(Integer id) throws SQLException{
		List<Location> st=new ArrayList<Location>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,name,uniqueLocationId FROM Location WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer idl=0; 
		Integer uli=0;
		String namel= "";
		while (rs.next()) {
			namel = rs.getString("name");
			idl = Integer.parseInt(rs.getString("id"));
			uli = Integer.parseInt(rs.getString("uniqueLocationId"));
			st.add(new Location(idl,uli, namel));
		}
		return st;
	}
	
	@Override
	public Location getLocationById(Integer id) throws SQLException{
		Location st=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT name,uniqueLocationId FROM Location WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL ); 
		String namel= "";
		Integer uli=0;
		while (rs.next()) {
			namel = rs.getString("name");
			uli= Integer.parseInt(rs.getString("uniqueLocationId"));
			st=new Location(id, uli, namel);
		}
		return st;
	}
	
	private Integer insertLocation(String name,Integer idHouse,Integer uli) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Locaion (name,House_id,uniqueLocationId) VALUES (?,?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, name);
		preparedStatement.setInt(2, idHouse);
		preparedStatement.setInt(3, uli);
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
	public Location updateLocation(Location loc, Integer idHouse) throws SQLException{
		Integer idL=loc.getId();
		
		// check if exist -> if exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM Location WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idL);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		while (rs.next()) {
			deleteLocation(idL);
		}
		
		//insert in the database
		Integer newIdLoc=0;
		newIdLoc= insertLocation(loc.getName(),idHouse,loc.getUniqueLocationId());
				
		//upload the id
		loc.setId(newIdLoc);
		
		return loc;
	}
	
	@Override
	public void deleteLocation(Integer id){
		//delete actual
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "DELETE FROM Location WHERE id = ?";
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
