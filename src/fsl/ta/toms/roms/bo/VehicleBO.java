package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.VehicleDO;


public class VehicleBO implements Serializable {

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = -2473873724018511056L;
	Integer vehicleId;
	String plateRegNo,engineNo,chassisNo;
	String colour,makeDescription,model,typeDesc;
	String ownerName;
	Integer year;
	String currentUserName;
	
	
	
	public VehicleBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VehicleBO(Integer vehicleId, String plateRegNo, String engineNo,
			String chassisNo, String colour, String makeDescription,
			String model, String typeDesc, String ownerName,String year,
			 String currentUser) {
		super();
		this.vehicleId = vehicleId;
		this.plateRegNo = plateRegNo.toUpperCase();
		this.engineNo = engineNo;
		this.chassisNo = chassisNo;
		this.colour = colour;
		this.makeDescription = makeDescription;
		this.model = model;
		this.typeDesc = typeDesc;
		this.ownerName = ownerName;
		
		if(! StringUtils.isEmpty(year))
		{
			try
			{
				Integer intYear = Integer.parseInt(year);
				this.year =  intYear;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		
		this.currentUserName = currentUser;
	}
	public VehicleBO(VehicleDO vehicleDo) {
	
		if(vehicleDo != null)
		{
			this.setVehicleId(vehicleDo.getVehicleId());
			this.setPlateRegNo(vehicleDo.getPlateRegNo().toUpperCase());
			this.setEngineNo(vehicleDo.getEngineNo());
			this.setChassisNo(vehicleDo.getChassisNo());
			this.setColour(vehicleDo.getColour());
			this.setMakeDescription(vehicleDo.getMakeDescription());
			this.setModel(vehicleDo.getModel());
			this.setTypeDesc(vehicleDo.getTypeDesc());
			this.setOwnerName(vehicleDo.getOwnerName());
			this.setYear(vehicleDo.getYear());
			
			
		}
		
	}
	
	public VehicleDO generateVehicleDO()
	{
		VehicleDO vehDO = new VehicleDO();
		
		vehDO.setChassisNo(this.getChassisNo());
		vehDO.setColour(this.getColour());
		vehDO.setEngineNo(this.getEngineNo());
		vehDO.setMakeDescription(this.getMakeDescription());
		vehDO.setModel(this.getModel());
		vehDO.setOwnerName(this.getOwnerName());
		vehDO.setPlateRegNo(this.getPlateRegNo().toUpperCase());
		vehDO.setTypeDesc(this.getTypeDesc());
		vehDO.setYear(this.getYear());
		
		return vehDO;
		
	}


	public Integer getVehicleId() {
		return vehicleId;
	}


	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}


	public String getPlateRegNo() {
		return plateRegNo;
	}


	public void setPlateRegNo(String plateRegNo) {
		if(StringUtils.isNotBlank(plateRegNo))
			this.plateRegNo = plateRegNo.trim().toUpperCase();
	}


	public String getEngineNo() {
		return engineNo;
	}


	public void setEngineNo(String engineNo) {
		if(StringUtils.isNotBlank(engineNo))
			this.engineNo = engineNo.trim();
	}


	public String getChassisNo() {
		return chassisNo;
	}


	public void setChassisNo(String chassisNo) 
	{
		if(StringUtils.isNotBlank(chassisNo))
			this.chassisNo = chassisNo.trim();
	}


	public String getColour() {
		return colour;
	}


	public void setColour(String colour) {
		if(StringUtils.isNotBlank(colour))
			this.colour = colour.trim();
	}


	public String getMakeDescription() {
		return makeDescription;
	}


	public void setMakeDescription(String makeDescription) {
		if(StringUtils.isNotBlank(makeDescription))
			this.makeDescription = makeDescription.trim();
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		if(StringUtils.isNotBlank(model))
			this.model = model.trim();
	}


	public String getTypeDesc() {
		return typeDesc;
	}


	public void setTypeDesc(String typeDesc) {
		if(StringUtils.isNotBlank(typeDesc))
			this.typeDesc = typeDesc.trim();
	}


	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		if(StringUtils.isNotBlank(ownerName))
			this.ownerName = ownerName.trim();
	}


	


	public Integer getYear() {
		return year;
	}


	public void setYear(Integer year) {
		this.year = year;
	}


	public String getCurrentUserName() {
		return currentUserName;
	}


	public void setCurrentUserName(String currentUserName) {
		if(StringUtils.isNotBlank(currentUserName))
			this.currentUserName = currentUserName;
	}
	
	public void trimVehicleDetails() {
		if(StringUtils.isNotBlank(this.plateRegNo)){
			setPlateRegNo(this.plateRegNo.trim().toUpperCase());
		}
		
		if(StringUtils.isNotBlank(this.engineNo)){
			setEngineNo(this.engineNo.trim());
		}
		
		if(StringUtils.isNotBlank(this.chassisNo)){
			setChassisNo(this.chassisNo.trim());
		}
		
		if(StringUtils.isNotBlank(this.colour)){
			setColour(this.colour.trim());
		}
		
		if(StringUtils.isNotBlank(this.makeDescription)){
			setMakeDescription(this.makeDescription.trim());
		}
		
		if(StringUtils.isNotBlank(this.model)){
			setModel(this.model.trim());
		}
	
		if(StringUtils.isNotBlank(this.typeDesc)){
			setTypeDesc(this.typeDesc.trim());
		}
	
		if(StringUtils.isNotBlank(this.ownerName)){
			setOwnerName(this.ownerName.trim());
		}
	}

	

	
	
}
