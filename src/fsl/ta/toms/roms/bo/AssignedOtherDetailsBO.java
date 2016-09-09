package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

public class AssignedOtherDetailsBO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8488554248704716322L;
	
	private List<Integer> operationLocationList;
	private List<Integer> operationArteryList;
	private List<Integer> operationStrategyList;
	
	
	
	public AssignedOtherDetailsBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public AssignedOtherDetailsBO(List<Integer> opeationLocationList,
			List<Integer> opeationArteryList, List<Integer> opeationStrategyList) {
		super();
		this.operationLocationList = opeationLocationList;
		this.operationArteryList = opeationArteryList;
		this.operationStrategyList = opeationStrategyList;
	}


	public List<Integer> getOperationLocationList() {
		return operationLocationList;
	}


	public void setOperationLocationList(List<Integer> operationLocationList) {
		this.operationLocationList = operationLocationList;
	}


	public List<Integer> getOperationArteryList() {
		return operationArteryList;
	}


	public void setOperationArteryList(List<Integer> operationArteryList) {
		this.operationArteryList = operationArteryList;
	}


	public List<Integer> getOperationStrategyList() {
		return operationStrategyList;
	}


	public void setOperationStrategyList(List<Integer> operationStrategyList) {
		this.operationStrategyList = operationStrategyList;
	}


	
}
