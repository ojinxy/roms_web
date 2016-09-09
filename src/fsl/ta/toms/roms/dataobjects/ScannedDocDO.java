package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
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

import fsl.ta.toms.roms.bo.ScannedDocBO;

@Entity
@Table(name="ROMS_scanned_doc")
public class ScannedDocDO implements Serializable {	
	
	private static final long serialVersionUID = -5811428198045121468L;	

	@Id
	@Column(name="scanned_doc_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer scannedDocId;
	
	@ManyToOne
	@JoinColumn(name="road_check_offence_outcome_id")
	RoadCheckOffenceOutcomeDO roadCheckOffenceOutcome;
	
	@Column(name="mime_type")
	String mimeType;
	
	@Column(name="relative_path")
	String relativePath;
	
	@Column(name="doc_type")
	String docType;
	
	@Column(name="description")
	String description;

	@Embedded
	AuditEntry auditEntry;
	
	@Column(name="print_username")
	String printUsername;
	
	@Column(name="print_dtime")
	Date printDTime;
	
	@Column(name="print_count")
	Integer printCount;

	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	
	@ManyToOne
	@JoinColumn(name = "reprint_reason_id")
	ReasonDO reprintReason;

	@Column(name = "reprint_comment")
	String reprintComment;
	
	public ScannedDocDO() {
		super();		
	}

	/**
	 * 
	 * @param doc
	 */
	public ScannedDocDO(ScannedDocBO doc) {
		this.docType = doc.getDocType();
		//road check details
		this.roadCheckOffenceOutcome = new RoadCheckOffenceOutcomeDO();
		this.roadCheckOffenceOutcome.roadCheckOffenceOutcomeId = doc.getRoadCheckOffenceOutcomeId();
		this.description = doc.getDescription();
		this.mimeType = doc.getMimeType();
		this.printCount = doc.getPrintCount();
		this.printDTime = doc.getPrintDTime();
		this.printUsername = doc.getPrintUsername();
		this.relativePath = doc.getRelativePath();
		this.reprintComment = doc.getReprintComment();
	
	}

	/**
	 * @return the scannedDocId
	 */
	public Integer getScannedDocId() {
		return scannedDocId;
	}

	/**
	 * @param scannedDocId the scannedDocId to set
	 */
	public void setScannedDocId(Integer scannedDocId) {
		this.scannedDocId = scannedDocId;
	}

	/**
	 * @return the roadCheckOffenceOutcome
	 */
	public RoadCheckOffenceOutcomeDO getRoadCheckOffenceOutcome() {
		return roadCheckOffenceOutcome;
	}

	/**
	 * @param roadCheckOffenceOutcome the roadCheckOffenceOutcome to set
	 */
	public void setRoadCheckOffenceOutcome(
			RoadCheckOffenceOutcomeDO roadCheckOffenceOutcome) {
		this.roadCheckOffenceOutcome = roadCheckOffenceOutcome;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @return the relativePath
	 */
	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * @param relativePath the relativePath to set
	 */
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	/**
	 * @return the docType
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * @param docType the docType to set
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 * @return the auditEntry
	 */
	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	/**
	 * @param auditEntry the auditEntry to set
	 */
	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	/**
	 * @return the printUsername
	 */
	public String getPrintUsername() {
		return printUsername;
	}

	/**
	 * @param printUsername the printUsername to set
	 */
	public void setPrintUsername(String printUsername) {
		this.printUsername = printUsername;
	}

	/**
	 * @return the printDTime
	 */
	public Date getPrintDTime() {
		return printDTime;
	}

	/**
	 * @param printDTime the printDTime to set
	 */
	public void setPrintDTime(Date printDTime) {
		this.printDTime = printDTime;
	}

	/**
	 * @return the printCount
	 */
	public Integer getPrintCount() {
		return printCount;
	}

	/**
	 * @param printCount the printCount to set
	 */
	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}

	/**
	 * @return the versionNbr
	 */
	public Integer getVersionNbr() {
		return versionNbr;
	}

	/**
	 * @param versionNbr the versionNbr to set
	 */
	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the reprintReason
	 */
	public ReasonDO getReprintReason() {
		return reprintReason;
	}

	/**
	 * @param reprintReason the reprintReason to set
	 */
	public void setReprintReason(ReasonDO reprintReason) {
		this.reprintReason = reprintReason;
	}

	/**
	 * @return the reprintComment
	 */
	public String getReprintComment() {
		return reprintComment;
	}

	/**
	 * @param reprintComment the reprintComment to set
	 */
	public void setReprintComment(String reprintComment) {
		this.reprintComment = reprintComment;
	}

	
	

}
