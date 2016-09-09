package org.fsl.Events;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.execution.Event;

public class SingleResult extends Event{
	

	private static final String SINGLE_RESULT_EVENT_ID = "one";

	public SingleResult(Object source, String id, AttributeMap attributes) {
		super(source, SINGLE_RESULT_EVENT_ID, attributes);
		
	}

	
	public SingleResult(Object source, String id) {
		super(source, SINGLE_RESULT_EVENT_ID);
		// TODO Auto-generated constructor stub
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 6571609111005841561L;

}
