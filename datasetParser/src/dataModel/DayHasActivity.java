package dataModel;

public class DayHasActivity {
	private Integer id;
	private Integer startSec;
	private Integer endSec;
	private Activity activity;
	
	public DayHasActivity(Integer id, Integer startSec, Integer endSec,
			Activity activity) {
		super();
		this.id = id;
		this.startSec = startSec;
		this.endSec = endSec;
		this.activity = activity;
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
	
	
}
