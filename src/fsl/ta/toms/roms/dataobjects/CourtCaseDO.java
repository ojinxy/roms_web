/**
 * Created By: oanguin
 * Date: Jul 3, 2013
 *
 */
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.constants.Constants;

/**
 * @author oanguin
 * Created Date: Jul 3, 2013
 */
@Entity
@Table(name="ROMS_court_case")
public class CourtCaseDO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="court_case_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer courtCaseId;
		
	@ManyToOne
	@JoinColumn(name="summons_id")
	SummonsDO summons;
	
	@Column(name="court_case_no")
    String courtCaseNo;
	
	/*@ManyToOne
	@JoinColumn(name="court_id")
	CourtDO court;
	*/
	@ManyToOne
	@JoinColumn(name="verdict_code")
    CDVerdictDO verdict;
		
	@Column(name="fine_amount")
	BigDecimal fineAmount;
	
	@Column(name="sentence_period_year")
	Integer sentencePeriodYear;
	
	@Column(name="sentence_period_month")
	Integer sentencePeriodMonth;
	
	@Column(name="sentence_period_day")
	Integer sentencePeriodDay;
	
	@Column(name="fine_paid_flag")
    String finePaidFlag;
	
	@Column(name="time_served_flag")
    String timeServedFlag;
	
    @Column
    String comment;
    
    @ManyToOne
    @JoinColumn(name="status_id")
	StatusDO status;
    
    
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getCourtCaseId() {
		return courtCaseId;
	}

	public void setCourtCaseId(Integer courtCaseId) {
		this.courtCaseId = courtCaseId;
	}

	public SummonsDO getSummons() {
		return summons;
	}

	public void setSummons(SummonsDO summons) {
		this.summons = summons;
	}

	public String getCourtCaseNo() {
		return courtCaseNo;
	}

	public void setCourtCaseNo(String courtCaseNo) {
		this.courtCaseNo = courtCaseNo;
	}



	public CDVerdictDO getVerdict() {
		return verdict;
	}

	public void setVerdict(CDVerdictDO verdict) {
		this.verdict = verdict;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}	
	
	public BigDecimal getFineAmount()
	{
		return fineAmount;
	}

	public void setFineAmount(BigDecimal fineAmount)
	{
		this.fineAmount = fineAmount;
	}

	public CourtCaseDO(Integer courtCaseId, SummonsDO summons,
			String courtCaseNo, CourtDO court, CDVerdictDO verdict,
			String comment, StatusDO status, AuditEntry auditEntry,
			Integer versionNbr) {
		super();
		this.courtCaseId = courtCaseId;
		this.summons = summons;
		this.courtCaseNo = courtCaseNo;
		//this.court = court;
		this.verdict = verdict;
		this.comment = comment;
		this.status = status;
		this.auditEntry = auditEntry;
		this.versionNbr = versionNbr;
	}
	
	public CourtCaseDO(Integer courtCaseId, SummonsDO summons,
			String courtCaseNo, CourtDO court, CDVerdictDO verdict,
			String comment, StatusDO status, AuditEntry auditEntry,
			Integer versionNbr, BigDecimal fine,
			Integer sentencePeriodDay,Integer sentencePeriodMonth,Integer sentencePeriodYear,
			String finePaidFlag, String timeServedFlag) {
		super();
		this.courtCaseId = courtCaseId;
		this.summons = summons;
		this.courtCaseNo = courtCaseNo;
		//this.court = court;
		this.verdict = verdict;
		this.comment = comment;
		this.status = status;
		this.auditEntry = auditEntry;
		this.versionNbr = versionNbr;
		this.fineAmount = fine;
		this.sentencePeriodDay = sentencePeriodDay;
		this.sentencePeriodDay = sentencePeriodMonth;
		this.sentencePeriodDay = sentencePeriodYear;
		this.finePaidFlag = finePaidFlag;
		this.timeServedFlag = timeServedFlag;
	}

	
	
	public CourtCaseDO() {
		super();
		// TODO Auto-generated constructor stub
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
