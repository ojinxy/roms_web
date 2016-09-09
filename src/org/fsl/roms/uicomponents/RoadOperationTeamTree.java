package org.fsl.roms.uicomponents;

import java.io.Serializable;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import fsl.ta.toms.roms.bo.RoadOperationSummaryResultsBO;
import fsl.ta.toms.roms.bo.RoadOperationTeamSummaryResults;

public class RoadOperationTeamTree implements Serializable 
{

	/**
	 * 
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

	public RoadOperationTeamTree() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RoadOperationTeamTree(RoadOperationSummaryResultsBO summary) 
	{
		
		
		 if(summary.getTeamSummaries() != null && summary.getTeamSummaries().size() > 0)
		 {
			 root = new DefaultTreeNode("Root", null);  
		     TreeNode node0 = new DefaultTreeNode("Teams (" + summary.getTeamSummaries().size() + ")" , root);  
		         
		     for( RoadOperationTeamSummaryResults teamResults : summary.getTeamSummaries())
		     {
		    	 TreeNode node = new DefaultTreeNode("Team Lead : " + teamResults.getTeamLeadName(), node0);
			     
		     }
		 }
		
	}

}
