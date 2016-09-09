package fsl.ta.toms.roms.bo;

import java.io.Serializable;

public class VehicleCheckResultOwnerBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4838099277356023689L;
	Integer ownerId;
	Integer vehicleCheckResultID;
	String firstName,midName,lastName;
	String trnNbr;
	String trnBranch;
	String markText;
	String streetNo;
	String streetName;
	String poLocationName;
	String poBoxNoo;
	String parish;
	String parishDesc;
	String city;
	
	
	
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
	public String getMarkText() {
		return markText;
	}
	public void setMarkText(String markText) {
		this.markText = markText;
	}
	public String getStreetNo() {
		return streetNo;
	}
	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getPoLocationName() {
		return poLocationName;
	}
	public void setPoLocationName(String poLocationName) {
		this.poLocationName = poLocationName;
	}
	public String getPoBoxNoo() {
		return poBoxNoo;
	}
	public void setPoBoxNoo(String poBoxNoo) {
		this.poBoxNoo = poBoxNoo;
	}
	public String getParish() {
		return parish;
	}
	public void setParish(String parish) {
		this.parish = parish;
	}
	public String getParishDesc() {
		return parishDesc;
	}
	public void setParishDesc(String parishDesc) {
		this.parishDesc = parishDesc;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	
	
}
