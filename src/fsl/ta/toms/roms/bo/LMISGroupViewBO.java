package fsl.ta.toms.roms.bo;

import java.io.Serializable;

import fsl.ta.toms.roms.dataobjects.LMIS_GroupViewDO;

public class LMISGroupViewBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2957968915772520740L;

	String groupName;

	String description;

	
	public LMISGroupViewBO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LMISGroupViewBO(LMIS_GroupViewDO item) {
		this.groupName = item.getGroupName();
		this.description = item.getDescription();
	}
	

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




}
