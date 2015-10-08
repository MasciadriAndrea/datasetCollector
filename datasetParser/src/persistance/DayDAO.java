package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.Day;

public interface DayDAO {
	public List<Day> getDayByHouse(Integer id) throws SQLException;
	public Day getDayById(Integer id) throws SQLException;
	public Day updateDay(Day st,Integer houseId) throws SQLException;
	public void deleteDay(Integer id);
}
