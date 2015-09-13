package persistance;

import java.sql.SQLException;
import dataModel.Dataset;

public interface DatasetDAO {
	public Dataset getDatasetById(Integer id) throws SQLException;
	public Dataset getDatasetByName(String name);
	public void updateDataset(Dataset ds);
	public void deleteDataset(Integer id);
}
