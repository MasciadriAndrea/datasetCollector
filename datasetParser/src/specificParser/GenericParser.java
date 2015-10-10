package specificParser;

import dataModel.Dataset;
import dataModel.House;

public abstract class GenericParser implements IParser {
	protected Dataset ds;
	
	protected GenericParser(){
		super();
		this.ds=null;
	}
	
	public Dataset getDataset() {
		return ds;
	}

	public void setDs(Dataset ds) {
		this.ds=ds;
	}
	
	public void createDataset(Integer id, String name){
		this.ds=new Dataset(id,name);
	}
	
	public void createHouse(String name){
		ds.getHouses().add(new House(null,name));
	}
}
