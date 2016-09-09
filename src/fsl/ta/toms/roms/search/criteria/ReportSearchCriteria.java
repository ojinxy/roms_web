/**
 * Created By: oanguin
 * Date: May 10, 2013
 *
 */
package fsl.ta.toms.roms.search.criteria;

/**
 * This interface is a contract which ensures all reports build a search criteria to be used in reports.
 * @author oanguin
 * Created Date: May 10, 2013
 */
public interface ReportSearchCriteria extends SearchCriteria 
{
	public String getSearchCriteriaString();
}
