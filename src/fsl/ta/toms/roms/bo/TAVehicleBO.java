package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.AssignedVehicleDO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.dataobjects.VehicleDO;

public class TAVehicleBO extends AttendanceDetailsBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6786998539042434532L;
	
	private Integer taVehicleId;
	private VehicleBO vehicle;
	private String officeLocationCode;
	private String statusId;
	private String statusDescription;
	private String currentUsername;
	
	
	private AuditEntryBO auditEntryBO;

	private boolean scheduled;
	
	public TAVehicleBO() {
		super();
		
		// TODO Auto-generated constructor stub
	}

	
	
	public TAVehicleBO(TAVehicleDO taVehicleDO) {
		
		if(taVehicleDO!=null){
			this.taVehicleId = taVehicleDO.getTaVehicleId();
			
			
			if(taVehicleDO.getVehicle()!=null){
				this.vehicle = new VehicleBO(taVehicleDO.getVehicle());
			}
			
			this.officeLocationCode = taVehicleDO.getOfficeLocationCode();
			if(taVehicleDO.getStatus()!=null){
				this.statusId = taVehicleDO.getStatus().getStatusId();
				this.statusDescription = taVehicleDO.getStatus().getDescription();
			}
			
			if(taVehicleDO.getAuditEntry()!=null){
				this.auditEntryBO = new AuditEntryBO(taVehicleDO.getAuditEntry());
			}
		}
	}

	
	public TAVehicleBO(TAVehicleDO taVehicleDO, AssignedVehicleDO assignedVehicleDO) {
		if(taVehicleDO!=null){
			this.taVehicleId = taVehicleDO.getTaVehicleId();
			
			if(taVehicleDO.getVehicle()!=null){
				this.vehicle = new VehicleBO(taVehicleDO.getVehicle());
				}
			
			this.officeLocationCode = taVehicleDO.getOfficeLocationCode();
			if(taVehicleDO.getStatus()!=null){
				this.statusId = taVehicleDO.getStatus().getStatusId();
				this.statusDescription = taVehicleDO.getStatus().getDescription();
			}
			
			if(taVehicleDO.getAuditEntry()!=null){
				this.auditEntryBO = new AuditEntryBO(taVehicleDO.getAuditEntry());
			}
		}
		
		if(assignedVehicleDO!=null){
			this.setComment(assignedVehicleDO.getComment());
			
			this.setScheduled(true);
			
			if(assignedVehicleDO.getAttended()==null){
				this.setAttended(null);
			}
			else{
				if(assignedVehicleDO.getAttended().equalsIgnoreCase("Y")){
					this.setAttended(true);
				}
				if(assignedVehicleDO.getAttended().equalsIgnoreCase("N")){
					this.setAttended(false);
				}
			}
		}
		
	}


	public Integer getTaVehicleId() {
		return taVehicleId;
	}

	public void setTaVehicleId(Integer taVehicleId) {
		this.taVehicleId = taVehicleId;
	}

	public VehicleBO getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleBO vehicle) {
		this.vehicle = vehicle;
	}

	

	

	public String getOfficeLocationCode() {
		return officeLocationCode;
	}



	public void setOfficeLocationCode(String officeLocationCode) {
		if(StringUtils.isNotBlank(officeLocationCode))
			this.officeLocationCode = officeLocationCode.trim();
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



	public AuditEntryBO getAuditEntryBO() {
		return auditEntryBO;
	}



	public void setAuditEntryBO(AuditEntryBO auditEntryBO) {
		this.auditEntryBO = auditEntryBO;
	}

	
	public boolean isScheduled() {
		return this.scheduled;
	}

	
	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
		
	}
	
	
}
