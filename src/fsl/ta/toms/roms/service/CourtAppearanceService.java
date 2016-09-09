/**
 * Created By: oanguin
 * Date: Apr 26, 2013
 *
 */
package fsl.ta.toms.roms.service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import fsl.ta.toms.roms.bo.ComplianceBO;
import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;

/**
 * @author jreid
 * Created Date: Apr 26, 2013
 */
public interface CourtAppearanceService   
{

	/**
	 * 
	 * @param currentCourtAppearance
	 * @param newCourtAppearance
	 * @throws ErrorSavingException
	 * @throws RequiredFieldMissingException
	 */
	public void updateCourtAppearance(CourtAppearanceBO currentCourtAppearance, CourtAppearanceBO newCourtAppearance) throws ErrorSavingException,RequiredFieldMissingException;
	
	
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

	
	/**
	 * 
	 * @param trial
	 * @return 
	 * @throws ErrorSavingException
	 *//*
	Integer createNewCourtAppearance(CourtAppearanceBO trial) throws ErrorSavingException;
*/
	CourtAppearanceDO getMostRecentCourtAppearanceBySummonsID(Integer summonsID);


	Serializable saveCourtAppearance(CourtAppearanceBO trial);


	List<CourtAppearanceBO> getCourtAppearancesBySummonsId(Integer summonsId)
			throws RequiredFieldMissingException;


	CourtAppearanceDO getFirstCourtAppearanceBySummonsID(Integer summonsID);


	void overrideCourtAppearance(CourtAppearanceBO currentCourtAppearance) throws ErrorSavingException, RequiredFieldMissingException;
	
	Date getInitialCourtDateByCourtCaseId(Integer courtCaseId);
}
