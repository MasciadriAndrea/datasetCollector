package specificParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import persistance.DatasetDAOSql;
import common.DbManager;
import dataModel.Activity;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.HSensor;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Location;
import dataModel.Resident;
import dataModel.SensorTime;
import dataModel.SensorType;

public class KasterenParser extends GenericParser {

	private static String urlData;
	private static String fileSensor="/sensors.txt";
	private static String fileActivity="/activities.txt";
	private static String fileas="/as.txt";
	private static String filess="/ss.txt";
	private static KasterenParser instance;

	private KasterenParser(){
		super();
	}

	public static KasterenParser getInstance(){
		if(instance==null){
			instance=new KasterenParser();
		}
		return instance;
	}
	
	public void deleteDatasetById(Integer id){
		try{
			System.out.println("Try to connect to database for data persistance");
			DbManager db=DbManager.getInstance();
			DatasetDAOSql dsDAO=DatasetDAOSql.getInstance();
			System.out.println("Connection ok.. deleting");
			dsDAO.deleteDataset(id);
			System.out.println("dataset deleted");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void saveDataset(){
		try{
			System.out.println("Try to connect to database for data persistance");
			DbManager db=DbManager.getInstance();
			DatasetDAOSql dsDAO=DatasetDAOSql.getInstance();
			System.out.println("Connection ok.. now importing.. (wait please)");
			this.ds=dsDAO.updateDataset(this.ds);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void updateHouseData(String nameDs,String nameHouse,String pathN){
		this.urlData=pathN;
		createDataset(0,nameDs);
		House h=createHouse(nameHouse);
		h.setActivities(getActivityList());
		h.setResidents(getResidentList());
		//get Locations is performed by getSensors
		//h.setLocations(getLocationList());
		h.setSensorTypes(getSensorTypeList());
		h.setSensors(getSensorList());
		h.setDays(getDayList());
	}

	@Override
	public List<Activity> getActivityList()  {
		List<Activity> al=new ArrayList<Activity>();
		try{
			File folder = new File(urlData);
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}

			//read the conf file
			File currentFile = new File(urlData+fileActivity);
			BufferedReader reader;
			if (!currentFile.exists()) {
				throw new FileNotFoundException(null);
			}
			ArrayList<String> configLines = new ArrayList<String>();
			reader = new BufferedReader(new FileReader(currentFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				configLines.add(line);
			}
			reader.close();

			//scan the line to get parameters
			Integer c=0;
			for (String pattern : configLines) {
				if(pattern.length()>0){
					c++;
					pattern=pattern.replaceAll("'", "");
					al.add(new Activity(0,c,pattern));			
				}
			}
			System.out.println("Imported "+c.toString()+" activities");
		}catch(IOException e){
			e.printStackTrace();
		}
		return al;
	}

	@Override
	public List<Resident> getResidentList() {
		List<Resident> rl=new ArrayList<Resident>();
		rl.add(new Resident(0,0,1));
		return rl;
	}

	@Override
	public List<Location> getLocationList() {
		return null;
	}

	@Override
	public List<SensorType> getSensorTypeList() {
		//TODO
		SensorType st=new SensorType(0, 1, "default");
		List<SensorType> lst=new ArrayList<SensorType>();
		lst.add(st);
		return lst;
	}

	@Override
	public List<HSensor> getSensorList() {
		List<HSensor> sl=new ArrayList<HSensor>();
		try{
			File folder = new File(urlData);
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}

			//read the conf file
			File currentFile = new File(urlData+fileSensor);
			BufferedReader reader;
			if (!currentFile.exists()) {
				throw new FileNotFoundException(null);
			}
			ArrayList<String> configLines = new ArrayList<String>();
			reader = new BufferedReader(new FileReader(currentFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				configLines.add(line);
			}
			reader.close();

			//scan the line to get parameters
			Integer c=0;
			for (String pattern : configLines) {
				String[] chunks = pattern.split("	");
				if(chunks.length>2){
					//since there is no Location and sensor type using standard one
					House h=super.getDataset().getHouses().get(0);
					String locName=chunks[2];
					Location loc=h.getLocationByName(locName);
					SensorType st=h.getSensorTypeByUniqueId(1);
					sl.add(new HSensor(0, Integer.valueOf(chunks[0]), chunks[1], "0", "0", st, loc));
					c++;
				}
			}
			System.out.println("Imported "+c.toString()+" sensors");
		}catch(IOException e){
			e.printStackTrace();
		}
		return sl;
	}

	@Override
	public List<HSensorset> getSensorsetList() {
		List<HSensorset> listSS=new ArrayList<HSensorset>();
		return listSS;
	}


	@Override
	public List<Day> getDayList() {	
		House h=super.getDataset().getHouses().get(0);
		int numSensors=h.getSensors().size();
		Resident r=h.getResidentByUniqueId(1);
		try{
			File folder = new File(urlData);
			if(!folder.exists()){
				throw new NotDirectoryException(null);
			}


			//------insert Day and DHA
			//read the conf file
			File currentFile = new File(urlData+fileas);
			BufferedReader reader;
			if (!currentFile.exists()) {
				throw new FileNotFoundException(null);
			}
			Map<Integer, List<String>> configStruct=new HashMap<Integer,List<String>>();
			reader = new BufferedReader(new FileReader(currentFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] chunks = line.split("	");
				if(chunks.length>2){
					String datestartstr=chunks[0];
					String dateendstr=chunks[1];
					Integer actId=Integer.parseInt(chunks[2]);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
					Date datestart=simpleDateFormat.parse(datestartstr);
					Date dateend=simpleDateFormat.parse(dateendstr);			
					Calendar cal = Calendar.getInstance();
			        cal.setTime(datestart);
					Integer day1=cal.get(Calendar.DAY_OF_MONTH);
					cal.setTime(dateend);
					Integer day2=cal.get(Calendar.DAY_OF_MONTH);
					if(!day1.equals(day2)){
						cal.setTime(datestart);
						String line1=simpleDateFormat.format(cal.getTime())+"	";
						cal.set(Calendar.SECOND,59);
						cal.set(Calendar.MINUTE,59);
						cal.set(Calendar.HOUR_OF_DAY,23);
						line1+=simpleDateFormat.format(cal.getTime())+"	"+actId;
						cal.setTime(dateend);
						cal.set(Calendar.SECOND,0);
						cal.set(Calendar.MINUTE,0);
						cal.set(Calendar.HOUR_OF_DAY,0);
						String line2=simpleDateFormat.format(cal.getTime())+"	";
						cal.setTime(dateend);
						line2+=simpleDateFormat.format(cal.getTime())+"	"+actId;
						if(day2-day1>1){
							//there are more days in the middle
							for(int i=1;i<day2-day1;i++){
								int dayn=day1+i;
								cal.setTime(datestart);
								cal.set(Calendar.SECOND,0);
								cal.set(Calendar.MINUTE,0);
								cal.set(Calendar.HOUR_OF_DAY,0);
								cal.set(Calendar.DAY_OF_MONTH,dayn);
								String linen=simpleDateFormat.format(cal.getTime())+"	";
								cal.setTime(datestart);
								cal.set(Calendar.SECOND,59);
								cal.set(Calendar.MINUTE,59);
								cal.set(Calendar.HOUR_OF_DAY,23);
								cal.set(Calendar.DAY_OF_MONTH,dayn);
								linen+=simpleDateFormat.format(cal.getTime())+"	"+actId;
								System.out.println("modified line n = "+linen);
								int indD=cal.get(Calendar.DAY_OF_YEAR);
								if(!configStruct.containsKey(indD)){
									configStruct.put(indD, new ArrayList<String>());
								}
								configStruct.get(indD).add(linen);
							}
						}
						System.out.println("modified line1= "+line1);
						System.out.println("modified line2= "+line2);
						System.out.println("--- ");
						cal.setTime(datestart);
						int indD=cal.get(Calendar.DAY_OF_YEAR);
						if(!configStruct.containsKey(indD)){
							configStruct.put(indD, new ArrayList<String>());
						}
						configStruct.get(indD).add(line1);
						cal.setTime(dateend);
						indD=cal.get(Calendar.DAY_OF_YEAR);
						if(!configStruct.containsKey(indD)){
							configStruct.put(indD, new ArrayList<String>());
						}
						configStruct.get(indD).add(line2);
					}else{
						cal.setTime(datestart);
						int indD=cal.get(Calendar.DAY_OF_YEAR);
						if(!configStruct.containsKey(indD)){
							configStruct.put(indD, new ArrayList<String>());
						}
						configStruct.get(indD).add(line);
					}
				}
			}
			reader.close();
			List<String> configLines=new ArrayList<String>();
			for(Entry<Integer, List<String>> ent:configStruct.entrySet()){
				for(String lin:ent.getValue()){
					configLines.add(lin);
				}
			}
			
			for (String pattern : configLines) {
				String[] chunks = pattern.split("	");
				if(chunks.length>2){
					String datestartstr=chunks[0];
					String dateendstr=chunks[1];
					Integer actId=Integer.parseInt(chunks[2]);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
					Date datestart=simpleDateFormat.parse(datestartstr);
					Date dateend=simpleDateFormat.parse(dateendstr);	
					Calendar cal = Calendar.getInstance();
			        cal.setTime(datestart);
					String day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
					String month=String.valueOf(cal.get(Calendar.MONTH)+1);
					String year=String.valueOf(cal.get(Calendar.YEAR));
					Integer incrementalDay=h.getIncrementalDayIdByDate(day, month, year);
					Day currentDay=null;
					if(incrementalDay.equals(0)){
						//new day
						incrementalDay=h.getDays().size()+1;
						System.out.println("Adding incremental day "+incrementalDay+" : day "+day+" month "+month+" year "+year);
						currentDay=new Day(0,incrementalDay,day,month,year,"");
						h.getDays().add(currentDay);
					}
					currentDay=h.getDayByIncremental(incrementalDay);
					Integer startsec=cal.get(Calendar.SECOND)+cal.get(Calendar.MINUTE)*60+cal.get(Calendar.HOUR_OF_DAY)*3600;
					cal.setTime(dateend);
					Integer endsec=cal.get(Calendar.SECOND)+cal.get(Calendar.MINUTE)*60+cal.get(Calendar.HOUR_OF_DAY)*3600;
					Activity a=h.getActivityByUniqueId(actId);
					currentDay.getDailyActivities().add(new DayHasActivity(0,startsec,endsec,a,r));
				}
			}

			//------insert secondDay and ss
			//read the conf file
			currentFile = new File(urlData+filess);
			if (!currentFile.exists()) {
				throw new FileNotFoundException(null);
			}
			configStruct=new HashMap<Integer,List<String>>();
			reader = new BufferedReader(new FileReader(currentFile));
			line = null;
			while ((line = reader.readLine()) != null) {
				String[] chunks = line.split("	");
				if(chunks.length>2){
					String datestartstr=chunks[0];
					String dateendstr=chunks[1];
					Integer ssId=Integer.parseInt(chunks[2]);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
					Date datestart=simpleDateFormat.parse(datestartstr);
					Date dateend=simpleDateFormat.parse(dateendstr);			
					Calendar cal = Calendar.getInstance();
			        cal.setTime(datestart);
					Integer day1=cal.get(Calendar.DAY_OF_MONTH);
					cal.setTime(dateend);
					Integer day2=cal.get(Calendar.DAY_OF_MONTH);
					if(!day1.equals(day2)){
						cal.setTime(datestart);
						String line1=simpleDateFormat.format(cal.getTime())+"	";
						cal.set(Calendar.SECOND,59);
						cal.set(Calendar.MINUTE,59);
						cal.set(Calendar.HOUR_OF_DAY,23);
						line1+=simpleDateFormat.format(cal.getTime())+"	"+ssId;
						cal.setTime(dateend);
						cal.set(Calendar.SECOND,0);
						cal.set(Calendar.MINUTE,0);
						cal.set(Calendar.HOUR_OF_DAY,0);
						String line2=simpleDateFormat.format(cal.getTime())+"	";
						cal.setTime(dateend);
						line2+=simpleDateFormat.format(cal.getTime())+"	"+ssId;
						if(day2-day1>1){
							//there are more days in the middle
							for(int i=1;i<day2-day1;i++){
								int dayn=day1+i;
								cal.setTime(datestart);
								cal.set(Calendar.SECOND,0);
								cal.set(Calendar.MINUTE,0);
								cal.set(Calendar.HOUR_OF_DAY,0);
								cal.set(Calendar.DAY_OF_MONTH,dayn);
								String linen=simpleDateFormat.format(cal.getTime())+"	";
								cal.setTime(datestart);
								cal.set(Calendar.SECOND,59);
								cal.set(Calendar.MINUTE,59);
								cal.set(Calendar.HOUR_OF_DAY,23);
								cal.set(Calendar.DAY_OF_MONTH,dayn);
								linen+=simpleDateFormat.format(cal.getTime())+"	"+ssId;
								System.out.println("modified line n = "+linen);
								int indD=cal.get(Calendar.DAY_OF_YEAR);
								if(!configStruct.containsKey(indD)){
									configStruct.put(indD, new ArrayList<String>());
								}
								configStruct.get(indD).add(linen);
							}
						}
						System.out.println("modified line1= "+line1);
						System.out.println("modified line2= "+line2);
						System.out.println("--- ");
						cal.setTime(datestart);
						int indD=cal.get(Calendar.DAY_OF_YEAR);
						if(!configStruct.containsKey(indD)){
							configStruct.put(indD, new ArrayList<String>());
						}
						configStruct.get(indD).add(line1);
						cal.setTime(dateend);
						indD=cal.get(Calendar.DAY_OF_YEAR);
						if(!configStruct.containsKey(indD)){
							configStruct.put(indD, new ArrayList<String>());
						}
						configStruct.get(indD).add(line2);
					}else{
						cal.setTime(datestart);
						int indD=cal.get(Calendar.DAY_OF_YEAR);
						if(!configStruct.containsKey(indD)){
							configStruct.put(indD, new ArrayList<String>());
						}
						configStruct.get(indD).add(line);
					}
				}
			}
			reader.close();
			int[][] ssDay=new int[86400][h.getSensors().size()];
			Day lastAnalizedDay=null;
			Day currentDay=null;
			int previousDay=0;
			configLines=new ArrayList<String>();
			for(Entry<Integer, List<String>> ent:configStruct.entrySet()){
				for(String lin:ent.getValue()){
					configLines.add(lin);
				}
			}
			for (String pattern : configLines) {
				String[] chunks = pattern.split("	");
				if(chunks.length>2){
					String datestartstr=chunks[0];
					String dateendstr=chunks[1];
					Integer ssId=Integer.parseInt(chunks[2]);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
					Date datestart=simpleDateFormat.parse(datestartstr);
					Date dateend=simpleDateFormat.parse(dateendstr);			
					Calendar cal = Calendar.getInstance();
			        cal.setTime(datestart);
					String day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
					String month=String.valueOf(cal.get(Calendar.MONTH)+1);
					String year=String.valueOf(cal.get(Calendar.YEAR));
					int incrementalDay=h.getIncrementalDayIdByDate(day, month, year);
					if(incrementalDay!=previousDay){
						previousDay=incrementalDay;
						System.out.println("");
						 System.out.println(h.getSensorsets().size()+" unique sensorsets created");
						System.out.println("Creating SS for day "+incrementalDay+" : day "+day+" month "+month+" year "+year);
					}
					currentDay=h.getDayByIncremental(incrementalDay);
					if(lastAnalizedDay==null) lastAnalizedDay=currentDay;
					if(!lastAnalizedDay.equals(currentDay)){
						//save the current date
						String secondIdSS="";
						for(int sec=0;sec<86400;sec++){
							List<SensorTime> listST=new ArrayList<SensorTime>();   
			                HSensor sens=null;
			                String value="";
			                SensorTime st=null;
			                for(Integer i=0;i<h.getSensors().size();i++){
			                	sens=h.getSensors().get(i);
			                	value=String.valueOf(ssDay[sec][i]);
			                	st=new SensorTime(0,sens,value);
			                	listST.add(st);
			                }
			                //list of SensorTime ready... if any sensorset has the same list -> add to Sensorsets list
			                HSensorset ss=getUniqueSS(h,listST,numSensors);
			                secondIdSS+=ss.getUniqueSensorsetId()+",";
						}
						
						 secondIdSS=secondIdSS.substring(0,secondIdSS.length()-1);
						 lastAnalizedDay.setSecondIdSS(secondIdSS);
						 lastAnalizedDay=currentDay;
						//new day
						ssDay=new int[86400][h.getSensors().size()];
					}else{
						//still in the same day
						Integer startsec=cal.get(Calendar.SECOND)+cal.get(Calendar.MINUTE)*60+cal.get(Calendar.HOUR_OF_DAY)*3600;
						cal.setTime(dateend);
						Integer endsec=cal.get(Calendar.SECOND)+cal.get(Calendar.MINUTE)*60+cal.get(Calendar.HOUR_OF_DAY)*3600;
						for(int sec=startsec;sec<=endsec;sec++){
							int pos=getPositionFromId(h.getSensors(),ssId);
							ssDay[sec][pos]=1;
						}
					}
				}
				System.out.print(".");
			}
			//save the last day
			String secondIdSS="";
			for(int sec=0;sec<86400;sec++){
				List<SensorTime> listST=new ArrayList<SensorTime>();   
                HSensor sens=null;
                String value="";
                SensorTime st=null;
                for(Integer i=0;i<h.getSensors().size();i++){
                	sens=h.getSensors().get(i);
                	value=String.valueOf(ssDay[sec][i]);
                	st=new SensorTime(0,sens,value);
                	listST.add(st);
                }
                //list of SensorTime ready... if any sensorset has the same list -> add to Sensorsets list
                HSensorset ss=getUniqueSS(h,listST,numSensors);
                secondIdSS+=ss.getUniqueSensorsetId()+",";
			}
			 secondIdSS=secondIdSS.substring(0,secondIdSS.length()-1);
			 currentDay.setSecondIdSS(secondIdSS);
			 System.out.println(h.getSensorsets().size()+" unique sensorsets created");
			 
		}catch(Exception e){
			e.printStackTrace();
		}



		return h.getDays();
	}

	private int getPositionFromId(List<HSensor> sensors, Integer ssId) {
		int pos=0;
		for(HSensor s:sensors){
			if(s.getUniqueSensorId().equals(ssId)){
				return pos;
			}
			pos=pos+1;
		}
		return 0;
	}

	private HSensorset getUniqueSS(House h,List<SensorTime> listST,int numSensors){
		List<HSensorset> lss=h.getSensorsets();
		Integer numSS=lss.size();
		Boolean foundSS=false;
		HSensorset SSfound=null;
		for(HSensorset ss:lss){
			List<SensorTime> lst=ss.getSensors();
			if(lst.size()!=numSensors){
				System.out.println("Strange number of Sensors in SS");
			}else{
				Integer numEqual=0;
				for(Integer sid=1;sid<=numSensors;sid++){
					SensorTime stSS=lst.get(sid-1);
					SensorTime stCompare=listST.get(sid-1);
					if(stSS.getValue().equals(stCompare.getValue())){
						if(stSS.getSensor().equals(stCompare.getSensor())){
							numEqual++;
						}
					}else{
						break;
					}
				}
				if(numEqual.equals(numSensors)){
					foundSS=true;
					SSfound=ss;
					break;
				}
			}
		}
		if(!foundSS){
			HSensorset hss=new HSensorset(0,numSS+1,listST);
			lss.add(hss);
			h.setSensorsets(lss);
			SSfound=hss;
		}
		return SSfound;
	}

	@Override
	public List<SensorTime> getSensorTimeList(Integer uniqueSensorsetId) {
		List<SensorTime> lst=new ArrayList<SensorTime>();
		return lst;
	}


	@Override
	public List<DayHasActivity> getDayHasActivityList(Integer incrementalDay) {
		List<DayHasActivity> dha=new ArrayList<DayHasActivity>();
		return dha;
	}

}
