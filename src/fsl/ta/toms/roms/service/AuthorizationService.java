package fsl.ta.toms.roms.service;

import fsl.ta.toms.roms.bo.AuthorizationBO;

public interface AuthorizationService {
	
	public boolean validateTAStaff(AuthorizationBO authorizationBO);
	
	public boolean validateJP(AuthorizationBO authorizationBO);
	
	public boolean hasPermission(AuthorizationBO authorizationBO);

}
