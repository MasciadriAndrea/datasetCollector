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
import dataModel.House;
import dataModel.Resident;
import dataModel.HSensor;
import dataModel.SensorType;
import dataModel.Location;
import dataModel.HSensorset;

public class HouseDAOSql implements HouseDAO {
private static HouseDAOSql instance;
	
	private HouseDAOSql(){
		super();
	}
	
	public static HouseDAOSql getInstance(){
		if(instance==null){
			instance=new HouseDAOSql();
		}
		return instance;
	}
	@Override
	public List<House> getHouseByDS(Integer id) throws SQLException{
		List<House> houses=new ArrayList<House>();
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT name,id FROM House WHERE Dataset_id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		String nameh=""; 
		Integer idh;
		while (rs.next()) {
			nameh = rs.getString("name");
			idh = Integer.valueOf(rs.getString("id"));
			House h=new House(idh,nameh);
			h=getFullHouse(h);	
			houses.add(h);
		}
		return houses;
	}
	
	@Override
	public House getHouseById(Integer id) throws SQLException{
		House house=null;
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT name FROM House WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		String nameh=""; 
		while (rs.next()) {
			nameh = rs.getString("name");
			House h=new House(id,nameh);
			house=getFullHouse(h);
		}
		return house;
	}
	
	private House getFullHouse(House h) throws SQLException{
		Integer id=h.getId();
		//retrieve all the sensorType
		 SensorTypeDAOSql sensorTypeDao=SensorTypeDAOSql.getInstance();
		 List<SensorType> st=sensorTypeDao.getSensorTypeByHouse(id);
		 h.setSensorTypes(st);
		 
		//retrieve all the activities
		 ActivityDAOSql activityDao= ActivityDAOSql.getInstance();
		 List<Activity> ac=activityDao.getActivityByHouse(id);
		 h.setActivities(ac);
		 
		//retrieve all the residents
		 ResidentDAOSql residentDao= ResidentDAOSql.getInstance();
		 List<Resident> r=residentDao.getResidentByHouse(id);
		 h.setResidents(r);
		
		//retrieve all the sensors
		 SensorDAOSql sensorDao=SensorDAOSql.getInstance();
		 List<HSensor> s=sensorDao.getSensorByHouse(id);
		 h.setSensors(s);
		 
		//retrieve all the sensorsets
		 SensorsetDAOSql sensorsetDao=SensorsetDAOSql.getInstance();
		 List<HSensorset> ssa=sensorsetDao.getSensorsetByHouse(id);
		 h.setSensorsets(ssa);
		 
		//retrieve all the location
		 LocationDAOSql locationDao=LocationDAOSql.getInstance();
		 List<Location> l=locationDao.getLocationByHouse(id);
		 h.setLocations(l);
		  
		//retrieve all the days
		 DayDAOSql dayDao=DayDAOSql.getInstance();
		 List<Day> d=dayDao.getDayByHouse(id);
		 h.setDays(d);
		 return h;
	}
	
