package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

public class VehicleCriteriaBO implements Serializable, SearchCriteria{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3639512581023911584L;
	/**
	 * 
	 */
	private String vehicleId;
	private String plateRegNo,engineNo,chassisNo;
	private String colour,makeDescription,model,typeDesc;
	private String ownerName;
	private String romsVehicleType;
	private Integer year;

	public VehicleCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getPlateRegNo() {
		return plateRegNo;
	}

	public void setPlateRegNo(String plateRegNo) {
		this.plateRegNo = plateRegNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getMakeDescription() {
		return makeDescription;
	}

	public void setMakeDescription(String makeDescription) {
		this.makeDescription = makeDescription;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getRomsVehicleType() {
		return romsVehicleType;
	}

	public void setRomsVehicleType(String romsVehicleType) {
		this.romsVehicleType = romsVehicleType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}


	
}