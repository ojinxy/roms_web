package fsl.ta.toms.roms.service.impl;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.xerces.parsers.DOMParser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fsl.ta.toms.roms.bo.BIMSStaffViewBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.ProcessControlBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.EventCode;
import fsl.ta.toms.roms.constants.Constants.TTMSCodeTypes;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.impl.ParentDAOImpl;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.CDCourtRulingDO;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.CDEventRefTypeDO;
import fsl.ta.toms.roms.dataobjects.CDPleaDO;
import fsl.ta.toms.roms.dataobjects.CDVerdictDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.PoliceOfficerDO;
import fsl.ta.toms.roms.dataobjects.ProcessControlDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.service.SchedulerService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.TTMSCodeService;
import fsl.ta.toms.roms.util.StringUtil;
/**
 * 
 * @author rbrooks
 * Created Date: April 21, 2013
 */

/**
 * @author rbrooks
 * Created Date: May 13, 2013
 */
@Transactional
public class SchedulerServiceImpl implements SchedulerService {
	private DAOFactory daoFactory;
	private ServiceFactory serviceFactory;
	 
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	
	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}


	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	
	 
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public boolean updatePoliceOfficer(String currentUser) throws NoRecordFoundException, ErrorSavingException{
		
			
				TTMSCodeService ttmsCodeService = serviceFactory.getTTMSCodeService();
				int numOfRecords = 0;
				String message="";
				boolean success = true;
				Integer currentProcID = 0;
				
				
				AuditEntry audit = new AuditEntry();
				
			try {
				String results = ttmsCodeService.updateTTMSCode(currentUser, TTMSCodeTypes.POLICE_OFFICER,EventCode.UPDATE_POLICE_OFFICER);
				
				results = results.replace("&", "and");
				
				//get code descriptions
				HashMap<String,String> hashOfRankDesc = new HashMap<String,String>();
				HashMap<String,String> hashOfStationDesc = new HashMap<String,String>();
				
				hashOfRankDesc = ttmsCodeService.getTTMSPoliceRanks();
				hashOfStationDesc = ttmsCodeService.getTTMSPoliceStations();
				
				
				DOMParser parser = new DOMParser();
				
				    parser.parse(new InputSource(new java.io.StringReader(results)));
				    Document doc = parser.getDocument();
				    
				    
				    NodeList policeOfficers = doc.getElementsByTagName("code_record");
				   
				  //Create Initial Process Record
	       			currentProcID = createProcessControlRecord(EventCode.UPDATE_POLICE_OFFICER,currentUser);
	       			
				    
				    /**
		            * SET UP AUDIT ENTRY
		            */
	       			audit = new AuditEntry();
	       			audit.setCreateUsername(currentUser);
	       			audit.setCreateDTime(Calendar.getInstance().getTime());
	       			//auditDO.setCreateDtime(new Date(System.currentTimeMillis()));
				    
	       		
				    for(int i=0; i<policeOfficers.getLength(); i++) 
				    {
				    		
				    			//System.out.println("Person Number " + i + " being processed.");
				    			PersonDO personDO = null;
				    			PoliceOfficerDO policeDO = null;
				    			
				    							    			
				               Element eachOfficer = (Element)policeOfficers.item(i);
				               
				               /**Function created to process EACH police within a transaction**/
				               saveorUpdatePoliceOfficer( personDO, policeDO, eachOfficer, hashOfRankDesc, hashOfStationDesc, audit, currentUser );
				               
				               numOfRecords++;
				       			
				    }
				    
				    /**
		                * SAVE EVENT AUDIT
		                */
				    
					    EventAuditDO auditDO = new EventAuditDO();
					    auditDO = createEventAuditRecord(success,currentUser,numOfRecords,message,Constants.EventCode.UPDATE_POLICE_OFFICER);
					    
					    auditDO.setAuditEntry(audit);
				    	daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);
					
				}
				catch(fsl.ta.toms.roms.exception.NoRecordFoundException e){
					success = false;
					message = e.getLocalizedMessage();
				}
				catch (SAXException e) {
				    // handle SAXException 
					success=false;
					message = e.getLocalizedMessage();
				} catch (IOException e) {
				    // handle IOException
					success=false;
					message = e.getLocalizedMessage();
				}
				catch(Exception dae)
				{
					success=false;
					message = dae.getLocalizedMessage();
					throw new ErrorSavingException(dae.getLocalizedMessage());
				}finally
				{
					//Update Process control record
					updateProcessControlRecord(numOfRecords,message, success, currentProcID,currentUser,EventCode.UPDATE_POLICE_OFFICER);
					
					createFailureAudit(success,currentUser,numOfRecords,message,Constants.EventCode.UPDATE_POLICE_OFFICER);
				}
					
				return true;
	}

	
	


	


	/**
	 * Function created to process EACH police within a transaction
	 * @author kpowell
	 * @date 2014-10-22
	 * 
	 * @param personDO
	 * @param policeDO
	 * @param eachOfficer
	 * @param hashOfRankDesc
	 * @param hashOfStationDesc
	 * @param audit
	 * @param currentUser
	 * @throws NoRecordFoundException
	 * @throws ErrorSavingException
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	private void saveorUpdatePoliceOfficer( PersonDO personDO, PoliceOfficerDO policeDO, Element eachOfficer, 
			HashMap<String,String> hashOfRankDesc, HashMap<String,String> hashOfStationDesc, AuditEntry audit, String currentUser )
			throws NoRecordFoundException, ErrorSavingException
	{
		       
       Integer polCompNo = new Integer(getChildValue(eachOfficer, "polCompNo", 0));
       String lastName = getChildValue(eachOfficer, "lastName", 0);
       String firstName = getChildValue(eachOfficer, "firstName", 0);
       String midName = getChildValue(eachOfficer, "midName", 0);
       
       if(midName != null && midName.equalsIgnoreCase("null")){
    	   midName = null;
       }
       
       String rankCode = getChildValue(eachOfficer, "rankCode", 0);
       
       /*RFC-ROMS-0035 : Use rank code instead of rank description - by OA - 3 Feb 2015*/
       //get rank description
       	//String rankDesc = ttmsCodeService.getTTMSCodeDescription(currentUser, TTMSCodeTypes.POLICE_RANK, rankCode);
