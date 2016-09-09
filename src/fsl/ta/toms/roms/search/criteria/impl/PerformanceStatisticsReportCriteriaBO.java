/**
 * Created By: oanguin
 * Date: May 20, 2013
 *
 */
package fsl.ta.toms.roms.search.criteria.impl;

import java.util.Date;
import java.util.List;

import fsl.ta.toms.roms.search.criteria.ReportSearchCriteria;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: May 20, 2013
 */
public class PerformanceStatisticsReportCriteriaBO extends ParentReportCriteria implements ReportSearchCriteria 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PerformanceStatisticsReportCriteriaBO() 
	{
		super();
		
	}

	Date startDate;
	Date endDate;
	String TAOfficeRegion;
	String TAStaffId;
	String TAStaffTRN;
	String TeamLeadId;
	String TeamLeadTRN;
	String OperationCategory;
	Integer roadOperationId;
	
	String roadOperationName;
	
	List<Integer> strategyIds;
	
	private String strategyDescriptions;
	
	private String TAOfficeDescription,
	   TAStaffName,
	   OperationCategoryDescription,
	   TeamLeadName;
	
	
	
	public void setTAOfficeDescription(String tAOfficeDescription) {
		TAOfficeDescription = tAOfficeDescription;
	}

	public void setTAStaffName(String tAStaffName) {
		TAStaffName = tAStaffName;
	}

	public void setOperationCategoryDescription(String operationCategoryDescription) 
	{
		
		this.OperationCategoryDescription = operationCategoryDescription;
	}

	


	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}

	public String getRoadOperationName() {
		return roadOperationName;
	}

	public void setRoadOperationName(String roadOperationName) {
		this.roadOperationName = roadOperationName;
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

	public String getTAOfficeRegion() {
		return TAOfficeRegion;
	}

	public void setTAOfficeRegion(String tAOfficeRegion) {
		TAOfficeRegion = tAOfficeRegion;
	}

	public String getTAStaffId() {
		return TAStaffId;
	}

	public void setTAStaffId(String tAStaffId) {
		TAStaffId = tAStaffId;
	}

	public String getOperationCategory() {
		return OperationCategory;
	}

	public void setOperationCategory(String operationCategory) {
		OperationCategory = operationCategory;
	}
	
	
	
	public String getTeamLeadId() {
		return TeamLeadId;
	}

	public void setTeamLeadId(String teamLeadId) {
		TeamLeadId = teamLeadId;
	}

	public void setTeamLeadName(String teamLeadName) {
		TeamLeadName = teamLeadName;
	}

	@Override
	public String getSearchCriteriaString() 
	{
		StringBuilder searchCriteria = new StringBuilder("");
		
		
		searchCriteria.append(this.getCriterionString("TA Office Region", this.TAOfficeDescription));
		searchCriteria.append(this.getCriterionString("Team Lead", this.TeamLeadName));
		searchCriteria.append(this.getCriterionString("TA Staff", this.TAStaffName));
		searchCriteria.append(this.getCriterionString("Operation Category", this.OperationCategoryDescription));
		searchCriteria.append(this.getCriterionString("Road Operation ID", this.roadOperationId));
		searchCriteria.append(this.getCriterionString("Road Operation Name", this.roadOperationName));
		searchCriteria.append(this.getCriterionString("Strategies Employed", this.strategyDescriptions));
		
		return searchCriteria.toString();
	}

	public PerformanceStatisticsReportCriteriaBO(Date startDate, Date endDate,
			String tAOfficeRegion, String tAStaffId, String operationCategory) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		TAOfficeRegion = tAOfficeRegion;
		TAStaffId = tAStaffId;
		OperationCategory = operationCategory;
	}

	public String getTAStaffTRN() {
		return TAStaffTRN;
	}

	public void setTAStaffTRN(String tAStaffTRN) {
		TAStaffTRN = tAStaffTRN;
	}


	public String getTeamLeadTRN() {
		return TeamLeadTRN;
	}

	public void setTeamLeadTRN(String teamLeadTRN) {
		TeamLeadTRN = teamLeadTRN;
	}

	public List<Integer> getStrategyIds() {
		return strategyIds;
	}

	public void setStrategyIds(List<Integer> strategyIds) {
		this.strategyIds = strategyIds;
	}

	public void setStrategyDescriptions(String strategyDescriptions) {
		this.strategyDescriptions = strategyDescriptions;
	}

	
	
}
