package specificParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import persistance.DatasetDAOSql;
import common.DbManager;
import dataModel.Activity;
import dataModel.Dataset;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.HSensor;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Location;
import dataModel.Resident;
import dataModel.SensorTime;
import dataModel.SensorType;

public class ArasParser extends GenericParser {
	
	private static String urlData;
	private static String folderConf="/confFile";
	private static String fileSensor="/sensors.txt";
	private static String fileActivity="/activities.txt";
	private static String fileResident="/residents.txt";
	private static String filePRE="DAY";
	private static ArasParser instance;
	private int numSensor;
	
	private ArasParser(){
		super();
		numSensor=0;
	}

	public static ArasParser getInstance(){
		if(instance==null){
			instance=new ArasParser();
		}
		return instance;
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
		createDataset(0,nameDs);
		this.urlData=pathN;
		House h=createHouse(nameHouse);
		h.setActivities(getActivityList());
		h.setResidents(getResidentList());
		h.setLocations(getLocationList());
		h.setSensorTypes(getSensorTypeList());
		h.setSensors(getSensorList());
		h.setDays(getDayList());
	}

	@Override
	public List<Activity> getActivityList()  {
		List<Activity> al=new ArrayList<Activity>();
		try{
			File folder = new File(urlData+folderConf);
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}
			
			//read the conf file
			File currentFile = new File(urlData+folderConf+fileActivity);
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
				String[] chunks = pattern.split(",");
				if(chunks.length>1){
					al.add(new Activity(0,Integer.valueOf(chunks[0]),chunks[1]));
					c++;
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
		try{
			File folder = new File(urlData+folderConf);
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}
			
			//read the conf file
			File currentFile = new File(urlData+folderConf+fileResident);
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
				String[] chunks = pattern.split(",");
				if(chunks.length>1){
					Integer uid=Integer.valueOf(chunks[0]);
					Integer age=Integer.valueOf(chunks[1]);
					rl.add(new Resident(0,age,uid));
					c++;
				}
			}
			System.out.println("Imported "+c.toString()+" residents");
		}catch(IOException e){
			e.printStackTrace();
		}
		return rl;
	}

	@Override
	public List<Location> getLocationList() {
		Location l=new Location(0,1,"default");
		List<Location> ll=new ArrayList<Location>();
		ll.add(l);
		return ll;
	}
	
	@Override
	public List<SensorType> getSensorTypeList() {
		SensorType st=new SensorType(0, 1, "default");
		List<SensorType> lst=new ArrayList<SensorType>();
		lst.add(st);
		return lst;
	}

	@Override
	public List<HSensor> getSensorList() {
		List<HSensor> sl=new ArrayList<HSensor>();
		try{
			File folder = new File(urlData+folderConf);
			if (!folder.exists()) {
				throw new NotDirectoryException(null);
			}
			
			//read the conf file
			File currentFile = new File(urlData+folderConf+fileSensor);
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
				String[] chunks = pattern.split(",");
				if(chunks.length>2){
					//since there is no Location and sensor type using standard one
					House h=super.getDataset().getHouses().get(0);
					Location loc=h.getLocationByUniqueId(1);
					SensorType st=h.getSensorTypeByUniqueId(1);
					sl.add(new HSensor(0, Integer.valueOf(chunks[0]), chunks[1], chunks[2], chunks[3], st, loc));
					c++;
				}
			}
			this.numSensor=c;
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
		List<Day> ld=new ArrayList<Day>();
		House h=super.getDataset().getHouses().get(0);
		try{
		File folder = new File(urlData);
        if(!folder.exists()){
            throw new NotDirectoryException(null);
        }

        BufferedReader reader = null;

        FilenameFilter ActFilter = (dir, name) -> {
            if(name.lastIndexOf('.')>0){
                if(name.contains("_")){
                        int lastIndexDot = name.lastIndexOf('.');
                        int lastIndexUnSl = name.lastIndexOf('_');
                        String filename = name.subSequence(0, lastIndexUnSl).toString();
                        String extension = name.substring(lastIndexDot);
                        if(filename.equals(filePRE) && extension.equals(".txt")){
                            return true;
                        }
                }
            }
            return false;
        };
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(folder.listFiles(ActFilter)));
        if(fileList.isEmpty()){
            throw new FileNotFoundException(null);
        }
        //order the file list
        ArrayList<File> orderedList=new ArrayList<File>();
        for(Integer i=0;i<=fileList.size();i++){
        	for (File CurrentFile : fileList) {
        		if(CurrentFile.getName().equals(filePRE+"_"+i.toString()+".txt")){
        			orderedList.add(CurrentFile);
        		}
        	}
        }
        if(fileList.size()!=(orderedList.size())){
        	throw new Exception("Dimension of list of files wrong");
        }
        fileList=null;
        Integer nFile=0;
       for (File CurrentFile : orderedList) {
    	   nFile++;
            ArrayList<String> configLines = new ArrayList<String>();
            try {
                reader = new BufferedReader(new FileReader(CurrentFile));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    configLines.add(line);
                }
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            
            List<DayHasActivity> ldha=new ArrayList<DayHasActivity>();
            String secondIdSS="";
            
            Integer previousActivity1=0;
            Integer currentActivity1=0;
            Integer startSec1=0;
            Integer endSec1=0;
            Integer previousActivity2=0;
            Integer currentActivity2=0;
            Integer startSec2=0;
            Integer endSec2=0;
            
            Integer secondtime=0;
            Resident resident1=h.getResidents().get(0);
            Resident resident2=null;
            if(h.getResidents().size()>1){
            	resident2=h.getResidents().get(1);
            }
            	
            for(String pattern:configLines){
            	//for every line -> it is a second
                String[] chunks = pattern.split(" ");
                if (chunks.length > numSensor+1){
                	if(secondtime.equals(0)){
                		//initialization
                		previousActivity1=Integer.parseInt(chunks[numSensor]);
                		startSec1=1;
                		endSec1=1;
                		previousActivity2=Integer.parseInt(chunks[numSensor+1]);
                		startSec2=1;
                		endSec2=1;
                	}
                	secondtime++;
                	//if the size is ok -> calculate list of sensorTime 
	                List<SensorTime> listST=new ArrayList<SensorTime>();   
	                Integer uniqueIdSensor=0;
	                HSensor sens=null;
	                String value="";
	                SensorTime st=null;
	                for(Integer i=0;i<numSensor;i++){
	                	sens=h.getSensors().get(i);
	                	value=chunks[i];
	                	st=new SensorTime(0,sens,value);
	                	listST.add(st);
	                }
	                //list of SensorTime ready... if any sensorset has the same list -> add to Sensorsets list
	                HSensorset ss=getUniqueSS(h,h.getSensorsets(),listST);
	                secondIdSS+=ss.getUniqueSensorsetId()+",";
	                currentActivity1=Integer.parseInt(chunks[numSensor]); 
	                currentActivity2=Integer.parseInt(chunks[numSensor+1]); 
	                if((!previousActivity1.equals(currentActivity1))||(secondtime==86400)){
	                	//if changed activity
	                	Activity currAct1=h.getActivityByUniqueId(previousActivity1);
		                ldha.add(new DayHasActivity(0,startSec1,endSec1,currAct1,resident1));
	                	startSec1=secondtime;
	                	endSec1=secondtime;
	                	previousActivity1=currentActivity1;
	                }else{
	                	//performing the same activity
	                	endSec1++;
	                }
	                
	                if(resident2!=null){
	                	if((!previousActivity2.equals(currentActivity2))||((secondtime==86400))){
		                	//if changed activity
		                	Activity currAct2=h.getActivityByUniqueId(previousActivity2);
			                ldha.add(new DayHasActivity(0,startSec2,endSec2,currAct2,resident2));
		                	startSec2=secondtime;
		                	endSec2=secondtime;
		                	previousActivity2=currentActivity2;
		                }else{
		                	//performing the same activity
		                	endSec2++;
		                }
	                }
	               
                }
                
            }
            secondIdSS=secondIdSS.substring(0,secondIdSS.length());
            //ARAS secondIdSS=secondIdSS.substring(0,secondIdSS.length()-1);
            Day currentDay=new Day(0,nFile,"","","",secondIdSS);
            currentDay.setDailyActivities(ldha);
            ld.add(currentDay);
            System.out.println("Finished importing day: "+CurrentFile.getName());
        } 
       	System.out.println("Number of unique SS found: "+h.getSensorsets().size());
        System.out.println("Scanned "+nFile+" files"); 
		}catch(IOException e){
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ld;
	}
	
	private HSensorset getUniqueSS(House h,List<HSensorset> lss,List<SensorTime> listST){
		Integer numSS=lss.size();
		Boolean foundSS=false;
		HSensorset SSfound=null;
		for(HSensorset ss:lss){
			List<SensorTime> lst=ss.getSensors();
				Integer numEqual=0;
				for(Integer sid=0;sid<lst.size();sid++){
					SensorTime stSS=lst.get(sid);
					SensorTime stCompare=listST.get(sid);
					if(stSS.getValue().equals(stCompare.getValue())){
						if(stSS.getSensor().equals(stCompare.getSensor())){
							numEqual++;
						}
					}else{
						break;
					}
				}
				if(numEqual.equals(lst.size())){
					foundSS=true;
					SSfound=ss;
					break;
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
