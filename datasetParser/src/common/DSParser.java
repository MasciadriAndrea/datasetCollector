package common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import persistance.DatasetDAOSql;
import specificParser.ArasParser;
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

		
		ArasParser ap=ArasParser.getInstance();
		
		//example load data from file and save in database
		//
		//ap.updateHouseData("GeneratedDS", "House1");
		//ap.saveDataset();
		
		
		//example load data from database and print
		//
		//DatasetDAOSql d=DatasetDAOSql.getInstance();
		//ap.setDs(d.getDatasetByName("GeneratedDs"));
		//System.out.println(ap.getDataset().getHouses().get(0).getName());
		
		//example load data from database and export to matlab format
		//
		DatasetDAOSql d=DatasetDAOSql.getInstance();
		House h=d.getDatasetByName("GeneratedDs").getHouses().get(0);
		MatlabOutManager.getInstance().computeSecondDayMatrix(h);
	}

}