	private Integer insertHouse(String name, Integer idDs) throws SQLException{
		Connection dbConnection=DbManager.getInstance().getConnection();
		String insertTableSQL = "INSERT INTO House (name,Dataset_id) VALUES (?,?)";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, name);
		preparedStatement.setInt(2, idDs);
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
	public House updateHouse(House h,Integer idDs) throws SQLException{
		System.out.println("inserting Home");
		Integer idH=h.getId();
		// check if House exist -> if the house exists remove it
		Connection dbConnection=DbManager.getInstance().getConnection();
		String selectSQL = "SELECT name FROM House WHERE id = ?";
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, idH);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			deleteHouse(idH);
		}
		
		//insert in the database
		Integer newIdHouse=0;
		newIdHouse= insertHouse(h.getName(),idDs);
		
		//upload the id
		h.setId(newIdHouse);
		
		//insert contained Objects
		
		//retrieve all the residents
		 ResidentDAOSql residentDao=ResidentDAOSql.getInstance();
		 for(Resident rss: h.getResidents()){
			rss=residentDao.updateResident(rss,newIdHouse);
		 }
		 
		//retrieve all the activities
		 ActivityDAOSql activityDao=ActivityDAOSql.getInstance();
		 for(Activity ac: h.getActivities()){
			ac=activityDao.updateActivity(ac,newIdHouse);
		 }
		 
		//retrieve all the location
		 LocationDAOSql locationDao=LocationDAOSql.getInstance();
		 for(Location l: h.getLocations()){
			l=locationDao.updateLocation(l,newIdHouse);
		 }
		 
		//retrieve all the sensorType
		 SensorTypeDAOSql sensorTypeDao=SensorTypeDAOSql.getInstance();
		 for(SensorType st: h.getSensorTypes()){
			st=sensorTypeDao.updateSensorType(st,newIdHouse);
		 }
		
		//retrieve all the sensorset
			 SensorsetDAOSql sensorsetDao=SensorsetDAOSql.getInstance();
			 for(HSensorset s: h.getSensorsets()){
				s=sensorsetDao.updateSensorset(s,newIdHouse);
			 }
		  
		//retrieve all the sensors
		 SensorDAOSql sensorDao=SensorDAOSql.getInstance();
		 for(HSensor s: h.getSensors()){
			s=sensorDao.updateSensor(s,newIdHouse);
		 }
		 
		 System.gc();
		//retrieve all the days
		 DayDAOSql dayDao=DayDAOSql.getInstance();
		  for(Day d: h.getDays()){
				  d=dayDao.updateDay(d,newIdHouse);
				  System.gc();
		  }
		return h;
	}
	
	@Override
	public void deleteHouse(Integer id){
		//delete everything contained
			try{
			
			//retrieve all the days
			  DayDAOSql dayDao=DayDAOSql.getInstance();
			  List<Day> ds=dayDao.getDayByHouse(id);
			  for(Day d: ds){
				dayDao.deleteDay(d.getId());
			}
				
			//retrieve all the activities
			 ActivityDAOSql activityDao=ActivityDAOSql.getInstance();
			 List<Activity> acs=activityDao.getActivityByHouse(id);
			 for(Activity ac: acs){
				activityDao.deleteActivity(ac.getId());
			}
			 
			//retrieve all the residents
			 ResidentDAOSql residentDao= ResidentDAOSql.getInstance();
			 List<Resident> rss=residentDao.getResidentByHouse(id);
			 for(Resident rs: rss){
				residentDao.deleteResident(rs.getId());
			}
			 
			//retrieve all the sensorsets
			 SensorsetDAOSql sensorsetDao=SensorsetDAOSql.getInstance();
			 List<HSensorset> sss=sensorsetDao.getSensorsetByHouse(id);
			 for(HSensorset ss: sss){
				sensorsetDao.deleteSensorset(ss.getId());
			 }
			
			//retrieve all the sensors
			 SensorDAOSql sensorDao=SensorDAOSql.getInstance();
			 List<HSensor> ss=sensorDao.getSensorByHouse(id);
			 for(HSensor s: ss){
				sensorDao.deleteSensor(s.getId());
			 }
			 
			//retrieve all the location
			 LocationDAOSql locationDao=LocationDAOSql.getInstance();
			 List<Location> ls=locationDao.getLocationByHouse(id);
			 for(Location l: ls){
				locationDao.deleteLocation(l.getId());
			 }
			
			//retrieve all the sensorType
			 SensorTypeDAOSql sensorTypeDao=SensorTypeDAOSql.getInstance();
			 List<SensorType> sts=sensorTypeDao.getSensorTypeByHouse(id);
			 for(SensorType st: sts){
				sensorTypeDao.deleteSensorType(st.getId());
			}
			 
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
				//delete actual
				
				Connection dbConnection=DbManager.getInstance().getConnection();
				String selectSQL = "DELETE FROM House WHERE id = ?";
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
