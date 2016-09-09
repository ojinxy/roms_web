package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AssignedTeamDetailsBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3184776141702615282L;

	private List<TAStaffBO> taStaffList = new ArrayList<TAStaffBO>();
	private List<JPBO> jpList = new ArrayList<JPBO>();
	private List<PoliceOfficerBO> policeOfficerList = new ArrayList<PoliceOfficerBO>() ;
	private List<ITAExaminerBO> itaExaminerList = new ArrayList<ITAExaminerBO>();
	private List<TAVehicleBO> taVehicleList = new ArrayList<TAVehicleBO>();
	
	private List<LocationBO> operationLocationList = new ArrayList<LocationBO>();
	private List<ArteryBO> operationArteryList = new ArrayList<ArteryBO>();
	private List<ParishBO> operationParishList = new ArrayList<ParishBO>();

	private TeamBO teamBO;
	

	public AssignedTeamDetailsBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public AssignedTeamDetailsBO(List<TAStaffBO> taStaffList,
			List<JPBO> jpList, List<PoliceOfficerBO> policeOfficerList,
			List<ITAExaminerBO> itaExaminerList,
			List<TAVehicleBO> taVehicleList,
			List<LocationBO> operationLocationList,
			List<ArteryBO> operationArteryList, TeamBO teamBO) {
		super();
		this.taStaffList = taStaffList;
		this.jpList = jpList;
		this.policeOfficerList = policeOfficerList;
		this.itaExaminerList = itaExaminerList;
		this.taVehicleList = taVehicleList;
		this.operationLocationList = operationLocationList;
		this.operationArteryList = operationArteryList;
		this.teamBO = teamBO;
	}
	
	public AssignedTeamDetailsBO(List<TAStaffBO> taStaffList,
			List<JPBO> jpList, List<PoliceOfficerBO> policeOfficerList,
			List<ITAExaminerBO> itaExaminerList,
			List<TAVehicleBO> taVehicleList,
			List<LocationBO> operationLocationList,
			List<ArteryBO> operationArteryList, TeamBO teamBO, List<ParishBO> operationParishList) {
		super();
		this.taStaffList = taStaffList;
		this.jpList = jpList;
		this.policeOfficerList = policeOfficerList;
		this.itaExaminerList = itaExaminerList;
		this.taVehicleList = taVehicleList;
		this.operationLocationList = operationLocationList;
		this.operationArteryList = operationArteryList;
		this.teamBO = teamBO;
		this.operationParishList=operationParishList;
	}


	public List<TAStaffBO> getTaStaffList() {
		return taStaffList;
	}


	public void setTaStaffList(List<TAStaffBO> taStaffList) {
		this.taStaffList = taStaffList;
	}


	public List<JPBO> getJpList() {
		return jpList;
	}


	public void setJpList(List<JPBO> jpList) {
		this.jpList = jpList;
	}


	public List<PoliceOfficerBO> getPoliceOfficerList() {
		return policeOfficerList;
	}


	public void setPoliceOfficerList(List<PoliceOfficerBO> policeOfficerList) {
		this.policeOfficerList = policeOfficerList;
	}


	public List<ITAExaminerBO> getItaExaminerList() {
		return itaExaminerList;
	}


	public void setItaExaminerList(List<ITAExaminerBO> itaExaminerList) {
		this.itaExaminerList = itaExaminerList;
	}


	public List<TAVehicleBO> getTaVehicleList() {
		return taVehicleList;
	}


	public void setTaVehicleList(List<TAVehicleBO> taVehicleList) {
		this.taVehicleList = taVehicleList;
	}


	public List<LocationBO> getOperationLocationList() {
		return operationLocationList;
	}


	public void setOperationLocationList(List<LocationBO> operationLocationList) {
		this.operationLocationList = operationLocationList;
	}


	public List<ArteryBO> getOperationArteryList() {
		return operationArteryList;
	}


	public void setOperationArteryList(List<ArteryBO> operationArteryList) {
		this.operationArteryList = operationArteryList;
	}


	public TeamBO getTeamBO() {
		return teamBO;
	}


	public void setTeamBO(TeamBO teamBO) {
		this.teamBO = teamBO;
	}


	public List<ParishBO> getOperationParishList() {
		return operationParishList;
	}


	public void setOperationParishList(List<ParishBO> operationParishList) {
		this.operationParishList = operationParishList;
	}


	
	
}
