package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

public class AvailableResourceIds implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3040392634738759697L;
	
	private List<Integer> taStaffList;
	private List<Integer> jpList;
	private List<Integer> policeOfficerList;
	private List<Integer> itaExaminerList;
	private List<Integer> taVehicleList;
	
	public AvailableResourceIds() {
		super();
		// TODO Auto-generated constructor stub
	}

		
	public AvailableResourceIds(List<Integer> taStaffList, List<Integer> jpList,
			List<Integer> policeOfficerList,
			List<Integer> itaExaminerList, List<Integer> taVehicleList) {
		super();
		this.taStaffList = taStaffList;
		this.jpList = jpList;
		this.policeOfficerList = policeOfficerList;
		this.itaExaminerList = itaExaminerList;
		this.taVehicleList = taVehicleList;
	}




	public List<Integer> getTaStaffList() {
		return taStaffList;
	}

	public void setTaStaffList(List<Integer> taStaffList) {
		this.taStaffList = taStaffList;
	}

	public List<Integer> getJpList() {
		return jpList;
	}

	public void setJpList(List<Integer> jpList) {
		this.jpList = jpList;
	}

	public List<Integer> getPoliceOfficerList() {
		return policeOfficerList;
	}

	public void setPoliceOfficerList(List<Integer> policeOfficerList) {
		this.policeOfficerList = policeOfficerList;
	}

	public List<Integer> getItaExaminerList() {
		return itaExaminerList;
	}

	public void setItaExaminerList(List<Integer> itaExaminerList) {
		this.itaExaminerList = itaExaminerList;
	}


	public List<Integer> getTaVehicleList() {
		return taVehicleList;
	}


	public void setTaVehicleList(List<Integer> taVehicleList) {
		this.taVehicleList = taVehicleList;
	}


	
}
