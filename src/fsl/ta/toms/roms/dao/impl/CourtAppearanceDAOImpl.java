/**
 * Created By: oanguin
 * Date: Apr 26, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.VERDICT;
import fsl.ta.toms.roms.dao.CourtAppearanceDAO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.VerdictDAO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CDCourtRulingDO;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.CDEventRefTypeDO;
import fsl.ta.toms.roms.dataobjects.CDPleaDO;
import fsl.ta.toms.roms.dataobjects.CDVerdictDO;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.dataobjects.CourtCaseDO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtAppearanceCriteriaBO;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.StringUtil;


/**
 * @author oanguin Created Date: Apr 26, 2013
 */
public class CourtAppearanceDAOImpl extends ParentDAOImpl implements CourtAppearanceDAO {

	@Autowired
	DAOFactory	daoFactory;

	/******************************* SAVE COURT APPEARANCE *******************************/

	@Override
	public void createNewCourtAppearance(CourtAppearanceBO trial) throws ErrorSavingException {

		AuditEntry auditEntry = new AuditEntry();
		auditEntry.setCreateUsername(trial.getCurrentUserName());
		auditEntry.setCreateDTime(Calendar.getInstance().getTime());

		CourtAppearanceDO trialToSave = new CourtAppearanceDO(trial);
		trialToSave.setAuditEntry(auditEntry);

		try {
			this.save(trial);

		} catch (Exception exe) {
			exe.printStackTrace();
			throw new ErrorSavingException(exe.getMessage());
		}

	}

