/**
 * Created By: oanguin
 * Date: May 7, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.BadgeBO;
import fsl.ta.toms.roms.dao.BadgeDAO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: May 7, 2013
 */
@Transactional
public class BIMSServiceImpl implements fsl.ta.toms.roms.service.BIMSService {

	private DAOFactory daoFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.service.BIMSService#getBadgeDetails(java.lang.String, java.lang.String)
	 */
	@Override
	public BadgeBO getBadgeDetails(String badgeNo, String badgeType)
			throws RequiredFieldMissingException, NoRecordFoundException
			
	{
		//if((badgeNo == null || badgeNo < 1) || (badgeType == null || badgeType.isEmpty())) 
		if((!StringUtil.isSet(badgeNo) || !StringUtil.isSet(badgeType)))
			throw new RequiredFieldMissingException();
		
		BadgeDAO badgeDAO = this.daoFactory.getBadgeDAO();
		
		BadgeBO badgeBO;
		
		try
		{
			badgeBO = badgeDAO.getBadgeDetails(badgeNo, badgeType);
		}
		catch(Exception exe)
		{
			exe.printStackTrace();
			throw new NoRecordFoundException();
		}
		
		if(badgeBO == null)
			throw new NoRecordFoundException();
		
		return badgeBO;
	}

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.service.BIMSService#getBadgeByPersonDetails(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<BadgeBO> getBadgeByPersonDetails(String firstName,
			String midName, String lastName, String userName)
			throws RequiredFieldMissingException, NoRecordFoundException 
	{
		
		if((firstName == null || firstName.isEmpty()) || (lastName == null || lastName.isEmpty()) || (userName == null || userName.isEmpty()))
			throw new RequiredFieldMissingException();
		
				
		BadgeDAO badgeDAO = this.daoFactory.getBadgeDAO();
		
		List<BadgeBO> badgeBOList;
		
		try
		{
			badgeBOList = badgeDAO.getBadgeByPersonDetails(firstName, midName, lastName,userName);
			
			
		}
		catch(Exception exe)
		{
			exe.printStackTrace();
			throw new RequiredFieldMissingException();
		}
		
		if(badgeBOList.isEmpty())
			throw new NoRecordFoundException();
		
		return badgeBOList;
	}

}
