/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.HolidayExceptionBO;
import fsl.ta.toms.roms.dao.HolidayExceptionsDAO;
import fsl.ta.toms.roms.dataobjects.HolidayExceptionsDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.HolidayExceptionsCriteriaBO;

/**
 * 
 * @author rbrooks
 * Created Date: Jul 16, 2013
 */
public class HolidayExceptionsDAOImpl extends ParentDAOImpl implements
HolidayExceptionsDAO {

	

	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.ConfigurationDAO#getConfigurationReference(java.util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<HolidayExceptionsDO> getHolidayExceptionReference(
			 HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<HolidayExceptionsDO>)super.getReference(HolidayExceptionsDO.class, status, filter);
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<HolidayExceptionsDO> lookupHolidayExceptions(
			HolidayExceptionsCriteriaBO holidayExcCriteriaBO) {

		List<HolidayExceptionsDO> holidayExceptionsList = new ArrayList<HolidayExceptionsDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(HolidayExceptionsDO.class);
		
		if(holidayExcCriteriaBO.getHolidayDate() != null ){
			criteriaS.add(Restrictions.eq("holidayDate", holidayExcCriteriaBO.getHolidayDate()));
		}
		
		if(holidayExcCriteriaBO.getDescription() != null ){
			criteriaS.add(Restrictions.eq("description", holidayExcCriteriaBO.getDescription()));
		}
		
		if(StringUtils.isNotBlank(holidayExcCriteriaBO.getStatusId())){
			criteriaS.add(Restrictions.eq("status.statusId", holidayExcCriteriaBO.getStatusId()));
		}
		
		//add the ordering 
		criteriaS.addOrder(Order.desc("holidayDate"));					
		
		holidayExceptionsList =  criteriaS.list();
		 
		if(holidayExceptionsList==null || holidayExceptionsList.size()<1){
			return null;
		}
		
		return holidayExceptionsList;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean holidayExceptionExists(Date holidayDate) {
		List<HolidayExceptionsDO> holidayExcDO = new ArrayList<HolidayExceptionsDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(HolidayExceptionsDO.class);
		criteriaS.add(Restrictions.eq("holidayDate", holidayDate));

		holidayExcDO = criteriaS.list();
		
		if(holidayExcDO==null || holidayExcDO.size()==0){
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean holidayExcDescriptionExists(HolidayExceptionBO holidayExc) {
		
		List<HolidayExceptionsDO> holidayExceptionsDO = new ArrayList<HolidayExceptionsDO>();
		
		Criteria criteriaS = this.getSession().createCriteria(HolidayExceptionsDO.class);
		
		criteriaS.add(Restrictions.eq("description", holidayExc.getDescription()).ignoreCase());
		
		if(holidayExc.getHolidayDate() != null ){
			criteriaS.add(Restrictions.ne("holidayDate", holidayExc.getHolidayDate()));
		}
		
		holidayExceptionsDO = criteriaS.list();
		
		if(holidayExceptionsDO==null || holidayExceptionsDO.size()==0){
			return false;
		}
		
		return true;
	}
}
