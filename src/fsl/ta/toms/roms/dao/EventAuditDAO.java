package fsl.ta.toms.roms.dao;

import fsl.dao.DAO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;

public interface EventAuditDAO extends DAO{
	public boolean saveEventAuditDO(EventAuditDO eventAuditDO); 
}
