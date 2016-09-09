package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.StrategyDO;

public class StrategyBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7397973759298282309L;
	private Integer strategyId;
	private String strategyDescription;
	private boolean vehicleRequired; 
	private boolean arteryRequired; 
	private String statusId;
	private String statusDescription;
	private String currentUsername;
	private AuditEntryBO auditEntryBO;
	
	
	public StrategyBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

	public StrategyBO(StrategyDO strategyDO) {
		this.strategyId = strategyDO.getStrategyId();
		this.strategyDescription = strategyDO.getDescription();
		
		if(strategyDO.getArteryRequired()==null || strategyDO.getArteryRequired().equalsIgnoreCase("N")){
			this.arteryRequired = false;
		}
		else{
			this.arteryRequired = true;
		}
		
		if(strategyDO.getVehicleRequired()==null || strategyDO.getVehicleRequired().equalsIgnoreCase("N")){
			this.vehicleRequired = false;
		}
		else{
			this.vehicleRequired = true;
		}
		
		
		
		if(strategyDO.getStatus()!=null){
			this.statusId = strategyDO.getStatus().getStatusId();
			this.statusDescription = strategyDO.getStatus().getDescription();
		}
		
		if(strategyDO.getAuditEntry()!=null){
			this.auditEntryBO = new AuditEntryBO(strategyDO.getAuditEntry());
		}
	}




	public Integer getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}
	
	public String getStrategyDescription() {
		return strategyDescription;
	}




	public void setStrategyDescription(String strategyDescription) {
		if(StringUtils.isNotBlank(strategyDescription))
			this.strategyDescription = strategyDescription.trim();
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




	public boolean isVehicleRequired() {
		return vehicleRequired;
	}




	public void setVehicleRequired(boolean vehicleRequired) {
		this.vehicleRequired = vehicleRequired;
	}




	public boolean isArteryRequired() {
		return arteryRequired;
	}




	public void setArteryRequired(boolean arteryRequired) {
		this.arteryRequired = arteryRequired;
	}




	public AuditEntryBO getAuditEntryBO() {
		return auditEntryBO;
	}




	public void setAuditEntryBO(AuditEntryBO auditEntryBO) {
		this.auditEntryBO = auditEntryBO;
	}





	
	
}
