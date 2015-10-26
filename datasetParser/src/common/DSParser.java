package common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import persistance.DatasetDAOSql;
import specificParser.ArasParser;
import specificParser.KasterenParser;
import dataModel.Activity;
import dataModel.Dataset;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.House;
import dataModel.Location;
import dataModel.Resident;
import dataModel.HSensor;
import dataModel.SensorTime;
import dataModel.SensorType;
import dataModel.HSensorset;

public class DSParser {

	public static void main(String[] args) throws SQLException {

		
		//ArasParser ap=ArasParser.getInstance();
		KasterenParser kp=KasterenParser.getInstance();
		
		//example load data from file and save in database
		//
		//kp.deleteDatasetById(27);
		kp.updateHouseData("Kasteren", "HouseC");
		kp.saveDataset();
		

		
		//example load data from database
		//
		//DatasetDAOSql d=DatasetDAOSql.getInstance();
		//kp.setDs(d.getDatasetByName("Kasteren"));

		
		
		//example load data from database and export to matlab format
		//
		//MatlabOutManager.getInstance().createMatrices(ap.getDataset().getHouses().get(0));
		
		

	}

}
