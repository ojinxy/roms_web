package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.AssignedPersonDO;
import fsl.ta.toms.roms.dataobjects.JPDO;

public class JPBO extends AttendanceDetailsBO implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = -5567976300754387317L;
	

	private Integer idNumber;
	private String regNumber;
	private PersonBO personBO;
	private String parishCode;
	private String parishDescription;
	private String oldPin;
	private String newPin;
	private String confirmedPin;
	private String pinHash;
	private String statusId;
	private String statusDescription;
	private String currentUsername;
	private AuditEntryBO auditEntryBO;


	private boolean scheduled;
	
	public JPBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public JPBO(JPDO jpdo) {
		if(jpdo!=null){
			
			this.idNumber = jpdo.getIdNumber();
			
			this.regNumber = jpdo.getRegNumber();
						
			if(jpdo.getPerson()!=null){
				this.personBO = new PersonBO(jpdo.getPerson());
			}
			
			if(jpdo.getParish()!=null){
				this.parishCode = jpdo.getParish().getParishCode();
				this.parishDescription = jpdo.getParish().getDescription();
			}
						
			this.pinHash = jpdo.getPinHash();
			if(jpdo.getStatus()!=null){
				this.statusId = jpdo.getStatus().getStatusId();
				this.statusDescription = jpdo.getStatus().getDescription();
			}
		
			if(jpdo.getAuditEntry()!=null){
				this.auditEntryBO = new AuditEntryBO(jpdo.getAuditEntry());
			}
		}
	}
	
	public JPBO(JPDO jpdo, AssignedPersonDO assignedPersonDO) {
		if(jpdo!=null){
			
			this.idNumber = jpdo.getIdNumber();
			
			this.regNumber = jpdo.getRegNumber();
			
			if(jpdo.getPerson()!=null){
				this.personBO = new PersonBO(jpdo.getPerson());
			}
			
			if(jpdo.getParish()!=null){
				this.parishCode = jpdo.getParish().getParishCode();
				this.parishDescription = jpdo.getParish().getDescription();
			}
						
			this.pinHash = jpdo.getPinHash();
			if(jpdo.getStatus()!=null){
				this.statusId = jpdo.getStatus().getStatusId();
				this.statusDescription = jpdo.getStatus().getDescription();
			}
		
			if(jpdo.getAuditEntry()!=null){
				this.auditEntryBO = new AuditEntryBO(jpdo.getAuditEntry());
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



	

	public String getRegNumber() {
		return regNumber;
	}



	public void setRegNumber(String regNumber) {
		if(StringUtils.isNotBlank(regNumber))
			this.regNumber = regNumber.trim();
	}



	public PersonBO getPersonBO() {
		return personBO;
	}

	public void setPersonBO(PersonBO personBO) {
		this.personBO = personBO;
	}

	public String getParishCode() {
		return parishCode;
	}

	public void setParishCode(String parishCode) {
		if(StringUtils.isNotBlank(parishCode))
			this.parishCode = parishCode.trim();
	}

	public String getParishDescription() {
		return parishDescription;
	}

	public void setParishDescription(String parishDescription) {
		if(StringUtils.isNotBlank(parishDescription))
			this.parishDescription = parishDescription.trim();
	}

	public String getPinHash() {
		return pinHash;
	}

	public void setPinHash(String pinHash) {
		if(StringUtils.isNotBlank(pinHash))
			this.pinHash = pinHash.trim();
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription.trim();
	}

	public String getCurrentUsername() {
		return currentUsername;
	}

	public void setCurrentUsername(String currentUsername) {
		if(StringUtils.isNotBlank(currentUsername))
			this.currentUsername = currentUsername.trim();
	}



	public String getOldPin() {
		return oldPin;
	}



	public void setOldPin(String oldPin) {
		if(StringUtils.isNotBlank(oldPin))
			this.oldPin = oldPin.trim();
	}



	public String getNewPin() {
		return newPin;
	}



	public void setNewPin(String newPin) {
		if(StringUtils.isNotBlank(newPin))
			this.newPin = newPin.trim();
	}



	public String getConfirmedPin() {
		return confirmedPin;
	}



	public void setConfirmedPin(String confirmedPin) {
		if(StringUtils.isNotBlank(confirmedPin))
			this.confirmedPin = confirmedPin.trim();
	}



	public AuditEntryBO getAuditEntryBO() {
		return auditEntryBO;
	}



	public void setAuditEntryBO(AuditEntryBO auditEntryBO) {
		this.auditEntryBO = auditEntryBO;
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
