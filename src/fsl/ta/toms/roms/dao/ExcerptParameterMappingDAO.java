/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.ExcerptParamMappingDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public interface ExcerptParameterMappingDAO extends DAO 
{
	public List<ExcerptParamMappingDO> getExcerptParameterMappingReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
}
