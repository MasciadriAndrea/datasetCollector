package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	public Dataset getDatasetByName(String name) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT id FROM Dataset WHERE name = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setString(1, name);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		Integer id=0;
		Dataset ds=null;
		while (rs.next()) {
			id = rs.getInt("id");
			ds=new Dataset(id,name);
			HouseDAOSql houseDao=new HouseDAOSql();
			List<House> houses=houseDao.getHouseByDS(id);
			ds.setHouses(houses);
		}
		return ds;
	}
	
	@Override
	public Dataset updateDataset(Dataset ds) throws SQLException{
		Integer idDs=ds.getId();
		
		// check if Dataset exist -> if the dataset exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT `name` FROM `Dataset` WHERE `id` = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idDs);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			deleteDataset(idDs);
		}
		
		//insert in the database
		Integer newIdDs=0;
		newIdDs= insertDataset(ds.getName());
		
		//upload the id
		ds.setId(newIdDs);
		
		//insert contained Objects
		HouseDAOSql houseDao=new HouseDAOSql();
		for(House h:ds.getHouses()){
			//System.out.println(h.getName());
			h=houseDao.updateHouse(h,newIdDs);
		}
		
		return ds;
	}
	
	private Integer insertDataset(String name) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO Dataset (name) VALUES (?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, name);
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
	public void deleteDataset(Integer id){
		//delete everything contained
		
		HouseDAOSql houseDao=new HouseDAOSql();
		List<House> houses;
		try {
			houses = houseDao.getHouseByDS(id);
			for(House h: houses){
				houseDao.deleteHouse(h.getId());
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		//delete actual
		
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "DELETE FROM Dataset WHERE id = ?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);
			preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
