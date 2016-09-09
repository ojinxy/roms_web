package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class OffenceLawKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7359367607012824016L;

	public OffenceLawKey() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@ManyToOne
	@JoinColumn(name="offence_id")
	OffenceDO offence;
	
	@ManyToOne
	@JoinColumn(name="law_id")
	GoverningLawDO governingLaw;

	public OffenceDO getOffence() {
		return offence;
	}

	public void setOffence(OffenceDO offence) {
		this.offence = offence;
	}

	public GoverningLawDO getGoverningLaw() {
		return governingLaw;
	}

	public void setGoverningLaw(GoverningLawDO governingLaw) {
		this.governingLaw = governingLaw;
	}
	
	

}
