package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author rbrooks
 * Created Date: Jul 16, 2013
 */
public class HolidayExceptionsCriteriaBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6429334793500664407L;

	Date holidayDate;

	String  description;

	String statusId;
	
	public HolidayExceptionsCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Date getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	
	

}
