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
}
