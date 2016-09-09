/**
 * 
 */
package org.fsl.roms.uicomponents;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.fsl.roms.view.AssociatedDocView;
import org.primefaces.model.SelectableDataModel;

/**
 * @author oanguin
 *
 */
public class AssociatedDocModel extends ListDataModel<AssociatedDocView>
		implements SelectableDataModel<AssociatedDocView> , Serializable
{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AssociatedDocModel() {
		super();
	
	}

	public AssociatedDocModel(List<AssociatedDocView> list) {
		super(list);
		
	}

	@Override
	public AssociatedDocView getRowData(String rowKey) 
	{
		
		List<AssociatedDocView> associatedDocViews = (List<AssociatedDocView>)this.getWrappedData();
		
		
		for(AssociatedDocView associatedDocView : associatedDocViews)
		{
			if(associatedDocView.getIndex().toString().equalsIgnoreCase(rowKey))
				return associatedDocView;
		}
		
		return null;
	}

	@Override
	public Object getRowKey(AssociatedDocView associatedDocView) 
	{
		
		return associatedDocView.getIndex();
	}

}
