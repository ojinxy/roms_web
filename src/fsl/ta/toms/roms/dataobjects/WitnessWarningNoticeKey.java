/**
 * Created By: oanguin
 * Date: May 9, 2013
 *
 */
package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author oanguin
 * Created Date: May 9, 2013
 */
@Embeddable
public class WitnessWarningNoticeKey implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1674585868656290973L;

	@ManyToOne
	@JoinColumn(name="witness_id")
	PersonDO witness;
	
	@ManyToOne
	@JoinColumn(name="warning_notice_id")
	WarningNoticeDO warningNotice;

	
	
	public WitnessWarningNoticeKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WitnessWarningNoticeKey(PersonDO witness,
			WarningNoticeDO warningNotice) {
		super();
		this.witness = witness;
		this.warningNotice = warningNotice;
	}

	public PersonDO getWitness() {
		return witness;
	}

	public void setWitness(PersonDO witness) {
		this.witness = witness;
	}

	public WarningNoticeDO getWarningNotice() {
		return warningNotice;
	}

	public void setWarningNotice(WarningNoticeDO warningNotice) {
		this.warningNotice = warningNotice;
	}
	
	
	
	
}