//       String rankDesc;
//       if(hashOfRankDesc != null)
//       {
//    	   if(hashOfRankDesc.containsKey(rankCode))
//    	   {
//    		   rankDesc = hashOfRankDesc.get(rankCode);
//    	   }else
//    	   {
//    		   rankDesc = rankCode;
//    	   }
//       }else
//	   {
//		   rankDesc = rankCode;
//	   }
       
       
       String polStationCode = getChildValue(eachOfficer, "polStationCode", 0);
       //get station code description
       //String stationDesc = ttmsCodeService.getTTMSCodeDescription(currentUser, TTMSCodeTypes.POLICE_STATION, polStationCode);
       String stationDesc;
       if(hashOfStationDesc != null)
       {
    	 
    	   if(hashOfStationDesc.containsKey(polStationCode))
    	   {
    		   stationDesc = hashOfStationDesc.get(polStationCode);
    	   }else
    	   {
    		  
    		   stationDesc = polStationCode;
    	   }
       }else
	   {
    	   stationDesc = polStationCode;
	   }
       	
       
       //Get Status 	
       String status = StringUtil.mapToROMSStatus(getChildValue(eachOfficer, "status", 0));
       StatusDO policeStatus = daoFactory.getSchedulerDAO().find(StatusDO.class,status);
		
		PoliceOfficerDO existingPolice = policeExists(polCompNo);
			
			if(existingPolice == null) 
			{
			       /**
                * CREATE AND SAVE PERSON RECORD
                */
				personDO = new PersonDO(null, null, firstName, midName, lastName, null, null, null, null, null, null, null, null, null, audit);
       			
       			//save Person
       			Integer personID = (Integer)daoFactory.getRoadCompliancyDAO().savePerson(personDO);
       			PersonDO savedPerson = daoFactory.getSchedulerDAO().find(PersonDO.class, personID);
       			
       			
       			
       			/**
	             * CREATE AND SAVE POLICE RECORD
	            */
	               
       			//Create and Save Police Officer Record
       			policeDO = new PoliceOfficerDO(polCompNo, savedPerson, rankCode, stationDesc, policeStatus,audit);
       			
       			//save Police
       			daoFactory.getPoliceOfficerDAO().save(policeDO);
			}else
			{
				/**
             * UPDATE PERSON RECORD
            */
				/*Commented out by OA - 3 Feb 2015 - Because PoliceOfficer Has a nested person object it will try and update the person BO objects as well.
				 * This lease to stale object as the personDO in existingPolice is not in sync as what the database has from the updatePerson function*/
				/*serviceFactory.getRoadCompliancyService().updatePerson(existingPolice.getPerson(), 
						new PersonBO(null, firstName, midName, lastName, null, null, null, null, null, null, null, null, null, currentUser));*/
				
				existingPolice.getPerson().setFirstName(firstName);
				existingPolice.getPerson().setLastName(lastName);
				existingPolice.getPerson().setMiddleName(midName);
				existingPolice.getPerson().getAuditEntry().setUpdateUsername(currentUser);
				existingPolice.getPerson().getAuditEntry().setUpdateDTime(Calendar.getInstance().getTime());
				
				
				/**
             * UPDATE POLICE RECORD
            */
				updateExistingPolice(existingPolice,new PoliceOfficerBO(polCompNo, firstName, midName, lastName, rankCode, stationDesc, status,currentUser),
						policeStatus);
				
			}
			

	}
	
	
	
	/**
	 * @param currentUser
	 * @return
	 * @throws NoRecordFoundException 
	 * @throws ErrorSavingException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean updatePlea(String currentUser) throws NoRecordFoundException, ErrorSavingException{
		
		TTMSCodeService ttmsCodeService = serviceFactory.getTTMSCodeService();
		
		AuditEntry audit = new AuditEntry();
	    Integer currentProcID = 0;
	    String message = "";
	    boolean success = true;
	    
		int numOfRecords = 0;
		
		DOMParser parser = new DOMParser();
		try {
			
			String results = ttmsCodeService.updateTTMSCode(currentUser, TTMSCodeTypes.PLEA,EventCode.UPDATE_PLEA);
			
			
		    parser.parse(new InputSource(new java.io.StringReader(results)));
		    Document doc = parser.getDocument();
		    
		    
		    
		    NodeList pleas = doc.getElementsByTagName("code_record");
		   
		    //Create Initial Process Record
   			currentProcID = createProcessControlRecord(EventCode.UPDATE_PLEA,currentUser);
   			
   			
		    /**
             * SET UP AUDIT ENTRY
             */
    		audit = new AuditEntry();
    		audit.setCreateUsername(currentUser);
    		audit.setCreateDTime(Calendar.getInstance().getTime());
    		//auditDO.setCreateDtime(new Date(System.currentTimeMillis()));
		    
    		for(int i=0; i<pleas.getLength(); i++) 
		    {
    			
    			
    			
		    	CDPleaDO pleaDO = null;
    			
    		   Element eachPlea = (Element)pleas.item(i);
               
               Integer pleaID = new Integer(getChildValue(eachPlea, "code", 0));
               String pleaDesc = getChildValue(eachPlea, "description", 0);
               String status = StringUtil.mapToROMSStatus(getChildValue(eachPlea, "status", 0));
               StatusDO pleaStatus = daoFactory.getSchedulerDAO().find(StatusDO.class,status);
               
             
               /**
	              * DETERMINE IF RECORD ALREADY EXISTS, AND UPDATE OR SAVE AS NECESSARRY
	            */
       			CDPleaDO existingPlea = pleaExists(pleaID);
       			
       			if(existingPlea == null) 
       			{
       			       /**
		                * CREATE AND SAVE PLEA RECORD
		                */
       				   pleaDO = new CDPleaDO(pleaID, pleaDesc, pleaStatus, audit);
		       			
		       			//save Plea
		       			daoFactory.getSchedulerDAO().save(pleaDO);
		       		
       			}else
       			{
       				/**
		             * UPDATE PLEA RECORD
		            */
       				updateExistingPlea(existingPlea,pleaStatus,pleaDesc, currentUser);
       				
       			}
       			
       			numOfRecords++;
		    }
		    
    		
    		/**
             * SAVE EVENT AUDIT
             */
		    
			    EventAuditDO auditDO = new EventAuditDO();
			    auditDO.setEvent(daoFactory.getSchedulerDAO().find(CDEventDO.class,Constants.EventCode.UPDATE_PLEA));
			    auditDO.setComment("Plea Records Updated");
				auditDO.setRefType1(daoFactory.getSchedulerDAO().find(CDEventRefTypeDO .class, Constants.EventRefTypeCode.STAFF_ID));
				auditDO.setRefValue1(currentUser);
				auditDO.setRefType2(daoFactory.getSchedulerDAO().find(CDEventRefTypeDO .class, Constants.EventRefTypeCode.NUM_RECS_PROCESSED));
				auditDO.setRefValue2(""+numOfRecords);
				auditDO.setAuditEntry(audit);
		    	daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);
    		
		} catch (SAXException e) {
		    // handle SAXException 
		} catch (IOException e) {
		    // handle IOException 
		}catch(Exception dae)
		{
			throw new ErrorSavingException(dae.getLocalizedMessage());
		}finally
		{
			
			//Update Process control record
			updateProcessControlRecord(numOfRecords,message, success, currentProcID,currentUser,EventCode.UPDATE_PLEA);
			
		}
			
		return true;
}
	
	
	
	/**
	 * 
	 * @param currentUser
	 * @return
	 * @throws NoRecordFoundException 
	 * @throws ErrorSavingException 
	 */
