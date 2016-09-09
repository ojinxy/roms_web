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

import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.ProcessControlBO;

@Entity
@Table(name="ROMS_process_control ")
public class ProcessControlDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8832000667269948805L;

	public ProcessControlDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	


	@Id
	@Column(name="proc_cntrl_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer procCntrlId;
	
	@ManyToOne	
	@JoinColumn(name="event_code")
	CDEventDO event;
	
	@Column(name="process_user")
	String processUser;
	
	@Column(name="run_start_dtime")
	Date runStartDtime;
	
	@Column(name="run_end_dtime")
	Date runEndDtime;
	
	@Column(name="trans_process_cnt")
	Integer transProcessCnt;

	@Column(name="status_code")
	String statusCode;

	@Column(name="message_txt")
	String messageTxt;


	@Version
	@Column(name="version_nbr")
	Integer versionNbr;

	public Integer getProcCntrlId() {
		return procCntrlId;
	}

	public void setProcCntrlId(Integer procCntrlId) {
		this.procCntrlId = procCntrlId;
	}

	

	public CDEventDO getEvent() {
		return event;
	}

	public void setEvent(CDEventDO event) {
		this.event = event;
	}

	public String getProcessUser() {
		return processUser;
	}

	public void setProcessUser(String processUser) {
		this.processUser = processUser;
	}

	public Date getRunStartDtime() {
		return runStartDtime;
	}

	public void setRunStartDtime(Date runStartDtime) {
		this.runStartDtime = runStartDtime;
	}

	public Date getRunEndDtime() {
		return runEndDtime;
	}

	public void setRunEndDtime(Date runEndDtime) {
		this.runEndDtime = runEndDtime;
	}

	public Integer getTransProcessCnt() {
		return transProcessCnt;
	}

	public void setTransProcessCnt(Integer transProcessCnt) {
		this.transProcessCnt = transProcessCnt;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessageTxt() {
		return messageTxt;
	}

	public void setMessageTxt(String messageTxt) {
		this.messageTxt = messageTxt;
	}

	public Integer getVersionNbr() {
		return versionNbr;
	}

	public void setVersionNbr(Integer versionNbr) {
		this.versionNbr = versionNbr;
	}

	      
	
}
