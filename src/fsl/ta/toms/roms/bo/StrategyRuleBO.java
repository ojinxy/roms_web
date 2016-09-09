package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.StrategyRuleDO;

public class StrategyRuleBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3893191508655343519L;

	Integer strategyId;
	String personTypeId;
	String personTypeDescription;
	Integer minimum;
	Integer maximum;
	
	public StrategyRuleBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public StrategyRuleBO(String personTypeId, Integer minimum, Integer maximum) {
		super();
		this.personTypeId = personTypeId;
		this.minimum = minimum;
		this.maximum = maximum;
	}



	public StrategyRuleBO(StrategyRuleDO strategyRuleDO) {
		
		
		if(strategyRuleDO.getStrategyRuleKey()!=null){
			if(strategyRuleDO.getStrategyRuleKey().getStrategy()!=null){
				this.strategyId = strategyRuleDO.getStrategyRuleKey().getStrategy().getStrategyId();
			}
			
			if(strategyRuleDO.getStrategyRuleKey().getPersonType()!=null){
				this.personTypeId = strategyRuleDO.getStrategyRuleKey().getPersonType().getPersonTypeId();
				this.personTypeDescription = strategyRuleDO.getStrategyRuleKey().getPersonType().getDescription();
				
			}
		}
		
		this.minimum = strategyRuleDO.getMinimum();
		this.maximum = strategyRuleDO.getMaximum();
	}



	public Integer getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}

	public String getPersonTypeId() {
		return personTypeId;
	}

	public void setPersonTypeId(String personTypeId) {
		if(StringUtils.isNotBlank(personTypeId))
			this.personTypeId = personTypeId.trim();
	}

	public Integer getMinimum() {
		return minimum;
	}

	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}

	public Integer getMaximum() {
		return maximum;
	}

	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}



	public String getPersonTypeDescription() {
		return personTypeDescription;
	}



	public void setPersonTypeDescription(String personTypeDescription) {
		if(StringUtils.isNotBlank(personTypeDescription))
			this.personTypeDescription = personTypeDescription.trim();
	}
	
	
}
