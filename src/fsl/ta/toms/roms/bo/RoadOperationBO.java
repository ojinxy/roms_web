package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.RoadOperationDO;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.NameUtil;

public class RoadOperationBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6641632847492709426L;

	private Integer roadOperationId;
	private String operationName;

	private String categoryId;	
	private String categoryDescription;

	/*private String teamLeadStaffId;
	private String teamLeadFirstName;
	private String teamLeadMiddleName;
	private String teamLeadLastName;
	private String teamLeadFullName;*/
	
	private TAStaffBO scheduler;
	
	private String officeLocCode;
	
	/*private Date scheduledStartDtime;
	private Date scheduledEndDtime;
	private Date actualStartDtime;
	private Date actualEndDtime;
	*/
	
	private Date scheduledStartDate;
	private String scheduledStartTime;
	private Date scheduledStartDtime;
	private Date scheduledEndDate;
	private String scheduledEndTime;
	private Date scheduledEndDtime;
	
	private Date actualStartDate;
	private String actualStartTime;
	private Date actualStartDtime;
	private Date actualEndDate;
	private String actualEndTime;
	private Date actualEndDtime;
	
	private String backDated;

	private String statusId;
	private String statusDescription;
	private Date statusDtime;
	
	private String authorized;
	private String authorizedUserName;
	private Date authorizedDtime;
	
	private Integer reasonId;
	private String comment;
	
	private CourtBO courtBO;
	private Date courtDate;
	private String courtTime;
	private Date courtDTime;
	private Date dateCreated;
	
	private String currentUsername;
	
	
	private List<StrategyBO> operationStrategyList = new ArrayList<StrategyBO>();
	
	private List<String> officeLocCodeList = new ArrayList<String>();
	
	private Date firstOffenceDate;
	private Date lastOffenceDate;
	
	public RoadOperationBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoadOperationBO(RoadOperationDO roadOperationDO) {
		if(roadOperationDO!=null){
			this.roadOperationId = roadOperationDO.getRoadOperationId();
			this.operationName = roadOperationDO.getOperationName();
			
			if(roadOperationDO.getCategory()!=null){
				this.categoryId = roadOperationDO.getCategory().getCategoryId();
				this.categoryDescription = roadOperationDO.getCategory().getDescription();
				
			}
			NameUtil util = new NameUtil();
			/*if(roadOperationDO.getTeamLead()!=null){
				this.teamLeadStaffId = roadOperationDO.getTeamLead().getStaffId();
				if(roadOperationDO.getTeamLead().getPerson()!=null){
					this.teamLeadFirstName = roadOperationDO.getTeamLead().getPerson().getFirstName();
					this.teamLeadMiddleName = roadOperationDO.getTeamLead().getPerson().getMiddleName();
					this.teamLeadLastName = roadOperationDO.getTeamLead().getPerson().getLastName();
					
					this.teamLeadFullName= util.getFullName(teamLeadFirstName, teamLeadLastName);
				}
			}*/
			
			if(roadOperationDO.getScheduler()!=null){
				this.scheduler = new TAStaffBO(roadOperationDO.getScheduler());
			}
			
	
			this.officeLocCode = roadOperationDO.getOfficeLocCode();
				
			if(roadOperationDO.getScheduledStartDtime()!=null){
			
				//String formatDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationDO.getScheduledStartDtime());
				//this.scheduledStartDate = DateUtils.parse(formatDate, "yyyy-MM-dd");
				this.scheduledStartDate = roadOperationDO.getScheduledStartDtime();
				
				String formatTime = DateUtils.formatDate("HH:mm", roadOperationDO.getScheduledStartDtime());
				this.scheduledStartTime = formatTime;
					
				//this.scheduledStartDtime = formatDate + " " + formatTime;
				this.scheduledStartDtime=roadOperationDO.getScheduledStartDtime();
			}
			
			if(roadOperationDO.getScheduledEndDtime()!=null){
				//String formatDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationDO.getScheduledEndDtime());
				//this.scheduledEndDate = DateUtils.parse(formatDate, "yyyy-MM-dd");
				this.scheduledEndDate = roadOperationDO.getScheduledEndDtime();
				
				String formatTime = DateUtils.formatDate("HH:mm", roadOperationDO.getScheduledEndDtime());
				this.scheduledEndTime = formatTime;
				
				//this.scheduledEndDtime = formatDate + " " + formatTime;
				this.scheduledEndDtime = roadOperationDO.getScheduledEndDtime();
			}
			
			if(roadOperationDO.getActualStartDtime()!=null){
				//String formatDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationDO.getActualStartDtime());
				//this.actualStartDate= DateUtils.parse(formatDate, "yyyy-MM-dd");
				this.actualStartDate= roadOperationDO.getActualStartDtime();
				
				String formatTime = DateUtils.formatDate("HH:mm", roadOperationDO.getActualStartDtime());
				this.actualStartTime = formatTime;
				
				//this.actualStartDtime = formatDate + " " + formatTime;
				this.actualStartDtime = roadOperationDO.getActualStartDtime();
			}
			
			if(roadOperationDO.getActualEndDtime()!=null){
				//String formatDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationDO.getActualEndDtime());
				//this.actualEndDate = DateUtils.parse(formatDate, "yyyy-MM-dd");
				this.actualEndDate = roadOperationDO.getActualEndDtime();
				
				String formatTime = DateUtils.formatDate("HH:mm", roadOperationDO.getActualEndDtime());
				this.actualEndTime = formatTime;
				
				//this.actualEndDtime = formatDate + " " + formatTime;
				this.actualEndDtime = roadOperationDO.getActualEndDtime();
			}
		
			
			this.backDated = roadOperationDO.getBackDated();
			
			if( roadOperationDO.getStatus() != null){
				this.statusId = roadOperationDO.getStatus().getStatusId();
				this.statusDescription = roadOperationDO.getStatus().getDescription();
			}
	
			this.statusDtime = roadOperationDO.getStatusDtime();
			this.authorized = roadOperationDO.getAuthorized();
			this.authorizedUserName = roadOperationDO.getAuthorizedUserName();
			this.authorizedDtime = roadOperationDO.getAuthorizedDtime();
			if(roadOperationDO.getReason()!=null){
				this.reasonId = roadOperationDO.getReason().getReasonId();
			}
			
			this.comment = roadOperationDO.getComment();
			
			if(roadOperationDO.getCourt()!=null){
				courtBO = new CourtBO(roadOperationDO.getCourt()); 
			}
			
			if(roadOperationDO.getCourtDtime()!=null){
				String formatDate = DateUtils.formatDate("yyyy-MM-dd", roadOperationDO.getCourtDtime());
				this.courtDate = DateUtils.parse(formatDate, "yyyy-MM-dd");
			
				String formatTime = DateUtils.formatDate("HH:mm", roadOperationDO.getCourtDtime());
				this.courtTime = formatTime;
				
				String formatDateTime = DateUtils.formatDate("yyyy-MM-dd hh:mm a", roadOperationDO.getCourtDtime());
				this.courtDTime = DateUtils.parse(formatDateTime, "yyyy-MM-dd hh:mm a");
			
				
			}
			
			this.dateCreated = roadOperationDO.getAuditEntry().getCreateDTime();
		}
	}

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
		if(StringUtils.isNotBlank(operationName))
			this.operationName = operationName.trim();
	}


	public String getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(String categoryId) {
		if(StringUtils.isNotBlank(categoryId))
			this.categoryId = categoryId.trim();
	}


	public String getCategoryDescription() {
		return categoryDescription;
	}


	public void setCategoryDescription(String categoryDescription) {
		if(StringUtils.isNotBlank(categoryDescription))
			this.categoryDescription = categoryDescription.trim();
	}


	/*public String getTeamLeadStaffId() {
		return teamLeadStaffId;
	}


	public void setTeamLeadStaffId(String teamLeadStaffId) {
		if(StringUtils.isNotBlank(teamLeadStaffId))
			this.teamLeadStaffId = teamLeadStaffId.trim();
	}


	public String getTeamLeadFirstName() {
		return teamLeadFirstName;
	}


	public void setTeamLeadFirstName(String teamLeadFirstName) {
		if(StringUtils.isNotBlank(teamLeadFirstName))
			this.teamLeadFirstName = teamLeadFirstName.trim();
	}


	public String getTeamLeadMiddleName() {
		return teamLeadMiddleName;
	}


	public void setTeamLeadMiddleName(String teamLeadMiddleName) {
		if(StringUtils.isNotBlank(teamLeadMiddleName))
			this.teamLeadMiddleName = teamLeadMiddleName.trim();
	}


	public String getTeamLeadLastName() {
		return teamLeadLastName;
	}


	public void setTeamLeadLastName(String teamLeadLastName) {
		if(StringUtils.isNotBlank(teamLeadLastName))
			this.teamLeadLastName = teamLeadLastName.trim();
	}

*/
	

	public String getOfficeLocCode() {
		return officeLocCode;
	}


	public TAStaffBO getScheduler() {
		return scheduler;
	}

	public void setScheduler(TAStaffBO scheduler) {
		this.scheduler = scheduler;
	}

	public void setOfficeLocCode(String officeLocCode) {
		if(StringUtils.isNotBlank(officeLocCode))
			this.officeLocCode = officeLocCode.trim();
	}


	public Date getScheduledStartDate() {
		return scheduledStartDate;
	}

	public void setScheduledStartDate(Date scheduledStartDate) {
		this.scheduledStartDate = scheduledStartDate;
	}

	public String getScheduledStartTime() {
		return scheduledStartTime;
	}

	public void setScheduledStartTime(String scheduledStartTime) {
		if(StringUtils.isNotBlank(scheduledStartTime))
			this.scheduledStartTime = scheduledStartTime.trim();
	}

	public Date getScheduledEndDate() {
		return scheduledEndDate;
	}

	public void setScheduledEndDate(Date scheduledEndDate) {
		this.scheduledEndDate = scheduledEndDate;
	}

	public String getScheduledEndTime() {
		return scheduledEndTime;
	}

	public void setScheduledEndTime(String scheduledEndTime) {
		if(StringUtils.isNotBlank(scheduledEndTime))
			this.scheduledEndTime = scheduledEndTime.trim();
	}

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(String actualStartTime) {
		if(StringUtils.isNotBlank(actualStartTime))
			this.actualStartTime = actualStartTime.trim();
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getActualEndTime() {
		return actualEndTime;
	}

	public void setActualEndTime(String actualEndTime) {
		if(StringUtils.isNotBlank(actualEndTime))
			this.actualEndTime = actualEndTime.trim();
	}

	public String getBackDated() {
		return backDated;
	}


	public void setBackDated(String backDated) {
		this.backDated = backDated;
	}


	public String getStatusId() {
		return statusId;
	}


	public void setStatusId(String statusId) {
		if(StringUtils.isNotBlank(statusId))
			this.statusId = statusId.trim();
	}


	public String getStatusDescription() {
		return statusDescription;
	}


	public void setStatusDescription(String statusDescription) {
		if(StringUtils.isNotBlank(statusDescription))
			this.statusDescription = statusDescription.trim();
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
		if(StringUtils.isNotBlank(authorized))
			this.authorized = authorized.trim();
	}


	public String getAuthorizedUserName() {
		return authorizedUserName;
	}


	public void setAuthorizedUserName(String authorizedUserName) {
		if(StringUtils.isNotBlank(authorizedUserName))
			this.authorizedUserName = authorizedUserName.trim();
	}


	public Date getAuthorizedDtime() {
		return authorizedDtime;
	}


	public void setAuthorizedDtime(Date authorizedDtime) {
		this.authorizedDtime = authorizedDtime;
	}


	


	public Integer getReasonId() {
		return reasonId;
	}


	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		if(StringUtils.isNotBlank(comment))
			this.comment = comment.trim();
	}


	
	public String getCurrentUsername() {
		return currentUsername;
	}


	public void setCurrentUsername(String currentUsername) {
		if(StringUtils.isNotBlank(currentUsername))
			this.currentUsername = currentUsername.trim();
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

	public Date getCourtDate() {
		return courtDate;
	}

	public void setCourtDate(Date courtDate) {
		this.courtDate = courtDate;
	}

	public String getCourtTime() {
		return courtTime;
	}

	public void setCourtTime(String courtTime) {
		if(StringUtils.isNotBlank(courtTime))
			this.courtTime = courtTime.trim();
	}

	/*public String getTeamLeadFullName() {
		return teamLeadFullName;
	}

	public void setTeamLeadFullName(String teamLeadFullName) {
		this.teamLeadFullName = teamLeadFullName;
	}*/

	

	public List<StrategyBO> getOperationStrategyList() {
		return operationStrategyList;
	}

	public void setOperationStrategyList(List<StrategyBO> operationStrategyList) {
		this.operationStrategyList = operationStrategyList;
	}

	public CourtBO getCourtBO() {
		return courtBO;
	}

	public void setCourtBO(CourtBO courtBO) {
		this.courtBO = courtBO;
	}

	public List<String> getOfficeLocCodeList() {
		return officeLocCodeList;
	}

	public void setOfficeLocCodeList(List<String> officeLocCodeList) {
		this.officeLocCodeList = officeLocCodeList;
	}

	public Date getCourtDTime() {
		return courtDTime;
	}

	public void setCourtDTime(Date courtDTime) {
		this.courtDTime = courtDTime;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getFirstOffenceDate() {
		return firstOffenceDate;
	}

	public void setFirstOffenceDate(Date firstOffenceDate) {
		this.firstOffenceDate = firstOffenceDate;
	}

	public Date getLastOffenceDate() {
		return lastOffenceDate;
	}

	public void setLastOffenceDate(Date lastOffenceDate) {
		this.lastOffenceDate = lastOffenceDate;
	}

	
	
	
	
	
}
