/**
 * Created By: oanguin
 * Date: May 20, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author oanguin
 * Created Date: May 20, 2013
 */
public class RoadOperationsStatisticsReportBO extends ReportBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<RegionStatisticsBO> regionStatistics;

	public List<RegionStatisticsBO> getRegionStatistics() {
		return regionStatistics;
	}

	public void setRegionStatistics(List<RegionStatisticsBO> regionStatistics) {
		this.regionStatistics = regionStatistics;
	}

	public RoadOperationsStatisticsReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription,
			List<RegionStatisticsBO> regionStatistics) {
		super(userName, region, applicationName, reportName, startDate,
				endDate, searchCriteria, regionDescription);
		this.regionStatistics = regionStatistics;
	}

	public RoadOperationsStatisticsReportBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoadOperationsStatisticsReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria, regionDescription);
		// TODO Auto-generated constructor stub
	}

	public RoadOperationsStatisticsReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria);
		// TODO Auto-generated constructor stub
	}

	
	
	
}
