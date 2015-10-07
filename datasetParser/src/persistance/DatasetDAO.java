package persistance;

import java.sql.SQLException;
import dataModel.Dataset;

public interface DatasetDAO {
	public Dataset getDatasetById(Integer id) throws SQLException;
	public Dataset getDatasetByName(String name) throws SQLException;
	public Dataset updateDataset(Dataset ds) throws SQLException;
	public void deleteDataset(Integer id);
}
