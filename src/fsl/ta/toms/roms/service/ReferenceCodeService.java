/**
 * Created By: oanguin
 * Date: Apr 17, 2013
 *
 */
package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.RefCodeCriteriaBO;
/**
 * @author oanguin
 * Created Date: Apr 17, 2013
 */
public interface ReferenceCodeService 
{
	public List<RefCodeBO> getReferenceCode(RefCodeCriteriaBO criteriaBO) throws RequiredFieldMissingException,NoRecordFoundException,InvalidFieldException;
}
