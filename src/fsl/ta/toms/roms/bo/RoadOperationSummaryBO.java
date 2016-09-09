/**
 * Created By: oanguin
 * Date: May 10, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This class has all the data to be displayed in the Operation Summary Report
 * @author oanguin
 * Created Date: May 10, 2013
 */
public class RoadOperationSummaryBO extends ReportBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<RoadOperationSummaryResultsBO> results;

	public RoadOperationSummaryBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoadOperationSummaryBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription,
			List<RoadOperationSummaryResultsBO> results) {
		super(userName, region, applicationName, reportName, startDate,
				endDate, searchCriteria, regionDescription);
		this.results = results;
	}

	public List<RoadOperationSummaryResultsBO> getResults() {
		return results;
	}

	public void setResults(List<RoadOperationSummaryResultsBO> results) {
		this.results = results;
	}

	


	
	
	
	
}
