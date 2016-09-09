/**
 * 
 */
package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.web.bind.annotation.RequestBody;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dataobjects.AddressDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;

public class RoadOperationUpdateCustomBO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6258338696079002050L;
	
	
	private RoadOperationBO roadOperationBO;
	private List<AssignedTeamDetailsBO> assignedTeamDetailsBOList;
	public RoadOperationBO getRoadOperationBO() {
		return roadOperationBO;
	}
	public void setRoadOperationBO(RoadOperationBO roadOperationBO) {
		this.roadOperationBO = roadOperationBO;
	}
	public List<AssignedTeamDetailsBO> getAssignedTeamDetailsBOList() {
		return assignedTeamDetailsBOList;
	}
	public void setAssignedTeamDetailsBOList(
			List<AssignedTeamDetailsBO> assignedTeamDetailsBOList) {
		this.assignedTeamDetailsBOList = assignedTeamDetailsBOList;
	}
	
	
	
	
}