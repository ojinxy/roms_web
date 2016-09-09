
package fsl.ta.toms.roms.service;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.PoundCriteriaBO;
/**
 * 
 * @author jreid
 * Created Date: May 28, 2013
 */
public interface PoundService 
{
	public List<PoundBO> getPoundReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	boolean poundExists(Integer poundId);

	List<PoundBO> lookupPounds(PoundCriteriaBO poundDOCriteriaBO);

	Integer savePound(PoundBO poundBO) throws ErrorSavingException;

	void updatePound(PoundBO poundBO) throws ErrorSavingException;

	boolean shortDescriptionExists(PoundBO poundBO);

	boolean poundNameExists(PoundBO poundBO);
}
