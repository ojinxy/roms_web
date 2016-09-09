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
public class RoadOperationStatisticsBO implements Serializable 
{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String teamLeadFullNames;
	
	String operationName;
	
	List<JPStatisticsBO> JPSummary;
	
	List<TAOfficerStatisticsBO> TAOfficerSummary;
	
	List<ITAExaminerStatisticsBO> ITAExaminerSummary;
	
	List<PoliceOfficerStatisticsBO> PoliceOfficerSummary;



	public List<JPStatisticsBO> getJPSummary() {
		return JPSummary;
	}

	public void setJPSummary(List<JPStatisticsBO> jPSummary) {
		JPSummary = jPSummary;
	}

	public List<TAOfficerStatisticsBO> getTAOfficerSummary() {
		return TAOfficerSummary;
	}

	public void setTAOfficerSummary(List<TAOfficerStatisticsBO> tAOfficerSummary) {
		TAOfficerSummary = tAOfficerSummary;
	}

	public List<ITAExaminerStatisticsBO> getITAExaminerSummary() {
		return ITAExaminerSummary;
	}

	public void setITAExaminerSummary(
			List<ITAExaminerStatisticsBO> iTAExaminerSummary) {
		ITAExaminerSummary = iTAExaminerSummary;
	}

	public List<PoliceOfficerStatisticsBO> getPoliceOfficerSummary() {
		return PoliceOfficerSummary;
	}

	public void setPoliceOfficerSummary(
			List<PoliceOfficerStatisticsBO> policeOfficerSummary) {
		PoliceOfficerSummary = policeOfficerSummary;
	}

	public RoadOperationStatisticsBO(String teamLeadFullNames, String operationName) {
		super();
		this.teamLeadFullNames = teamLeadFullNames;
		this.operationName = operationName;
	}

	public RoadOperationStatisticsBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTeamLeadFullNames() {
		return teamLeadFullNames;
	}

	public void setTeamLeadFullNames(String teamLeadFullNames) {
		this.teamLeadFullNames = teamLeadFullNames;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	
	
}
