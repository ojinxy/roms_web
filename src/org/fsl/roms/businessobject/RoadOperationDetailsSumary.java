/**
 * 
 */
package org.fsl.roms.businessobject;

import java.io.Serializable;

import org.fsl.roms.uicomponents.RoadOperationStatisticsTree;
import org.fsl.roms.uicomponents.RoadOperationTeamTree;

import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryResultsBO;

/**
 * @author oanguin
 *
 */
public class RoadOperationDetailsSumary implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RoadOperationBO roadOp;
	
	RoadOperationSummaryResultsBO roadOpSummary;
	
	RoadOperationStatisticsTree roadOpStatsTree;
	
	RoadOperationTeamTree roadOpTeamTree;

	public RoadOperationDetailsSumary(RoadOperationBO roadOp,
			RoadOperationSummaryResultsBO roadOpSummary) {
		super();
		this.roadOp = roadOp;
		this.roadOpSummary = roadOpSummary;
		this.roadOpStatsTree = new RoadOperationStatisticsTree(roadOpSummary);
		this.roadOpTeamTree = new RoadOperationTeamTree(roadOpSummary);
	}
	
	public RoadOperationDetailsSumary(RoadOperationBO roadOp)
	{
		super();
		this.roadOp = roadOp;
		this.roadOpSummary = null;
		
	}

	
	public RoadOperationDetailsSumary() 
	{
		super();
		this.roadOp = new RoadOperationBO();
		this.roadOpSummary = new RoadOperationSummaryResultsBO();
		this.roadOpStatsTree = new RoadOperationStatisticsTree();
		this.roadOpTeamTree = new RoadOperationTeamTree();
	}

	public RoadOperationBO getRoadOp() {
		return roadOp;
	}

	public void setRoadOp(RoadOperationBO roadOp) {
		this.roadOp = roadOp;
	}

	public RoadOperationSummaryResultsBO getRoadOpSummary() {
		return roadOpSummary;
	}

	public void setRoadOpSummary(RoadOperationSummaryResultsBO roadOpSummary) 
	{
		this.roadOpSummary = roadOpSummary;
		
		this.roadOpStatsTree = new RoadOperationStatisticsTree(roadOpSummary);
		this.roadOpTeamTree = new RoadOperationTeamTree(roadOpSummary);
	}

	public RoadOperationStatisticsTree getRoadOpStatsTree() {
		return roadOpStatsTree;
	}

	public void setRoadOpStatsTree(RoadOperationStatisticsTree roadOpStatsTree) {
		this.roadOpStatsTree = roadOpStatsTree;
	}

	public RoadOperationTeamTree getRoadOpTeamTree() {
		return roadOpTeamTree;
	}

	public void setRoadOpTeamTree(RoadOperationTeamTree roadOpTeamTree) {
		this.roadOpTeamTree = roadOpTeamTree;
	}
	
	
	
}
