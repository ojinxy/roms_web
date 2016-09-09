package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roms_lmis_user_group_view")
public class LMIS_UserGroupViewDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7453852924620934703L;

	public LMIS_UserGroupViewDO() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Id
	@Column(name="username")
	String userName;
	
	@Column(name="groupname")
	String groupName;



	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	
}
