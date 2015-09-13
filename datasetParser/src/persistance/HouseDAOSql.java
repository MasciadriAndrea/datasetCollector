package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DbManager;
import dataModel.Activity;
import dataModel.Day;
import dataModel.House;
import dataModel.Resident;
import dataModel.Sensor;
import dataModel.SensorType;
import dataModel.Location;

public class HouseDAOSql implements HouseDAO {
	
	@Override
	public List<House> getHouseByDS(Integer id) throws SQLException{
		List<House> houses=new ArrayList<House>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT name,id FROM House WHERE Dataset_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		String nameh=""; 
		Integer idh;
		while (rs.next()) {
			nameh = rs.getString("name");
			idh = Integer.valueOf(rs.getString("id"));
			House h=new House(idh,nameh);
			
			//retrieve all the sensorType
			 SensorTypeDAOSql sensorTypeDao=new SensorTypeDAOSql();
			 List<SensorType> st=sensorTypeDao.getSensorTypeByHouse(idh);
			 h.setSensorTypes(st);
			 
			//retrieve all the activities
			 ActivityDAOSql activityDao=new ActivityDAOSql();
			 List<Activity> ac=activityDao.getActivityByHouse(idh);
			 h.setActivities(ac);
			 
			//retrieve all the residents
			 ResidentDAOSql residentDao=new ResidentDAOSql();
			 List<Resident> r=residentDao.getResidentByHouse(idh);
			 h.setResidents(r);
			
			//retrieve all the sensors
			 SensorDAOSql sensorDao=new SensorDAOSql();
			 List<Sensor> s=sensorDao.getSensorByHouse(idh);
			 h.setSensors(s);
			 
			//retrieve all the location
			 LocationDAOSql locationDao=new LocationDAOSql();
			 List<Location> l=locationDao.getLocationByHouse(idh);
			 h.setLocations(l);
			 
			//retrieve all the days
			 DayDAOSql dayDao=new DayDAOSql();
			 List<Day> d=dayDao.getDayByHouse(idh);
			 h.setDays(d);
			
			houses.add(h);
		}
		return houses;
	}
	
	@Override
	public House getHouseById(Integer id){
		//TODO
		return null;
	}
	
	@Override
	public House getHouseByName(String name){
		//TODO
		return null;
	}
	
	@Override
	public void updateHouse(House ds){
		//TODO
	}
	
	@Override
	public void deleteHouse(Integer id){
		//TODO
	}
}
