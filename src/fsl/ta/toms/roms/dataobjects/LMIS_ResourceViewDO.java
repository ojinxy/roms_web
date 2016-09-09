package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roms_lmis_resource_view")
public class LMIS_ResourceViewDO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3680295773744145139L;

		
	
	public LMIS_ResourceViewDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="resource_key")
	String resourceKey;
	
	@Column(name="resource_type")
	String resourceType;
	
	@Column(name="description")
	String description;
	
	@Column(name="admin_allow_flag")
	Character adminAllowFlag;

	
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Character getAdminAllowFlag() {
		return adminAllowFlag;
	}

	public void setAdminAllowFlag(Character adminAllowFlag) {
		this.adminAllowFlag = adminAllowFlag;
	}

	/**
	 * @return the resourceKey
	 */
	public String getResourceKey() {
		return resourceKey;
	}

	/**
	 * @param resourceKey the resourceKey to set
	 */
	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}
	
	
}
