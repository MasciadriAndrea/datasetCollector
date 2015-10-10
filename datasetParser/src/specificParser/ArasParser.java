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

import dataModel.Activity;
import dataModel.Day;
import dataModel.DayHasActivity;
import dataModel.HSensor;
import dataModel.HSensorset;
import dataModel.House;
import dataModel.Location;
import dataModel.Resident;
import dataModel.SecondHasSensorset;
import dataModel.SensorTime;
import dataModel.SensorType;

public class ArasParser extends GenericParser {
	
	private static String urlData="dataIn/aras";
	private static String folderConf="/confFile";
	private static String fileSensor="/sensors.txt";
	private static String fileActivity="/activities.txt";
	private static String fileResident="/residents.txt";
	private static String filePRE="DAY";
	private static ArasParser instance;
	
	private ArasParser(){
		super();
	}

	public static ArasParser getInstance(){
		if(instance==null){
			instance=new ArasParser();
		}
		return instance;
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
					al.add(new Activity(null,Integer.valueOf(chunks[0]),chunks[1]));
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
					rl.add(new Resident(null,age,uid));
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
		Location l=new Location(null,1,"default");
		List<Location> ll=new ArrayList<Location>();
		ll.add(l);
		return ll;
	}
	
	@Override
	public List<SensorType> getSensorTypeList() {
		SensorType st=new SensorType(null, 1, "default");
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
				if(chunks.length>1){
					//since there is no Location and sensor type using standard one
					House h=super.getDataset().getHouses().get(0);
					Location loc=h.getLocationByUniqueId(1);
					SensorType st=h.getSensorTypeByUniqueId(1);
					sl.add(new HSensor(null, c+1, chunks[0], chunks[1], chunks[1], st, loc));
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
        Integer nFile=0;
       for (File CurrentFile : fileList) {
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
            
            Day currentDay=new Day(null,nFile,null,null,null);
            List<SecondHasSensorset> lshs=new ArrayList<SecondHasSensorset>();
            List<DayHasActivity> ldha=new ArrayList<DayHasActivity>();
            
            Integer previousActivity=0;
            Integer currentActivity=0;
            Integer startSec=0;
            Integer endSec=0;
            
            Integer secondtime=0;
            for(String pattern:configLines){
            	//for every line -> it is a second
                String[] chunks = pattern.split(" ");
                if (chunks.length > 21){
                	if(secondtime.equals(0)){
                		//initialization
                		previousActivity=Integer.parseInt(chunks[20]);
                		startSec=1;
                		endSec=1;
                	}
                	secondtime++;
                	//if the size is ok -> calculate list of sensorTime 
	                List<SensorTime> listST=new ArrayList<SensorTime>();   
	                Integer uniqueIdSensor=0;
	                HSensor sens=null;
	                String value="";
	                SensorTime st=null;
	                for(Integer i=0;i<20;i++){
	                	uniqueIdSensor=i+1;
	                	sens=h.getSensorByUniqueId(uniqueIdSensor);
	                	value=chunks[i];
	                	st=new SensorTime(null,sens,value);
	                	listST.add(st);
	                }
	                //list of SensorTime ready... if any sensorset has the same list -> add to Sensorsets list
	                HSensorset ss=getUniqueSS(h.getSensorsets(),listST);
	                lshs.add(new SecondHasSensorset(null,secondtime,ss));
	                
	                currentActivity=Integer.parseInt(chunks[20]);
	                if(!previousActivity.equals(currentActivity)){
	                	//if changed activity
	                	Activity currAct=h.getActivityByUniqueId(previousActivity);
		                ldha.add(new DayHasActivity(null,startSec,endSec,currAct));
	                	startSec=secondtime;
	                	endSec=secondtime;
	                	previousActivity=currentActivity;
	                }else{
	                	//performing the same activity
	                	endSec++;
	                }
	               
                }
                
            }
            currentDay.setDailyActivities(ldha);
            currentDay.setSecondHasSensorsets(lshs);
            ld.add(currentDay);
        } 
       	System.out.println("Number of unique SS found: "+h.getSensorsets().size());
        System.out.println("Scanned "+nFile+" files"); 
		}catch(IOException e){
			e.printStackTrace();
		}
		return ld;
	}
	
	private HSensorset getUniqueSS(List<HSensorset> lss,List<SensorTime> listST){
		Integer numSS=lss.size();
		Boolean foundSS=false;
		HSensorset SSfound=null;
		for(HSensorset ss:lss){
			List<SensorTime> lst=ss.getSensors();
			if(lst.size()!=20){
				System.out.println("Strange number of Sensors in SS");
			}else{
				Integer numEqual=0;
				for(Integer sid=1;sid<=20;sid++){
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
				if(numEqual.equals(20)){
					foundSS=true;
					SSfound=ss;
					break;
				}
			}
		}
		if(!foundSS){
			HSensorset hss=new HSensorset(null,numSS+1,listST);
			lss.add(hss);
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

	@Override
	public List<SecondHasSensorset> getSecondHasSensorset(Integer incrementalDay) {
		List<SecondHasSensorset> lshs=new ArrayList<SecondHasSensorset>();
		return lshs;
	}
	
}
