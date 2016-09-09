package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

public class RoadOperationCriteriaBO implements Serializable, SearchCriteria{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5248408933952169409L;
	
	private Integer roadOperationId;
	private String operationName;
	private String categoryId;
	private Integer strategyId;
	private String teamLeadStaffId;
	private String schedulerStaffId;
	private String officeLocCode;
	private String parishCode;
	private Integer locationId;
	private Integer arteryId;
	private Date operationStartDate;
	private Date operationEndDate;
	private String operationStartTime;
	private String operationEndTime;
	private boolean isScheduledDTime;
	private boolean isUnAuthorized;
	private boolean isBackDated;
	private String statusId;
	
	public RoadOperationCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		if(StringUtils.isNotBlank(operationName))
			this.operationName = operationName.trim();
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		if(StringUtils.isNotBlank(categoryId))
			this.categoryId = categoryId.trim();
	}

	public String getTeamLeadStaffId() {
		return teamLeadStaffId;
	}

	public void setTeamLeadStaffId(String teamLeadStaffId) {
		if(StringUtils.isNotBlank(teamLeadStaffId))
			this.teamLeadStaffId = teamLeadStaffId.trim();
	}

	public String getSchedulerStaffId() {
		return schedulerStaffId;
	}

	public void setSchedulerStaffId(String schedulerStaffId) {
		if(StringUtils.isNotBlank(schedulerStaffId))
			this.schedulerStaffId = schedulerStaffId.trim();
	}

	public String getOfficeLocCode() {
		return officeLocCode;
	}

	public void setOfficeLocCode(String officeLocCode) {
		if(StringUtils.isNotBlank(officeLocCode))
			this.officeLocCode = officeLocCode.trim();
	}

	public String getParishCode() {
		return parishCode;
	}

	public void setParishCode(String parishCode) {
		if(StringUtils.isNotBlank(parishCode))
			this.parishCode = parishCode.trim();
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}

	
	public Integer getArteryId() {
		return arteryId;
	}

	public void setArteryId(Integer arteryId) {
		this.arteryId = arteryId;
	}

	public Date getOperationStartDate() {
		return operationStartDate;
	}

	public void setOperationStartDate(Date operationStartDate) {
		this.operationStartDate = operationStartDate;
	}

	public Date getOperationEndDate() {
		return operationEndDate;
	}

	public void setOperationEndDate(Date operationEndDate) {
		this.operationEndDate = operationEndDate;
	}

	public String getOperationStartTime() {
		return operationStartTime;
	}

	public void setOperationStartTime(String operationStartTime) {
		if(StringUtils.isNotBlank(operationStartTime))
			this.operationStartTime = operationStartTime.trim();
	}

	public String getOperationEndTime() {
		return operationEndTime;
	}

	public void setOperationEndTime(String operationEndTime) {
		if(StringUtils.isNotBlank(operationEndTime))
			this.operationEndTime = operationEndTime.trim();
	}

	public boolean isScheduledDTime() {
		return isScheduledDTime;
	}

	public void setScheduledDTime(boolean isScheduledDTime) {
		this.isScheduledDTime = isScheduledDTime;
	}

	

	public boolean isBackDated() {
		return isBackDated;
	}

	public void setBackDated(boolean isBackDated) {
		this.isBackDated = isBackDated;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public boolean isUnAuthorized() {
		return isUnAuthorized;
	}

	public void setUnAuthorized(boolean isUnAuthorized) {
		this.isUnAuthorized = isUnAuthorized;
	}

	
	
	
}
