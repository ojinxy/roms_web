package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="ROMS_cd_other_role_observed")
public class CDOtherRoleDO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8956306560367132842L;



	public CDOtherRoleDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="other_role_observed_id")
	String otherRoleId;
	
	@Column(name="other_role_observed_desc")	
	String description;
	
	@Column(name="create_username")	
	String createUserName;
	
	@Column(name="create_dtime")	
	String createDateTime;



	public String getOtherRoleId() {
		return otherRoleId;
	}

	public void setOtherRoleId(String otherRoleId) {
		this.otherRoleId = otherRoleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}
	
	
}
