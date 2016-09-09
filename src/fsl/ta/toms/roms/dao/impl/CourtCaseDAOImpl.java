/**
 * Created By: oanguin
 * Date: Jul 17, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.CourtCaseDAO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CourtCaseDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.CourtCaseCriteriaBO;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: Jul 17, 2013
 */
public class CourtCaseDAOImpl extends ParentDAOImpl implements CourtCaseDAO {

	
	/******************************** SAVE COURT CASE **************************************/
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.CourtCaseDAO#createCourtCase(fsl.ta.toms.roms.bo.CourtCaseBO)
	 */
	@Override
	public Serializable createCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException 
	{
		if(courtCaseExist(courtCaseBO.getSummonsId()))
		{
			
			throw new ErrorSavingException("Only one court case can exist for a summons.");
		}
		else
		{
			CourtCaseDO courtCaseDO = new CourtCaseDO();
			
			AuditEntry auditEntry = new AuditEntry();
			auditEntry.setCreateUsername(courtCaseBO.getCurrentUserName());
			auditEntry.setCreateDTime(Calendar.getInstance().getTime());
			
			courtCaseDO.setAuditEntry(auditEntry);
			
			courtCaseDO.setComment(courtCaseBO.getComment());
			
			//CourtDO court = this.hibernateTemplate.load(CourtDO.class, courtCaseBO.getCourtId());
			
			//courtCaseDO.setCourt(court);
			
			courtCaseDO.setCourtCaseNo(courtCaseBO.getCourtCaseNo());
			
			courtCaseDO.setStatus(this.hibernateTemplate.load(StatusDO.class, Constants.Status.COURT_CASE_OPEN));
			
			courtCaseDO.setSummons(this.hibernateTemplate.load(SummonsDO.class,courtCaseBO.getSummonsId()));
			
			
			return this.save(courtCaseDO);
		}

	}

	/*************************** UPDATE **********************************/
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.CourtCaseDAO#updateCourtCase(fsl.ta.toms.roms.bo.CourtCaseBO)
	 */
	@Override
	public void updateCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException 
	{
		CourtCaseDO courtCaseDO = this.find(CourtCaseDO.class, courtCaseBO.getCourtCaseId());
		
		/*if(courtCaseDO.getStatus().getStatusId().compareTo(Constants.Status.COURT_CASE_OPEN) != 0)
		{
			throw new ErrorSavingException("You are not allowed to update any fields on a court case which is not open.");
		}
		else
		{*/
			StringBuffer changeComments = new StringBuffer();
			
			if(courtCaseBO.getComment() != null && ! courtCaseBO.getComment().trim().equalsIgnoreCase(courtCaseDO.getComment()))
			{
				if(changeComments.length() > 0)
					changeComments.append(";");
				
				changeComments.append("{Comments were changed.}");
				
				courtCaseDO.setComment(courtCaseBO.getComment().trim());
			}
			
			if(courtCaseBO.getCourtCaseNo() != null && ! courtCaseBO.getCourtCaseNo().trim().equalsIgnoreCase(courtCaseDO.getCourtCaseNo()))
			{
				if(changeComments.length() > 0)
					changeComments.append(";");
				
				changeComments.append(String.format("{court_case_no:(ov:%s)-(nv:%s)}",courtCaseDO.getCourtCaseNo() == null?"":courtCaseDO.getCourtCaseNo(),courtCaseBO.getCourtCaseNo()));
				
				courtCaseDO.setCourtCaseNo(courtCaseBO.getCourtCaseNo());
			}
			
			/*Change Fine Amount and Prison Sentence added based on UR-034 by OA 11 Dec 2014*/
			/*if(courtCaseBO.getPrisonSentenceStartDate() != null 
					&& courtCaseBO.getPrisonSentenceStartDate() != null
					&& courtCaseBO.getPrisonSentenceStartDate().before(courtCaseBO.getPrisonSentenceEndDate())
					&& (courtCaseBO.getPrisonSentenceStartDate() != courtCaseDO.getPrisonSentenceStartDate() 
							|| courtCaseBO.getPrisonSentenceEndDate() != courtCaseDO.getPrisonSentenceEndDate())){*/
				
				//courtCaseDO.setPrisonSentenceStartDate(courtCaseBO.getPrisonSentenceStartDate());
				//courtCaseDO.setPrisonSentenceEndDate(courtCaseBO.getPrisonSentenceEndDate());
				
			//}
			
			//if(courtCaseBO.getFineAmount() != null && (courtCaseBO.getFineAmount() != courtCaseDO.getFineAmount())){
				courtCaseDO.setFineAmount(courtCaseBO.getFineAmount());
				courtCaseDO.setSentencePeriodDay(courtCaseBO.getSentencePeriodDay());
				courtCaseDO.setSentencePeriodMonth(courtCaseBO.getSentencePeriodMonth());
				courtCaseDO.setSentencePeriodYear(courtCaseBO.getSentencePeriodYear());
				courtCaseDO.setFinePaidFlag(courtCaseBO.getFinePaidFlag());
				courtCaseDO.setTimeServedFlag(courtCaseBO.getTimeServedFlag());
			//}
			
			
			//if(StringUtil.isSet(changeComments.toString()))
		//	{
				courtCaseDO.getAuditEntry().setUpdateUsername(courtCaseBO.getCurrentUserName());
				
				courtCaseDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				//courtCaseDO.getStatus().setStatusId(courtCaseBO.getCaseStatusCode());
				this.update(courtCaseDO);
				
				
			//}
		//}
	}

	
	
