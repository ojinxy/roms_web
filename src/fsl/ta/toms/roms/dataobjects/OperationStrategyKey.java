package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class OperationStrategyKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8716461724023989559L;

	public OperationStrategyKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	@ManyToOne
	@JoinColumn(name="road_operation_id")
	RoadOperationDO roadOperation;
	
	@ManyToOne
	@JoinColumn(name="strategy_id")
	StrategyDO strategy;

	public RoadOperationDO getRoadOperation() {
		return roadOperation;
	}

	public void setRoadOperation(RoadOperationDO roadOperation) {
		this.roadOperation = roadOperation;
	}

	public StrategyDO getStrategy() {
		return strategy;
	}

	public void setStrategy(StrategyDO strategy) {
		this.strategy = strategy;
	}
	
	
}
