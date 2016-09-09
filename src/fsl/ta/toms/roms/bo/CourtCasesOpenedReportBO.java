/**
 * Created By: oanguin
 * Date: May 15, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author oanguin
 * Created Date: May 15, 2013
 */
public class CourtCasesOpenedReportBO extends ReportBO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	List<CourtCasesOpenedReportResultsBO> courtCaseList;


	public List<CourtCasesOpenedReportResultsBO> getCourtCaseList() {
		return courtCaseList;
	}


	public void setCourtCaseList(List<CourtCasesOpenedReportResultsBO> courtCaseList) {
		this.courtCaseList = courtCaseList;
	}


	public CourtCasesOpenedReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription,
			List<CourtCasesOpenedReportResultsBO> courtCaseList) {
		super(userName, region, applicationName, reportName, startDate,
				endDate, searchCriteria, regionDescription);
		this.courtCaseList = courtCaseList;
	}


	public CourtCasesOpenedReportBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public CourtCasesOpenedReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria, regionDescription);
		// TODO Auto-generated constructor stub
	}


	public CourtCasesOpenedReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria);
		// TODO Auto-generated constructor stub
	}
	
	
}