	@Override
	public void overrideCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException 
	{
		CourtCaseDO courtCaseDO = this.find(CourtCaseDO.class, courtCaseBO.getCourtCaseId());
		
		/*if(courtCaseDO.getStatus().getStatusId().compareTo(Constants.Status.COURT_CASE_OPEN) != 0)
		{
			throw new ErrorSavingException("You are not allowed to update any fields on a court case which is not open.");
		}
		else
		{*/
			//StringBuffer changeComments = new StringBuffer();
			
			/*if(courtCaseBO.getComment() != null && ! courtCaseBO.getComment().trim().equalsIgnoreCase(courtCaseDO.getComment()))
			{
				if(changeComments.length() > 0)
					changeComments.append(";");*/
				
				//changeComments.append("{Comments were changed.}");
				
				//courtCaseDO.setComment(courtCaseBO.getComment().trim());
			//}
			
//			if(courtCaseBO.getCourtCaseNo() != null && ! courtCaseBO.getCourtCaseNo().trim().equalsIgnoreCase(courtCaseDO.getCourtCaseNo()))
//			{
				/*if(changeComments.length() > 0)
					changeComments.append(";");
				
				changeComments.append(String.format("{court_case_no:(ov:%s)-(nv:%s)}",courtCaseDO.getCourtCaseNo() == null?"":courtCaseDO.getCourtCaseNo(),courtCaseBO.getCourtCaseNo()));
			*/	
				//courtCaseDO.setCourtCaseNo(courtCaseBO.getCourtCaseNo());
			//}
			
			/*Change Fine Amount and Prison Sentence added based on UR-034 by OA 11 Dec 2014*/
			/*if(courtCaseBO.getPrisonSentenceStartDate() != null 
					&& courtCaseBO.getPrisonSentenceStartDate() != null
					&& courtCaseBO.getPrisonSentenceStartDate().before(courtCaseBO.getPrisonSentenceEndDate())
					&& (courtCaseBO.getPrisonSentenceStartDate() != courtCaseDO.getPrisonSentenceStartDate() 
							|| courtCaseBO.getPrisonSentenceEndDate() != courtCaseDO.getPrisonSentenceEndDate())){*/
				
				/*courtCaseDO.setPrisonSentenceStartDate(courtCaseBO.getPrisonSentenceStartDate());
				courtCaseDO.setPrisonSentenceEndDate(courtCaseBO.getPrisonSentenceEndDate());*/
				
			//}
			
			//if(courtCaseBO.getFineAmount() != null && (courtCaseBO.getFineAmount() != courtCaseDO.getFineAmount())){
		
				courtCaseDO.setFineAmount(courtCaseBO.getFineAmount());
				courtCaseDO.setSentencePeriodDay(courtCaseBO.getSentencePeriodDay());
				courtCaseDO.setSentencePeriodMonth(courtCaseBO.getSentencePeriodMonth());
				courtCaseDO.setSentencePeriodYear(courtCaseBO.getSentencePeriodYear());
				courtCaseDO.setFinePaidFlag(courtCaseBO.getFinePaidFlag());
				courtCaseDO.setTimeServedFlag(courtCaseBO.getTimeServedFlag());
			//}
			
			
			//if(StringUtil.isSet(changeComments.toString()))
		//	{
				//courtCaseDO.getAuditEntry().setUpdateUsername(courtCaseBO.getCurrentUserName());
				
				//courtCaseDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				
				
				/*StatusDO courtStatus = (StatusDO) hibernateTemplate.get(StatusDO.class, courtCaseBO.getCaseStatusCode());
				if(courtStatus != null)
					courtCaseDO.setStatus(courtStatus);*/
				
				//courtCaseDO.getStatus().setStatusId(courtCaseBO.getCaseStatusCode());
				this.update(courtCaseDO);
				
				
			//}
		//}
	}

	/******************************* LOOKUP COURT CASE **************************/
	
