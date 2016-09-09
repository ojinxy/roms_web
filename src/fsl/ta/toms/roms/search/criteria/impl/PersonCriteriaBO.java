package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

public class PersonCriteriaBO implements Serializable, SearchCriteria{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1814962227844962586L;
	private String personId;
	private String trnNbr, firstName , middleName,lastName;
	private String telephoneHome,telephoneCell,telephoneWork;
	private String markText, streetNo,streetName,poLocationName,poBoxNo;
	private String parishCode;
	

	public PersonCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getPersonId() {
		return personId;
	}


	public void setPersonId(String personId) {
		this.personId = personId;
	}


	public String getTrnNbr() {
		return trnNbr;
	}


	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
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


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getTelephoneHome() {
		return telephoneHome;
	}


	public void setTelephoneHome(String telephoneHome) {
		this.telephoneHome = telephoneHome;
	}


	public String getTelephoneCell() {
		return telephoneCell;
	}


	public void setTelephoneCell(String telephoneCell) {
		this.telephoneCell = telephoneCell;
	}


	public String getTelephoneWork() {
		return telephoneWork;
	}


	public void setTelephoneWork(String telephoneWork) {
		this.telephoneWork = telephoneWork;
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


	public String getPoBoxNo() {
		return poBoxNo;
	}


	public void setPoBoxNo(String poBoxNo) {
		this.poBoxNo = poBoxNo;
	}


	public String getParishCode() {
		return parishCode;
	}


	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
}