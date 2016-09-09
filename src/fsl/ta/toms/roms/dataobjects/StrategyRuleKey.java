package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class StrategyRuleKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5845471350487523832L;

	public StrategyRuleKey() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@ManyToOne	
	@JoinColumn(name="strategy_id")
	StrategyDO strategy;
	
	@ManyToOne
	@JoinColumn(name="person_type_id")
	CDPersonTypeDO personType;

	public StrategyDO getStrategy() {
		return strategy;
	}

	public void setStrategy(StrategyDO strategy) {
		this.strategy = strategy;
	}

	public CDPersonTypeDO getPersonType() {
		return personType;
	}

	public void setPersonType(CDPersonTypeDO personType) {
		this.personType = personType;
	}
	
	

}
