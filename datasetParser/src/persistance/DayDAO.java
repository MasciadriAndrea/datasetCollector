package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.Day;

public interface DayDAO {
	public List<Day> getDayByHouse(Integer id) throws SQLException;
	public void updateDay(Day st);
	public void deleteDay(Integer id);
}
