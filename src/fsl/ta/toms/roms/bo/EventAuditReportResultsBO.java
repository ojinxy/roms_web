/**
 * Created By: oanguin
 * Date: May 24, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author oanguin
 * Created Date: May 24, 2013
 */
public class EventAuditReportResultsBO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	String refType1Value;
	
	String refType1Desc;
	
	String refType1Label;
	
	String refType2Value;
	
	String refType2Desc;
	
	String refType2Label;
	
	String Comment;
	
	String eventDesc;
	
	String eventDate;
	
	String createUserName;
	
	String officeLocation;
	
	String createUserFullName;

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getRefType1Value() {
		return refType1Value;
	}

	public void setRefType1Value(String refType1Value) {
		this.refType1Value = refType1Value;
	}

	public String getRefType1Desc() {
		return refType1Desc;
	}

	public void setRefType1Desc(String refType1Desc) {
		this.refType1Desc = refType1Desc;
	}

	public String getRefType1Label() {
		return refType1Label;
	}

	public void setRefType1Label(String refType1Label) {
		this.refType1Label = refType1Label;
	}

	public String getRefType2Value() {
		return refType2Value;
	}

	public void setRefType2Value(String refType2Value) {
		this.refType2Value = refType2Value;
	}

	public String getRefType2Desc() {
		return refType2Desc;
	}

	public void setRefType2Desc(String refType2Desc) {
		this.refType2Desc = refType2Desc;
	}

	public String getRefType2Label() {
		return refType2Label;
	}

	public void setRefType2Label(String refType2Label) {
		this.refType2Label = refType2Label;
	}

	public String getComment() {
		return Comment;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}


	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getOfficeLocation() {
		return officeLocation;
	}

	public void setOfficeLocation(String officeLocation) {
		this.officeLocation = officeLocation;
	}

	public EventAuditReportResultsBO(String refType1Value, String refType1Desc,
			String refType1Label, String refType2Value, String refType2Desc,
			String refType2Label, String comment, String eventDesc,
			String eventDate, String createUserName, String officeLocation, String createUserFullName) {
		super();
		this.refType1Value = refType1Value;
		this.refType1Desc = refType1Desc;
		this.refType1Label = refType1Label;
		this.refType2Value = refType2Value;
		this.refType2Desc = refType2Desc;
		this.refType2Label = refType2Label;
		this.Comment = comment;
		this.eventDesc = eventDesc;
		this.eventDate = eventDate;
		this.createUserName = createUserName;
		this.officeLocation = officeLocation;
		this.createUserFullName = createUserFullName;
	}

	public EventAuditReportResultsBO() {
		super();
		
	}

	public String getCreateUserFullName() {
		return createUserFullName;
	}

	public void setCreateUserFullName(String createUserFullName) {
		this.createUserFullName = createUserFullName;
	}

	

	
	
	
	
	
	
	
}
