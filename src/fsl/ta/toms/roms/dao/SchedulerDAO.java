package fsl.ta.toms.roms.dao;

import java.util.Date;
import java.util.List;

import fsl.dao.DAO;
import fsl.ta.toms.roms.bo.BIMSStaffViewBO;
import fsl.ta.toms.roms.bo.ProcessControlBO;
import fsl.ta.toms.roms.dataobjects.PoliceOfficerDO;
import fsl.ta.toms.roms.dataobjects.ProcessControlDO;


public interface SchedulerDAO extends DAO {

	public List<BIMSStaffViewBO> findTAStaff(Date lastRun);
	
	public boolean updatePolice(PoliceOfficerDO policeDO);
	
	public List<ProcessControlBO> getLasRunProcesses();
	
	 public List<ProcessControlDO> findLastEventByCode(Integer eventCode);

	public Integer createTerminatedProcessControlRecord(
			ProcessControlDO proCntrolDO);
}
