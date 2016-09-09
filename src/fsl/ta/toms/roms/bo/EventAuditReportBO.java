/**
 * Created By: oanguin
 * Date: May 24, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author oanguin
 * Created Date: May 24, 2013
 */
public class EventAuditReportBO extends ReportBO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	List<EventAuditReportResultsBO> results;


	public List<EventAuditReportResultsBO> getResults() {
		return results;
	}


	public void setResults(List<EventAuditReportResultsBO> results) {
		this.results = results;
	}


	public EventAuditReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria,String regionDescription,
			List<EventAuditReportResultsBO> results) {
		super(userName, region, applicationName, reportName, startDate,
				endDate, searchCriteria,regionDescription);
		this.results = results;
	}


	public EventAuditReportBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public EventAuditReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria, regionDescription);
		// TODO Auto-generated constructor stub
	}


	public EventAuditReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
