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
public class SummonsReportCriteriaBO extends ParentReportCriteria
		implements Serializable, ReportSearchCriteria 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Date operationStartDate,
		 operationEndDate,
		 printStartDate,
		 printEndDate,
		 issuedStartDate,
		 issuedEndDate,
		 offenceStartDate,
		 offenceEndDate;
	
	String TAOfficeRegion,
		   TAStaffId,
		   TAStaffTRN,
		   OffenderTRN,
		   OffenderFirstName,
		   OffenderLastName,
		   operationCategory,
		   roadOperationName,
		   status;
	
	private String TAOfficeRegionDesc,
				   TAStaffName,
				   operationCategoryDesc,
				   offenderName,
				   roadOperationDesc;
	
	Integer roadOperationId,
			offenderId;
	
		
	@Override
	public String getSearchCriteriaString() {
		StringBuilder criteriaString = new StringBuilder("");
		
		
		criteriaString.append(this.getCriterionString("TA Office Region",this.TAOfficeRegionDesc));
		criteriaString.append(this.getCriterionString("TA Staff Name", this.TAStaffName));
		criteriaString.append(this.getCriterionString("Operation Category", this.operationCategoryDesc));
		criteriaString.append(this.getCriterionString("Road Operation ID", this.roadOperationId));
		criteriaString.append(this.getCriterionString("Road Operation Name", this.roadOperationName));
		criteriaString.append(this.getCriterionString("Offender Name", this.offenderName));
		criteriaString.append(this.getCriterionString("Status", this.getStatus()));
		
		if(this.operationStartDate != null && this.operationEndDate != null)
		{
			criteriaString.append(this.getCriterionString("Operation Start Date", this.getOperationStartDate()));
			criteriaString.append(this.getCriterionString("Operation End Date", this.getOperationEndDate()));
		}
		
		if(this.issuedStartDate != null && this.issuedEndDate != null)
		{
			criteriaString.append(this.getCriterionString("Issue Start Date", this.getIssuedStartDate()));
			criteriaString.append(this.getCriterionString("Issue End Date", this.getIssuedEndDate()));
		}
		
		if(this.printStartDate != null && this.printEndDate != null)
		{
			criteriaString.append(this.getCriterionString("Print Start Date", this.getPrintStartDate()));
			criteriaString.append(this.getCriterionString("Print End Date", this.getPrintEndDate()));
		}
		
		if(this.offenceStartDate != null && this.offenceEndDate != null)
		{
			criteriaString.append(this.getCriterionString("Offence Start Date", this.getOffenceStartDate()));
			criteriaString.append(this.getCriterionString("Offence End Date", this.getOffenceEndDate()));
		}
		
		criteriaString.append(this.getCriterionString("Offender First Name", this.getOffenderFirstName()));
		criteriaString.append(this.getCriterionString("Offender Last Name", this.getOffenderLastName()));
		
		
		
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


	public SummonsReportCriteriaBO(Date operationStartDate,
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


	public SummonsReportCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
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


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Date getPrintStartDate() {
		return printStartDate;
	}


	public void setPrintStartDate(Date printStartDate) {
		this.printStartDate = printStartDate;
	}


	public Date getPrintEndDate() {
		return printEndDate;
	}


	public void setPrintEndDate(Date printEndDate) {
		this.printEndDate = printEndDate;
	}


	public Date getIssuedStartDate() {
		return issuedStartDate;
	}


	public void setIssuedStartDate(Date issuedStartDate) {
		this.issuedStartDate = issuedStartDate;
	}


	public Date getIssuedEndDate() {
		return issuedEndDate;
	}


	public void setIssuedEndDate(Date issuedEndDate) {
		this.issuedEndDate = issuedEndDate;
	}


	public String getTAStaffTRN() {
		return TAStaffTRN;
	}


	public void setTAStaffTRN(String tAStaffTRN) {
		TAStaffTRN = tAStaffTRN;
	}


	public String getOffenderTRN() {
		return OffenderTRN;
	}


	public void setOffenderTRN(String offenderTRN) {
		OffenderTRN = offenderTRN;
	}


	public String getOffenderFirstName() {
		return OffenderFirstName;
	}


	public void setOffenderFirstName(String offenderFirstName) {
		OffenderFirstName = offenderFirstName;
	}


	public String getOffenderLastName() {
		return OffenderLastName;
	}


	public void setOffenderLastName(String offenderLastName) {
		OffenderLastName = offenderLastName;
	}


	public Date getOffenceStartDate() {
		return offenceStartDate;
	}


	public void setOffenceStartDate(Date offenceStartDate) {
		this.offenceStartDate = offenceStartDate;
	}


	public Date getOffenceEndDate() {
		return offenceEndDate;
	}


	public void setOffenceEndDate(Date offenceEndDate) {
		this.offenceEndDate = offenceEndDate;
	}
	
	
	
}
