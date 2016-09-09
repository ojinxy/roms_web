package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ComplianceParamKey implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 3897187218791846986L;

	public ComplianceParamKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@ManyToOne
	@JoinColumn(name="param_map_id")
	ExcerptParamMappingDO excerptParamMapDO;
	
	@ManyToOne
	@JoinColumn(name="compliance_id")
	ComplianceDO complianceDO;

	public ExcerptParamMappingDO getExcerptParamMapDO() {
		return excerptParamMapDO;
	}

	public void setExcerptParamMapDO(ExcerptParamMappingDO excerptParamMapDO) {
		this.excerptParamMapDO = excerptParamMapDO;
	}

	public ComplianceDO getComplianceDO() {
		return complianceDO;
	}

	public void setComplianceDO(ComplianceDO complianceDO) {
		this.complianceDO = complianceDO;
	}

	
	
		
	
	
	
}
