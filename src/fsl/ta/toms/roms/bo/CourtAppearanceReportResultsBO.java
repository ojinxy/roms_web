/**
 * Created By: oanguin
 * Date: Jul 11, 2013
 *
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author oanguin
 * Created Date: Jul 11, 2013
 */
public class CourtAppearanceReportResultsBO implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String courtAppearanceStatus;
	
	String plea;
	
	String comment;
	
	Date courtDateTime;
	
	String courtRuling;
	
	String courtDetails;

	public String getCourtAppearanceStatus() {
		return courtAppearanceStatus;
	}

	public void setCourtAppearanceStatus(String courtAppearanceStatus) {
		this.courtAppearanceStatus = courtAppearanceStatus;
	}

	public String getPlea() {
		return plea;
	}

	public void setPlea(String plea) {
		this.plea = plea;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCourtDateTime() {
		return courtDateTime;
	}

	public void setCourtDateTime(Date courtDateTime) {
		this.courtDateTime = courtDateTime;
	}

	public String getCourtRuling() {
		return courtRuling;
	}

	public void setCourtRuling(String courtRuling) {
		this.courtRuling = courtRuling;
	}

	public String getCourtDetails() {
		return courtDetails;
	}

	public void setCourtDetails(String courtDetails) {
		this.courtDetails = courtDetails;
	}

	public CourtAppearanceReportResultsBO(String courtAppearanceStatus,
			String plea, String comment, Date courtDateTime,
			String courtRuling, String courtDetails) {
		super();
		this.courtAppearanceStatus = courtAppearanceStatus;
		this.plea = plea;
		this.comment = comment;
		this.courtDateTime = courtDateTime;
		this.courtRuling = courtRuling;
		this.courtDetails = courtDetails;
	}

	public CourtAppearanceReportResultsBO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
