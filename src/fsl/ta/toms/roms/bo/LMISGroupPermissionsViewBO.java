package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.LMIS_GroupPermissionsViewDO;

/**
 * 
 * @author jreid
 * Created Date: Nov 14, 2013
 */
public class LMISGroupPermissionsViewBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2873285536473907309L;

	String groupName;

	String resourceKey;

	public LMISGroupPermissionsViewBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LMISGroupPermissionsViewBO(LMIS_GroupPermissionsViewDO item) {
		this.groupName = item.getGroupName();
		this.resourceKey = item.getResourceKey();
	}

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