	@Override
	public Serializable save(Object entity) {
		CourtAppearanceBO courtAppearanceBO = (CourtAppearanceBO) entity;

		CourtCaseDO courtCase = (CourtCaseDO) hibernateTemplate.get(CourtCaseDO.class, courtAppearanceBO.getCourtCaseId());

		SummonsDO summons = (SummonsDO) hibernateTemplate.get(SummonsDO.class, courtCase.getSummons().getSummonsId());

		/*
		 * boolean allCourtAppearanceClosed =
		 * allCourtAppearancesForSummonsClosed(summons .getSummonsId());
		 */
		if (isCaseOpen(summons.getSummonsId())) {

			Date court_date_time = null;

			CourtAppearanceDO courtAppearanceDO = new CourtAppearanceDO();

			//hard code set time to 10 am
			courtAppearanceBO.getCourtDate().setHours(10);
			courtAppearanceDO.setCourtDTime(courtAppearanceBO.getCourtDate());

			courtAppearanceDO.setComment(courtAppearanceBO.getComment());

			if (courtAppearanceBO.getCourtId() != null && courtAppearanceBO.getCourtId() > 0)
				courtAppearanceDO.setCourt((CourtDO) hibernateTemplate.get(CourtDO.class, courtAppearanceBO.getCourtId()));
			/*
			 * else courtAppearanceDO.setCourt((CourtDO) hibernateTemplate.get(
			 * CourtDO.class, courtCase.getCourt().getCourtId()));
			 */// courtAppearanceDO.setCourtDtime(court_date_time);

			CDCourtRulingDO courtRuling = null;
			if (courtAppearanceBO.getCourtRulingId() == null || courtAppearanceBO.getCourtRulingId().isEmpty()) {
				courtRuling = null;
			} else {
				courtRuling = (CDCourtRulingDO) hibernateTemplate.get(CDCourtRulingDO.class, courtAppearanceBO.getCourtRulingId().toUpperCase());

				courtAppearanceDO.setCourtRuling(courtRuling);
			}

			if (courtAppearanceBO.getPleaId() != null && courtAppearanceBO.getPleaId() > 0)
				courtAppearanceDO.setPlea((CDPleaDO) hibernateTemplate.get(CDPleaDO.class, courtAppearanceBO.getPleaId()));

			/*
			 * Check if Court Ruling is Final if So Update Court Status on
			 * Summons and CourtAppearance record to Closed.
			 */
			if (courtRuling != null && courtRuling.getFinalRuling() != null && Character.toLowerCase(courtRuling.getFinalRuling()) == 'y') {
				/* Check if a verdict is provided for the final court ruling */
				CDVerdictDO verdict = this.hibernateTemplate.get(CDVerdictDO.class, courtAppearanceBO.getVerdictId());

				if (verdict == null)
					throw new RuntimeException("A verdict must be set for a final court ruling.");

				StatusDO courtCaseStatus = (StatusDO) hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_CLOSED);

				courtCase.setStatus(courtCaseStatus);
				courtCase.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
				courtCase.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				courtCase.setVerdict(verdict);

				courtCase.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
				courtCase.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());

				courtAppearanceDO.setStatus((StatusDO) hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_CLOSED));

			} else if (courtRuling != null) {
				// If there is a non-final court ruling close trial but not
				// court case.
				courtAppearanceDO.setStatus((StatusDO) hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_CLOSED));
			} else {
				// If no ruling has been set then keep trial open.
				courtAppearanceDO.setStatus((StatusDO) hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_OPEN));
			}

			courtAppearanceDO.setCourtCase(courtCase);

			AuditEntry auditEntry = new AuditEntry();
			auditEntry.setCreateUsername(courtAppearanceBO.getCurrentUserName());
			auditEntry.setCreateDTime(Calendar.getInstance().getTime());

			courtAppearanceDO.setAuditEntry(auditEntry);

			super.save(courtAppearanceDO);

			hibernateTemplate.merge(courtCase);

			courtAppearanceBO.setCourtAppearanceId(courtAppearanceDO.getCourtAppearanceId());

			courtAppearanceBO.setSummonsId(summons.getSummonsId());

			this.createCourtAppearanceEventAudit(courtAppearanceBO, auditEntry,Constants.EventCode.CREATE_COURT_APPEARANCE);

		} else {
			if (!isCaseOpen(summons.getSummonsId())) {
				throw new RuntimeException("The court case related to the summons is not open therefore no new court appearances can be created.");
			}/*
			 * else { throw new RuntimeException(
			 * "All court appearances must be closed before creating a new court appearance."
			 * ); }
			 */

		}

		return null;
	}

	/******************************* UPDATE *************************************/

	@Override
	/**
	 * @author oanguin
	 * @throws ErrorSavingException
	 * @param <code>CourtAppearanceBO currentCourtAppearance</code> Contains information of trial to be updated.
	 * @param <code>CourtAppearanceBO mewCourtAppearance</code> Contains information on the new trial date.
	 */
	public void updateCourtAppearance(CourtAppearanceBO currentCourtAppearance, CourtAppearanceBO newCourtAppearance) throws ErrorSavingException, RequiredFieldMissingException, InvalidFieldException {

		checkIfCourtAppearanceIsUpdatable(currentCourtAppearance);

		/* Check for Required Missing Fields */
		String errorMessage = "";

		if (currentCourtAppearance.getCourtAppearanceId() == null || currentCourtAppearance.getCourtAppearanceId() < 0) {
			errorMessage = "Please provide CourtAppearance ID/ Court Case ID.";
		}

		if (newCourtAppearance != null && (newCourtAppearance.getCourtDate() == null || newCourtAppearance.getCourtTime().isEmpty())) {
			if (!errorMessage.isEmpty())
				errorMessage += "\n";

			errorMessage += "A court date and time must be provided if you are creating a new court date.";

		}

		/*
		 * if (!errorMessage.isEmpty()) { throw new
		 * RequiredFieldMissingException(errorMessage); }
		 */
		/*
		 * Check to see if a new CourtAppearance Should be saved based on
		 * criteria and if so if New CourtAppearance Date is valid.
		 */
		// validateCourtAppearances(currentCourtAppearance, newCourtAppearance);
		validateCourtAppearances(currentCourtAppearance);

		this.update(currentCourtAppearance);

		/*
		 * Session session = hibernateTemplate.getSessionFactory()
		 * .getCurrentSession(); CDPleaDO plea = null; if (
		 * currentCourtAppearance.getPleaId() != null &&
		 * currentCourtAppearance.getPleaId() > 0) plea = (CDPleaDO)
		 * session.load(CDPleaDO.class, currentCourtAppearance.getPleaId());
		 * CDCourtRulingDO courtRuling = null; if
		 * (StringUtil.isSet(currentCourtAppearance.getCourtRulingId()))
		 * courtRuling = (CDCourtRulingDO) session.load(CDCourtRulingDO.class,
		 * currentCourtAppearance.getCourtRulingId().toUpperCase()); Create new
		 * trial date if newCourtAppearance is not null if (newCourtAppearance
		 * != null) { try { this.save(newCourtAppearance); } catch (Exception
		 * exe) { throw new ErrorSavingException(exe.getMessage()); } }
		 */

	}

	/*
	 * (non-Javadoc)
	 * @see fsl.dao.DAO#update(java.lang.Object)
	 */
	@Override
	public void update(Object entity) {
		
		CourtAppearanceBO courtAppearanceBO = (CourtAppearanceBO) entity;

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();

		CourtAppearanceDO courtAppearanceDO = (CourtAppearanceDO) session.get(CourtAppearanceDO.class, courtAppearanceBO.getCourtAppearanceId());
		CourtCaseDO courtCase = courtAppearanceDO.getCourtCase();
		
		/*OAnguin - 14 September 2015
		 * Set plea in court case DO if a plea exists */
		if(courtAppearanceBO.getPleaId() != null && courtAppearanceBO.getPleaId() > 0){
			courtAppearanceDO.setPlea((CDPleaDO)session.get(CDPleaDO.class, courtAppearanceBO.getPleaId()));
		}

		CDCourtRulingDO courtRuling = null;
		CDVerdictDO verdict = null;

		SummonsDO summons = (SummonsDO) session.get(SummonsDO.class, courtCase.getSummons().getSummonsId());
		// courtAppearanceDO.setSummons(summons);

		if (courtCase.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.COURT_CASE_OPEN) && courtAppearanceDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.COURT_CASE_OPEN)) {

			if (StringUtil.isSet(courtAppearanceBO.getCourtRulingId())
				&& !courtAppearanceBO.getCourtRulingId().equalsIgnoreCase(courtAppearanceDO.getCourtRuling() == null ? null : courtAppearanceDO.getCourtRuling().getRulingId())) {
				courtRuling = (CDCourtRulingDO) session.load(CDCourtRulingDO.class, courtAppearanceBO.getCourtRulingId().toUpperCase());

				courtAppearanceDO.setCourtRuling(courtRuling);

				courtAppearanceDO.setStatus((StatusDO) hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_CLOSED));

				/*
				 * StatusDO summonsCourtStatus = (StatusDO)
				 * hibernateTemplate.get( StatusDO.class,
				 * Constants.DocumentStatus.CLOSED);
				 * summons.setStatus(summonsCourtStatus);//set the state of the
				 * summons to closed
				 */
				/*
				 * Check if the court ruling has been changed to a final court
				 * ruling then change the case status state of the court case
				 */
				if (courtRuling.getFinalRuling().compareTo('Y') == 0) {

					//CDVerdictDO verdict = this.hibernateTemplate.get(CDVerdictDO.class, courtAppearanceBO.getVerdictId());

					//courtCase.setVerdict(verdict);

					courtCase.setStatus(this.hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_CLOSED));

					courtCase.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
					courtCase.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				}

			}

//			if ((courtAppearanceDO.getComment() == null && courtAppearanceBO.getComment() != null) || (courtAppearanceDO.getComment() != null && courtAppearanceBO.getComment() == null)
//				|| (courtAppearanceBO.getComment() != null && courtAppearanceDO.getComment() != null && !courtAppearanceDO.getComment().equalsIgnoreCase(courtAppearanceBO.getComment()))) {
//				if (changeComments.length() > 0)
//					changeComments.append(";");
//
//				changeComments.append("{Comments were changed.}");
//				courtAppearanceDO.setComment(courtAppearanceBO.getComment());
//			}

