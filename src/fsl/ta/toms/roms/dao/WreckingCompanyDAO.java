/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.dataobjects.WreckingCompanyDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.WreckingCompanyCriteriaBO;

/**
 * @author oanguin
 * Created Date: Apr 22, 2013
 */
public interface WreckingCompanyDAO extends DAO 
{
	public List<WreckingCompanyDO> getWreckingCompanyReference(HashMap<String,String> filter,String status) throws InvalidFieldException;

	List<WreckingCompanyDO> lookupWreckingCompany(
			WreckingCompanyCriteriaBO wreckingCompanyCriteriaBO);

	boolean wreckingCompanyExists(Integer wreckingCompanyId);
	
	boolean wreckingCompanyNameExists(WreckingCompanyBO wreckingCompany);
	
	boolean trnBranchExists(WreckingCompanyBO wreckingCompanyBO);
	
	boolean wreckingCompanyShortDescriptionExists(
			WreckingCompanyBO wreckingCompany);
}
