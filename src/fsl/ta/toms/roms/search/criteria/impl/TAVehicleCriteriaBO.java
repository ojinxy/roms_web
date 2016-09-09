package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.bo.VehicleBO;

public class TAVehicleCriteriaBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 800542929384649118L;

	private Integer taVehicleId;
	private VehicleBO vehicle;
	private String officeLocationCode;
	private String statusId;
		
	
	public TAVehicleCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getTaVehicleId() {
		return taVehicleId;
	}


	public void setTaVehicleId(Integer taVehicleId) {
		this.taVehicleId = taVehicleId;
	}


	public VehicleBO getVehicle() {
		return vehicle;
	}


	public void setVehicle(VehicleBO vehicle) {
		this.vehicle = vehicle;
	}





	public String getOfficeLocationCode() {
		return officeLocationCode;
	}


	public void setOfficeLocationCode(String officeLocationCode) {
		if(StringUtils.isNotBlank(officeLocationCode))
			this.officeLocationCode = officeLocationCode.trim();
	}


	public String getStatusId() {
		return statusId;
	}


	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}


	
	
}
