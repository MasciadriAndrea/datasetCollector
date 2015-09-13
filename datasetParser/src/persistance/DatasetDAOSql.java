package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import common.DbManager;
import dataModel.Dataset;
import dataModel.House;

public class DatasetDAOSql implements DatasetDAO {
	@Override
	public Dataset getDatasetById(Integer id) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT name FROM Dataset WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String name="";
		Dataset ds=null;
		while (rs.next()) {
			name = rs.getString("name");
			ds=new Dataset(id,name);
			HouseDAOSql houseDao=new HouseDAOSql();
			List<House> houses=houseDao.getHouseByDS(id);
			ds.setHouses(houses);
		}
		return ds;
	}
	
	@Override
	public Dataset getDatasetByName(String name){
		//TODO
		return null;
	}
	
	@Override
	public void updateDataset(Dataset ds){
		//TODO
	}
	
	@Override
	public void deleteDataset(Integer id){
		//TODO
	}
}
