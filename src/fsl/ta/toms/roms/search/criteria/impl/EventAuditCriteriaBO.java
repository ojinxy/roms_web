package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

public class EventAuditCriteriaBO implements Serializable, SearchCriteria{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1814962227844962586L;
	
	private Integer eventAuditId;
	private Integer eventCode;
	String username;
	public Integer getEventAuditId() {
		return eventAuditId;
	}
	public void setEventAuditId(Integer eventAuditId) {
		this.eventAuditId = eventAuditId;
	}
	
	public Integer getEventCode() {
		return eventCode;
	}
	public void setEventCode(Integer eventCode) {
		this.eventCode = eventCode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
	
}