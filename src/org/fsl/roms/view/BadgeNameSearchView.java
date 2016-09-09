package org.fsl.roms.view;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;

public class BadgeNameSearchView implements Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 3467776181457277159L;
	private String trn;
	private String fullName;
	private String firstName;
	private String lastName;
	private String midName;
	private String photoURL;
	private String badgeDesc;
	
	
	
	
	public BadgeNameSearchView() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTrn() {
		return trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhotoURL() {
		return photoURL;
	}
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
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
	public String getMidName() {
		return midName;
	}
	public void setMidName(String midName) {
		this.midName = midName;
	}
	public String getBadgeDesc() {
		return badgeDesc;
	}
	public void setBadgeDesc(String badgeDesc) {
		this.badgeDesc = badgeDesc;
	}
	
	
}
