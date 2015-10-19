package generatorParameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import common.ClusteringHandler;
import common.DbManager;
import dataModel.Activity;
import dataModel.Dataset;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.HSensor;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Resident;
import dataModel.SensorTime;
import persistance.DatasetDAOSql;
import specificParser.ArasParser;

public class ParametersHandler {
	private static String directoryInput = "dataIn/generatorParam";
	private static String directoryOutput = "dataOut/generatorParam";
	private static String confFileName = "/fileName";
	private static ParametersHandler instance;
	private final static Integer cluster_param_N=5;
	private final static Integer cluster_param_K=2;
	private Parameters parameters;
	private House house;


	private ParametersHandler(){
		super();
		this.parameters = new Parameters();
		this.house = null;
	}

	public static ParametersHandler getInstance(){
		if(instance==null){
			instance=new ParametersHandler();
		}
		return instance;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public void processChain(House house) throws IOException{
		this.house = house;
		//		this.setActivitiesWithConfigurations();
		this.parseGeneratorParam();
		this.setDay();
		this.setTransitionMatrices();
		ClusteringHandler.getInstance().clusterizeDha(parameters,cluster_param_N,cluster_param_K);
		this.computeProbMatrices();
		this.computePatternInitialProb();
		this.computeSSiniProbInPattern();
		//		this.computeActivitiesRhytm();
		//TODO computeTimeDistribution();
		//TODO exportAll();

		/*
		 * PRINT how many pattern for every activtyGP
		 * 
		for(ActivityGP agp:this.parameters.getActivities()){
			System.out.println("--------Patterns of activity: "+agp.getName());
			Integer countP=0;
			for(Pattern patt:agp.getPatterns()){
				countP++;
				Integer countDha=0;
				for(DayHasActivity dha:patt.getDhasInCluster()){
					countDha++;
				}
				System.out.println("pattern n: "+countP+" has "+countDha+" dha");	
			}
		}*/
	}

	private void computeSSiniProbInPattern() {
		for(ActivityGP agp:this.parameters.getActivities()){
			for(Pattern pattern:agp.getPatterns()){
				Integer sumOfDiagonal=0;
				Integer[][] m=pattern.getSStransMatrix();
				Integer numOfSS=this.parameters.getSensorsets().size();
				for(int row=0;row<numOfSS;row++){
					sumOfDiagonal+=m[row][row];
				}
				ArrayList<Float> pl=new ArrayList<Float>();
				for(int row=0;row<numOfSS;row++){
					pl.add((float) (m[row][row]/sumOfDiagonal));
				}
				pattern.setSsIniProbInPattern(pl);
			}
		}
	}

	private void computePatternInitialProb() {
		for(ActivityGP agp:this.parameters.getActivities()){
			for(Pattern pattern:agp.getPatterns()){
				pattern.setInitialProb((float) (pattern.getDhasInCluster().size()/agp.getDhaInActivity()));
			}
		}

	}

	private void computeProbMatrices() {
		//compute the overallProbSS from the overallTransitionSS
		this.parameters.setOverallProbSS(normalizeByRow(this.parameters.getOverallTransitionSS()));
		//compute SSProbMatrix of every pattern using its SStransMatrix that is sum of SStransMatrix
		Integer numOfSS=this.parameters.getSensorsets().size();
		for(ActivityGP agp:this.parameters.getActivities()){
			for(Pattern pattern:agp.getPatterns()){				
				Integer[][] initialZeroMatrix=new Integer[numOfSS][numOfSS];
				initializeMatrix(initialZeroMatrix,numOfSS);
				pattern.setSStransMatrix(initialZeroMatrix);
				for(DayHasActivity dha:pattern.getDhasInCluster()){
					pattern.setSStransMatrix(sumOfMatrices(pattern.getSStransMatrix(),dha.getSStransMatrix()));
				}
				pattern.setSSProbMatrix(normalizeByRow(pattern.getSStransMatrix()));
			}
		}
	}

	private Integer[][] sumOfMatrices(Integer[][] m1,Integer[][] m2){
		Integer d11=m1.length;
		Integer d12=m2.length;
		Integer d21=m1[0].length;
		Integer d22=m2[0].length;
		Integer[][] m3=new Integer[d11][d21];
		if((d11==d12)&&(d21==d22)){
			for(int row=0;row<d11;row++){
				for(int col=0;col<d21;col++){
					m3[row][col]=m1[row][col]+m2[row][col];
				}
			}
		}
		return m3;
	}

	private Float[][] normalizeByRow(Integer[][] m){
		int numRow=m.length;
		int numCol=m[0].length;
		Float[][] m2=new Float[numRow][numCol];
		for(int row=0;row<numRow;row++){
			Integer sumRow=0;
			for(int col=0;col<numCol;col++){
				sumRow+=m[row][col];
			}
			for(int col=0;col<numCol;col++){
				m2[row][col]=(float) m[row][col]/sumRow;
			}
		}
		return m2;
	}

	private void initializeMatrix(Integer[][] matr, Integer numUniqueSS){
		for(int row=0;row<numUniqueSS;row++){
			for(int col=0;col<numUniqueSS;col++){
				matr[row][col]=0;
			}
		}
	}

	private void setTransitionMatrices(){
		System.out.println("Computing all the SS transition matrices");
		//initialization
		Integer numUniqueSS=this.parameters.getSensorsets().size();
		Integer[][] allTransSS=new Integer[numUniqueSS][numUniqueSS];
		initializeMatrix(allTransSS,numUniqueSS);

		for(Resident r:this.parameters.getResidents()){
			Integer previousSSid=0;
			Integer currentSSid=0;
			for(DayGP daygp:this.parameters.getDays()){
				if(daygp.resident.getUniqueResidentId()==r.getUniqueResidentId()){
					String[] ssidSecond=daygp.getSSid();
					for(DayHasActivity dha: daygp.getDailyActivities()){
						Integer[][] dhaTransSS=new Integer[numUniqueSS][numUniqueSS];
						initializeMatrix(dhaTransSS,numUniqueSS);
						//transition from previous activity
						currentSSid=Integer.valueOf(ssidSecond[dha.getStartSec()-1]);
						if((currentSSid!=0)&&(previousSSid!=0)){
							Integer prev=allTransSS[previousSSid-1][currentSSid-1];
							allTransSS[previousSSid-1][currentSSid-1]=prev+1;
						}
						previousSSid=currentSSid;
						//inside dha loop... all the transition inside
						for(Integer sec=dha.getStartSec()+1;sec<dha.getEndSec();sec++){
							currentSSid=Integer.valueOf(ssidSecond[sec-1]);
							if((currentSSid!=0)&&(previousSSid!=0)){
								Integer prev=allTransSS[previousSSid-1][currentSSid-1];
								allTransSS[previousSSid-1][currentSSid-1]=prev+1;
								prev=dhaTransSS[previousSSid-1][currentSSid-1];
								dhaTransSS[previousSSid-1][currentSSid-1]=prev+1;
							}
							previousSSid=currentSSid;
						}
						dha.setSStransMatrix(dhaTransSS);
					}
				}
			}
		}
		this.parameters.setOverallTransitionSS(allTransSS);
	}

	private void setDay(){
		System.out.println("Loading days");
		List<Day> houseDays = house.getDays();
		List<DayGP> dayGP=new ArrayList<DayGP>();
		Integer dhaUniqueId=0;
		for(Resident res:this.parameters.getResidents()){
			//for each resident import its days
			for(Day realDay:houseDays){
				System.out.println("Uploading day "+realDay.getIncrementalDay()+" resident "+res.getUniqueResidentId());
				//I should create a dayGP for this resident
				DayGP daygp=new DayGP(0,realDay.getIncrementalDay(),realDay.getDay(),realDay.getMonth(),realDay.getYear(),realDay.getSecondIdSS(),res);
				List<DayHasActivity> dhaRes=new ArrayList<DayHasActivity>();
				String[] ids=daygp.getSSid();
				Integer[] idsInt=new Integer[86400];
				Integer okSec=0;
				Integer koSec=0;
				for(DayHasActivity dha:realDay.getDailyActivities()){
					if(dha.getResident().getUniqueResidentId()==res.getUniqueResidentId()){

						Integer performedSubActivityId=dha.getActivity().getUniqueActivityId();
						ActivityGP agp=this.getParameters().getActivityGpBySubActId(performedSubActivityId);
						if((agp==null)||(agp.getUniqueActivityId()==0)){
							agp=this.parameters.getActivityGpByIdGP(0);
						}
						dhaUniqueId++;
						DayHasActivityGP dhaNew= new DayHasActivityGP(0,dhaUniqueId,dha.getStartSec(),dha.getEndSec(),agp,dha.getResident());
						dhaRes.add(dhaNew);			
						Integer startSec=dha.getStartSec();
						Integer endSec=dha.getEndSec();
						for(Integer sec=startSec;sec<=endSec;sec++){
							//filtering data
							if(agp.getUniqueActivityId()!=0){
								Integer previousSSId=Integer.parseInt(ids[sec-1]);
								HSensorset previousSS=house.getSensorsetByUniqueId(previousSSId);
								List<Integer> filteredSIds=previousSS.getActivatedSensorsId();
								filteredSIds.retainAll(agp.getAllowedSensorsId());
								Integer newSSId=this.manageSS(filteredSIds);
								idsInt[sec-1]=newSSId;
								okSec++;
								//System.out.println("Changing ss "+previousSSId+" in "+newSSId+" for day "+daygp.getIncrementalDay());
							}else{
								idsInt[sec-1]=0;koSec++;
							}
						}


					}
				}
				System.out.println("Total seconds parsed: "+(okSec+koSec)+" -> not accepted seconds: "+koSec);	
				daygp.setSSid(idsInt);
				daygp.setDailyActivities(dhaRes);
				dayGP.add(daygp);
			}
		}
		System.out.println("Number of unique ss before: "+this.house.getSensorsets().size());
		System.out.println("Number of unique ss after: "+this.parameters.getSensorsets().size());
		this.parameters.setDays(dayGP);

	}

	public Integer manageSS(List<Integer> incomingList){
		Integer numberOfSS=0;
		for(HSensorset ss:this.parameters.getSensorsets()){
			if(ss.getUniqueSensorsetId()!=0){
				numberOfSS++;
				Integer matchingSensor=0;
				List<Integer> actSensorss=ss.getActivatedSensorsId();
				for(Integer ids:actSensorss){
					if(incomingList.contains(ids)){
						matchingSensor++;
					}
				}
				if((matchingSensor==actSensorss.size())&&(actSensorss.size()==incomingList.size())){
					//is the same ss
					return ss.getUniqueSensorsetId();
				}
			}
		}
		//otherwise create SS
		List<SensorTime> lst=new ArrayList<SensorTime>();
		for(Integer sens=1;sens<=this.house.getSensors().size();sens++){
			HSensor sensor=this.house.getSensorByUniqueId(sens);
			String value="0";
			if(incomingList.contains(sens)){
				value="1";
			}
			SensorTime st=new SensorTime(0,sensor,value);
			lst.add(st);
		}
		HSensorset newss=new HSensorset(0,numberOfSS+1,lst);
		this.parameters.getSensorsets().add(newss);
		return numberOfSS+1;
	}
	public void parseGeneratorParam() throws IOException{



		File folder = new File(directoryInput);
		if (!folder.exists()) {
			throw new NotDirectoryException(null);
		}

		File generalConfig = new File(directoryInput + "/general.conf");
		if (!generalConfig.exists()) {
			throw new FileNotFoundException(null);
		}

		List<String> activityNames = null;

		BufferedReader readerGeneral = new BufferedReader(new FileReader(generalConfig));
		String line = null;
		int count = 0;
		while ((line = readerGeneral.readLine()) != null) {
			count++;
			if (count == 1){
				String[] residentIds = line.split(",");

				for (String id: residentIds){
					ParametersHandler.getInstance().getParameters().addResident(this.house.getResidentByUniqueId(Integer.decode(id)));
				}
				
			}
			//			second line stands for Activities
			if (count == 2){
				activityNames =  Arrays.asList(line.split(","));
				break;
			}	
		}
		readerGeneral.close();

		for (int i = 0; i < activityNames.size(); i++){
			String activityName = activityNames.get(i);

			List<Activity> subactivities = new ArrayList<Activity>();
			List<HSensor> allowedSensors = new ArrayList<HSensor>();

			activityName = activityName.replaceAll("\"", "");
			activityName = activityName.replaceAll(" ", "");
			
			File activityConfig = new File(directoryInput+"/"+activityName+".conf");
			if (!activityConfig.exists()) {
				throw new FileNotFoundException(null);
			}
			
			BufferedReader readerActivities = new BufferedReader(new FileReader(activityConfig));
			String string = null;
			count = 0;
			while ((string = readerActivities.readLine()) != null) {
				count++;
				//				first line stands for subactivities
				if (count == 1){
					List<String> subactivitiesIds = Arrays.asList(string.split(","));

					for(String subactivityId: subactivitiesIds){
						subactivities.add(this.house.getActivityByUniqueId(Integer.decode(subactivityId)));
					}
				}
				//			second line stands for sensors
				if (count == 2){
					List<String >allowedSensorIds =  Arrays.asList(string.split(","));
					for(String allowedSensorId : allowedSensorIds){
						allowedSensors.add(this.house.getSensorByUniqueId(Integer.decode(allowedSensorId)));
					}
					break;
				}
			}
			readerActivities.close();
			ActivityGP newActivity = new ActivityGP(0, i, activityName, subactivities, allowedSensors);
			this.parameters.addActivity(newActivity);
		}
	}

}




