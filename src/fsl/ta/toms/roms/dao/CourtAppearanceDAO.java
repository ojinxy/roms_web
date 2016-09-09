/**
 * Created By: oanguin
 * Date: Apr 26, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.dataobjects.CourtCaseDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtAppearanceCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 26, 2013
 */
public interface CourtAppearanceDAO extends DAO 
{

	public void updateCourtAppearance(CourtAppearanceBO currentCourtAppearance, CourtAppearanceBO newCourtAppearance) throws ErrorSavingException,RequiredFieldMissingException, InvalidFieldException;
	
	
	/**
	 * @author oanguin
	 * Create Date: 30 April 2013
	 * @param <code>summons</code> This is the summons object to check all related trials to see if they are closed.
	 * @return True if all trials are closed false if all trials are not closed
	 */
	public boolean allCourtAppearancesForSummonsClosed(Integer summonsId);
	
	/**
	 * @summary This Method returns a list of trial data objects based on the search criteria.
	 * @param <code>filter</code> A hash map of search criteria with the keys being the table column names and the values being what is to be searched for.
	 * @param <code>status</code> The status of the trial can be CC_CLO,CC_OPE,CC_CAN
	 * @return <code>List<CourtAppearanceDO></code> A list of trial data objects based on passed in criteria.
	 * @throws InvalidFieldException 
	 */
	public List<CourtAppearanceDO> getCourtAppearanceReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	
	void createNewCourtAppearance(CourtAppearanceBO trial) throws ErrorSavingException;


	CourtAppearanceDO getMostRecentCourtAppearanceBySummonsID(Integer summonsID);
	
	
	CourtAppearanceDO getFirstCourtAppearanceBySummonsID(Integer summonsID);
	
	/**
	 * This function uses the criteria to look of for trial records.
	 * @param trialCriteria
	 * @return
	 * @throws InvalidFieldException 
	 */
	public List<CourtAppearanceBO> lookUpCourtAppearance(CourtAppearanceCriteriaBO trialCriteria) throws InvalidFieldException;


	void overrideCourtAppearance(CourtAppearanceBO overriddenCourtAppearance) throws ErrorSavingException, RequiredFieldMissingException, InvalidFieldException;


	CourtAppearanceBO getCourtAppearanceByID(Integer appearanceId);
	
	Date getInitialCourtDateByCourtCaseId(Integer courtCaseId);
	
	
	
}
