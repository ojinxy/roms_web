/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.dataobjects.JPDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.JPCriteriaBO;


/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public interface JPDAO extends DAO 
{
	public List<JPDO> getJPReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
	
	public List<JPBO> lookupJP(JPCriteriaBO jpCriteriaBO);
	
	public boolean jpExistWithTRN(String trnNbr);

	List<JPBO> getJPListByRegion(String regionCode);

	public JPDO findByRegNumber(String regNumber);
}
