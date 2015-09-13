package persistance;

import java.sql.SQLException;
import java.util.List;

import dataModel.Resident;

public interface ResidentDAO {
	public List<Resident> getResidentByHouse(Integer id) throws SQLException;
	public void updateResident(Resident st);
	public void deleteResident(Integer id);
}
