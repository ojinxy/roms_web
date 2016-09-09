package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.Date;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

public class RoadCompliancyCriteriaBO implements Serializable, SearchCriteria{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4253069297197755427L;
	/**
	 * 
	 */
	
	private String trn, firstName,lastName,middleName;
	
	private String region;
	
	private String operationName;
	
	private Integer operationID;
	
	private String currentUserName;
	
	private String status;
	
	private Date scheduledStartDateRange;
	
	private Date scheduledEndDateRange;
	
	private Date actualStartDateRange;
	
	private Date actualEndDateRange;
	
	private Date roadCheckStartDateRange;
	
	private Date roadCheckEndDateRange;
	
	private String staffID;
	
	private boolean isScheduledDTime;
	
	private Integer complianceId;
	
	
	public RoadCompliancyCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getTrn() {
		return trn;
	}


	public void setTrn(String trn) {
		this.trn = trn;
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


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public String getOperationName() {
		return operationName;
	}


	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}


	public Integer getOperationID() {
		return operationID;
	}


	public void setOperationID(Integer operationID) {
		this.operationID = operationID;
	}


	public String getCurrentUserName() {
		return currentUserName;
	}


	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getStaffID() {
		return staffID;
	}


	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}


	public Date getScheduledStartDateRange() {
		return scheduledStartDateRange;
	}


	public void setScheduledStartDateRange(Date scheduledStartDateRange) {
		this.scheduledStartDateRange = scheduledStartDateRange;
	}


	public Date getScheduledEndDateRange() {
		return scheduledEndDateRange;
	}


	public void setScheduledEndDateRange(Date scheduledEndDateRange) {
		this.scheduledEndDateRange = scheduledEndDateRange;
	}


	public Date getActualStartDateRange() {
		return actualStartDateRange;
	}


	public void setActualStartDateRange(Date actualStartDateRange) {
		this.actualStartDateRange = actualStartDateRange;
	}


	public Date getActualEndDateRange() {
		return actualEndDateRange;
	}


	public void setActualEndDateRange(Date actualEndDateRange) {
		this.actualEndDateRange = actualEndDateRange;
	}


	public boolean isScheduledDTime() {
		return isScheduledDTime;
	}


	public void setScheduledDTime(boolean isScheduledDTime) {
		this.isScheduledDTime = isScheduledDTime;
	}


	public Integer getComplianceId() {
		return complianceId;
	}


	public void setComplianceId(Integer complianceId) {
		this.complianceId = complianceId;
	}


	public Date getRoadCheckStartDateRange() {
		return roadCheckStartDateRange;
	}


	public void setRoadCheckStartDateRange(Date roadCheckStartDateRange) {
		this.roadCheckStartDateRange = roadCheckStartDateRange;
	}


	public Date getRoadCheckEndDateRange() {
		return roadCheckEndDateRange;
	}


	public void setRoadCheckEndDateRange(Date roadCheckEndDateRange) {
		this.roadCheckEndDateRange = roadCheckEndDateRange;
	}



	
	


}
