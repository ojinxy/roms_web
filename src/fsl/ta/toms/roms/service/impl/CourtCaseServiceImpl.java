/**
 * Created By: oanguin
 * Date: Jul 18, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import java.io.Serializable;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.CourtCaseDAO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CDEventRefTypeDO;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.dataobjects.CourtCaseDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtCaseCriteriaBO;
import fsl.ta.toms.roms.service.CourtCaseService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin Created Date: Jul 18, 2013
 */
@Transactional
public class CourtCaseServiceImpl implements CourtCaseService {

	@Autowired
	private DAOFactory		daoFactory;

	@Autowired
	private ServiceFactory	serviceFactory;

	@Override
	public Serializable createCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException, RequiredFieldMissingException {
		CourtCaseDAO courtCaseDAO = this.daoFactory.getCourtCaseDAO();

		boolean requiredFieldMissing = false;

		if (courtCaseBO == null)
			throw new RequiredFieldMissingException();

		if (!StringUtil.isSet(courtCaseBO.getCurrentUserName()))
			requiredFieldMissing = true;

		/*
		 * if(! StringUtil.isSet(courtCaseBO.getCurrentUserRegion()))
		 * requiredFieldMissing =true;
		 */
		/*
		 * if (courtCaseBO.getCourtId() == null || courtCaseBO.getCourtId() < 1)
		 * requiredFieldMissing = true;
		 */
		if (courtCaseBO.getSummonsId() == null || courtCaseBO.getSummonsId() < 1)
			requiredFieldMissing = true;

		if (requiredFieldMissing)
			throw new RequiredFieldMissingException();

		/************* SAVE ****************/
		Serializable caseId = courtCaseDAO.createCourtCase(courtCaseBO);

		// set id on business object
		courtCaseBO.setCourtCaseId((Integer) caseId);

		/*************** AUDIT ******************/
		// create the audit entry object
		//AuditEntry auditEntry = new AuditEntry();
		// set the audit details
		//auditEntry.setCreateUsername(courtCaseBO.getCurrentUserName());
		//auditEntry.setCreateDTime(Calendar.getInstance().getTime());
		//EventAuditDO eventAudit = createCourtCaseEventAudit(courtCaseBO, auditEntry);

		//Removed as a result of review of event audit driven by UR-037 
		
		// save the event audit
		//serviceFactory.getEventAuditService().saveEventAuditDO(eventAudit);

		return caseId;
	}

