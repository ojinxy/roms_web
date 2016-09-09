/**
 * Created By: oanguin
 * Date: May 10, 2013
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
 * Created Date: May 10, 2013
 */
public class OperationSummaryReportCriteriaBO extends ParentReportCriteria
		implements Serializable,ReportSearchCriteria 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Date operationStartDate,
		 operationEndDate;
	
	String operationCategory,
		   roadOperationName;
	
	List<String> teamLeadStaffIds = new ArrayList<String>();
	List<String> teamLeadTRNs = new ArrayList<String>();
	
	List<String> TAOfficeRegions = new ArrayList<String>();
	
	Integer roadOperationId;
	
	List<Integer> roadOperationIds = new ArrayList<Integer>();
	
	private String TAOfficeDescription,TAStaffFullName, OperationCategoryDescription;
	
	
	

	
	public String getRoadOperationName() {
		return roadOperationName;
	}



	public void setRoadOperationName(String roadOperationName) {
		this.roadOperationName = roadOperationName;
	}



	public void setTAOfficeDescription(String tAOfficeDescription) {
		TAOfficeDescription = tAOfficeDescription;
	}



	public void setTAStaffFullName(String tAStaffFullName) {
		TAStaffFullName = tAStaffFullName;
	}



	public void setOperationCategoryDescription(String operationCategoryDescription) {
		OperationCategoryDescription = operationCategoryDescription;
	}



	public OperationSummaryReportCriteriaBO(Date operationStartDate,
			Date operationEndDate, List<String> tAOfficeRegions, List<String> teamLeadStaffIds,
			String operationCategory, Integer roadOperationId) {
		super();
		this.operationStartDate = operationStartDate;
		this.operationEndDate = operationEndDate;
		this.TAOfficeRegions = tAOfficeRegions;
		this.teamLeadStaffIds = teamLeadStaffIds;
		this.operationCategory = operationCategory;
		this.roadOperationId = roadOperationId;
	}
	
	public OperationSummaryReportCriteriaBO(Date operationStartDate,
			Date operationEndDate, List<String> tAOfficeRegion, List<String> teamLeadStaffIds,
			String operationCategory, Integer roadOperationId, String roadOpearationName) {
		super();
		this.operationStartDate = operationStartDate;
		this.operationEndDate = operationEndDate;
		this.TAOfficeRegions = tAOfficeRegion;
		this.teamLeadStaffIds = teamLeadStaffIds;
		this.operationCategory = operationCategory;
		this.roadOperationId = roadOperationId;
		this.roadOperationName = roadOpearationName;
	}
	
	

	public OperationSummaryReportCriteriaBO() {
		super();
		
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




	public List<String> getTAOfficeRegions() {
		return TAOfficeRegions;
	}



	public void setTAOfficeRegions(List<String> tAOfficeRegions) {
		TAOfficeRegions = tAOfficeRegions;
	}



	public String getOperationCategory() {
		return operationCategory;
	}


	public void setOperationCategory(String operationCategory) {
		this.operationCategory = operationCategory;
	}


	public Integer getRoadOperationId() {
		return roadOperationId;
	}


	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}


	@Override
	public String getSearchCriteriaString() {
		StringBuilder criteriaString = new StringBuilder("");
		
		criteriaString.append(this.getCriterionString("TA Office Region", this.TAOfficeDescription));
		criteriaString.append(this.getCriterionString("TA Staff Name", this.TAStaffFullName));
		criteriaString.append(this.getCriterionString("Operation Category", this.OperationCategoryDescription));
		criteriaString.append(this.getCriterionString("Road Operation Id", this.roadOperationId));
		criteriaString.append(this.getCriterionString("Road Operation Name", this.roadOperationName));
		
		return criteriaString.toString();
	}



	public List<String> getTeamLeadStaffIds() {
		return teamLeadStaffIds;
	}



	public void setTeamLeadStaffIds(List<String> teamLeadStaffIds) {
		this.teamLeadStaffIds = teamLeadStaffIds;
	}



	public List<String> getTeamLeadTRNs() {
		return teamLeadTRNs;
	}



	public void setTeamLeadTRNs(List<String> teamLeadTRNs) {
		this.teamLeadTRNs = teamLeadTRNs;
	}



	public List<Integer> getRoadOperationIds() {
		return roadOperationIds;
	}



	public void setRoadOperationIds(List<Integer> roadOperationIds) {
		this.roadOperationIds = roadOperationIds;
	}




	
}