//			if ((courtAppearanceBO.getPleaId() != null && courtAppearanceBO.getPleaId() > 0)
//				&& !courtAppearanceBO.getPleaId().equals(courtAppearanceDO.getPlea() == null ? -1 : courtAppearanceDO.getPlea().getPleaId())) {
//				if (changeComments.length() > 0)
//					changeComments.append(";");
//
//				changeComments.append(String.format("{Plea:(ov:%s)-(nv:%s)}", courtAppearanceDO.getPlea() != null ? courtAppearanceDO.getPlea().getDescription() : "", courtAppearanceBO
//					.getPleaDescription()));
//
//				courtAppearanceDO.setPlea((CDPleaDO) session.load(CDPleaDO.class, courtAppearanceBO.getPleaId()));
//
//			}

			/*
			 * Check if Court Ruling is Final if So Update Court Status on
			 * Summons and CourtAppearance record to Closed.
			 */
			if (courtRuling != null && Character.toLowerCase(courtRuling.getFinalRuling()) == 'y') {
				StatusDO courtStatus = (StatusDO) hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_CLOSED);

				StatusDO summonsCourtStatus = (StatusDO) hibernateTemplate.get(StatusDO.class, Constants.DocumentStatus.CLOSED);

				summons.setStatus(summonsCourtStatus);// set the state of the
														// summons to closed
				summons.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
				summons.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());

				courtAppearanceDO.setStatus(courtStatus);

				// createCourtAppearanceEventAudit(courtAppearanceBO,
				// summons.getAuditEntry(), changeComments.toString(),
				// Constants.EventCode.UPDATE_COURT_APPEARANCE);

			}

			// if the court case is withdrawn then close the summons
			if (courtAppearanceBO != null && courtAppearanceBO.getVerdictId() != null && courtAppearanceBO.getVerdictId().equals(VERDICT.WITHDRAWN)) {

				StatusDO courtStatus = (StatusDO) hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_WITHDRAWN);

				StatusDO summonsCourtStatus = (StatusDO) hibernateTemplate.get(StatusDO.class, Constants.DocumentStatus.CLOSED);

				/*CDVerdictDO verdict = this.hibernateTemplate.get(CDVerdictDO.class, courtAppearanceBO.getVerdictId());

				if (verdict == null)
					throw new RuntimeException("A verdict must be set for a final court ruling.");*/

				summons.setStatus(summonsCourtStatus);// set the state of the
														// summons to closed
				summons.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
				summons.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());

				courtAppearanceDO.setStatus(courtStatus);

				//courtCase.setVerdict(verdict);
				courtCase.setStatus(courtStatus);

			}

//			if (changeComments.length() > 0) {
				AuditEntry auditEntry = new AuditEntry();
				auditEntry.setCreateUsername(courtAppearanceBO.getCurrentUserName());
				auditEntry.setCreateDTime(Calendar.getInstance().getTime());

				courtAppearanceBO.setSummonsId(summons.getSummonsId());

				createCourtAppearanceEventAudit(courtAppearanceBO, auditEntry, Constants.EventCode.UPDATE_COURT_APPEARANCE);

				/*
				 * Assume some change was made to the DO so update audit for
				 * record changed By and changed date.
				 */
				courtAppearanceDO.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
				courtAppearanceDO.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
