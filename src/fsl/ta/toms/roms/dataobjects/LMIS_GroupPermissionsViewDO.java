package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roms_lmis_group_permissions_view")
public class LMIS_GroupPermissionsViewDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 518765528487509897L;

	public LMIS_GroupPermissionsViewDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="groupname")
	String groupName;
	
	@Column(name="resource_key")
	String resourceKey;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}					
	
	

}
