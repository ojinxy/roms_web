package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class ReasonCriteriaBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1609855939263584556L;
	
	private Integer reasonId;
	private String reasonDescription;
	private String type;
	private String statusId;
	
	public ReasonCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		if(StringUtils.isNotBlank(reasonDescription))
			this.reasonDescription = reasonDescription.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if(StringUtils.isNotBlank(type))
			this.type = type.trim();
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}
	
	
}
