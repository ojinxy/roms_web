package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

public class OffenceOutcomeWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ComplianceBO updatedComplianceDetails;
	List<CompliancyCheckBO> listOfCompliancyChecks;
	/**
	 * @return the updatedComplianceDetails
	 */
	public ComplianceBO getUpdatedComplianceDetails() {
		return updatedComplianceDetails;
	}
	/**
	 * @param updatedComplianceDetails the updatedComplianceDetails to set
	 */
	public void setUpdatedComplianceDetails(ComplianceBO updatedComplianceDetails) {
		this.updatedComplianceDetails = updatedComplianceDetails;
	}
	/**
	 * @return the listOfCompliancyChecks
	 */
	public List<CompliancyCheckBO> getListOfCompliancyChecks() {
		return listOfCompliancyChecks;
	}
	/**
	 * @param listOfCompliancyChecks the listOfCompliancyChecks to set
	 */
	public void setListOfCompliancyChecks(
			List<CompliancyCheckBO> listOfCompliancyChecks) {
		this.listOfCompliancyChecks = listOfCompliancyChecks;
	}
		
}
