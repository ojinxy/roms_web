package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AuditEntry implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7430093238816264730L;

	
	public AuditEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Column(name="create_username")
	String createUsername;
	
	@Column(name="create_dtime")
	Date createDTime;
	
	@Column(name="update_username")
	String updateUsername;
	
	@Column(name="update_dtime")
	Date updateDTime;


	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	public Date getCreateDTime() {
		return createDTime;
	}

	public void setCreateDTime(Date createDTime) {
		this.createDTime = createDTime;
	}

	public String getUpdateUsername() {
		return updateUsername;
	}

	public void setUpdateUsername(String updateUsername) {
		this.updateUsername = updateUsername;
	}

	public Date getUpdateDTime() {
		return updateDTime;
	}

	public void setUpdateDTime(Date updateDTime) {
		this.updateDTime = updateDTime;
	}
	
	
	
}
