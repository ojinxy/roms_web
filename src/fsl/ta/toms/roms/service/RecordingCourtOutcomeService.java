/**
 * Created By: oanguin
 * Date: Apr 26, 2013
 *
 */
package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtAppearanceCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 26, 2013
 */
public interface RecordingCourtOutcomeService 
{
	public void createNewCourtAppearance(CourtAppearanceBO courtAppearance) throws ErrorSavingException;
	
	public void updateCourtAppearance(CourtAppearanceBO currentCourtAppearance,CourtAppearanceBO newCourtAppearance) throws ErrorSavingException,RequiredFieldMissingException, InvalidFieldException;
	
	public List<CourtAppearanceBO> lookUpCourtAppearance(CourtAppearanceCriteriaBO courtAppearanceCriteria) throws InvalidFieldException;
}
