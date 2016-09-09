package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

public class RoadOperationOtherDetailsBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2437494078139952056L;
	
	/*private List<AssignedPersonBO> assignedPersonList;
	private List<AssignedVehicleBO> assignedVehicleList;
	private List<OperationLocationBO> operationLocationList;
	private List<OperationArteryBO> operationArteryList;*/
	private List<AssignedTeamDetailsBO> assignedTeamDetailsBOList;
	private List<StrategyBO> operationStrategyList;
	private List<String> operationRegionList;
	 
	public RoadOperationOtherDetailsBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	




	/*public List<AssignedPersonBO> getAssignedPersonList() {
		return assignedPersonList;
	}

	public void setAssignedPersonList(List<AssignedPersonBO> assignedPersonList) {
		this.assignedPersonList = assignedPersonList;
	}

	public List<AssignedVehicleBO> getAssignedVehicleList() {
		return assignedVehicleList;
	}

	public void setAssignedVehicleList(List<AssignedVehicleBO> assignedVehicleList) {
		this.assignedVehicleList = assignedVehicleList;
	}

	public List<OperationLocationBO> getOperationLocationList() {
		return operationLocationList;
	}

	public void setOperationLocationList(
			List<OperationLocationBO> operationLocationList) {
		this.operationLocationList = operationLocationList;
	}

	public List<OperationArteryBO> getOperationArteryList() {
		return operationArteryList;
	}

	public void setOperationArteryList(List<OperationArteryBO> operationArteryList) {
		this.operationArteryList = operationArteryList;
	}
*/
	







	





	public List<AssignedTeamDetailsBO> getAssignedTeamDetailsBOList() {
		return assignedTeamDetailsBOList;
	}

	




	public RoadOperationOtherDetailsBO(
			List<AssignedTeamDetailsBO> assignedTeamDetailsBOList,
			List<StrategyBO> operationStrategyList) {
		super();
		this.assignedTeamDetailsBOList = assignedTeamDetailsBOList;
		this.operationStrategyList = operationStrategyList;
	}

	public RoadOperationOtherDetailsBO(
			List<AssignedTeamDetailsBO> assignedTeamDetailsBOList,
			List<StrategyBO> operationStrategyList, List<String> operationRegionList) {
		super();
		this.assignedTeamDetailsBOList = assignedTeamDetailsBOList;
		this.operationStrategyList = operationStrategyList;
		this.operationRegionList = operationRegionList;
	}




	public List<StrategyBO> getOperationStrategyList() {
		return operationStrategyList;
	}






	public void setOperationStrategyList(List<StrategyBO> operationStrategyList) {
		this.operationStrategyList = operationStrategyList;
	}






	public void setAssignedTeamDetailsBOList(
			List<AssignedTeamDetailsBO> assignedTeamDetailsBOList) {
		this.assignedTeamDetailsBOList = assignedTeamDetailsBOList;
	}






	public List<String> getOperationRegionList() {
		return operationRegionList;
	}






	public void setOperationRegionList(List<String> operationRegionList) {
		this.operationRegionList = operationRegionList;
	}

	
	
}
