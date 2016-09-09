/**
 * Created By: oanguin
 * Date: May 20, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

/**
 * @author oanguin
 * Created Date: May 20, 2013
 */
public class RegionStatisticsBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String officeLocCode;
	String officeLocDescription;
	
	List<RoadOperationStatisticsBO> roadOperationStatistics;
	
	List<RoadOperationSummaryResultsBO> roadOpSummary;
	



	public List<RoadOperationSummaryResultsBO> getRoadOpSummary() {
		return roadOpSummary;
	}

	public void setRoadOpSummary(List<RoadOperationSummaryResultsBO> roadOpSummary) {
		this.roadOpSummary = roadOpSummary;
	}

	public String getOfficeLocCode() {
		return officeLocCode;
	}

	public void setOfficeLocCode(String officeLocCode) {
		this.officeLocCode = officeLocCode;
	}

	public List<RoadOperationStatisticsBO> getRoadOperationStatistics() {
		return roadOperationStatistics;
	}

	public void setRoadOperationStatistics(
			List<RoadOperationStatisticsBO> roadOperationStatistics) {
		this.roadOperationStatistics = roadOperationStatistics;
	}

	public RegionStatisticsBO() {
		super();
		
	}
	
	public RegionStatisticsBO(String officeLocCode) 
	{
		super();
		this.officeLocCode = officeLocCode;
		
	}

	public String getOfficeLocDescription() {
		return officeLocDescription;
	}

	public void setOfficeLocDescription(String officeLocDescription) {
		this.officeLocDescription = officeLocDescription;
	}

	public RegionStatisticsBO(String officeLocCode, String officeLocDescription) {
		super();
		this.officeLocCode = officeLocCode;
		this.officeLocDescription = officeLocDescription;
	}

	
	
	
	
	
}
