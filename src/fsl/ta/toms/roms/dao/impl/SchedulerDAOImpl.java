package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.BIMSStaffViewBO;
import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.bo.ProcessControlBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.SchedulerDAO;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.PoliceOfficerDO;
import fsl.ta.toms.roms.dataobjects.ProcessControlDO;

public class SchedulerDAOImpl extends ParentDAOImpl implements SchedulerDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<BIMSStaffViewBO> findTAStaff(Date lastRun) {
		
		List<BIMSStaffViewBO> citations = new ArrayList<BIMSStaffViewBO>();
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.BIMSStaffViewBO(b.staffId, b.trnNbr,b.inspRegNo, " +
				" b.staffType, b.firstName, b.middleName, b.lastName, b.genderCode,b.locationCode,b.badgeStatusCode,b.badgeUpdateDTime," +
				"b.staffUpdateDTime)" +
		        " from BIMS_StaffView b , TAStaffDO t" +
		        " where b.staffId = t.staffId ");
		
		
		//Last Run Date 
		if (lastRun != null) {
			queryString.append(" and " +
								"(b.badgeUpdateDTime >= '").append(lastRun + "'");
				queryString.append(" or  b.staffUpdateDTime >= '").append(lastRun + "')");
		}
		
		citations = hibernateTemplate.find(queryString.toString());
		
		
		return citations;
	}

	@Override
	public boolean updatePolice(PoliceOfficerDO policeDO)
	{
		try{
			hibernateTemplate.update(policeDO);
			return true;
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	 public List<ProcessControlBO> getLasRunProcesses()
	 {
		 
		StringBuffer queryString = new StringBuffer();
		
		queryString.append(" from ProcessControlDO e ");
		
		queryString.append(" where e.procCntrlId = (select max(x.procCntrlId) from ProcessControlDO x " +
				" where x.event = e.event ");
		
		return hibernateTemplate.find(queryString.toString());
	 }

	 
	 
	 
	 public List<ProcessControlDO> findLastEventByCode(Integer eventCode)
		{
			
			StringBuffer queryString = new StringBuffer();
			
			queryString.append(" from ProcessControlDO e where 1=1 ");
			
			//Event Code
			if (eventCode != null) 
			{queryString.append(" and e.event.eventCode = ").append(eventCode).append("");}
			
			queryString.append(" and e.procCntrlId = (select max(a.procCntrlId) from ProcessControlDO a " +
					" where a.event.eventCode = " + eventCode + 
					" and a.statusCode = 'C'" + 
					" and a.transProcessCnt > 0 )");  //indicates success
			
			return hibernateTemplate.find(queryString.toString());
			
		}
	 
	 	
	 	public Integer createTerminatedProcessControlRecord(ProcessControlDO proCntrolDO)
		{
			
		 	Integer id=0;
			Session session = null;
	    	Transaction tx = null;
	 
	    	try{
	    		session = hibernateTemplate.getSessionFactory().openSession();
	    		tx = session.beginTransaction();
	    		//tx.setTimeout(5);
	 
	    	    id = (Integer)save(proCntrolDO);
	 
	    		tx.commit();
	 
	 
	    	}catch(RuntimeException e){
	    		try{
	    			tx.rollback();
	    		}catch(RuntimeException rbe){
	    			
	    		}
	    		throw e;
	    	}
	    	finally{
	    		if(session!=null){
	    			session.close();
	    		}
	    	}
			
			return id;
			
		}
	 
}
