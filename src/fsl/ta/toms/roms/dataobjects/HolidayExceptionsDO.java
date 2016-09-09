package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.bo.HolidayExceptionBO;
import fsl.ta.toms.roms.bo.ParishBO;

@Entity
@Table(name="roms_holiday_exceptions")
public class HolidayExceptionsDO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8770337316025920584L;



	public HolidayExceptionsDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
/**
 * 
 * @param parishBO
 */
	public HolidayExceptionsDO(HolidayExceptionBO holidayExceptionsBO) {
	
		this.holidayDate = holidayExceptionsBO.getHolidayDate();
		this.description = holidayExceptionsBO.getDescription();
		this.status = new StatusDO();
		this.status.setStatusId(holidayExceptionsBO.getStatusId());
		
	}


	@Id
	@Column(name="holiday_date")
	Date holidayDate;
	
	@Column(name="holiday_desc")
	String description;
	
	@Column(name="allow")
	String allow;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;


	public void updateHolidayExceptionDOFields(HolidayExceptionBO holidayExceptionsBO) {
		
		this.description = holidayExceptionsBO.getDescription();
		//this.holidayDate = holidayExceptionsBO.getHolidayDate();
		this.status = new StatusDO();
		this.status.setStatusId(holidayExceptionsBO.getStatusId());
		
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

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
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

	public String getAllow() {
		return allow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}



	
		
}
