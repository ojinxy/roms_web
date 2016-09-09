package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.OffenceMandatoryOutcomeDO;

public class OffenceMandatoryOutcomeBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -298439491323884229L;

	private Integer offenceId;
	private String offenceDescription;
	private String shortDescription;
	private String outcomeTypeId;
	private  String statusId;
	
	
	
	public OffenceMandatoryOutcomeBO(OffenceMandatoryOutcomeDO offenceMandatoryOutcomeDO) {
		super();
		this.offenceId = offenceMandatoryOutcomeDO.getOffenceMandatoryOutcomeKey().getOffenceDO().getOffenceId();
		this.offenceDescription = offenceMandatoryOutcomeDO.getOffenceMandatoryOutcomeKey().getOffenceDO().getDescription();
		this.shortDescription = offenceMandatoryOutcomeDO.getOffenceMandatoryOutcomeKey().getOffenceDO().getShortDescription();
		this.outcomeTypeId = offenceMandatoryOutcomeDO.getOffenceMandatoryOutcomeKey().getOutcomeTypeDO().getOutcomeTypeId();
		this.statusId = offenceMandatoryOutcomeDO.getStatus().getStatusId();
	}
	public OffenceMandatoryOutcomeBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getOffenceId() {
		return offenceId;
	}
	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}
	public String getOutcomeTypeId() {
		return outcomeTypeId;
	}
	public void setOutcomeTypeId(String outcomeTypeId) {
		this.outcomeTypeId = outcomeTypeId;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getOffenceDescription() {
		return offenceDescription;
	}
	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	
	
}
