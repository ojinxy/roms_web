package fsl.ta.toms.roms.search.criteria.impl;

import java.util.HashMap;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

/**
 * @author oanguin
 * Created Date: Apr 17, 2013
 */
public class RefCodeCriteriaBO implements SearchCriteria
{
	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;
	private String tableName;
	private String status;
	private HashMap<String,String> filter;
	
	public RefCodeCriteriaBO()
	{
		
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public HashMap<String, String> getFilter() {
		return filter;
	}
	public void setFilter(HashMap<String, String> filter) {
		this.filter = filter;
	}
	public RefCodeCriteriaBO(String tableName,
			String status, HashMap<String, String> filter) {
		super();
		this.tableName = tableName;
		this.status = status;
		this.filter = filter;
	}
	
	
}
