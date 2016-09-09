package org.fsl.roms.view;

import java.io.Serializable;

public class WitnessView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5289299451269381843L;
	private String lastName;
	private String firstName;
	private String middleName;
	private AddressView addressView;
	private String mobilePhoneNo;
	private String homePhoneNo;
	private String workPhoneNo;
	private boolean rowSelected;
	private String parishDesc;
	
	public WitnessView() {
		super();
		this.addressView = new AddressView();
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public AddressView getAddressView() {
		return addressView;
	}
	public void setAddressView(AddressView addressView) {
		this.addressView = addressView;
	}
	public String getMobilePhoneNo() {
		return mobilePhoneNo;
	}
	public void setMobilePhoneNo(String mobilePhoneNo) {
		this.mobilePhoneNo = mobilePhoneNo;
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
	public boolean isRowSelected() {
		return rowSelected;
	}
	public void setRowSelected(boolean rowSelected) {
		this.rowSelected = rowSelected;
	}
	public String getParishDesc() {
		return parishDesc;
	}
	public void setParishDesc(String parishDesc) {
		this.parishDesc = parishDesc;
	}
	
	


	
	
}
