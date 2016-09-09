package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

public class TicketCheckResultBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4231122713642896764L;
	
	Integer ticketCheckId;
	Integer citationCheckID;
	String ticketNo;
	Date issueDate;
	String offenceDescription;
	String ticketStatus;
	
	
	public Integer getTicketCheckId() {
		return ticketCheckId;
	}
	public void setTicketCheckId(Integer ticketCheckId) {
		this.ticketCheckId = ticketCheckId;
	}
	public Integer getCitationCheckID() {
		return citationCheckID;
	}
	public void setCitationCheckID(Integer citationCheckID) {
		this.citationCheckID = citationCheckID;
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
	
	
}
