package generatorParameters;

import java.util.ArrayList;
import java.util.List;

import dataModel.DayHasActivity;

public class Pattern {
	private ActivityGP activity;
	private Integer uniqueIdPattern;
	private int[][] SStransMatrix;
	private float[][] SSProbMatrix;
	private List<DayHasActivityGP> dhasInCluster;
	private float initialProb;
	private ArrayList<Float> ssIniProbInPattern;
	
	public Pattern(Integer uniqueIdPattern,	List<DayHasActivityGP> dhasInCluster,ActivityGP activity) {
		super();
		this.uniqueIdPattern = uniqueIdPattern;
		SStransMatrix = null;
		SSProbMatrix = null;
		this.dhasInCluster = dhasInCluster;
		this.activity=activity;
		this.initialProb=(float) 0;
		this.ssIniProbInPattern=new ArrayList<Float>();
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
	public Float getInitialProb() {
		return initialProb;
	}

	public void setInitialProb(Float initialProb) {
		this.initialProb = initialProb;
	}
	
	public ArrayList<Float> getSsIniProbInPattern() {
		return ssIniProbInPattern;
	}

	public void setSsIniProbInPattern(ArrayList<Float> ssIniProbInPattern) {
		this.ssIniProbInPattern = ssIniProbInPattern;
	}

}
