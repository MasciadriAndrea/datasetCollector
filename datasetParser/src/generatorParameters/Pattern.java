package generatorParameters;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataModel.DayHasActivity;
import dataModel.HSensorset;
import dataModel.Day;

public class Pattern {
	private ActivityGP activity;
	private Integer uniqueIdPattern;
	private int[][] SStransMatrix;
	private float[][] SSProbMatrix;
	private List<DayHasActivityGP> dhasInCluster;
	private double initialProb;
	private List<Float> ssIniProbInPattern;
	private List<Integer> ssInPattId;
	private Map<Integer,List<Float>> percentageDurations;
	private List<Float> expValueTimeDisSSs;
	
	public Pattern(Integer uniqueIdPattern,	List<DayHasActivityGP> dhasInCluster,ActivityGP activity) {
		super();
		this.uniqueIdPattern = uniqueIdPattern;
		SStransMatrix = null;
		SSProbMatrix = null;
		this.dhasInCluster = dhasInCluster;
		this.activity=activity;
		this.initialProb=(float) 0;
		this.ssIniProbInPattern=new ArrayList<Float>();
		this.ssInPattId=new ArrayList<Integer>();
		this.expValueTimeDisSSs=new ArrayList<Float>();
		this.percentageDurations=new HashMap<Integer,List<Float>>();
	}
	
	public void retrieveMedoid(){
		System.out.println("patt");
		if((this.activity.getName().equals("Lunch"))||(this.activity.getName().equals("Shower"))||(this.activity.getName().equals("Cleaning"))){
			Double minDist=Double.MAX_VALUE;
			DayHasActivityGP medoid=null;
			for(DayHasActivityGP dha:dhasInCluster){
				Double dist=dha.getDist(this.SStransMatrix);
				if(dist<minDist){
					minDist=dist;
					medoid=dha;
				}
			}
			System.out.println("Exporting Act: "+this.activity.getName()+" pattern: "+this.uniqueIdPattern);
			System.out.println("minDist: "+minDist);
			PrintWriter outp;
			try{
				outp=new PrintWriter(new FileWriter("dataOut/generatorParam/medoids/"+this.activity.getName()+"_"+this.uniqueIdPattern+".txt"));
				Integer startsec=medoid.getStartSec();
				System.out.println("Start sec: "+startsec);
				Integer endsec=medoid.getEndSec();
				System.out.println("End sec: "+endsec);
				Day currentDay=null;
				for(DayGP day:ParametersHandler.getInstance().getParameters().getDays()){
					for(DayHasActivity dhas:day.getDailyActivities()){
						if(dhas.equals(medoid)){
							currentDay=day;
						}
					}
				}
				System.out.println("Day: "+currentDay.getIncrementalDay());
				for(Integer sec=startsec;sec<endsec;sec++){
					Integer ssId=currentDay.getSSidBySecond(sec);
					HSensorset ss=ParametersHandler.getInstance().getHouse().getSensorsetByUniqueId(ssId);
					String arasLine=ss.getStringSSARAS()+" "+this.getActivity().getUniqueActivityId();
					outp.println(arasLine);
				}
				outp.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public Integer getUniqueIdPattern() {
		return uniqueIdPattern;
	}

	public void setUniqueIdPattern(Integer uniqueIdPattern) {
		this.uniqueIdPattern = uniqueIdPattern;
	}

	public int[][] getSStransMatrix() {
		return SStransMatrix;
	}

	public void setSStransMatrix(int[][] sStransMatrix) {
		SStransMatrix = sStransMatrix;
	}

	public List<DayHasActivityGP> getDhasInCluster() {
		return dhasInCluster;
	}

	public void setDhasInCluster(List<DayHasActivityGP> dhasInCluster) {
		this.dhasInCluster = dhasInCluster;
	}
	public ActivityGP getActivity() {
		return activity;
	}

	public void setActivity(ActivityGP activity) {
		this.activity = activity;
	}

	public float[][] getSSProbMatrix() {
		return SSProbMatrix;
	}

	public void setSSProbMatrix(float[][] sSProbMatrix) {
		SSProbMatrix = sSProbMatrix;
	}
	public double getInitialProb() {
		return initialProb;
	}

	public void setInitialProb(double initialProb) {
		this.initialProb = initialProb;
	}
	
	public List<Float> getSsIniProbInPattern() {
		return ssIniProbInPattern;
	}

	public void setSsIniProbInPattern(List<Float> ssIniProbInPattern) {
		this.ssIniProbInPattern = ssIniProbInPattern;
	}
	
	public List<Integer> getSsInPatt() {
		return ssInPattId;
	}

	public void setSsInPatt(List<Integer> ssInPatt) {
		this.ssInPattId = ssInPatt;
	}

	public List<Float> getExpValueTimeDisSSs() {
		return expValueTimeDisSSs;
	}

	public void setExpValueTimeDisSSs(List<Float> expValueTimeDisSSs) {
		this.expValueTimeDisSSs = expValueTimeDisSSs;
	}

	public void setInitialProb(float initialProb) {
		this.initialProb = initialProb;
	}
	public List<Integer> getSsInPattId() {
		return ssInPattId;
	}

	public void setSsInPattId(List<Integer> ssInPattId) {
		this.ssInPattId = ssInPattId;
	}

	public Map<Integer, List<Float>> getPercentageDurations() {
		return percentageDurations;
	}

	public void setPercentageDurations(Map<Integer, List<Float>> percentageDurations) {
		this.percentageDurations = percentageDurations;
	}

}
