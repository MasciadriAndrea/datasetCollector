package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.DayHasActivity;

public interface DayHasActivityDAO {
	public List<DayHasActivity> getDayHasActivityByDay(Integer id) throws SQLException;
	public void updateDayHasActivity(DayHasActivity st);
	public void deleteDayHasActivity(Integer id);
}
