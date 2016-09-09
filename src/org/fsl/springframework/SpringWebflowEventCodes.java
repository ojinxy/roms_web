package org.fsl.springframework;

import org.springframework.webflow.action.EventFactorySupport;
import org.springframework.webflow.execution.Event;

public class SpringWebflowEventCodes extends EventFactorySupport {
	private static final String SUMMONS_EVENT_ID = "SUM";

	private static final String WARNING_NOTICE_EVENT_ID = "WN";

	private static final String WARNING_NO_PROSECUTION_EVENT_ID = "WNP";

	private String summonsEventId = SUMMONS_EVENT_ID;

	private String warningNoticeEventId = WARNING_NOTICE_EVENT_ID;

	private String warningNoProsectionEventId = WARNING_NO_PROSECUTION_EVENT_ID;

	
	
	/**
	 * 	
	 * @param source
	 * @return
	 */
	public Event summons(Object source) {
		return event(source, getSummonsEventId());
	}

	/**
	 * 
	 * @param source
	 * @param e
	 * @return
	 */
	public Event summons(Object source, Exception e) {
		return event(source, getSummonsEventId(), getExceptionAttributeName(), e);
	}
	/**
	 * 	
	 * @param source
	 * @return
	 */
	public Event warningNotice(Object source) {
		return event(source, getWarningNoticeEventId());
	}

	/**
	 * 
	 * @param source
	 * @param e
	 * @return
	 */
	public Event warningNotice(Object source, Exception e) {
		return event(source, getWarningNoticeEventId(), getExceptionAttributeName(), e);
	}
	/**
	 * 	
	 * @param source
	 * @return
	 */
	public Event warningNoProsection(Object source) {
		return event(source, getWarningNoProsectionEventId());
	}

	/**
	 * 
	 * @param source
	 * @param e
	 * @return
	 */
	public Event warningNoProsection(Object source, Exception e) {
		return event(source, getWarningNoProsectionEventId(), getExceptionAttributeName(), e);
	}
	

	/****************************
	 * Getters and Setters
	 ***************************/
	
	
	/**
	 * @return the summonsEventId
	 */
	public String getSummonsEventId() {
		return summonsEventId;
	}

	/**
	 * @param summonsEventId the summonsEventId to set
	 */
	public void setSummonsEventId(String summonsEventId) {
		this.summonsEventId = summonsEventId;
	}

	/**
	 * @return the warningNoticeEventId
	 */
	public String getWarningNoticeEventId() {
		return warningNoticeEventId;
	}

	/**
	 * @param warningNoticeEventId the warningNoticeEventId to set
	 */
	public void setWarningNoticeEventId(String warningNoticeEventId) {
		this.warningNoticeEventId = warningNoticeEventId;
	}

	/**
	 * @return the warningNoProsectionEventId
	 */
	public String getWarningNoProsectionEventId() {
		return warningNoProsectionEventId;
	}

	/**
	 * @param warningNoProsectionEventId the warningNoProsectionEventId to set
	 */
	public void setWarningNoProsectionEventId(String warningNoProsectionEventId) {
		this.warningNoProsectionEventId = warningNoProsectionEventId;
	}

	

	

	
	
}
