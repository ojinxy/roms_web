package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roms_lmis_group_view")
public class LMIS_GroupViewDO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2957968915772520740L;
	
	public LMIS_GroupViewDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="groupname")
	String groupName;
	
	@Column(name="description")
	String description;
	
	@Column(name="status")
	String status;
	

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	
}
