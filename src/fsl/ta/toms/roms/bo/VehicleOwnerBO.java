package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.VehicleCheckResultOwnerDO;
import fsl.ta.toms.roms.dataobjects.VehicleOwnerDO;

public class VehicleOwnerBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4838099277356023689L;
	Integer ownerId;
	Integer vehicleCheckResultID;
	String firstName,midName,lastName;
	String trnNbr;
	String trnBranch;
	
	
	AddressBO address;
	
	/*String markText;
	String streetNo;
	String streetName;
	String poLocationName;
	String poBoxNoo;
	String parish;
	String parishDesc;
	String city;
	String country;*/
	
	
	
	public VehicleOwnerBO(VehicleOwnerDO vehicle) {
		super();
		this.ownerId = vehicle.getOwnerId();
		
		this.firstName = vehicle.getFirstName();
		this.midName = vehicle.getMidName();
		this.lastName = vehicle.getLastName();
		this.trnNbr = vehicle.getTrnNbr();
		this.trnBranch = vehicle.getTrnBranch();
		
		if(vehicle.getAddress() != null){
			this.address = new AddressBO(vehicle.getAddress());
		}
	}
	
	
	
	

	public VehicleOwnerBO(VehicleCheckResultOwnerDO vehicle) {
		super();
		this.ownerId = vehicle.getOwnerId();
		
		this.firstName = vehicle.getFirstName();
		this.midName = vehicle.getMidName();
		this.lastName = vehicle.getLastName();
		this.trnNbr = vehicle.getTrnNbr();
		this.trnBranch = vehicle.getTrnBranch();
		
		if(vehicle.getAddress() != null){
			this.address = new AddressBO(vehicle.getAddress());
		}
	}
	
	public VehicleOwnerBO() {
		super();
	}





	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	public Integer getVehicleCheckResultID() {
		return vehicleCheckResultID;
	}
	public void setVehicleCheckResultID(Integer vehicleCheckResultID) {
		this.vehicleCheckResultID = vehicleCheckResultID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMidName() {
		return midName;
	}
	public void setMidName(String midName) {
		this.midName = midName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getTrnNbr() {
		return trnNbr;
	}
	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
	}
	public String getTrnBranch() {
		return trnBranch;
	}
	public void setTrnBranch(String trnBranch) {
		this.trnBranch = trnBranch;
	}
	public AddressBO getAddress() {
		return address;
	}
	public void setAddress(AddressBO address) {
		this.address = address;
	}
	
	
	
}
