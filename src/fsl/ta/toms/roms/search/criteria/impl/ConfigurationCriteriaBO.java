package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author jreid
 * Created Date: May 24, 2013
 */
public class ConfigurationCriteriaBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6429334793500664407L;

	String cfgCode;

	Character dataType;

	String description;

	Date endDate;

	Character selectable;

	Date startDate;

	String statusId;

	String value;
	
	public ConfigurationCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public String getCfgCode() {
		return cfgCode;
	}

	public Character getDataType() {
		return dataType;
	}

	public String getDescription() {
		return description;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Character getSelectable() {
		return selectable;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getStatusId() {
		return statusId;
	}

	public String getValue() {
		return value;
	}

	public void setCfgCode(String cfgCode) {
		if(StringUtils.isNotBlank(cfgCode))
			this.cfgCode = cfgCode.trim();
	}

	public void setDataType(Character dataType) {
		this.dataType = dataType;
	}

	public void setDescription(String description) {
		if(StringUtils.isNotBlank(description))
			this.description = description.trim();
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setSelectable(Character selectable) {
		this.selectable = selectable;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	public void setValue(String value) {
		if(StringUtils.isNotBlank(value))
			this.value = value.trim();
	}

	


	
}