	/* (non-Javadoc)
	 * @see fsl.ta.toms.roms.dao.CourtCaseDAO#lookUpCourtCase()
	 */
	@Override
	public List<CourtCaseBO> lookUpCourtCase(CourtCaseCriteriaBO searchCriteria) 
	{
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(CourtCaseDO.class,"cc");
		
		/*create aliases for tables*/
		criteria.createAlias("cc.summons", "sum");
		criteria.createAlias("sum.roadCheckOffenceOutcome.roadCheckOffence.roadCheck.compliance.roadOperation", "roadOp");
		criteria.createAlias("sum.taStaff", "taStaff");
		//criteria.createAlias("cc.court", "court");
		//criteria.createAlias("cc.verdict", "verdict");
		criteria.createAlias("cc.status", "status");
		
		
		if(searchCriteria.getCourtCaseId() != null && searchCriteria.getCourtCaseId() > 0)
		{
			criteria.add(Restrictions.eq("cc.courtCaseId", searchCriteria.getCourtCaseId()));
		}
		
		if( StringUtil.isSet(searchCriteria.getCourtCaseNo()))
		{
			criteria.add(Restrictions.eq("cc.courtCaseNo", searchCriteria.getCourtCaseNo().trim()));
		}
		
		/*if(searchCriteria.getCourtId() != null && searchCriteria.getCourtId() > 0)
		{
			criteria.add(Restrictions.eq("court.courtId", searchCriteria.getCourtId()));
		}*/
		
		if(StringUtil.isSet(searchCriteria.getOfficeLocCode()))
		{
			criteria.add(Restrictions.eq("roadOp.officeLocCode", searchCriteria.getOfficeLocCode().trim()));
		}
		
		if(searchCriteria.getRoadOperationId() != null && searchCriteria.getRoadOperationId() > 0)
		{
			criteria.add(Restrictions.eq("roadOp.roadOperationId", searchCriteria.getRoadOperationId()));
		}
		
		if( StringUtil.isSet(searchCriteria.getStaffId()))
		{
			criteria.add(Restrictions.eq("taStaff.staffId", searchCriteria.getStaffId()));
		}
		
		if( StringUtil.isSet(searchCriteria.getInspectorFirstName()))
		{
			criteria.add(Restrictions.eq("taStaff.person.firstName", searchCriteria.getInspectorFirstName()));
		}
		
		if( StringUtil.isSet(searchCriteria.getInspectorLastName()))
		{
			criteria.add(Restrictions.eq("taStaff.person.lastName", searchCriteria.getInspectorLastName()));
		}
		
		if(StringUtil.isSet(searchCriteria.getStatusCode()))
		{
			criteria.add(Restrictions.eq("status.statusId", searchCriteria.getStatusCode().trim()));
		}
		
		if(searchCriteria.getSummonsId() != null && searchCriteria.getSummonsId() > 0)
		{
			criteria.add(Restrictions.eq("sum.summonsId", searchCriteria.getSummonsId()));
		}
		
		if( searchCriteria.getVerdictCode() != null && searchCriteria.getVerdictCode() > 0)
		{
			criteria.add(Restrictions.eq("cc.verdict.verdict_code", searchCriteria.getVerdictCode()));
		}
		
		
		if( StringUtil.isSet(searchCriteria.getDefendantFirstName()))
		{
			criteria.add(Restrictions.eq("summ.offender.firstName", searchCriteria.getDefendantFirstName()));
		}
		
		if( StringUtil.isSet(searchCriteria.getDefendantLastName()))
		{
			criteria.add(Restrictions.eq("summ.offender.lastName", searchCriteria.getDefendantLastName()));
		}
		
		List<CourtCaseBO> courtCaseList = new ArrayList<CourtCaseBO>();
		
		criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		@SuppressWarnings("rawtypes")
		Iterator iterator = criteria.list().iterator();
		
		while(iterator.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map map = (Map) iterator.next();
			
			CourtCaseDO courtCaseDO = (CourtCaseDO) map.get("cc");
		
			CourtCaseBO courtCaseBO = new CourtCaseBO(courtCaseDO);
			
			
			courtCaseList.add(courtCaseBO);
		}
		
		return courtCaseList;
	}

	
	@Override
	public CourtCaseBO lookUpCourtCaseBySummonsId(Integer summonsId) {
		Criteria criteria = this.getSession().createCriteria(CourtCaseDO.class);

		criteria.add(Restrictions.eq("summons.summonsId", summonsId));

		List<CourtCaseDO> res = criteria.list();

		if (res != null && res.size() > 0) {

			CourtCaseBO courtCaseBO = new CourtCaseBO(res.get(0));

			return courtCaseBO;
		}

		else {
			return null;
		}

	}
	
	/**
	 * 
	 * @param summonsId
	 * @return
	 */
	private boolean courtCaseExist(Integer summonsId)
	{
		
		//System.err.println("Summons ID: "+summonsId);
		
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(CourtCaseDO.class,"cc");
		
		criteria.add(Restrictions.eq("cc.summons.summonsId", summonsId));
		
		if(criteria.list().size() > 0)
			return true;
		else
			return false;
				
	}
	
	

	@Override
	public List<CourtCaseDO> getCourtCaseReference(HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<CourtCaseDO>)super.getReference(CourtCaseDO.class, status, filter);
	}
	
	
	
}
