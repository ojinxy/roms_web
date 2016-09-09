package fsl.ta.toms.roms.search.criteria.impl;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;
/**
 * 
 * @author jreid
 * Created Date: Jun 4, 2013
 */
public class GoverningLawCriteriaBO implements SearchCriteria  {

	
	private static final long serialVersionUID = 593628627843819751L;

	String description;

	Integer lawId;

	String shortDesc;

	String statusId;

	public GoverningLawCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getDescription() {
		return description;
	}

	public Integer getLawId() {
		return lawId;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setDescription(String description) {
		if(StringUtils.isNotBlank(description))
			this.description = description.trim();
	}

	public void setLawId(Integer lawId) {
		this.lawId = lawId;
	}

	public void setShortDesc(String shortDesc) {
		if(StringUtils.isNotBlank(shortDesc))
			this.shortDesc = shortDesc.trim();
	}

	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}

	

	
}
