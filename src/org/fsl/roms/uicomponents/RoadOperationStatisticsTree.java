package org.fsl.roms.uicomponents;

import java.io.Serializable;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import fsl.ta.toms.roms.bo.RoadOperationSummaryResultsBO;

public class RoadOperationStatisticsTree implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TreeNode root;

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public RoadOperationStatisticsTree() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RoadOperationStatisticsTree(RoadOperationSummaryResultsBO summary) 
	{
		
		
			 root = new DefaultTreeNode("Root", null);  
		     TreeNode node0 = new DefaultTreeNode("Operation Statistics", root);  
		          
		     TreeNode node00 = new DefaultTreeNode("Road Checks (" + summary.getCountCompliancyActivitiesCommited() + ")", node0);  
		     TreeNode node01 = new DefaultTreeNode("Summonses Served (" + summary.getCountSummonsIssued() + ")", node0); 
		     TreeNode node02 = new DefaultTreeNode("Plates Removed (" + summary.getCountPlatesRemoved() + ")", node0);
		     TreeNode node03 = new DefaultTreeNode("Warnings for Prosecution (" + summary.getWarningsForProcecution() + ")", node0);
		     TreeNode node04 = new DefaultTreeNode("Warning Notices Issued (" + summary.getCountWaningNoticesIssued() + ")", node0);
		     TreeNode node05 = new DefaultTreeNode("Seized Vehicles (" + summary.getCountVehiclesSeized() + ")", node0);
		
	}

}
