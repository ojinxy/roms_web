package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

public class RoadOpSearchResultsMobileBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5748801416868615809L;

	List <RoadOperationBO> roadOps;
	int resultCount;


	public List<RoadOperationBO> getRoadOps() {
		return roadOps;
	}

	public void setRoadOps(List<RoadOperationBO> roadOps) {
		this.roadOps = roadOps;
	}

	/**
	 * @return the resultCount
	 */
	public int getResultCount() {
		return resultCount;
	}

	/**
	 * @param resultCount the resultCount to set
	 */
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	

	
	
}
