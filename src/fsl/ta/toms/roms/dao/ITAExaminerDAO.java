/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.dataobjects.ITAExaminerDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.ITAExaminerCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public interface ITAExaminerDAO extends DAO 
{
	public List<ITAExaminerDO> getITAExaminerReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
	
	public List<ITAExaminerBO> lookupITAExaminer(ITAExaminerCriteriaBO itaExaminerCriteriaBO);
	
	public boolean itaExaminerExistWithTRN(String trnNbr);

	public ITAExaminerDO findByExaminerId(String idNumber);
	
}
