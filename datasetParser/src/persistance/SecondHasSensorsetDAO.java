package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.DayHasActivity;
import dataModel.SecondHasSensorset;

public interface SecondHasSensorsetDAO {
	public List<SecondHasSensorset> getSecondHasSensorsetByDay(Integer id) throws SQLException;
	public SecondHasSensorset getSecondHasSensorsetById(Integer id) throws SQLException;
	public SecondHasSensorset updateSecondHasSensorset(SecondHasSensorset st,Integer idDay) throws SQLException;
	public void deleteSecondHasSensorset(Integer id);
}
