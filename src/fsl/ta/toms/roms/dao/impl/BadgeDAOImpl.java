/**
 * Created By: oanguin
 * Date: May 6, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import fsl.ta.toms.roms.bo.BadgeBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.BadgeDAO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.BIMS_BadgeView;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.CDEventRefTypeDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;

/**
 * @author oanguin
 * Created Date: May 6, 2013
 */
public class BadgeDAOImpl extends ParentDAOImpl implements BadgeDAO {

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.BadgeDAO#getBadgeDetails(java.lang.String, java.lang.String)
	 */
	@Override
	public BadgeBO getBadgeDetails(String badgeNo, String badgeType) throws SQLException 
	{
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(BIMS_BadgeView.class,"b");
		
		criteria.add(Restrictions.eq("b.trn", badgeNo.trim()));
		
		criteria.add(Restrictions.eq("b.badgeTypeCode", badgeType.trim()).ignoreCase());
		
		criteria.addOrder(Order.desc("b.badgeNo"));		
		
		List<?> resultList = criteria.list();
		
		if(resultList.size() > 0)
		{
			BadgeBO badgeBO = this.convertFromBadgeDOtoBadgeBO((BIMS_BadgeView)resultList.get(0));
			
			return badgeBO;
		}
		else
		{
			return null;
		}
		
		
	}

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.BadgeDAO#getBadgeByPersonDetails(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BadgeBO> getBadgeByPersonDetails(String firstName,
			String midName, String lastName, String userName) throws Exception 
	{
		try
		{
			Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(BIMS_BadgeView.class,"b");
			criteria.add(Restrictions.like("b.firstName", firstName, MatchMode.START).ignoreCase());
			
			if(midName != null && ! midName.isEmpty())
			{
				criteria.add(Restrictions.like("b.midName", midName, MatchMode.START).ignoreCase());
			}
			criteria.add(Restrictions.like("b.lastName", lastName, MatchMode.START).ignoreCase());
			
			criteria.addOrder(Order.asc("b.trn"));
			criteria.addOrder(Order.desc("b.badgeNo"));
			
			List<BadgeBO> badgeBOList = new ArrayList<BadgeBO>();
			
			for(BIMS_BadgeView badgeDO : (List<BIMS_BadgeView>)criteria.list())
			{
				badgeBOList.add(this.convertFromBadgeDOtoBadgeBO(badgeDO));
			}
			
			AuditEntry auditEntry = new AuditEntry();
			auditEntry.setCreateUsername(userName);
			auditEntry.setCreateDTime(Calendar.getInstance().getTime());
			//String comment = String.format("User %s searched for a badge by name based on the first name and last name entered in this audit record.",userName);
			String comment = "";
			
			this.createBadgeSearchEventAudit(firstName, lastName, auditEntry, comment);
			return this.removeOldBadges(badgeBOList);
		}
		catch(Exception exe)
		{
			throw exe;
		}
	}
	
	/**
	 * This <b>function</b> take a list of BadgeBOs and removes previous badge numbers attached to the same trn.
	 * @param oldBadgeBOList
	 * @return
	 */
	private List<BadgeBO> removeOldBadges(List<BadgeBO> oldBadgeBOList)
	{
		List<BadgeBO> newBadgeBOList = new ArrayList<BadgeBO>();
		
		for(BadgeBO curBadgeBO : oldBadgeBOList)
		{
			boolean notInList = true;
			
			//check if trn is in the new list already
			for(BadgeBO innerBadgeBO : newBadgeBOList)
			{
				if(curBadgeBO.getTrn().equalsIgnoreCase(innerBadgeBO.getTrn()) && curBadgeBO.getBadgeDesc().equalsIgnoreCase(innerBadgeBO.getBadgeDesc()))
					notInList = false;
			}
			
			if(notInList)
			{
				newBadgeBOList.add(curBadgeBO);
			}
		}
		
		return newBadgeBOList;
	}
	
	private BadgeBO convertFromBadgeDOtoBadgeBO(BIMS_BadgeView badgeDO) throws SQLException
	{
		
		
		BadgeBO badgeBO = new BadgeBO(badgeDO.getTrn(),
									 	badgeDO.getBadgeDesc(),
									 	badgeDO.getFirstName(),
									 	badgeDO.getMidName(),
									 	badgeDO.getLastName(),
									 	badgeDO.getStatusDesc(),
									 	badgeDO.getIssueDate(),
									 	badgeDO.getExpiryDate(),
									 	badgeDO.getBadgeNo(),
									 	badgeDO.getBiometricsSno(),
									 	badgeDO.getPhotoImg() == null?null:badgeDO.getPhotoImg().getBytes(1, (int) badgeDO.getPhotoImg().length()));
		
		return badgeBO;
	}

	@Override
	public final Serializable save(Object entity) throws DataAccessException {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public final void update(Object entity) throws DataAccessException {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public final void delete(Object entity) throws DataAccessException {
		throw new UnsupportedOperationException("not supported");
	}
	
	/**
	 * This method is used where a badge search is done by the names of the person.
	 * @param firstName
	 * @param lastName
	 * @param auditEntry
	 * @param comment
	 */
	private void createBadgeSearchEventAudit(String firstName,String lastName, AuditEntry auditEntry, String comment)
	{
		EventAuditDO eventAudit = new EventAuditDO();
		
		eventAudit.setAuditEntry(auditEntry);
		
		eventAudit.setEvent(hibernateTemplate.load(CDEventDO.class,Constants.EventCode.LOOKUP_DRIVER_CONDUCTOR_BADGE_INFORMATION));
		eventAudit.setComment(comment);
		
		eventAudit.setRefType1(hibernateTemplate.load(CDEventRefTypeDO .class, Constants.EventRefTypeCode.PERSON_FNAME));
		eventAudit.setRefValue1(firstName);
		
		eventAudit.setRefType2(hibernateTemplate.load(CDEventRefTypeDO .class, Constants.EventRefTypeCode.PERSON_LNAME));	
		eventAudit.setRefValue2(lastName);
		
		
		hibernateTemplate.save(eventAudit);
		
	}
	
	

}
