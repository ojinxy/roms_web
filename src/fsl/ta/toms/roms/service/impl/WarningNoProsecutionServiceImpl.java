/**
 * Created By: jreid
 * Date: Apr 30, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.ScannedDocBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.DocumentStatus;
import fsl.ta.toms.roms.constants.Constants.DocumentType;
import fsl.ta.toms.roms.constants.Constants.YesNo;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.WarningNoProsecutionDAO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.RoadCheckOffenceOutcomeDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.documents.view.NoticeView;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.WarningNoProsecutionService;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin Created Date: August 08, 2013
 */
public class WarningNoProsecutionServiceImpl implements WarningNoProsecutionService {
	private DAOFactory	daoFactory;

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Autowired
	ServiceFactory	serviceFactory;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer saveWarningNoProsecution(WarningNoProsecutionBO warningNoProsecution) throws ErrorSavingException {

		WarningNoProsecutionDO warningNoProsecutionTosave = new WarningNoProsecutionDO(warningNoProsecution);

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		try {
			AuditEntry auditEntry = new AuditEntry();

			// eventAuditBO.set)(new
			// Date(warningNoProsecution.getIssueDate().getTime()));
			auditEntry.setCreateUsername(warningNoProsecution.getCurrentUsername());
			auditEntry.setCreateDTime(currentDate);
			warningNoProsecutionTosave.setAuditEntry(auditEntry);

			// set the road operation outcome
			RoadCheckOffenceOutcomeDO roadCheckOffenceOutcomeDO = this.daoFactory.getWarningNoProsecutionDAO().find(RoadCheckOffenceOutcomeDO.class,
				warningNoProsecution.getRoadCheckOffenceOutcomeCode());
			if (roadCheckOffenceOutcomeDO != null)
				warningNoProsecutionTosave.setRoadCheckOffenceOutcome(roadCheckOffenceOutcomeDO);

			// get the offender object
			PersonDO offender = this.daoFactory.getWarningNoProsecutionDAO().find(PersonDO.class, warningNoProsecution.getOffenderId());
			// set the operation object
			if (offender != null)
				warningNoProsecutionTosave.setOffender(offender);

			// get the staff object
			TAStaffDO staff = this.daoFactory.getWarningNoProsecutionDAO().find(TAStaffDO.class, warningNoProsecution.getTaStaffId());

			// set the operation object
			if (staff != null)
				warningNoProsecutionTosave.setTaStaff(staff);

			// set the status
			StatusDO status = null;
			// If the manual serial number is not null, set status to printed
			if (StringUtils.isNotBlank(warningNoProsecution.getManualSerialNo())) {
				// set the status
				status = this.daoFactory.getWarningNoProsecutionDAO().find(StatusDO.class, Constants.DocumentStatus.PRINTED);
			} else {
				// set the status
				status = this.daoFactory.getWarningNoProsecutionDAO().find(StatusDO.class, Constants.DocumentStatus.CREATED);

			}
			// set the operation object
			if (status != null)
				warningNoProsecutionTosave.setStatus(status);

			if (!StringUtils.isEmpty(warningNoProsecution.getVerificationRequired()))
				warningNoProsecutionTosave.setVerificationReq(warningNoProsecution.getVerificationRequired());

			// save the warningNoProsecution
			// warningNoProsecutionTosave.setIssueDate(currentDate);

			Integer warningNoProsecutionId = (Integer) this.daoFactory.getWarningNoProsecutionDAO().save(warningNoProsecutionTosave);

			/************ SAVE SCANNED DOCS ***********/
			if (warningNoProsecution.getScannedDocList() != null && !warningNoProsecution.getScannedDocList().isEmpty()) {
				// List<WarningNoProsecutionScannedDocBO>
				// warningNoProsecutionDocList =
				// warningNoProsecution.getScannedDocList().get(0).setWarningNoProsecutionId(warningNoProsecutionId);
				processScannedWarningNoProsecutionDocuments(warningNoProsecution.getScannedDocList());

			}

			WarningNoProsecutionDO warningNoProsecutionSaved = this.daoFactory.getWarningNoProsecutionDAO().find(WarningNoProsecutionDO.class, warningNoProsecutionId);
			
			/************ END SAVE ************/
			// set the id
			warningNoProsecution.setAutomaticSerialNo(warningNoProsecutionId);
			warningNoProsecution.setReferenceNo(Constants.formatRefSeqNo(warningNoProsecutionSaved.getReferenceNo()));

			// save audit
			EventAuditDO eventAuditDO = createEventAuditRecord(warningNoProsecution);
			eventAuditDO.setEventCode(Constants.EventCode.CREATE_DOCUMENT);
			eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
			eventAuditDO.getAuditEntry().setCreateUsername(warningNoProsecutionTosave.getAuditEntry().getCreateUsername());
			this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);

			return warningNoProsecutionId;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException();
		}
		// return null;
	}

	@Override
	@Transactional
	public WarningNoProsecutionBO updateWarningNoProsecution(WarningNoProsecutionBO warningNoProsecution) throws ErrorSavingException {
		if (warningNoProsecution == null)
			throw new ErrorSavingException("Warning Notice details cannot be empty");

		EventAuditDO eventAuditDO = null;

		try {
			/************************ CANCELLATION *********************/
			if (warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED)) {

				cancelWarningNoProsecution(warningNoProsecution);

				eventAuditDO = createEventAuditRecord(warningNoProsecution);
				eventAuditDO.setEventCode(Constants.EventCode.CANCEL_DOCUMENT);
			}

			/******************* SERVING ***********************************/
			if (warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.SERVED)) {

				serveWarningNoProsecution(warningNoProsecution);

				eventAuditDO = createEventAuditRecord(warningNoProsecution);
				eventAuditDO.setEventCode(Constants.EventCode.SERVE_DOCUMENT);
			}

			/******************* PRINTING ***********************************/

			if (warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.PRINTED)) {
				generateWarningNoProsecution(warningNoProsecution);

				eventAuditDO = createEventAuditRecord(warningNoProsecution);
				eventAuditDO.setEventCode(Constants.EventCode.PRINT_DOCUMENT);
			}
			/******************* REPRINTING ********************************/

			if (warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.REPRINTED)) {
				reGenerateWarningNoProsecution(warningNoProsecution);

				eventAuditDO = createEventAuditRecord(warningNoProsecution);
				eventAuditDO.setEventCode(Constants.EventCode.REPRINT_DOCUMENT);
			}

			/******************* Authorising details ***********************************/
			if (warningNoProsecution.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.AUTHORISED)
				|| warningNoProsecution.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.VERIFIED)) {

				authoriseWarningNoProsecutionDetails(warningNoProsecution);

				eventAuditDO = createEventAuditRecordVerify(warningNoProsecution);
				eventAuditDO.setEventCode(Constants.EventCode.VERIFY_DOCUMENT);
		
				//removed in response to ROMS1.0-231
//				if (warningNoProsecution.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.AUTHORISED)) {
//					eventAuditDO.setEventCode(Constants.EventCode.AUTHORIZE_DOCUMENT);
//				} else {
//					eventAuditDO.setEventCode(Constants.EventCode.VERIFY_DETAILS_DOCUMENT);
//				}
			}

			/******************* Not Authorising details ***********************************/
			if (warningNoProsecution.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.DENY_AUTHORISATION)) {

				denyAuthorisationWarningNoProsecutionDetails(warningNoProsecution);

				// create audit trail
				eventAuditDO = createEventAuditRecordDenyVerify(warningNoProsecution);
			}

			/******************** EDIT ********************************/

			if (warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.EDITED)) {

				editWarningNoProsecution(warningNoProsecution);
				// create audit trail
				// eventAuditDO = createEventAuditRecord(warningNoProsecution);

			}

			/************ END ************/

			// save audit'
			if (eventAuditDO != null) {

				eventAuditDO.setAuditEntry(new AuditEntry());
				eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
				eventAuditDO.getAuditEntry().setCreateUsername(warningNoProsecution.getCurrentUsername());
				this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);

			}

			return warningNoProsecution;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}
	}

	/**************************** HELPER CLASSES **********************************************/
	/**
	 * 
	 * @param warningNotice
	 * @return
	 * @throws ErrorSavingException
	 */
	public WarningNoProsecutionBO editWarningNoProsecution(WarningNoProsecutionBO warningNotice) throws ErrorSavingException {

		try {
			StringBuffer comments = new StringBuffer();
			/***************** UPDATE THE warningNoProsecution **********************/
			WarningNoProsecutionDO warningNoticeDO = daoFactory.getWarningNoProsecutionDAO().find(WarningNoProsecutionDO.class, warningNotice.getAutomaticSerialNo());

			if (warningNoticeDO == null)
				throw new ErrorSavingException("WarningNoProsecution with this ID does not exist.");

			if (warningNoticeDO != null) {

				warningNoticeDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoticeDO.getAuditEntry().setUpdateUsername(warningNotice.getCurrentUsername());

				
				if((StringUtil.isSet(warningNotice.getComment()) && !StringUtil.isSet(warningNoticeDO.getComment())) //if new comment or comment changed 
						|| (StringUtil.isSet(warningNotice.getComment()) && !warningNotice.getComment().equalsIgnoreCase(warningNoticeDO.getComment())) )
								comments.append(StringUtil.appendSemiColon(comments.toString(), "Comment: " + warningNotice.getNewComment()));
				
				// update updateable fields
				warningNoticeDO.setComment(warningNotice.getComment());

				// warningNoticeDO.setManualSerialNumber(warningNotice.getManualSerialNo());

				if (warningNotice.getOrigin() != null && warningNotice.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_MANUAL) && warningNotice.getManualSerialNo() != null) {
					if (warningNoticeDO.getManualSerialNumber() == null)
						comments.append(StringUtil.appendSemiColon(comments.toString(),"New Origin: " + DocumentType.ORIGIN_MANUAL));

					warningNoticeDO.setManualSerialNumber(warningNotice.getManualSerialNo());
					
					
					//jreid 2014-06-26
					//ensure when document is changed to manual then it is set to printed
					//set to printed on issue date
					// get the status
					StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, Constants.DocumentStatus.PRINTED);
					if (status != null) {
						warningNoticeDO.setStatus(status);
						warningNoticeDO.setPrintDtime(warningNoticeDO.getOffenceDtime());
						warningNoticeDO.setPrintCount(1);
					}

				}

				if (warningNotice.getOrigin() != null && warningNotice.getOrigin().equalsIgnoreCase(DocumentType.ORIGIN_AUTOMATIC)) {
					if (warningNoticeDO.getManualSerialNumber() != null)
						comments.append(StringUtil.appendSemiColon(comments.toString(), "New Origin: " + DocumentType.ORIGIN_AUTOMATIC));

					//warningNoticeDO.setManualSerialNumber(null);
					//jreid 2014-06-26
					//ensure when document is changed to automatic then it is set to created
					//set to printed date to null, print count to zero					
					StatusDO status = this.daoFactory.getSummonsDAO().find(StatusDO.class, Constants.DocumentStatus.CREATED);
					if (status != null && warningNoticeDO.getManualSerialNumber() != null) {
						warningNoticeDO.setStatus(status);
						warningNoticeDO.setPrintDtime(null);
						warningNoticeDO.setPrintCount(0);
					}

					if (warningNotice.getScannedDocList() != null && !warningNotice.getScannedDocList().isEmpty()) {
						ScannedDocBO doc = warningNotice.getScannedDocList().get(0);
						doc.setCurrentUsername(warningNotice.getCurrentUsername());
						this.serviceFactory.getScannedDocService().deleteManualScannedManualDoc(doc, warningNotice);
					}
					
					warningNoticeDO.setManualSerialNumber(null);

				}
				
				
				/*
				 * if(warningNotice.getOrigin() != null &&
				 * !warningNoticeDO.getManualSerialNumber
				 * ().equalsIgnoreCase(warningNotice.getManualSerialNo())){
				 * if(warningNotice.getManualSerialNo() != null) comments +=
				 * "Origin Changed to Manual; "; else comments +=
				 * "Origin Changed to Automatic " + "previous Manual Number is "
				 * + warningNoticeDO.getManualSerialNumber()+ " ; "; }
				 */
				/*
				 * warningNoticeDO.setVerificationReq(Constants.YesNo.YES_FLAG +
				 * "");
				 */
				warningNoticeDO.setAuthorizedFlag(Constants.YesNo.NO_FLAG + "");

				// update
				daoFactory.getWarningNoProsecutionDAO().update(warningNoticeDO);

				// create audit trail
				EventAuditDO eventAuditDO = createEventAuditRecord(warningNotice);
				eventAuditDO.setAuditEntry(new AuditEntry());
				eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
				eventAuditDO.getAuditEntry().setCreateUsername(warningNotice.getCurrentUsername());
				eventAuditDO.setComment(comments.toString());
				this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);

				// update the warningNotice record
				warningNotice = new WarningNoProsecutionBO(warningNoticeDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return warningNotice;

	}

	/**
	 * 
	 * @param warningNoProsecution
	 * @return
	 * @throws ErrorSavingException
	 */
	public WarningNoProsecutionBO authoriseWarningNoProsecutionDetails(WarningNoProsecutionBO warningNoProsecution) throws ErrorSavingException {

		try {

			/***************** UPDATE THE warningNoProsecution **********************/
			WarningNoProsecutionDO warningNoProsecutionDO = daoFactory.getWarningNoProsecutionDAO().find(WarningNoProsecutionDO.class, warningNoProsecution.getAutomaticSerialNo());

			if (warningNoProsecutionDO == null)
				throw new ErrorSavingException("WarningNoProsecution with this ID does not exist.");
			/*
			 * // get the status StatusDO status =
			 * this.daoFactory.getWarningNoProsecutionDAO()
			 * .find(StatusDO.class, Constants.DocumentStatus.AUTHORISED); //
			 * set the stsatus if (status != null)
			 * warningNoProsecutionDO.setStatus(status); else throw new
			 * InvalidFieldException("Status Id is invalid.");
			 */

			if (warningNoProsecutionDO != null) {

				// update details
				warningNoProsecutionDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoProsecutionDO.getAuditEntry().setUpdateUsername(warningNoProsecution.getCurrentUsername());

				if (StringUtils.isNotBlank(warningNoProsecution.getAuthorizedFlag()) && warningNoProsecution.getAuthorizedFlag().equalsIgnoreCase(YesNo.NO_LABEL_STR)
					&& StringUtils.isNotBlank(warningNoProsecution.getVerificationRequired()) && warningNoProsecution.getVerificationRequired().equalsIgnoreCase(YesNo.YES_LABEL_STR)) {
					warningNoProsecutionDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG + "");
					warningNoProsecutionDO.setAuthorizeDtime(Calendar.getInstance().getTime());
					warningNoProsecutionDO.setVerificationReq(Constants.YesNo.NO_LABEL_STR + "");
					warningNoProsecutionDO.setAuthorizeUsername(warningNoProsecution.getCurrentUsername().toUpperCase());
				} else if (StringUtils.isNotBlank(warningNoProsecution.getAuthorizedFlag()) && warningNoProsecution.getAuthorizedFlag().equalsIgnoreCase(YesNo.NO_LABEL_STR)) {
					warningNoProsecutionDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG + "");
					warningNoProsecutionDO.setAuthorizeDtime(Calendar.getInstance().getTime());
					warningNoProsecutionDO.setAuthorizeUsername(warningNoProsecution.getCurrentUsername().toUpperCase());
				} else

				if (StringUtils.isNotBlank(warningNoProsecution.getVerificationRequired()) && warningNoProsecution.getVerificationRequired().equalsIgnoreCase(YesNo.YES_LABEL_STR)) {
					warningNoProsecutionDO.setVerificationReq(Constants.YesNo.NO_LABEL_STR + "");

					
				}
				warningNoProsecutionDO.setComment(warningNoProsecution.getComment());
				// update
				daoFactory.getWarningNoProsecutionDAO().update(warningNoProsecutionDO);

				// update the warningNoProsecution record
				warningNoProsecution = new WarningNoProsecutionBO(warningNoProsecutionDO);

			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return warningNoProsecution;

	}

	/**
	 * 
	 * @param warningNoProsecution
	 * @return
	 * @throws ErrorSavingException
	 */
	public WarningNoProsecutionBO denyAuthorisationWarningNoProsecutionDetails(WarningNoProsecutionBO warningNoProsecution) throws ErrorSavingException {

		try {

			/***************** UPDATE THE warningNoProsecution **********************/
			WarningNoProsecutionDO warningNoProsecutionDO = daoFactory.getWarningNoProsecutionDAO().find(WarningNoProsecutionDO.class, warningNoProsecution.getAutomaticSerialNo());

			if (warningNoProsecutionDO == null)
				throw new ErrorSavingException("WarningNoProsecution with this ID does not exist.");

			if (warningNoProsecutionDO != null) {

				// update details
				//warningNoProsecutionDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				//warningNoProsecutionDO.getAuditEntry().setUpdateUsername(warningNoProsecution.getCurrentUsername());
				
				warningNoProsecutionDO.setComment(warningNoProsecution.getComment());
				
				// authorisation details
				// warningNoProsecutionDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG
				// + "");
				/*
				 * warningNoProsecutionDO.setVerificationReq(Constants.YesNo.NO_FLAG
				 * + "");
				 */
				// warningNoProsecutionDO.setAuthorizeDtime(Calendar.getInstance().getTime());
				// warningNoProsecutionDO.setAuthorizeUsername(warningNoProsecution.getCurrentUsername());

				// update
				daoFactory.getWarningNoProsecutionDAO().update(warningNoProsecutionDO);

				// update the warningNoProsecution record
				warningNoProsecution = new WarningNoProsecutionBO(warningNoProsecutionDO);

			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return warningNoProsecution;

	}

	/**
	 * 
	 * @param warningNoProsecution
	 * @return
	 * @throws ErrorSavingException
	 */
	public WarningNoProsecutionBO cancelWarningNoProsecution(WarningNoProsecutionBO warningNoProsecution) throws ErrorSavingException {

		try {

			/***************** UPDATE THE warningNoProsecution **********************/
			WarningNoProsecutionDO warningNoProsecutionDO = daoFactory.getWarningNoProsecutionDAO().find(WarningNoProsecutionDO.class, warningNoProsecution.getAutomaticSerialNo());

			// get the status
			StatusDO status = this.daoFactory.getWarningNoProsecutionDAO().find(StatusDO.class, warningNoProsecution.getNewStatusId());

			if (warningNoProsecutionDO == null)
				throw new ErrorSavingException("Warning Notice with this ID does not exist.");

			// set the status
			if (status != null) {
				warningNoProsecutionDO.setStatus(status);

			} else
				throw new InvalidFieldException("Status Id is invalid.");

			ReasonDO reason = this.daoFactory.getWarningNoProsecutionDAO().find(ReasonDO.class, warningNoProsecution.getReasonCode());

			// set the reason
			if (reason != null)
				warningNoProsecutionDO.setReason(reason);
			else
				throw new InvalidFieldException("Reason is invalid.");

			if (warningNoProsecutionDO != null) {

				warningNoProsecutionDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoProsecutionDO.getAuditEntry().setUpdateUsername(warningNoProsecution.getCurrentUsername());

				warningNoProsecutionDO.setComment(warningNoProsecution.getComment());

				// update
				daoFactory.getWarningNoProsecutionDAO().saveOrUpdate(warningNoProsecutionDO);

				// update the warningNoProsecution record
				warningNoProsecution = new WarningNoProsecutionBO(warningNoProsecutionDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException();
		}

		return warningNoProsecution;

	}

	/**
	 * 
	 * @param warningNoProsecution
	 * @return
	 */
	public WarningNoProsecutionBO serveWarningNoProsecution(WarningNoProsecutionBO warningNoProsecution) {

		try {

			/***************** UPDATE THE warningNoProsecution **********************/
			WarningNoProsecutionDO warningNoProsecutionDO = daoFactory.getWarningNoProsecutionDAO().find(WarningNoProsecutionDO.class, warningNoProsecution.getAutomaticSerialNo());

			if (warningNoProsecutionDO == null)
				throw new ErrorSavingException("Warning Notice with this ID does not exist.");

			if (warningNoProsecutionDO != null) {

				// get the status
				StatusDO status = this.daoFactory.getWarningNoProsecutionDAO().find(StatusDO.class, warningNoProsecution.getNewStatusId());
				// set the stsatus
				if (status != null)
					warningNoProsecutionDO.setStatus(status);
				else
					throw new InvalidFieldException("Status Id is invalid.");

				warningNoProsecutionDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoProsecutionDO.getAuditEntry().setUpdateUsername(warningNoProsecution.getCurrentUsername());
				warningNoProsecutionDO.setServedOnDate(warningNoProsecution.getServedOnDate());

				// get the staff object
				TAStaffDO staff = this.daoFactory.getWarningNoProsecutionDAO().find(TAStaffDO.class, warningNoProsecution.getServedByUserID());
				if (staff != null)
					warningNoProsecutionDO.setServedByTAStaff(staff);
				else
					throw new InvalidFieldException("Staff Id is invalid.");

				// warningNoProsecutionDO.setServedAtAddress(warningNoProsecution.getServedAtAddress());
				// warningNoProsecutionDO.setServedAtParish(warningNoProsecution.getServedAtParish());

				// update
				daoFactory.getWarningNoProsecutionDAO().update(warningNoProsecutionDO);

				// update the warningNoProsecution record
				warningNoProsecution = new WarningNoProsecutionBO(warningNoProsecutionDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return warningNoProsecution;

	}

	/**
	 * 
	 * @param warningNoProsecution
	 * @return
	 */

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public NoticeView generateWarningNoProsecution(WarningNoProsecutionBO warningNoProsecution) {

		NoticeView warningNoProsecutionView = null;

		try {

			/***************** UPDATE THE warningNoProsecution **********************/
			WarningNoProsecutionDO warningNoProsecutionDO = daoFactory.getWarningNoProsecutionDAO().find(WarningNoProsecutionDO.class, warningNoProsecution.getAutomaticSerialNo());

			if (warningNoProsecutionDO == null)
				throw new ErrorSavingException("Warning Notice with this ID does not exist.");

			// get the status
			StatusDO status = this.daoFactory.getWarningNoProsecutionDAO().find(StatusDO.class, DocumentStatus.PRINTED);
			// set the status
			if (status != null)
				warningNoProsecutionDO.setStatus(status);
			else
				throw new InvalidFieldException("Status Id is invalid.");

			if (warningNoProsecutionDO != null) {

				warningNoProsecutionDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoProsecutionDO.getAuditEntry().setUpdateUsername(warningNoProsecution.getCurrentUsername());
				warningNoProsecutionDO.setPrintDtime(Calendar.getInstance().getTime());
				warningNoProsecutionDO.setPrintUsername(warningNoProsecution.getCurrentUsername());

				// set the print count
				if (warningNoProsecutionDO.getPrintCount() != null)
					warningNoProsecutionDO.setPrintCount(warningNoProsecutionDO.getPrintCount().intValue() + 1);
				else
					warningNoProsecutionDO.setPrintCount(1);

				// update
				daoFactory.getWarningNoProsecutionDAO().update(warningNoProsecutionDO);
				// update the warningNoProsecution record
				warningNoProsecution = new WarningNoProsecutionBO(warningNoProsecutionDO);
			}

			/***************** Populate WarningNoProsecution View For Print *************************/

			warningNoProsecutionView = new NoticeView(warningNoProsecutionDO);

			/*
			 * if (warningNoProsecution.getListOfOffences() != null) {
			 * List<OffenceBO> offences =
			 * warningNoProsecution.getListOfOffences();
			 * System.err.println("warning no pros offences" + offences);
			 * warningNoProsecutionView
			 * .setOffenceExcerpt(DocumentViewBO.formatOffences(offences)); }
			 */

			List<OffenceBO> offences = daoFactory.getRoadCompliancyDAO().getOffencesForDocument(warningNoProsecutionDO.getWarningNoProsecutionID(),
				Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION, warningNoProsecutionDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			warningNoProsecutionView.setOffenceExcerpt(DocumentViewBO.formatOffences(offences));

			List<VehicleOwnerBO> owners = daoFactory.getRoadCompliancyDAO().findVehicleOwnersByPlate(warningNoProsecution.getVehicle().getPlateRegNo());
			if (owners != null) {
				warningNoProsecutionView.setOwnerFullName(WordUtils.capitalize(DocumentViewBO.formatOwnersNames(owners), Constants.STRING_DELIM_ARRAY));
				warningNoProsecutionView.setOwnerAddress(DocumentViewBO.formatOwnerAddresses(owners));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return warningNoProsecutionView;

	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public NoticeView reGenerateWarningNoProsecution(WarningNoProsecutionBO warningNoProsecution) {

		NoticeView warningNoProsecutionView = null;

		try {

			/***************** UPDATE THE warningNoProsecution **********************/
			WarningNoProsecutionDO warningNoProsecutionDO = daoFactory.getWarningNoProsecutionDAO().find(WarningNoProsecutionDO.class, warningNoProsecution.getAutomaticSerialNo());

			if (warningNoProsecutionDO == null)
				throw new ErrorSavingException("Warning Notice with this ID does not exist.");
			// get the status
			/*
			 * StatusDO status = this.daoFactory.getWarningNoProsecutionDAO()
			 * .find(StatusDO.class, DocumentStatus.PRINTED); // set the status
			 * if (status != null) warningNoProsecutionDO.setStatus(status);
			 * else throw new InvalidFieldException("Status Id is invalid.");
			 */
			if (warningNoProsecutionDO != null) {

				warningNoProsecutionDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoProsecutionDO.getAuditEntry().setUpdateUsername(warningNoProsecution.getCurrentUsername());
				warningNoProsecutionDO.setReprintDtime(Calendar.getInstance().getTime());
				warningNoProsecutionDO.setReprintUsername(warningNoProsecution.getCurrentUsername());
				warningNoProsecutionDO.setReprintComment(warningNoProsecution.getReprintComment());
				warningNoProsecutionDO.setComment(warningNoProsecution.getComment());
				// set the print count
				if (warningNoProsecutionDO.getPrintCount() != null) {
					warningNoProsecutionDO.setPrintCount(warningNoProsecutionDO.getPrintCount().intValue() + 1);

					if (warningNoProsecutionDO.getPrintCount() >= 1 && warningNoProsecution.getReprintReasonCode() != null) {
						// get the status
						ReasonDO reprintReason = this.daoFactory.getWarningNoProsecutionDAO().find(ReasonDO.class, warningNoProsecution.getReprintReasonCode());
						// set the status
						if (reprintReason != null)
							warningNoProsecutionDO.setReprintReason(reprintReason);
						else
							throw new InvalidFieldException("Reason is invalid.");
					}

				} else
					warningNoProsecutionDO.setPrintCount(1);

				// update
				daoFactory.getWarningNoProsecutionDAO().update(warningNoProsecutionDO);
				// update the warningNoProsecution record
				warningNoProsecution = new WarningNoProsecutionBO(warningNoProsecutionDO);
			}

			/***************** Populate WarningNoProsecution View For Print *************************/

			warningNoProsecutionView = new NoticeView(warningNoProsecutionDO);

			/*
			 * if (warningNoProsecution.getListOfOffences() != null) {
			 * List<OffenceBO> offences =
			 * warningNoProsecution.getListOfOffences();
			 * System.err.println("warning no pros offences" + offences);
			 * warningNoProsecutionView
			 * .setOffenceExcerpt(DocumentViewBO.formatOffences(offences)); }
			 */

			List<OffenceBO> offences = daoFactory.getRoadCompliancyDAO().getOffencesForDocument(warningNoProsecutionDO.getWarningNoProsecutionID(),
				Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION, warningNoProsecutionDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			warningNoProsecutionView.setOffenceExcerpt(DocumentViewBO.formatOffences(offences));

			// System.err.println("warning no pros offences" + offences);

			List<VehicleOwnerBO> owners = daoFactory.getRoadCompliancyDAO().findVehicleOwnersByPlate(warningNoProsecution.getVehicle().getPlateRegNo());
			if (owners != null) {

				warningNoProsecutionView.setOwnerFullName(WordUtils.capitalize(DocumentViewBO.formatOwnersNames(owners), Constants.STRING_DELIM_ARRAY));
				warningNoProsecutionView.setOwnerAddress(DocumentViewBO.formatOwnerAddresses(owners));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return warningNoProsecutionView;

	}

	/**************************** END HELPER CLASSES ********************************/

	/*
	 * (non-Javadoc)
	 * @see fsl.ta.toms.roms.service.WarningNoProsecutionService#
	 * lookupWarningNoProsecution(fsl.ta.toms.roms
	 * .search.criteria.impl.WarningNoProsecutionCriteriaBO)
	 */
	@Override
	@Transactional(readOnly = true)
	public ArrayList<DocumentViewBO> lookupWarningNoProsecution(DocumentsCriteriaBO criteria) {
		ArrayList<DocumentViewBO> results = null;
		try {
			results = this.daoFactory.getWarningNoProsecutionDAO().lookupWarningNoProsecution(criteria);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return results;
	}

	@Override
	@Transactional
	public void processScannedWarningNoProsecutionDocuments(List<ScannedDocBO> warningNoProsecutionDocList) {

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		if (warningNoProsecutionDocList == null || warningNoProsecutionDocList.isEmpty())
			return;

		WarningNoProsecutionDAO warningNoProsecutionDAO = this.daoFactory.getWarningNoProsecutionDAO();
		// update warningNoProsecution object with this update time and user
		// just for reference sake
		WarningNoProsecutionDO warningNoProsecutionTosave = null;
		// this.daoFactory.getWarningNoProsecutionDAO().find(WarningNoProsecutionDO.class,warningNoProsecutionDocList.get(0).get)
		// ;

		// iterate through scanned document list
		for (ScannedDocBO scannedDoc : warningNoProsecutionDocList) {
			// create new scanned Doc business object
			ScannedDocDO scannedDocToSave = new ScannedDocDO(scannedDoc);

			AuditEntry auditEntry = new AuditEntry();
			auditEntry.setCreateDTime(currentDate);
			// auditEntry.setCreateUsername(scannedDoc.getUserName());
			auditEntry.setUpdateDTime(currentDate);
			// auditEntry.setUpdateUsername(scannedDoc.getUserName());
			scannedDocToSave.setAuditEntry(auditEntry);
			// scannedDocToSave.setWarningNoProsecution(warningNoProsecutionTosave);
			// save object
			warningNoProsecutionDAO.saveOrUpdate(scannedDocToSave);
		}

		// new WarningNoProsecutionDO(warningNoProsecution);
		AuditEntry auditEntry = warningNoProsecutionTosave.getAuditEntry();
		auditEntry.setUpdateDTime(currentDate);
		// auditEntry.setUpdateUsername(warningNoProsecutionDocList.get(0).getUserName());
		warningNoProsecutionDAO.update(warningNoProsecutionTosave);

		// save audit
		/*
		 * EventAuditDO eventAuditDO =
		 * createEventAuditRecord(warningNoProsecutionTosave);
		 * this.serviceFactory
		 * .getEventAuditService().saveEventAuditDO(eventAuditDO);
		 */
	}

	/**
	 * 
	 * @param warningNoProsecution
	 * @return
	 */
	private EventAuditDO createEventAuditRecord(WarningNoProsecutionBO warningNoProsecution) {

		EventAuditDO eventAuditBO = new EventAuditDO();
		StringBuffer comment = new StringBuffer();
		
		if (warningNoProsecution.getAutomaticSerialNo() == null) {

			eventAuditBO.setEventCode(Constants.EventCode.CREATE_DOCUMENT);
			eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.CREATE_DOCUMENT));
		} else {
			eventAuditBO.setEventCode(Constants.EventCode.UPDATE_DOCUMENT);
			eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.UPDATE_DOCUMENT));
		}

		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.DOCUMENT_TYPE);
		eventAuditBO.setRefValue1(warningNoProsecution.getDocumentTypeName());
			
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_NUMBER);
		eventAuditBO.setRefValue2(warningNoProsecution.getReferenceNo() + "");
		
		
		if (warningNoProsecution.getNewStatusId() != null && warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.REPRINTED)) 
		{
		
			Integer reason = warningNoProsecution.getReprintReasonCode();
			ReasonDO reasonDO = new ReasonDO();
			
			if(reason != null)
			{	
				reasonDO = daoFactory.getReasonDAO().find(ReasonDO.class, reason);
				
				if(reason != null)
				{
					comment.append("Reason: " + reasonDO.getDescription() );
				}
				
			}
			
			if (StringUtil.isSet(warningNoProsecution.getReprintComment()))
			{
				comment.append("; Comment: " + warningNoProsecution.getReprintComment());
			}
			
			
			
		}
		
		if ((warningNoProsecution.getNewStatusId() != null && warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED) )||
				warningNoProsecution.getNewStatusId() == null ) //null newStatusID indicates a newly created document)
		{
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Offender Name: " + warningNoProsecution.getOffenderFullName()));
			
			
			String origin = "";
			if(StringUtils.isNotBlank(warningNoProsecution.getManualSerialNo())){
				origin = DocumentType.ORIGIN_MANUAL;
			}else
			{
				origin = DocumentType.ORIGIN_AUTOMATIC;
			}
			
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Document Origin: " + origin));
		}
		
		
		if (warningNoProsecution.getNewStatusId() != null && warningNoProsecution.getNewStatusId().equals(Constants.DocumentStatus.SERVED)) 
		{
			StaffUserMappingCriteriaBO crit = new StaffUserMappingCriteriaBO();
			crit.setStaffId(warningNoProsecution.getServedByUserID());
			
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
			
			if(warningNoProsecution.getServedOnDate() != null)
				servedDate = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  warningNoProsecution.getServedOnDate());
			
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Served Date: " + servedDate));
		}
		
		
		eventAuditBO.setComment(comment.toString());

		return eventAuditBO;
	}

	private EventAuditDO createEventAuditRecordVerify(WarningNoProsecutionBO warningNotice) {

		EventAuditDO eventAuditBO = new EventAuditDO();

		eventAuditBO.setEventCode(Constants.EventCode.VERIFY_DOCUMENT);
		eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.VERIFY_DOCUMENT));

		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.DOCUMENT_TYPE);
		eventAuditBO.setRefValue1(warningNotice.getDocumentTypeName());
			
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_NUMBER);
		eventAuditBO.setRefValue2(warningNotice.getReferenceNo() + "");
		
		if(StringUtil.isSet(warningNotice.getComment()))
				eventAuditBO.setComment("; Comment: " + warningNotice.getNewComment()); 
		
		eventAuditBO.setAuditEntry(new AuditEntry());

		return eventAuditBO;
	}
	
	private EventAuditDO createEventAuditRecordDenyVerify(WarningNoProsecutionBO summon) {

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
	public WarningNoProsecutionBO getWarningNoProsecutionDetailsById(Integer warningNoProsecutionId) {

		if (warningNoProsecutionId == null)
			return null;

		WarningNoProsecutionBO warningNoProsecutionView = this.daoFactory.getWarningNoProsecutionDAO().lookupWarningNoProsecutionById(warningNoProsecutionId);

		if (warningNoProsecutionView == null)
			return null;

		return warningNoProsecutionView;

	}
	
	@Override
	@Transactional(readOnly = true)
	public NoticeView getNoticeView(WarningNoProsecutionBO warningNoProsecutionBO) {
		NoticeView warningNoProsecutionView = null;
		try{
			if (warningNoProsecutionBO == null)
				return null;
	
			WarningNoProsecutionDO warningNoProsecutionDO = daoFactory.getWarningNoProsecutionDAO().find(WarningNoProsecutionDO.class, warningNoProsecutionBO.getAutomaticSerialNo());
	
			if (warningNoProsecutionDO == null)
				return null;
			
			warningNoProsecutionView = new NoticeView(warningNoProsecutionDO);

			List<OffenceBO> offences = daoFactory.getRoadCompliancyDAO().getOffencesForDocument(warningNoProsecutionDO.getWarningNoProsecutionID(),
				Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION, warningNoProsecutionDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			warningNoProsecutionView.setOffenceExcerpt(DocumentViewBO.formatOffences(offences));

			List<VehicleOwnerBO> owners = daoFactory.getRoadCompliancyDAO().findVehicleOwnersByPlate(warningNoProsecutionBO.getVehicle().getPlateRegNo());
			if (owners != null) {
				warningNoProsecutionView.setOwnerFullName(WordUtils.capitalize(DocumentViewBO.formatOwnersNames(owners), Constants.STRING_DELIM_ARRAY));
				warningNoProsecutionView.setOwnerAddress(DocumentViewBO.formatOwnerAddresses(owners));
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return warningNoProsecutionView;

	}

}
