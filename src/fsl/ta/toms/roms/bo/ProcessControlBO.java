package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.dataobjects.CourtDO;

/**
 * 
 * @author rbrooks
 * Created Date: Oct 18, 2013
 */
public class ProcessControlBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -404299112923394529L;

	/**
	 * 
	 */
	
Integer procCntrlId;
	
	@Column(name="process_name")
	String processName;
	
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

	public Integer getProcCntrlId() {
		return procCntrlId;
	}

	public void setProcCntrlId(Integer procCntrlId) {
		this.procCntrlId = procCntrlId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
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
	
	
	
}