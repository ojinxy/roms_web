/**
 * Created By: oanguin
 * Date: Apr 22, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.dao.WreckingCompanyDAO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.WreckingCompanyDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.WreckingCompanyCriteriaBO;

/**
 * @author oanguin Created Date: Apr 22, 2013
 */
public class WreckingCompanyDAOImpl extends ParentDAOImpl implements
		WreckingCompanyDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fsl.ta.toms.roms.dao.WreckingCompanyDAO#getWreckingCompanyReference(java
	 * .util.List, java.util.HashMap, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WreckingCompanyDO> getWreckingCompanyReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		List<WreckingCompanyDO> wreckingCompanyList = (List<WreckingCompanyDO>)super.getReference(WreckingCompanyDO.class, status, filter);
		//return (List<WreckingCompanyDO>)super.getReference(WreckingCompanyDO.class, status, filter);
		

		Collections.sort(wreckingCompanyList, new Comparator<WreckingCompanyDO>() {
		    public int compare(WreckingCompanyDO result1, WreckingCompanyDO result2) {
		        return result1.getCompanyName().toLowerCase().compareTo(result2.getCompanyName().toLowerCase());
		    }
		});
		
		
		return wreckingCompanyList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WreckingCompanyDO> lookupWreckingCompany(
			WreckingCompanyCriteriaBO wcCriteriaBO) {

		List<WreckingCompanyDO> wreckingCompanyList = new ArrayList<WreckingCompanyDO>();
		Criteria criteria = this.getSession().createCriteria(
				WreckingCompanyDO.class);

		if (wcCriteriaBO.getWreckingCompanyId() != null) {
			criteria.add(Restrictions.eq("wreckingCompanyId",
					wcCriteriaBO.getWreckingCompanyId()));
		}

		if (StringUtils.isNotBlank(wcCriteriaBO.getTrnNbr())) {
			criteria.add(Restrictions.eq("trnNbr", wcCriteriaBO.getTrnNbr()));
		}

		if (StringUtils.isNotBlank(wcCriteriaBO.getTrnBranch())) {
			criteria.add(Restrictions.eq("trnBranch",
					wcCriteriaBO.getTrnBranch()));
		}

		if (StringUtils.isNotBlank(wcCriteriaBO.getCompanyName())) {
			criteria.add(Restrictions.like("companyName",
					"%" + wcCriteriaBO.getCompanyName()+ "%" ).ignoreCase());
		}

		if (StringUtils.isNotBlank(wcCriteriaBO.getContactPersonFirstName())) {
			criteria.add(Restrictions.like("contactPersonFirstName",
					"%" + wcCriteriaBO.getContactPersonFirstName()+ "%").ignoreCase());
		}

		if (StringUtils.isNotBlank(wcCriteriaBO.getContactPersonLastName())) {
			criteria.add(Restrictions.like("contactPersonLastName",
					"%" + wcCriteriaBO.getContactPersonLastName()+ "%").ignoreCase());
		}

		if (StringUtils.isNotBlank(wcCriteriaBO.getContactPersonMiddleName())) {
			criteria.add(Restrictions.like("contactPersonMiddleName",
					"%" + wcCriteriaBO.getContactPersonMiddleName()+ "%").ignoreCase());
		}

		if (StringUtils.isNotBlank(wcCriteriaBO.getParishCode())) {
			criteria.add(Restrictions.eq("address.parish.parishCode",
					wcCriteriaBO.getParishCode()));
		}
		
		if (StringUtils.isNotBlank(wcCriteriaBO.getStatusId())) {
			criteria.add(Restrictions.eq("status.statusId",
					wcCriteriaBO.getStatusId()));
		}


		if (StringUtils.isNotBlank(wcCriteriaBO.getShortDesc())) {
			criteria.add(Restrictions.like("shortDesc",
					"%" + wcCriteriaBO.getShortDesc()+ "%").ignoreCase());
		}
		// add the ordering
		criteria.addOrder(Order.asc("companyName"));

		wreckingCompanyList = criteria.list();

		if (wreckingCompanyList == null || wreckingCompanyList.size() < 1) {
			return null;
		}
		
		for(WreckingCompanyDO wreckingDO: wreckingCompanyList){
			if(wreckingDO.getAuditEntry().getUpdateUsername() != null)
				wreckingDO.getAuditEntry().setUpdateUsername(usernameToFullName(wreckingDO.getAuditEntry().getUpdateUsername()));
			else
				wreckingDO.getAuditEntry().setUpdateUsername(usernameToFullName(wreckingDO.getAuditEntry().getCreateUsername()));
		}

		return wreckingCompanyList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean wreckingCompanyExists(Integer wreckingCompanyId) {
		List<WreckingCompanyDO> wreckingCompanies = new ArrayList<WreckingCompanyDO>();

		Criteria criteria = this.getSession().createCriteria(
				WreckingCompanyDO.class);

		criteria.add(Restrictions.eq("wreckingCompanyId", wreckingCompanyId));

		wreckingCompanies = criteria.list();

		if (wreckingCompanies == null || wreckingCompanies.size() == 0) {
			return false;
		}

		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean wreckingCompanyShortDescriptionExists(WreckingCompanyBO wreckingCompany) {
		List<WreckingCompanyDO> wreckingCompanies = new ArrayList<WreckingCompanyDO>();

		Criteria criteria = this.getSession().createCriteria(
				WreckingCompanyDO.class);

		criteria.add(Restrictions.eq("shortDesc", wreckingCompany.getShortDesc()).ignoreCase());
		
		if(wreckingCompany.getWreckingCompanyId() != null)
			criteria.add(Restrictions.ne("wreckingCompanyId", wreckingCompany.getWreckingCompanyId()));


		wreckingCompanies = criteria.list();

		if (wreckingCompanies == null || wreckingCompanies.size() == 0) {
			return false;
		}

		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean wreckingCompanyNameExists(WreckingCompanyBO wreckingCompany) {
		List<WreckingCompanyDO> wreckingCompanies = new ArrayList<WreckingCompanyDO>();

		Criteria criteria = this.getSession().createCriteria(
				WreckingCompanyDO.class);

		criteria.add(Restrictions.eq("companyName", wreckingCompany.getCompanyName()).ignoreCase());
		
		if(wreckingCompany.getWreckingCompanyId() != null)
			criteria.add(Restrictions.ne("wreckingCompanyId", wreckingCompany.getWreckingCompanyId()));

		wreckingCompanies = criteria.list();

		if (wreckingCompanies == null || wreckingCompanies.size() == 0) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
    @Override
    public boolean trnBranchExists(WreckingCompanyBO wreckingCompanyBO) {
          List<WreckingCompanyDO> wreckingCompanies = new ArrayList<WreckingCompanyDO>();

          Criteria criteria = this.getSession().createCriteria(
                      WreckingCompanyDO.class);
          
         
         criteria.add(Restrictions.sqlRestriction(" cast(trn_branch as int)= cast(" + wreckingCompanyBO.getTrnBranch() + " as int)" + 
                  	 "AND  cast(trn_nbr as int) = cast(" + wreckingCompanyBO.getTrnNbr() +"as int)"));
         
         
          if(wreckingCompanyBO.getWreckingCompanyId() != null)
                criteria.add(Restrictions.ne("wreckingCompanyId", wreckingCompanyBO.getWreckingCompanyId()));

          wreckingCompanies = criteria.list();
          //System.err.println("TRN "+ wreckingCompanyBO.getTrnNbr() +" TRN BRanch" + wreckingCompanyBO.getTrnBranch() + "size " + wreckingCompanies.size());
          if (wreckingCompanies == null || wreckingCompanies.size() == 0) {
                return false;
          }
          return true;
    }
}
