/**
 * Created By: oanguin
 * Date: Jul 18, 2013
 *
 */
package fsl.ta.toms.roms.service;

import java.io.Serializable;
import java.util.List;

import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtCaseCriteriaBO;

/**
 * @author oanguin
 * Created Date: Jul 18, 2013
 */
public interface CourtCaseService 
{
	/*public Serializable createCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException, RequiredFieldMissingException;
*/	
	public void updateCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException, RequiredFieldMissingException;
	
	public List<CourtCaseBO> lookUpCourtCase(CourtCaseCriteriaBO criteria) throws NoRecordFoundException, RequiredFieldMissingException;

	CourtCaseBO getCourtCaseBySummonsId(Integer summonsId)
			throws RequiredFieldMissingException;

	
	Serializable createCourtCase(CourtCaseBO courtCaseBO)
			throws ErrorSavingException, RequiredFieldMissingException;

	Serializable createCourtCaseNFirstAppearanceForSummons(SummonsBO summons)
			throws ErrorSavingException, RequiredFieldMissingException;

	void overrideCourtCaseDetails(CourtCaseBO courtCaseBO) throws ErrorSavingException, RequiredFieldMissingException;

}
