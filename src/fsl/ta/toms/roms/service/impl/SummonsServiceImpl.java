/**
 * Created By: jreid
 * Date: Apr 30, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.DocumentStatus;
import fsl.ta.toms.roms.constants.Constants.DocumentType;
import fsl.ta.toms.roms.constants.Constants.YesNo;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.SummonsDAO;
import fsl.ta.toms.roms.dataobjects.AddressDO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.dataobjects.CourtCaseDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.JPDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.RoadCheckOffenceOutcomeDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.documents.view.NoticeView;
import fsl.ta.toms.roms.documents.view.SummonsDischargeAndReleaseFormView;
import fsl.ta.toms.roms.documents.view.SummonsView;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.ReasonCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.SummonsService;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author jreid Created Date: Apr 30, 2013
 */

public class SummonsServiceImpl implements SummonsService {

	@Autowired
	private ServiceFactory	serviceFactory;

	private DAOFactory		daoFactory;

	/**
	 * @param daoFactory
	 */
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	/**
	 * This method saves the summons, with a new court case and court appearance
	 * 
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer saveSummons(SummonsBO summons) throws ErrorSavingException {

		// create a summons data object from the view object
		SummonsDO summonsTosave = new SummonsDO(summons);

		// get the current time
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		try {
			// create the audit entry object
			AuditEntry auditEntry = new AuditEntry();

			// set the audit details
			auditEntry.setCreateUsername(summons.getCurrentUsername());
			auditEntry.setCreateDTime(currentDate);

			// set the audit entry details
			summonsTosave.setAuditEntry(auditEntry);

			summonsTosave.setExcerpt(createSummonsExcerpt(summons.getOffenceExcerpt(), summons.getExcerptParams()));

			// set the road operation outcome
			RoadCheckOffenceOutcomeDO roadCheckOffenceOutcomeDO = this.daoFactory.getSummonsDAO().find(RoadCheckOffenceOutcomeDO.class, summons.getRoadCheckOffenceOutcomeCode());
			if (roadCheckOffenceOutcomeDO != null)
				summonsTosave.setRoadCheckOffenceOutcome(roadCheckOffenceOutcomeDO);
			else
				throw new InvalidFieldException("Road Check Outcome Id is invalid.");

			// get the offender object
			PersonDO offender = this.daoFactory.getSummonsDAO().find(PersonDO.class, summons.getOffenderId());
			// set the operation object
			if (offender != null)
				summonsTosave.setOffender(offender);
			else
				throw new InvalidFieldException("Offender Id is invalid.");
			
				
			/**
			 * If offence type is Aiding and Abetting Driver etc.(Summons 35 & 36) change offender to the owner		 * 
			 */
			//Did this check in RoadCompliancyServiceImpl line 542 instead. 
			//This was to address a persistence issue as the aid and abet owner was not being retrieved from the 
			//complianceDO in the session.
//			try{
//				Integer offenceCode = summonsTosave.getRoadCheckOffenceOutcome().
//						getRoadCheckOffence().getOffence().getOffenceId();
//			
//				if(offenceCode.intValue()==Constants.OffenceAidAndAbbet.NO_ROAD_LIC.intValue()||
//						offenceCode.intValue()==Constants.OffenceAidAndAbbet.NO_PPV_INSUR.intValue()){
//					PersonDO personDO = summonsTosave.getRoadCheckOffenceOutcome().
//							getRoadCheckOffence().getRoadCheck().getCompliance().getAidAbetVehicleOwner();
//					
//					summonsTosave.setOffender(personDO);
//				}
//			}catch(Exception exception){
//				exception.printStackTrace();
//			}

			// get the staff object
			TAStaffDO staff = this.daoFactory.getSummonsDAO().find(TAStaffDO.class, summons.getTaStaffId());
			// set the operation object
			if (staff != null)
				summonsTosave.setTaStaff(staff);
			else
				throw new InvalidFieldException("Staff Id is invalid.");

			if (!StringUtils.isEmpty(summons.getVerificationRequired()))
				summonsTosave.setVerificationReq(summons.getVerificationRequired());

			/*
			 * // get the JP object JPDO jp =
			 * this.daoFactory.getSummonsDAO().find(JPDO.class,
			 * summons.getJpRegNumber()); // set the operation object if (jp !=
			 * null) summonsTosave.setJusticeOfPeace(jp); else throw new
			 * InvalidFieldException("JP Id is invalid.");
			 */

			StatusDO status = null;
			// If the manual serial number is not null, set status to printed
			if (StringUtils.isNotBlank(summons.getManualSerialNo())) {
				// set the status
				status = this.daoFactory.getSummonsDAO().find(StatusDO.class, Constants.DocumentStatus.PRINTED);
			} else {
				// set the status
				status = this.daoFactory.getSummonsDAO().find(StatusDO.class, Constants.DocumentStatus.CREATED);
			}

			// set the operation object
			if (status != null)
				summonsTosave.setStatus(status);
			else
				throw new InvalidFieldException("Status Id is invalid.");

			Integer summonId = (Integer) this.daoFactory.getSummonsDAO().save(summonsTosave);
			
			SummonsDO summonsSaved = this.daoFactory.getSummonsDAO().find(SummonsDO.class, summonId);

			/**************************** AUDITING **********************/
			// set id on view object
			summons.setAutomaticSerialNo(summonId);
			summons.setReferenceNo(Constants.formatRefSeqNo(summonsSaved.getReferenceNo()));

			// save audit for summons
			EventAuditDO eventAuditDO = createEventAuditRecord(summons);
			
			//set event code 
			eventAuditDO.setEventCode(Constants.EventCode.CREATE_DOCUMENT);
			eventAuditDO.setEvent(new CDEventDO(Constants.EventCode.CREATE_DOCUMENT));
			
			eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
			eventAuditDO.getAuditEntry().setCreateUsername(summons.getCurrentUsername());
			this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);

			/************ SAVE SCANNED DOCS ***********/
			if (summons.getScannedDocList() != null && !summons.getScannedDocList().isEmpty()) {
				// List<ScannedDocBO> summonsDocList =
				// summons.getScannedDocList().get(0).setAutomaticSerialNo(summonId);
				processScannedSummonsDocuments(summons.getScannedDocList());
			}

			/****************** CREATE COURT CASE & FIRST COURT APPEARANCE ***********************/
			Integer newCourtCaseId = (Integer) serviceFactory.getCourtCaseService().createCourtCaseNFirstAppearanceForSummons(summons);

			if (newCourtCaseId == null)
				throw new ErrorSavingException("There was an error saving court case.");

			// return id of new summons
			return summonId;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException("There was an error saving summons.");

		}
	}

	/**
	 * 
	 * @param excerpt
	 * @param excerptParams
	 * @return
	 */
	private String createSummonsExcerpt(String excerpt, HashMap<String, String> excerptParams) {

		try {
			// OA - 23 Jan 2013 : This code is crashing on a null pointer
			// exception
			if (StringUtils.isNotBlank(excerpt) && excerptParams != null) {
				List<String> list = new ArrayList<String>(excerptParams.keySet());
				for (String key : list) {
					// replace the parameters with the data coming over
					if (excerptParams.get(key) != null) {
						String value = excerptParams.get(key).trim();
						String keyS = "[" + key + "]"; // /
						excerpt = StringUtils.replace(excerpt, keyS, value);
						// System.err.println(" the param " + keyS + " = " +
					}
				}
			}

			return excerpt;
		} catch (Exception exe) {
			exe.printStackTrace();
			return excerpt;
		}

	}

	/**
	 * This method updates the summons
	 * 
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 * @throws InvalidFieldException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SummonsBO updateSummons(SummonsBO summons) throws ErrorSavingException, InvalidFieldException {

		EventAuditDO eventAuditDO = null;

		try {
			if (summons == null)
				throw new ErrorSavingException("Summons details cannot be empty");

			/************************ CANCELLATION *********************/
			if (summons.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED)) {

				cancelSummons(summons);

				// create audit trail
				eventAuditDO = createEventAuditRecord(summons);
				eventAuditDO.setEventCode(Constants.EventCode.CANCEL_DOCUMENT);
			}

			/******************* SERVING ***********************************/
			if (summons.getNewStatusId().equals(Constants.DocumentStatus.SERVED)) {

				serveSummons(summons);

				// create audit trail
				eventAuditDO = createEventAuditRecord(summons);
				eventAuditDO.setEventCode(Constants.EventCode.SERVE_DOCUMENT);
			}

			/******************* Not Authorising details ***********************************/
			if (summons.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.DENY_AUTHORISATION)) {

				denyAuthorisationSummonsDetails(summons);
				//System.err.println(" denying ");
				// create audit trail
				eventAuditDO = createEventAuditRecordDenyVerify(summons);
				if(eventAuditDO != null)
					eventAuditDO.setEventCode(Constants.EventCode.REJECT_DOCUMENT);
			}
			
			/******************* Authorising details ***********************************/
			if (summons.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.AUTHORISED) || summons.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.VERIFIED)) {

				authoriseSummonsDetails(summons);
				//System.err.println("authorising ");
				// create audit trail
				eventAuditDO = createEventAuditRecordVerify(summons);
				eventAuditDO.setEventCode(Constants.EventCode.VERIFY_DOCUMENT);
				
				//removed in response to ROMS1.0-231				
//				if (summons.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.AUTHORISED)) {
//					eventAuditDO.setEventCode(Constants.EventCode.AUTHORIZE_DOCUMENT);
//				} else {
//					eventAuditDO.setEventCode(Constants.EventCode.VERIFY_DETAILS_DOCUMENT);
//				}

			}

			

			/******************* JP Authorising details ***********************************/
			if ((summons.isJPAuthorisationRequested() && !summons.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED))
				&&  (summons.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.AUTHORISED) || summons.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.VERIFIED))){

				authoriseJPDetails(summons);
				// create audit trail
				eventAuditDO = createEventAuditRecordVerify(summons);
			}

			/*	*//******************* PRINTING ***********************************/

			if (summons.getNewStatusId().equals(Constants.DocumentStatus.PRINTED)) {
				generateSummons(summons);
				// create audit trail
				eventAuditDO = createEventAuditRecord(summons);
				eventAuditDO.setEventCode(Constants.EventCode.PRINT_DOCUMENT);

			}
			/******************* REPRINTING ********************************/

			if (summons.getNewStatusId().equals(Constants.DocumentStatus.REPRINTED)) {
				reGenerateSummons(summons);
				// create audit trail
				eventAuditDO = createEventAuditRecord(summons);
				eventAuditDO.setEventCode(Constants.EventCode.REPRINT_DOCUMENT);
			}

			/******************** WITHDRAWN ********************************/

			if (summons.getNewStatusId().equals(Constants.DocumentStatus.WITHDRAWN)) {
				withdrawSummons(summons);
				// create audit trail
				eventAuditDO = createEventAuditRecord(summons);
				eventAuditDO.setEventCode(Constants.EventCode.WITHDRAW_DOCUMENT);

			}

			// save audit
			if (eventAuditDO != null) {
				eventAuditDO.setAuditEntry(new AuditEntry());
				eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
				eventAuditDO.getAuditEntry().setCreateUsername(summons.getCurrentUsername());
				this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			}
			/******************** EDIT ********************************/
			if (summons.getNewStatusId().equals(Constants.DocumentStatus.EDITED)) {

				editSummons(summons);
				/*
				 * // create audit trail eventAuditDO =
				 * createEventAuditRecord(summons);
				 */
			}
			/****************** END **************************************/

			return summons;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
		// return false;
	}

	/***********************
	 * HELPER CLASSES
	 * 
	 * @throws ErrorSavingException
	 **********************************/

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SummonsDischargeAndReleaseFormView generateSummonsDischargeForm(SummonsBO summonsBO) throws ErrorSavingException {

		SummonsDischargeAndReleaseFormView dischargeFormView = null;
		try {

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().load(SummonsDO.class, summonsBO.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");

			if (summonsDO != null) {
				// get the status
				/*
				 * StatusDO status = this.daoFactory.getSummonsDAO().find(
				 * StatusDO.class,
				 * Constants.DocumentStatus.DISCHARGED_FORM_PRINTED); // set the
				 * status if (status != null) summonsDO.setStatus(status); else
				 * throw new InvalidFieldException("Status Id is invalid.");
				 */
				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(summonsBO.getCurrentUsername());

				// update
				daoFactory.getSummonsDAO().update(summonsDO);
				// update the summons record
				summonsBO = new SummonsBO(summonsDO);
			}

			/***************** Populate Discharge View For Print *************************/
			// cancel trial

			dischargeFormView = new SummonsDischargeAndReleaseFormView(summonsDO);

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return dischargeFormView;

	}

	/**
	 * 
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SummonsView reGenerateSummons(SummonsBO summons) throws ErrorSavingException {

		SummonsView summonsView = null;
		try {

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, summons.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");
			/*
			 * // get the status StatusDO status =
			 * this.daoFactory.getSummonsDAO().find( StatusDO.class,
			 * summons.getNewStatusId()); // set the stsatus if (status != null)
			 * summonsDO.setStatus(status); else throw new
			 * InvalidFieldException("Status Id is invalid.");
			 */
			/*
			 * ReasonDO reason = this.daoFactory.getSummonsDAO().find(
			 * ReasonDO.class, summons.getReasonCode()); // set the stsatus if
			 * (reason != null) summonsDO.setReason(reason); else throw new
			 * InvalidFieldException("Reason is invalid.");
			 */
			if (summonsDO != null) {
				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());
				summonsDO.setReprintDtime(Calendar.getInstance().getTime());
				summonsDO.setReprintUsername(summons.getCurrentUsername());
				summonsDO.setReprintComment(summons.getReprintComment());
				summonsDO.setComment(summons.getComment());
				// set the print count
				if (summonsDO.getPrintCount() != null) {
					summonsDO.setPrintCount(summonsDO.getPrintCount().intValue() + 1);

					if (summonsDO.getPrintCount() >= 1 && summons.getReprintReasonCode() != null) {
						// get the status
						ReasonDO reprintReason = this.daoFactory.getSummonsDAO().find(ReasonDO.class, summons.getReprintReasonCode());
						// set the status
						if (reprintReason != null)
							summonsDO.setReprintReason(reprintReason);
						else
							throw new InvalidFieldException("Reason is invalid.");
					}

				} else
					summonsDO.setPrintCount(1);

				// update
				daoFactory.getSummonsDAO().update(summonsDO);
				// update the summons record
				summons = new SummonsBO(summonsDO);
			}

			/***************** Populate Summons View For Print *************************/
			// cancel trial
			CourtAppearanceDO courtAppearanceForSummons = serviceFactory.getCourtAppearanceService().getFirstCourtAppearanceBySummonsID(summons.getAutomaticSerialNo());

			summonsView = new SummonsView(summonsDO, courtAppearanceForSummons);

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return summonsView;

	}

	/**
	 * 
	 * @param summons
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SummonsView generateSummons(SummonsBO summons) {

		SummonsView summonsView = null;

		try {

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, summons.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");

			// get the status
			StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, DocumentStatus.PRINTED);
			// set the status
			if (status != null)
				summonsDO.setStatus(status);
			else
				throw new InvalidFieldException("Status Id is invalid.");

			if (summonsDO != null) {

				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());
				summonsDO.setPrintDtime(Calendar.getInstance().getTime());
				summonsDO.setPrintUsername(summons.getCurrentUsername());

				// set the print count
				if (summonsDO.getPrintCount() != null) {
					summonsDO.setPrintCount(summonsDO.getPrintCount().intValue() + 1);

				} else {
					summonsDO.setPrintCount(1);
				}
				// update
				daoFactory.getSummonsDAO().update(summonsDO);
				// update the summons record
				summons = new SummonsBO(summonsDO);
			}

			/***************** Populate Summons View For Print *************************/
			// cancel trial
			CourtAppearanceDO courtAppearanceForSummons = serviceFactory.getCourtAppearanceService().getFirstCourtAppearanceBySummonsID(summons.getAutomaticSerialNo());

			summonsView = new SummonsView(summonsDO, courtAppearanceForSummons);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return summonsView;

	}

	/**
	 * 
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 */
	public SummonsBO authoriseSummonsDetails(SummonsBO summons) throws ErrorSavingException {

		try {

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, summons.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");
			/*
			 * // get the status StatusDO status =
			 * this.daoFactory.getSummonsDAO().find( StatusDO.class,
			 * Constants.DocumentStatus.AUTHORISED); // set the status if
			 * (status != null) summonsDO.setStatus(status); else throw new
			 * InvalidFieldException("Status Id is invalid.");
			 */
			if (summonsDO != null) {

				// update details
				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());

				// authorisation details
				if (StringUtils.isNotBlank(summons.getAuthorizedFlag()) && summons.getAuthorizedFlag().equalsIgnoreCase(YesNo.NO_LABEL_STR)
					&& StringUtils.isNotBlank(summons.getVerificationRequired()) && summons.getVerificationRequired().equalsIgnoreCase(YesNo.YES_LABEL_STR)) {
					summonsDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG + "");
					summonsDO.setAuthorizeDtime(Calendar.getInstance().getTime());
					summonsDO.setVerificationReq(Constants.YesNo.NO_LABEL_STR + "");
					summonsDO.setAuthorizeUsername(summons.getCurrentUsername().toUpperCase());
				} else

				if (StringUtils.isNotBlank(summons.getAuthorizedFlag()) && summons.getAuthorizedFlag().equalsIgnoreCase(YesNo.NO_LABEL_STR)) {
					summonsDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG + "");
					summonsDO.setAuthorizeDtime(Calendar.getInstance().getTime());
					summonsDO.setAuthorizeUsername(summons.getCurrentUsername().toUpperCase());
				} else if (StringUtils.isNotBlank(summons.getVerificationRequired()) && summons.getVerificationRequired().equalsIgnoreCase(YesNo.YES_LABEL_STR)) {
					summonsDO.setVerificationReq(Constants.YesNo.NO_LABEL_STR + "");
				}

				summonsDO.setComment(summons.getComment());

				// update
				daoFactory.getSummonsDAO().update(summonsDO);

				// update the summons record
				summons = new SummonsBO(summonsDO);

			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return summons;

	}

	/**
	 * 
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 */
	public SummonsBO denyAuthorisationSummonsDetails(SummonsBO summons) throws ErrorSavingException {

		try {

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, summons.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");

			if (summonsDO != null) {

				// update details
				//summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				//summonsDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());
				
				summonsDO.setComment(summons.getComment());
				
				// authorisation details
				// summonsDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG + "");
				/* summonsDO.setVerificationReq(Constants.YesNo.NO_FLAG + ""); */
				// summonsDO.setAuthorizeDtime(Calendar.getInstance().getTime());
				// summonsDO.setAuthorizeUsername(summons.getCurrentUsername());

				// update
				daoFactory.getSummonsDAO().update(summonsDO);

				// update the summons record
				summons = new SummonsBO(summonsDO);

			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return summons;

	}

	/**
	 * 
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 */
	public SummonsBO authoriseJPDetails(SummonsBO summons) throws ErrorSavingException {

		try {

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, summons.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");
			// get the status
			/*
			 * StatusDO status = this.daoFactory.getSummonsDAO().find(
			 * StatusDO.class, Constants.DocumentStatus.AUTHORISED); // set the
			 * stsatus if (status != null) summonsDO.setStatus(status); else
			 * throw new InvalidFieldException("Status Id is invalid.");
			 */

			if (summonsDO != null) {

				// update details
				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());

				summonsDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG + "");
				/* summonsDO.setVerificationReq(Constants.YesNo.NO_FLAG + ""); */
				summonsDO.setAuthorizeDtime(Calendar.getInstance().getTime());
				summonsDO.setAuthorizeUsername(summons.getCurrentUsername().toUpperCase());

				// get jp
				/*
				 * if (summons.getJpRegNumber() != null) { // get the status
				 * JPDO justiceOfPeace = this.daoFactory.getSummonsDAO().find(
				 * JPDO.class, summons.getJpRegNumber()); // set the jp if
				 * (justiceOfPeace != null)
				 * summonsDO.setJusticeOfPeace(justiceOfPeace); else throw new
				 * InvalidFieldException( "Justice Of Peace Id is invalid."); }
				 */

				// update
				daoFactory.getSummonsDAO().update(summonsDO);

				// update the summons record
				summons = new SummonsBO(summonsDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return summons;

	}

	/**
	 * 
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 */
	public SummonsBO serveSummons(SummonsBO summons) throws ErrorSavingException {

		try {

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, summons.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");
			// get the status
			StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, summons.getNewStatusId());
			// set the stsatus
			if (status != null)
				summonsDO.setStatus(status);
			else
				throw new InvalidFieldException("Status Id is invalid.");

			if (summonsDO != null) {

				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());
				summonsDO.setServedOnDate(summons.getServedOnDate());

				// get the staff object
				TAStaffDO staff = this.daoFactory.getSummonsDAO().find(TAStaffDO.class, summons.getServedByUserID());
				if (staff != null)
					summonsDO.setServedByTAStaff(staff);
				else
					throw new InvalidFieldException("Staff Id is invalid.");

				if (summons.getServedAtAddressBO() != null)
					summonsDO.setServedAtAddress(summons.getServedAtAddressBO().getAddressLine1() + ", " + summons.getServedAtAddressBO().getAddressLine2());

				summonsDO.setServedAtParish(summons.getServedAtParish());

				// update
				daoFactory.getSummonsDAO().update(summonsDO);

				// update the summons record
				summons = new SummonsBO(summonsDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return summons;

	}

	/**
	 * 
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 */
	public SummonsBO editSummons(SummonsBO summons, Boolean... isFromRoadCheck) throws ErrorSavingException {

		try {
			//String comments = "";
			StringBuffer comments = new StringBuffer();
			
			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, summons.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");

			if (summonsDO != null) {

				if (summons.getJpIdNumber()!=null) {
					// get the status
					JPDO jp = this.daoFactory.getJPDAO().find(JPDO.class,summons.getJpIdNumber());
					// set the status
					if (jp != null) {

						if (summonsDO.getJusticeOfPeace() != null && !summonsDO.getJusticeOfPeace().getRegNumber().equalsIgnoreCase(jp.getRegNumber())) {
							comments.append("JP: " + summonsDO.getJusticeOfPeace().getPerson().getFirstName() + " " + summonsDO.getJusticeOfPeace().getPerson().getLastName() + 
									" replaced by " + jp.getPerson().getFirstName() + " " + jp.getPerson().getLastName());
						}else
						{
							comments.append("JP: " + jp.getPerson().getFirstName() + " " + jp.getPerson().getLastName());
						}

						summonsDO.setJusticeOfPeace(jp);
						
					} else
						throw new InvalidFieldException("Justice of the Peace is invalid.");

				} else {
					if (summonsDO.getJusticeOfPeace() != null) {
						comments.append("JP: " + summonsDO.getJusticeOfPeace().getPerson().getFirstName() + " " + summonsDO.getJusticeOfPeace().getPerson().getLastName() + " deleted");
					}

					// justice of the peace removes
					summonsDO.setJusticeOfPeace(null);
				}
				
				if((StringUtil.isSet(summons.getComment()) && !StringUtil.isSet(summonsDO.getComment())) //if new comment or comment changed 
						|| (StringUtil.isSet(summons.getComment()) && !summons.getComment().equalsIgnoreCase(summonsDO.getComment())) )
								comments.append(StringUtil.appendSemiColon(comments.toString(), "Comment: " + summons.getNewComment()));
					

				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());

				summonsDO.setComment(summons.getComment());

				
				
				if (summons.getOrigin() != null && summons.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL) && summons.getManualSerialNo() != null) {
					if (summonsDO.getManualSerialNumber() == null)
						comments.append(StringUtil.appendSemiColon(comments.toString(), "New Origin: " + DocumentType.ORIGIN_MANUAL));

					summonsDO.setManualSerialNumber(summons.getManualSerialNo());
					
					
					//jreid 2014-06-26
					//ensure when document is changed to manual then it is set to printed
					//set to printed on issue date
					// get the status
					StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, Constants.DocumentStatus.PRINTED);
					if (status != null) {
						summonsDO.setStatus(status);
						summonsDO.setPrintDtime(summonsDO.getOffenceDtime());
						summonsDO.setPrintCount(1);
					}
				}

				if (summons.getOrigin() != null && summons.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_AUTOMATIC)) {
					if (summonsDO.getManualSerialNumber() != null)
						comments.append(StringUtil.appendSemiColon(comments.toString(), "New Origin: " + DocumentType.ORIGIN_AUTOMATIC));
			
				//	summonsDO.setManualSerialNumber(null);
					
					//jreid 2014-06-26
					//ensure when document is changed to automatic then it is set to created
					//set to printed date to null, print count to zero					
					StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, Constants.DocumentStatus.CREATED);
					//System.err.println("summonsDO.getManualSerialNumber() : " + summonsDO.getManualSerialNumber() );
					if (status != null && summonsDO.getManualSerialNumber() != null) {
						summonsDO.setStatus(status);
						summonsDO.setPrintDtime(null);
						summonsDO.setPrintCount(0);
					}

					if (summons.getScannedDocList() != null && !summons.getScannedDocList().isEmpty()) {
						ScannedDocBO doc = summons.getScannedDocList().get(0);
						doc.setCurrentUsername(summons.getCurrentUsername());
						this.serviceFactory.getScannedDocService().deleteManualScannedManualDoc(doc,summons);
					}
					
					summonsDO.setManualSerialNumber(null);

				}
				
				
				/*
				 * if (summons.getOrigin() != null &&
				 * !summonsDO.getOrigin().equalsIgnoreCase(summons.getOrigin()))
				 * { if (summons.getManualSerialNo() != null) comments +=
				 * "Origin Changed to Manual; "; else comments +=
				 * "Origin Changed to Automatic " + "previous Manual Number is "
				 * + summonsDO.getManualSerialNumber() + " ; "; }
				 */
				
				/*For Discrepancy number 126 where a JP is being added from road check a document should not need to be authorised*/
				if(isFromRoadCheck != null && isFromRoadCheck.length > 0 && isFromRoadCheck[0])
				{
					summonsDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG + "");

				}
				else
				{
					summonsDO.setAuthorizedFlag(Constants.YesNo.NO_FLAG + "");
				}
				
				// update
				daoFactory.getSummonsDAO().update(summonsDO);

				// create audit trail
				EventAuditDO eventAuditDO = createEventAuditRecord(summons);
				eventAuditDO.setAuditEntry(new AuditEntry());
				eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
				eventAuditDO.getAuditEntry().setCreateUsername(summons.getCurrentUsername());
				eventAuditDO.setComment(comments.toString());
				this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);

				// update the summons record
				summons = new SummonsBO(summonsDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return summons;

	}

	/**
	 * 
	 * @param summons
	 * @return
	 * @throws ErrorSavingException
	 */
	public SummonsBO cancelSummons(SummonsBO summons) throws ErrorSavingException {

		try {
			// cancel trial
			CourtCaseBO caseToCancel = serviceFactory.getCourtCaseService().getCourtCaseBySummonsId(summons.getAutomaticSerialNo());

			if (caseToCancel == null)
				throw new InvalidFieldException(" No trial exist for this summons Id.");

			List<CourtAppearanceBO> courtAppearancesList = caseToCancel.getCourtAppearances();

			// get the data object
			CourtCaseDO caseToCancelDO = daoFactory.getCourtCaseDAO().find(CourtCaseDO.class, caseToCancel.getCourtCaseId());

			// get the status
			StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, summons.getNewStatusId());

			if (status != null) {
				caseToCancelDO.setStatus(status);
			}

			caseToCancelDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
			caseToCancelDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());
			// caseToCancel.setCaseStatusCode(Constants.Status.COURT_CASE_CANCELLED);

			// save
			daoFactory.getCourtCaseDAO().saveOrUpdate(caseToCancelDO);

			if (courtAppearancesList != null) {
				for (CourtAppearanceBO appearance : courtAppearancesList) {
					CourtAppearanceDO courtAppearanceDO = daoFactory.getCourtAppearanceDAO().find(CourtAppearanceDO.class, appearance.getCourtAppearanceId());

					if (status != null)
						courtAppearanceDO.setStatus(status);

					courtAppearanceDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
					courtAppearanceDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());

					// update
					daoFactory.getCourtCaseDAO().saveOrUpdate(courtAppearanceDO);

				} // end for loop
			}
			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, summons.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");

			// set the status
			if (status != null) {
				summonsDO.setStatus(status);

			} else
				throw new InvalidFieldException("Status Id is invalid.");

			ReasonDO reason = this.daoFactory.getSummonsDAO().find(ReasonDO.class, summons.getReasonCode());

			// set the status
			if (reason != null)
				summonsDO.setReason(reason);
			else
				throw new InvalidFieldException("Reason is invalid.");

			if (summonsDO != null) {

				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());
				summonsDO.setComment(summons.getComment());

				// update
				daoFactory.getSummonsDAO().saveOrUpdate(summonsDO);

				// update the summons record
				summons = new SummonsBO(summonsDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return summons;

	}

	/*******************************
	 * WITH DRAW
	 * 
	 * @throws ErrorSavingException
	 *********************************************************/
	public SummonsBO withdrawSummons(SummonsBO summons) throws ErrorSavingException {
		try {
			if (summons == null)
				throw new InvalidFieldException(" Summons details is required.");

			// cancel trial
			CourtCaseBO caseToCancel = serviceFactory.getCourtCaseService().getCourtCaseBySummonsId(summons.getAutomaticSerialNo());

			if (caseToCancel == null)
				throw new InvalidFieldException(" No trial exist for this summons Id.");

			List<CourtAppearanceBO> courtAppearancesList = caseToCancel.getCourtAppearances();

			// get the data object
			CourtCaseDO caseToCancelDO = daoFactory.getCourtCaseDAO().find(CourtCaseDO.class, caseToCancel.getCourtCaseId());

			if (caseToCancelDO != null) {
				// get the status
				StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, Constants.Status.COURT_CASE_WITHDRAWN);
				// set the stsatus
				if (status != null) {
					caseToCancelDO.setStatus(status);

				} else
					throw new InvalidFieldException("Status Id is invalid.");

				caseToCancelDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				caseToCancelDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());

				// save
				daoFactory.getCourtCaseDAO().update(caseToCancelDO);

				for (CourtAppearanceBO appearance : courtAppearancesList) {
					CourtAppearanceDO courtAppearanceDO = daoFactory.getCourtAppearanceDAO().find(CourtAppearanceDO.class, appearance.getCourtAppearanceId());

					if (courtAppearanceDO != null) {

						courtAppearanceDO.setStatus(status);
						courtAppearanceDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
						courtAppearanceDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());

						// update
						daoFactory.getCourtCaseDAO().update(courtAppearanceDO);
					}

				} // end for loop

			}
			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, summons.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");
			// get the status
			StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, summons.getNewStatusId());
			// set the status
			if (status != null) {
				summonsDO.setStatus(status);

			} else
				throw new InvalidFieldException("Status Id is invalid.");
			String trn = null;

			ReasonDO reason = this.daoFactory.getSummonsDAO().find(ReasonDO.class, summons.getReasonCode());
			// set the status
			if (reason != null)
				summonsDO.setReason(reason);
			else
				throw new InvalidFieldException("Reason is invalid.");

			/****************** UPDATE SUMMONS *********************/
			if (summonsDO != null) {

				/****************** SAVE TEH WITNESS *****************************/

				if (summons.getDischargeWitness() == null)
					throw new InvalidFieldException(" Discharge Witness details is required.");

				if (summons.getDischargeWitness() != null && StringUtils.isNotBlank(summons.getDischargeWitness().getTrnNbr())) {

					trn = summons.getDischargeWitness().getTrnNbr().replaceAll("-", "").trim();
					boolean personExist = daoFactory.getPersonDAO().personExistsByTRN(trn);

					// if the person is there then update them and add the
					// details
					if (personExist) {
						PersonDO personToAdd = daoFactory.getPersonDAO().findPersonByTRN(trn);

						if (personToAdd != null) {
							if (personToAdd.getAddress() != null) {
								if (personToAdd.getAddress().getParish() != null) {
									ParishDO par = daoFactory.getPersonDAO().find(ParishDO.class, summons.getDischargeWitness().getParishCode());
									if (par != null) {
										personToAdd.getAddress().setParish(par);
									}
								} else {
									ParishDO par = daoFactory.getPersonDAO().find(ParishDO.class, summons.getDischargeWitness().getParishCode());
									if (par != null) {
										personToAdd.getAddress().setParish(par);
										// personToAdd.getAddress().getParish().setParishCode(summons.getDischargeWitness().getParishCode());
									}
								}
								personToAdd.getAddress().setMarkText(summons.getDischargeWitness().getMarkText());
								personToAdd.getAddress().setPoBoxNo(summons.getDischargeWitness().getPoBoxNo());
								personToAdd.getAddress().setPoLocationName(summons.getDischargeWitness().getPoLocationName());
								personToAdd.getAddress().setStreetName(summons.getDischargeWitness().getStreetName());
								personToAdd.getAddress().setStreetNo(summons.getDischargeWitness().getStreetNo());
							} else {
								personToAdd.setAddress(new AddressDO());
								if (personToAdd.getAddress().getParish() != null) {
									ParishDO par = daoFactory.getPersonDAO().find(ParishDO.class, summons.getDischargeWitness().getParishCode());
									if (par != null) {
										personToAdd.getAddress().setParish(par);
									}
								} else {
									ParishDO par = daoFactory.getPersonDAO().find(ParishDO.class, summons.getDischargeWitness().getParishCode());
									if (par != null) {
										personToAdd.getAddress().setParish(par);
										// personToAdd.getAddress().getParish().setParishCode(summons.getDischargeWitness().getParishCode());
									}
								}
								personToAdd.getAddress().setMarkText(summons.getDischargeWitness().getMarkText());
								personToAdd.getAddress().setPoBoxNo(summons.getDischargeWitness().getPoBoxNo());
								personToAdd.getAddress().setPoLocationName(summons.getDischargeWitness().getPoLocationName());
								personToAdd.getAddress().setStreetName(summons.getDischargeWitness().getStreetName());
								personToAdd.getAddress().setStreetNo(summons.getDischargeWitness().getStreetNo());

							}
							personToAdd.setOccupation(summons.getDischargeWitness().getOccupation());
							personToAdd.setTelephoneCell(summons.getDischargeWitness().getTelephoneCell());
							personToAdd.setTelephoneHome(summons.getDischargeWitness().getTelephoneHome());
							personToAdd.setTelephoneWork(summons.getDischargeWitness().getTelephoneWork());
							personToAdd.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
							personToAdd.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());

							/*
							 * if
							 * (StringUtils.isNotBlank(summons.getDischargeWitness
							 * ().getTelephoneCell()))
							 * personToAdd.setTelephoneCell
							 * (summons.getDischargeWitness
							 * ().getTelephoneCell().replace("_",
							 * "").replace("-", "")); if
							 * (StringUtils.isNotBlank(
							 * summons.getDischargeWitness
							 * ().getTelephoneHome()))
							 * personToAdd.setTelephoneHome
							 * (summons.getDischargeWitness
							 * ().getTelephoneHome().replace("_",
							 * "").replace("-", "")); if
							 * (StringUtils.isNotBlank(
							 * summons.getDischargeWitness
							 * ().getTelephoneWork()))
							 * personToAdd.setTelephoneWork
							 * (summons.getDischargeWitness
							 * ().getTelephoneWork().replace("_",
							 * "").replace("-", ""));
							 */
							// update the person details in ROMS
							daoFactory.getPersonDAO().saveOrUpdate(personToAdd);

							summonsDO.setDischargeWitness(personToAdd);
							// System.err.println(" updated the person details");
						} else {
							throw new InvalidFieldException("Witness Details is invalid.");
						}
					} else {
						summons.getDischargeWitness().setCurrentUserName(summons.getCurrentUsername());
						summons.getDischargeWitness().setTrnNbr(trn);
						Integer personId = daoFactory.getPersonDAO().savePerson(summons.getDischargeWitness());
						//System.err.println("Saved Person ID " + personId);
						PersonDO personToAdd = daoFactory.getPersonDAO().findPersonById(personId);

						if (personToAdd != null) {
							summonsDO.setDischargeWitness(personToAdd);
						}

						// System.err.println(" saved the person details");

					}
				}
				/***************** END ADD THE WITNESS *******************************/

				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());

				// update
				daoFactory.getSummonsDAO().update(summonsDO);

				// update the summons record
				summons = new SummonsBO(summonsDO);

			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return summons;

	}

	/******************************************* CLOSE ***********************************************/
	public SummonsBO closeSummons(SummonsBO summons) {
		try {
			// cancel trial
			CourtCaseBO caseToCancel = serviceFactory.getCourtCaseService().getCourtCaseBySummonsId(summons.getAutomaticSerialNo());

			if (caseToCancel == null)
				throw new InvalidFieldException(" No trial exist for this summons Id.");

			List<CourtAppearanceBO> courtAppearancesList = caseToCancel.getCourtAppearances();

			// get the data object
			CourtCaseDO caseToCancelDO = daoFactory.getCourtCaseDAO().load(CourtCaseDO.class, caseToCancel.getCourtCaseId());
			caseToCancelDO.getStatus().setStatusId(Constants.Status.COURT_CASE_CLOSED);
			caseToCancelDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
			caseToCancelDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());
			// caseToCancel.setCaseStatusCode(Constants.Status.COURT_CASE_CANCELLED);
			// save
			daoFactory.getCourtCaseDAO().update(caseToCancelDO);

			for (CourtAppearanceBO appearance : courtAppearancesList) {
				CourtAppearanceDO courtAppearanceDO = daoFactory.getCourtAppearanceDAO().load(CourtAppearanceDO.class, appearance.getCourtAppearanceId());
				courtAppearanceDO.getStatus().setStatusId(Constants.Status.COURT_CASE_CLOSED);
				courtAppearanceDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				courtAppearanceDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());

				// update
				daoFactory.getCourtCaseDAO().update(courtAppearanceDO);

			} // end for loop

			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, summons.getAutomaticSerialNo());

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");
			// get the status
			StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, summons.getNewStatusId());
			// set the stsatus
			if (status != null) {
				summonsDO.setStatus(status);

			} else
				throw new InvalidFieldException("Status Id is invalid.");

			if (summonsDO != null) {

				summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				summonsDO.getAuditEntry().setUpdateUsername(summons.getCurrentUsername());

				// update
				daoFactory.getSummonsDAO().update(summonsDO);

				// update the summons record
				summons = new SummonsBO(summonsDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return summons;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * fsl.ta.toms.roms.service.SummonsService#lookupSummons(fsl.ta.toms.roms
	 * .search.criteria.impl.SummonsCriteriaBO)
	 */
	@Override
	@Transactional(readOnly = true)
	public ArrayList<DocumentViewBO> lookupSummons(DocumentsCriteriaBO criteria) {
		ArrayList<DocumentViewBO> results = null;
		try {
			results = this.daoFactory.getSummonsDAO().lookupSummons(criteria);
			// add the scanned documents
			/*
			 * for (SummonsBO summon : results) { CourtCaseBO courtCase =
			 * serviceFactory.getCourtCaseService()
			 * .getCourtCaseBySummonsId(summon.getAutomaticSerialNo());
			 * System.err.println(" court case : " + courtCase + "/" +
			 * summon.getAutomaticSerialNo()); summon.setCourtCase(courtCase); }
			 */
			return results;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	@Transactional
	public void processScannedSummonsDocuments(List<ScannedDocBO> summonsDocList) {

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		if (summonsDocList == null || summonsDocList.isEmpty())
			return;

		SummonsDAO summonsDAO = this.daoFactory.getSummonsDAO();

		// update summons object with this update time and user just for
		// reference sake
		// SummonsDO summonsTosave = null;

		// iterate through scanned document list
		for (ScannedDocBO scannedDoc : summonsDocList) {

			if (scannedDoc.getScannedDocId() == null) {
				ScannedDocDO scannedDocToSave = new ScannedDocDO(scannedDoc);
				AuditEntry auditEntry = new AuditEntry();
				auditEntry.setCreateDTime(currentDate);
				auditEntry.setCreateUsername(scannedDoc.getCurrentUsername());
				scannedDocToSave.setAuditEntry(auditEntry);

				// save object
				summonsDAO.saveOrUpdate(scannedDocToSave);

			} else {
				ScannedDocDO scannedDocToSave = new ScannedDocDO(scannedDoc);
				AuditEntry auditEntry = new AuditEntry();
				auditEntry.setUpdateDTime(currentDate);
				auditEntry.setUpdateUsername(scannedDoc.getCurrentUsername());
				scannedDocToSave.setAuditEntry(auditEntry);

				// save object
				summonsDAO.saveOrUpdate(scannedDocToSave);
			}

		}

	}

	/**
	 * 
	 * @param summon
	 * @return
	 */
	private EventAuditDO createEventAuditRecord(SummonsBO summon) {

		EventAuditDO eventAuditBO = new EventAuditDO();
		StringBuffer comment = new StringBuffer();

		if (summon.getAutomaticSerialNo() == null) {

			eventAuditBO.setEventCode(Constants.EventCode.CREATE_DOCUMENT);
			eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.CREATE_DOCUMENT));
		} else {
			eventAuditBO.setEventCode(Constants.EventCode.UPDATE_DOCUMENT);
			eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.UPDATE_DOCUMENT));
		}

		
		
		
		if(summon.getNewStatusId() != null && summon.getNewStatusId().equals(Constants.DocumentStatus.WITHDRAWN))
		{
			eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.SUMMONS_ID);
			eventAuditBO.setRefValue2(summon.getReferenceNo() + "");
		}else
		{
			eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.DOCUMENT_TYPE);
			eventAuditBO.setRefValue1(summon.getDocumentTypeName());
				
			eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_NUMBER);
			eventAuditBO.setRefValue2(summon.getReferenceNo() + "");
		}
		
		
		if (summon.getNewStatusId() != null && summon.getNewStatusId().equals(Constants.DocumentStatus.REPRINTED) ||
				summon.getNewStatusId() != null && summon.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED)) 
		{
			
			Integer reason = summon.getReprintReasonCode();
			ReasonDO reasonDO = new ReasonDO();
			
			if(reason != null)
			{	
				reasonDO = daoFactory.getReasonDAO().find(ReasonDO.class, reason);
				
				if(reason != null)
					comment.append(StringUtil.appendSemiColon(comment.toString(),"Reason: " + reasonDO.getDescription()));
			}
			
			if(StringUtil.isSet(summon.getReprintComment()))
				comment.append(StringUtil.appendSemiColon(comment.toString(),"Comment: " + summon.getReprintComment()));
		}
		
		
		if (summon.getNewStatusId() != null && summon.getNewStatusId().equals(Constants.DocumentStatus.WITHDRAWN)) 
		{
			
			Integer reason = summon.getReasonCode();
			ReasonDO reasonDO = new ReasonDO();
			
			if(reason != null)
			{	
				reasonDO = daoFactory.getReasonDAO().find(ReasonDO.class, reason);
				
				if(reason != null)
					comment.append(StringUtil.appendSemiColon(comment.toString(),"Reason: " + reasonDO.getDescription()));
			}
			
			if(StringUtil.isSet(summon.getComment()))
				comment.append(StringUtil.appendSemiColon(comment.toString(),"Comment: " + summon.getNewComment()));
		}
		
		
		
		if ((summon.getNewStatusId() != null && summon.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED)) ||
				summon.getNewStatusId()== null ) //null newStatusID indicates a newly created document 
		{
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Offender Name: " + summon.getOffenderFullName()));
			
			String origin = "";
			if(StringUtils.isNotBlank(summon.getManualSerialNo())){
				origin = DocumentType.ORIGIN_MANUAL;
			}else
			{
				origin = DocumentType.ORIGIN_AUTOMATIC;
			}
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Document Origin: " + origin));
		}
		
		
		

		if (summon.getNewStatusId() != null && summon.getNewStatusId().equals(Constants.DocumentStatus.SERVED)) 
		{
			StaffUserMappingCriteriaBO crit = new StaffUserMappingCriteriaBO();
			crit.setStaffId(summon.getServedByUserID());
			
			List<StaffUserMappingBO> tastaff = this.daoFactory.getStaffUserMappingDAO().lookupTAStaff(crit);
			String firstN = "";
			String lastN = "";
			
			if (tastaff != null && tastaff.size()>0)
			{
				StaffUserMappingBO staff = tastaff.get(0);
				
				firstN = staff.getFirstName();
				lastN = staff.getLastName();
			}
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Served by TA Staff: " + firstN + " " + lastN));
			
			
			String servedDate = "";
			
			if(summon.getServedOnDate() != null)
				servedDate = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  summon.getServedOnDate());
			
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Served Date: " + servedDate));
		}
		
		
		//included JP details as part of the Print Document event
		//(coming out of discussions on Sept 7, 2015 relating to fixing issues logged to freedcamp)
		if (summon.getNewStatusId()!= null && summon.getNewStatusId().equals(Constants.DocumentStatus.PRINTED))
		{
			DocumentsCriteriaBO docCrit = new DocumentsCriteriaBO();
			docCrit.setReferenceNo(summon.getReferenceNo());
			
			ArrayList<DocumentViewBO> savedSummonsList = this.daoFactory.getSummonsDAO().lookupSummons(docCrit);
			DocumentViewBO savedSummons = new DocumentViewBO();
			
			if(savedSummonsList != null && savedSummonsList.size()> 0)
			{
				savedSummons = savedSummonsList.get(0);
			}
			
			//only if the jp details are not yet saved on the summons record in the database at the time of printing
			//and the value is being sent over on the summonsbo object 
			if(savedSummons != null && summon.getJpIdNumber()!= null && savedSummons.getJpIdNumber() == null)
			{
				JPDO jp =  this.daoFactory.getSummonsDAO().find(JPDO.class, summon.getJpIdNumber());
				comment.append(jp!= null? jp.getPerson()!= null? 
							StringUtil.appendSemiColon(comment.toString(),"J.P. Added: " + jp.getPerson().getFirstName() + " " + jp.getPerson().getLastName())
							:"": "");
			}
		} 
		
		eventAuditBO.setComment(comment.toString());
	
		eventAuditBO.setAuditEntry(new AuditEntry());

		return eventAuditBO;
	}

	/**
	 * 
	 * @param summon
	 * @return
	 */
	private EventAuditDO createEventAuditRecordJPDetails(SummonsBO summon) {

		EventAuditDO eventAuditBO = new EventAuditDO();

		eventAuditBO.setEventCode(Constants.EventCode.UPDATE_JP_PIN);
		eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.UPDATE_JP_PIN));

		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.SUMMONS);
		eventAuditBO.setRefValue1(summon.getReferenceNo() + "");

		eventAuditBO.setAuditEntry(new AuditEntry());

		return eventAuditBO;
	}

	/**
	 * 
	 * @param summon
	 * @return
	 */
	private EventAuditDO createEventAuditRecordVerify(SummonsBO summon) {

		EventAuditDO eventAuditBO = new EventAuditDO();

		eventAuditBO.setEventCode(Constants.EventCode.VERIFY_DOCUMENT);
		eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.VERIFY_DOCUMENT));

		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.DOCUMENT_TYPE);
		eventAuditBO.setRefValue1(summon.getDocumentTypeName());
			
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_NUMBER);
		eventAuditBO.setRefValue2(summon.getReferenceNo() + "");
		
		if(StringUtil.isSet(summon.getComment()))
			eventAuditBO.setComment("Comment: " + summon.getNewComment()); 
		
		eventAuditBO.setAuditEntry(new AuditEntry());

		return eventAuditBO;
	}

	private EventAuditDO createEventAuditRecordDenyVerify(SummonsBO summon) {

		EventAuditDO eventAuditBO = new EventAuditDO();

		eventAuditBO.setEventCode(Constants.EventCode.REJECT_DOCUMENT);
		eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.REJECT_DOCUMENT));

		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.DOCUMENT_TYPE);
		eventAuditBO.setRefValue1(summon.getDocumentTypeName());
			
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_NUMBER);
		eventAuditBO.setRefValue2(summon.getReferenceNo() + "");

		eventAuditBO.setAuditEntry(new AuditEntry());
		
		if(StringUtil.isSet(summon.getComment()))
			eventAuditBO.setComment("Comment: " + summon.getNewComment());

		return eventAuditBO;
	}

	@Override
	@Transactional(readOnly = true)
	public SummonsBO getSummonsDetailsById(Integer summonsId) {

		if (summonsId == null)
			return null;

		SummonsBO summonsBO = this.daoFactory.getSummonsDAO().lookupSummonsById(summonsId);

		try {
			if (summonsBO != null) {
				/** Get Court Case Details */
				CourtCaseBO courtCase = null;

				courtCase = serviceFactory.getCourtCaseService().getCourtCaseBySummonsId(summonsBO.getAutomaticSerialNo());

				summonsBO.setCourtCase(courtCase);
				
				if( summonsBO.getJpIdNumber()!=null){
					JPDO jpdo = daoFactory.getJPDAO().find(JPDO.class, summonsBO.getJpIdNumber());
				
					summonsBO.setJpRegNumber(jpdo.getRegNumber());
				}
			}
		} catch (RequiredFieldMissingException e) {

			e.printStackTrace();
		}

		return summonsBO;

	}

	/**
	 * If road check from road, is handheld is true and verification is not required
	 * else verification is required and change is in house
	 * @param summonsIds
	 * @param jpRegNo
	 * @param isHandHeld
	 * @throws ErrorSavingException
	 * @throws InvalidFieldException
	 * @throws RequiredFieldMissingException 
	 */
	@Override
	public void addJPDetailsFromRoadCheck(List<Integer> summonsIds, Integer jpIdNo, boolean isHandHeld, String updateUser) throws ErrorSavingException, InvalidFieldException, RequiredFieldMissingException {

		if (updateUser == null || StringUtils.isBlank(updateUser))
			throw new RequiredFieldMissingException("Update user is required.");
		
		if (jpIdNo == null)
			throw new RequiredFieldMissingException("Justice of the Peace is required.");
		
		if (summonsIds == null || summonsIds.isEmpty())
			throw new RequiredFieldMissingException("Summonses for update are required.");
		
		
		//iterate through list 		
		for (Integer serialNo : summonsIds) {
			
			/***************** UPDATE THE SUMMONS **********************/
			SummonsDO summonsDO = daoFactory.getSummonsDAO().find(SummonsDO.class, serialNo);

			if (summonsDO == null)
				throw new ErrorSavingException("Summons with this ID does not exist.");	
			
			/***************** GET JP DETAIL ****************************/
			JPDO jp = this.daoFactory.getJPDAO().find(JPDO.class, jpIdNo);
			// set the operation object
			if (jp != null)
				summonsDO.setJusticeOfPeace(jp);
			else
				throw new InvalidFieldException("JP Id is invalid.");

			//if isHandHeld is false, thus in house then do below
			//This is no longer the case as road check will not forcing authorization based on the adding a JP
			//O.Anguin - 25 Sept 2014 Based on changes for discrepancy 126
			//if(!isHandHeld){
			//	summonsDO.setAuthorizedFlag(Constants.YesNo.NO_FLAG + "");
			//}
			
			//add update user info
			summonsDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
			summonsDO.getAuditEntry().setUpdateUsername(updateUser);
			
			// update summons
			daoFactory.getSummonsDAO().update(summonsDO);

			/**************** AUDIT ********************************/
			// update the summons record
			SummonsBO summonsBO = new SummonsBO(summonsDO);

			// create audit trail
//			EventAuditDO eventAuditDO = createEventAuditRecordJPDetails(summonsBO);
//			eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
//			eventAuditDO.getAuditEntry().setCreateUsername(summonsDO.getAuditEntry().getCreateUsername());
//			this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			//the JP details will be captured on the Print Documents event instead
			//(coming out of discussions on Sept 7, 2015 relating to fixing issues logged to freedcamp)

		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public SummonsView getSummonsView(SummonsBO summonsBO) {
		SummonsView summonsView = null;
		try{
			if (summonsBO == null)
				return null;
	
			SummonsDO summonsDO = daoFactory.getSummonsDAO().load(SummonsDO.class, summonsBO.getAutomaticSerialNo());
	
			if (summonsDO == null)
				return null;
			
			CourtAppearanceDO courtAppearanceForSummons = serviceFactory.getCourtAppearanceService().getFirstCourtAppearanceBySummonsID(summonsBO.getAutomaticSerialNo());

			summonsView = new SummonsView(summonsDO, courtAppearanceForSummons);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return summonsView;

	}
	
	
	

}
