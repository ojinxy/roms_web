package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import javax.persistence.*;

@Embeddable
public class OffenceParamKey implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@ManyToOne
	@JoinColumn(name="offence_id")
	OffenceDO offence;
	
	@ManyToOne
	@JoinColumn(name="param_map_id")
	ExcerptParamMappingDO excerptParamMapping;
	
	public OffenceParamKey()
	{
		super();
	}

	public OffenceDO getOffence() {
		return offence;
	}

	public void setOffence(OffenceDO offence) {
		this.offence = offence;
	}

	public ExcerptParamMappingDO getExcerptParamMapping() {
		return excerptParamMapping;
	}

	public void setExcerptParamMapping(ExcerptParamMappingDO excerptParamMapping) {
		this.excerptParamMapping = excerptParamMapping;
	}
	
	

}
