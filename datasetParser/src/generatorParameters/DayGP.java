package generatorParameters;

import dataModel.Day;
import dataModel.Resident;

public class DayGP extends Day {
	public Resident resident;
	
	public DayGP(Integer id, Integer incrementalDay, String day, String month,
			String year, String idSSs,Resident resident) {
		super(id, incrementalDay, day, month, year, idSSs);
		this.resident=resident;
	}
	
	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	
}
