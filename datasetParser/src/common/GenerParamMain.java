package common;

import generatorParameters.Parameters;
import generatorParameters.ParametersHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import persistance.DatasetDAOSql;
import specificParser.ArasParser;
import dataModel.Activity;
import dataModel.DayHasActivity;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Resident;
import dataModel.SensorTime;

public class GenerParamMain {

	public static void main(String[] args) throws SQLException, IOException {
		System.out.println("loading Aras dataset from database");
		System.out.println("----------------------------------");
		ArasParser ds =ArasParser.getInstance();
		DatasetDAOSql d=DatasetDAOSql.getInstance();
		ds.setDs(d.getDatasetByName("ArasDs"));
		
		
		ParametersHandler ph = ParametersHandler.getInstance();

		ph.processChain(ds.getDataset().getHouses().get(0));


	}

}
