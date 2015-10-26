package common;

import generatorParameters.Parameters;
import generatorParameters.ParametersHandler;
import generatorParameters.ParametersHandlerK;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import persistance.DatasetDAOSql;
import specificParser.ArasParser;
import specificParser.KasterenParser;
import dataModel.Activity;
import dataModel.DayHasActivity;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Resident;
import dataModel.SensorTime;

public class GenerParamMain {

	public static void main(String[] args) throws SQLException, IOException {
		System.out.println("loading dataset from database");
		System.out.println("----------------------------------");
		
		
		DatasetDAOSql d=DatasetDAOSql.getInstance();
		
		
		
		// ---- FOR ARAS
		//ArasParser ds =ArasParser.getInstance();
		//ds.setDs(d.getDatasetByName("ArasDS"));
		//ParametersHandler ph = ParametersHandler.getInstance();
		
		
		// ---- FOR KASTEREN
		KasterenParser kp=KasterenParser.getInstance();
		kp.setDs(d.getDatasetByName("Kasteren"));
		ParametersHandlerK ph = ParametersHandlerK.getInstance();
		try{
			ph.processChain(kp.getDataset().getHouses().get(0));
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