//			}
			
			if(courtAppearanceBO.getCourtDate() != null){
				//hard code set time to 10 am
				courtAppearanceBO.getCourtDate().setHours(10);
				courtAppearanceDO.setCourtDTime(courtAppearanceBO.getCourtDate());
			}
			
			if(courtAppearanceBO.getInspectorAttended() != null){
				courtAppearanceDO.setInspectorAttended(courtAppearanceBO.getInspectorAttended());
			}
			
			if(StringUtils.isNotEmpty(courtAppearanceBO.getComment())){
				courtAppearanceDO.setComment(courtAppearanceBO.getComment());
			}
			
			if (courtAppearanceBO.getVerdictId() != null)
				verdict = this.hibernateTemplate.get(CDVerdictDO.class, courtAppearanceBO.getVerdictId());
			
			if(verdict == null)
				courtCase.setVerdict(null);
			else
				courtCase.setVerdict(verdict);

			session.update(courtAppearanceDO);
			session.update(courtCase);
			session.update(summons);
		} else {
			throw new RuntimeException("You cannot update court appearance information on a withdrawn, cancelled or closed court appearance.");
		}

	}

	/**
	 * This function checks if a trial object is updatable based on the business
	 * rules.
	 * <ul>
	 * <li>If a trial is Open all fields can be updated except for Summons ID.</li>
	 * <li>If a trial is closed only plea id, comment and court ruling can be
	 * updated.</li>
	 * <li>Only the last trial can be updated.</li>
	 * </ul>
	 * 
	 * @param CourtAppearanceBO
	 * @throws InvalidFieldException
	 */
	private void checkIfCourtAppearanceIsUpdatable(CourtAppearanceBO courtAppearanceBO) throws ErrorSavingException, InvalidFieldException {
		/* Check if its the last record. */
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();

		CourtAppearanceDO courtAppearanceDO = (CourtAppearanceDO) session.get(CourtAppearanceDO.class, courtAppearanceBO.getCourtAppearanceId());

		CourtAppearanceDO latestCourtAppearance = null;

		latestCourtAppearance = this.getMostRecentCourtAppearanceBySummonsID(courtAppearanceDO.getCourtCase().getSummons().getSummonsId());

		if (courtAppearanceDO.getCourtAppearanceId().compareTo(latestCourtAppearance.getCourtAppearanceId()) != 0) {
			throw new ErrorSavingException("You are only able to update the latest court appearance on a court case.");
		}

		/* Court Case ID should never be editable */
		if (courtAppearanceBO.getCourtCaseId().compareTo(courtAppearanceDO.getCourtCase().getCourtCaseId()) != 0) {
			throw new ErrorSavingException("Court Case ID is not an editable field.");
		}

		/* Check if court appearance is closed */
		if (courtAppearanceDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.COURT_CASE_CLOSED)) {

			/*
			 * try { CourtAppearanceBODateTime =
			 * DateUtils.combineDateTime(courtAppearanceBO.getCourtDate(),
			 * courtAppearanceBO.getCourtTime()); } catch (Exception exe) {
			 * exe.printStackTrace(); throw new InvalidFieldException(); }
			 */
			// All business logics for stopping updates on closed court
			// appearance
			if (courtAppearanceBO.getCourtId() != null && courtAppearanceDO.getCourt().getCourtId().compareTo(courtAppearanceBO.getCourtId()) != 0)
				throw new ErrorSavingException("Court ID is not an editable field for closed court appearance.");

			if (courtAppearanceBO.getCourtRulingId() != null && courtAppearanceDO.getCourtRuling().getRulingId().toLowerCase().compareTo(courtAppearanceBO.getCourtRulingId().toLowerCase()) != 0)
				throw new ErrorSavingException("Court Ruling is not an editable field for closed court appearance.");

			if (courtAppearanceDO.getCourtDTime() != null && courtAppearanceDO.getCourtDTime().compareTo(courtAppearanceDO.getCourtDTime()) != 0)
				throw new ErrorSavingException("Court Date is not an editable field for closed court appearance.");

			if (courtAppearanceBO.getPleaId() != null && courtAppearanceDO.getPlea().getPleaId().compareTo(courtAppearanceBO.getPleaId()) != 0)
				throw new ErrorSavingException("Plea is not an editable field for closed court appearance.");

			if (courtAppearanceDO.getComment() != null && courtAppearanceDO.getComment().compareTo(courtAppearanceDO.getComment()) != 0)
				throw new ErrorSavingException("Comment is not an editable field for closed court appearance.");
		}

		/* Check if court case is closed */
		if (courtAppearanceDO.getCourtCase().getStatus().getStatusId().compareToIgnoreCase(Constants.Status.COURT_CASE_OPEN) != 0)
			throw new ErrorSavingException("Court appearances can only be updated where the court case is open.");

	}

	/**
	 * This function <b>validates</b> trials based on a series of business
	 * rules.
	 * 
	 * @param currentCourtAppearance
	 * @param newCourtAppearance
	 * @throws ErrorSavingException
	 */
	private void validateCourtAppearances(CourtAppearanceBO currentCourtAppearance, CourtAppearanceBO newCourtAppearance) throws ErrorSavingException {

		CDCourtRulingDO currentCourtRuling = null;
		CDCourtRulingDO newCourtRuling = null;

		CDVerdictDO currentVerdict = null;
		CDVerdictDO newVerdict = null;

		CDPleaDO currentPlea = null;

		if (currentCourtAppearance.getVerdictId() != null)
			currentVerdict = this.hibernateTemplate.get(CDVerdictDO.class, currentCourtAppearance.getVerdictId());

		if (newCourtAppearance != null && newCourtAppearance.getVerdictId() != null)
			newVerdict = this.hibernateTemplate.get(CDVerdictDO.class, newCourtAppearance.getVerdictId());

		if (currentCourtAppearance.getCourtRulingId() != null)
			currentCourtRuling = this.hibernateTemplate.get(CDCourtRulingDO.class, currentCourtAppearance.getCourtRulingId());

		if (newCourtAppearance != null && newCourtAppearance.getCourtRulingId() != null)
			newCourtRuling = this.hibernateTemplate.get(CDCourtRulingDO.class, newCourtAppearance.getCourtRulingId());

		if (currentCourtAppearance.getPleaId() != null)
			currentPlea = this.hibernateTemplate.get(CDPleaDO.class, currentCourtAppearance.getPleaId());

		if (currentCourtRuling != null && currentCourtRuling.getFinalRuling().toString().trim().equalsIgnoreCase("y") && newCourtAppearance != null) {
			throw new ErrorSavingException("No new court appearance date can be set for a final court ruling.");
		} else if (currentCourtRuling != null && currentPlea != null && currentCourtRuling.getFinalRuling().toString().trim().equalsIgnoreCase("n")
			&& currentPlea.getDescription().toLowerCase().contains("not guilty") && newCourtAppearance == null) {
			throw new ErrorSavingException("A new court appearance is mandatory because the plea is not guilty and the court ruling is non-final.");
		}

		if (currentCourtAppearance != null && newCourtAppearance != null) {
			// Compare date to ensure that new CourtAppearance has a later date
			// than the current trial.

			/*
			 * try { String current_court_date_part = DateUtils.formatDate(
			 * "yyyy-MM-dd", currentCourtAppearance.getCourtDate());
			 * java.util.Date current_court_date = new SimpleDateFormat(
			 * "yyyy-MM-dd HH:mm:ss").parse(current_court_date_part + " " +
			 * currentCourtAppearance.getCourtTime()); String
			 * new_court_date_part = DateUtils.formatDate("yyyy-MM-dd",
			 * newCourtAppearance.getCourtDate()); java.util.Date new_court_date
			 * = new SimpleDateFormat(
			 * "yyyy-MM-dd HH:mm:ss").parse(new_court_date_part + " " +
			 * newCourtAppearance.getCourtTime()); if
			 * (new_court_date.compareTo(current_court_date) <= 0) throw new
			 * ErrorSavingException(
			 * "New court appearance date cannot be before the current court appearance date being updated."
			 * ); } catch (ParseException e) { e.printStackTrace(); throw new
			 * ErrorSavingException(
			 * "Please ensure date and time are in the correct formats."); }
			 */
			currentCourtAppearance.setStatusCode(Constants.Status.COURT_CASE_CLOSED);
		}

		/*
		 * Check if a court ruling is final and if so that a verdict code has
		 * been set for that final court ruling
		 */
//		if (currentCourtRuling != null && currentCourtRuling.getFinalRuling().toString().trim().equalsIgnoreCase("y")) {
//			if (currentVerdict == null)
//				throw new ErrorSavingException("You need to enter a verdict for final court rulings.");
//		}
//
//		if (newCourtRuling != null && newCourtRuling.getFinalRuling().toString().trim().equalsIgnoreCase("y")) {
//			if (newVerdict == null)
//				throw new ErrorSavingException("You need to enter a verdict for final court rulings.");
//		}

	}

	private void validateCourtAppearances(CourtAppearanceBO currentCourtAppearance) throws ErrorSavingException {

		CDCourtRulingDO currentCourtRuling = null;
		// CDCourtRulingDO newCourtRuling = null;

		CDVerdictDO currentVerdict = null;
		CDVerdictDO newVerdict = null;

		CDPleaDO currentPlea = null;

		if (currentCourtAppearance.getVerdictId() != null)
			currentVerdict = this.hibernateTemplate.get(CDVerdictDO.class, currentCourtAppearance.getVerdictId());

		if (StringUtils.isNotBlank(currentCourtAppearance.getCourtRulingId()))
			currentCourtRuling = this.hibernateTemplate.get(CDCourtRulingDO.class, currentCourtAppearance.getCourtRulingId());

		if (currentCourtAppearance.getPleaId() != null)
			currentPlea = this.hibernateTemplate.get(CDPleaDO.class, currentCourtAppearance.getPleaId());

		/*
		 * Check if a court ruling is final and if so that a verdict code has
		 * been set for that final court ruling
		 */
//		if (currentCourtRuling != null && currentCourtRuling.getFinalRuling().toString().trim().equalsIgnoreCase("y")) {
//			if (currentVerdict == null)
//				throw new ErrorSavingException("You need to enter a verdict for final court rulings.");
//		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see fsl.ta.toms.roms.dao.CourtAppearanceDAO#allCourtAppearancesForSummonsClosed(fsl.ta.toms.roms.dataobjects.SummonsDO)
	 */
	@Override
	public boolean allCourtAppearancesForSummonsClosed(Integer summonsId) {
		boolean allCasesClosed;

		Criteria searchCriteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(CourtAppearanceDO.class, "t");
		searchCriteria.createAlias("t.courtCase", "cc");

		searchCriteria.add(Restrictions.eq("t.status.statusId", "cc_ope").ignoreCase());
		searchCriteria.add(Restrictions.eq("cc.summons.summonsId", summonsId));

		List<?> results = searchCriteria.list();
		if (results != null && results.size() > 0) {
			allCasesClosed = false;
		} else {
			allCasesClosed = true;
		}

		return allCasesClosed;

	}

	/**
	 * This function returns true of the court case for the summons is open
	 * otherwise false is returned.
	 * 
	 * @param summonsId
	 * @return
	 */
	public boolean isCaseOpen(Integer summonsId) {
		boolean courtCaseOpen = false;

		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(CourtCaseDO.class, "cc");

		criteria.add(Restrictions.eq("cc.summons.summonsId", summonsId));
		criteria.add(Restrictions.eq("cc.status.statusId", Constants.Status.COURT_CASE_OPEN).ignoreCase());

		List<?> results = criteria.list();

		if (results != null && results.size() > 0) {
			courtCaseOpen = true;
		} else {
			courtCaseOpen = false;
		}

		return courtCaseOpen;

	}

	/**
	 * Gets the most recent CourtAppearance by summon Id
	 * 
	 * @author jreid
	 * @param summonsID
	 * @return
	 */
	@Override
	public CourtAppearanceDO getMostRecentCourtAppearanceBySummonsID(Integer summonsID) {

		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(CourtAppearanceDO.class, "ca");

		criteria.createAlias("ca.courtCase", "cc");

		criteria.add(Restrictions.eq("cc.summons.summonsId", summonsID));

		criteria.addOrder(Order.desc("ca.courtDTime"));
		criteria.addOrder(Order.desc("ca.courtAppearanceId"));

		criteria.setMaxResults(1);

		List<CourtAppearanceDO> trials = criteria.list();

		if (trials != null && !trials.isEmpty()) {
			return trials.get(0);
		}

		return null;
	}

	/**
	 * Gets the CourtAppearance by Id
	 * 
	 * @author jreid
	 * @param summonsID
	 * @return
	 */
	@Override
	public CourtAppearanceBO getCourtAppearanceByID(Integer appearanceId) {
		if (appearanceId == null)
			return null;

		CourtAppearanceDO ca = this.hibernateTemplate.load(CourtAppearanceDO.class, appearanceId);
		CourtAppearanceBO bo = null;
		if (ca != null) {
			bo = new CourtAppearanceBO(ca);
		}

		return bo;
	}

	/**
	 * Gets the First CourtAppearance by summon Id
	 * 
	 * @author jreid
	 * @param summonsID
	 * @return
	 */
	@Override
	public CourtAppearanceDO getFirstCourtAppearanceBySummonsID(Integer summonsID) {

		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(CourtAppearanceDO.class);
		//System.err.println(" Summons Id ;" + summonsID + ";");

		criteria.createAlias("courtCase", "cc");

		criteria.add(Restrictions.eq("cc.summons.summonsId", summonsID));

		// criteria.add(Restrictions.eq("courtCase.summons.summonsId",
		// summonsID));

		// criteria.addOrder(Order.asc("courtDTime"));

		// criteria.setMaxResults(1);

		@SuppressWarnings("unchecked")
		List<CourtAppearanceDO> trials = (List<CourtAppearanceDO>) criteria.list();

		if (trials != null && !trials.isEmpty()) {
			return trials.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CourtAppearanceBO> lookUpCourtAppearance(CourtAppearanceCriteriaBO trialCriteria) throws InvalidFieldException {

		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(CourtAppearanceDO.class);

		criteria.createAlias("courtCase", "cc");

		criteria.addOrder(Order.desc("courtDTime"));

		Criterion criterion = null;

		if (trialCriteria.getCourtDate() != null) {

			if (StringUtil.isSet(trialCriteria.getCourtTime())) {
				java.util.Date trialSearchDate = null;
				try {
					trialSearchDate = DateUtils.combineDateTime(trialCriteria.getCourtDate(), trialCriteria.getCourtTime());
				} catch (Exception exe) {
					exe.printStackTrace();

					throw new InvalidFieldException();
				}

				if (trialSearchDate != null) {
					criterion = Restrictions.eq("courtDTime", trialSearchDate);
				}

			} else {
				criterion = Restrictions.between("courtDTime", DateUtils.searchDateFormater(trialCriteria.getCourtDate(), DateUtils.SEARCHDATETYPE.START),
					DateUtils.searchDateFormater(trialCriteria.getCourtDate(), DateUtils.SEARCHDATETYPE.END));
			}

		}

		if (StringUtil.isSet(trialCriteria.getCourtCaseNo())) {
			Criterion newCriterion = Restrictions.eq("cc.courtCaseNo", trialCriteria.getCourtCaseNo().trim());

			if (criterion != null) {
				criterion = Restrictions.and(criterion, newCriterion);
			} else {
				criterion = newCriterion;
			}
		}

		if (trialCriteria.getCourtId() != null && trialCriteria.getCourtId() > 0) {
			Criterion newCriterion = Restrictions.eq("court.courtId", trialCriteria.getCourtId());

			if (criterion != null) {
				criterion = Restrictions.and(criterion, newCriterion);
			} else {
				criterion = newCriterion;
			}
		}

		if (StringUtils.trimToNull(trialCriteria.getCourtRulingId()) != null) {
			Criterion newCriterion = Restrictions.eq("courtRuling.rulingId", trialCriteria.getCourtRulingId().trim()).ignoreCase();

			if (criterion != null) {
				criterion = Restrictions.and(criterion, newCriterion);
			} else {
				criterion = newCriterion;
			}
		}

		if (StringUtils.trimToNull(trialCriteria.getStatusCode()) != null) {
			Criterion newCriterion = Restrictions.eq("status.statusId", trialCriteria.getStatusCode().trim()).ignoreCase();

			if (criterion != null) {
				criterion = Restrictions.and(criterion, newCriterion);
			} else {
				criterion = newCriterion;
			}
		}

		if (trialCriteria.getPleaId() != null && trialCriteria.getPleaId() > 0) {

			Criterion newCriterion = Restrictions.eq("plea.pleaId", trialCriteria.getPleaId());

			if (criterion != null) {
				criterion = Restrictions.and(criterion, newCriterion);
			} else {
				criterion = newCriterion;
			}

		}

		if (trialCriteria.getSummonsId() != null && trialCriteria.getSummonsId() > 0) {
			Criterion newCriterion = Restrictions.eq("cc.summons.summonsId", trialCriteria.getSummonsId());

			if (criterion != null) {
				criterion = Restrictions.and(criterion, newCriterion);
			} else {
				criterion = newCriterion;
			}

		}

		if (trialCriteria.getCourtAppearanceId() != null && trialCriteria.getCourtAppearanceId() > 0) {
			Criterion newCriterion = Restrictions.eq("courtAppearanceId", trialCriteria.getCourtAppearanceId());

			if (criterion != null) {
				criterion = Restrictions.and(criterion, newCriterion);
			} else {
				criterion = newCriterion;
			}

		}

		if (criterion != null)
			criteria.add(criterion);

		List<CourtAppearanceBO> CourtAppearanceBOList = new ArrayList<CourtAppearanceBO>();
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		for (CourtAppearanceDO courtAppearanceDO : (List<CourtAppearanceDO>) criteria.list()) {

			CourtAppearanceBO CourtAppearanceBO = new CourtAppearanceBO(courtAppearanceDO);

			/*
			 * CourtAppearanceBO CourtAppearanceBO = new CourtAppearanceBO(
			 * courtAppearanceDO.getCourtAppearanceId() trialId ,
			 * courtAppearanceDO.getCourtCase().getSummons() != null ?
			 * courtAppearanceDO .getCourtCase().getSummons().getSummonsId() :
			 * null summonsId , courtAppearanceDO.getCourt() != null ?
			 * courtAppearanceDO .getCourt().getCourtId() : null courtId ,
			 * courtAppearanceDO.getPlea() != null ? courtAppearanceDO
			 * .getPlea().getPleaId() : null pleaId ,
			 * courtAppearanceDO.getCourtRuling() != null ? courtAppearanceDO
			 * .getCourtRuling().getRulingId() : null courtRulingId ,
			 * courtAppearanceDO.getCourtRuling() != null ? courtAppearanceDO
			 * .getCourtRuling().getDescription() : null courtRulingDescription
			 * , courtAppearanceDO.getPlea() != null ? courtAppearanceDO
			 * .getPlea().getDescription() : null pleaDescription ,
			 * courtAppearanceDO.getStatus() != null ? courtAppearanceDO
			 * .getStatus().getDescription() : null statusDescription ,
			 * courtAppearanceDO.getCourtDTime() != null ? timeFormat
			 * .format(courtAppearanceDO.getCourtDTime()) : null courtTime ,
			 * courtAppearanceDO.getComment() comments ,
			 * courtAppearanceDO.getStatus() != null ? courtAppearanceDO
			 * .getStatus().getStatusId() : null statusCode ,
			 * courtAppearanceDO.getCourtDTime() != null ? courtAppearanceDO
			 * .getCourtDTime() : null courtDate
			 */

			// );

			CourtAppearanceBOList.add(CourtAppearanceBO);
		}

		if (CourtAppearanceBOList.size() < 1)
			return null;
		else
			return CourtAppearanceBOList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CourtAppearanceDO> getCourtAppearanceReference(HashMap<String, String> filter, String status) throws InvalidFieldException {

		return (List<CourtAppearanceDO>) super.getReference(CourtAppearanceDO.class, status, filter);
	}

	/***************************** CASE/APPEARANCE OVERRIDE *****************/

	@Override
	/**
	 * @author jreid 
	 * WIll handle override
	 */
	public void overrideCourtAppearance(CourtAppearanceBO overriddenCourtAppearance) throws ErrorSavingException, RequiredFieldMissingException, InvalidFieldException {

		checkIfCourtAppearanceIsOverrideable(overriddenCourtAppearance);

		/* Check for Required Missing Fields */
		String errorMessage = "";

		if (overriddenCourtAppearance.getCourtAppearanceId() == null || overriddenCourtAppearance.getCourtAppearanceId() < 0) {
			errorMessage = "Please provide CourtAppearance ID/ Court Case ID.";
			throw new RequiredFieldMissingException(errorMessage);
		}

		validateCourtAppearances(overriddenCourtAppearance);

		this.override(overriddenCourtAppearance);

	}

	/**
	 * @author jreid
	 * @param courtAppearanceBO
	 * @throws ErrorSavingException
	 * @throws InvalidFieldException
	 */
	private void checkIfCourtAppearanceIsOverrideable(CourtAppearanceBO courtAppearanceBO) throws ErrorSavingException, InvalidFieldException {
		/* Check if its the last record. */
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();

		CourtAppearanceDO courtAppearanceDO = (CourtAppearanceDO) session.get(CourtAppearanceDO.class, courtAppearanceBO.getCourtAppearanceId());

		CourtAppearanceDO latestCourtAppearance = null;

		latestCourtAppearance = this.getMostRecentCourtAppearanceBySummonsID(courtAppearanceDO.getCourtCase().getSummons().getSummonsId());

		if (courtAppearanceDO.getCourtAppearanceId().compareTo(latestCourtAppearance.getCourtAppearanceId()) != 0) {
			throw new ErrorSavingException("You are only able to update the latest court appearance on a court case.");
		}

		/* Court Case ID should never be editable */
		if (courtAppearanceBO.getCourtCaseId().compareTo(courtAppearanceDO.getCourtCase().getCourtCaseId()) != 0) {
			throw new ErrorSavingException("Court Case ID is not an editable field.");
		}

	}

	// @Override
	public void override(Object entity) {
		// StringBuffer changeComments = new StringBuffer();
		CourtAppearanceBO courtAppearanceBO = (CourtAppearanceBO) entity;

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();

		CourtAppearanceDO courtAppearanceDOFromDatabase = (CourtAppearanceDO) session.get(CourtAppearanceDO.class, courtAppearanceBO.getCourtAppearanceId());
		CourtCaseDO courtCase = courtAppearanceDOFromDatabase.getCourtCase();

		CDCourtRulingDO courtRuling = null;
		CDVerdictDO verdict = null;

		SummonsDO summons = (SummonsDO) session.get(SummonsDO.class, courtCase.getSummons().getSummonsId());

		// if the court case is withdrawn then close the summons etc
		if (courtAppearanceBO != null && courtAppearanceBO.getVerdictId() != null && courtAppearanceBO.getVerdictId().equals(VERDICT.WITHDRAWN)) {

			StatusDO courtStatus = (StatusDO) hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_WITHDRAWN);

			StatusDO summonsCourtStatus = (StatusDO) hibernateTemplate.get(StatusDO.class, Constants.DocumentStatus.CLOSED);

			//CDVerdictDO verdict = this.hibernateTemplate.get(CDVerdictDO.class, courtAppearanceBO.getVerdictId());

			/*if (verdict == null)
				throw new RuntimeException("A verdict must be set for a final court ruling.");*/

			summons.setStatus(summonsCourtStatus);// set the state of the
													// summons to closed
			summons.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
			summons.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());

			// update the appearance, set ruling to null 
			//courtAppearanceDOFromDatabase.setCourtRuling(null);
			courtAppearanceDOFromDatabase.setStatus(courtStatus);

			//courtCase.setVerdict(verdict);
			courtCase.setStatus(courtStatus);

		} 
		else{
			if (courtAppearanceBO != null && StringUtils.isNotBlank(courtAppearanceBO.getCourtRulingId())) {
				courtRuling = (CDCourtRulingDO) session.load(CDCourtRulingDO.class, courtAppearanceBO.getCourtRulingId().toUpperCase());
			//if not withdrawn
				/*
				 * Check if the court ruling has been changed to a final court
				 * ruling then change the case status state of the court case
				 * in addition change any fines or prison sentences
				 */
				if (courtRuling.getFinalRuling().compareTo(Constants.YesNo.YES_FLAG) == 0) {
	
					//CDVerdictDO verdict = null;
	
		/*			if (courtAppearanceBO.getVerdictId() != null)
						verdict = this.hibernateTemplate.get(CDVerdictDO.class, courtAppearanceBO.getVerdictId());
	
					if (verdict != null)
						courtCase.setVerdict(verdict);*/
	
					courtCase.setStatus(this.hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_CLOSED));
	
					courtCase.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
					courtCase.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
	
					// update the appearance
					courtAppearanceDOFromDatabase.setStatus((StatusDO) hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_CLOSED));
	
					// update the summons
					StatusDO summonsCourtStatus = (StatusDO) hibernateTemplate.get(StatusDO.class, Constants.DocumentStatus.CLOSED);
	
					summons.setStatus(summonsCourtStatus);// set the state of
															// the
															// summons to closed
					summons.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
					summons.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
	
				} else {
					// court ruling is not final
	
					//courtCase.setVerdict(null);
	
					courtCase.setStatus(this.hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_OPEN));
	
					courtCase.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
					courtCase.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
	
					// update the appearance
					courtAppearanceDOFromDatabase.setStatus((StatusDO) hibernateTemplate.get(StatusDO.class, Constants.Status.COURT_CASE_OPEN));
	
					// update the summons
					StatusDO summonsCourtStatus = (StatusDO) hibernateTemplate.get(StatusDO.class, Constants.DocumentStatus.SERVED);
	
					summons.setStatus(summonsCourtStatus);
	
					summons.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
					summons.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
	
				}
			}
		}
		if (courtAppearanceBO != null && StringUtils.isNotBlank(courtAppearanceBO.getCourtRulingId())) {
			
			courtRuling = (CDCourtRulingDO) session.load(CDCourtRulingDO.class, courtAppearanceBO.getCourtRulingId().toUpperCase());

			// update the appearance
			courtAppearanceDOFromDatabase.setCourtRuling(courtRuling);

			// update the plea
			CDPleaDO plea = null;

			if (courtAppearanceBO.getPleaId() != null)
				plea = this.hibernateTemplate.get(CDPleaDO.class, courtAppearanceBO.getPleaId());

			//if (plea != null)
			courtAppearanceDOFromDatabase.setPlea(plea);

			
			//update he inspector attendance
			courtAppearanceDOFromDatabase.setInspectorAttended(courtAppearanceBO.getInspectorAttended());
			
			
			if (courtAppearanceBO.getVerdictId() != null)
				verdict = this.hibernateTemplate.get(CDVerdictDO.class, courtAppearanceBO.getVerdictId());
			
			if(verdict == null)
				courtCase.setVerdict(null);
			else
				courtCase.setVerdict(verdict);
		
		}
		AuditEntry auditEntry = new AuditEntry();
		auditEntry.setCreateUsername(courtAppearanceBO.getCurrentUserName());
		auditEntry.setCreateDTime(Calendar.getInstance().getTime());

		courtAppearanceBO.setSummonsId(summons.getSummonsId());

		//createCourtAppearanceEventAudit(courtAppearanceBO, auditEntry, "Add comments here ", Constants.EventCode.UPDATE_COURT_APPEARANCE);

		/*
		 * Assume some change was made to the DO so update audit for record
		 * changed By and changed date.
		 */
		courtAppearanceDOFromDatabase.getAuditEntry().setUpdateUsername(courtAppearanceBO.getCurrentUserName());
		courtAppearanceDOFromDatabase.getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
		
		if(courtAppearanceBO.getCourtDate() != null){
			//hard code set time to 10 am
			courtAppearanceBO.getCourtDate().setHours(10);
			courtAppearanceDOFromDatabase.setCourtDTime(courtAppearanceBO.getCourtDate());
		}
		
		if (courtAppearanceBO.getCourtId() != null && courtAppearanceBO.getCourtId() > 0)
			courtAppearanceDOFromDatabase.setCourt((CourtDO) hibernateTemplate.get(CourtDO.class, courtAppearanceBO.getCourtId()));
		
		// update the comment
		if(courtAppearanceBO.getComment() != null)
			courtAppearanceDOFromDatabase.setComment(courtAppearanceBO.getComment());
		
		
