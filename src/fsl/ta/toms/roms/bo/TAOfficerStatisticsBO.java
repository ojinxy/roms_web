/**
 * Created By: oanguin
 * Date: May 20, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;

/**
 * @author oanguin
 * Created Date: May 20, 2013
 */
public class TAOfficerStatisticsBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String fullName;
	
	String staffId;
	
	String staffType;
	
	Integer countCompliancyChecks;
	
	Integer countMVChecks;
	
	Integer countDLChecks;
	
	Integer countBadgeChecks;
	
	Integer countCitationChecks;
	
	Integer countRLChecks;
	
	Integer countOtherChecks;
	
	Integer countWarningNoticesIssued;
	
	Integer countSummonsIssued;
	
	Integer countVehiclesSeized;
	
	String attended;
	
	Integer countPlatesRemoved;
	
	Integer warningsForProcecution;
	
	Integer allInOrders;
	
	

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}

	public Integer getCountCompliancyChecks() {
		return countCompliancyChecks;
	}

	public void setCountCompliancyChecks(Integer countCompliancyChecks) {
		this.countCompliancyChecks = countCompliancyChecks;
	}

	public Integer getCountMVChecks() {
		return countMVChecks;
	}

	public void setCountMVChecks(Integer countMVChecks) {
		this.countMVChecks = countMVChecks;
	}

	public Integer getCountDLChecks() {
		return countDLChecks;
	}

	public void setCountDLChecks(Integer countDLChecks) {
		this.countDLChecks = countDLChecks;
	}

	public Integer getCountBadgeChecks() {
		return countBadgeChecks;
	}

	public void setCountBadgeChecks(Integer countBadgeChecks) {
		this.countBadgeChecks = countBadgeChecks;
	}

	public Integer getCountCitationChecks() {
		return countCitationChecks;
	}

	public void setCountCitationChecks(Integer countCitationChecks) {
		this.countCitationChecks = countCitationChecks;
	}

	public Integer getCountRLChecks() {
		return countRLChecks;
	}

	public void setCountRLChecks(Integer countRLChecks) {
		this.countRLChecks = countRLChecks;
	}

	public Integer getCountOtherChecks() {
		return countOtherChecks;
	}

	public void setCountOtherChecks(Integer countOtherChecks) {
		this.countOtherChecks = countOtherChecks;
	}

	public Integer getCountWarningNoticesIssued() {
		return countWarningNoticesIssued;
	}

	public void setCountWarningNoticesIssued(Integer countWarningNoticesIssued) {
		this.countWarningNoticesIssued = countWarningNoticesIssued;
	}

	public Integer getCountSummonsIssued() {
		return countSummonsIssued;
	}

	public void setCountSummonsIssued(Integer countSummonsIssued) {
		this.countSummonsIssued = countSummonsIssued;
	}

	public Integer getCountVehiclesSeized() {
		return countVehiclesSeized;
	}

	public void setCountVehiclesSeized(Integer countVehiclesSeized) {
		this.countVehiclesSeized = countVehiclesSeized;
	}

	public String getAttended() {
		return attended;
	}

	public void setAttended(String attended) {
		this.attended = attended;
	}

	public Integer getCountPlatesRemoved() {
		return countPlatesRemoved;
	}

	public void setCountPlatesRemoved(Integer countPlatesRemoved) {
		this.countPlatesRemoved = countPlatesRemoved;
	}

	public Integer getWarningsForProcecution() {
		return warningsForProcecution;
	}

	public void setWarningsForProcecution(Integer warningsForProcecution) {
		this.warningsForProcecution = warningsForProcecution;
	}

	public Integer getAllInOrders() {
		return allInOrders;
	}

	public void setAllInOrders(Integer allInOrders) {
		this.allInOrders = allInOrders;
	}

	public TAOfficerStatisticsBO(String fullName, String staffType,
			Integer countCompliancyChecks, Integer countMVChecks,
			Integer countDLChecks, Integer countBadgeChecks,
			Integer countCitationChecks, Integer countRLChecks,
			Integer countOtherChecks, Integer countWarningNoticesIssued,
			Integer countSummonsIssued, Integer countVehiclesSeized,
			String attended, Integer countPlatesRemoved,
			Integer warningsForProcecution, Integer allInOrders, String staffId) {
		super();
		this.fullName = fullName;
		this.staffType = staffType;
		this.countCompliancyChecks = countCompliancyChecks;
		this.countMVChecks = countMVChecks;
		this.countDLChecks = countDLChecks;
		this.countBadgeChecks = countBadgeChecks;
		this.countCitationChecks = countCitationChecks;
		this.countRLChecks = countRLChecks;
		this.countOtherChecks = countOtherChecks;
		this.countWarningNoticesIssued = countWarningNoticesIssued;
		this.countSummonsIssued = countSummonsIssued;
		this.countVehiclesSeized = countVehiclesSeized;
		this.attended = attended;
		this.countPlatesRemoved = countPlatesRemoved;
		this.warningsForProcecution = warningsForProcecution;
		this.allInOrders = allInOrders;
		this.staffId = staffId;
	}

	public TAOfficerStatisticsBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
