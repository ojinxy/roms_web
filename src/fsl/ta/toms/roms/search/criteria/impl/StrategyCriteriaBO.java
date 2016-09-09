package fsl.ta.toms.roms.search.criteria.impl;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class StrategyCriteriaBO implements Serializable{

	/**
	 * 
	 */

	private static final long serialVersionUID = -3354262505653755088L;

	private Integer strategyId;
	private String strategyDescription;
	private String statusId;
		
	
	public StrategyCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Integer getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}
	public String getStrategyDescription() {
		return strategyDescription;
	}
	public void setStrategyDescription(String strategyDescription) {
		if(StringUtils.isNotBlank(strategyDescription))
			this.strategyDescription = strategyDescription.trim();
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}
	
	
}
