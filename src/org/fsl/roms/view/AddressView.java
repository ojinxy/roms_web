package org.fsl.roms.view;

import java.io.Serializable;

public class AddressView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2884426592710816196L;


	private String markText;
	private String parish;
	private String poBoxNo;
	private String poLocationName;
	private String streetName;
	private String streetNo;
	private String addressLine1;
	private String addressLine2;
	private String addressLine2NL;
	private String parishDescription;
	
	public AddressView() {
		super();
		// TODO Auto-generated constructor stub
	}


	public AddressView(String markText, String parish, String poBoxNo,
			String poLocationName, String streetName, String streetNo,
			String addressLine1, String addressLine2, String addressLine2NL,
			String parishDescription) {
		super();
		this.markText = markText;
		this.parish = parish;
		this.poBoxNo = poBoxNo;
		this.poLocationName = poLocationName;
		this.streetName = streetName;
		this.streetNo = streetNo;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressLine2NL = addressLine2NL;
		this.parishDescription = parishDescription;
	}


	public String getMarkText() {
		return markText;
	}


	public void setMarkText(String markText) {
		if(markText!=null){
			this.markText = markText.trim();
		}
	}


	public String getParish() {
		return parish;
	}


	public void setParish(String parish) {
		if(parish!=null){
			this.parish = parish.trim();
		}
	}


	public String getPoBoxNo() {
		return poBoxNo;
	}


	public void setPoBoxNo(String poBoxNo) {
		if(poBoxNo!=null){
			this.poBoxNo = poBoxNo.trim();
		}
	}


	public String getPoLocationName() {
		return poLocationName;
	}


	public void setPoLocationName(String poLocationName) {
		if(poLocationName!=null){
			this.poLocationName = poLocationName.trim();
		}
	}


	public String getStreetName() {
		return streetName;
	}


	public void setStreetName(String streetName) {
		if(streetName!=null){
			this.streetName = streetName.trim();
		}
	}


	public String getStreetNo() {
		return streetNo;
	}


	public void setStreetNo(String streetNo) {
		if(streetNo!=null){
			this.streetNo = streetNo.trim();
		}
	}


	public String getAddressLine1() {
		return addressLine1;
	}


	public void setAddressLine1(String addressLine1) {
		if(addressLine1!=null){
			this.addressLine1 = addressLine1.trim();
		}
	}


	public String getAddressLine2() {
		return addressLine2;
	}


	public void setAddressLine2(String addressLine2) {
		if(addressLine2!=null){
			this.addressLine2 = addressLine2.trim();
		}
	}


	public String getAddressLine2NL() {
		return addressLine2NL;
	}


	public void setAddressLine2NL(String addressLine2NL) {
		if(addressLine2NL!=null){
			this.addressLine2NL = addressLine2NL.trim();
		}
	}


	public String getParishDescription() {

		return parishDescription;
	}


	public void setParishDescription(String parishDescription) {

		this.parishDescription = parishDescription;
	}
	
	
	
}
