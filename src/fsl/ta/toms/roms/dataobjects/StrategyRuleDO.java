package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_strategy_rule")
public class StrategyRuleDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1543415610159824421L;

	public StrategyRuleDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@EmbeddedId
	StrategyRuleKey strategyRuleKey;
	
	@Column(name="minimum")
	Integer minimum;
	
	@Column(name="maximum")
	Integer maximum;
	
	//@ManyToOne
	//@JoinColumn(name="status_id")
	//StatusDO status;
	
	@Embedded
	AuditEntry auditEntry;
	
	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	

	public StrategyRuleKey getStrategyRuleKey() {
		return strategyRuleKey;
	}

	public void setStrategyRuleKey(StrategyRuleKey strategyRuleKey) {
		this.strategyRuleKey = strategyRuleKey;
	}

	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}

	
}
