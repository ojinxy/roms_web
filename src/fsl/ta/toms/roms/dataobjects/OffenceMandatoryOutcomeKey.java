package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class OffenceMandatoryOutcomeKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3327205721474634716L;

	public OffenceMandatoryOutcomeKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	@ManyToOne
	@JoinColumn(name="offence_id")
	OffenceDO offenceDO;
	
	@ManyToOne
	@JoinColumn(name="outcome_type_id")
	CDOutcomeTypeDO outcomeTypeDO;

	public OffenceDO getOffenceDO() {
		return offenceDO;
	}

	public void setOffenceDO(OffenceDO offenceDO) {
		this.offenceDO = offenceDO;
	}

	public CDOutcomeTypeDO getOutcomeTypeDO() {
		return outcomeTypeDO;
	}

	public void setOutcomeTypeDO(CDOutcomeTypeDO outcomeTypeDO) {
		this.outcomeTypeDO = outcomeTypeDO;
	}


	
	
}
