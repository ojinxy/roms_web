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
import org.joda.time.DateTime;
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
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.DocumentStatus;
import fsl.ta.toms.roms.constants.Constants.DocumentType;
import fsl.ta.toms.roms.constants.Constants.YesNo;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.WarningNoticeDAO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.CDPersonTypeDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.PoundDO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.RoadCheckOffenceOutcomeDO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.dataobjects.VehicleDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.dataobjects.WreckingCompanyDO;
import fsl.ta.toms.roms.documents.view.NoticeView;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.WarningNoticeService;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author jreid Created Date: Apr 30, 2013
 */
public class WarningNoticeServiceImpl implements WarningNoticeService {

	private DAOFactory	daoFactory;

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Autowired
	ServiceFactory	serviceFactory;

	@Override
	@Transactional
	public Integer saveWarningNotice(WarningNoticeBO warningNotice) throws ErrorSavingException {

		WarningNoticeDO warningNoticeTosave = new WarningNoticeDO(warningNotice);

		if (warningNotice.getReasonCode() == null || warningNotice.getReasonCode() < 0)
			warningNoticeTosave.setReason(null);

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		try {
			AuditEntry auditEntry = new AuditEntry();

			// eventAuditBO.set)(new
			// Date(warningNotice.getIssueDate().getTime()));
			auditEntry.setCreateUsername(warningNotice.getCurrentUsername());
			auditEntry.setCreateDTime(currentDate);
			warningNoticeTosave.setAuditEntry(auditEntry);

			// set the road operation outcome
			RoadCheckOffenceOutcomeDO roadCheckOffenceOutcomeDO = this.daoFactory.getWarningNoticeDAO().find(RoadCheckOffenceOutcomeDO.class, warningNotice.getRoadCheckOffenceOutcomeCode());
			if (roadCheckOffenceOutcomeDO != null)
				warningNoticeTosave.setRoadCheckOffenceOutcome(roadCheckOffenceOutcomeDO);

			// get the offender object
			PersonDO offender = this.daoFactory.getWarningNoticeDAO().find(PersonDO.class, warningNotice.getOffenderId());
			// set the operation object
			if (offender != null)
				warningNoticeTosave.setOffender(offender);

			// get teh staff object
			TAStaffDO staff = this.daoFactory.getWarningNoticeDAO().find(TAStaffDO.class, warningNotice.getTaStaffId());
			// System.err.println(" staff :" + staff);
			// set the operation object
			if (staff != null)
				warningNoticeTosave.setTaStaff(staff);

			if (!StringUtils.isEmpty(warningNotice.getVerificationRequired()))
				warningNoticeTosave.setVerificationReq(warningNotice.getVerificationRequired());

			// set the status
			StatusDO status = null;
			// If the manual serial number is not null, set status to printed
			if (StringUtils.isNotBlank(warningNotice.getManualSerialNo())) {
				// set the status
				status = this.daoFactory.getWarningNoticeDAO().find(StatusDO.class, Constants.DocumentStatus.PRINTED);
			} else {
				// set the status
				status = this.daoFactory.getWarningNoticeDAO().find(StatusDO.class, Constants.DocumentStatus.CREATED);

			}

			// set the operation object
			if (status != null)
				warningNoticeTosave.setStatus(status);

			// set the pound
			PoundDO pound = this.daoFactory.getWarningNoticeDAO().find(PoundDO.class, warningNotice.getPoundId());
			// set the operation object
			if (pound != null)
				warningNoticeTosave.setPound(pound);

			// set the wrecking company
			if (warningNotice.getWreckingCompanyId() != null) {
				WreckingCompanyDO wreckingCompanyDO = this.daoFactory.getWarningNoticeDAO().find(WreckingCompanyDO.class, warningNotice.getWreckingCompanyId());
				// set the operation object
				if (wreckingCompanyDO != null)
					warningNoticeTosave.setWreckingCompany(wreckingCompanyDO);
			}

			if (warningNotice.getVehicleMoverPersonType() != null) {
				// set the vehicle mover person type
				CDPersonTypeDO vehicleMoverPersonTypeDO = this.daoFactory.getWarningNoticeDAO().find(CDPersonTypeDO.class, warningNotice.getVehicleMoverPersonType());
				// set the operation object
				if (vehicleMoverPersonTypeDO != null)
					warningNoticeTosave.setVehicleDeliveredByPersonType(vehicleMoverPersonTypeDO);
			}

			if (warningNotice.getWreckerVehicleID() != null) {
				// set the wrecker vehicle
				VehicleDO wreckingVehicleDO = this.daoFactory.getWarningNoticeDAO().find(VehicleDO.class, warningNotice.getWreckerVehicleID());
				// set the operation object
				if (wreckingVehicleDO != null)
					warningNoticeTosave.setWreckerVehicle(wreckingVehicleDO);
			}

			if (warningNotice.getVehicleMoverPersonID() != null) {
				// set the vehicle mover
				PersonDO vehicleMoverDO = this.daoFactory.getWarningNoticeDAO().find(PersonDO.class, warningNotice.getVehicleMoverPersonID());
				// set the operation object
				if (vehicleMoverDO != null)
					warningNoticeTosave.setVehicleDeliveredByPerson(vehicleMoverDO);
			}

			// Added seizure date time which was not being saved earlier OA 20
			// Jan 2014
			warningNoticeTosave.setSeizureDtime(warningNotice.getOffenceDtime() == null ? DateTime.now().toDate() : warningNotice.getOffenceDtime());

			// save the warningNotice
			// warningNoticeTosave.setIssueDate(currentDate);
			Integer warningNoticeId = (Integer) this.daoFactory.getWarningNoticeDAO().save(warningNoticeTosave);

			/************ SAVE SCANNED DOCS ***********/
			if (warningNotice.getScannedDocList() != null && !warningNotice.getScannedDocList().isEmpty()) {
				// List<WarningNoticeScannedDocBO> warningNoticeDocList =
				// warningNotice.getScannedDocList().get(0).setWarningNoticeId(warningNoticeId);
				processScannedWarningNoticeDocuments(warningNotice.getScannedDocList());

			}

			/************ END SAVE ************/
			
			warningNoticeTosave = this.daoFactory.getWarningNoticeDAO().find(WarningNoticeDO.class, warningNoticeId);
			// set the id
			warningNotice.setAutomaticSerialNo(warningNoticeId);
			warningNotice.setReferenceNo(Constants.formatRefSeqNo(warningNoticeTosave.getReferenceNo()));

			// save audit
			EventAuditDO eventAuditDO = createEventAuditRecord(warningNotice);
			
			//set event code
			eventAuditDO.setEventCode(Constants.EventCode.CREATE_DOCUMENT);
			eventAuditDO.setEvent(new CDEventDO(Constants.EventCode.CREATE_DOCUMENT));
			
			eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
			eventAuditDO.getAuditEntry().setCreateUsername(warningNoticeTosave.getAuditEntry().getCreateUsername());
			this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);

			return warningNoticeId;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorSavingException();
		}
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * fsl.ta.toms.roms.service.WarningNoticeService#updateWarningNotice(fsl
	 * .ta.toms.roms .bo.WarningNoticeBO)
	 */
	@Override
	@Transactional
	public WarningNoticeBO updateWarningNotice(WarningNoticeBO warningNotice) throws ErrorSavingException {

		if (warningNotice == null)
			throw new ErrorSavingException("Warning Notice details cannot be empty");

		EventAuditDO eventAuditDO = null;

		// System.err.println("  new status " + warningNotice.getNewStatusId());

		try {
			/************************ CANCELLATION *********************/
			if (warningNotice.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED)) {

				cancelWarningNotice(warningNotice);

				eventAuditDO = createEventAuditRecord(warningNotice);
				eventAuditDO.setEventCode(Constants.EventCode.CANCEL_DOCUMENT);
			}

			/******************* SERVING ***********************************/
			if (warningNotice.getNewStatusId().equals(DocumentStatus.SERVED)) {

				serveWarningNotice(warningNotice);

				eventAuditDO = createEventAuditRecord(warningNotice);
				eventAuditDO.setEventCode(Constants.EventCode.SERVE_DOCUMENT);
			}

			/******************* PRINTING ***********************************/

			if (warningNotice.getNewStatusId().equals(DocumentStatus.PRINTED)) {
				generateWarningNotice(warningNotice);

				eventAuditDO = createEventAuditRecord(warningNotice);
				eventAuditDO.setEventCode(Constants.EventCode.PRINT_DOCUMENT);
			}
			/******************* REPRINTING ********************************/

			if (warningNotice.getNewStatusId().equals(DocumentStatus.REPRINTED)) {

				reGenerateWarningNotice(warningNotice);

				eventAuditDO = createEventAuditRecord(warningNotice);
				eventAuditDO.setEventCode(Constants.EventCode.REPRINT_DOCUMENT);
			}
			/******************* Authorising details ***********************************/
			if (warningNotice.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.AUTHORISED) || warningNotice.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.VERIFIED)) {

				authoriseWarningNoticeDetails(warningNotice);

				eventAuditDO = createEventAuditRecordVerify(warningNotice);
				eventAuditDO.setEventCode(Constants.EventCode.VERIFY_DOCUMENT);
				
				//removed in response to ROMS1.0-231
//				if (warningNotice.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.AUTHORISED)) {
//					eventAuditDO.setEventCode(Constants.EventCode.AUTHORIZE_DOCUMENT);
//				} else {
//					eventAuditDO.setEventCode(Constants.EventCode.VERIFY_DETAILS_DOCUMENT);
//				}
			}

			/******************* Not Authorising details ***********************************/
			if (warningNotice.getNewStatusId().equalsIgnoreCase(Constants.DocumentStatus.DENY_AUTHORISATION)) {

				denyAuthorisationWarningNoticeDetails(warningNotice);

				// create audit trail
				eventAuditDO = createEventAuditRecordDenyVerify(warningNotice);
			}

			// save audit
			if (eventAuditDO != null) {
				eventAuditDO.setAuditEntry(new AuditEntry());
				eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
				eventAuditDO.getAuditEntry().setCreateUsername(warningNotice.getCurrentUsername());
				this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);
			}

			/******************** EDIT ********************************/

			if (warningNotice.getNewStatusId().equals(Constants.DocumentStatus.EDITED)) {

				editWarningNotice(warningNotice);
				// create audit trail
				// eventAuditDO = createEventAuditRecord(warningNotice);

			}

			/****************** END **************************************/

		} catch (Exception e) {
			e.printStackTrace();
			// System.err.println(" caught here 1");
			throw new ErrorSavingException(e.getMessage());
		}

		return warningNotice;

	}

	/**************************** HELPER CLASSES **********************************************/
	/**
	 * 
	 * @param warningNotice
	 * @return
	 * @throws ErrorSavingException
	 */
	public WarningNoticeBO editWarningNotice(WarningNoticeBO warningNotice) throws ErrorSavingException {

		try {
			StringBuffer comments = new StringBuffer();
			/***************** UPDATE THE warningNotice **********************/
			WarningNoticeDO warningNoticeDO = daoFactory.getWarningNoticeDAO().find(WarningNoticeDO.class, warningNotice.getAutomaticSerialNo());

			if (warningNoticeDO == null)
				throw new ErrorSavingException("Warning Notice with this ID does not exist.");

			if (warningNoticeDO != null) {

				warningNoticeDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoticeDO.getAuditEntry().setUpdateUsername(warningNotice.getCurrentUsername());

				if((StringUtil.isSet(warningNotice.getComment()) && !StringUtil.isSet(warningNoticeDO.getComment())) //if new comment or comment changed 
						|| (StringUtil.isSet(warningNotice.getComment()) && !warningNotice.getComment().equalsIgnoreCase(warningNoticeDO.getComment())) )
								comments.append(StringUtil.appendSemiColon(comments.toString(), "Comment: " + warningNotice.getNewComment()));
				
				// set the updateable fields
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
						warningNoticeDO.setPrintDtime(warningNoticeDO
								.getRoadCheckOffenceOutcome()
								.getRoadCheckOffence().getRoadCheck()
								.getCompliance().getOffenceDateTime());
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
						this.serviceFactory.getScannedDocService().deleteManualScannedManualDoc(doc,warningNotice);
					}
					
					warningNoticeDO.setManualSerialNumber(null);
				}

				
				
				/*
				 * if (warningNotice.getOrigin() != null &&
				 * warningNoticeDO.getOrigin() != null &&
				 * !warningNoticeDO.getOrigin
				 * ().equalsIgnoreCase(warningNotice.getOrigin())) { if
				 * (warningNotice.getManualSerialNo() != null) comments +=
				 * "Origin Changed to Manual; "; else comments +=
				 * "Origin Changed to Automatic " + "previous Manual Number is "
				 * + warningNoticeDO.getManualSerialNumber() + " ; "; }
				 */
				warningNoticeDO.setAuthorizedFlag(Constants.YesNo.NO_FLAG + "");

				// update
				daoFactory.getWarningNoticeDAO().update(warningNoticeDO);

				// create audit trail
				EventAuditDO eventAuditDO = createEventAuditRecord(warningNotice);
				eventAuditDO.setAuditEntry(new AuditEntry());
				eventAuditDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
				eventAuditDO.getAuditEntry().setCreateUsername(warningNotice.getCurrentUsername());
				eventAuditDO.setComment(comments.toString());
				this.serviceFactory.getEventAuditService().saveEventAuditDO(eventAuditDO);

				// update the warningNotice record
				warningNotice = new WarningNoticeBO(warningNoticeDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return warningNotice;

	}

	/**
	 * 
	 * @param warningNotice
	 * @return
	 * @throws ErrorSavingException
	 */
	public WarningNoticeBO authoriseWarningNoticeDetails(WarningNoticeBO warningNotice) throws ErrorSavingException {

		try {

			/***************** UPDATE THE warningNotice **********************/
			WarningNoticeDO warningNoticeDO = daoFactory.getWarningNoticeDAO().find(WarningNoticeDO.class, warningNotice.getAutomaticSerialNo());

			if (warningNoticeDO == null)
				throw new ErrorSavingException("WarningNotice with this ID does not exist.");
			// get the status
			/*
			 * StatusDO status = this.daoFactory.getWarningNoticeDAO().find(
			 * StatusDO.class, Constants.DocumentStatus.AUTHORISED); // set the
			 * status if (status != null) warningNoticeDO.setStatus(status);
			 * else throw new InvalidFieldException("Status Id is invalid.");
			 */

			if (warningNoticeDO != null) {

				// update details
				warningNoticeDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoticeDO.getAuditEntry().setUpdateUsername(warningNotice.getCurrentUsername());

				if (StringUtils.isNotBlank(warningNotice.getAuthorizedFlag()) && warningNotice.getAuthorizedFlag().equalsIgnoreCase(YesNo.NO_LABEL_STR)
					&& StringUtils.isNotBlank(warningNotice.getVerificationRequired()) && warningNotice.getVerificationRequired().equalsIgnoreCase(YesNo.YES_LABEL_STR)) {
					warningNoticeDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG + "");
					warningNoticeDO.setAuthorizeDtime(Calendar.getInstance().getTime());
					warningNoticeDO.setVerificationReq(Constants.YesNo.NO_LABEL_STR + "");
					warningNoticeDO.setAuthorizeUsername(warningNotice.getCurrentUsername().toUpperCase());
				} else if (StringUtils.isNotBlank(warningNotice.getAuthorizedFlag()) && warningNotice.getAuthorizedFlag().equalsIgnoreCase(YesNo.NO_LABEL_STR)) {
					warningNoticeDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG + "");
					warningNoticeDO.setAuthorizeDtime(Calendar.getInstance().getTime());
					warningNoticeDO.setAuthorizeUsername(warningNotice.getCurrentUsername().toUpperCase());
				} else

				if (StringUtils.isNotBlank(warningNotice.getVerificationRequired()) && warningNotice.getVerificationRequired().equalsIgnoreCase(YesNo.YES_LABEL_STR)) {
					warningNoticeDO.setVerificationReq(Constants.YesNo.NO_LABEL_STR + "");

				}
				warningNoticeDO.setComment(warningNotice.getComment());

				// update
				daoFactory.getWarningNoticeDAO().update(warningNoticeDO);

				// update the warningNotice record
				warningNotice = new WarningNoticeBO(warningNoticeDO);

			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return warningNotice;

	}

	/**
	 * 
	 * @param warningNotice
	 * @return
	 * @throws ErrorSavingException
	 */
	public WarningNoticeBO denyAuthorisationWarningNoticeDetails(WarningNoticeBO warningNotice) throws ErrorSavingException {

		try {

			/***************** UPDATE THE warningNotice **********************/
			WarningNoticeDO warningNoticeDO = daoFactory.getWarningNoticeDAO().find(WarningNoticeDO.class, warningNotice.getAutomaticSerialNo());

			if (warningNoticeDO == null)
				throw new ErrorSavingException("WarningNotice with this ID does not exist.");

			if (warningNoticeDO != null) {

				// update details
				//warningNoticeDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				//warningNoticeDO.getAuditEntry().setUpdateUsername(warningNotice.getCurrentUsername());
				
				warningNoticeDO.setComment(warningNotice.getComment());
				
				
				// authorisation details
				// warningNoticeDO.setAuthorizedFlag(Constants.YesNo.YES_FLAG +
				// "");
				/*
				 * warningNoticeDO.setVerificationReq(Constants.YesNo.NO_FLAG +
				 * "");
				 */
				// warningNoticeDO.setAuthorizeDtime(Calendar.getInstance().getTime());
				// warningNoticeDO.setAuthorizeUsername(warningNotice.getCurrentUsername());

				// update
				daoFactory.getWarningNoticeDAO().update(warningNoticeDO);

				// update the warningNotice record
				warningNotice = new WarningNoticeBO(warningNoticeDO);

			}

		} catch (Exception e) {

			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

		return warningNotice;

	}

	/**
	 * 
	 * @param warningNotice
	 * @return
	 * @throws ErrorSavingException
	 * @throws InvalidFieldException
	 */
	private WarningNoticeBO cancelWarningNotice(WarningNoticeBO warningNotice) throws ErrorSavingException, InvalidFieldException {

		/***************** UPDATE THE warningNotice **********************/
		WarningNoticeDO warningNoticeDO = daoFactory.getWarningNoticeDAO().find(WarningNoticeDO.class, warningNotice.getAutomaticSerialNo());

		// get the status
		StatusDO status = this.daoFactory.getWarningNoticeDAO().find(StatusDO.class, warningNotice.getNewStatusId());

		if (warningNoticeDO == null)
			throw new ErrorSavingException("Warning Notice with this ID does not exist.");

		// set the status
		if (status != null) {
			warningNoticeDO.setStatus(status);

		} else
			throw new InvalidFieldException("Status Id is invalid.");

		ReasonDO reason = this.daoFactory.getWarningNoticeDAO().find(ReasonDO.class, warningNotice.getReasonCode());

		// set the reason
		if (reason != null)
			warningNoticeDO.setReason(reason);
		else
			throw new InvalidFieldException("Reason is invalid.");

		if (warningNoticeDO != null) {

			warningNoticeDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
			warningNoticeDO.getAuditEntry().setUpdateUsername(warningNotice.getCurrentUsername());

			warningNoticeDO.setComment(warningNotice.getComment());

			// update
			daoFactory.getWarningNoticeDAO().saveOrUpdate(warningNoticeDO);

			// update the warningNotice record
			warningNotice = new WarningNoticeBO(warningNoticeDO);
		}

		return warningNotice;

	}

	/**
	 * 
	 * @param warningNotice
	 * @return
	 */
	private WarningNoticeBO serveWarningNotice(WarningNoticeBO warningNotice) {

		try {

			/***************** UPDATE THE warningNotice **********************/
			WarningNoticeDO warningNoticeDO = daoFactory.getWarningNoticeDAO().find(WarningNoticeDO.class, warningNotice.getAutomaticSerialNo());

			if (warningNoticeDO == null)
				throw new ErrorSavingException("Warning Notice with this ID does not exist.");

			if (warningNoticeDO != null) {

				// get the status
				StatusDO status = this.daoFactory.getWarningNoticeDAO().find(StatusDO.class, DocumentStatus.SERVED);
				// set the status
				if (status != null)
					warningNoticeDO.setStatus(status);
				else
					throw new InvalidFieldException("Status Id is invalid.");

				warningNoticeDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoticeDO.getAuditEntry().setUpdateUsername(warningNotice.getCurrentUsername());
				warningNoticeDO.setServedOnDate(warningNotice.getServedOnDate());

				// get the staff object
				TAStaffDO staff = this.daoFactory.getWarningNoticeDAO().find(TAStaffDO.class, warningNotice.getServedByUserID());
				if (staff != null)
					warningNoticeDO.setServedByTAStaff(staff);
				else
					throw new InvalidFieldException("Staff Id is invalid.");

				// warningNoticeDO.setServedAtAddress(warningNotice.getServedAtAddress());
				// warningNoticeDO.setServedAtParish(warningNotice.getServedAtParish());

				// update
				daoFactory.getWarningNoticeDAO().update(warningNoticeDO);

				// update the warningNotice record
				warningNotice = new WarningNoticeBO(warningNoticeDO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return warningNotice;

	}

	/**
	 * 
	 * @param warningNotice
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public NoticeView generateWarningNotice(WarningNoticeBO warningNotice) {

		NoticeView warningNoticeView = null;

		try {

			/***************** UPDATE THE warningNotice **********************/
			WarningNoticeDO warningNoticeDO = daoFactory.getWarningNoticeDAO().load(WarningNoticeDO.class, warningNotice.getAutomaticSerialNo());

			if (warningNoticeDO == null)
				throw new ErrorSavingException("Warning Notice with this ID does not exist.");

			// get the status
			StatusDO status = this.daoFactory.getWarningNoticeDAO().find(StatusDO.class, DocumentStatus.PRINTED);
			// set the status
			if (status != null)
				warningNoticeDO.setStatus(status);
			else
				throw new InvalidFieldException("Status Id is invalid.");

			if (warningNoticeDO != null) {

				warningNoticeDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoticeDO.getAuditEntry().setUpdateUsername(warningNotice.getCurrentUsername());
				warningNoticeDO.setPrintDtime(Calendar.getInstance().getTime());
				warningNoticeDO.setPrintUsername(warningNotice.getCurrentUsername());

				// set the print count
				if (warningNoticeDO.getPrintCount() != null)
					warningNoticeDO.setPrintCount(warningNoticeDO.getPrintCount().intValue() + 1);
				else
					warningNoticeDO.setPrintCount(1);

				// update
				daoFactory.getWarningNoticeDAO().update(warningNoticeDO);
				// update the warningNotice record
				warningNotice = new WarningNoticeBO(warningNoticeDO);
			}

			/***************** Populate WarningNotice View For Print *************************/

			warningNoticeView = new NoticeView(warningNoticeDO);

			List<OffenceBO> offences = daoFactory.getRoadCompliancyDAO().getOffencesForDocument(warningNoticeDO.getWarningNoticeId(), Constants.DocumentType.WARNING_NOTICE,
				warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			warningNoticeView.setOffenceExcerpt(DocumentViewBO.formatOffences(offences));

			/*
			 * if(warningNotice.getListOfWitnesses() != null){ List<PersonBO>
			 * witnesses = warningNotice.getListOfWitnesses();
			 * warningNoticeView.
			 * setWitnessFullName(DocumentViewBO.formatWitnessesNames
			 * (witnesses)); warningNoticeView.setWitnessAddress(DocumentViewBO.
			 * formatWitnessesAddresses(witnesses)); }
			 */

			List<VehicleOwnerBO> owners = daoFactory.getRoadCompliancyDAO().findVehicleOwnersByPlate(warningNotice.getVehicle().getPlateRegNo());
			if (owners != null) {

				warningNoticeView.setOwnerFullName(DocumentViewBO.formatOwnersNames(owners));
				warningNoticeView.setOwnerAddress(DocumentViewBO.formatOwnerAddresses(owners));
			}

			List<PersonBO> witnesses = daoFactory.getRoadCompliancyDAO().getWitnessesforCompliance(
				warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			warningNoticeView.setWitnessFullName(DocumentViewBO.formatWitnessesNames(witnesses));
			warningNoticeView.setWitnessAddress(DocumentViewBO.formatWitnessesAddresses(witnesses));

		} catch (Exception e) {

			e.printStackTrace();
		}

		return warningNoticeView;

	}

	/**
	 * 
	 * @param warningNotice
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public NoticeView reGenerateWarningNotice(WarningNoticeBO warningNotice) {

		NoticeView warningNoticeView = null;

		try {

			/***************** UPDATE THE warningNotice **********************/
			WarningNoticeDO warningNoticeDO = daoFactory.getWarningNoticeDAO().load(WarningNoticeDO.class, warningNotice.getAutomaticSerialNo());

			if (warningNoticeDO == null)
				throw new ErrorSavingException("Warning Notice with this ID does not exist.");
			// get the status
			/*
			 * StatusDO status = this.daoFactory.getWarningNoticeDAO().find(
			 * StatusDO.class, DocumentStatus.PRINTED); // set the status if
			 * (status != null) warningNoticeDO.setStatus(status); else throw
			 * new InvalidFieldException("Status Id is invalid.");
			 */
			if (warningNoticeDO != null) {

				warningNoticeDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				warningNoticeDO.getAuditEntry().setUpdateUsername(warningNotice.getCurrentUsername());
				warningNoticeDO.setReprintDtime(Calendar.getInstance().getTime());
				warningNoticeDO.setReprintUsername(warningNotice.getCurrentUsername());
				warningNoticeDO.setReprintComment(warningNotice.getReprintComment());
				warningNoticeDO.setComment(warningNotice.getComment());
				
				// set the print count
				if (warningNoticeDO.getPrintCount() != null) {
					warningNoticeDO.setPrintCount(warningNoticeDO.getPrintCount().intValue() + 1);

					if (warningNoticeDO.getPrintCount() >= 1 && warningNotice.getReprintReasonCode() != null) {
						// get the status
						ReasonDO reprintReason = this.daoFactory.getWarningNoticeDAO().find(ReasonDO.class, warningNotice.getReprintReasonCode());
						// set the status
						if (reprintReason != null)
							warningNoticeDO.setReprintReason(reprintReason);
						else
							throw new InvalidFieldException("Reason is invalid.");
					}

				} else
					warningNoticeDO.setPrintCount(1);

				// update
				daoFactory.getWarningNoticeDAO().update(warningNoticeDO);
				// update the warningNotice record
				warningNotice = new WarningNoticeBO(warningNoticeDO);
			}

			/***************** Populate WarningNotice View For Print *************************/

			warningNoticeView = new NoticeView(warningNoticeDO);

			/*
			 * if (warningNotice.getListOfOffences() != null) { List<OffenceBO>
			 * offences = warningNotice.getListOfOffences();
			 * System.err.println("warning no pros offences" + offences);
			 * warningNoticeView
			 * .setOffenceExcerpt(DocumentViewBO.formatOffences(offences)); }
			 */
			List<OffenceBO> offences = daoFactory.getRoadCompliancyDAO().getOffencesForDocument(warningNoticeDO.getWarningNoticeId(), Constants.DocumentType.WARNING_NOTICE,
				warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			warningNoticeView.setOffenceExcerpt(DocumentViewBO.formatOffences(offences));

			List<VehicleOwnerBO> owners = daoFactory.getRoadCompliancyDAO().findVehicleOwnersByPlate(warningNotice.getVehicle().getPlateRegNo());
			if (owners != null) {
				warningNoticeView.setOwnerFullName(DocumentViewBO.formatOwnersNames(owners));
				warningNoticeView.setOwnerAddress(DocumentViewBO.formatOwnerAddresses(owners));
			}

			List<PersonBO> witnesses = daoFactory.getRoadCompliancyDAO().getWitnessesforCompliance(
				warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			warningNoticeView.setWitnessFullName(DocumentViewBO.formatWitnessesNames(witnesses));
			warningNoticeView.setWitnessAddress(DocumentViewBO.formatWitnessesAddresses(witnesses));

		} catch (Exception e) {

			e.printStackTrace();
		}

		return warningNoticeView;

	}

	/**************************** END HELPER CLASSES ********************************/

	@Override
	@Transactional(readOnly = true)
	public ArrayList<DocumentViewBO> lookupWarningNotice(DocumentsCriteriaBO criteria) {
		ArrayList<DocumentViewBO> results = null;
		try {
			results = this.daoFactory.getWarningNoticeDAO().lookupWarningNotice(criteria);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return results;
	}

	@Override
	@Transactional
	public void processScannedWarningNoticeDocuments(List<ScannedDocBO> warningNoticeDocList) {

		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		if (warningNoticeDocList == null || warningNoticeDocList.isEmpty())
			return;

		WarningNoticeDAO warningNoticeDAO = this.daoFactory.getWarningNoticeDAO();
		// update warningNotice object with this update time and user just for
		// reference sake
		WarningNoticeDO warningNoticeTosave = null;
		// this.daoFactory.getWarningNoticeDAO().find(WarningNoticeDO.class,warningNoticeDocList.get(0).getWarningNoticeId())
		// ;

		// iterate through scanned document list
		for (ScannedDocBO scannedDoc : warningNoticeDocList) {
			// create new scanned Doc business object
			ScannedDocDO scannedDocToSave = new ScannedDocDO(scannedDoc);

			AuditEntry auditEntry = new AuditEntry();
			auditEntry.setCreateDTime(currentDate);
			// auditEntry.setCreateUsername(scannedDoc.getUserName());
			auditEntry.setUpdateDTime(currentDate);
			// auditEntry.setUpdateUsername(scannedDoc.getUserName());
			scannedDocToSave.setAuditEntry(auditEntry);
			// scannedDocToSave.setWarningNotice(warningNoticeTosave);
			// save object
			warningNoticeDAO.saveOrUpdate(scannedDocToSave);
		}

		// new WarningNoticeDO(warningNotice);
		AuditEntry auditEntry = warningNoticeTosave.getAuditEntry();
		auditEntry.setUpdateDTime(currentDate);
		// auditEntry.setUpdateUsername(warningNoticeDocList.get(0).getUserName());
		warningNoticeDAO.update(warningNoticeTosave);

		// save audit
		/*
		 * EventAuditDO eventAuditDO =
		 * createEventAuditRecord(warningNoticeTosave);
		 * this.serviceFactory.getEventAuditService
		 * ().saveEventAuditDO(eventAuditDO);
		 */
	}

	/**
	 * 
	 * @param warningNotice
	 * @return
	 */
	private EventAuditDO createEventAuditRecord(WarningNoticeBO warningNotice) {

		EventAuditDO eventAuditBO = new EventAuditDO();
		StringBuffer comment = new StringBuffer();

		if (warningNotice.getAutomaticSerialNo() == null) {

			eventAuditBO.setEventCode(Constants.EventCode.CREATE_DOCUMENT);
			eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.CREATE_DOCUMENT));
		} else {
			eventAuditBO.setEventCode(Constants.EventCode.UPDATE_DOCUMENT);
			eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.UPDATE_DOCUMENT));
		}

		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.DOCUMENT_TYPE);
		eventAuditBO.setRefValue1(warningNotice.getDocumentTypeName());
			
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_NUMBER);
		eventAuditBO.setRefValue2(warningNotice.getReferenceNo() + "");
		
		
		if (warningNotice.getNewStatusId() != null && warningNotice.getNewStatusId().equals(Constants.DocumentStatus.REPRINTED)) 
		{
			
			Integer reason = warningNotice.getReprintReasonCode();
			ReasonDO reasonDO = new ReasonDO();
			
			if(reason != null)
			{	
				reasonDO = daoFactory.getReasonDAO().find(ReasonDO.class, reason);
				
				if(reason != null)
				{
					comment.append("Reason: " + reasonDO.getDescription());
				}
				
			}
			
			if (StringUtil.isSet(warningNotice.getReprintComment()))
			{
				comment.append(StringUtil.appendSemiColon(comment.toString(),"Comment: " + warningNotice.getReprintComment()));
			}
			
		}
		
		if ((warningNotice.getNewStatusId() != null && warningNotice.getNewStatusId().equals(Constants.DocumentStatus.CANCELLED))||
				warningNotice.getNewStatusId() == null ) //null newStatusID indicates a newly created document) 
		{
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Offender Name: " + warningNotice.getOffenderFullName()));
			
			
			String origin = "";
			if(StringUtils.isNotBlank(warningNotice.getManualSerialNo())){
				origin = DocumentType.ORIGIN_MANUAL;
			}else
			{
				origin = DocumentType.ORIGIN_AUTOMATIC;
			}
			
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Document Origin: " + origin));
		}
		
		if (warningNotice.getNewStatusId() != null && warningNotice.getNewStatusId().equals(Constants.DocumentStatus.SERVED)) 
		{
			StaffUserMappingCriteriaBO crit = new StaffUserMappingCriteriaBO();
			crit.setStaffId(warningNotice.getServedByUserID());
			
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
			
			if(warningNotice.getServedOnDate() != null)
				servedDate = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  warningNotice.getServedOnDate());
			
			comment.append(StringUtil.appendSemiColon(comment.toString(),"Served Date: " + servedDate));
		}
		
		
		eventAuditBO.setComment(comment.toString());
		
		return eventAuditBO;
	}

	private EventAuditDO createEventAuditRecordVerify(WarningNoticeBO warningNotice) {

		EventAuditDO eventAuditBO = new EventAuditDO();

		eventAuditBO.setEventCode(Constants.EventCode.VERIFY_DOCUMENT);
		eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.VERIFY_DOCUMENT));

		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.DOCUMENT_TYPE);
		eventAuditBO.setRefValue1(warningNotice.getDocumentTypeName());
			
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_NUMBER);
		eventAuditBO.setRefValue2(warningNotice.getReferenceNo() + "");

		if(StringUtil.isSet(warningNotice.getComment()))
			eventAuditBO.setComment("Comment: " + warningNotice.getNewComment());
		
		eventAuditBO.setAuditEntry(new AuditEntry());

		return eventAuditBO;
	}
	
	private EventAuditDO createEventAuditRecordDenyVerify(WarningNoticeBO summon) {

		EventAuditDO eventAuditBO = new EventAuditDO();

		eventAuditBO.setEventCode(Constants.EventCode.REJECT_DOCUMENT);
		eventAuditBO.setEvent(new CDEventDO(Constants.EventCode.REJECT_DOCUMENT));

		eventAuditBO.setRefType1Code(Constants.EventRefTypeCode.DOCUMENT_TYPE);
		eventAuditBO.setRefValue1(summon.getDocumentTypeName());
			
		eventAuditBO.setRefType2Code(Constants.EventRefTypeCode.REFERENCE_NUMBER);
		eventAuditBO.setRefValue2(summon.getReferenceNo() + "");

		eventAuditBO.setAuditEntry(new AuditEntry());
		eventAuditBO.setComment("Comment: " + summon.getNewComment());

		return eventAuditBO;
	}

	@Override
	@Transactional(readOnly = true)
	public WarningNoticeBO getWarningNoticeDetailsById(Integer warningNoticeId) {

		if (warningNoticeId == null)
			return null;

		WarningNoticeBO warningNoticeView = this.daoFactory.getWarningNoticeDAO().lookupWarningNoticeById(warningNoticeId);

		if (warningNoticeView == null)
			return null;

		/** Get Road Licence Details */
		/*
		 * if (StringUtils.isNotBlank(warningNoticeView.getVehicle()
		 * .getPlateRegNo())) { RoadLicenceBO roadLicence =
		 * daoFactory.getRoadLicenceDAO() .getRoadLicenceByPlateNo(
		 * warningNoticeView.getVehicle().getPlateRegNo());
		 * warningNoticeView.setRoadLicence(roadLicence); }
		 */

		return warningNoticeView;

	}
	
	@Override
	@Transactional(readOnly = true)
	public NoticeView getNoticeView(WarningNoticeBO warningNoticeBO) {
		NoticeView warningNoticeView = null;
		try{
			if (warningNoticeBO == null)
				return null;
	
			WarningNoticeDO warningNoticeDO = daoFactory.getWarningNoticeDAO().find(WarningNoticeDO.class, warningNoticeBO.getAutomaticSerialNo());
	
			if (warningNoticeDO == null)
				return null;
			
			warningNoticeView = new NoticeView(warningNoticeDO);
	
			List<OffenceBO> offences = daoFactory.getRoadCompliancyDAO().getOffencesForDocument(warningNoticeDO.getWarningNoticeId(), Constants.DocumentType.WARNING_NOTICE,
					warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
				warningNoticeView.setOffenceExcerpt(DocumentViewBO.formatOffences(offences));
	
				/*
				 * if(warningNotice.getListOfWitnesses() != null){ List<PersonBO>
				 * witnesses = warningNotice.getListOfWitnesses();
				 * warningNoticeView.
				 * setWitnessFullName(DocumentViewBO.formatWitnessesNames
				 * (witnesses)); warningNoticeView.setWitnessAddress(DocumentViewBO.
				 * formatWitnessesAddresses(witnesses)); }
				 */
	
			List<VehicleOwnerBO> owners = daoFactory.getRoadCompliancyDAO().findVehicleOwnersByPlate(warningNoticeBO.getVehicle().getPlateRegNo());
			if (owners != null) {

				warningNoticeView.setOwnerFullName(DocumentViewBO.formatOwnersNames(owners));
				warningNoticeView.setOwnerAddress(DocumentViewBO.formatOwnerAddresses(owners));
			}

			List<PersonBO> witnesses = daoFactory.getRoadCompliancyDAO().getWitnessesforCompliance(
				warningNoticeDO.getRoadCheckOffenceOutcome().getRoadCheckOffence().getRoadCheck().getCompliance().getComplianceId());
			warningNoticeView.setWitnessFullName(DocumentViewBO.formatWitnessesNames(witnesses));
			warningNoticeView.setWitnessAddress(DocumentViewBO.formatWitnessesAddresses(witnesses));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return warningNoticeView;

	}

	@Override
	@Transactional(readOnly=true)
	public String getAssociatedWarningNotice(Integer roadCheckOffenceOutcomeId) {
		Integer refSeqNo = this.daoFactory.getWarningNoticeDAO().getAssociatedWarningNotice(roadCheckOffenceOutcomeId);
		if(refSeqNo!=null){			
			return Constants.formatRefSeqNo(refSeqNo);
		}		
		return null;
	}

}