@Override
@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
public boolean updateVerdict(String currentUser) throws NoRecordFoundException, ErrorSavingException{
		
		TTMSCodeService ttmsCodeService = serviceFactory.getTTMSCodeService();
		
		
		Integer currentProcID = 0;
	    String message = "";
	    boolean success = true;
	    int numOfRecords = 0;
		
		DOMParser parser = new DOMParser();
		try {
			
			String results = ttmsCodeService.updateTTMSCode(currentUser, TTMSCodeTypes.VERDICT,EventCode.UPDATE_VERDICT);
			
		    parser.parse(new InputSource(new java.io.StringReader(results)));
		    Document doc = parser.getDocument();
		    
		    
		    
		    NodeList verdicts = doc.getElementsByTagName("code_record");
		   
		    AuditEntry audit = new AuditEntry();
		    
		    //Create Initial Process Record
   			currentProcID = createProcessControlRecord(EventCode.UPDATE_VERDICT,currentUser);
   			
		    
		    /**
             * SET UP AUDIT ENTRY
             */
    		audit = new AuditEntry();
    		audit.setCreateUsername(currentUser);
    		audit.setCreateDTime(Calendar.getInstance().getTime());
    		//auditDO.setCreateDtime(new Date(System.currentTimeMillis()));
		    
		   
    		for(int i=0; i<verdicts.getLength(); i++) 
		    {
		    		
		    	CDVerdictDO verdictDO = null;
    			
    		   Element eachVerdict = (Element)verdicts.item(i);
               
               Integer verdictCode = new Integer(getChildValue(eachVerdict, "code", 0));
               String verdictDesc = getChildValue(eachVerdict, "description", 0);
               String status = StringUtil.mapToROMSStatus(getChildValue(eachVerdict, "status", 0));
               StatusDO verdictStatus = daoFactory.getSchedulerDAO().find(StatusDO.class,status);
               
             
               /**
	              * DETERMINE IF RECORD ALREADY EXISTS, AND UPDATE OR SAVE AS NECESSARRY
	            */
       			CDVerdictDO existingVerdict = verdictExists(verdictCode);
       			
       			if(existingVerdict == null) 
       			{
       			       /**
		                * CREATE AND SAVE Verdict RECORD
		                */
       					verdictDO = new CDVerdictDO(verdictCode, verdictDesc, verdictStatus);
		       			
		       			//save Verdict
		       			daoFactory.getSchedulerDAO().save(verdictDO);
		       		
       			}else
       			{
       				/**
		             * UPDATE VERDICT RECORD
		            */
       				updateExistingVerdict(existingVerdict,verdictStatus,verdictDesc, currentUser);
       				
       			}
       			
       			numOfRecords++;
		    }
		    
    		/**
             * SAVE EVENT AUDIT
             */
		    
			    EventAuditDO auditDO = new EventAuditDO();
			    auditDO.setEvent(daoFactory.getSchedulerDAO().find(CDEventDO.class,Constants.EventCode.UPDATE_VERDICT));
			    auditDO.setComment("Verdict Records Updated");
				auditDO.setRefType1(daoFactory.getSchedulerDAO().find(CDEventRefTypeDO .class, Constants.EventRefTypeCode.STAFF_ID));
				auditDO.setRefValue1(currentUser);
				auditDO.setAuditEntry(audit);
		    	daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);
   			
    		
       		
		} catch (SAXException e) {
		    // handle SAXException 
		} catch (IOException e) {
		    // handle IOException 
		}catch(Exception dae)
		{
			throw new ErrorSavingException(dae.getLocalizedMessage());
		}finally
		{
			//Update Process control record
			updateProcessControlRecord(numOfRecords,message, success, currentProcID,currentUser,EventCode.UPDATE_VERDICT);
		}
			
			
		return true;
}
	
	
	
	
/**
 * 
 * @param currentUser
 * @return
 * @throws NoRecordFoundException 
 * @throws ErrorSavingException 
 */
