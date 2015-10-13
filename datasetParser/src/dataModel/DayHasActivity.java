package dataModel;

public class DayHasActivity {
	private Integer id;
	private Integer startSec;
	private Integer endSec;
	private Activity activity;
	private Resident resident;
	
	public DayHasActivity(Integer id, Integer startSec, Integer endSec,
			Activity activity,Resident resident) {
		super();
		this.id = id;
		this.startSec = startSec;
		this.endSec = endSec;
		this.activity = activity;
		this.resident = resident;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStartSec() {
		return startSec;
	}
	public void setStartSec(Integer startSec) {
		this.startSec = startSec;
	}
	public Integer getEndSec() {
		return endSec;
	}
	public void setEndSec(Integer endSec) {
		this.endSec = endSec;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public Resident getResident() {
		return resident;
	}
	public void setResident(Resident resident) {
		this.resident = resident;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activity == null) ? 0 : activity.hashCode());
		result = prime * result + ((endSec == null) ? 0 : endSec.hashCode());
		result = prime * result
				+ ((resident == null) ? 0 : resident.hashCode());
		result = prime * result
				+ ((startSec == null) ? 0 : startSec.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DayHasActivity other = (DayHasActivity) obj;
		if (activity == null) {
			if (other.activity != null)
				return false;
		} else if (!activity.equals(other.activity))
			return false;
		if (endSec == null) {
			if (other.endSec != null)
				return false;
		} else if (!endSec.equals(other.endSec))
			return false;
		if (resident == null) {
			if (other.resident != null)
				return false;
		} else if (!resident.equals(other.resident))
			return false;
		if (startSec == null) {
			if (other.startSec != null)
				return false;
		} else if (!startSec.equals(other.startSec))
			return false;
		return true;
	}
	
}
