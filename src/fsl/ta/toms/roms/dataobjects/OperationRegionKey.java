package fsl.ta.toms.roms.dataobjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class OperationRegionKey implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2774522845706433661L;

	
	public OperationRegionKey() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@ManyToOne
	@JoinColumn(name="road_operation_id")
	RoadOperationDO roadOperation;
	
	@Column(name="loc_code")
	String loc_code;


	public RoadOperationDO getRoadOperation() {
		return roadOperation;
	}

	public void setRoadOperation(RoadOperationDO roadOperation) {
		this.roadOperation = roadOperation;
	}

	public String getLoc_code() {
		return loc_code;
	}

	public void setLoc_code(String loc_code) {
		this.loc_code = loc_code;
	}
	

	
	
	
	

}
