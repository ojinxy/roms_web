package fsl.ta.toms.roms.dao.impl;

import fsl.ta.toms.roms.dao.EventAuditDAO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;

public class EventAuditDAOImpl extends ParentDAOImpl implements EventAuditDAO {


	
	
	@Override
	public boolean saveEventAuditDO(EventAuditDO eventAuditDO){
		
			hibernateTemplate.save(eventAuditDO);
			
			return true;
		
		
	}
}