//		if(courtAppearanceBO.getCourtId() != null){
//			fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO courtCriteria = new fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO();
//
//			courtCriteria.setCourtId(courtAppearanceBO.getCourtId());
//
//			List<CourtDO> courtList = daoFactory.getCourtDAO().lookupCourt(courtCriteria);
//
//			if (courtList.size() > 0)
//				courtAppearanceDOFromDatabase.setCourt(courtList.get(0));
//		}
		
		

		session.update(courtAppearanceDOFromDatabase);
		session.update(courtCase);
		session.update(summons);

	}

	/****************************** AUDIT *********************************/

	/**
	 * This method is to be used to create a audit record for new
	 * CourtAppearances being created.
	 * 
	 * @param <code>trial</code> This is the trial object which is being saved.
	 * @param <code>auditEntry</code> This is the audit details about the person
	 *        doing the process.
	 * @param <code>comment</code> This takes a comment that is to be appended
	 *        to the audit record. This string should have a semicolon separated
	 *        list of values that were changed. For example {Plea(Field
	 *        Name)(Old Value:old value)-(New Value:new value)}
	 * @return EventAudit
	 */
	private void createCourtAppearanceEventAudit(CourtAppearanceBO trial, AuditEntry auditEntry, Integer eventCode) {
		EventAuditDO eventAudit = new EventAuditDO();
		StringBuffer comment = new StringBuffer();
		
		eventAudit.setAuditEntry(auditEntry);
		// eventAudit.getAuditEntry().setCreateUsername(auditEntry.getCreateUsername());

		eventAudit.setEvent(hibernateTemplate.load(CDEventDO.class, eventCode));
		

		Integer summID = trial.getSummonsId();
		SummonsDO summons = daoFactory.getSummonsDAO().find(SummonsDO.class,summID);
		
		if(summons != null)		
		{
			eventAudit.setRefType1(hibernateTemplate.load(CDEventRefTypeDO.class, Constants.EventRefTypeCode.SUMMONS_ID));
			eventAudit.setRefValue1(Constants.formatRefSeqNo(summons.getReferenceNo()));
		}
		
		
		Integer ccID = trial.getCourtCaseId();
		CourtCaseBO courtCase = daoFactory.getCourtCaseDAO().lookUpCourtCaseBySummonsId(summID);
		
		if (courtCase != null)
		{
			String ccNo = courtCase.getCourtCaseNo();
			
			if(StringUtil.isSet(ccNo))
			{
					eventAudit.setRefType2(hibernateTemplate.load(CDEventRefTypeDO.class, Constants.EventRefTypeCode.COURT_CASE_NUMBER));
					eventAudit.setRefValue2(ccNo);
			}
		}
		
		if(trial.getCourtDate() != null)
		{
			comment.append("Court Date: " + DateUtils.formatDate("yyyy-MM-dd hh:mm a",  trial.getCourtDate()));
		}
		
		
		if(StringUtil.isSet(trial.getPleaDescription()))
		{
			comment.append("; Plea: " + trial.getPleaDescription());
		}
		
		if(trial.getVerdictId() != null)
		{
			CDVerdictDO verdictDO = hibernateTemplate.load(CDVerdictDO.class, trial.getVerdictId());
			
			if(verdictDO != null)
				comment.append("; Verdict: " + verdictDO.getVerdict_desc());
		}
		
		
		if(StringUtil.isSet(trial.getPleaDescription()))
		{
			comment.append("; Court Ruling: " + trial.getCourtRulingDescription());
		}
		
		if(StringUtil.isSet(trial.getPleaDescription()))
		{
			comment.append("; Comment: " + trial.getComment());
		}
		
		eventAudit.setComment(comment.toString());
		
		super.save(eventAudit);
  
	}

	@Override
	public Date getInitialCourtDateByCourtCaseId(Integer courtCaseId) {
		Date courtDate = null;
		java.sql.Timestamp courtDatetimeStamp = null;
		
		String queryString = "Select f.court_date_time "
				+"from roms_court_case a inner join roms_summons b "
				+"on a.summons_id = b.summons_id "
				+"inner join roms_road_check_offence_outcome c "
				+"on b.road_check_offence_outcome_id = c.road_check_offence_outcome_id "
				+"inner join roms_road_check_offence d "
				+"on c.road_check_offence_id = d.road_check_offence_id "
				+"inner join roms_road_check e "
				+"on d.road_check_id = e.road_check_id "
				+"inner join roms_compliance f "
				+"on e.compliance_id = f.compliance_id "
				+"where a.court_case_id = "+courtCaseId;
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
//		Date courtDate = (Date)session.createQuery(queryString.toString()).uniqueResult();
		
		 Query query = session.createSQLQuery(queryString);
		 courtDatetimeStamp = (java.sql.Timestamp)query.uniqueResult();
		 if (courtDatetimeStamp != null)
		 {
			 courtDate = new Date(courtDatetimeStamp.getTime()); 
		 }
		 
		 return courtDate;
	}

}
