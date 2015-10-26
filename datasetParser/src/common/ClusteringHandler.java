package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dataModel.Resident;
import dataModel.DayHasActivity;
import generatorParameters.ActivityGP;
import generatorParameters.DayGP;
import generatorParameters.DayHasActivityGP;
import generatorParameters.Parameters;
import generatorParameters.Pattern;

public class ClusteringHandler {
	private static ClusteringHandler instance;

	private ClusteringHandler() {
		super();
	}

	public static ClusteringHandler getInstance(){
		if(instance==null){
			instance=new ClusteringHandler();
		}
		return instance;
	}

	public void clusterizeDha(Parameters param,Integer N, Integer K) throws Exception{
		List<Pattern> pattList=new ArrayList<Pattern>();
		System.out.println("Clustering dhas to find patterns");
		for(ActivityGP agp:param.getActivities()){
			if(agp.getUniqueActivityId()!=0){
				System.out.println("clustering activity: "+agp.getName());
				Integer idActivity=agp.getUniqueActivityId();
				List<DayHasActivityGP> dhaOfActivity=new ArrayList<DayHasActivityGP>();
				for(Resident r:param.getResidents()){
					for(DayGP daygp:param.getDays()){
						if(daygp.resident.getUniqueResidentId().equals(r.getUniqueResidentId())){
							for(DayHasActivity dha:daygp.getDailyActivities()){
								DayHasActivityGP dha1=(DayHasActivityGP) dha;
								if(dha.getActivity().getUniqueActivityId().equals(idActivity)){
									dhaOfActivity.add(dha1);
								}
							}
						}
					}
				}
				System.out.println("Found dhas: "+dhaOfActivity.size());
				agp.setDhaInActivity(dhaOfActivity.size());
				if(dhaOfActivity.size()>0){
				//now i have the list of dha (performance) of that activity			
				Map<DayHasActivityGP, List<DayHasActivityGP>> nNeighbors= new HashMap<DayHasActivityGP, List<DayHasActivityGP>>();
				
				
				for(DayHasActivityGP dha:dhaOfActivity){
					Map<DayHasActivityGP,Float> neighbors=new HashMap<DayHasActivityGP,Float>();
					Float maxValueInList=(float) 0;
					for(DayHasActivityGP dha2:dhaOfActivity){
						if(!dha.equals(dha2)){
							Float distance=this.dhaDistance(dha, dha2);
							if(neighbors.size()>=N){
								if(distance<maxValueInList){
									for(Entry<DayHasActivityGP, Float> neighbor : neighbors.entrySet()){
										if(neighbor.getValue().equals(maxValueInList)){
											neighbors.remove(neighbor.getKey());
											neighbors.put(dha2,distance);
											break;
										}
									}
									maxValueInList=(float) 0;
									for(Entry<DayHasActivityGP, Float> neighbor : neighbors.entrySet()){
										if(neighbor.getValue()>maxValueInList){
											maxValueInList=neighbor.getValue();
										}
									}
								}
							}else{
								//before complete the list
								neighbors.put(dha2,distance);
								if(maxValueInList<distance){
									maxValueInList=distance;
								}
							}
						}
					}
					
					List<DayHasActivityGP> closestDhas=new ArrayList<DayHasActivityGP>();
					for(Entry<DayHasActivityGP, Float> neighbor : neighbors.entrySet()){
						closestDhas.add(neighbor.getKey());
					}
					nNeighbors.put(dha, closestDhas);
				}
				// here i have nNeighbors like map of dha and List of N closest dhas
				pattList=computeCluster(agp,nNeighbors,N,K);
				System.out.println("Found patterns: "+pattList.size());
				agp.setPatterns(pattList);
				}
			}else{
				//System.out.println("Not clustering activity"+agp.getName());
			}
		}
	}
	
