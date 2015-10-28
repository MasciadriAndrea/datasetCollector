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
		
		/**********************************
		 ** IMPORT FROM FILE TO DATABASE **
		 **********************************/
		//ArasParser ap=ArasParser.getInstance();
		//----------import data in database from real ARAS dataset (Aras Format)
		//ap.updateHouseData("Aras", "House 1","dataIn/aras");
		//ap.saveDataset();
		
		//----------import data in database from data generated like aras (but generated in Aras Format)
		//ap.updateHouseData("ArasGeneratedRes2", "House 1","dataIn/generatedAras");
		//ap.saveDataset();
		
		//----------import data in database from data generated like kasteren (but generated in Aras Format)
		//ap.updateHouseData("KasterenGenerated", "HouseC","dataIn/generatedKasteren");
		//ap.saveDataset();
		
		//----------import data in database from real Kasteren dataset (kasteren format)
		//KasterenParser kp=KasterenParser.getInstance();
		//kp.updateHouseData("Kasteren", "HouseC", "dataIn/kasteren");
		//kp.saveDataset();
		
		/**************************************
		 ** DELETE ONE DATASET FROM DATABASE **
		 **************************************/
		
		//KasterenParser kp=KasterenParser.getInstance();
		//kp.deleteDatasetById(2);
		
		/*****************************
		 ** LOAD DATA FROM DATABASE **
		 *****************************/
		
		//ArasParser ap=ArasParser.getInstance();
		//DatasetDAOSql d=DatasetDAOSql.getInstance();
		//ap.setDs(d.getDatasetByName("ArasGeneratedRes2"));

		
		/***************************
		 ** EXPORT DATA TO MATLAB **
		 ***************************/
		//MatlabOutManager.getInstance().createMatrices(ap.getDataset().getHouses().get(0));
		
	}

}
