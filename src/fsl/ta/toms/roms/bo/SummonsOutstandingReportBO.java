/**
 * Created By: oanguin
 * Date: May 13, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This object contains all the information needed for the Outstanding summons report.
 * @author oanguin
 * Created Date: May 13, 2013
 */
public class SummonsOutstandingReportBO extends ReportBO implements Serializable 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<SummonsOutstandingReportResultsBO> results;



	public List<SummonsOutstandingReportResultsBO> getResults() {
		return results;
	}

	public void setResults(List<SummonsOutstandingReportResultsBO> results) {
		this.results = results;
	}

	public SummonsOutstandingReportBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SummonsOutstandingReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria, regionDescription);
		// TODO Auto-generated constructor stub
	}

	public SummonsOutstandingReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
