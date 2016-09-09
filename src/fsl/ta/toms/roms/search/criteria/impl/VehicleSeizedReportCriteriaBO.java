/**
 * Created By: oanguin
 * Date: May 8, 2013
 *
 */
package fsl.ta.toms.roms.search.criteria.impl;

import java.util.Date;

import org.apache.commons.lang.WordUtils;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.search.criteria.ReportSearchCriteria;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: May 8, 2013
 */
public class VehicleSeizedReportCriteriaBO extends ParentReportCriteria implements ReportSearchCriteria 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Date offenceStartDate,
		 offenceEndDate;
	
	String TAOfficeRegion,
		   TAStaffId,
		   roadOperationName,
		   TAStaffTRN,
		   offenderTRN,
		   offenderFirstName,
		   offenderLastName;
	
	Integer offenderId,
			poundId,
			wreckingCompanyId,
			roadOperationId;
	
	private String TAOfficeDescription,
				   TAStaffName,
				   OffenderName,
				   PoundName,
				   WreckingCompanyName;

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

	public Integer getOffenderId() {
		return offenderId;
	}

	public void setOffenderId(Integer offenderId) {
		this.offenderId = offenderId;
	}

	public Integer getPoundId() {
		return poundId;
	}

	public void setPoundId(Integer poundId) {
		this.poundId = poundId;
	}

	public Integer getWreckingCompanyId() {
		return wreckingCompanyId;
	}

	public void setWreckingCompanyId(Integer wreckingCompanyId) {
		this.wreckingCompanyId = wreckingCompanyId;
	}

	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}

	public VehicleSeizedReportCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VehicleSeizedReportCriteriaBO(Date offenceStartDate,
			Date offenceEndDate, String tAOfficeRegion, String tAStaffId,
			Integer offenderId, Integer poundId, Integer wreckingCompanyId,
			Integer roadOperationId) {
		super();
		this.offenceStartDate = this.searchDateFormater(offenceStartDate, searchDate.START);
		this.offenceEndDate = this.searchDateFormater(offenceEndDate, searchDate.END);
		TAOfficeRegion = tAOfficeRegion;
		TAStaffId = tAStaffId;
		this.offenderId = offenderId;
		this.poundId = poundId;
		this.wreckingCompanyId = wreckingCompanyId;
		this.roadOperationId = roadOperationId;
	}
	
	@Override
	public String getSearchCriteriaString()
	{
		StringBuilder searchCriteria = new StringBuilder("");
		
		
		searchCriteria.append(this.getCriterionString("TA Office Region", this.TAOfficeDescription));
		searchCriteria.append(this.getCriterionString("TA Staff Name", this.TAStaffName));
		searchCriteria.append(this.getCriterionString("Offender Name", this.OffenderName));
		searchCriteria.append(this.getCriterionString("Pound Name", this.PoundName));
		searchCriteria.append(this.getCriterionString("Wrecker Company Name", this.WreckingCompanyName));
		searchCriteria.append(this.getCriterionString("Road Operation ID", this.roadOperationId));
		searchCriteria.append(this.getCriterionString("Road Operation Name", this.roadOperationName));
		searchCriteria.append(this.getCriterionString("Offender First Name", this.offenderFirstName));
		searchCriteria.append(this.getCriterionString("Offender Last Name", this.offenderLastName));
		
		
		
		return searchCriteria.toString();
		
	}


	
	
	public VehicleSeizedReportCriteriaBO(Date offenceStartDate,
			Date offenceEndDate, String tAOfficeRegion, String tAStaffId,
			Integer offenderId, Integer poundId, Integer wreckingCompanyId,
			Integer roadOperationId,String roadOperationName) {
		super();
		this.offenceStartDate = this.searchDateFormater(offenceStartDate, searchDate.START);
		this.offenceEndDate = this.searchDateFormater(offenceEndDate, searchDate.END);
		TAOfficeRegion = tAOfficeRegion;
		TAStaffId = tAStaffId;
		this.offenderId = offenderId;
		this.poundId = poundId;
		this.wreckingCompanyId = wreckingCompanyId;
		this.roadOperationId = roadOperationId;
		this.roadOperationName = roadOperationName;
	}

	public String getRoadOperationName() {
		return roadOperationName;
	}

	public void setRoadOperationName(String roadOperationName)
	{
		this.roadOperationName = roadOperationName;
	}

	public void setTAOfficeDescription(String tAOfficeDescription) 
	{
		
		
		if(StringUtil.isSet(tAOfficeDescription))
			this.TAOfficeDescription = WordUtils.capitalize(tAOfficeDescription.toLowerCase(),
					Constants.STRING_DELIM_ARRAY);
		else
			this.TAOfficeDescription = tAOfficeDescription;
		
		
	}

	public void setTAStaffName(String tAStaffName) {
		TAStaffName = tAStaffName;
	}

	public void setOffenderName(String offenderName) {
		OffenderName = offenderName;
	}

	public void setPoundName(String poundName) 
	{
		
		if(StringUtil.isSet(poundName))
			this.PoundName = WordUtils.capitalize(poundName.toLowerCase(),
					Constants.STRING_DELIM_ARRAY);
		else
			this.PoundName = poundName;
		
	}

	public void setWreckingCompanyName(String wreckingCompanyName) 
	{
		if(StringUtil.isSet(wreckingCompanyName))
			this.WreckingCompanyName = WordUtils.capitalize(wreckingCompanyName.toLowerCase(),
					Constants.STRING_DELIM_ARRAY);
		else
			this.WreckingCompanyName = wreckingCompanyName;
		
	}

	public String getTAStaffTRN() {
		return TAStaffTRN;
	}

	public void setTAStaffTRN(String tAStaffTRN) {
		TAStaffTRN = tAStaffTRN;
	}

	public String getOffenderTRN() {
		return offenderTRN;
	}

	public void setOffenderTRN(String offenderTRN) {
		this.offenderTRN = offenderTRN;
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
	
	
	
	
}
