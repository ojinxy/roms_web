/**
 * Created By: oanguin
 * Date: Jun 19, 2013
 *
 */
package fsl.ta.toms.roms.search.criteria.impl;

import java.util.Date;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;

/**
 * @author oanguin
 * Created Date: Jun 19, 2013
 */
public class CourtCaseAppearanceCriteriaBO implements SearchCriteria 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer trialId,summonsId,courtId,pleaId;
	
	private String courtRulingId,						   
				   statusCode,
				   courtTime;
	
	private Date courtDate;

	public Integer getTrialId() {
		return trialId;
	}

	public void setTrialId(Integer trialId) {
		this.trialId = trialId;
	}

	public Integer getSummonsId() {
		return summonsId;
	}

	public void setSummonsId(Integer summonsId) {
		this.summonsId = summonsId;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Integer getPleaId() {
		return pleaId;
	}

	public void setPleaId(Integer pleaId) {
		this.pleaId = pleaId;
	}

	public String getCourtRulingId() {
		return courtRulingId;
	}

	public void setCourtRulingId(String courtRulingId) {
		this.courtRulingId = courtRulingId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Date getCourtDate() {
		return courtDate;
	}

	public void setCourtDate(Date courtDate) {
		this.courtDate = courtDate;
	}

	public CourtCaseAppearanceCriteriaBO(Integer trialId, Integer summonsId, Integer courtId,
			Integer pleaId, String courtRulingId, String statusCode,
			Date courtDate) {
		super();
		this.trialId = trialId;
		this.summonsId = summonsId;
		this.courtId = courtId;
		this.pleaId = pleaId;
		this.courtRulingId = courtRulingId;
		this.statusCode = statusCode;
		this.courtDate = courtDate;
	}

	public CourtCaseAppearanceCriteriaBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCourtTime() {
		return courtTime;
	}

	public void setCourtTime(String courtTime) {
		this.courtTime = courtTime;
	}

	public CourtCaseAppearanceCriteriaBO(Integer trialId, Integer summonsId, Integer courtId,
			Integer pleaId, String courtRulingId, String statusCode,
			String courtTime, Date courtDate) {
		super();
		this.trialId = trialId;
		this.summonsId = summonsId;
		this.courtId = courtId;
		this.pleaId = pleaId;
		this.courtRulingId = courtRulingId;
		this.statusCode = statusCode;
		this.courtTime = courtTime;
		this.courtDate = courtDate;
	}
	
	

}
