package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.OperationStrategyDO;

public class OperationStrategyBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2728618237091371564L;
	
	private Integer roadOperationId;
	private Integer strategyId;
	
	public OperationStrategyBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OperationStrategyBO(OperationStrategyDO operationStrategyDO) {
		if(operationStrategyDO!=null){
			if(operationStrategyDO.getOperationStrategyKey()!=null){
				if(operationStrategyDO.getOperationStrategyKey().getRoadOperation()!=null){
					this.roadOperationId = operationStrategyDO.getOperationStrategyKey().getRoadOperation().getRoadOperationId();
				}
				if(operationStrategyDO.getOperationStrategyKey().getStrategy()!=null){
					this.strategyId = operationStrategyDO.getOperationStrategyKey().getStrategy().getStrategyId();
				}
			}
		}
		
	}

	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}

	public Integer getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}

}
