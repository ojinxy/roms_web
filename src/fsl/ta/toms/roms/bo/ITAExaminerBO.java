package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.AssignedPersonDO;
import fsl.ta.toms.roms.dataobjects.ITAExaminerDO;

public class ITAExaminerBO extends AttendanceDetailsBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7056872881458503175L;
	
	private Integer idNumber;
	private String examinerId;
	private PersonBO personBO;
	private String officeLocationCode;
	private String statusId;
	private String statusDescription;
	private String currentUsername;
	private AuditEntryBO auditEntryBO;

	private boolean scheduled;
	
	
	public ITAExaminerBO(ITAExaminerDO itaExaminerDO) {
		if(itaExaminerDO!=null){
			this.setIdNumber(itaExaminerDO.getIdNumber());
			this.examinerId = itaExaminerDO.getExaminerId();
			
			if(itaExaminerDO.getPerson()!=null){
				this.personBO = new PersonBO(itaExaminerDO.getPerson());
			}
			
			this.officeLocationCode = itaExaminerDO.getOfficeLocationCode();
			if(itaExaminerDO.getStatus()!=null){
				this.statusId = itaExaminerDO.getStatus().getStatusId();
				this.statusDescription = itaExaminerDO.getStatus().getDescription();
			}
			
			if(itaExaminerDO.getAuditEntry()!=null){
				this.auditEntryBO = new AuditEntryBO(itaExaminerDO.getAuditEntry());
			}
			
			
			
		}
	}
	
	public ITAExaminerBO(ITAExaminerDO itaExaminerDO, AssignedPersonDO assignedPersonDO) {
		if(itaExaminerDO!=null){
			this.setIdNumber(itaExaminerDO.getIdNumber());
		
			this.examinerId = itaExaminerDO.getExaminerId();
			
			if(itaExaminerDO.getPerson()!=null){
				this.personBO = new PersonBO(itaExaminerDO.getPerson());
			}
			
			this.officeLocationCode = itaExaminerDO.getOfficeLocationCode();
			if(itaExaminerDO.getStatus()!=null){
				this.statusId = itaExaminerDO.getStatus().getStatusId();
				this.statusDescription = itaExaminerDO.getStatus().getDescription();
			}
			if(itaExaminerDO.getAuditEntry()!=null){
				this.auditEntryBO = new AuditEntryBO(itaExaminerDO.getAuditEntry());
			}
			
		}
		if(assignedPersonDO!=null){
			this.setComment(assignedPersonDO.getComment());
			
			this.setScheduled(true);
			
			if(assignedPersonDO.getAttended()==null){
				this.setAttended(null);
			}
			else{
				if(assignedPersonDO.getAttended().equalsIgnoreCase("Y")){
					this.setAttended(true);
				}
				if(assignedPersonDO.getAttended().equalsIgnoreCase("N")){
					this.setAttended(false);
				}
			}
		}
	}

	public ITAExaminerBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public PersonBO getPersonBO() {
		return personBO;
	}

	public void setPersonBO(PersonBO personBO) {
		this.personBO = personBO;
	}

	public String getOfficeLocationCode() {
		return officeLocationCode;
	}

	public void setOfficeLocationCode(String officeLocationCode) {
		if(StringUtils.isNotBlank(officeLocationCode))
			this.officeLocationCode = officeLocationCode;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription;
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public void setCurrentUsername(String currentUsername) {
		if(StringUtils.isNotBlank(currentUsername))
			this.currentUsername = currentUsername;
	}

	public AuditEntryBO getAuditEntryBO() {
		return auditEntryBO;
	}

	public void setAuditEntryBO(AuditEntryBO auditEntryBO) {
		this.auditEntryBO = auditEntryBO;
	}

	public String getExaminerId() {
		return examinerId;
	}

	public void setExaminerId(String examinerId) {
		this.examinerId = examinerId;
	}

	public Integer getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(Integer idNumber) {
		this.idNumber = idNumber;
	}
	
	
	public boolean isScheduled() {
		return this.scheduled;
	}

	
	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
		
	}
	
	
}
