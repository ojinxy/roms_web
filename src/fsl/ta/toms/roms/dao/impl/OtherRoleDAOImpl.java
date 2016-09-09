package fsl.ta.toms.roms.dao.impl;

import java.util.HashMap;
import java.util.List;

import fsl.ta.toms.roms.dao.OtherRoleDAO;
import fsl.ta.toms.roms.dataobjects.CDCategoryDO;
import fsl.ta.toms.roms.dataobjects.CDOtherRoleDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;


public class OtherRoleDAOImpl extends ParentDAOImpl implements OtherRoleDAO {



	/** @param columns the columns that are expected to be filled in the DO
	 * @param filter a hashmap with the keys being the column names and the values being the filter value for a column
	 * @param status this should either be ACT or INA
	 * @return List of CDCategoryDO objects
	 * @throws InvalidFieldException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CDOtherRoleDO> getOtherRoleReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<CDOtherRoleDO>)super.getReference(CDOtherRoleDO.class, status, filter);
	}



}
