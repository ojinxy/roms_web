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

@Entity
@Table(name="ROMS_ticket_check_result")
public class TicketCheckResultDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1962033947502975036L;

	public TicketCheckResultDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public TicketCheckResultDO(CitationCheckDO citationCheck, String ticketNo,
			Date issueDate, String offenceDescription, String ticketStatus,
			AuditEntry auditEntry) {
		super();
		this.citationCheck = citationCheck;
		this.ticketNo = ticketNo;
		this.issueDate = issueDate;
		this.offenceDescription = offenceDescription;
		this.ticketStatus = ticketStatus;
		this.auditEntry = auditEntry;
	}




	@Id
	@Column(name="ticket_check_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer ticketCheckId;
	
	@ManyToOne
	@JoinColumn(name="citation_check_id")
	CitationCheckDO citationCheck;
	
	@Column(name="ticket_no")	
	String ticketNo;
	
	@Column(name="issue_date")
	Date issueDate;
	
	@Column(name="offence_desc")
	String offenceDescription;
	
	@Column(name="ticket_status")
	String ticketStatus;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getTicketCheckId() {
		return ticketCheckId;
	}

	public void setTicketCheckId(Integer ticketCheckId) {
		this.ticketCheckId = ticketCheckId;
	}

	public CitationCheckDO getCitationCheck() {
		return citationCheck;
	}

	public void setCitationCheck(CitationCheckDO citationCheck) {
		this.citationCheck = citationCheck;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getOffenceDescription() {
		return offenceDescription;
	}

	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
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
	
	

}
