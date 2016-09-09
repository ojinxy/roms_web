/**
 * Created By: oanguin
 * Date: May 24, 2013
 *
 */
package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.search.criteria.ReportSearchCriteria;

/**
 * @author oanguin
 * Created Date: May 24, 2013
 */
public class EventAuditReportCriteriaBO extends ParentReportCriteria implements
		Serializable, ReportSearchCriteria 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Date startDate;
	
	Date endDate;
	
	String refType;
	
	String createUserName;
	
	Integer eventCode;
	
	List<String> TAOfficeRegion = new ArrayList<String>();
	
	String sortBy;
	
	
	private String TAOfficeDescription, eventDescription, refTypeDesc, userFullName, sortByDesc;


	


	public void setSortByDesc(String sortByDesc) {
		this.sortByDesc = sortByDesc;
	}




	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}




	public void setRefTypeDesc(String refTypeDesc) {
		this.refTypeDesc = refTypeDesc;
	}




	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}




	public void setTAOfficeDescription(String tAOfficeDescription) {
		TAOfficeDescription = tAOfficeDescription;
	}




	public String getCreateUserName() {
		return createUserName;
	}




	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}




	public String getRefType() {
		return refType;
	}




	public void setRefType(String refType) {
		this.refType = refType;
	}




	public Date getStartDate() {
		return startDate;
	}




	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}




	public Date getEndDate() {
		return endDate;
	}




	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public Integer getEventCode() {
		return eventCode;
	}




	public void setEventCode(Integer eventCode) {
		this.eventCode = eventCode;
	}




	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.search.criteria.ReportSearchCriteria#getSearchCriteriaString()
	 */
	@Override
	public String getSearchCriteriaString() 
	{
		StringBuilder searchCriteria = new StringBuilder("");		

		searchCriteria.append(this.getCriterionString("Event Ref Type", this.refTypeDesc));
		searchCriteria.append(this.getCriterionString("Create User Name", this.userFullName));
		searchCriteria.append(this.getCriterionString("Event Description", this.eventDescription));
		searchCriteria.append(this.getCriterionString("TA Office Region", this.TAOfficeDescription));
		searchCriteria.append(this.getCriterionString("Sort By", this.sortByDesc));
	
		
		return searchCriteria.toString();
	}







	public EventAuditReportCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}




	public EventAuditReportCriteriaBO(Date startDate, Date endDate,
			String refType, String createUserName, Integer eventCode) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.refType = refType;
		this.createUserName = createUserName;
		this.eventCode = eventCode;
	}









	public String getSortBy() {
		return sortBy;
	}




	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}








	public List<String> getTAOfficeRegion() {
		return TAOfficeRegion;
	}




	public void setTAOfficeRegion(List<String> tAOfficeRegion) {
		TAOfficeRegion = tAOfficeRegion;
	}




	public EventAuditReportCriteriaBO(Date startDate, Date endDate,
			String refType, String createUserName, Integer eventCode,
			List<String> tAOfficeRegion, String sortBy) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.refType = refType;
		this.createUserName = createUserName;
		this.eventCode = eventCode;
		TAOfficeRegion = tAOfficeRegion;
		this.sortBy = sortBy;
	
	}






	



	
}
