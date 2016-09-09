package fsl.ta.toms.roms.webservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtCaseCriteriaBO;
import fsl.ta.toms.roms.service.CourtCaseService;
import fsl.ta.toms.roms.service.ServiceFactory;

/**
 * 
 * @author jreid Created Date: Sep 16, 2013
 */
public class CourtCaseMaintenance_ extends SpringBeanAutowiringSupport {

	@Autowired
	private ServiceFactory serviceFactory;

	public List<CourtCaseBO> getCourtCaseDetails(
			CourtCaseCriteriaBO courtCaseCriteria)
			throws RequiredFieldMissingException, NoRecordFoundException {

		List<CourtCaseBO>  cases = null;
		
		if (courtCaseCriteria == null)
			throw new RequiredFieldMissingException(
					"Search Details cannot be empty");

		if (courtCaseCriteria.getRoadOperationId() != null && courtCaseCriteria.getCourtDate() == null)
			throw new RequiredFieldMissingException(
					"Court Date is required");

		if (courtCaseCriteria.getCourtId() != null
				&& courtCaseCriteria.getCourtDate() == null)
			throw new RequiredFieldMissingException(
					"Court Date is required to search by court");

		CourtCaseService courtCaseService = serviceFactory
				.getCourtCaseService();

		cases = courtCaseService.lookUpCourtCase(courtCaseCriteria);

		return cases;

	}

	/*public void updateCourtCaseStatus() {
	}
*/
	
	public boolean updateCourtCase(
		CourtCaseBO courtCaseBO)
				throws RequiredFieldMissingException {

			if (courtCaseBO == null)
				throw new RequiredFieldMissingException(
						"Court Case Details cannot be empty");

			if (courtCaseBO.getCourtCaseId() == null)
				throw new RequiredFieldMissingException(
						"Court Case Id is required");

		/*	if (courtCaseBO.getCourtId() == null)
				throw new RequiredFieldMissingException(
						"Court case Id is required");
		*/	
			 if (courtCaseBO.getCourtAppearances()== null)
				throw new RequiredFieldMissingException(
						"Court Appearances is required");

			CourtCaseService courtCaseService = serviceFactory
					.getCourtCaseService();

			try {
				courtCaseService.updateCourtCase(courtCaseBO);
			} catch (ErrorSavingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
	}
}
