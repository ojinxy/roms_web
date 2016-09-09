package org.fsl.roms.reports;

import java.io.Serializable;

public class CourtScheduleRegionCount implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
	}
	
	
	
	public CourtScheduleRegionCount(String regionDesc, String regionCount)
	{
		super();
		RegionDesc = regionDesc;
		RegionCount = regionCount;
	}



	public String RegionDesc;
	
	public String RegionCount;

	public String getRegionDesc()
	{
		return RegionDesc;
	}

	public void setRegionDesc(String regionDesc)
	{
		RegionDesc = regionDesc;
	}

	public String getRegionCount()
	{
		return RegionCount;
	}

	public void setRegionCount(String regionCount)
	{
		RegionCount = regionCount;
	}
	
	
}
