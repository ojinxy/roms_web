/**
 * Created By: oanguin
 * Date: Apr 26, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.CourtAppearanceDAO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtAppearanceCriteriaBO;
import fsl.ta.toms.roms.service.RecordingCourtOutcomeService;

/**
 * 
 * @author oanguin
 * Created Date: Apr 26, 2013
 */
@Transactional
public class RecordingCourtOutcomeServiceImpl implements
		RecordingCourtOutcomeService {

	private DAOFactory daoFactory;

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void createNewCourtAppearance(CourtAppearanceBO courtAppearance) throws ErrorSavingException 
	{
		CourtAppearanceDAO courtAppearanceDAO = daoFactory.getCourtAppearanceDAO();
		
		courtAppearanceDAO.createNewCourtAppearance(courtAppearance);
		
		
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void updateCourtAppearance(CourtAppearanceBO currentCourtAppearance,CourtAppearanceBO newCourtAppearance) throws ErrorSavingException, RequiredFieldMissingException, InvalidFieldException 
	{
		
		CourtAppearanceDAO CourtAppearanceDAO = daoFactory.getCourtAppearanceDAO();
		CourtAppearanceDAO.updateCourtAppearance(currentCourtAppearance, newCourtAppearance);
		
	}

	@Override
	public List<CourtAppearanceBO> lookUpCourtAppearance(CourtAppearanceCriteriaBO courtAppearanceCriteria) throws InvalidFieldException 
	{
		CourtAppearanceDAO CourtAppearanceDAO = daoFactory.getCourtAppearanceDAO();
		return CourtAppearanceDAO.lookUpCourtAppearance(courtAppearanceCriteria);
	}


	

}
