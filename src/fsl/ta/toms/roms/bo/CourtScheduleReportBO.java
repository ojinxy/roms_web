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
public class CourtScheduleReportBO extends ReportBO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	List<CourtScheduleReportResultsBO> results;


	public CourtScheduleReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription,
			List<CourtScheduleReportResultsBO> results) {
		super(userName, region, applicationName, reportName, startDate,
				endDate, searchCriteria, regionDescription);
		this.results = results;
	}


	public List<CourtScheduleReportResultsBO> getResults() {
		return results;
	}


	public void setResults(List<CourtScheduleReportResultsBO> results) {
		this.results = results;
	}


	public CourtScheduleReportBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public CourtScheduleReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria, regionDescription);
		// TODO Auto-generated constructor stub
	}


	public CourtScheduleReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria);
		// TODO Auto-generated constructor stub
	}


	
	
	
	
}
