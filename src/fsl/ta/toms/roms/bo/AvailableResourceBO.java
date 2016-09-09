package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

public class AvailableResourceBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3040392634738759697L;
	
	private List<TAStaffBO> taStaffList;
	private List<JPBO> jpList;
	private List<PoliceOfficerBO> policeOfficerList;
	private List<ITAExaminerBO> itaExaminerList;
	private List<TAVehicleBO> taVehicleList;
	
	public AvailableResourceBO() {
		super();
		// TODO Auto-generated constructor stub
	}

		
	public AvailableResourceBO(List<TAStaffBO> taStaffList, List<JPBO> jpList,
			List<PoliceOfficerBO> policeOfficerList,
			List<ITAExaminerBO> itaExaminerList, List<TAVehicleBO> taVehicleList) {
		super();
		this.taStaffList = taStaffList;
		this.jpList = jpList;
		this.policeOfficerList = policeOfficerList;
		this.itaExaminerList = itaExaminerList;
		this.taVehicleList = taVehicleList;
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


	
}
