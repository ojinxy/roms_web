package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.OffenceParamDO;

public class OffenceParamBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3503975056115915846L;
	private Integer offenceId;
	private Integer paramMapId;
	private String paramName;
	
	
	public OffenceParamBO(OffenceParamDO offenceParamDO) {
		super();
		this.offenceId = offenceParamDO.getOffenceParamKey().getOffence().getOffenceId();
		this.paramMapId =  offenceParamDO.getOffenceParamKey().getExcerptParamMapping().getParamMapId();
		this.paramName = offenceParamDO.getOffenceParamKey().getExcerptParamMapping().getParamName();
	}
	public OffenceParamBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getOffenceId() {
		return offenceId;
	}
	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}
	public Integer getParamMapId() {
		return paramMapId;
	}
	public void setParamMapId(Integer paramMapId) {
		this.paramMapId = paramMapId;
	}
	/**
	 * @return the paramName
	 */
	public String getParamName() {
		return paramName;
	}
	/**
	 * @param paramName the paramName to set
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	

}
