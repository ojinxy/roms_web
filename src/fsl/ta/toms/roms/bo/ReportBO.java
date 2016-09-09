/**
 * Created By: oanguin
 * Date: May 8, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.WordUtils;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.util.StringUtil;


/**
 * This class is a generic class which will contains data which will be used on all reports.
 * @author oanguin
 * Created Date: May 8, 2013
 */
public class ReportBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String userName,
		   region,
		   applicationName,
		   reportName,
		   regionDescription;
	
	Date startDate,
		 endDate;
	
	String searchCriteria;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
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

	

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	
	
	
	public String getRegionDescription() {
		return regionDescription;
	}

	public void setRegionDescription(String regionDescription) {
		
		if(StringUtil.isSet(regionDescription))
			this.regionDescription = WordUtils.capitalize(regionDescription.toLowerCase(),
					Constants.STRING_DELIM_ARRAY);
		else
			this.regionDescription = regionDescription;
		
	}

	public ReportBO(String userName, String region, String applicationName,
			String reportName, Date startDate, Date endDate,
			String searchCriteria) {
		super();
		this.userName = userName;
		this.region = region;
		this.applicationName = applicationName;
		this.reportName = reportName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.searchCriteria = searchCriteria;
	}
	
	public ReportBO(String userName, String region, String applicationName,
			String reportName, Date startDate, Date endDate,
			String searchCriteria,String regionDescription) {
		super();
		this.userName = userName;
		this.region = region;
		this.applicationName = applicationName;
		this.reportName = reportName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.searchCriteria = searchCriteria;
		
		this.setRegionDescription(regionDescription);
	}
	
	

	public ReportBO() {
		super();
		
	}

	public String getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	
	
	
	
	
}
