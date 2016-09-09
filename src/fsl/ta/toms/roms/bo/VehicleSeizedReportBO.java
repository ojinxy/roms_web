/**
 * Created By: oanguin
 * Date: May 8, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * This object has all the data needed for the vehicle seized report
 * @author oanguin
 * Created Date: May 8, 2013
 */
public class VehicleSeizedReportBO extends ReportBO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<VehicleSeizedReportResultsBO> results;

	public List<VehicleSeizedReportResultsBO> getResults() {
		return results;
	}

	public void setResults(List<VehicleSeizedReportResultsBO> results) {
		this.results = results;
	}

	public VehicleSeizedReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria,
			List<VehicleSeizedReportResultsBO> results,String regionDescription) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria, regionDescription);
		this.results = results;
	}

	public VehicleSeizedReportBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VehicleSeizedReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria, regionDescription);
		// TODO Auto-generated constructor stub
	}

	public VehicleSeizedReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria);
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
