/**
 * 
 */
package org.fsl.roms.view;

import java.io.Serializable;
import java.util.Date;

import org.primefaces.model.ScheduleEvent;

import fsl.ta.toms.roms.bo.RoadOperationBO;

/**
 * @author oanguin
 *
 */
public class RoadOpCalEvent implements ScheduleEvent, Serializable 
{
	private String styleClass;
	
	private RoadOperationBO roadOp;
	
	private String id;
	
	private Date endDate;
	
	private Date startDate;
	
	private String title;
	
	private Boolean allDay;
	
	private Boolean editable;
	
	
	

	public RoadOpCalEvent() {
		super();
		
	}

	public RoadOpCalEvent(String styleClass, RoadOperationBO roadOp,
			String id, Date endDate, Date startDate, String title,
			Boolean allDay, Boolean editable) {
		super();
		this.styleClass = styleClass;
		this.roadOp = roadOp;
		this.id = id;
		this.endDate = endDate;
		this.startDate = startDate;
		this.title = title;
		this.allDay = allDay;
		this.editable = editable;
	}

	@Override
	public Object getData() {
		
		return this.roadOp;
	}

	@Override
	public Date getEndDate() {
		
		return this.endDate;
	}

	@Override
	public String getId() {
		
		return this.id;
	}

	@Override
	public Date getStartDate() {
		
		return this.startDate;
	}

	@Override
	public String getStyleClass() {

		return this.styleClass;
	}

	@Override
	public String getTitle() {

		return this.title;
	}

	@Override
	public boolean isAllDay() {

		return this.allDay;
	}

	@Override
	public boolean isEditable() {
		
		return this.editable;
	}

	@Override
	public void setId(String arg0) {
		this.id = arg0;
		
	}

	public RoadOperationBO getRoadOp() {
		return roadOp;
	}

	public void setRoadOp(RoadOperationBO roadOp) {
		this.roadOp = roadOp;
	}

	public Boolean getAllDay() {
		return allDay;
	}

	public void setAllDay(Boolean allDay) {
		this.allDay = allDay;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	
	
}
