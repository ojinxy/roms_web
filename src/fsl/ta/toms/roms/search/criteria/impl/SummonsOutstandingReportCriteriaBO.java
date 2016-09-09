/**
 * Created By: oanguin
 * Date: May 13, 2013
 *
 */
package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.WordUtils;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.search.criteria.ReportSearchCriteria;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * This class contains search criteria for Outstanding Summons Report
 * @author oanguin
 * Created Date: May 13, 2013
 */
public class SummonsOutstandingReportCriteriaBO extends ParentReportCriteria
		implements Serializable, ReportSearchCriteria 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Date operationStartDate,
		 operationEndDate;
	
	String TAOfficeRegion,
		   TAStaffId,
		   operationCategory,
		   roadOperationName,
		   TAStaffTRN,
		   offenderFirstName,
		   offenderLastName,
		   offenderTRN;
	
	private String TAOfficeRegionDesc,
				   TAStaffName,
				   operationCategoryDesc,
				   offenderName,
				   roadOperationDesc;
	
	Integer roadOperationId,
			offenderId;
	
		
	@Override
	public String getSearchCriteriaString() 
	{
		StringBuilder criteriaString = new StringBuilder("");
		
		
		criteriaString.append(this.getCriterionString("TA Office Region",this.TAOfficeRegionDesc));
		criteriaString.append(this.getCriterionString("TA Staff Name", this.TAStaffName));
		criteriaString.append(this.getCriterionString("Operation Category", this.operationCategoryDesc));
		criteriaString.append(this.getCriterionString("Road Operation ID", this.roadOperationId));
		criteriaString.append(this.getCriterionString("Road Operation Name", this.roadOperationName));
		criteriaString.append(this.getCriterionString("Offender Name", this.offenderName));
		criteriaString.append(this.getCriterionString("Offender First Name", this.offenderFirstName));
		criteriaString.append(this.getCriterionString("Offender Last Name", this.offenderLastName));
		
		return criteriaString.toString();
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


	public Integer getOffenderId() {
		return offenderId;
	}


	public void setOffenderId(Integer offenderId) {
		this.offenderId = offenderId;
	}


	public SummonsOutstandingReportCriteriaBO(Date operationStartDate,
			Date operationEndDate, String tAOfficeRegion, String tAStaffId,
			String operationCategory, Integer roadOperationId,
			Integer offenderId) {
		super();
		this.operationStartDate = operationStartDate;
		this.operationEndDate = operationEndDate;
		TAOfficeRegion = tAOfficeRegion;
		TAStaffId = tAStaffId;
		this.operationCategory = operationCategory;
		this.roadOperationId = roadOperationId;
		this.offenderId = offenderId;
	}


	public SummonsOutstandingReportCriteriaBO() 
	{
		super();
		
	}


	public void setTAOfficeRegionDesc(String tAOfficeRegionDesc) 
	{
		
		
		
		if(StringUtil.isSet(tAOfficeRegionDesc))
			TAOfficeRegionDesc = WordUtils.capitalize(tAOfficeRegionDesc.toLowerCase(),
					Constants.STRING_DELIM_ARRAY);
		else
			TAOfficeRegionDesc = tAOfficeRegionDesc;
	}


	public void setTAStaffName(String tAStaffName) {
		TAStaffName = tAStaffName;
	}


	public void setOperationCategoryDesc(String operationCategoryDesc) {
		
		if(StringUtil.isSet(operationCategoryDesc))
			this.operationCategoryDesc = WordUtils.capitalize(operationCategoryDesc.toLowerCase(),
					Constants.STRING_DELIM_ARRAY);
		else
			this.operationCategoryDesc = operationCategoryDesc;
	}


	public void setRoadOperationName(String roadOperationName) {
		this.roadOperationName = roadOperationName;
	}


	public void setOffenderName(String offenderName) {
		this.offenderName = offenderName;
	}


	public String getRoadOperationName() 
	{
		return roadOperationName;
	}


	public void setRoadOperationDesc(String roadOperationDesc) {
		this.roadOperationDesc = roadOperationDesc;
	}


	public String getTAStaffTRN() {
		return TAStaffTRN;
	}


	public void setTAStaffTRN(String tAStaffTRN) {
		TAStaffTRN = tAStaffTRN;
	}


	public String getOffenderFirstName() {
		return offenderFirstName;
	}


	public void setOffenderFirstName(String offenderFirstName) {
		this.offenderFirstName = offenderFirstName;
	}


	public String getOffenderLastName() {
		return offenderLastName;
	}


	public void setOffenderLastName(String offenderLastName) {
		this.offenderLastName = offenderLastName;
	}


	public String getOffenderTRN() {
		return offenderTRN;
	}


	public void setOffenderTRN(String offenderTRN) {
		this.offenderTRN = offenderTRN;
	}
	
	
	
}
