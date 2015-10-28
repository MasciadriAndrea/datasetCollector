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
	
	public House createHouse(String name){
		House h=new House(0,name);
		ds.getHouses().add(h);
		return h;
	}
}
