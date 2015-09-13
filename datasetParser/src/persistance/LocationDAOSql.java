package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Location;

public class LocationDAOSql implements LocationDAO {
	@Override
	public List<Location> getLocationByHouse(Integer id) throws SQLException{
		List<Location> st=new ArrayList<Location>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,name FROM Location WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer idl=0; 
		String namel= "";
		while (rs.next()) {
			namel = rs.getString("name");
			idl = Integer.parseInt(rs.getString("id"));
			st.add(new Location(idl, namel));
		}
		return st;
	}
	
	@Override
	public void updateLocation(Location st){
		//TODO
	}
	
	@Override
	public void deleteLocation(Integer id){
		//TODO
	}
}
