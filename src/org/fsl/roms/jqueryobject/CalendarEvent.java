/**
 * 
 */
package org.fsl.roms.jqueryobject;

import java.io.Serializable;
import java.util.Date;

/**
 * @author oanguin
 *
 */
public class CalendarEvent implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Integer id;
	
	public String title;
	
	public boolean allDay;
	
	public Long start;
	
	public Long end;
	
	public String url;
	
	public String className;
	
	public boolean editable;
	
	public boolean startEditable;
	
	public boolean durationEditable;
	
	public String status;
	
	public String statusId;
	
	public String scheduledStartDateTime;
	
	public String scheduledEndDateTime;
	
	public String actualStartDateTime;
	
	public String actualEndDateTime;
	
	public String borderColor;
	
	public String backgroundColor;
	
	public String textColor;

	public CalendarEvent(Integer id, String title, boolean allDay, Long start,
			Long end, String url, String className, boolean editable,
			boolean startEditable, boolean durationEditable) {
		super();
		this.id = id;
		this.title = title;
		this.allDay = allDay;
		this.start = start;
		this.end = end;
		this.url = url;
		this.className = className;
		this.editable = editable;
		this.startEditable = startEditable;
		this.durationEditable = durationEditable;
	}


	public CalendarEvent(Integer id, String title, Long start,
			Long end) {
		super();
		this.id = id;
		this.title = title;
		this.start = start;
		this.end = end;

	}
	
	public CalendarEvent() 
	{
		super();
		
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isStartEditable() {
		return startEditable;
	}

	public void setStartEditable(boolean startEditable) {
		this.startEditable = startEditable;
	}

	public boolean isDurationEditable() {
		return durationEditable;
	}

	public void setDurationEditable(boolean durationEditable) {
		this.durationEditable = durationEditable;
	}
	

	
	
	
	

}
