/**
 * Created By: oanguin
 * Date: Apr 17, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.CategoryDAO;
import fsl.ta.toms.roms.dataobjects.CDCategoryDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

/**
 * @author oanguin
 * Created Date: Apr 17, 2013
 */
public class CategoryDAOImpl extends ParentDAOImpl implements CategoryDAO {



	/** @param columns the columns that are expected to be filled in the DO
	 * @param filter a hashmap with the keys being the column names and the values being the filter value for a column
	 * @param status this should either be ACT or INA
	 * @return List of CDCategoryDO objects
	 * @throws InvalidFieldException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CDCategoryDO> getCategoryReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<CDCategoryDO>)super.getReference(CDCategoryDO.class, status, filter);
	}



}
