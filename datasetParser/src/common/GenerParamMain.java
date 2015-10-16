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
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Resident;
import dataModel.SensorTime;

public class GenerParamMain {

	public static void main(String[] args) throws SQLException {

		ArasParser ds =ArasParser.getInstance();
		DatasetDAOSql d=DatasetDAOSql.getInstance();
		ds.setDs(d.getDatasetByName("ArasDs"));
		
		ParametersHandler ph = ParametersHandler.getInstance();

		//		 Manual settings of subactivities
		ph.processChain(ds.getDataset().getHouses().get(0));
		/*
		List<HSensorsetGP> senss = ph.getParameters().getSensorsets();
		for (HSensorsetGP ss: senss){
			System.out.print("Sensorset id: " + ss.getUniqueSensorsetId() + " ");
			for (SensorTime st : ss.getSensors()){
				if (st.getValue().equals("1")){
					System.out.print(st.getSensor().getId() + "-"+st.getSensor().getName() + " ");
				}
			}
			System.out.println("");
		}
		*/
/*
		System.out.println(ph.getParameters().getActivities().get(0).getName());
		System.out.println(ph.getParameters().getActivities().get(0).getSubactivities().get(0).getName());
		 */
	}

}
