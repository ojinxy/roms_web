/**
 * Created By: oanguin
 * Date: Apr 26, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.util.DateUtils;

/**
 * @author oanguin
 * Created Date: Apr 26, 2013
 */
public class CourtAppearanceBO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer courtAppearanceId,summonsId,courtId,pleaId,courtCaseId,verdictId;
	
	private String courtRulingId, verdictDescription,
				   courtRulingDescription,
				   pleaDescription,
				   statusDescription,
				   courtTime,
				   courtName,
				   comment,				   
				   statusCode,
				   currentUserName;
	
	
	private Date courtDate;
	
	private boolean finalAppearance;
	
	private String inspectorAttended;


	public Integer getVerdictId() {
		return verdictId;
	}

	public void setVerdictId(Integer verdictId) {
		this.verdictId = verdictId;
	}

	public Integer getCourtCaseId() {
		return courtCaseId;
	}

	public void setCourtCaseId(Integer courtCaseId) {
		this.courtCaseId = courtCaseId;
	}

	public String getCourtRulingDescription() {
		return courtRulingDescription;
	}

	public void setCourtRulingDescription(String courtRulingDescription) {
		this.courtRulingDescription = courtRulingDescription;
	}

	public String getPleaDescription() {
		return pleaDescription;
	}

	public void setPleaDescription(String pleaDescription) {
		this.pleaDescription = pleaDescription;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public Integer getCourtAppearanceId() {
		return courtAppearanceId;
	}

	public void setCourtAppearanceId(Integer courtAppearanceId) {
		this.courtAppearanceId = courtAppearanceId;
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

	public String getCourtTime() {
		return courtTime;
	}

	public void setCourtTime(String courtTime) {
		this.courtTime = courtTime;
	}

	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getCurrentUserName() {
		return currentUserName;
	}

	public void setCurrentUserName(String userName) {
		this.currentUserName = userName;
	}

	public Date getCourtDate() {
		return courtDate;
	}

	public void setCourtDate(Date courtDateNTime) {
		this.courtDate = courtDateNTime;
	}

	
	/**
	 * @return the courtName
	 */
	public String getCourtName() {
		return courtName;
	}

	/**
	 * @param courtName the courtName to set
	 */
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the finalAppearance
	 */
	public boolean isFinalAppearance() {
		return finalAppearance;
	}

	/**
	 * @param finalAppearance the finalAppearance to set
	 */
	public void setFinalAppearance(boolean finalAppearance) {
		this.finalAppearance = finalAppearance;
	}
	
	

	public CourtAppearanceBO(Integer courtAppearanceId, Integer summonsId, Integer courtId,
			Integer pleaId, String courtRulingId, String courtTime,
			String comments, String statusCode,  String userName, Date courtDate, String inspectorAttended) {
		super();
		this.courtAppearanceId = courtAppearanceId;
		this.summonsId = summonsId;
		this.courtId = courtId;
		this.pleaId = pleaId;
		this.courtRulingId = courtRulingId;
		this.courtTime = courtTime;
		this.comment = comments;
		this.statusCode = statusCode;
		this.setInspectorAttended(inspectorAttended);
		this.currentUserName = userName;
		this.courtDate = courtDate;
	}

	
	
	public CourtAppearanceBO(Integer courtAppearanceId, Integer summonsId, Integer courtId,
			Integer pleaId, String courtRulingId,
			String courtRulingDescription, String pleaDescription,
			String statusDescription, String courtTime, String comments,
			String statusCode,Date courtDate, String inspectorAttended) {
		super();
		this.courtAppearanceId = courtAppearanceId;
		this.summonsId = summonsId;
		this.courtId = courtId;
		this.pleaId = pleaId;
		this.courtRulingId = courtRulingId;
		this.courtRulingDescription = courtRulingDescription;
		this.pleaDescription = pleaDescription;
		this.statusDescription = statusDescription;
		this.setInspectorAttended(inspectorAttended);
		this.courtTime = courtTime;
		this.comment = comments;
		this.statusCode = statusCode;
		this.courtDate = courtDate;
	}
	
	public CourtAppearanceBO(Integer courtAppearanceId, Integer summonsId, Integer courtId,
			Integer pleaId, String courtRulingId,
			String courtRulingDescription, String pleaDescription,
			String statusDescription,  String comments,
			String statusCode,String inspectorAttended) {
		super();
		this.courtAppearanceId = courtAppearanceId;
		this.summonsId = summonsId;
		this.courtId = courtId;
		this.pleaId = pleaId;
		this.courtRulingId = courtRulingId;
		this.courtRulingDescription = courtRulingDescription;
		this.pleaDescription = pleaDescription;
		this.statusDescription = statusDescription;
		this.setInspectorAttended(inspectorAttended);
		this.comment = comments;
		this.statusCode = statusCode;
		
	}


	
	public CourtAppearanceBO()
	{
		super();
		
	}
	
	/**
	 * 
	 * @param summons
	 */
	public CourtAppearanceBO(SummonsBO summons) {
		this.summonsId= summons.getAutomaticSerialNo();
		
		this.statusCode = Constants.Status.ACTIVE;
		
		this.courtDate = summons.getCourtDtime();
		this.courtTime =DateUtils.getTimeFromDate(summons.getCourtDtime());	
		
		this.courtId = summons.getCourtCode();
		this.setCurrentUserName(summons.getCurrentUsername());
		this.setCourtTime(summons.getCourtTime());
		
	}
	
	/**
	 * 
	 * @param summons
	 */
	public CourtAppearanceBO(CourtAppearanceDO courtAppearance) {
		
		this.statusCode = courtAppearance.getStatus().getStatusId();
		this.statusDescription = courtAppearance.getStatus().getDescription();
				
		this.courtDate = courtAppearance.getCourtDTime();
		this.courtTime =DateUtils.getTimeFromDate(courtAppearance.getCourtDTime());	
		this.courtId = courtAppearance.getCourt().getCourtId();
		this.courtName = courtAppearance.getCourt().getShortDesc();// changed from description to short description jreid 2014-06-26
		this.courtAppearanceId = courtAppearance.getCourtAppearanceId();
		this.comment = courtAppearance.getComment();
		this.courtCaseId = courtAppearance.getCourtCase().getCourtCaseId();
		this.inspectorAttended = courtAppearance.getInspectorAttended();
		
		if(courtAppearance.getCourtRuling() != null){
			this.courtRulingId = courtAppearance.getCourtRuling().getRulingId();
			this.courtRulingDescription = courtAppearance.getCourtRuling().getDescription();
			this.finalAppearance = (courtAppearance.getCourtRuling().getFinalRuling() != null && courtAppearance.getCourtRuling().getFinalRuling().equals(Constants.YesNo.YES)? true : false );
		}
		
		if(courtAppearance.getPlea() != null){
			this.pleaId = courtAppearance.getPlea().getPleaId();
			this.pleaDescription = courtAppearance.getPlea().getDescription();
		}		
	}

	public String getInspectorAttended() {
		return inspectorAttended;
	}

	public void setInspectorAttended(String inspectorAttended) {
		this.inspectorAttended = inspectorAttended;
	}


	
}
