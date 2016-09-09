package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;


import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.HolidayExceptionsDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;


/**
 * 
 * @author rbrooks
 * Created Date: Jul 16, 2013
 */
public class HolidayExceptionBO implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -2857804229004287292L;
	String currentUsername;
	String description;
	Date holidayDate;
	String statusDescription;
	String statusId;
	String allow;
	
	public HolidayExceptionBO() {
		super();
	}
	
	/**
	 * Constructor of BO from DO object
	 * @param parishDO
	 */
	public HolidayExceptionBO(HolidayExceptionsDO holidayExceptionsDO) {
		this.holidayDate = holidayExceptionsDO.getHolidayDate();
		this.description = holidayExceptionsDO.getDescription();
		this.allow = holidayExceptionsDO.getAllow();
		
		if(holidayExceptionsDO.getStatus() != null) {
			this.statusId = holidayExceptionsDO.getStatus().getStatusId();
			this.statusDescription = holidayExceptionsDO.getStatus().getDescription();
		}
		
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public void setCurrentUsername(String currentUsername) {
		this.currentUsername = currentUsername;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getAllow() {
		return allow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}

	
}
