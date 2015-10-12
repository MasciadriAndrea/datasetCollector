package generatorParameters;

public class Transition {
	private Integer uniqueSensorsetId;
	private Integer number;
	private Float probability;
	
	public Transition(Integer uniqueSensorsetId, Integer number) {
		super();
		this.uniqueSensorsetId = uniqueSensorsetId;
		this.number = 1;
	}

	public Integer getUniqueSensorsetId() {
		return uniqueSensorsetId;
	}

	public void setUniqueSensorsetId(Integer uniqueSensorsetId) {
		this.uniqueSensorsetId = uniqueSensorsetId;
	}

	public Float getProbability() {
		return probability;
	}

	public void setProbability(Float probability) {
		this.probability = probability;
	}
	
	public void incrementNumber() {
		this.number++;
	}

}
