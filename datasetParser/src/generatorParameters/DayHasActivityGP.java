package generatorParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataModel.Activity;
import dataModel.DayHasActivity;
import dataModel.Resident;

public class DayHasActivityGP extends DayHasActivity {

	private	 Integer uniqueDayHasActivityId;
	
	public int[] vectorChangeSS;
	public Map<Integer,Integer> usedSS;
	
	public DayHasActivityGP(Integer id, Integer dhaId,Integer startSec, Integer endSec,
			Activity activity, Resident resident) {
		super(id, startSec, endSec, activity, resident);
		this.uniqueDayHasActivityId=dhaId;
		this.usedSS=new HashMap<Integer,Integer>();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((uniqueDayHasActivityId == null) ? 0
						: uniqueDayHasActivityId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		DayHasActivityGP other = (DayHasActivityGP) obj;
		if (uniqueDayHasActivityId == null) {
			if (other.uniqueDayHasActivityId != null)
				return false;
		} else if (!uniqueDayHasActivityId.equals(other.uniqueDayHasActivityId))
			return false;
		return true;
	}

	public Integer getUniqueDayHasActivityId() {
		return uniqueDayHasActivityId;
	}

	public void setUniqueDayHasActivityId(Integer uniqueDayHasActivityId) {
		this.uniqueDayHasActivityId = uniqueDayHasActivityId;
	}

	public int[] getVectorChangeSS() {
		return vectorChangeSS;
	}

	public void setVectorChangeSS(int[] vectorChangeSS) {
		this.vectorChangeSS = vectorChangeSS;
	}
	
	public void addUsedSSId(Integer n){
		this.usedSS.put(n, 0);
	}
	public Map<Integer, Integer> getUsedSS() {
		return usedSS;
	}

	public void setUsedSS(Map<Integer, Integer> usedSS) {
		this.usedSS = usedSS;
	}

}
