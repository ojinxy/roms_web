/**
 * Created By: oanguin
 * Date: May 3, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.LicenceOwnerDO;

/**
 * @author oanguin
 * Created Date: May 3, 2013
 */
public class RoadLicenceOwnerBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	Integer licenceNo;
	String firstName;
	String lastName;
	String trn;
	String ownerType;
	
	String streetNo;
	String streetName;
	String mark;
	String poBox;
	String postOffice;
	String parishCode;
	String homePhoneNo;
	String workPhoneNo;
	String mobilePhoneNo;
	

	public RoadLicenceOwnerBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getLicenceNo() {
		return licenceNo;
	}


	public void setLicenceNo(Integer licenceNo) {
		this.licenceNo = licenceNo;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public RoadLicenceOwnerBO(Integer licenceNo, String firstName,
			String lastName) {
		super();
		this.licenceNo = licenceNo;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public RoadLicenceOwnerBO(LicenceOwnerDO licOwner) 
	{
		super();
		this.licenceNo = licOwner.getLicenceNo();
		this.firstName = licOwner.getFirstName();
		this.lastName = licOwner.getLastName();
		this.homePhoneNo = licOwner.getTelephoneHome();
		this.licenceNo = licOwner.getLicenceNo();
		this.mark = licOwner.getMarkText();
		this.mobilePhoneNo = licOwner.getTelephoneWork();
		this.ownerType = licOwner.getOwnerType();
		this.parishCode = licOwner.getParishDesc();
		this.poBox = licOwner.getPoBoxNo();
		this.postOffice = licOwner.getPoLocationName();
		this.streetName = licOwner.getStreetName();
		this.streetNo = licOwner.getStreetNo();
		this.trn = licOwner.getTrnNbr();
		this.workPhoneNo = licOwner.getTelephoneWork();
		
	}



	public String getTrn() {
		return trn;
	}


	public void setTrn(String trn) {
		this.trn = trn;
	}


	public RoadLicenceOwnerBO(Integer licenceNo, String firstName,
			String lastName, String trn) {
		super();
		this.licenceNo = licenceNo;
		this.firstName = firstName;
		this.lastName = lastName;
		this.trn = trn;
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


	public String getMark() {
		return mark;
	}


	public void setMark(String mark) {
		this.mark = mark;
	}


	public String getPoBox() {
		return poBox;
	}


	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}


	public String getPostOffice() {
		return postOffice;
	}


	public void setPostOffice(String postOffice) {
		this.postOffice = postOffice;
	}


	public String getParishCode() {
		return parishCode;
	}


	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}


	public String getHomePhoneNo() {
		return homePhoneNo;
	}


	public void setHomePhoneNo(String homePhoneNo) {
		this.homePhoneNo = homePhoneNo;
	}


	public String getWorkPhoneNo() {
		return workPhoneNo;
	}


	public void setWorkPhoneNo(String workPhoneNo) {
		this.workPhoneNo = workPhoneNo;
	}


	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}


	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
	}


	public String getOwnerType() {
		return ownerType;
	}


	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	
	
	
	
}