	@Override
	public Serializable createCourtCaseNFirstAppearanceForSummons(SummonsBO summons) throws ErrorSavingException, RequiredFieldMissingException {
		CourtCaseDAO courtCaseDAO = this.daoFactory.getCourtCaseDAO();

		if (summons == null)
			throw new RequiredFieldMissingException("Summons object cannot be empty.");

		/************** SAVE COURT CASE ****************/
		CourtCaseBO courtCaseBO = new CourtCaseBO(summons);
		Integer caseId = (Integer) courtCaseDAO.createCourtCase(courtCaseBO);

		// set id on court case business object
		courtCaseBO.setCourtCaseId(caseId);

		/************* CREATE NEW COURT APPEARANCE *********/

		CourtAppearanceBO appearanceBO = new CourtAppearanceBO(summons);
		appearanceBO.setCourtCaseId(caseId);

		Integer courtAppearanceId = (Integer) serviceFactory.getCourtAppearanceService().saveCourtAppearance(appearanceBO);

		// set id on the court appearance object
		appearanceBO.setCourtAppearanceId(courtAppearanceId);

		/*************** AUDIT ******************/
		// create the audit entry object
		AuditEntry auditEntry = new AuditEntry();
		// set the audit details
		auditEntry.setCreateUsername(courtCaseBO.getCurrentUserName());
		auditEntry.setCreateDTime(Calendar.getInstance().getTime());
		EventAuditDO eventAudit = createCourtCaseEventAudit(courtCaseBO, auditEntry);

		// save the event audit
		serviceFactory.getEventAuditService().saveEventAuditDO(eventAudit);
		/************ END *****************/
		// set objects on summons
		courtCaseBO.getCourtAppearances().add(appearanceBO);
		summons.setCourtCase(courtCaseBO);

		return caseId;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateCourtCase(CourtCaseBO courtCaseBO) throws ErrorSavingException, RequiredFieldMissingException {

		//Based on review of event audit, as a spin off from UR-037
		//Retrieve saved court case in order to compare old and new court case number
		CourtCaseDO savedCourtCaseAux = (CourtCaseDO) daoFactory.getCourtCaseDAO().find(CourtCaseDO.class, courtCaseBO.getCourtCaseId());
		
		CourtCaseBO savedCourtCase = new CourtCaseBO(savedCourtCaseAux);
		
		
		if (courtCaseBO == null)
			throw new RequiredFieldMissingException();

		if (courtCaseBO.getCourtCaseId() == null || courtCaseBO.getCourtCaseId() < 1)
			throw new RequiredFieldMissingException("Court Case Id is required.");

		if (StringUtils.isBlank(courtCaseBO.getCurrentUserName()))
			throw new RequiredFieldMissingException("Current User Name is required.");

		// update the actual court case
		daoFactory.getCourtCaseDAO().updateCourtCase(courtCaseBO);

		
		/*************** AUDIT ******************/
		// create the audit entry object
		AuditEntry auditEntry = new AuditEntry();
		// set the audit details
		auditEntry.setCreateUsername(courtCaseBO.getCurrentUserName());
		auditEntry.setCreateDTime(Calendar.getInstance().getTime());
		EventAuditDO eventAudit = updateCourtCaseEventAudit(courtCaseBO, auditEntry,savedCourtCase);
		serviceFactory.getEventAuditService().saveEventAuditDO(eventAudit);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void overrideCourtCaseDetails(CourtCaseBO courtCaseBO) throws ErrorSavingException, RequiredFieldMissingException {

		EventAuditDO eventAudit = null;
		Boolean courtDateOverride = false;
		Boolean courtOverride = false;

		if (courtCaseBO == null)
			throw new RequiredFieldMissingException();

		if (courtCaseBO.getCourtCaseId() == null || courtCaseBO.getCourtCaseId() < 1)
			throw new RequiredFieldMissingException("Court Case Id is required.");

		if (StringUtils.isBlank(courtCaseBO.getCurrentUserName()))
			throw new RequiredFieldMissingException("Current User Name is required.");

		// check for list of court appearances, iterate and update
		List<CourtAppearanceBO> courtAppearancesList = courtCaseBO.getCourtAppearances();

		// get the first item and test if it is new
		if (courtAppearancesList != null && !courtAppearancesList.isEmpty()) {

			// NEWEST COURT APPEARANCE
			CourtAppearanceBO appearance = courtAppearancesList.get(0);

//			CourtAppearanceDO appearanceFromDB = daoFactory.getCourtAppearanceDAO().getCourtAppearanceByID(appearance.getCourtAppearanceId());
			CourtAppearanceDO appearanceFromDBAux = daoFactory.getCourtAppearanceDAO().find(CourtAppearanceDO.class,appearance.getCourtAppearanceId());
			
			CourtAppearanceBO appearanceFromDB = new CourtAppearanceBO(appearanceFromDBAux);
			// if teh record is in the database then cross reference
			// System.err.println(" appearance " + appearanceFromDB);
			if (appearanceFromDB != null) {

				if (appearance.getCourtRulingId() == null && (appearance.getCourtRulingId() != appearanceFromDB.getCourtRulingId()) && appearance.getVerdictId()!=null && !appearance.getVerdictId().equals(Constants.VERDICT.WITHDRAWN)) {
					throw new RequiredFieldMissingException("Court Ruling is required.");
				}
				// it is a new court appearance
				if (appearance.getCourtAppearanceId() == null) {
					throw new RequiredFieldMissingException("Court Appearance Id is required.");
				} else {
					
					appearance.setCurrentUserName(courtCaseBO.getCurrentUserName());
					serviceFactory.getCourtAppearanceService().overrideCourtAppearance(appearance);
					// update the actual court case
					
					daoFactory.getCourtCaseDAO().overrideCourtCase(courtCaseBO);
					
					
					
				}
				
				StringBuffer comment = new StringBuffer();
				
				//get summons
				Integer summID = courtCaseBO.getSummonsId();
				SummonsBO summonsBO = null;
				if(summID != null)
					summonsBO = daoFactory.getSummonsDAO().lookupSummonsById(summID);
				
				// AUDIT WHAT CHANGED
				// create the audit entry object
				AuditEntry auditEntry = new AuditEntry();
				// set the audit details
				auditEntry.setCreateUsername(courtCaseBO.getCurrentUserName());
				auditEntry.setCreateDTime(Calendar.getInstance().getTime());
			
				eventAudit = updateCourtAppearanceEventAudit(appearanceFromDB, auditEntry,summonsBO);
				eventAudit.setEventCode(Constants.EventCode.OVERRIDE_COURT_APPEARANCE);
				
				if (appearance.getCourtDate() != null) {
						//IF COURT DATE CHANGED
						if (new java.sql.Date(appearanceFromDB.getCourtDate().getTime()).before(new java.sql.Date(appearance.getCourtDate().getTime())) || new java.sql.Date(appearanceFromDB.getCourtDate().getTime()).after(new java.sql.Date(appearance.getCourtDate().getTime()))) {
							comment.append("Previous Court Date: " + new java.sql.Date(appearanceFromDB.getCourtDate().getTime()) + " ; New Court Date: " + new java.sql.Date(appearance.getCourtDate().getTime()));
						}
						
						//IF COURT CHANGED
						if(!(appearanceFromDB.getCourtId()+" ".trim()).equalsIgnoreCase(appearance.getCourtId() + " ".trim()))
						{
							comment.append("Previous Court: " + appearanceFromDB.getCourtName() + " ;New Court: " + appearance.getCourtName());
						}
						//IF ANYTING ELSE CHANGED
						else 
						{
							//eventAudit.setEventCode(Constants.EventCode.OVERRIDE_COURT_APPEARANCE);
						
							if (appearance.getCourtRulingId() != appearanceFromDB.getCourtRulingId())
								comment.append("Previous Ruling: " + appearanceFromDB.getCourtRulingId() + " ; New Ruling: " + appearance.getCourtRulingId());
	
							if (appearance.getPleaId() != appearanceFromDB.getPleaId())
								comment.append("Previous Plea: " + appearanceFromDB.getPleaId() + " ; New Plea: " + appearance.getPleaId() );
	
							if (appearance.getVerdictId() != appearanceFromDB.getVerdictId())
								comment.append("Previous Verdict: " + appearanceFromDB.getVerdictId() + " ; New Verdict: " + appearance.getVerdictId());
							
//							if (appearance.getInspectorAttended() != appearanceFromDB.getInspectorAttended())
//								comment.append("Previous Inspector Attended: " + appearanceFromDB.getInspectorAttended() + " / " + "New Inspector Attended: " + appearance.getInspectorAttended() + "; ";
						}
					
				}else{
					throw new RequiredFieldMissingException("Court Date is required");
				}

				//TODO - NEED TO SEE IF U.I. is sending back reason
				//comment.append("Reason: " + );
				
				if(StringUtil.isSet(appearance.getComment()))
				{
					comment.append(" ; Comment: " + appearance.getComment());
				}
				
				eventAudit.setComment(comment.toString());

				serviceFactory.getEventAuditService().saveEventAuditDO(eventAudit);
			}

		} 
		
		//commented out as per event audit review, driven by UR-037
		//else {
			// jreid 2014-06-18
			// i pass the court appearance list empty deliberately to force an
			// update
			// of the court case only

			// update the actual court case
			//daoFactory.getCourtCaseDAO().overrideCourtCase(courtCaseBO);

			/*************** AUDIT ******************/
			// create the audit entry object
			//AuditEntry auditEntry = new AuditEntry();
			// set the audit details
			//auditEntry.setCreateUsername(courtCaseBO.getCurrentUserName());
			//auditEntry.setCreateDTime(Calendar.getInstance().getTime());
			//eventAudit = updateCourtCaseEventAudit(courtCaseBO, auditEntry);
			//eventAudit.setEventCode(Constants.EventCode.OVERRIDE_COURT_CASE);
		//}

		serviceFactory.getEventAuditService().saveEventAuditDO(eventAudit);
	}

	@Override
	public List<CourtCaseBO> lookUpCourtCase(CourtCaseCriteriaBO criteria) throws NoRecordFoundException, RequiredFieldMissingException {

		if (criteria == null)
			throw new RequiredFieldMissingException();

		CourtCaseDAO courtCaseDAO = this.daoFactory.getCourtCaseDAO();

		List<CourtCaseBO> courtCaseList = courtCaseDAO.lookUpCourtCase(criteria);

		if (courtCaseList.size() < 1)
			throw new NoRecordFoundException();
		else {
			// set the court appearances on the court case object
			for (CourtCaseBO courtCase : courtCaseList) {
				List<CourtAppearanceBO> courtAppearances = this.serviceFactory.getCourtAppearanceService().getCourtAppearancesBySummonsId(courtCase.getSummonsId());
				courtCase.setCourtAppearances(courtAppearances);
			}
		}

		return courtCaseList;
	}

	@Override
	@Transactional(readOnly = true)
	public CourtCaseBO getCourtCaseBySummonsId(Integer summonsId) throws RequiredFieldMissingException {

		if (summonsId == null)
			throw new RequiredFieldMissingException();

		// add trial details
		CourtCaseCriteriaBO courtCriteria = new CourtCaseCriteriaBO();
		courtCriteria.setSummonsId(summonsId);
		CourtCaseBO courtCaseBO = this.daoFactory.getCourtCaseDAO().lookUpCourtCaseBySummonsId(summonsId);

		if (courtCaseBO != null) {

			List<CourtAppearanceBO> courtAppearances = this.serviceFactory.getCourtAppearanceService().getCourtAppearancesBySummonsId(summonsId);

			courtCaseBO.setCourtAppearances(courtAppearances);

			if (courtAppearances != null && !courtAppearances.isEmpty()) {
				courtCaseBO.setNextAppearanceCourtDate(courtAppearances.get(0).getCourtDate());
				courtCaseBO.setNextAppearanceCourtTime(courtAppearances.get(0).getCourtTime());
				courtCaseBO.setCourtName(courtAppearances.get(0).getCourtName());
			}
			return courtCaseBO;
		} else
			return null;
	}

	/********************* HELPER CLASSES **************************/
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public CourtCaseBO cancelCourtCase(CourtCaseBO courtCaseBO) {
		List<CourtAppearanceBO> courtAppearancesList = courtCaseBO.getCourtAppearances();

		try {
			for (CourtAppearanceBO appearance : courtAppearancesList) {

				appearance.setStatusCode(Constants.Status.COURT_CASE_CANCELLED);

				serviceFactory.getCourtAppearanceService().updateCourtAppearance(appearance, null);

			} // end for loop

			// update the case record
			daoFactory.getCourtCaseDAO().updateCourtCase(courtCaseBO);

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, courtCaseBO.getSummonsId());

			if (summonsDO != null) {
				summonsDO.getStatus().setStatusId(courtCaseBO.getCaseStatusCode());
				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(courtCaseBO.getCurrentUserName());

				daoFactory.getSummonsDAO().saveOrUpdate(summonsDO);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return courtCaseBO;

	}

	/**
	 * 
	 * @param courtCaseBO
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public CourtCaseBO withdrawCourtCase(CourtCaseBO courtCaseBO) {
		List<CourtAppearanceBO> courtAppearancesList = courtCaseBO.getCourtAppearances();

		try {
			for (CourtAppearanceBO appearance : courtAppearancesList) {

				appearance.setStatusCode(Constants.Status.COURT_CASE_WITHDRAWN);

				serviceFactory.getCourtAppearanceService().updateCourtAppearance(appearance, null);

			} // end for loop

			// update the case record
			daoFactory.getCourtCaseDAO().updateCourtCase(courtCaseBO);

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, courtCaseBO.getSummonsId());

			if (summonsDO != null) {
				summonsDO.getStatus().setStatusId(courtCaseBO.getCaseStatusCode());
				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(courtCaseBO.getCurrentUserName());

				daoFactory.getSummonsDAO().update(summonsDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return courtCaseBO;

	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public CourtCaseBO closeCourtCase(CourtCaseBO courtCaseBO) {
		List<CourtAppearanceBO> courtAppearancesList = courtCaseBO.getCourtAppearances();

		try {
			for (CourtAppearanceBO appearance : courtAppearancesList) {

				appearance.setStatusCode(Constants.Status.COURT_CASE_CLOSED);

				serviceFactory.getCourtAppearanceService().updateCourtAppearance(appearance, null);

			} // end for loop

			/***************** UPDATE THE COURT CASE **********************/
			// update the case record
			daoFactory.getCourtCaseDAO().updateCourtCase(courtCaseBO);

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, courtCaseBO.getSummonsId());

			if (summonsDO != null) {
				summonsDO.getStatus().setStatusId(courtCaseBO.getCaseStatusCode());
				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(courtCaseBO.getCurrentUserName());

				daoFactory.getSummonsDAO().update(summonsDO);
			}
			/************************ END *********************************/

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return courtCaseBO;
	}

	/********************** AUDIT ********************************/

	/**
	 * This method is to be used to create a audit record for new CourtCases
	 * being created.
	 * 
	 * @param <code>courtcase</code> This is the appearance object which is
	 *        being saved.
	 * @param <code>auditEntry</code> This is the audit details about the person
	 *        doing the process.
	 * @return EventAudit
	 */
	private EventAuditDO createCourtCaseEventAudit(CourtCaseBO courtCase, AuditEntry auditEntry) {
		EventAuditDO eventAudit = new EventAuditDO();

		eventAudit.setAuditEntry(auditEntry);

		eventAudit.setEventCode(Constants.EventCode.CREATE_COURT_CASE);
		// eventAudit.setComment("New Court Case Record Created.");

//		eventAudit.setRefType1Code(Constants.EventRefTypeCode.COURT_CASE_ID + "");
//		eventAudit.setRefValue1(String.valueOf(courtCase.getCourtCaseId()));

		Integer summID = courtCase.getSummonsId();
		
		SummonsDO summons = daoFactory.getSummonsDAO().find(SummonsDO.class,summID);
		
		if(summons != null)		
		{
			eventAudit.setRefType2Code(Constants.EventRefTypeCode.SUMMONS_ID + "");
			eventAudit.setRefValue2(Constants.formatRefSeqNo(summons.getReferenceNo()));
		}

		return eventAudit;

	}

	/**
	 * This method is to be used to create a audit record for UPDATING
	 * CourtCases being created.
	 * @param savedCourtCase 
	 * 
	 * @param <code>courtcase</code> This is the appearance object which is
	 *        being saved.
	 * @param <code>auditEntry</code> This is the audit details about the person
	 *        doing the process.
	 * @return EventAudit
	 */
	private EventAuditDO updateCourtCaseEventAudit(CourtCaseBO courtCase, AuditEntry auditEntry, CourtCaseBO savedCourtCase) {
		EventAuditDO eventAudit = new EventAuditDO();
		
		StringBuffer comment =  new StringBuffer();
		
		eventAudit.setAuditEntry(auditEntry);

		eventAudit.setEventCode(Constants.EventCode.UPDATE_COURT_CASE);
		//eventAudit.setComment(courtCase.getComment());

		Integer summID = courtCase.getSummonsId();
		
		SummonsDO summons = daoFactory.getSummonsDAO().find(SummonsDO.class,summID);
		
		if(summons != null)		
		{
			eventAudit.setRefType1Code(Constants.EventRefTypeCode.SUMMONS_ID);
			eventAudit.setRefValue1(Constants.formatRefSeqNo(summons.getReferenceNo()));
		}
		
		eventAudit.setRefType2Code(Constants.EventRefTypeCode.COURT_CASE_NUMBER);
		eventAudit.setRefValue2(courtCase.getCourtCaseNo());

		
		if(savedCourtCase.getCourtCaseNo() != null && courtCase.getCourtCaseNo() != null)
		{
			if(!(savedCourtCase.getCourtCaseNo().equalsIgnoreCase(courtCase.getCourtCaseNo())))
			{
				comment.append("Old Case No: " + savedCourtCase.getCourtCaseNo());
			}
		}

		
		if(StringUtil.isSet(courtCase.getComment()))
		{
			comment.append(StringUtil.appendSemiColon(comment.toString(), " Comment: " + courtCase.getComment() )); //the reason details are actually sent over with a semi-colon at the end+ ";");
		}
		
		
		eventAudit.setComment(comment.toString());
		
		return eventAudit;

	}

	

	private EventAuditDO updateCourtAppearanceEventAudit(CourtAppearanceBO courtAppear, AuditEntry auditEntry, SummonsBO summonsBO) {
		EventAuditDO eventAudit = new EventAuditDO();

		eventAudit.setAuditEntry(auditEntry);

		eventAudit.setEventCode(Constants.EventCode.OVERRIDE_COURT_APPEARANCE);
		eventAudit.setComment(courtAppear.getComment());

		eventAudit.setRefType1Code(Constants.EventRefTypeCode.SUMMONS_ID);
		eventAudit.setRefValue1(summonsBO.getReferenceNo());

		Integer courtCaseID = courtAppear.getCourtCaseId();
		CourtCaseDO courtCase = (CourtCaseDO)daoFactory.getCourtCaseDAO().find(CourtCaseDO.class, courtCaseID);
		
		if (courtCase != null)
		{
			String ccNo = courtCase.getCourtCaseNo();
			
			if(StringUtil.isSet(ccNo))
			{
					eventAudit.setRefType2(daoFactory.getCourtCaseDAO().load(CDEventRefTypeDO.class, Constants.EventRefTypeCode.COURT_CASE_NUMBER));
					eventAudit.setRefValue2(ccNo);
			}
		}
		
		return eventAudit;

	}

}
