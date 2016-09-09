package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.OffenceLawDO;

public class OffenceLawBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 897682521981887606L;
	private Integer offenceId;
	private Integer lawId;
	
	
	
	
	public OffenceLawBO(OffenceLawDO offenceLawDO) {
		super();
		this.offenceId = offenceLawDO.getOffenceLawKey().getOffence().getOffenceId();
		this.lawId = offenceLawDO.getOffenceLawKey().getGoverningLaw().getLawId();
	}
	public OffenceLawBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getOffenceId() {
		return offenceId;
	}
	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}
	public Integer getLawId() {
		return lawId;
	}
	public void setLawId(Integer lawId) {
		this.lawId = lawId;
	}
	
	
}
