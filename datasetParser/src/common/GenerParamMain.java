package common;

import generatorParameters.Parameters;
import generatorParameters.ParametersHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import persistance.DatasetDAOSql;
import specificParser.ArasParser;
import dataModel.Activity;
import dataModel.DayHasActivity;
import dataModel.House;
import dataModel.Resident;

public class GenerParamMain {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub


		ArasParser ds =ArasParser.getInstance();
		DatasetDAOSql d=DatasetDAOSql.getInstance();
		ds.setDs(d.getDatasetByName("ArasDs"));
		//		System.out.println(ds.getDataset().getHouses().get(0).getName());
//		House h=ds.getDataset().getHouses().get(0);
//		for(Resident r:h.getResidents()){
//			System.out.println("Resident" + r.getUniqueResidentId());
//			for (DayHasActivity dha : h.getDays().get(0).getDailyActivities()){
//				if (dha.getResident().getUniqueResidentId() == r.getUniqueResidentId()){
//					//System.out.println("Activity: " + dha.getActivity().getName()+ " StartSec: " + dha.getStartSec());
//				}
//			}
//		}
		
		ParametersHandler ph = ParametersHandler.getInstance();

		//		 Manual settings of subactivities
		ph.processChain(ds.getDataset().getHouses().get(0));
/*
		System.out.println(ph.getParameters().getActivities().get(0).getName());
		System.out.println(ph.getParameters().getActivities().get(0).getSubactivities().get(0).getName());
		 */
	}

}
