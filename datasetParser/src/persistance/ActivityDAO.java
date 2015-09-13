package persistance;

import java.sql.SQLException;
import java.util.List;
import dataModel.Activity;

public interface ActivityDAO {
	public List<Activity> getActivityByHouse(Integer id) throws SQLException;
	public Activity getActivityById(Integer aid) throws SQLException;
	public void updateActivity(Activity st);
	public void deleteActivity(Integer id);
}
