package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ROMS_citation_offence_view")
public class ROMS_CitationOffenceView implements Serializable {

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 2351901401259759717L;

	public ROMS_CitationOffenceView() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="offence_date_time")
	Date offenceDateTime;
	
	@Column(name="road_check_offence_id")
	Integer roadCheckOffenceID;
	
	@Column(name="offence_description")
	String offenceDescription;
	
	@Column(name="offence_short_desc")
	String offenceShortDesc;
	
	@Column(name="offence_type")
	String offenceType;
	
	@Column(name="court_date")
	Date courtDate;
	
	@Column(name="court_outcome")
	String courtOutcome;
	
	@Column(name="plate_number")
	String plateNumber;
	
	@Column(name="trn_nbr")
	String trnNbr;
	
	@Column(name="dl_no")
	String dlNo;
	
	@Column(name="case_status")
	String caseStatus;
	
	@Column(name="verdict_desc")
	String verdictDesc;
	
	@Column(name="person_role")
	String personRole;
	
	@Column(name="other_role_observed_id")
	String otherRoleId;
	
	@Column(name="other_role_observed_desc")
	String otherRoleDesc;
	
	
	@Column(name="offender_first_name")
	String offenderFirstName;
	
	@Column(name="offender_last_name")
	String offenderLastName;
	
	@Column(name="offender_mid_name")
	String offenderMidName;

	public Date getOffenceDateTime() {
		return offenceDateTime;
	}

	public void setOffenceDateTime(Date offenceDateTime) {
		this.offenceDateTime = offenceDateTime;
	}

	public Integer getRoadCheckOffenceID() {
		return roadCheckOffenceID;
	}

	public void setRoadCheckOffenceID(Integer roadCheckOffenceID) {
		this.roadCheckOffenceID = roadCheckOffenceID;
	}

	public String getOffenceDescription() {
		return offenceDescription;
	}

	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}

	public String getOffenceShortDesc() {
		return offenceShortDesc;
	}

	public void setOffenceShortDesc(String offenceShortDesc) {
		this.offenceShortDesc = offenceShortDesc;
	}

	public String getOffenceType() {
		return offenceType;
	}

	public void setOffenceType(String offenceType) {
		this.offenceType = offenceType;
	}

	public Date getCourtDate() {
		return courtDate;
	}

	public void setCourtDate(Date courtDate) {
		this.courtDate = courtDate;
	}

	public String getCourtOutcome() {
		return courtOutcome;
	}

	public void setCourtOutcome(String courtOutcome) {
		this.courtOutcome = courtOutcome;
	}

	public String getTrnNbr() {
		return trnNbr;
	}

	public void setTrnNbr(String trnNbr) {
		this.trnNbr = trnNbr;
	}

	public String getDlNo() {
		return dlNo;
	}

	public void setDlNo(String dlNo) {
		this.dlNo = dlNo;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getVerdictDesc() {
		return verdictDesc;
	}

	public void setVerdictDesc(String verdictDesc) {
		this.verdictDesc = verdictDesc;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getPersonRole()
	{
		return personRole;
	}

	public void setPersonRole(String personRole)
	{
		this.personRole = personRole;
	}

	public String getOffenderFirstName()
	{
		return offenderFirstName;
	}

	public void setOffenderFirstName(String offenderFirstName)
	{
		this.offenderFirstName = offenderFirstName;
	}

	public String getOffenderLastName()
	{
		return offenderLastName;
	}

	public void setOffenderLastName(String offenderLastName)
	{
		this.offenderLastName = offenderLastName;
	}

	public String getOffenderMidName()
	{
		return offenderMidName;
	}

	public void setOffenderMidName(String offenderMidName)
	{
		this.offenderMidName = offenderMidName;
	}

	public String getOtherRoleId() {
		return otherRoleId;
	}

	public void setOtherRoleId(String otherRoleId) {
		this.otherRoleId = otherRoleId;
	}

	public String getOtherRoleDesc()
	{
		return otherRoleDesc;
	}

	public void setOtherRoleDesc(String otherRoleDesc)
	{
		this.otherRoleDesc = otherRoleDesc;
	}


	

}
