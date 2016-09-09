package fsl.ta.toms.roms.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.CDEventRefTypeDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.service.EventAuditService;

@Transactional
public class EventAuditServiceImpl implements EventAuditService{
	private DAOFactory daoFactory;

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Transactional(readOnly=false)
	public boolean saveEventAuditDO(EventAuditDO eventAuditDO){
		boolean saved = false;
		Calendar calendar = Calendar.getInstance();	
		Date currentDate = calendar.getTime();
		
		CDEventDO cdEventDO = new CDEventDO();
		CDEventRefTypeDO eventRefType1 = new CDEventRefTypeDO();
		CDEventRefTypeDO eventRefType2 = new CDEventRefTypeDO();
		
		cdEventDO = (CDEventDO) daoFactory.getEventAuditDAO().find(CDEventDO.class, eventAuditDO.getEventCode());
		eventAuditDO.setEvent(cdEventDO);
		
		if(StringUtils.isNotBlank(eventAuditDO.getRefType1Code())){
			eventRefType1 = (CDEventRefTypeDO) daoFactory.getEventAuditDAO().find(CDEventRefTypeDO.class, eventAuditDO.getRefType1Code());
			eventAuditDO.setRefType1(eventRefType1);
		}
		if(StringUtils.isNotBlank(eventAuditDO.getRefType2Code())){
			eventRefType2 = (CDEventRefTypeDO) daoFactory.getEventAuditDAO().find(CDEventRefTypeDO.class, eventAuditDO.getRefType2Code());
			eventAuditDO.setRefType2(eventRefType2);
		}	
		eventAuditDO.getAuditEntry().setCreateDTime(currentDate);
		
		saved = daoFactory.getEventAuditDAO().saveEventAuditDO(eventAuditDO);
		
		return saved;
	}
}
