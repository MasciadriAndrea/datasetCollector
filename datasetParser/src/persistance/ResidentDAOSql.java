package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Resident;

public class ResidentDAOSql implements ResidentDAO {
	@Override
	public List<Resident> getResidentByHouse(Integer id) throws SQLException{
		List<Resident> st=new ArrayList<Resident>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id,age FROM Resident WHERE House_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer ager=0; 
		Integer idr = 0;
		while (rs.next()) {
			ager = Integer.parseInt(rs.getString("age"));
			idr = Integer.parseInt(rs.getString("id"));
			st.add(new Resident(idr,ager));
		}
		return st;
	}
	
	@Override
	public void updateResident(Resident st){
		//TODO
	}
	
	@Override
	public void deleteResident(Integer id){
		//TODO
	}
}