	public List<Pattern> computeCluster(ActivityGP agp,Map<DayHasActivityGP, List<DayHasActivityGP>> nNeighbors,Integer N,Integer K){
		List<Pattern> pattList=new ArrayList<Pattern>();
		Integer patternId=0;
		List<DayHasActivityGP> overallList=new ArrayList<DayHasActivityGP>();
		for(Entry<DayHasActivityGP, List<DayHasActivityGP>> neighbor:nNeighbors.entrySet()){
			overallList.add(neighbor.getKey());
			/*
			 * TO SHOW FOR EVERY DHA ITS N NEIGHBORS
			 * 
			String strids="";
			for(DayHasActivityGP dha:neighbor.getValue()){
				strids+=dha.getUniqueDayHasActivityId()+" - ";
			}
			System.out.println("DayHasActivity "+neighbor.getKey().getUniqueDayHasActivityId()+" has these neighbors: "+strids);
		  */
		}
		Integer numClus=0;
		while(!overallList.isEmpty()){
			/*
			 
			String strids="";
			for(DayHasActivityGP dha:overallList){
				strids+=dha.getUniqueDayHasActivityId()+" - ";
			}
			System.out.println("Overall to be computed: "+overallList.size()+" -> "+strids);
			 */
			patternId++;
			DayHasActivityGP currentNode=overallList.get(0);
			
			//TODO ------- check here assignment
			List<DayHasActivityGP> neighbor=new ArrayList<DayHasActivityGP>();
			List<DayHasActivityGP> temp=nNeighbors.get(currentNode);
			for(DayHasActivityGP dhatemp:temp){
				neighbor.add(dhatemp);
			}
			//---------
			
			
			neighbor.retainAll(overallList);
			/*
			strids="";
			for(DayHasActivityGP dha:neighbor){
				strids+=dha.getUniqueDayHasActivityId()+" - ";
			}
			System.out.println("Neighbors to compute -> "+strids);
			*/
			overallList.remove(0);
			
			numClus++;
			//System.out.println("Evaluating cluster "+numClus+" starting from dha "+currentNode.getUniqueDayHasActivityId());
			List<DayHasActivityGP> inPattern=new ArrayList<DayHasActivityGP>();
			inPattern.add(currentNode);
			inPattern=recursiveCluster(nNeighbors,overallList,currentNode,inPattern,neighbor,K);
			
			Pattern p=new Pattern(patternId,inPattern,agp);
			pattList.add(p);
			
			/*
			strids="";
			for(DayHasActivityGP dha:inPattern){
				strids+=dha.getUniqueDayHasActivityId()+" - ";
			}
			System.out.println("created "+pattList.size()+" cluster of: "+inPattern.size()+" -> "+strids);
			*/
			
		}
		
		return pattList;
	}
	
	public Boolean sameCluster(Map<DayHasActivityGP, List<DayHasActivityGP>> nNeighbors,DayHasActivityGP dha1,DayHasActivityGP dha2, Integer K){
		List<DayHasActivityGP> neigh1=nNeighbors.get(dha1);
		List<DayHasActivityGP> neigh2=nNeighbors.get(dha2);
		Integer common=0;
		for(DayHasActivityGP dhaN1:neigh1){
			for(DayHasActivityGP dhaN2:neigh2){
				if(dhaN1.getUniqueDayHasActivityId().equals(dhaN2.getUniqueDayHasActivityId())){
					common++;
				}
			}
		}
		return common>=K;
	}
	
	public List<DayHasActivityGP> recursiveCluster(Map<DayHasActivityGP, List<DayHasActivityGP>> nNeighbors,List<DayHasActivityGP> overallList, DayHasActivityGP currNode, List<DayHasActivityGP> inPattern,List<DayHasActivityGP> toCompute,Integer K ){
		if(toCompute.isEmpty()){
			return inPattern;
		}else{
			//explore the first 
			Iterator<DayHasActivityGP> iter= toCompute.iterator();
	
			while (iter.hasNext()){
				try{
				DayHasActivityGP dha=iter.next();
				//System.out.println("Evaluating if dha "+dha.getUniqueDayHasActivityId()+" is with dha "+currNode.getUniqueDayHasActivityId());
				if(sameCluster(nNeighbors,currNode,dha,K)){
					//System.out.println("They are together..going deep");
					if(!inPattern.contains(dha)){
						inPattern.add(dha);
					}
					overallList.remove(dha);
					
					//recursion here
					
					//TODO ---------- check the assigment here
					List<DayHasActivityGP> subneighbor=new ArrayList<DayHasActivityGP>();
					List<DayHasActivityGP> temp=nNeighbors.get(dha);
					for(DayHasActivityGP dhatemp:temp){
						subneighbor.add(dhatemp);
					}
					//-----------------
					
					subneighbor.removeAll(inPattern);
					subneighbor.retainAll(overallList);
					inPattern=recursiveCluster(nNeighbors,overallList,dha,inPattern,subneighbor,K);			
				}else{
					//System.out.println("not together");
				}
				}catch(Exception e){
					System.out.println("Exception catched");
					return inPattern;
				}
			}
		
			return inPattern;
		}
	}

	public float dhaDistance(DayHasActivityGP day1,DayHasActivityGP day2) throws Exception{
		return matrixdistance(day1.getSStransMatrix(),day2.getSStransMatrix());
	}

	public float matrixdistance(int[][] m1,int[][] m2) throws Exception{
		int d11=m1.length;
		int d12=m2.length;
		int d21=m1[0].length;
		int d22=m2[0].length;
		int sumM=0;
		if((d11==(d12))&&(d21==(d22))){
			for(int row=0;row<d11;row++){
				for(int col=0;col<d21;col++){
					sumM+=(int) Math.pow((m1[row][col]-m2[row][col]), 2);
				}
			}
			return (float) sumM/(d11*d21);
		}else{
			System.out.println("Length error: d11: "+d11+" d12: "+d12+" d21: "+d21+" d22: "+d22);
			throw new Exception("Error in length");
		}
	}
}
