package org.fsl.roms.uicomponents;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.fsl.roms.view.TeamView;
import org.primefaces.model.SelectableDataModel;
  
/**
 * 
 * @author rbrooks
 *
 */
public class TeamDataModel extends ListDataModel<TeamView> implements Serializable, SelectableDataModel<TeamView> {    
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -9116670388746363176L;

	/**
	 * 
	 */
	

	public TeamDataModel() {  
    }  
  
    public TeamDataModel(List<TeamView> data) {  
        super(data);  
    }  
      
    @Override  
    public TeamView getRowData(String rowKey) {  
          
        List<TeamView> teams = (List<TeamView>)getWrappedData();  
          
        for(TeamView team : teams) {  
            if(team.getTeamID().equals(rowKey))
        	    return team;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(TeamView team) {  
        return team.getTeamIDNumber();  
    }  
}  
                      