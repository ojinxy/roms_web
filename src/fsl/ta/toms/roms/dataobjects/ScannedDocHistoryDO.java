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
import fsl.ta.toms.roms.bo.ScannedDocHistoryBO;

@Entity
@Table(name="ROMS_scanned_doc_history")
public class ScannedDocHistoryDO implements Serializable {	
	
	private static final long serialVersionUID = -5811428198045121468L;	

	@Id
	@Column(name="scanned_doc_history_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer scannedDocHistoryId;
	
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

	@Column(name="create_username")
	String createUsername;
	
	@Column(name="create_dtime")
	Date createDTime;
	
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;
	
	public ScannedDocHistoryDO() {
		super();		
	}

	/**
	 * 
	 * @param doc
	 */
	public ScannedDocHistoryDO(ScannedDocHistoryBO doc) {
		this.docType = doc.getDocType();
		//road check details
		this.roadCheckOffenceOutcome = new RoadCheckOffenceOutcomeDO();
		this.roadCheckOffenceOutcome.roadCheckOffenceOutcomeId = doc.getRoadCheckOffenceOutcomeId();
	//	System.err.println(" Road Check Outcome histoty " + doc.getRoadCheckOffenceOutcomeId());
		this.scannedDocId = doc.getScannedDocId();
		this.description = doc.getDescription();
		this.mimeType = doc.getMimeType();
		
		this.relativePath = doc.getRelativePath();
	
	}
	
	
	public ScannedDocHistoryDO(ScannedDocBO doc) {
		this.docType = doc.getDocType();
		//road check details
		this.roadCheckOffenceOutcome = new RoadCheckOffenceOutcomeDO();
		this.roadCheckOffenceOutcome.roadCheckOffenceOutcomeId = doc.getRoadCheckOffenceOutcomeId();
		//System.err.println(" Road Check Outcome histoty " + doc.getRoadCheckOffenceOutcomeId());
		this.scannedDocId = doc.getScannedDocId();
		this.description = doc.getDescription();
		this.mimeType = doc.getMimeType();
		
		this.relativePath = doc.getRelativePath();
	
	}


	/**
	 * @return the scannedDocId
	 */
	public Integer getScannedDocHistoryId() {
		return scannedDocHistoryId;
	}

	/**
	 * @param scannedDocId the scannedDocId to set
	 */
	public void setScannedDocHistoryId(Integer scannedDocId) {
		this.scannedDocHistoryId = scannedDocId;
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

	
	public Integer getScannedDocId() {
		return scannedDocId;
	}

	public void setScannedDocId(Integer scannedDocId) {
		this.scannedDocId = scannedDocId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	public Date getCreateDTime() {
		return createDTime;
	}

	public void setCreateDTime(Date createDTime) {
		this.createDTime = createDTime;
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

	
	

}
