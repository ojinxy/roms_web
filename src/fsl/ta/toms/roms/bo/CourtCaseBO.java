/**
 * Created By: oanguin
 * Date: Jul 11, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import fsl.ta.toms.roms.dataobjects.CourtCaseDO;

/**
 * @author oanguin Created Date: Jul 11, 2013
 */
public class CourtCaseBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String statusDescription, verdictDescription, courtDetail;

	String currentUserName;

	String caseStatusCode;
	
	String caseStatusDescription;

	Integer verdictCode;

	String comment;
	
	String newComments;

	String courtName;

	String courtCaseNo;//input by user

	Integer summonsId;

	Integer courtCaseId;// auto generated

	String currentUserRegion;

	String nextAppearanceCourtTime;
	Date nextAppearanceCourtDate;
	
	BigDecimal fineAmount;
	
	Integer sentencePeriodYear;
	
	Integer sentencePeriodMonth;
	
	Integer sentencePeriodDay;
	
	String finePaidFlag;
	
	String timeServedFlag;
	
	

	// list of court appearance
	List<CourtAppearanceBO> courtAppearances;

	public CourtCaseBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CourtCaseBO(String statusDescription, String verdictDescription,
			String courtDetail, String currentUserName, String statusCode,
			Integer verdictCode, String comment, Integer courtId,
			String courtCaseNo, Integer summonsId, Integer courtCaseId,
			String currentUserRegion) {
		super();
		this.statusDescription = statusDescription;
		this.verdictDescription = verdictDescription;
		this.courtDetail = courtDetail;
		this.currentUserName = currentUserName;
		this.caseStatusCode = statusCode;
		this.verdictCode = verdictCode;
		this.comment = comment;
		this.courtCaseNo = courtCaseNo;
		this.summonsId = summonsId;
		this.courtCaseId = courtCaseId;
		this.currentUserRegion = currentUserRegion;
	}

	public CourtCaseBO(SummonsBO summons) {
		super();

		this.currentUserName = summons.getCurrentUsername();
		this.summonsId = summons.getAutomaticSerialNo();
		this.courtAppearances = new ArrayList<CourtAppearanceBO>();
	}

	public CourtCaseBO(CourtCaseDO caseInfo) {

		// this.courtId = caseInfo.getCourt().getCourtId();
		// this.courtName = caseInfo.getCourt().getShortDesc();
		this.comment = caseInfo.getComment();
		
		if( caseInfo.getVerdict() != null){
			this.verdictDescription = caseInfo.getVerdict().getVerdict_desc();
			this.verdictCode = caseInfo.getVerdict().getVerdict_code();
		}
		
		this.caseStatusCode = caseInfo.getStatus().getStatusId();
		this.caseStatusDescription = caseInfo.getStatus().getDescription();
		
		this.courtCaseId = caseInfo.getCourtCaseId();
		this.courtCaseNo = caseInfo.getCourtCaseNo();
		this.summonsId = caseInfo.getSummons().getSummonsId();
		this.fineAmount = caseInfo.getFineAmount();
		this.sentencePeriodDay = caseInfo.getSentencePeriodDay();
		this.sentencePeriodMonth = caseInfo.getSentencePeriodMonth();
		this.sentencePeriodYear = caseInfo.getSentencePeriodYear();
		this.finePaidFlag = caseInfo.getFinePaidFlag();
		this.timeServedFlag = caseInfo.getTimeServedFlag();

	}

	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}

	/**
	 * @param statusDescription
	 *            the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	/**
	 * @return the verdictDescription
	 */
	public String getVerdictDescription() {
		return verdictDescription;
	}

	/**
	 * @param verdictDescription
	 *            the verdictDescription to set
	 */
	public void setVerdictDescription(String verdictDescription) {
		this.verdictDescription = verdictDescription;
	}

	/**
	 * @return the courtDetail
	 */
	public String getCourtDetail() {
		return courtDetail;
	}

	/**
	 * @param courtDetail
	 *            the courtDetail to set
	 */
	public void setCourtDetail(String courtDetail) {
		this.courtDetail = courtDetail;
	}

	/**
	 * @return the currentUserName
	 */
	public String getCurrentUserName() {
		return currentUserName;
	}

	/**
	 * @param currentUserName
	 *            the currentUserName to set
	 */
	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	/**
	 * @return the caseStatusCode
	 */
	public String getCaseStatusCode() {
		return caseStatusCode;
	}

	/**
	 * @param caseStatusCode
	 *            the caseStatusCode to set
	 */
	public void setCaseStatusCode(String caseStatusCode) {
		this.caseStatusCode = caseStatusCode;
	}

	/**
	 * @return the caseStatusDescription
	 */
	public String getCaseStatusDescription() {
		return caseStatusDescription;
	}

	/**
	 * @param caseStatusDescription
	 *            the caseStatusDescription to set
	 */
	public void setCaseStatusDescription(String caseStatusDescription) {
		this.caseStatusDescription = caseStatusDescription;
	}

	/**
	 * @return the courtName
	 */
	public String getCourtName() {
		return courtName;
	}

	/**
	 * @param courtName
	 *            the courtName to set
	 */
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	/**
	 * @return the nextAppearanceCourtTime
	 */
	public String getNextAppearanceCourtTime() {
		return nextAppearanceCourtTime;
	}

	/**
	 * @param nextAppearanceCourtTime
	 *            the nextAppearanceCourtTime to set
	 */
	public void setNextAppearanceCourtTime(String nextAppearanceCourtTime) {
		this.nextAppearanceCourtTime = nextAppearanceCourtTime;
	}

	/**
	 * @return the nextAppearanceCourtDate
	 */
	public Date getNextAppearanceCourtDate() {
		return nextAppearanceCourtDate;
	}

	/**
	 * @param nextAppearanceCourtDate
	 *            the nextAppearanceCourtDate to set
	 */
	public void setNextAppearanceCourtDate(Date nextAppearanceCourtDate) {
		this.nextAppearanceCourtDate = nextAppearanceCourtDate;
	}

	/**
	 * @return the verdictCode
	 */
	public Integer getVerdictCode() {
		return verdictCode;
	}

	/**
	 * @param verdictCode
	 *            the verdictCode to set
	 */
	public void setVerdictCode(Integer verdictCode) {
		this.verdictCode = verdictCode;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the courtCaseNo
	 */
	public String getCourtCaseNo() {
		return courtCaseNo;
	}

	/**
	 * @param courtCaseNo
	 *            the courtCaseNo to set
	 */
	public void setCourtCaseNo(String courtCaseNo) {
		this.courtCaseNo = courtCaseNo;
	}

	/**
	 * @return the summonsId
	 */
	public Integer getSummonsId() {
		return summonsId;
	}

	/**
	 * @param summonsId
	 *            the summonsId to set
	 */
	public void setSummonsId(Integer summonsId) {
		this.summonsId = summonsId;
	}

	/**
	 * @return the courtCaseId
	 */
	public Integer getCourtCaseId() {
		return courtCaseId;
	}

	/**
	 * @param courtCaseId
	 *            the courtCaseId to set
	 */
	public void setCourtCaseId(Integer courtCaseId) {
		this.courtCaseId = courtCaseId;
	}

	/**
	 * @return the currentUserRegion
	 */
	public String getCurrentUserRegion() {
		return currentUserRegion;
	}

	/**
	 * @param currentUserRegion
	 *            the currentUserRegion to set
	 */
	public void setCurrentUserRegion(String currentUserRegion) {
		this.currentUserRegion = currentUserRegion;
	}

	/**
	 * @return the newComments
	 */
	public String getNewComments() {
		return newComments;
	}

	/**
	 * @param newComments the newComments to set
	 */
	public void setNewComments(String newComments) {
		this.newComments = newComments;
	}

	/**
	 * @return the courtAppearances
	 */
	public List<CourtAppearanceBO> getCourtAppearances() {
		return courtAppearances;
	}

	/**
	 * @param courtAppearances the courtAppearances to set
	 */
	public void setCourtAppearances(List<CourtAppearanceBO> courtAppearances) {
		this.courtAppearances = courtAppearances;
	}

	public BigDecimal getFineAmount()
	{
		return fineAmount;
	}

	public void setFineAmount(BigDecimal fineAmount)
	{
		this.fineAmount = fineAmount;
	}

	/**
	 * @return the sentencePeriodYear
	 */
	public Integer getSentencePeriodYear() {
		return sentencePeriodYear;
	}

	/**
	 * @param sentencePeriodYear the sentencePeriodYear to set
	 */
	public void setSentencePeriodYear(Integer sentencePeriodYear) {
		this.sentencePeriodYear = sentencePeriodYear;
	}

	/**
	 * @return the sentencePeriodMonth
	 */
	public Integer getSentencePeriodMonth() {
		return sentencePeriodMonth;
	}

	/**
	 * @param sentencePeriodMonth the sentencePeriodMonth to set
	 */
	public void setSentencePeriodMonth(Integer sentencePeriodMonth) {
		this.sentencePeriodMonth = sentencePeriodMonth;
	}

	/**
	 * @return the sentencePeriodDay
	 */
	public Integer getSentencePeriodDay() {
		return sentencePeriodDay;
	}

	/**
	 * @param sentencePeriodDay the sentencePeriodDay to set
	 */
	public void setSentencePeriodDay(Integer sentencePeriodDay) {
		this.sentencePeriodDay = sentencePeriodDay;
	}

	/**
	 * @return the finePaidFlag
	 */
	public String getFinePaidFlag() {
		return finePaidFlag;
	}

	/**
	 * @param finePaidFlag the finePaidFlag to set
	 */
	public void setFinePaidFlag(String finePaidFlag) {
		this.finePaidFlag = finePaidFlag;
	}

	/**
	 * @return the timeServedFlag
	 */
	public String getTimeServedFlag() {
		return timeServedFlag;
	}

	/**
	 * @param timeServedFlag the timeServedFlag to set
	 */
	public void setTimeServedFlag(String timeServedFlag) {
		this.timeServedFlag = timeServedFlag;
	}

}