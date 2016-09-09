package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

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

@Entity
@Table(name="ROMS_citation_check")
public class CitationCheckDO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1584520577082038426L;
	
	public CitationCheckDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	public CitationCheckDO(RoadCheckDO roadCheck, String trnNbr, String dlNo,
			String regPlateNo, Boolean includeTTMSResults,AuditEntry auditEntry) {
		super();
		this.roadCheck = roadCheck;
		this.trnNbr = trnNbr;
		this.dlNo = dlNo;
		this.regPlateNo = regPlateNo;
		this.auditEntry = auditEntry;
		this.includeTTMSResults = includeTTMSResults;
	}




	@Id
	@Column(name="citation_check_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer citationCheckId;
	
	@ManyToOne
	@JoinColumn(name="road_check_id")
	RoadCheckDO roadCheck;
	
	@Column(name="trn_nbr")
	String trnNbr;
	
	@Column(name="dl_no")
	String dlNo;
	
	
	@Column(name="reg_plate_no")
	String regPlateNo;
	
	@Column(name="include_ttms_results")
	Boolean includeTTMSResults;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getCitationCheckId() {
		return citationCheckId;
	}

	public void setCitationCheckId(Integer citationCheckId) {
		this.citationCheckId = citationCheckId;
	}

	public RoadCheckDO getRoadCheck() {
		return roadCheck;
	}

	public void setRoadCheck(RoadCheckDO roadCheck) {
		this.roadCheck = roadCheck;
	}

	public String getTrnNbr() {
		return trnNbr;
	}

	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
	}

	public String getDlNo() {
		return dlNo;
	}

	public void setDlNo(String dlNo) {
		this.dlNo = dlNo;
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

	public String getRegPlateNo() {
		return regPlateNo;
	}

	public void setRegPlateNo(String regPlateNo) {
		this.regPlateNo = regPlateNo;
	}




	public Boolean getIncludeTTMSResults()
	{
		return includeTTMSResults;
	}




	public void setIncludeTTMSResults(Boolean includeTTMSResults)
	{
		this.includeTTMSResults = includeTTMSResults;
	}

		
}
