package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class OffenceCriteriaBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5424357948075393271L;
	
	private Integer offenceId;
	private String roadCheckTypeId;
	private String offenceDescription;
	private String shortDescription;
	private String statusId;
	
	public OffenceCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getOffenceId() {
		return offenceId;
	}

	public void setOffenceId(Integer offenceId) {
		this.offenceId = offenceId;
	}

	public String getRoadCheckTypeId() {
		return roadCheckTypeId;
	}

	public void setRoadCheckTypeId(String roadCheckTypeId) {
		if(StringUtils.isNotBlank(roadCheckTypeId))
			this.roadCheckTypeId = roadCheckTypeId.trim();
	}

	public String getOffenceDescription() {
		return offenceDescription;
	}

	public void setOffenceDescription(String offenceDescription) {
		if(StringUtils.isNotBlank(offenceDescription))
			this.offenceDescription = offenceDescription.trim();
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		if(StringUtils.isNotBlank(shortDescription))
			this.shortDescription = shortDescription.trim();
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}
	

}
