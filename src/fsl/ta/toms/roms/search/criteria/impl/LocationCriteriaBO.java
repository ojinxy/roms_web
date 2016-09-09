package fsl.ta.toms.roms.search.criteria.impl;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;
/**
 * 
 * @author jreid
 * Created Date: Jun 4, 2013
 */
public class LocationCriteriaBO implements SearchCriteria {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3645882642921136335L;
	private String locationDescription;
	private Integer locationId;
	private String parishCode;
	private String shortDesc;
	private  String statusId;
	
	public LocationCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public String getParishCode() {
		return parishCode;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setLocationDescription(String locationDescription) {
		if(StringUtils.isNotBlank(locationDescription))
			this.locationDescription = locationDescription.trim();
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public void setParishCode(String parishCode) {
		if(StringUtils.isNotBlank(parishCode))
			this.parishCode = parishCode.trim();
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
