package generatorParameters;

import java.util.ArrayList;
import java.util.List;

import dataModel.HSensorset;
import dataModel.SensorTime;

public class HSensorsetGP extends HSensorset {
	
	private List<Transition> transitionProbabilities;
	
	public HSensorsetGP(Integer id, Integer uniqueId, List<SensorTime> sensors) {
		super(id, uniqueId, sensors);
		this.transitionProbabilities = new ArrayList<Transition>();
	}

	public List<Transition> getTransitionProbabilities() {
		return transitionProbabilities;
	}

	public void setTransitionProbabilities(
			List<Transition> transitionProbabilities) {
		this.transitionProbabilities = transitionProbabilities;
	}
	
}
