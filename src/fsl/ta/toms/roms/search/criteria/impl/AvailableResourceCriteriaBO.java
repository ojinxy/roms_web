package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

public class AvailableResourceCriteriaBO implements Serializable, SearchCriteria{

	/**
	 * 
	 */
	private static final long serialVersionUID = -593460838524010709L;
	
	private String categoryId;
	private Date scheduledStartDate;
	private String scheduledStartTime;
	private Date scheduledEndDate;
	private String scheduledEndTime;
	//private String officeLocCode;
	private List<String> officeLocCodeList = new ArrayList<String>();
	private List<String> parishCodeList = new ArrayList<String>();;
	private Integer resourceId;
	private Integer roadOperationId;
	private Integer policeOfficerCompNum;
	private String policeOfficerFirstName;
	private String policeOfficerLastName;
	private String policeStation;
	private Boolean usePoliceMaxFilter;

	
	public AvailableResourceCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	

	public Date getScheduledStartDate() {
		return scheduledStartDate;
	}

	public void setScheduledStartDate(Date scheduledStartDate) {
		this.scheduledStartDate = scheduledStartDate;
	}

	public String getScheduledStartTime() {
		return scheduledStartTime;
	}

	public void setScheduledStartTime(String scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}

	public Date getScheduledEndDate() {
		return scheduledEndDate;
	}

	public void setScheduledEndDate(Date scheduledEndDate) {
		this.scheduledEndDate = scheduledEndDate;
	}

	public String getScheduledEndTime() {
		return scheduledEndTime;
	}

	public void setScheduledEndTime(String scheduledEndTime) {
		this.scheduledEndTime = scheduledEndTime;
	}


	

	public List<String> getOfficeLocCodeList() {
		return officeLocCodeList;
	}

	public void setOfficeLocCodeList(List<String> officeLocCodeList) {
		this.officeLocCodeList = officeLocCodeList;
	}

	public List<String> getParishCodeList() {
		return parishCodeList;
	}

	public void setParishCodeList(List<String> parishCodeList) {
		this.parishCodeList = parishCodeList;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}
	
	

	public Integer getPoliceOfficerCompNum()
	{
		return policeOfficerCompNum;
	}

	public void setPoliceOfficerCompNum(Integer policeOfficerCompNum)
	{
		this.policeOfficerCompNum = policeOfficerCompNum;
	}

	public String getPoliceOfficerFirstName()
	{
		return policeOfficerFirstName;
	}

	public void setPoliceOfficerFirstName(String policeOfficerFirstName)
	{
		this.policeOfficerFirstName = policeOfficerFirstName;
	}

	public String getPoliceOfficerLastName()
	{
		return policeOfficerLastName;
	}

	public void setPoliceOfficerLastName(String policeOfficerLastName)
	{
		this.policeOfficerLastName = policeOfficerLastName;
	}

	public String getPoliceStation()
	{
		return policeStation;
	}

	public void setPoliceStation(String policeStation)
	{
		this.policeStation = policeStation;
	}
	
	

	public Boolean getUsePoliceMaxFilter()
	{
		return usePoliceMaxFilter;
	}

	public void setUsePoliceMaxFilter(Boolean usePoliceMaxFilter)
	{
		this.usePoliceMaxFilter = usePoliceMaxFilter;
	}

	@Override
	public String toString() {
		return "AvailableResourceCriteriaBO [categoryId=" + categoryId
				+ ", scheduledStartDate=" + scheduledStartDate
				+ ", scheduledStartTime=" + scheduledStartTime
				+ ", scheduledEndDate=" + scheduledEndDate
				+ ", scheduledEndTime=" + scheduledEndTime
				+ ", officeLocCodeList=" + officeLocCodeList
				+ ", policeOfficerFirstName=" + policeOfficerFirstName
				+ ", policeOfficerLastName=" + policeOfficerLastName
				+ ", policeOfficerCompNum=" + policeOfficerCompNum
				+ ", policeStation=" + policeStation
				+ ", parishCodeList=" + parishCodeList + ", resourceId="
				+ resourceId + ", roadOperationId=" + roadOperationId + "]";
	}

	
}
