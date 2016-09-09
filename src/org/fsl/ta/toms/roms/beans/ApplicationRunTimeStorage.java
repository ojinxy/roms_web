package org.fsl.ta.toms.roms.beans;

import java.io.Serializable;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("applicationRunTimeStorage")
public class ApplicationRunTimeStorage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7187927196022297640L;
	
	private HashMap<Object,Object> values;
	//protected Logger logger = LogManager.getLogger(this.getClass().getName());

	public ApplicationRunTimeStorage()
	{
		super();
		values = new HashMap<Object,Object>();
		//logger.info(this.getClass().getName(), "{Created ApplicationRunTimeStorage}");
	}

	public HashMap<Object, Object> getValues()
	{
		return values;
	}

	public void setValues(HashMap<Object, Object> values)
	{
		this.values = values;
	}
	
	
	
}
