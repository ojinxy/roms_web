package fsl.ta.toms.roms.dao;

import java.util.HashMap;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.CDCategoryDO;
import fsl.ta.toms.roms.dataobjects.CDOtherRoleDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;

public interface OtherRoleDAO extends DAO
{
	public List<CDOtherRoleDO> getOtherRoleReference(HashMap<String,String> filter,String status) throws InvalidFieldException;
}
