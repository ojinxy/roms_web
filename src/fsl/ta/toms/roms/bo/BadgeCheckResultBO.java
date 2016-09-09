package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import fsl.ta.toms.roms.dataobjects.BadgeCheckResultDO;

public class BadgeCheckResultBO implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = -7782853911310356453L;

	Integer badgeCheckId;
	String badgeNumber;
	String badgeType;
	String badgeTypeDescription;
	String firstName;
	String middleName;
	String lastName;
	Integer biometricsSno;
	byte[] photograph;
	Date issueDate;
	Date expiryDate;
	String currentUserName;
	String comment;
	RoadCheckBO roadCheckBO;
	Integer complianceID;
	
	

	public BadgeCheckResultBO(Integer badgeCheckId, String badgeNumber,
			String badgeType, String badgeTypeDescription, String firstName,
			String middleName, String lastName, Integer biometricsSno,
			byte[] photograph, Date issueDate, Date expiryDate,
			String currentUserName, String comment, RoadCheckBO roadCheckBO,
			Integer complianceID) {
		super();
		this.badgeCheckId = badgeCheckId;
		this.badgeNumber = badgeNumber;
		this.badgeType = badgeType;
		this.badgeTypeDescription = badgeTypeDescription;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.biometricsSno = biometricsSno;
		this.photograph = photograph;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.currentUserName = currentUserName;
		this.comment = comment;
		this.roadCheckBO = roadCheckBO;
		this.complianceID = complianceID;
	}

	public BadgeCheckResultBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BadgeCheckResultBO(BadgeCheckResultDO badgeCheckResultDO,
			byte[] photoImg) {
		this.badgeCheckId = badgeCheckResultDO.getBadgeCheckId();
		this.roadCheckBO = new RoadCheckBO(badgeCheckResultDO.getRoadCheck());
		this.badgeNumber = badgeCheckResultDO.getBadgeNumber();
		this.badgeTypeDescription = badgeCheckResultDO.getBadgeTypeDescription();
		this.firstName = badgeCheckResultDO.getFirstName();
		this.middleName = badgeCheckResultDO.getMiddleName();
		this.lastName = badgeCheckResultDO.getLastName();
		this.biometricsSno = badgeCheckResultDO.getBiometricsSno();
		this.photograph = photoImg;
		this.issueDate = badgeCheckResultDO.getIssueDate();
		this.expiryDate = badgeCheckResultDO.getExpiryDate();
	}


	public Integer getBadgeCheckId() {
		return badgeCheckId;
	}
	public void setBadgeCheckId(Integer badgeCheckId) {
		this.badgeCheckId = badgeCheckId;
	}
	public String getBadgeNumber() {
		return badgeNumber;
	}
	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}
	public String getBadgeTypeDescription() {
		return badgeTypeDescription;
	}
	public void setBadgeTypeDescription(String badgeTypeDescription) {
		this.badgeTypeDescription = badgeTypeDescription;
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
	
	
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getCurrentUserName() {
		return currentUserName;
	}
	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}
	public String getBadgeType() {
		return badgeType;
	}
	public void setBadgeType(String badgeType) {
		this.badgeType = badgeType;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}


	public Integer getBiometricsSno() {
		return biometricsSno;
	}


	public void setBiometricsSno(Integer biometricsSno) {
		this.biometricsSno = biometricsSno;
	}


	public byte[] getPhotograph() {
		return photograph;
	}


	public void setPhotograph(byte[] photograph) {
		this.photograph = photograph;
	}

	public RoadCheckBO getRoadCheckBO() {
		return roadCheckBO;
	}

	public void setRoadCheckBO(RoadCheckBO roadCheckBO) {
		this.roadCheckBO = roadCheckBO;
	}

	public Integer getComplianceID() {
		return complianceID;
	}

	public void setComplianceID(Integer complianceID) {
		this.complianceID = complianceID;
	}

	
}
