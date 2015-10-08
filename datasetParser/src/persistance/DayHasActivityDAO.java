package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.DayHasActivity;

public interface DayHasActivityDAO {
	public List<DayHasActivity> getDayHasActivityByDay(Integer id) throws SQLException;
	public DayHasActivity getDayHasActivityById(Integer id) throws SQLException;
	public DayHasActivity updateDayHasActivity(DayHasActivity st,Integer idDay) throws SQLException;
	public void deleteDayHasActivity(Integer id);
}
