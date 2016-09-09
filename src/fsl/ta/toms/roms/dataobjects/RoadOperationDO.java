package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_road_operation")
public class RoadOperationDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4982747727673948340L;

	public RoadOperationDO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@Column(name="road_operation_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer roadOperationId;
	
	@Column(name="operation_name")
	String operationName;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	CDCategoryDO category;
	
	
	@ManyToOne
	@JoinColumn(name="scheduler_id")
	TAStaffDO scheduler;
	
	@Column(name="office_loc_code")
	String officeLocCode;
	
	@Column(name="sch_start_date_time")
	Date scheduledStartDtime;
	
	@Column(name="sch_end_date_time")
	Date scheduledEndDtime;
	
	@Column(name="actual_start_date_time")
	Date actualStartDtime;
	
	@Column(name="actual_end_date_time")
	Date actualEndDtime;
	
	@Column(name="back_dated")
	String backDated;
	
	
	@ManyToOne
	@JoinColumn(name="status_id")
	StatusDO status;
	
	@Column(name="status_dtime")
	Date statusDtime;
	
	@Column(name="authorized")
	String authorized;
	
	@Column(name="authorize_username")
	String authorizedUserName;
	
	@Column(name="authorize_dtime")
	Date authorizedDtime;
	
	@ManyToOne
	@JoinColumn(name="reason_id")
	ReasonDO reason;
	
	@Column(name="comment")
	String comment;
	
	@ManyToOne
	@JoinColumn(name="court_id")
	CourtDO court;
	
	@Column(name="court_dtime")
	Date courtDtime;
	
	@Embedded
	AuditEntry auditEntry;

	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getRoadOperationId() {
		return roadOperationId;
	}

	public void setRoadOperationId(Integer roadOperationId) {
		this.roadOperationId = roadOperationId;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public CDCategoryDO getCategory() {
		return category;
	}

	public void setCategory(CDCategoryDO category) {
		this.category = category;
	}


	public TAStaffDO getScheduler() {
		return scheduler;
	}

	public void setScheduler(TAStaffDO scheduler) {
		this.scheduler = scheduler;
	}

	public AuditEntry getAuditEntry() {
		return auditEntry;
	}

	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}

	public String getOfficeLocCode() {
		return officeLocCode;
	}

	public void setOfficeLocCode(String officeLocCode) {
		this.officeLocCode = officeLocCode;
	}

	public Date getScheduledStartDtime() {
		return scheduledStartDtime;
	}

	public void setScheduledStartDtime(Date scheduledStartDtime) {
		this.scheduledStartDtime = scheduledStartDtime;
	}

	public Date getScheduledEndDtime() {
		return scheduledEndDtime;
	}

	public void setScheduledEndDtime(Date scheduledEndDtime) {
		this.scheduledEndDtime = scheduledEndDtime;
	}

	public Date getActualStartDtime() {
		return actualStartDtime;
	}

	public void setActualStartDtime(Date actualStartDtime) {
		this.actualStartDtime = actualStartDtime;
	}

	public Date getActualEndDtime() {
		return actualEndDtime;
	}

	public void setActualEndDtime(Date actualEndDtime) {
		this.actualEndDtime = actualEndDtime;
	}

	public String getBackDated() {
		return backDated;
	}

	public void setBackDated(String backDated) {
		this.backDated = backDated;
	}

	public StatusDO getStatus() {
		return status;
	}

	public void setStatus(StatusDO status) {
		this.status = status;
	}

	public Date getStatusDtime() {
		return statusDtime;
	}

	public void setStatusDtime(Date statusDtime) {
		this.statusDtime = statusDtime;
	}

	public String getAuthorized() {
		return authorized;
	}

	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}

	public String getAuthorizedUserName() {
		return authorizedUserName;
	}

	public void setAuthorizedUserName(String authorizedUserName) {
		this.authorizedUserName = authorizedUserName;
	}

	public Date getAuthorizedDtime() {
		return authorizedDtime;
	}

	public void setAuthorizedDtime(Date authorizedDtime) {
		this.authorizedDtime = authorizedDtime;
	}

	public ReasonDO getReason() {
		return reason;
	}

	public void setReason(ReasonDO reason) {
		this.reason = reason;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}

	public CourtDO getCourt() {
		return court;
	}

	public void setCourt(CourtDO court) {
		this.court = court;
	}

	public Date getCourtDtime() {
		return courtDtime;
	}

	public void setCourtDtime(Date courtDtime) {
		this.courtDtime = courtDtime;
	}

	
	

}
