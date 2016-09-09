package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.CDRoadCheckTypeDO;
import fsl.ta.toms.roms.dataobjects.ExcerptParamMappingDO;
/**
 * 
 * @author rbrooks
 * Created Date: Oct 22, 2013
 */
public class ExcerptParamMappingBO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7496298459510252558L;
	Integer paramMapId;
	String paramName;
	String paramDescription;
	Integer paramSize;
	String mapColName;
	String roadCheckTypeId;
	String dataEntered;
	
	public ExcerptParamMappingBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
	public ExcerptParamMappingBO(ExcerptParamMappingDO excerptParamMappingDO) {
		super();
		this.paramMapId = excerptParamMappingDO.getParamMapId();
		this.paramName = excerptParamMappingDO.getParamName();
		this.paramDescription = excerptParamMappingDO.getParamDescription();
		this.paramSize = excerptParamMappingDO.getParamSize();
		this.mapColName = excerptParamMappingDO.getMapColName();
		
		if(excerptParamMappingDO.getRoadCheckType() != null)
			{this.roadCheckTypeId = excerptParamMappingDO.getRoadCheckType().getRoadCheckTypeId();}
		this.dataEntered = excerptParamMappingDO.getDataEntered();
	}

	public Integer getParamMapId() {
		return paramMapId;
	}

	public void setParamMapId(Integer paramMapId) {
		this.paramMapId = paramMapId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamDescription() {
		return paramDescription;
	}

	public void setParamDescription(String paramDescription) {
		this.paramDescription = paramDescription;
	}

	public Integer getParamSize() {
		return paramSize;
	}

	public void setParamSize(Integer paramSize) {
		this.paramSize = paramSize;
	}

	public String getMapColName() {
		return mapColName;
	}

	public void setMapColName(String mapColName) {
		this.mapColName = mapColName;
	}

	public String getRoadCheckTypeId() {
		return roadCheckTypeId;
	}

	public void setRoadCheckTypeId(String roadCheckTypeId) {
		this.roadCheckTypeId = roadCheckTypeId;
	}

	public String getDataEntered() {
		return dataEntered;
	}

	public void setDataEntered(String dataEntered) {
		this.dataEntered = dataEntered;
	}

	
	
	
	}
