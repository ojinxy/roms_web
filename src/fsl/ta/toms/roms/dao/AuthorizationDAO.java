package fsl.ta.toms.roms.dao;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.AuthorizationBO;

public interface AuthorizationDAO extends DAO{
	
	public boolean hasPermission(AuthorizationBO authorizationBO);

}
