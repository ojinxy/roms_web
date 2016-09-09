package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

public class AssignedResourceBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3184776141702615282L;

	private List<AssignedPersonBO> taStaffList;
	private List<AssignedPersonBO> jpList;
	private List<AssignedPersonBO> policeOfficerList;
	private List<AssignedPersonBO> itaExaminerList;
	private List<AssignedVehicleBO> taVehicleList;
	private String teamLeadStaffId;
	private Integer teamId;
	private String teamName;
	
	
	public AssignedResourceBO() {
		super();
		// TODO Auto-generated constructor stub
	}



	public AssignedResourceBO(List<AssignedPersonBO> taStaffList,
			List<AssignedPersonBO> jpList,
			List<AssignedPersonBO> policeOfficerList,
			List<AssignedPersonBO> itaExaminerList,
			List<AssignedVehicleBO> taVehicleList) {
		super();
		this.taStaffList = taStaffList;
		this.jpList = jpList;
		this.policeOfficerList = policeOfficerList;
		this.itaExaminerList = itaExaminerList;
		this.taVehicleList = taVehicleList;
	}



	public AssignedResourceBO(List<AssignedPersonBO> taStaffList,
			List<AssignedPersonBO> jpList,
			List<AssignedPersonBO> policeOfficerList,
			List<AssignedPersonBO> itaExaminerList,
			List<AssignedVehicleBO> taVehicleList, String teamLeadStaffId,
			Integer teamId, String teamName) {
		super();
		this.taStaffList = taStaffList;
		this.jpList = jpList;
		this.policeOfficerList = policeOfficerList;
		this.itaExaminerList = itaExaminerList;
		this.taVehicleList = taVehicleList;
		this.teamLeadStaffId = teamLeadStaffId;
		this.teamId = teamId;
		this.teamName = teamName;
	}



	public List<AssignedPersonBO> getTaStaffList() {
		return taStaffList;
	}



	public void setTaStaffList(List<AssignedPersonBO> taStaffList) {
		this.taStaffList = taStaffList;
	}



	public List<AssignedPersonBO> getJpList() {
		return jpList;
	}



	public void setJpList(List<AssignedPersonBO> jpList) {
		this.jpList = jpList;
	}



	public List<AssignedPersonBO> getPoliceOfficerList() {
		return policeOfficerList;
	}



	public void setPoliceOfficerList(List<AssignedPersonBO> policeOfficerList) {
		this.policeOfficerList = policeOfficerList;
	}



	public List<AssignedPersonBO> getItaExaminerList() {
		return itaExaminerList;
	}



	public void setItaExaminerList(List<AssignedPersonBO> itaExaminerList) {
		this.itaExaminerList = itaExaminerList;
	}



	public List<AssignedVehicleBO> getTaVehicleList() {
		return taVehicleList;
	}



	public void setTaVehicleList(List<AssignedVehicleBO> taVehicleList) {
		this.taVehicleList = taVehicleList;
	}



	public String getTeamLeadStaffId() {
		return teamLeadStaffId;
	}



	public void setTeamLeadStaffId(String teamLeadStaffId) {
		this.teamLeadStaffId = teamLeadStaffId;
	}



	public Integer getTeamId() {
		return teamId;
	}



	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}



	public String getTeamName() {
		return teamName;
	}



	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}



	
	
}
