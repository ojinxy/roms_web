package fsl.ta.toms.roms.service.impl;

import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.AuthorizationBO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.JPDO;
import fsl.ta.toms.roms.dataobjects.LMIS_UserViewDO;
import fsl.ta.toms.roms.security.Encryptor;
import fsl.ta.toms.roms.service.AuthorizationService;

@Transactional
public class AuthorizationServiceImpl implements AuthorizationService{

	private DAOFactory daoFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public boolean validateTAStaff(AuthorizationBO authorizationBO) {
		boolean isValid = false;
		LMIS_UserViewDO userView = new LMIS_UserViewDO();
		String hashedPassword;
		
		userView = daoFactory.getAuthorizationDAO().find(LMIS_UserViewDO.class, authorizationBO.getUsername().trim().toUpperCase());
		
		if(userView==null || userView.getPasswordHash()==null){
			return false;
		}
		else{
			hashedPassword = Encryptor.encrypt(authorizationBO.getPassword().trim());
			
			if(hashedPassword.equals(userView.getPasswordHash())){
				return true;
			}
		}
		
		return isValid;
	}

	@Override
	public boolean validateJP(AuthorizationBO authorizationBO) {
		boolean isValid = false;
		JPDO jp = new JPDO();
		String hashedPin;
		
		//System.err.println("Auth: "+authorizationBO);
		
		Integer jpid = Integer.valueOf(authorizationBO.getUsername().trim());
		
		//System.err.println("JP id:"+jpid);
		
		jp = daoFactory.getAuthorizationDAO().find(JPDO.class, jpid);
				
		//jp=daoFactory.getJPDAO().findByRegNumber(authorizationBO.getUsername().trim());
		
		if(jp==null || jp.getPinHash()==null){
			return false;
		}
		else{
			hashedPin = Encryptor.encrypt(authorizationBO.getPassword().trim());
			
			if(hashedPin.equals(jp.getPinHash())){
				return true;
			}
		}
		
		return isValid;
	}

	@Override
	public boolean hasPermission(AuthorizationBO authorizationBO) {
		return daoFactory.getAuthorizationDAO().hasPermission(authorizationBO);
	}

}
