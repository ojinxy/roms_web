package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.CDRoadCheckTypeDO;
import fsl.ta.toms.roms.dataobjects.ComplianceParamDO;
import fsl.ta.toms.roms.dataobjects.ExcerptParamMappingDO;
/**
 * 
 * @author rbrooks
 * Created Date: Oct 22, 2013
 */
public class ComplianceParamBO implements Serializable{


	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -953169909046052942L;
	Integer paramMapId;
	Integer complianceId;
	String paramName;
	String paramValue;
	
	
	public ComplianceParamBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
	public ComplianceParamBO(ComplianceParamDO complianceParamDO ) {
		super();
		this.paramMapId = complianceParamDO.getComplianceParamKey().getExcerptParamMapDO().getParamMapId();
		this.complianceId = complianceParamDO.getComplianceParamKey().getComplianceDO().getComplianceId();
		this.paramValue = complianceParamDO.getParamValue();
		this.paramName = complianceParamDO.getComplianceParamKey().getExcerptParamMapDO().getParamName();
	
	}





	public Integer getParamMapId() {
		return paramMapId;
	}





	public void setParamMapId(Integer paramMapId) {
		this.paramMapId = paramMapId;
	}





	public Integer getComplianceId() {
		return complianceId;
	}





	public void setComplianceId(Integer complianceId) {
		this.complianceId = complianceId;
	}





	public String getParamValue() {
		return paramValue;
	}





	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}





	public String getParamName() {
		return paramName;
	}





	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	
	
	
	}
