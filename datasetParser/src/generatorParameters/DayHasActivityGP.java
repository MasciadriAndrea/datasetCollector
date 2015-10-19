package generatorParameters;

import dataModel.Activity;
import dataModel.DayHasActivity;
import dataModel.Resident;

public class DayHasActivityGP extends DayHasActivity {
	public Integer getUniqueDayHasActivityId() {
		return uniqueDayHasActivityId;
	}

	public void setUniqueDayHasActivityId(Integer uniqueDayHasActivityId) {
		this.uniqueDayHasActivityId = uniqueDayHasActivityId;
	}

	private	 Integer uniqueDayHasActivityId;
	
	public DayHasActivityGP(Integer id, Integer dhaId,Integer startSec, Integer endSec,
			Activity activity, Resident resident) {
		super(id, startSec, endSec, activity, resident);
		this.uniqueDayHasActivityId=dhaId;
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

	
}