@Override
@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
public boolean updateCourtRuling(String currentUser) throws NoRecordFoundException, ErrorSavingException{
	
	TTMSCodeService ttmsCodeService = serviceFactory.getTTMSCodeService();
	
	Integer currentProcID = 0;
    String message = "";
    boolean success = true;
    int numOfRecords = 0;
	
	DOMParser parser = new DOMParser();
	try {
		
		String results = ttmsCodeService.updateTTMSCode(currentUser, TTMSCodeTypes.COURT_RULING,EventCode.UPDATE_COURT_RULING);
		
	    parser.parse(new InputSource(new java.io.StringReader(results)));
	    Document doc = parser.getDocument();
	    
	    
	    
	    NodeList crtRulings = doc.getElementsByTagName("code_record");
	   
	    //Create Initial Process Record
		currentProcID = createProcessControlRecord(EventCode.UPDATE_COURT_RULING,currentUser);
			
	    
	    
	    AuditEntry audit = new AuditEntry();
	    
	    /**
         * SET UP AUDIT ENTRY
         */
		audit = new AuditEntry();
		audit.setCreateUsername(currentUser);
		audit.setCreateDTime(Calendar.getInstance().getTime());
		//auditDO.setCreateDtime(new Date(System.currentTimeMillis()));
	    
	   
		for(int i=0; i<crtRulings.getLength(); i++) 
	    {
	    		
	    	CDCourtRulingDO courtRulingDO = null;
			
		   Element eachCourtRuling = (Element)crtRulings.item(i);
           
           String rulingID = getChildValue(eachCourtRuling, "code", 0);
           String desc = getChildValue(eachCourtRuling, "description", 0);
           String status = StringUtil.mapToROMSStatus(getChildValue(eachCourtRuling, "status", 0));
           StatusDO rulingStatus = daoFactory.getSchedulerDAO().find(StatusDO.class,status);
           
         
           /**
              * DETERMINE IF RECORD ALREADY EXISTS, AND UPDATE OR SAVE AS NECESSARRY
            */
           CDCourtRulingDO existingRuling = rulingExists(rulingID);
   			
   			if(existingRuling == null) 
   			{
   			       /**
	                * CREATE AND SAVE Verdict RECORD
	                */
   					courtRulingDO = new CDCourtRulingDO(rulingID, desc, 'Y', rulingStatus, audit);
	       			
   				
	       			//save Ruling
	       			daoFactory.getSchedulerDAO().save(courtRulingDO);
	       		
   			}else
   			{
   				/**
	             * UPDATE VERDICT RECORD
	            */
   				updateExistingRuling(existingRuling,rulingStatus,desc, currentUser);
   				
   			}
   			numOfRecords++;
	    }
	    
		/**
         * SAVE EVENT AUDIT
         */
	    
		    EventAuditDO auditDO = new EventAuditDO();
		    auditDO.setEvent(daoFactory.getSchedulerDAO().find(CDEventDO.class,Constants.EventCode.UPDATE_COURT_RULING));
		    auditDO.setComment("Court Ruling Records Updated");
			auditDO.setRefType1(daoFactory.getSchedulerDAO().find(CDEventRefTypeDO .class, Constants.EventRefTypeCode.STAFF_ID));
			auditDO.setRefValue1(currentUser);
			auditDO.setAuditEntry(audit);
	    	daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);
		
	} catch (SAXException e) {
	    // handle SAXException 
	} catch (IOException e) {
	    // handle IOException 
	}catch(Exception dae)
	{
		throw new ErrorSavingException(dae.getLocalizedMessage());
	}finally
	{
		
		//Update Process control record
		updateProcessControlRecord(numOfRecords,message, success, currentProcID,currentUser,EventCode.UPDATE_COURT_RULING);
	}
		
	return true;
}
	
	
	
	
	/**
	 * @throws ErrorSavingException 
	 * 
	 */
