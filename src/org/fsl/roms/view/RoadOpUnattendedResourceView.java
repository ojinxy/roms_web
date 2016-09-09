package org.fsl.roms.view;

import java.io.Serializable;

import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;

public class RoadOpUnattendedResourceView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1832745940292381517L;
	
	private String name;
	private String type;
	
	
	public RoadOpUnattendedResourceView() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RoadOpUnattendedResourceView(TAStaffBO taStaff) {
		super();
		this.name = taStaff.getFullName() + " - " + taStaff.getStaffId();
		this.type = "TA Staff";
	}
	
	public RoadOpUnattendedResourceView(ITAExaminerBO ita) {
		super();
		//this.name = ita.getPersonBO().getFullName() + " - " + ita.getIdNumber();
		this.name = ita.getPersonBO().getFullName() + " - " + ita.getExaminerId();
		this.type = "ITA Examiner";
	}
	

	public RoadOpUnattendedResourceView(PoliceOfficerBO police) {
		super();
		this.name = police.getFullName() + " - " + police.getPolOfficerCompNo();
		this.type = "Police";
	}
	

	public RoadOpUnattendedResourceView(JPBO jp) {
		super();
		this.name = jp.getPersonBO().getFullName() + " - " + jp.getRegNumber();
		this.type = "JP";
	}
	
	public RoadOpUnattendedResourceView(TAVehicleBO vehicle) {
		super();
		this.name = vehicle.getVehicle().getPlateRegNo() + " - " + vehicle.getVehicle().getMakeDescription() + " " + vehicle.getVehicle().getModel() + " [" + vehicle.getVehicle().getColour()+"]";
		this.type = "Vehicle";
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	

}
