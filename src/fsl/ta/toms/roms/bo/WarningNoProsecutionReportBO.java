/**
 * Created By: oanguin
 * Date: Aug 8, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author oanguin
 * Created Date: Aug 8, 2013
 */
public class WarningNoProsecutionReportBO extends ReportBO implements
		Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<WarningNoProsecutionDetailsBO> warningNoProsecutionDetailsList;

	public List<WarningNoProsecutionDetailsBO> getWarningNoProsecutionDetailsList() {
		return warningNoProsecutionDetailsList;
	}

	public void setWarningNoProsecutionDetailsList(
			List<WarningNoProsecutionDetailsBO> warningNoProsecutionDetailsList) {
		this.warningNoProsecutionDetailsList = warningNoProsecutionDetailsList;
	}

	public WarningNoProsecutionReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription,
			List<WarningNoProsecutionDetailsBO> warningNoProsecutionDetailsList) {
		super(userName, region, applicationName, reportName, startDate,
				endDate, searchCriteria, regionDescription);
		this.warningNoProsecutionDetailsList = warningNoProsecutionDetailsList;
	}

	public WarningNoProsecutionReportBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WarningNoProsecutionReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria, String regionDescription) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria, regionDescription);
		// TODO Auto-generated constructor stub
	}

	public WarningNoProsecutionReportBO(String userName, String region,
			String applicationName, String reportName, Date startDate,
			Date endDate, String searchCriteria) {
		super(userName, region, applicationName, reportName, startDate, endDate,
				searchCriteria);
		// TODO Auto-generated constructor stub
	}
	


	
	
	
	
}
