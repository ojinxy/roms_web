/**
 * Created By: oanguin
 * Date: Jul 17, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.dataobjects.CourtCaseDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.CourtCaseCriteriaBO;
/**
 * @author oanguin
 * Created Date: Jul 17, 2013
 */
public interface CourtCaseDAO extends DAO  
{
	/**
	 * This function ceates a court case for a summons if one does not exist
	 * @param courtCaseBO
	 * @return 
	 */
	public Serializable createCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException;
	
	public void updateCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException;
	
	public List<CourtCaseBO> lookUpCourtCase(CourtCaseCriteriaBO criteria);
	
	/**
	 * This method returns a list of court case data objects based on the search criteria.
	 * * @param <code>filter</code> A hash map of search criteria with the keys being the table column names and the values being what is to be searched for.
	 * @param <code>status</code> The status of the trial can be CC_CLO,CC_OPE,CC_CAN
	 * @return
	 * @throws InvalidFieldException
	 */
	public List<CourtCaseDO> getCourtCaseReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	CourtCaseBO lookUpCourtCaseBySummonsId(Integer summonsId);

	void overrideCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException;
	
}
