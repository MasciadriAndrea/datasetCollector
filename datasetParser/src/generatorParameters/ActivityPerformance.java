package generatorParameters;

import java.util.List;

public class ActivityPerformance {
	private Integer uniquePatternId;
	private List<HSensorsetGP> sensorsets;
	private Integer number;
	private Float priorProbability;
	
	public ActivityPerformance(Integer uniquePatternId, List<HSensorsetGP> sensorsets,
			Float priorProbability) {
		super();
		this.uniquePatternId = uniquePatternId;
		this.sensorsets = sensorsets;
		this.priorProbability = priorProbability;
		this.number = 1;
	}

	public Integer getUniquePatternId() {
		return uniquePatternId;
	}

	public void setUniquePatternId(Integer uniquePatternId) {
		this.uniquePatternId = uniquePatternId;
	}

	public List<HSensorsetGP> getSensorsets() {
		return sensorsets;
	}

	public void setSensorsets(List<HSensorsetGP> sensorsets) {
		this.sensorsets = sensorsets;
	}

	public Float getPriorProbability() {
		return priorProbability;
	}

	public void setPriorProbability(Float priorProbability) {
		this.priorProbability = priorProbability;
	}
	


}