@Override
@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public boolean updateTAStaff(String currentUser) throws ErrorSavingException{
	
	Integer currentProcID = 0;
    String message = "";
    boolean success = true;
    int numOfRecords = 0;
	
	try{
			Date lastRun = null;
			
			//Create Initial Process Record
   			currentProcID = createProcessControlRecord(EventCode.UPDATE_TA_STAFF,currentUser);
   			
			
			
			
			if(!StringUtil.isSet(currentUser))
			{
				currentUser = "SYSTEM";
			}
			
			
			//Establish of this event was previously run or not
			lastRun = getLastRunDate(EventCode.UPDATE_TA_STAFF);
			
			List<BIMSStaffViewBO> listOfBIMSStaff = daoFactory.getSchedulerDAO().findTAStaff(lastRun);
			
			if (listOfBIMSStaff != null)
			{
				for (BIMSStaffViewBO bimsStaffViewBO : listOfBIMSStaff) {
					
					//get the staff record
					TAStaffDO taStaff = daoFactory.getSchedulerDAO().find(TAStaffDO.class, bimsStaffViewBO.getStaffId());
					
					
					/******Save Person Record*************/
					//update the person record for this staff
					PersonDO staffPerson = taStaff.getPerson();
					staffPerson = updateStaffPersonDetails(staffPerson,bimsStaffViewBO);
					
					AuditEntry retrievedPersonAudit = staffPerson.getAuditEntry();
					retrievedPersonAudit.setUpdateUsername(currentUser);
					retrievedPersonAudit.setUpdateDTime(Calendar.getInstance().getTime());
					staffPerson.setAuditEntry(retrievedPersonAudit);
		           
		   			
		   			//save Person
		   			daoFactory.getRoadCompliancyDAO().update(staffPerson);
		   			
		   			
					/************Save Staff*****************/		
					//update the staff record
		   			taStaff = updateStaffDetails(taStaff, bimsStaffViewBO);
		   			
		   			AuditEntry retrievedStaffAudit = staffPerson.getAuditEntry();
					retrievedStaffAudit.setUpdateUsername(currentUser);
					retrievedStaffAudit.setUpdateDTime(Calendar.getInstance().getTime());
		   			taStaff.setAuditEntry(retrievedStaffAudit);
		   		
		   			
		   			//save staff
		   			daoFactory.getSchedulerDAO().update(taStaff);
		   			
		   			numOfRecords++;
				}
				
				
					AuditEntry audit = new AuditEntry();
					audit = new AuditEntry();
		   			audit.setCreateUsername(currentUser);
		   			audit.setCreateDTime(Calendar.getInstance().getTime());
		   			
		   			
		   			/**
		   	         * SAVE EVENT AUDIT
		   	         */
		   		    
		   			
		   			EventAuditDO auditDO = new EventAuditDO();
				    auditDO = createEventAuditRecord(success,currentUser,numOfRecords,message,Constants.EventCode.UPDATE_TA_STAFF);
		   			
				  
		   				auditDO.setAuditEntry(audit);
		   		    	daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);
		   		
			}
		
			return true;
		}catch(Exception dae)
		{
			throw new ErrorSavingException(dae.getLocalizedMessage());
		}finally
		{
			
			//Update Process control record
			updateProcessControlRecord(numOfRecords,message, success, currentProcID,currentUser,Constants.EventCode.UPDATE_TA_STAFF);
		}
		
	}
	
	
	


	public List<ProcessControlBO> getLasRunProcesses() throws ErrorSavingException 
	{
		try{
			return  daoFactory.getSchedulerDAO().getLasRunProcesses();
		}catch(Exception dae)
		{
			throw new ErrorSavingException(dae.getLocalizedMessage());
		}
		
	}

















	
	/****************************************************************************************/
	/************HELPING METHODS************************************************************/
	

		private String getChildValue(Element parent, String childNodeName, int index)
	{
		String response = null;
		
		NodeList list = parent.getElementsByTagName(childNodeName);
		
		if(list.getLength() > 0)
		{
			Node node = list.item(index);
			if(node.hasChildNodes())
			{ 
				response = node.getFirstChild().getNodeValue().trim();
			} 
		}
		
		return response;
	}
	
	
	private Date getLastRunDate(Integer event)
	{
		Date createDateStr  = null;
		
		List<ProcessControlDO> events = daoFactory.getSchedulerDAO().findLastEventByCode(event);
		
	
		if(!events.isEmpty())
		{	int lastPos = (events.size()-1);
			createDateStr = ((ProcessControlDO)(events.get(lastPos))).getRunStartDtime();
			
		}
				
		return createDateStr;
		
	}
	
	
	private PersonDO updateStaffPersonDetails(PersonDO personDO,BIMSStaffViewBO bimsStaffViewBO)
	{
		personDO.setFirstName(bimsStaffViewBO.getFirstName());
		personDO.setLastName(bimsStaffViewBO.getLastName());
		personDO.setMiddleName(bimsStaffViewBO.getMiddleName());
		personDO.setTrnNbr(bimsStaffViewBO.getTrnNbr());
		
		return personDO;
	}
	
	
	private TAStaffDO updateStaffDetails(TAStaffDO taStaffDO,BIMSStaffViewBO bimsStaffViewBO)
	{
		taStaffDO.setOfficeLocationCode(bimsStaffViewBO.getLocationCode());
		taStaffDO.setStaffTypeCode(bimsStaffViewBO.getStaffType());
		
		String status = StringUtil.mapBadgeStatusToROMSStatus(bimsStaffViewBO.getBadgeStatusCode());
		taStaffDO.setStatus(daoFactory.getSchedulerDAO().find(StatusDO.class, status));
		
		return taStaffDO;
	}

		
	
	private PoliceOfficerDO policeExists(Integer polCompNo) 
	{
		PoliceOfficerDO retrievedPolice = null;
		retrievedPolice = daoFactory.getSchedulerDAO().find(PoliceOfficerDO.class, polCompNo);
		
		return retrievedPolice;
		
	}
	
	
	
	private boolean updateExistingPolice(PoliceOfficerDO retrievedPoliceDO,PoliceOfficerBO policeBO, StatusDO status)
	{

		retrievedPoliceDO.setRank(policeBO.getRank());
		retrievedPoliceDO.setStationDescription(policeBO.getStationDescription());
		retrievedPoliceDO.setStatus(status);
		
		
		//add audit entry
		AuditEntry audit = retrievedPoliceDO.getAuditEntry();
		audit.setUpdateUsername(policeBO.getCurrentUserName());
		audit.setUpdateDTime(Calendar.getInstance().getTime());
		retrievedPoliceDO.setAuditEntry(audit);
		
		
		
		return daoFactory.getSchedulerDAO().updatePolice(retrievedPoliceDO);
		
	}
	
	
	private CDPleaDO pleaExists(Integer pleaCode) 
	{
		CDPleaDO retrievedPlea = null;
		retrievedPlea = daoFactory.getSchedulerDAO().find(CDPleaDO.class, pleaCode);
		
		return retrievedPlea;
		
	}
	
	
	private void updateExistingPlea(CDPleaDO retrievedPleaDO, StatusDO status, String desc,String currentUser)
	{

		retrievedPleaDO.setStatus(status);
		retrievedPleaDO.setDescription(desc);
		
		//add audit entry
		AuditEntry audit = retrievedPleaDO.getAuditEntry();
		audit.setUpdateUsername(currentUser);
		audit.setUpdateDTime(Calendar.getInstance().getTime());
		retrievedPleaDO.setAuditEntry(audit);
		
		daoFactory.getSchedulerDAO().update(retrievedPleaDO);
		
	}
	
	
	private CDVerdictDO verdictExists(Integer verdictCode) 
	{
		CDVerdictDO retrievedVerdict = null;
		retrievedVerdict = daoFactory.getSchedulerDAO().find(CDVerdictDO.class, verdictCode);
		
		return retrievedVerdict;
		
	}
	
	
	private void updateExistingVerdict(CDVerdictDO retrievedVerdictDO, StatusDO status, String desc,String currentUser)
	{

		retrievedVerdictDO.setStatus(status);
		retrievedVerdictDO.setVerdict_desc(desc);
		
		//add audit entry
		AuditEntry audit = retrievedVerdictDO.getAuditEntry();
		audit.setUpdateUsername(currentUser);
		audit.setUpdateDTime(Calendar.getInstance().getTime());
		retrievedVerdictDO.setAuditEntry(audit);
		
		daoFactory.getSchedulerDAO().update(retrievedVerdictDO);
		
	}
	

	private CDCourtRulingDO rulingExists(String rulingID) 
	{
		CDCourtRulingDO retrievedRuling = null;
		retrievedRuling = daoFactory.getSchedulerDAO().find(CDCourtRulingDO.class, rulingID);
		
		return retrievedRuling;
		
	}
	
	
	private void updateExistingRuling(CDCourtRulingDO retrievedRulingDO, StatusDO status, String desc,String currentUser)
	{

		retrievedRulingDO.setStatus(status);
		retrievedRulingDO.setDescription(desc);
		
		//add audit entry
		AuditEntry audit = retrievedRulingDO.getAuditEntry();
		audit.setUpdateUsername(currentUser);
		audit.setUpdateDTime(Calendar.getInstance().getTime());
		retrievedRulingDO.setAuditEntry(audit);
		
		daoFactory.getSchedulerDAO().update(retrievedRulingDO);
		
	}


	@Transactional(propagation=Propagation.NESTED)
	private Integer createTerminatedProcessControlRecord(Integer eventCode, String user,String message)
	{
		Integer id = 0;
		
    		//TODO - link process_event to event table
    		ProcessControlDO proCntrolDO = new ProcessControlDO();
    		proCntrolDO.setEvent(daoFactory.getSchedulerDAO().find(CDEventDO.class,eventCode));
    		proCntrolDO.setProcessUser(user);
    		proCntrolDO.setRunStartDtime(Calendar.getInstance().getTime());
    		proCntrolDO.setStatusCode(Constants.ProcessControl.COMPLETED);
    		proCntrolDO.setTransProcessCnt(0);
    		proCntrolDO.setRunEndDtime(Calendar.getInstance(). getTime());
    		proCntrolDO.setMessageTxt(message);
    		
    		
    		id = (Integer)daoFactory.getSchedulerDAO().createTerminatedProcessControlRecord(proCntrolDO);
 		
		return id;
		
	}
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private Integer createProcessControlRecord(Integer eventCode, String user)
	{
		//TODO - link process_event to event table
		ProcessControlDO proCntrolDO = new ProcessControlDO();
		proCntrolDO.setEvent(daoFactory.getSchedulerDAO().find(CDEventDO.class,eventCode));
		proCntrolDO.setProcessUser(user);
		proCntrolDO.setRunStartDtime(Calendar.getInstance().getTime());
		proCntrolDO.setStatusCode(Constants.ProcessControl.STARTED);
		proCntrolDO.setTransProcessCnt(0);
		
		Integer id = (Integer)daoFactory.getSchedulerDAO().save(proCntrolDO);
		
		return id;
		
	}
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	private void updateProcessControlRecord(Integer numOfRecords,String message, boolean status, Integer currentProcID, String currentUser,Integer event)
	{
		//System.err.println("current id " + currentProcID);
		
		//TODO - link process_event to event table and store message
		if(currentProcID > 0)
		{
			ProcessControlDO proCntrolDO = daoFactory.getSchedulerDAO().find(ProcessControlDO.class, currentProcID);
			
			if(proCntrolDO != null)
			{
				proCntrolDO.setRunEndDtime(Calendar.getInstance().getTime());
				proCntrolDO.setStatusCode(Constants.ProcessControl.COMPLETED);
				proCntrolDO.setTransProcessCnt(numOfRecords);
			
				daoFactory.getSchedulerDAO().update(proCntrolDO);
			}
		}else
		{
			int newProcID = createTerminatedProcessControlRecord(event,currentUser,message);
			//System.err.println("new id " + newProcID);
		}
		
	}
	

	
	private EventAuditDO createEventAuditRecord(boolean success,
			String currentUser, int numOfRecords, String message, Integer eventCode) {
		
		
		 	EventAuditDO auditDO = new EventAuditDO();
		 
		 	auditDO.setEvent(daoFactory.getSchedulerDAO().find(CDEventDO.class,eventCode));
		    auditDO.setRefType1(daoFactory.getSchedulerDAO().find(CDEventRefTypeDO .class, Constants.EventRefTypeCode.NUM_RECS_PROCESSED));
			auditDO.setRefValue1(""+numOfRecords);
			
			String msgStr = StringUtil.isSet(message)? "Message: " + message : "";
			
			auditDO.setComment(success? "Success" : msgStr);
			
			return auditDO;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	private void createFailureAudit(boolean success, String currentUser,
			int numOfRecords, String message, Integer updatePoliceOfficer) {

		EventAuditDO auditDO = new EventAuditDO();
		auditDO = createEventAuditRecord(success, currentUser,numOfRecords, message, updatePoliceOfficer);
		
		/**
         * SET UP AUDIT ENTRY
         */
		AuditEntry audit = new AuditEntry();
		audit = new AuditEntry();
		audit.setCreateUsername(currentUser);
		audit.setCreateDTime(Calendar.getInstance().getTime());
		
		auditDO.setAuditEntry(audit);
		daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);
	}
}

