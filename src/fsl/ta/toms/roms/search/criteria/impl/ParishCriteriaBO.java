package fsl.ta.toms.roms.search.criteria.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

public class ParishCriteriaBO implements SearchCriteria {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8106577323109687964L;

	String description;

	List<String> officeLocationCode = new ArrayList<String>();

	
	String parishCode;

	String statusId;

	public ParishCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getDescription() {
		return description;
	}
	

	

		public List<String> getOfficeLocationCode() {
		return officeLocationCode;
	}


	public void setOfficeLocationCode(List<String> officeLocationCode) {
		this.officeLocationCode = officeLocationCode;
	}


		public String getParishCode() {
		return parishCode;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setDescription(String description) {
		if(StringUtils.isNotBlank(description))
			this.description = description.trim();
	}

	

	public void setParishCode(String parishCode) {
		if(StringUtils.isNotBlank(parishCode))
			this.parishCode = parishCode.trim();
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	

}
