package generatorParameters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

public class ParametersHandlerK {
	
	/*
	 * 
	 * THIS IS for KASTEREN -> NO FILTERING, SECONDS FROM ZERO
	 * 
	 * 
	 */
	private static String directoryInput = "dataIn/generatorParamKasteren";
	private static String directoryOutput = "dataOut/generatorParam";
	private static ParametersHandlerK instance;
	private final static int cluster_param_N=7;
	private final static int cluster_param_K=3;
	private final static int DCT_K=10;
	private Parameters parameters;
	private House house;


	private ParametersHandlerK(){
		super();
		this.parameters = new Parameters();
		this.house = null;
	}

	public static ParametersHandlerK getInstance(){
		if(instance==null){
			instance=new ParametersHandlerK();
		}
		return instance;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public void processChain(House house) throws Exception{
		this.house = house;
		this.parseGeneratorParam();
		this.setDay();
		this.setTransitionMatrices();
		ClusteringHandler.getInstance().clusterizeDha(parameters,cluster_param_N,cluster_param_K);
		this.computeProbMatrices();
		this.computePatternInitialProb();
		this.computeSSiniProbInPattern();
		//this.computeActivitiesRhytm(); NOT USED
		this.computeTimeDistribution();
		this.exportAll();
	}


	private void exportAll() throws IOException {
		System.out.println("Exporting all");
		File folder = new File(directoryOutput);
		if (!folder.exists()) {
			throw new NotDirectoryException(null);
		}
		//-----------save Global SS transition probabilities
		exportOverallTransition();
		//------------------save Patterns
		exportPatterns();
		//---------------save rhythm
		//exportRhythm(); NOT USED
		//---------------save sensorsets
		exportSensorsets();
	}

	private void exportOverallTransition() throws IOException{
		BufferedWriter writerGlobalSStransProb = new BufferedWriter(new FileWriter(directoryOutput+"/SStransitionProbability.conf"));
		for (float[] row : this.parameters.getOverallProbSS()){
			String line = "";
			for (Float column : row){
				line += column+",";
			}
			line=line.substring(0,line.length()-1);
			writerGlobalSStransProb.write(line);
			writerGlobalSStransProb.newLine();
		}
		writerGlobalSStransProb.close();
	}

	private void exportPatterns() throws IOException{
		// idAct, prob, Name, numberOfSSInPattern, listOfSSID (comma separated), listOfInitialProb, matrix of probability in line
		for (ActivityGP agp : parameters.getActivities()){
			if(!agp.getUniqueActivityId().equals(0)){
			BufferedWriter writerActivity = new BufferedWriter(new FileWriter(directoryOutput+"/pattSS_"+agp.getName()+".conf"));
			int npatt=0;
			for(Pattern patt:agp.getPatterns()){
				npatt++;
				Map<Integer,Integer> ssInPatt=new HashMap<Integer,Integer>();
				for(DayHasActivityGP dha:patt.getDhasInCluster()){
					Map<Integer,Integer> ssInDha=dha.getUsedSS();
					for(Integer ssid:ssInDha.keySet()){
						ssInPatt.put(ssid, 0);
					}
				}
				int numSSinPatt=ssInPatt.size();
				String line = agp.getUniqueActivityId()+","+patt.getInitialProb()+",Patt_"+npatt+","+numSSinPatt;
				//add list of SS in Patt
				List<Float> iniProbSSinPatt=patt.getSsIniProbInPattern();
				List<Float> shortiniProbSSinPatt=new ArrayList<Float>();
				List<Integer> allowedSS=new ArrayList<Integer>();
				for(Integer ssid:ssInPatt.keySet()){
					line+=","+ssid;
					int ssidInt=Integer.valueOf(ssid);
					if(ssidInt!=0){
						allowedSS.add(ssidInt);
						shortiniProbSSinPatt.add(iniProbSSinPatt.get(ssidInt-1));
					}
				}
				for(Float iniss:shortiniProbSSinPatt){
					line+=","+iniss.toString();
				}
				float[][] ssProbMatrix=patt.getSSProbMatrix();
				for(int row=0;row<allowedSS.size();row++){
					for(int col=0;col<allowedSS.size();col++){
						int indR=allowedSS.get(row);
						int indC=allowedSS.get(col);
						if((indR<=ssProbMatrix.length)&&(indC<=ssProbMatrix[0].length)){
							line+=","+ssProbMatrix[indR-1][indC-1];
						}else{
							System.out.println("Err indR: "+indR+" indC: "+indC+" matR: "+ssProbMatrix.length+" matC:"+ssProbMatrix[0].length);
						}
					}
				}
				
				writerActivity.write(line);
				writerActivity.newLine();
			}
			writerActivity.close();
		}}
	}
	private void exportSensorsets() throws IOException{
		// idSensorset, maxDuration, expectedValueTimeDistr , list of the ids of the activated sensors
		File folder = new File(directoryOutput);
		if (!folder.exists()) {
			throw new NotDirectoryException(null);
		}

		//
		BufferedWriter writerSS = new BufferedWriter(new FileWriter(directoryOutput+"/sensorsets.conf"));
		for (HSensorset ss: this.parameters.getSensorsets()){
			if(!ss.getUniqueSensorsetId().equals(0)){
				List<Integer> as= ss.getActivatedSensorsId();
				String line = ss.getUniqueSensorsetId().toString();
				line+=","+ss.getMaxDuration();
				line+=","+ss.getExpValTimeDist();
				for (Integer column : as){
					line += ","+column;
				}
				writerSS.write(line);
				writerSS.newLine();
			}
		}
		writerSS.close();
	}


	private void exportRhythm() throws IOException{
		/*
		 * 
		 * NO MORE USED
		 * 
		 * 
		 */
		File folder = new File(directoryOutput);
		if (!folder.exists()) {
			throw new NotDirectoryException(null);
		}

		//	save Rhythm
		BufferedWriter writerRhythm = new BufferedWriter(new FileWriter(directoryOutput+"/activityRhythm.conf"));
		for (ActivityGP agp: this.parameters.getActivities()){
			if(!agp.getUniqueActivityId().equals(0)){
				List<Float> rhythm= agp.getRhythm();
				String line = agp.getUniqueActivityId()+","+rhythm.size();
				for (Float column : rhythm){
					line += ","+column;
				}
				writerRhythm.write(line);
				writerRhythm.newLine();
			}
		}
		writerRhythm.close();
	}

	private void computeTimeDistribution() {
		System.out.println("Computing durations");
		for(DayGP daygp:this.parameters.getDays()){
			String[] secId=daygp.getSSid();
			String precValue=secId[0];
			Integer duration=0;
			for(int pos=0;pos<secId.length;pos++){
				if(secId[pos].equals(precValue)){
					duration++;
				}else{
					Integer id=Integer.parseInt(precValue);
					if(!id.equals(0)){
						HSensorset ss=this.parameters.getSensorsetByUniqueId(id);
						if(duration>0){
							ss.addDuration(duration);
						}
					}
					duration=0;
					precValue=secId[pos];
				}
			}
		}
		//found all of the durations -> now computing expected value and max duration
				for(HSensorset ss:this.parameters.getSensorsets()){
					if(!ss.getUniqueSensorsetId().equals(0)){
						Collections.sort(ss.getDurations());
						int maxV=0;
						int totalOccurrences=ss.getDurations().size();
						int dur=0;
						int occurrencesOfdur=0;
						if(totalOccurrences>0){
							dur=ss.getDurations().get(0);
							occurrencesOfdur=1;
						}
						float expV=0;
						int countOcc=0;
						for(Integer n:ss.getDurations()){
							countOcc++;
							if((n==dur)&&(totalOccurrences>1)&&(countOcc<totalOccurrences)){
								occurrencesOfdur++;
							}else{
								Float nf=(float) (((float) occurrencesOfdur/totalOccurrences)*dur);
								expV+=nf.floatValue();
								dur=n;
								occurrencesOfdur=1;
							}
							
							//compute max
							if(n>maxV){
								maxV=n;
							}
						}
						ss.setMaxDuration(maxV);
						ss.setExpValTimeDist(expV);
					}
				}
	}

	private void computeActivitiesRhytm() {
		/*
		 * 
		 * NO MORE USED
		 * 
		 * 
		 */
		System.out.println("Computing rhythm");
		for(ActivityGP agp:this.parameters.getActivities()){
			if(agp.getUniqueActivityId()!=0){
				Integer longerTimeDha=0;
				for(Pattern patt:agp.getPatterns()){
					for(DayHasActivityGP dha:patt.getDhasInCluster()){
						Integer duration=dha.getVectorChangeSS().length;
						if(duration>longerTimeDha){
							longerTimeDha=duration;
						}
					}
				}
				//System.out.println("Activity "+agp.getName()+" has longer duration "+longerTimeDha);
				//now I have the maximum duration for the current activity
				for(Pattern patt:agp.getPatterns()){
					float[] sumV=new float[longerTimeDha];
					for(int pos=0;pos<longerTimeDha;pos++){
						sumV[pos]=(float) 0;
					}
					for(DayHasActivityGP dha:patt.getDhasInCluster()){
						if(dha.getVectorChangeSS()!=null){
							float[] stretchedVector=stretchVector(dha.getVectorChangeSS(),longerTimeDha);
							sumV=sumVectors(sumV,stretchedVector);
						}
					}
					agp.setRhythm(computeDCT(sumV));
				}
			}
		}
	}

	private List<Float> computeDCT(float[] vector){
		List<Float> dct=new ArrayList<Float>();
		Integer vectorL=vector.length;
		Integer N=Math.round(vectorL/DCT_K);

		//first iteration: k=1
		Double insum= 0.0;
		for(int n=0;n<N;n++){
			int pos=(int) Math.floor((vectorL*n)/N);
			insum+=vector[pos];
		}
		Double yk=(double) ((1/Math.sqrt(N))*(insum));
		//other iterations
		Double wk= (Math.sqrt((double) 2/N));
		for(int k=2;k<=N;k++){
			insum= 0.0;
			for(int n=0;n<N;n++){
				int pos=(int) Math.floor((vectorL*n)/N);
				insum+=(vector[pos])*(Math.cos((Math.PI/(2*N))*((2*(n+1))-1)*(k-1)));
			}
			yk+=wk*insum;	
			dct.add(yk.floatValue());
		}
		return dct;
	}

	private float[] sumVectors(float[] sumV, float[] stretchedVector) {
		int l1=sumV.length;
		int l2=stretchedVector.length;
		float[] list=new float[l1];
		if(l1==l2){
			for(int pos=0;pos<l1;pos++){
				list[pos]=sumV[pos]+stretchedVector[pos];
			}
			return list;
		}
		return null;
	}

	private float[] stretchVector(int[] vectorChangeSS, int longerTimeDha) {
		Integer currentLength=vectorChangeSS.length;
		if(currentLength.equals(longerTimeDha)){
			float[] stretched=new float[longerTimeDha];
			for(int pos=0;pos<longerTimeDha;pos++){
				stretched[pos]= vectorChangeSS[pos];
			}
			return stretched;
		}
		if(currentLength<longerTimeDha){
			//make bigger
			float[] stretched=new float[longerTimeDha];
			stretched[0]=(float) vectorChangeSS[0];
			for(int pos=1;pos<longerTimeDha;pos++){
				stretched[pos]=(float) Math.floor((pos-1)/vectorChangeSS.length);
			}
			return stretched;
		}
		//otherwise 
		if(currentLength>longerTimeDha){
			//make shorter
			float[] stretched=new float[longerTimeDha];
			for(int pos=0;pos<longerTimeDha;pos++){
				int POS=(int) Math.floor((pos*currentLength)/longerTimeDha);
				stretched[pos]=(float) vectorChangeSS[POS];
			}
			return stretched;
		}
		System.out.println("currLength"+currentLength);
		System.out.println("stretch to"+longerTimeDha);
		System.out.println("Impossible stretch condition");
		return null;
	}

	private void computeSSiniProbInPattern() {
		System.out.println("Computing SS initial probobabilities for patterns");
		for(ActivityGP agp:this.parameters.getActivities()){
			if(!agp.getUniqueActivityId().equals(0))
			for(Pattern pattern:agp.getPatterns()){
				Integer sumOfDiagonal=0;
				int[][] m=pattern.getSStransMatrix();
				Integer numOfSS=this.parameters.getSensorsets().size();
				for(int row=0;row<numOfSS;row++){
					sumOfDiagonal+=m[row][row];
				}
				ArrayList<Float> pl=new ArrayList<Float>();
				for(int row=0;row<numOfSS;row++){
					pl.add((float) (m[row][row]/sumOfDiagonal));
				}
				//System.out.println("Pattern p of "+pattern.getActivity().getName() +" length ssinp "+pl.size());
				pattern.setSsIniProbInPattern(pl);
			}
		}
	}

	private void computePatternInitialProb() {
		System.out.println("Computing initial probability for patterns");
		for(ActivityGP agp:this.parameters.getActivities()){
			if(!agp.getUniqueActivityId().equals(0))
			for(Pattern pattern:agp.getPatterns()){
				Float p=(float) pattern.getDhasInCluster().size()/agp.getDhaInActivity();
				pattern.setInitialProb(p);
				//System.out.println("act "+agp.getName()+" patt "+pattern.getUniqueIdPattern()+" dha inside "+pattern.getDhasInCluster().size()+" ini prob: "+p.toString());
			}
		}

	}

	private void computeProbMatrices() throws Exception {
		System.out.println("Computing prob transitions matrices");
		//compute the overallProbSS from the overallTransitionSS
		this.parameters.setOverallProbSS(normalizeByRow(this.parameters.getOverallTransitionSS()));
		//compute SSProbMatrix of every pattern using its SStransMatrix that is sum of SStransMatrix
		Integer numOfSS=this.parameters.getSensorsets().size();
		for(ActivityGP agp:this.parameters.getActivities()){
			if(!agp.getUniqueActivityId().equals(0))
			for(Pattern pattern:agp.getPatterns()){	
				
				int[][] initialZeroMatrix=new int[numOfSS][numOfSS];
				//initialZeroMatrix=initializeMatrix(initialZeroMatrix,numOfSS);
				pattern.setSStransMatrix(initialZeroMatrix);
				//System.out.println("Considering activity "+agp.getName()+" with "+agp.getPatterns().size()+" patterns");
				for(DayHasActivity dha:pattern.getDhasInCluster()){
					if(!dha.getActivity().getUniqueActivityId().equals(0)){
						//System.out.println("sum");
						pattern.setSStransMatrix(sumOfMatrices(pattern.getSStransMatrix(),dha.getSStransMatrix()));
					}
				}
				pattern.setSSProbMatrix(normalizeByRow(pattern.getSStransMatrix()));
			}
		}
	}

	private int[][] sumOfMatrices(int[][] m1,int[][] m2) throws Exception{
		int d11=m1.length;
		int d12=m2.length;
		int d21=m1[0].length;
		int d22=m2[0].length;
		int[][] m3=new int[d11][d21];
		if((d11==d12)&&(d21==d22)){
			for(int row=0;row<d11;row++){
				for(int col=0;col<d21;col++){
					m3[row][col]=m1[row][col]+m2[row][col];
				}
			}
			return m3;
		}else{
			throw new Exception("Can not sum matrices with different length");	
		}
	}

	private float[][] normalizeByRow(int[][] m) throws Exception{
		if(m==null){
			throw new Exception("Can not normalize empty matrix");
		}
		int numRow=m.length;
		int numCol=m[0].length;
		float[][] m2=new float[numRow][numCol];
		for(int row=0;row<numRow;row++){
			Integer sumRow=0;
			for(int col=0;col<numCol;col++){
				sumRow+=m[row][col];
			}
			sumRow = Math.max(1, sumRow);
			for(int col=0;col<numCol;col++){
				m2[row][col]=(float) m[row][col]/sumRow;
				if(m2[row][col]==(0.0)){
					m2[row][col]=(float) 0.0001;
				}
			}
		}
		return m2;
	}

	private Integer[][] initializeMatrix(Integer[][] matr, Integer numUniqueSS){
		for(int row=0;row<numUniqueSS;row++){
			for(int col=0;col<numUniqueSS;col++){
				matr[row][col]=new Integer(0);
			}
		}
		return matr;
	}

	private void setTransitionMatrices(){
		System.out.println("Computing all the SS transition matrices");
		//initialization
		Integer numUniqueSS=this.parameters.getSensorsets().size();
		int[][] allTransSS=new int[numUniqueSS][numUniqueSS];

		for(Resident r:this.parameters.getResidents()){
			Integer previousSSid=0;
			Integer currentSSid=0;
			for(DayGP daygp:this.parameters.getDays()){
				if(daygp.resident.getUniqueResidentId()==r.getUniqueResidentId()){
					String[] ssidSecond=daygp.getSSid();
					for(DayHasActivity dha: daygp.getDailyActivities()){
						//System.out.println("computing tp day "+daygp.getIncrementalDay()+" dha start time"+dha.getStartSec());
						if(!dha.getActivity().getUniqueActivityId().equals(0)){
						int[][] dhaTransSS=new int[numUniqueSS][numUniqueSS];
						//transition from previous activity
						currentSSid=Integer.valueOf(ssidSecond[dha.getStartSec()]);
						if((currentSSid!=0)&&(previousSSid!=0)){
							Integer prev=allTransSS[previousSSid-1][currentSSid-1];
							allTransSS[previousSSid-1][currentSSid-1]=prev+1;
						}
						previousSSid=currentSSid;
						//inside dha loop... all the transition inside
						for(Integer sec=dha.getStartSec();sec<dha.getEndSec();sec++){
							currentSSid=Integer.valueOf(ssidSecond[sec]);
							if((currentSSid!=0)&&(previousSSid!=0)){
								Integer prev=allTransSS[previousSSid-1][currentSSid-1];
								allTransSS[previousSSid-1][currentSSid-1]=prev+1;
								prev=dhaTransSS[previousSSid-1][currentSSid-1];
								dhaTransSS[previousSSid-1][currentSSid-1]=prev+1;
							}
							previousSSid=currentSSid;
						}
						/*
						for(int i=0; i<dhaTransSS.length;i++){
							for(int ii=0;ii<dhaTransSS[i].length;ii++){
								if(dhaTransSS[i][ii]!=0)
									System.out.println(i+","+ii+" -> "+dhaTransSS[i][ii]+"-");
							}
						}*/
						dha.setSStransMatrix(dhaTransSS);
						}
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
				//for dha create the vector of ss changes
				String[] daySSid=daygp.getSSid();
				Integer[] ssChanges=new Integer[86400];
				String prev="0";
				for(int pos=0;pos<daySSid.length;pos++){
					if(daySSid[pos].equals(prev)){
						ssChanges[pos]=0;
					}else{
						ssChanges[pos]=1;
					}
					prev=daySSid[pos];
				}		
				List<DayHasActivity> dhaRes=new ArrayList<DayHasActivity>();
				String[] ids=daygp.getSSid();
				Integer[] idsInt=new Integer[86400];
				Integer okSec=0;
				Integer koSec=0;
				for(DayHasActivity dha:realDay.getDailyActivities()){
					if(dha.getResident().getUniqueResidentId()==res.getUniqueResidentId()){

						Integer performedSubActivityId=dha.getActivity().getUniqueActivityId();
						ActivityGP agp=this.getParameters().getActivityGpBySubActId(performedSubActivityId);
						if((agp==null)||(agp.getUniqueActivityId().equals(0))){
							agp=this.parameters.getActivityGpByIdGP(0);
						}
						dhaUniqueId++;
						DayHasActivityGP dhaNew= new DayHasActivityGP(0,dhaUniqueId,dha.getStartSec(),dha.getEndSec(),agp,dha.getResident());
						//System.out.println("end "+dha.getEndSec());
						//System.out.println("start "+dha.getStartSec());
						//System.out.println("diff "+(dha.getEndSec()-dha.getStartSec()));
						int[] vChangeSS=new int[(dha.getEndSec()-dha.getStartSec())+1];
						for(Integer s=dha.getStartSec();s<dha.getEndSec();s++){
							vChangeSS[s-dha.getStartSec()]=ssChanges[s];
						}
						dhaNew.setVectorChangeSS(vChangeSS);						
						dhaRes.add(dhaNew);
						Integer startSec=dha.getStartSec();
						Integer endSec=dha.getEndSec();
						for(Integer sec=startSec;sec<=endSec;sec++){
							//filtering data
							if(agp.getUniqueActivityId()!=0){
								Integer previousSSId=Integer.parseInt(ids[sec]);
								dhaNew.addUsedSSId(previousSSId);
							}
						}
					}
				}
				System.out.println("Total seconds parsed: "+(okSec+koSec)+" -> not accepted seconds: "+koSec);	
				daygp.setSSid(realDay.getSecondIdSS());
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
					ParametersHandlerK.getInstance().getParameters().addResident(this.house.getResidentByUniqueId(Integer.valueOf(id)));
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
						subactivities.add(this.house.getActivityByUniqueId(Integer.valueOf(subactivityId)));
					}
				}
				//			second line stands for sensors
				if (count == 2){
					List<String >allowedSensorIds =  Arrays.asList(string.split(","));
					for(String allowedSensorId : allowedSensorIds){
						allowedSensors.add(this.house.getSensorByUniqueId(Integer.valueOf(allowedSensorId)));
					}
					break;
				}
			}
			readerActivities.close();
			ActivityGP newActivity = new ActivityGP(0, i+1, activityName, subactivities, allowedSensors);
			this.parameters.addActivity(newActivity);
		}
		ActivityGP a=new ActivityGP(0,0,"DO NOT CONSIDER",new ArrayList<Activity>(),new ArrayList<HSensor>());
		this.parameters.addActivity(a);
		this.parameters.setSensorsets(house.getSensorsets());
		this.parameters.setSensors(house.getSensors());
	}

}




