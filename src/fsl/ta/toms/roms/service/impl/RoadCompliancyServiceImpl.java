package fsl.ta.toms.roms.service.impl;



import java.io.Serializable;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.amvswebservice.ArrayOfVehOwner;
import fsl.ta.toms.roms.amvswebservice.FslWebServiceException_Exception;
import fsl.ta.toms.roms.amvswebservice.VehFitness;
import fsl.ta.toms.roms.amvswebservice.VehInfo;
import fsl.ta.toms.roms.amvswebservice.VehInsurance;
import fsl.ta.toms.roms.amvswebservice.VehMvrc;
import fsl.ta.toms.roms.amvswebservice.VehOwner;
import fsl.ta.toms.roms.amvswebservice.Vehicle;
import fsl.ta.toms.roms.bo.AddressBO;
import fsl.ta.toms.roms.bo.BadgeBO;
import fsl.ta.toms.roms.bo.BadgeCheckResultBO;
import fsl.ta.toms.roms.bo.CitationCheckResultBO;
import fsl.ta.toms.roms.bo.CitationOffenceBO;
import fsl.ta.toms.roms.bo.ComplianceBO;
import fsl.ta.toms.roms.bo.ComplianceDetailsBO;
import fsl.ta.toms.roms.bo.ComplianceParamBO;
import fsl.ta.toms.roms.bo.CompliancyCheckBO;
import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.DLCheckResultBO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.ExcerptParamMappingBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.OffenceMandatoryOutcomeBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.bo.RoadCheckBO;
import fsl.ta.toms.roms.bo.RoadCheckOffenceOutcomeBO;
import fsl.ta.toms.roms.bo.RoadLicCheckResultBO;
import fsl.ta.toms.roms.bo.RoadLicenceBO;
import fsl.ta.toms.roms.bo.RoadLicenceDriverConductorBO;
import fsl.ta.toms.roms.bo.RoadLicenceOwnerBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.VehicleBO;
import fsl.ta.toms.roms.bo.VehicleCheckResultBO;
import fsl.ta.toms.roms.bo.VehicleFitnessBO;
import fsl.ta.toms.roms.bo.VehicleInsuranceBO;
import fsl.ta.toms.roms.bo.VehicleOwnerBO;
import fsl.ta.toms.roms.bo.WarningNoProsecutionBO;
import fsl.ta.toms.roms.bo.WarningNoticeBO;
import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.DocumentStatus;
import fsl.ta.toms.roms.constants.Constants.EventCode;
import fsl.ta.toms.roms.constants.Constants.EventRefTypeCode;
import fsl.ta.toms.roms.constants.Constants.OutcomeType;
import fsl.ta.toms.roms.constants.Constants.RoadCheckType;
import fsl.ta.toms.roms.constants.Constants.Status;
import fsl.ta.toms.roms.constants.Constants.YesNo;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.AddressDO;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.BadgeCheckResultDO;
import fsl.ta.toms.roms.dataobjects.CDEventDO;
import fsl.ta.toms.roms.dataobjects.CDEventRefTypeDO;
import fsl.ta.toms.roms.dataobjects.CDOutcomeTypeDO;
import fsl.ta.toms.roms.dataobjects.CDRoadCheckTypeDO;
import fsl.ta.toms.roms.dataobjects.CitationCheckDO;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.ComplianceParamDO;
import fsl.ta.toms.roms.dataobjects.ComplianceParamKey;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.DLCheckResultDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.ExcerptParamMappingDO;
import fsl.ta.toms.roms.dataobjects.LicenceDriverConductorDO;
import fsl.ta.toms.roms.dataobjects.LicenceOwnerDO;
import fsl.ta.toms.roms.dataobjects.OffenceCheckResultDO;
import fsl.ta.toms.roms.dataobjects.OffenceDO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;
import fsl.ta.toms.roms.dataobjects.ReasonDO;
import fsl.ta.toms.roms.dataobjects.RoadCheckDO;
import fsl.ta.toms.roms.dataobjects.RoadCheckOffenceDO;
import fsl.ta.toms.roms.dataobjects.RoadCheckOffenceOutcomeDO;
import fsl.ta.toms.roms.dataobjects.RoadLicCheckResultDO;
import fsl.ta.toms.roms.dataobjects.RoadOperationDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;
import fsl.ta.toms.roms.dataobjects.VehicleCheckResultDO;
import fsl.ta.toms.roms.dataobjects.VehicleCheckResultOwnerDO;
import fsl.ta.toms.roms.dataobjects.VehicleDO;
import fsl.ta.toms.roms.dataobjects.VehicleFitnessDO;
import fsl.ta.toms.roms.dataobjects.VehicleInsuranceDO;
import fsl.ta.toms.roms.dataobjects.VehicleOwnerDO;
import fsl.ta.toms.roms.dataobjects.WarningNoProsecutionDO;
import fsl.ta.toms.roms.dataobjects.WarningNoticeDO;
import fsl.ta.toms.roms.dataobjects.WitnessWarningNoticeDO;
import fsl.ta.toms.roms.dataobjects.WitnessWarningNoticeKey;
import fsl.ta.toms.roms.dlwebservice.DriverLicenceDetails;
import fsl.ta.toms.roms.dlwebservice.FslDriversLicence;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.DocumentsCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.PersonCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadCompliancyCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.VehicleCriteriaBO;
import fsl.ta.toms.roms.service.LMISService;
import fsl.ta.toms.roms.service.RoadCompliancyService;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.trnwebservice.InvalidTrnBranchException_Exception;
import fsl.ta.toms.roms.trnwebservice.NotValidTrnTypeException_Exception;
import fsl.ta.toms.roms.trnwebservice.SystemErrorException_Exception;
import fsl.ta.toms.roms.trnwebservice.TrnDTO;
import fsl.ta.toms.roms.trnwebservice.TrnValidatorSEI;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.NameUtil;
import fsl.ta.toms.roms.util.StringUtil;
import fsl.ta.toms.roms.webservices.AMVSWebService;
import fsl.ta.toms.roms.webservices.DocumentsManager;
import fsl.ta.toms.roms.webservices.StaffUserMapping;
//import com.ibm.wsspi.sib.exitpoint.ra.HashMap;

@Transactional
public class RoadCompliancyServiceImpl implements RoadCompliancyService{
	
	private DAOFactory daoFactory;
	
	@Autowired
	private ServiceFactory serviceFactory;
	 
	
	@Autowired
	TrnValidatorSEI trnService;
	
	@Autowired
	FslDriversLicence dlWebservice;
	
	protected Logger logger = LogManager.getLogger(this.getClass().getName());
	
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
	public <T> T find(Class<T> clazz, Serializable id ) {
		
		return daoFactory.getRoadCompliancyDAO().find(clazz, id);
	}
	
	
	/**
	 * Helper method created to manage transaction for completing road check.
	 * This was necessary because the @Transactional annotation at the interface level resulted in a 404 error.
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public synchronized List<DocumentViewBO> recordOffenceOutcomeHelper(List<CompliancyCheckBO> listOfCompliancyChecks, ComplianceBO updatedComplianceDetails) 
			throws RequiredFieldMissingException, InvalidFieldException, ErrorSavingException
	{
		List<DocumentViewBO> allSavedDocs = new ArrayList<DocumentViewBO>();
		
		
		/**
		 * Loop through list of documents and process
		 */
		if (listOfCompliancyChecks != null && listOfCompliancyChecks.size()>0) {
			for (CompliancyCheckBO  compliancyCheck : listOfCompliancyChecks) {

				// current user is required
				if (!StringUtil.isSet(compliancyCheck.getCurrentUserName())) {
					throw new RequiredFieldMissingException(
							"Current User is Required");
				}

				allSavedDocs.addAll(recordOffenceOutcome(compliancyCheck));
			}
		}
		
		if(allSavedDocs != null)
		{
			/* Loop through save doc view and ensure only one warning notice and warning no prosecution is in the
			 * list. */
			Integer maxWarningNoticeOffence = 0;
			Integer maxWarningNoProOffence = 0;
	
			DocumentViewBO warningNoticeNotToBeRemoved = null;
			DocumentViewBO warningNoProNotToBeRemoved = null;
	
			for (DocumentViewBO doc : allSavedDocs) {
				if (doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					if (doc.getListOfOffences().size() > maxWarningNoticeOffence) {
						maxWarningNoticeOffence = doc.getListOfOffences().size();
						warningNoticeNotToBeRemoved = doc;
					}
	
				} else if (doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					if (doc.getListOfOffences().size() > maxWarningNoProOffence) {
						maxWarningNoProOffence = doc.getListOfOffences().size();
						warningNoProNotToBeRemoved = doc;
					}
	
				}
	
			}
	
			List<DocumentViewBO> documentsToBeRemoved = new ArrayList<DocumentViewBO>();
			for (DocumentViewBO doc : allSavedDocs) {
				if (doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE)) {
					if (doc.getListOfOffences().size() <= maxWarningNoticeOffence) {
						if (!doc.equals(warningNoticeNotToBeRemoved)) {
							documentsToBeRemoved.add(doc);
						}
	
					}
	
				} else if (doc.getDocumentTypeCode().equalsIgnoreCase(Constants.DocumentType.WARNING_NOTICE_NO_PROSECUTION)) {
					if (doc.getListOfOffences().size() <= maxWarningNoProOffence) {
						if (!doc.equals(warningNoProNotToBeRemoved)) {
							documentsToBeRemoved.add(doc);
						}
					}
	
				}
	
			}
	
			for (DocumentViewBO doc : documentsToBeRemoved) {
				allSavedDocs.remove(doc);
			}
		}
		
		return allSavedDocs;
			
	
	}
	
	
	/**
	 * This method was updated by Oneal Anguin on 23 December 2013
	 * 	-Update key : oa12232013
	 * 	-These updates are to allow a list of road check offences created to be passed back in web service. 
	 * 	-This will assist with loading scanned documents from screen as this process is dependant on road check offence
	 * 	-outcome IDs.
	 *  -Update key : oa02052013
	 *  -This update is to stop the creation of duplicate warning notice and warning no prosecution. Also to create road check type of
	 *  -other where an offence related to other is being recorded.
	 *  -Update key : oa02132013
	 *  -This update is to ensure that all documents created in house have their verification flag set to false.
	 */
	@Override
	public synchronized List<DocumentViewBO> recordOffenceOutcome(CompliancyCheckBO compliancyCheck) throws InvalidFieldException, 
	RequiredFieldMissingException, ErrorSavingException
	{
		//update key: oa12232013
		List<DocumentViewBO> returnListOfDocumentViews = new ArrayList<DocumentViewBO>();
		
		String currentUserName = compliancyCheck.getCurrentUserName();
		RoadCheckDO roadChkDO = null;
		ComplianceDO complianceDO = null;
		RoadOperationDO roadOperationDO = null;
		
		//retrieve road check record
		Integer roadCheckID = compliancyCheck.getRoadCheckID();
		
		if(roadCheckID == null)
		{
			throw new RequiredFieldMissingException("Road Check ID Required");
		}else
		{
			roadChkDO = daoFactory.getRoadCompliancyDAO().find(RoadCheckDO.class, roadCheckID);
			
			if(roadChkDO == null)
			{
				throw new InvalidFieldException("Invalid Road Check ID");
			}
		}
		
		//retrieve compliance record
		Integer complianceID = roadChkDO.getCompliance().getComplianceId();
		complianceDO = daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, complianceID);
		
		
		//retrieve roadOperationID
		Integer roadOperationId = null;
		if( complianceDO.getRoadOperation() != null)
		{
			roadOperationId = complianceDO.getRoadOperation().getRoadOperationId();
			roadOperationDO = daoFactory.getRoadCompliancyDAO().find(RoadOperationDO.class, roadOperationId);
		}
		
		
		//retrieveOffender Info
		PersonDO offenderPersonDO = complianceDO.getPerson();
		PersonBO offender = new PersonBO(offenderPersonDO);
		
		
		//retrieve Vehicle Info
		
		
		//retrieve Staff Info
		StaffUserMapping staffUserMappingI = new StaffUserMapping();
		TAStaffBO taStaffBO = new TAStaffBO();
		
		try
		{
			/*OA- 4Feb2014 : Put in because the staff that should be saved against documents is the staff whom did the compliance check.*/
			//TODO To be reviewed.
			//taStaffBO = staffUserMappingI.getStaffByUsername(currentUserName);
			taStaffBO = staffUserMappingI.getStaffByUsername(complianceDO.getTaStaff().getLoginUsername());
		}catch(NoRecordFoundException nr)
		{
			throw new InvalidFieldException("Invalid TA Staff ID : " + complianceDO.getTaStaff().getStaffId());
		} 
		
		
		//retrieve Court Info for regional operations
		Integer courtID = null;
		String courtName = null;
		Date courtDateTime =  null;
		
		//removed by kpowell: UR-065 - pull court as set by roadcheck, since user now has the ability to change it,
		//even when it is a regional operation
		//if this is a Regional Operation, retrieve court info from road operation record
	/*	if(roadOperationDO != null && roadOperationDO.getCategory().getCategoryId().equals(Category.REGIONAL))
		{
			courtID = roadOperationDO.getCourt().getCourtId();
			courtName = roadOperationDO.getCourt().getDescription();
			courtDateTime = roadOperationDO.getCourtDtime();
		}*/
		
		
		//initialize variable to keep track of number of warning notices created
		int numWarningNotice = 0;
		
		//initialize variable to keep track of number of warning notices no prosecution created
		int numWarningNoticeNoPros = 0;
		
		//prepare audit entry
		AuditEntry audit = new AuditEntry();
		audit = new AuditEntry();
		audit.setCreateUsername(currentUserName);
		audit.setCreateDTime(Calendar.getInstance().getTime());
		
		int negativeCnt = 0;
		
		/******
		 * If the outcome is All in Order, this means no offence was noted.
		 * Otherwise, proceed to process offence(s) recorded and their outcomes
		 */
		if(!compliancyCheck.getOutcomeTypeID().equalsIgnoreCase(OutcomeType.ALL_IN_ORDER))
		{
			/**
			 * process each offence outcome 
			 * save road check offence records.
			 * extract and save each offence commited for each check
			 */
			List<RoadCheckOffenceOutcomeBO> offenceOutcomes = compliancyCheck.getListOfRoadChkOffOutcome();
			
			if(offenceOutcomes == null)
			{
				throw new RequiredFieldMissingException("There Must Be At Least One Offence and Outcome");
				
			}
			
			Integer newOffenceID = null;
			Integer prevOffenceID = null;
			RoadCheckOffenceDO savedRoadCheckOff = new RoadCheckOffenceDO();
			OffenceDO offenceDO = new OffenceDO();
			
			WarningNoticeBO warningNoticeBO = null;
			
			WarningNoProsecutionBO warningNoticeNoPros = null;
			
			/*Update Key: oa02052013*/
			//search for warning notice or warning no prosecution based on compliancy id
			//It is put in two different try blocks in case it fails to find one but finds the next.
			try
			{
				DocumentsManager searchForWarningDocs = new DocumentsManager();
				
				DocumentsCriteriaBO docCriteria = new DocumentsCriteriaBO();
				
				docCriteria.setComplianceId(complianceDO.getComplianceId());
				
				DocumentViewBO doc  = searchForWarningDocs.lookupWarningNotice(docCriteria).get(0);
				
				
				if(warningNoticeBO != doc)
				{
					warningNoticeBO = searchForWarningDocs.getWarningNoticeDetails(doc.getAutomaticSerialNo());
					returnListOfDocumentViews.add(warningNoticeBO);
					numWarningNotice++;
				}
				
				
			}
			catch(Exception exe)
			{
				exe.printStackTrace();
			}
			
			try
			{
				DocumentsManager searchForWarningDocs = new DocumentsManager();
				
				DocumentsCriteriaBO docCriteria = new DocumentsCriteriaBO();
				
				docCriteria.setComplianceId(complianceDO.getComplianceId());
				
				DocumentViewBO doc  = searchForWarningDocs.lookupWarningNoProsecution(docCriteria).get(0);
				
				if(doc != null)
				{
					warningNoticeNoPros = searchForWarningDocs.getWarningNoProsecutionDetails(doc.getAutomaticSerialNo());
					returnListOfDocumentViews.add(warningNoticeNoPros);
					numWarningNoticeNoPros++;
				}
				
				
			}
			catch(Exception exe)
			{
				exe.printStackTrace();
			}
			
			for (RoadCheckOffenceOutcomeBO roadChkOffOutcome : offenceOutcomes) {
			
				newOffenceID = roadChkOffOutcome.getOffenceId();
				
				if(prevOffenceID != newOffenceID)  //only if outcome relates to a new offence should a new record be created
				{
					//Create Road check offence record
					offenceDO = daoFactory.getRoadCompliancyDAO().find(OffenceDO.class, newOffenceID);
					
					RoadCheckOffenceDO roadChkOffDo = new RoadCheckOffenceDO(roadChkDO, offenceDO, audit);
					
					Integer roadCheckOffenceID = (Integer)daoFactory.getRoadCompliancyDAO().save(roadChkOffDo);
					
					savedRoadCheckOff = daoFactory.getRoadCompliancyDAO().find(RoadCheckOffenceDO.class, roadCheckOffenceID);
					
				}
				
				
				
				/********************************************************************************
				 * Extract any information that may be used to create summons or warning notice
				 ********************************************************************************
				 */
				 				
					//retrieve JP Info
					//String jpRegNo = roadChkOffOutcome.getJpRegNum();
					Integer jpIdNo = roadChkOffOutcome.getJpIdNumber();
					
					
					//System.err.println("Court Selected for Document :: "+ roadChkOffOutcome.getCourtId());
					//modified by kpowell: UR-065 - pull court as set by roadcheck, since user now has the ability to change it,
					//even when it is a regional operation
					//retrieve Court Info
					//if this is NOT a Regional Operation, retrieve court info for each summons. 
				/*	if( (roadOperationDO == null || !roadOperationDO.getCategory().getCategoryId().equals(Category.REGIONAL)) && roadChkOffOutcome.getCourtId() != null )
					{*/
					if(roadChkOffOutcome.getCourtId() != null){
						//System.err.println("This is NOT a REGIONAL Operation!");	
						courtID = roadChkOffOutcome.getCourtId();
						CourtDO courtDO = daoFactory.getRoadCompliancyDAO().find(CourtDO.class, courtID);
						courtName =courtDO.getDescription();
						courtDateTime = roadChkOffOutcome.getCourtDateTime();
					}
					
					//retrieve Pound Info
					Integer poundID = roadChkOffOutcome.getPoundID();
					
					//retrieve Wrecking Company Info
					Integer wreckingCoID = roadChkOffOutcome.getWreckingCoID();
					
					Integer vehicleMoverPersonID = roadChkOffOutcome.getVehicleMoverPersonID();
					
					Integer wreckerVehicleID = roadChkOffOutcome.getWreckerVehicleID();
					
					String vehicleMoverPersonType = roadChkOffOutcome.getVehicleMoverPersonType();
					
					//retrieve Issued Date
					Date issueDate = roadChkOffOutcome.getIssueDate();
					
					//retrieve Offence Date
					Date offenceDateTime = roadChkOffOutcome.getOffenceDateTime();
				
					
				
					
					
					
				/**********************************************************************************
				 * Process Outcomes
				 ********************************************************************************
				 */		
				 //Create Road Check Offence Outcome Record
				String outcomeTypeID = roadChkOffOutcome.getOutcomeTypeID();
				CDOutcomeTypeDO cdOutcomeTypeDo = daoFactory.getRoadCompliancyDAO().find(CDOutcomeTypeDO.class, outcomeTypeID);
				
				RoadCheckOffenceOutcomeDO roadChkOffOutcomeDO = new RoadCheckOffenceOutcomeDO(savedRoadCheckOff, cdOutcomeTypeDo, audit);
				
				Integer rdChkOffOutcomeID = (Integer)daoFactory.getRoadCompliancyDAO().save(roadChkOffOutcomeDO);
				
				RoadCheckOffenceOutcomeDO savedRoadCheckOffenceOutcome = daoFactory.getRoadCompliancyDAO().find(RoadCheckOffenceOutcomeDO.class,rdChkOffOutcomeID);
				
				
			
				//if outcome is not "All in order", then increase negative count
				if(!outcomeTypeID.equalsIgnoreCase(OutcomeType.ALL_IN_ORDER))
				{	negativeCnt++;	}
				
				
				//if outcome is issue summons, create summons
				//Summons should also be created for warned for prosecution OA @ 15 January 2014
				if(outcomeTypeID.equalsIgnoreCase(OutcomeType.ISSUE_SUMMONS) || outcomeTypeID.equalsIgnoreCase(OutcomeType.WARNED_FOR_PROSECUTION))
				{	
					//Validate Court Date
					DateUtils.validateCourtDate(courtDateTime, issueDate);
					
					if(roadChkOffOutcome.getOwner()!=null){
						VehicleOwnerBO owner = roadChkOffOutcome.getOwner();
						PersonDO personDO = new PersonDO();
						personDO.setFirstName(owner.getFirstName());
						personDO.setMiddleName(owner.getMidName());
						personDO.setLastName(owner.getLastName());
						personDO.setTrnNbr(owner.getTrnNbr());
						
						AddressDO addressDO = new AddressDO();
						
						addressDO.setStreetName(owner.getAddress().getStreetName());
						addressDO.setStreetNo(owner.getAddress().getStreetNumber());
						addressDO.setMarkText(owner.getAddress().getMark());
						addressDO.setPoBoxNo(owner.getAddress().getPoBoxNumber());
						addressDO.setPoLocationName(owner.getAddress().getPoLocation());
						
						ParishDO parishDO = null;
						try{
							parishDO = daoFactory.getRoadCompliancyDAO()
								.find(ParishDO.class, owner.getAddress().getParish_code());
							addressDO.setParish(parishDO);
						}catch(Exception exception){
							addressDO.setParish(null);
						}
						
						personDO.setAddress(addressDO);
						
						/*PersonBO personBO = savePerson(new PersonBO(personDO));
						
						PersonDO existPerson = daoFactory.getPersonDAO().findPersonByTRN(personDO.getTrnNbr());
						if(existPerson==null){
							personDO.s
						}*/
						
						personDO = addORUpdatePersonByTrn(personDO, currentUserName);
						
						
						/*daoFactory.getRoadCompliancyDAO().saveOrUpdate(personDO);												
						personDO  = daoFactory.getPersonDAO().findPersonByTRN(personDO.getTrnNbr());*/
												
						
						complianceDO.setAidAbetVehicleOwner(personDO);
						
					}					
					
					SummonsBO summonsBO = new SummonsBO(roadOperationId, savedRoadCheckOffenceOutcome.getRoadCheckOffenceOutcomeId(),
							offender.getPersonId(),taStaffBO.getStaffId(), jpIdNo, null, false, offenceDateTime, 
							offenceDO.getOffenceId(),complianceDO.getCompliancyArtery().getDescription(), offenceDO.getExcerpt(), 
							courtID,courtName,courtDateTime, Status.COURT_CASE_OPEN, DocumentStatus.CREATED, null, null, 
							YesNo.YES_LABEL, null, null, null, null, null, null, null, currentUserName, issueDate, 
							null, currentUserName,compliancyCheck.getExcerptParams());
					
					/*
					 * OA - 24 Jan 2014 : Where the road operation is terminated and a document is being created it needs to be
					 * verified.
					 * */
					/*-Update key : oa02132013*/
					if(roadOperationDO != null && roadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED) || (compliancyCheck.getInHouse() != null && compliancyCheck.getInHouse()))
					{
						summonsBO.setVerificationRequired(Constants.YesNo.YES_FLAG + "");
					}
					
					
					//Set offender name
					if(offender != null)
					{
						summonsBO.setOffenderFullName(offender.getFullName());
					}
					
					try{
						Integer offenceCode = summonsBO.getOffenceCode();
					
						/**
						 * If offence type is Aiding and Abetting Driver etc.(Summons 35 & 36) change offender to the owner		 * 
						 */
						if(offenceCode.intValue()==Constants.OffenceAidAndAbbet.NO_ROAD_LIC.intValue()||
								offenceCode.intValue()==Constants.OffenceAidAndAbbet.NO_PPV_INSUR.intValue()){
							
							summonsBO.setOffenderId(complianceDO.getAidAbetVehicleOwner().getPersonId());
						}
					}catch(Exception exception){
						exception.printStackTrace();
					}
					
					
					DocumentsManager summosI = new DocumentsManager();
					summosI.createSummons(summonsBO);
					
					List<OffenceBO> offenceList = new ArrayList<OffenceBO>();
					
					offenceList.add(new OffenceBO(offenceDO));
					
					summonsBO.setListOfOffences(offenceList);
					
					//update key: oa12232013
					returnListOfDocumentViews.add(summonsBO);
				}
				
				
				
				//if outcome is issue warning notice, create warning notice
				if(outcomeTypeID.equalsIgnoreCase(OutcomeType.ISSUE_WARNING_NOTICE))
				{	
					
					if(numWarningNotice == 0 || warningNoticeBO == null) //only one warning notice should be created per offender
					{
							warningNoticeBO = new WarningNoticeBO(roadOperationId, 
								savedRoadCheckOffenceOutcome.getRoadCheckOffenceOutcomeId(), 
								offender.getPersonId(), taStaffBO.getStaffId(), poundID, wreckingCoID, false, null, offenceDateTime, 
								DocumentStatus.CREATED, null, null, YesNo.YES_LABEL, null, null, null, null, null, null, null, 
								currentUserName, issueDate, null, currentUserName, compliancyCheck.getAllegation());
						
							
							warningNoticeBO.setWreckerVehicleID(wreckerVehicleID);
							warningNoticeBO.setVehicleMoverPersonID(vehicleMoverPersonID);
							warningNoticeBO.setVehicleMoverPersonType(vehicleMoverPersonType);
						
						/*
						 * OA - 24 Jan 2013 : Where the road operation is terminated and a document is being created it needs to be
						 * verified.
						 * */
						/*-Update key : oa02132013*/
						if(roadOperationDO != null && roadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED) || (compliancyCheck.getInHouse() != null && compliancyCheck.getInHouse()))
						{
							warningNoticeBO.setVerificationRequired(Constants.YesNo.YES_FLAG + "");
						}
						
				
						//Set offender name
						if(offender != null)
						{
							warningNoticeBO.setOffenderFullName(offender.getFullName());
						}
							
						DocumentsManager warningNoticeI = new DocumentsManager();
						Integer wNoticeID1 = warningNoticeI.createWarningNotice(warningNoticeBO);
						
						
						
						//update key: oa12232013
						returnListOfDocumentViews.add(warningNoticeBO);
						warningNoticeBO.setListOfOffences(new ArrayList<OffenceBO>());
						
						numWarningNotice++;
						
						//CREATE WITNESS RECORD AND SAVE/UPDATE PERSON RECORD FR WITNESS
						createWitnesses(roadChkOffOutcome,wNoticeID1,audit); 
					}
					
					//update key: oa12232013
					warningNoticeBO.getListOfOffences().add(new OffenceBO(offenceDO));
					
				}
				
				
				/*Added by OA - 21 Jan 2014 to facilitate creating Warning Notice No Prosecution it is similar to warning notice in that only one
				  can be created per road check.*/
				if(outcomeTypeID.equalsIgnoreCase(OutcomeType.WARNED_NO_PROSECUTION))
				{	
					
					if(numWarningNoticeNoPros == 0 || warningNoticeNoPros == null) //only one warning notice no prosecution should be created per offender
					{
							warningNoticeNoPros = new WarningNoProsecutionBO(null/*warningNoProsecutionId*/, 
														roadOperationId, 
														savedRoadCheckOffenceOutcome.getRoadCheckOffenceOutcomeId()/*roadCheckOffenceOutcomeCode*/, 
														offender.getPersonId()/*offenderId*/, 
														taStaffBO.getStaffId()/*taStaffId*/, 
														false/*isManualWarningNotice*/, 
														null/*manualSerialNumber*/, 
														DocumentStatus.CREATED/*statusId*/, 
														null/*reasonCode*/, 
														null/*comment*/, 
														YesNo.YES_LABEL/*allowUpload*/, 
														null/*printUsername*/, 
														null/*printDtime*/, 
														null/*reprintUsername*/, 
														null/*reprintDtime*/, 
														null/*authorizedFlag*/, 
														null/*authorizeUsername*/, 
														null/*authorizeDtime*/, 
														currentUserName/*issueUsername*/, 
														issueDate, 
														null/*scannedDocList*/, 
														currentUserName/*currentUsername*/, 
														issueDate/*offenceDtime*/,
														compliancyCheck.getAllegation());
							
						
						/*
						 * OA - 24 Jan 2013 : Where the road operation is terminated and a document is being created it needs to be
						 * verified.
						 * */
						/*-Update key : oa02132013*/
						if(roadOperationDO!=null && roadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED) || (compliancyCheck.getInHouse() != null && compliancyCheck.getInHouse()))
						{
							warningNoticeNoPros.setVerificationRequired(Constants.YesNo.YES_FLAG + "");
						}
						
						
						//Set offender name
						if(offender != null)
						{
							warningNoticeNoPros.setOffenderFullName(offender.getFullName());
						}
						
						
						DocumentsManager warningNoticeNoProsI = new DocumentsManager();
						Integer wNoticeNoProsID1 = warningNoticeNoProsI.createWarningNoProsecution(warningNoticeNoPros);
						
						
						//update key: oa12232013
						returnListOfDocumentViews.add(warningNoticeNoPros);
						warningNoticeNoPros.setListOfOffences(new ArrayList<OffenceBO>());
						
						numWarningNoticeNoPros++;
						
	
					}
					
					//update key: oa12232013
					warningNoticeNoPros.getListOfOffences().add(new OffenceBO(offenceDO));
					
				}
				
				//if outcome is vehicle seizure, create both summons and warning notice
				if(outcomeTypeID.equalsIgnoreCase(OutcomeType.VEHICLE_SEIZURE))
				{	

					//Validate Court Date
					DateUtils.validateCourtDate(courtDateTime, issueDate);
					
					//CREATE SUMMONS
					SummonsBO summonsBO = new SummonsBO(roadOperationId, savedRoadCheckOffenceOutcome.getRoadCheckOffenceOutcomeId(), 
							offender.getPersonId(),taStaffBO.getStaffId(), jpIdNo, null, false, offenceDateTime, 
							offenceDO.getOffenceId(),complianceDO.getCompliancyArtery().getDescription(), offenceDO.getExcerpt(), 
							courtID,courtName,courtDateTime, Status.COURT_CASE_OPEN, DocumentStatus.CREATED, null, null, 
							YesNo.YES_LABEL, null, null, null, null, null, null, null, currentUserName, issueDate, 
							null, currentUserName,compliancyCheck.getExcerptParams());
					
					/*
					 * OA - 24 Jan 2014 : Where the road operation is terminated and a document is being created it needs to be
					 * verified.
					 * */
					/*-Update key : oa02132013*/
					if(roadOperationDO != null && roadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED) || (compliancyCheck.getInHouse() != null && compliancyCheck.getInHouse()))
					{
						summonsBO.setVerificationRequired(Constants.YesNo.YES_FLAG + "");
					}
					
					
					//Set offender name
					if(offender != null)
					{
						summonsBO.setOffenderFullName(offender.getFullName());
					}
					
					DocumentsManager summosI = new DocumentsManager();
					summosI.createSummons(summonsBO);
					
					//update key: oa12232013
					List<OffenceBO> offenceList = new ArrayList<OffenceBO>();
					
					offenceList.add(new OffenceBO(offenceDO));
					
					summonsBO.setListOfOffences(offenceList);
					
					returnListOfDocumentViews.add(summonsBO);
					
					if(numWarningNotice == 0 || warningNoticeBO == null) //only one warning notice should be created per offender
					{	
						//CREATE WARNING NOTICE
						warningNoticeBO = new WarningNoticeBO(roadOperationId, savedRoadCheckOffenceOutcome.getRoadCheckOffenceOutcomeId(), 
								offender.getPersonId(), taStaffBO.getStaffId(), poundID, wreckingCoID, false, null, offenceDateTime, 
								DocumentStatus.CREATED, null, null, YesNo.YES_LABEL, null, null, null, null, null, null, null, 
								currentUserName, issueDate, null, currentUserName, compliancyCheck.getAllegation());
						
						warningNoticeBO.setWreckerVehicleID(wreckerVehicleID);
						warningNoticeBO.setVehicleMoverPersonID(vehicleMoverPersonID);
						warningNoticeBO.setVehicleMoverPersonType(vehicleMoverPersonType);
						
						/*
						 * OA - 24 Jan 2014 : Where the road operation is terminated and a document is being created it needs to be
						 * verified.
						 * */
						/*-Update key : oa02132013*/
						if(roadOperationDO != null && roadOperationDO.getStatus().getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED) || (compliancyCheck.getInHouse() != null && compliancyCheck.getInHouse()))
						{
							warningNoticeBO.setVerificationRequired(Constants.YesNo.YES_FLAG + "");
						}
						
						
						//Set offender name
						if(offender != null)
						{
							warningNoticeBO.setOffenderFullName(offender.getFullName());
						}
						
						DocumentsManager warningNoticeI = new DocumentsManager();
						Integer wNoticeID2 = warningNoticeI.createWarningNotice(warningNoticeBO);
						
						//update key: oa12232013
						returnListOfDocumentViews.add(warningNoticeBO);
						
						warningNoticeBO.setListOfOffences(new ArrayList<OffenceBO>());
						
						numWarningNotice++;
						
						//CREATE WITNESS RECORD AND SAVE/UPDATE PERSON RECORD FR WITNESS
						createWitnesses(roadChkOffOutcome,wNoticeID2,audit);
					}
					
					warningNoticeBO.getListOfOffences().add(new OffenceBO(offenceDO));
				}
				
				//set current offence to previous
				prevOffenceID = newOffenceID;
			}
		
						
			/**Update road check record with compliancy status
			 * if negativeCnt is greater than zero, 'N', otherwise, leave it.
			 */
			if(negativeCnt > 0)
			{	
				//add audit entry
				AuditEntry updAuditRdChk = roadChkDO.getAuditEntry();
				updAuditRdChk.setUpdateUsername(currentUserName);
				updAuditRdChk.setUpdateDTime(Calendar.getInstance().getTime());
				roadChkDO.setAuditEntry(updAuditRdChk);
				
				roadChkDO.setCompliant(YesNo.NO_LABEL);
				daoFactory.getRoadCompliancyDAO().update(roadChkDO);
			}
			
			
			/**Update compliance record with compliancy status and isComplete flag
			 * if negativeCnt is greater than zero, 'N', otherwise, leave it.
			 */
			if(negativeCnt > 0)
			{	
				//add audit entry
				AuditEntry updAuditCompl = complianceDO.getAuditEntry();
				updAuditCompl.setUpdateUsername(currentUserName);
				updAuditCompl.setUpdateDTime(Calendar.getInstance().getTime());
				complianceDO.setAuditEntry(updAuditCompl);
				
				complianceDO.setCompliant(YesNo.NO_LABEL_STR);
				complianceDO.setStatus(this.daoFactory.getRoadCompliancyDAO().load(StatusDO.class, Constants.Status.ROAD_CHECK_COMPLETE));
				daoFactory.getRoadCompliancyDAO().update(complianceDO);
				
			}
			
			
		
		}

		
		//update key: oa12232013
		
		
		return returnListOfDocumentViews;
		
		
}
	
	private PersonDO addORUpdatePersonByTrn(PersonDO person,String currentUser){
		
		PersonDO existPerson = daoFactory.getPersonDAO().findPersonByTRN(person.getTrnNbr());
		if(existPerson==null){
			AuditEntry audit = new AuditEntry();
			audit.setCreateUsername(currentUser);
			audit.setCreateDTime(Calendar.getInstance().getTime());
			person.setAuditEntry(audit);
			daoFactory.getPersonDAO().save(person);
		}else{
			AuditEntry audit = existPerson.getAuditEntry();
			audit.setUpdateUsername(currentUser);
			audit.setUpdateDTime(Calendar.getInstance().getTime());
			//existPerson.setAuditEntry(audit);
			//person = existPerson;
			person.setAuditEntry(audit);
			person.setPersonId(existPerson.getPersonId());
			person.setVersionNbr(existPerson.getVersionNbr());
			daoFactory.getPersonDAO().merge(person);
		}
		
		
		
		person = daoFactory.getPersonDAO().findPersonByTRN(person.getTrnNbr());
		
		return person;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public VehicleCheckResultBO performMVCheck(VehicleCheckResultBO mvCheck, boolean isQuickQuery,RoadLicenceBO roadLicDetails) throws NoRecordFoundException, InvalidFieldException
	{
		/**Check Database First for Vehicle details //I-143826
		 * If details found, copy details from there, and save as motor vehicle check results.
		 * Otherwise, call the MV Service, save retrieved details as motor vehicle check results,
		 * and update the vehicle details
		 */
		
		Vehicle amvsVehicle = new Vehicle();
		VehicleBO vehicleBO = new VehicleBO();
		
		/*
		//Check Db for Vehicle Details
		VehicleCheckResultDO vhChkRsltDO = getLatestMVDetailsForPlateNum(mvCheck.getPlateRegNo());
		
		VehicleCheckResultBO vhChkRsltBO = null;
		*/
		/**
		 * 2015-01-06
		 * Change to always use AMVS WS to get the latest vehicle info
		 */
		
		//System.err.println("Was vehicle found?");
		
		/*if(vhChkRsltDO != null)
		{
			System.err.println("Yes");
			vhChkRsltBO = convertVehChckRsltDOtoBO(getLatestMVDetailsForPlateNum(mvCheck.getPlateRegNo()));
			amvsVehicle = convertVehChkRsltBOtoVehicle(vhChkRsltBO);
			
			
			//Process and Save all other necessary records for MV Check
			try 
			{
				mvCheck = this.processMVCheck(mvCheck,amvsVehicle,vhChkRsltBO.getVehicleId(),isQuickQuery,roadLicDetails);
			}
			catch (ErrorSavingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else
		{*/
			//System.err.println("NO");
		System.err.println("Use AMVS WS to get vehicle details");
			//Call MV Service
			try{
				AMVSWebService amvsWS = new AMVSWebService();
			
				amvsVehicle = amvsWS.getVehicleByPlateNumber(mvCheck.getPlateRegNo());
				
				System.err.println("AMVS WS vehicle details: "+ amvsVehicle);
				
				//Process VehicleBO
				if(amvsVehicle != null)  
				{
					//StringBuffer sbVehOwners = new StringBuffer();
					
					String ownerName = "";
					
					try{
						List<VehOwner> vehOwners = amvsVehicle.getVehOwners().getVehOwner();
						
						VehOwner vehicleOwner = vehOwners.get(0);
						
						NameUtil nameUtil = new NameUtil();
						
						ownerName = nameUtil.getLastNameCapsFirstNameMiddleName
								(vehicleOwner.getFirstName(), vehicleOwner.getLastName(),"");
						
						System.err.println("Owner: "+ownerName);
						
					}catch(Exception ex){
						System.err.println("No owner found ");
					}
										
					
					if(! isQuickQuery)
					{
						
						
						
						
						/*for (VehOwner vehOwner : vehOwners)
						{
							sbVehOwners.append(vehOwner.getTrnNo() + "-" + vehOwner.getTrnBranch() + "-" + 
									vehOwner.getFirstName() + "-" + vehOwner.getLastName()+ ";");
						}
						String strOfVehOwners = sbVehOwners.toString();*/
						
						String strOfVehOwners = ownerName;
						
						//Save Vehicle Record
						vehicleBO = new VehicleBO(null, mvCheck.getPlateRegNo(), amvsVehicle.getVehInfo().getEngineNo(), 
								amvsVehicle.getVehInfo().getChassisNo(), amvsVehicle.getVehInfo().getVehicleColour(), amvsVehicle.getVehInfo().getVehicleMakeDesc(),
								amvsVehicle.getVehInfo().getVehicleModel(), amvsVehicle.getVehInfo().getVehicleTypeDesc(),strOfVehOwners,amvsVehicle.getVehInfo().getVehicleYear(),
								mvCheck.getCurrentUserName());
						
						vehicleBO = saveVehicle(vehicleBO);
						
						
						//Link vehicle created to compliancy
						ComplianceBO complianceBO = new ComplianceBO();
						complianceBO.setVehicle(vehicleBO);
						
						ComplianceDO retrievedCompliance = daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, mvCheck.getComplianceID());
						//updateCompliance(retrievedCompliance,complianceBO);
						updateComplianceVehicle(retrievedCompliance,complianceBO);
					}
					
					//Proceess and Save all other necessary records for MV Check
					mvCheck = this.processMVCheck(mvCheck,amvsVehicle,vehicleBO.getVehicleId(),isQuickQuery,roadLicDetails);
					
				}else
				{
					throw new NoRecordFoundException();
				}
				
				
			
			}catch(FslWebServiceException_Exception fslwe)
			{
				String error = fslwe.getFaultInfo().getErrorCode();
				
				if(error.equals(Constants.FslWebServiceExceptionCodes.NO_REC_FOUND))
				{
					throw new NoRecordFoundException();
				}
				
				fslwe.printStackTrace();
			} catch (ErrorSavingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
		

		
		
		return mvCheck;
	}
	
	
	
	private VehicleCheckResultDO getLatestMVDetailsForPlateNum(String plateRegNo) {

		VehicleCheckResultDO vehDO = null;
		
		return daoFactory.getRoadCompliancyDAO().findLatestVehicleForPlateNo(plateRegNo);
		
	}

	private boolean updateComplianceVehicle(ComplianceDO retrievedCompliance,ComplianceBO complianceBO)
	{
			try{
				retrievedCompliance.setVehicle(daoFactory.getRoadCompliancyDAO().find(VehicleDO.class,complianceBO.getVehicle().getVehicleId()));
				//add audit entry
				AuditEntry audit = retrievedCompliance.getAuditEntry();
				audit.setUpdateUsername(complianceBO.getCurrentUserName());
				audit.setUpdateDTime(Calendar.getInstance().getTime());
				retrievedCompliance.setAuditEntry(audit);
				
				return daoFactory.getRoadCompliancyDAO().updateCompliance(retrievedCompliance);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	private VehicleCheckResultBO processMVCheck(VehicleCheckResultBO mvCheckRequest, Vehicle mvDetails, Integer vehicleID, 
			boolean isQuickQuery,RoadLicenceBO roadLicBO)
			throws InvalidFieldException, ErrorSavingException
	{	
		DateFormat formatter ; 
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date expiryDate = null;
		Date issueDate = null;						
		//boolean errorGettingAddress = false;
		
		AuditEntry audit = null;
		

			//prepare audit entry
			 audit = new AuditEntry();
			 audit = new AuditEntry();
			 audit.setCreateUsername(mvCheckRequest.getCurrentUserName());
			 audit.setCreateDTime(Calendar.getInstance().getTime());
		
			
		
		ComplianceDO complianceDO  = null;
		
		if(! isQuickQuery)
		{
			//Check if valid ComplianceID entered
			 complianceDO = daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, mvCheckRequest.getComplianceID());
				
			 if(complianceDO == null)
			 {throw new InvalidFieldException("Compliance ID Invalid");}
		}
		
		 
		//create Road Check Record
		RoadCheckDO roadCheckDO = new RoadCheckDO(
				daoFactory.getRoadCompliancyDAO().find(CDRoadCheckTypeDO.class, RoadCheckType.MOTOR_VEHICLE) 
				,complianceDO,'Y',mvCheckRequest.getComment(),audit);
		
		Integer roadCheckID = null;
		
		RoadCheckDO savedRoadCheckDO = null;
		
		if(! isQuickQuery)
		{
			roadCheckID = (Integer)daoFactory.getRoadCompliancyDAO().save(roadCheckDO);
			
			savedRoadCheckDO = daoFactory.getRoadCompliancyDAO().find(RoadCheckDO.class,roadCheckID);
		}
		else
		{
			savedRoadCheckDO = roadCheckDO;
		}
		
		
		//Get Road Licence Information...... Call LMIS Road Licence service
		LMISService lmisService = serviceFactory.getLMISService();
		 
		RoadLicenceBO roadLicDetails = roadLicBO; //new RoadLicenceBO(); //I-143826 - There was duplicate work being done here. 
		
//			try{
//				roadLicDetails = lmisService.getRoadLicenceDetails(mvCheckRequest.getPlateRegNo());
//			} catch (RequiredFieldMissingException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			} catch (NoRecordFoundException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			}
			
			//populate ResultDOs and Save
			try{
					if(mvDetails.getVehMvrc() != null)
					{
						if(mvDetails.getVehMvrc().getMvrcExpiryDate() != null )
						{
							expiryDate = formatter.parse(mvDetails.getVehMvrc().getMvrcExpiryDate());
						}
						
						if(mvDetails.getVehMvrc().getMvrcIssueDate() != null)
						{
							issueDate = formatter.parse(mvDetails.getVehMvrc().getMvrcIssueDate());
						}
					}
					
				}
			catch(ParseException pe)
			{
				pe.printStackTrace();
			}
			
			//1.Save MVCheck Results
			VehicleCheckResultDO mvCheckResultDO = new VehicleCheckResultDO(savedRoadCheckDO, mvCheckRequest.getPlateRegNo(), 
					mvDetails.getVehInfo().getEngineNo(),
					mvDetails.getVehInfo().getChassisNo(), mvDetails.getVehInfo().getVehicleColour(), mvDetails.getVehInfo().getVehicleMakeDesc(),
					mvDetails.getVehInfo().getVehicleModel(), mvDetails.getVehInfo().getVehicleTypeDesc(),  mvDetails.getVehMvrc() != null ? mvDetails.getVehMvrc().getMvrcNo():null,  
					expiryDate, new Integer(mvDetails.getVehInfo().getVehicleYear()), 
					roadLicDetails != null? roadLicDetails.getLicenceNo(): null, 
					roadLicDetails != null && roadLicDetails.getLicenceOwners() != null && roadLicDetails.getLicenceOwners().size() >0 ? roadLicDetails.getLicenceOwners()!= null? roadLicDetails.getLicenceOwners().get(0).getFirstName() + " " + roadLicDetails.getLicenceOwners().get(0).getLastName():null :null,
					roadLicDetails != null? roadLicDetails.getStatusDesc() :null, audit,issueDate);
					

			Integer mvCheckResultID = null;
			
			VehicleCheckResultDO savedVehicleChkRslt = null;
			
			if(! isQuickQuery)
			{
				mvCheckResultID = (Integer)daoFactory.getRoadCompliancyDAO().save(mvCheckResultDO);
				
				savedVehicleChkRslt = daoFactory.getRoadCompliancyDAO().find(VehicleCheckResultDO.class,mvCheckResultID);
			}
			else
			{
				savedVehicleChkRslt = mvCheckResultDO;
			}
					
					
			//2.Save Vehicle Owner Information
			List<VehOwner> vehOwners = new ArrayList<VehOwner>();
			if(mvDetails.getVehOwners() != null)
			{
				vehOwners = mvDetails.getVehOwners().getVehOwner();
			}
			
			//VehicleOwnerDO vehicleOwnerDO = new VehicleOwnerDO();
			VehicleCheckResultOwnerDO vehicleCheckResultOwnerDO = null; //new VehicleCheckResultOwnerDO();
			//DLWebService dlService = new DLWebService();
			//TRNWebService trnWebService = new TRNWebService();
			
									
			List<VehicleCheckResultOwnerDO> listOfvehicleChkOwners = new ArrayList<VehicleCheckResultOwnerDO>();
			
			if (vehOwners != null)
			{
				for (VehOwner vehicleOwner : vehOwners) 
				{
					
					String trnNbr = vehicleOwner.getTrnNo();
					String trnBranch = vehicleOwner.getTrnBranch();
					
					/**************
					 * I-143826 - 1. Look in database for owner details first. 
					 * 			  2. If details do not exist, go straight to TRN service, as opposed to going to DL then TRN(which previously existed).
					 **************/
					
					/***
					 * Check if person exists in database
					 */
					AddressDO retrievedPersonAddress = null;
					ParishDO parishDO = null;
					AddressDO serviceAddressDO = null;
					
					try 
					{								
						///////////////////////////////////////////
						retrievedPersonAddress = getPersonAddress(trnNbr,trnBranch);
						
					
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
					//retrievedPersonAddress = null;
					
					if(retrievedPersonAddress != null)
					{
							parishDO = retrievedPersonAddress.getParish();
							
							vehicleCheckResultOwnerDO = new VehicleCheckResultOwnerDO(savedVehicleChkRslt,vehicleOwner.getTrnNo(),vehicleOwner.getTrnBranch(),
								vehicleOwner.getFirstName(), null, vehicleOwner.getLastName(), retrievedPersonAddress,
								null,null, null,audit);
								//personDTO.getAddrPoLocName(),personDTO.getAddrParishDesc(), personDTO.getAddrCountryDesc(),audit);
						
					}else
					{									
						//Check vehicle owner table

						//System.err.println("Check TRN Service");

						//System.err.println("Point L " + Calendar.getInstance().getTime());
				
						//if(!isQuickQuery){
							TrnDTO personDTO = null;
							
							try{
								Integer trnInt = Integer.parseInt(trnNbr);
								
								personDTO = trnService.validateTRN(trnInt, (short)0);
								
								if(personDTO != null){
								
									parishDO = null;
									
									if(StringUtil.isSet(personDTO.getAddrParishCode())){
										parishDO = daoFactory.getRoadCompliancyDAO().find(ParishDO.class, personDTO.getAddrParishCode());
									}
								
									//set Address based on values retrieved from TRN
									serviceAddressDO = new AddressDO(personDTO.getAddrMarkText(), personDTO.getAddrStreetNo(), personDTO.getAddrStreetName(),
											personDTO.getAddrPoLocName(), personDTO.getAddrPoBoxNo(),parishDO);
									
									vehicleCheckResultOwnerDO = new VehicleCheckResultOwnerDO(savedVehicleChkRslt,vehicleOwner.getTrnNo(),vehicleOwner.getTrnBranch(),
											vehicleOwner.getFirstName(), null, vehicleOwner.getLastName(), serviceAddressDO,
											personDTO.getAddrPoLocName(),personDTO.getAddrParishDesc(), personDTO.getAddrCountryDesc(),audit);
								}
								
							}catch (NumberFormatException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								//errorGettingAddress = true;
								
							} catch (InvalidTrnBranchException_Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								//errorGettingAddress = true;
								
							} catch (NotValidTrnTypeException_Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								//errorGettingAddress = true;
								
							} catch (SystemErrorException_Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								//errorGettingAddress = true;
								
							}catch(Exception ex){
								ex.printStackTrace();
							}
								
								
								
						}
						
						if(vehicleCheckResultOwnerDO == null)
						{
						
							vehicleCheckResultOwnerDO = new VehicleCheckResultOwnerDO(savedVehicleChkRslt,vehicleOwner.getTrnNo(),vehicleOwner.getTrnBranch(),
									vehicleOwner.getFirstName(), null, vehicleOwner.getLastName(), 
									null, null, null, null,audit);
						}
					
						listOfvehicleChkOwners.add(vehicleCheckResultOwnerDO);
				
				
						if(! isQuickQuery)
						{
							daoFactory.getRoadCompliancyDAO().save(vehicleCheckResultOwnerDO);
						}

					} 
						
					
				}
				
				if(! isQuickQuery)
				{
					//save current owner information and link to the vehicle. this will be used as the most recent user and placed on warning notice
					saveCurrentOwners(listOfvehicleChkOwners, vehicleID, mvCheckRequest.getCurrentUserName());
				}
				
			
			
			
			//3.Save Insurance Information
			VehInsurance mvInsuranceDetails = mvDetails.getVehInsurance();
			
			VehicleInsuranceDO vehicleInsDO = null;
			
			if(mvInsuranceDetails != null)
			{
				vehicleInsDO = new VehicleInsuranceDO(savedVehicleChkRslt, mvInsuranceDetails.getPolicyNo(),
						mvInsuranceDetails.getCompName(),DateUtils.stringToDate(mvInsuranceDetails.getIssueDate()), 
						DateUtils.stringToDate(mvInsuranceDetails.getExpDate()), audit);
				
				if(! isQuickQuery)
				{
					daoFactory.getRoadCompliancyDAO().save(vehicleInsDO);
				}
			}
			
			
			//4.Save Fitness Information
			VehFitness mvFitnessDetails = mvDetails.getVehFitness();
			
			VehicleFitnessDO  vehicleFitnessDO = null;
			
			if(mvFitnessDetails != null)
			{
				vehicleFitnessDO = new VehicleFitnessDO(savedVehicleChkRslt, mvFitnessDetails.getFitnessNo(), 
						mvFitnessDetails.getExamDepot(), DateUtils.stringToDate(mvFitnessDetails.getIssueDate()), 
						DateUtils.stringToDate(mvFitnessDetails.getExpDate()),audit);
				
				if(! isQuickQuery)
				{
					daoFactory.getRoadCompliancyDAO().save(vehicleFitnessDO);
				}
			}
				
			
			//do event audit
			doMVCheckEventAudit(isQuickQuery, mvCheckRequest, complianceDO, audit);
			
			
			VehicleCheckResultBO mvCheckResult = null;
			
			//Return Results to User
			if(! isQuickQuery)
			{
				mvCheckResult = convertVehChckRsltDOtoBO(mvCheckResultDO);
			}
			else
			{

				mvCheckResult = convertVehChckRsltDOtoBO(mvCheckResultDO,vehicleFitnessDO,vehicleInsDO,listOfvehicleChkOwners);
				
			}	
			
			mvCheckResult.setTypeCode(mvDetails.getVehInfo().getPlateTypeCode());
			
			return mvCheckResult;
		
	}
	


	
	
	public void doMVCheckEventAudit(boolean isQuickQuery, VehicleCheckResultBO mvCheckRequest,ComplianceDO complianceDO, AuditEntry audit) {
		EventAuditDO auditDO = new EventAuditDO();
		
		if(! isQuickQuery)//commented out on Dec 30, 2014. Marion asked that quick queries be audited. 
		{
			auditDO.setEvent(daoFactory.getRoadCompliancyDAO().find(CDEventDO.class,EventCode.QUERY_VEHICLE_REGISTRATION_DETAILS));
			
		}
		else{
			auditDO.setEvent(daoFactory.getRoadCompliancyDAO().find(CDEventDO.class,EventCode.QUICK_QUERY));
		}		
		auditDO.setRefType1(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.PLATE_NUMBER));
		auditDO.setRefValue1(mvCheckRequest.getPlateRegNo());
		auditDO.setRefType1Code(EventRefTypeCode.PLATE_NUMBER);
		
		auditDO.setRefType2(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.OPERATION_NAME));
		
		if(! isQuickQuery)//commented out on Dec 30, 2014. Marion asked that quick queries be audited. 
		{
			auditDO.setRefValue2(complianceDO.getRoadOperation() != null? 
						StringUtil.isSet(complianceDO.getRoadOperation().getOperationName())? complianceDO.getRoadOperation().getOperationName(): "Unscheduled Operation": 
						"Unscheduled Operation");
			auditDO.setRefType2Code(EventRefTypeCode.OPERATION_NAME);
		}
		auditDO.setAuditEntry(audit);
		
		daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);

		
	}

	private void saveCurrentOwners(
			List<VehicleCheckResultOwnerDO> listOfVehCheckRsltOwnerDOs,
			Integer vehicleID, String currentUser) {

		boolean success = daoFactory.getRoadCompliancyDAO().deletePreviousOwners(vehicleID);
		
		if(success)
		{
			
			//add audit entry
			AuditEntry audit = new AuditEntry();
			audit.setCreateUsername(currentUser);
			audit.setCreateDTime(Calendar.getInstance().getTime());
			
			for (VehicleCheckResultOwnerDO vehicleCheckResultOwnerDO : listOfVehCheckRsltOwnerDOs) 
			{
					VehicleOwnerDO vehicleOwnerDO = new VehicleOwnerDO(daoFactory.getRoadCompliancyDAO().find(VehicleDO.class, vehicleID),
					vehicleCheckResultOwnerDO.getTrnNbr(),vehicleCheckResultOwnerDO.getTrnBranch(),
					vehicleCheckResultOwnerDO.getFirstName(), vehicleCheckResultOwnerDO.getMidName(), vehicleCheckResultOwnerDO.getLastName(), 
					(vehicleCheckResultOwnerDO.getAddress()!= null? new AddressDO(vehicleCheckResultOwnerDO.getAddress().getMarkText(), vehicleCheckResultOwnerDO.getAddress().getStreetNo(),
							vehicleCheckResultOwnerDO.getAddress().getStreetName(), vehicleCheckResultOwnerDO.getAddress().getPoLocationName(),
							vehicleCheckResultOwnerDO.getAddress().getPoBoxNo(), vehicleCheckResultOwnerDO.getAddress().getParish()):null),
							vehicleCheckResultOwnerDO.getCity(), vehicleCheckResultOwnerDO.getParishDesc(), vehicleCheckResultOwnerDO.getCountry(),
							audit);
			
				
					daoFactory.getRoadCompliancyDAO().save(vehicleOwnerDO);
			}
			
		}
		
//		if(retrievedVehicleOwnerDO != null)
//		{
//			/*Allow Update of all other fields except ones forming unique constraint i.e. plate #, engine #, chassis #, color aand year*/	
//			retrievedVehicleOwnerDO.setAddress(new AddressDO(vehicleCheckResultOwnerDO.getAddress().getMarkText(), vehicleCheckResultOwnerDO.getAddress().getStreetNo(),
//					vehicleCheckResultOwnerDO.getAddress().getStreetName(), vehicleCheckResultOwnerDO.getAddress().getPoLocationName(),
//					vehicleCheckResultOwnerDO.getAddress().getPoBoxNo(), vehicleCheckResultOwnerDO.getAddress().getParish()));
//			
//			retrievedVehicleOwnerDO.setCity(vehicleCheckResultOwnerDO.getCity());
//			retrievedVehicleOwnerDO.setParishDesc(vehicleCheckResultOwnerDO.getParishDesc());
//			retrievedVehicleOwnerDO.setCountry(vehicleCheckResultOwnerDO.getCountry());
//			
//			//add audit entry
//			AuditEntry audit = retrievedVehicleOwnerDO.getAuditEntry();
//			audit.setUpdateUsername(vehicleCheckResultOwnerDO.getAuditEntry().getCreateUsername());
//			audit.setUpdateDTime(Calendar.getInstance().getTime());
//			retrievedVehicleOwnerDO.setAuditEntry(audit);
//			
//			daoFactory.getRoadCompliancyDAO().updateVehicleOwner(retrievedVehicleOwnerDO);
//			
//		}
		
			
	}



	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public DLCheckResultBO processDLCheck(DLCheckResultBO dlCheckRequest, DriverLicenceDetails dlDetails) throws InvalidFieldException
	{	DateFormat formatter ; 
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date expiryDate = null;
		Date issueDate = null;
		
		//prepare audit entry
		 AuditEntry audit = new AuditEntry();
		 audit = new AuditEntry();
		 audit.setCreateUsername(dlCheckRequest.getCurrentUserName());
		 audit.setCreateDTime(Calendar.getInstance().getTime());
			
		
		//Check if valid ComplianceID entered
		 ComplianceDO complianceDO = daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, dlCheckRequest.getComplianceID());
			
		 if(complianceDO == null)
		 {throw new InvalidFieldException("Compliance ID Invalid");}
		 
		 
		//create Road Check Record
		RoadCheckDO roadCheckDO = new RoadCheckDO(
				daoFactory.getRoadCompliancyDAO().find(CDRoadCheckTypeDO.class, RoadCheckType.DRIVERS_LICENCE) 
				,complianceDO,
				'Y',dlCheckRequest.getComment(),audit);
		
		Integer roadCheckID = (Integer)daoFactory.getRoadCompliancyDAO().save(roadCheckDO);
		
		RoadCheckDO savedRoadCheckDO = daoFactory.getRoadCompliancyDAO().find(RoadCheckDO.class,roadCheckID);

		
		//populate ResultDOs and Save
		
		//1.Save DLCheck Results
		Blob photo = null;
		if(dlDetails.getPhotograph() != null)
		{
			photo = Hibernate.createBlob(dlDetails.getPhotograph());
		}
		
		try{
			expiryDate = formatter.parse(dlDetails.getExpiryDate());
			issueDate = formatter.parse(dlDetails.getIssueDate());
		}
		catch(ParseException pe)
		{
			pe.printStackTrace();
		}
		
		DLCheckResultDO dlCheckResultDO = new DLCheckResultDO(savedRoadCheckDO, dlCheckRequest.getDlNo(), dlDetails.getFirstName(),
				dlDetails.getMiddleName(), dlDetails.getLastName(), dlDetails.getDlClassDesc(), photo, issueDate, expiryDate, audit);
				
		Integer dlChkRsltID = (Integer)daoFactory.getRoadCompliancyDAO().save(dlCheckResultDO);
		
		
		//do event audit
		EventAuditDO auditDO = new EventAuditDO();
		auditDO.setEvent(daoFactory.getRoadCompliancyDAO().find(CDEventDO.class,EventCode.QUERY_DRIVERS_LICENCE));
	
		auditDO.setRefType1(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.DRIVERS_LICENCE_NUMBER));
		auditDO.setRefValue1(dlCheckRequest.getDlNo());
		auditDO.setRefType1Code(EventRefTypeCode.DRIVERS_LICENCE_NUMBER);
		    
		
		auditDO.setRefType2(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.OPERATION_NAME));
		auditDO.setRefValue2(complianceDO.getRoadOperation() != null? 
					StringUtil.isSet(complianceDO.getRoadOperation().getOperationName())? complianceDO.getRoadOperation().getOperationName(): "Unscheduled Operation": 
					"Unscheduled Operation");
		auditDO.setRefType2Code(EventRefTypeCode.OPERATION_NAME);
		    
		auditDO.setAuditEntry(audit);
		
		daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);
		
		
		//Return Results to User
		DLCheckResultBO dlCheckResult = new DLCheckResultBO(savedRoadCheckDO, dlCheckRequest.getDlNo(), dlDetails.getFirstName(),
				dlDetails.getMiddleName(), dlDetails.getLastName(), dlDetails.getDlClassDesc(), photo,  issueDate,expiryDate, dlDetails.getNationality(),
				dlDetails.getCollectorateFirstIssued(), dlDetails.getDateFirstIssued(), dlDetails.getDateOfBirth(), dlDetails.getGender(), dlDetails.getAddress(), dlDetails.getVehicleLicencedToDrive(), dlDetails.getTrnNo());
	
		return dlCheckResult;
		
	}
	
	
	
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public CitationCheckResultBO processCitationHistoryCheck(CitationCheckResultBO citationCheck) throws InvalidFieldException
	{
		//prepare audit entry
		 AuditEntry audit = new AuditEntry();
		 audit = new AuditEntry();
		 audit.setCreateUsername(citationCheck.getCurrentUserName());
		 audit.setCreateDTime(Calendar.getInstance().getTime());
			
		 
		 //Check if valid ComplianceID entered
		 ComplianceDO complianceDO = daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, citationCheck.getComplianceID());
			
		 if(complianceDO == null)
		 {throw new InvalidFieldException("Compliance ID Invalid");}
		 
			
		//create Road Check Record
//		RoadCheckDO roadCheckDO = new RoadCheckDO(
//				daoFactory.getRoadCompliancyDAO().find(CDRoadCheckTypeDO.class, RoadCheckType.ROAD_LICENCE) 
//				,complianceDO,
//				'Y',citationCheck.getComment(),audit);
		 
		RoadCheckDO roadCheckDO = new RoadCheckDO(
					daoFactory.getRoadCompliancyDAO().find(CDRoadCheckTypeDO.class, RoadCheckType.CITATION) 
					,complianceDO,
					'Y',citationCheck.getComment(),audit);
		
		Integer roadCheckID = (Integer)daoFactory.getRoadCompliancyDAO().save(roadCheckDO);
		
		RoadCheckDO savedRoadCheckDO = daoFactory.getRoadCompliancyDAO().find(RoadCheckDO.class,roadCheckID);

		
		//populate ResultDOs and Save
		CitationCheckDO citationCheckDO = new CitationCheckDO(savedRoadCheckDO, citationCheck.getTrnNbr(), citationCheck.getDlNo(),
				citationCheck.getRegPlateNo(), citationCheck.getIncludeTTMSResults(),audit);		
		
		//1.Save General Results
		Integer citationChkRsltID = (Integer)daoFactory.getRoadCompliancyDAO().save(citationCheckDO);
		CitationCheckDO savedCitationCheckDO = daoFactory.getRoadCompliancyDAO().find(CitationCheckDO.class, citationChkRsltID);
		
		//2.Save Each Traffic Ticket
//		List<TrafficTicket> trafficTickets = citationCheck.getTrafficTickets();
//		
//		if(trafficTickets != null  && !trafficTickets.isEmpty())
//		{
//			for (TrafficTicket trafficTicket : trafficTickets) {
//		
//				TicketCheckResultDO ticketChkRsltDO =  
//					new TicketCheckResultDO(savedCitationCheckDO, trafficTicket.getTicketNo(),
//							DateUtils.stringToDate(trafficTicket.getIssueDate()),trafficTicket.getOffenceDesc(), 
//							trafficTicket.getStatusCode(), audit);
//			
//				daoFactory.getRoadCompliancyDAO().save(ticketChkRsltDO);
//			}
//		}
		
		
		//3. Save Each Offence combined vehicle and person
		List<CitationOffenceBO> romsOffences = new ArrayList<CitationOffenceBO>();
		
		if(citationCheck.getCitationOffences() != null){
			romsOffences.addAll(citationCheck.getCitationOffences());
		}
		romsOffences.addAll(citationCheck.getCitationOffencesVehicle());
		
		if(romsOffences != null && !romsOffences.isEmpty())
		{		
			for (CitationOffenceBO citationOffenceBO : romsOffences) {
				
				RoadCheckOffenceDO roadCheckOffence = daoFactory.getRoadCompliancyDAO().find(RoadCheckOffenceDO.class, citationOffenceBO.getRoadCheckOffenceID());
				
				RoadLicenceBO roadLicBO = citationOffenceBO.getRoadLicDetails();
						
				OffenceCheckResultDO offenceChkRsltDO = 
						new OffenceCheckResultDO(savedCitationCheckDO,roadCheckOffence,
								roadLicBO != null? roadLicBO.getLicenceNo(): null,
								(roadLicBO != null && roadLicBO.getLicenceOwners() != null && roadLicBO.getLicenceOwners().size() > 0)? roadLicBO.getLicenceOwners().get(0).getFirstName() + " " + roadLicBO.getLicenceOwners().get(0).getLastName(): null, 
								roadLicBO != null? roadLicBO.getStatusDesc(): null,
							audit); 
				
				daoFactory.getRoadCompliancyDAO().save(offenceChkRsltDO);
			}
		}
		
		
		//do event audit
		EventAuditDO auditDO = new EventAuditDO();
		StringBuffer comment = new StringBuffer();
		
		auditDO.setEvent(daoFactory.getRoadCompliancyDAO().find(CDEventDO.class,EventCode.LOOKUP_CITATION_HISTORY));
		
		auditDO.setRefType1(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.TRN));
		auditDO.setRefValue1(citationCheck.getTrnNbr());
			
		auditDO.setRefType2(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.PLATE_NUMBER));
		auditDO.setRefValue2(citationCheck.getRegPlateNo());
		
		
		if(StringUtil.isSet(citationCheck.getDlNo()))
		{
			comment.append("DLN : " + citationCheck.getDlNo());
		}
		
		comment.append(StringUtil.appendSemiColon(comment.toString(), "Operation Name: "));
		comment.append(complianceDO.getRoadOperation() != null? 
					StringUtil.isSet(complianceDO.getRoadOperation().getOperationName())? complianceDO.getRoadOperation().getOperationName(): "Unscheduled Operation": 
					"Unscheduled Operation");
		
		
		auditDO.setComment(comment.toString());
		
		auditDO.setAuditEntry(audit);

		    
		daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);
		
		//Return Results to User
		citationCheck.setCitationCheckId(citationChkRsltID);
		
		return citationCheck;
		
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public CitationCheckResultBO getCitationCheck(Integer complianceId) throws InvalidFieldException
	{

		CitationCheckResultBO citationCheck = new CitationCheckResultBO();
		
		CitationCheckDO citationCheckDO = daoFactory.getRoadCompliancyDAO().getCitationCheckDO(complianceId);
		
		if(citationCheckDO == null)
			return null;
		
		citationCheck.setCitationCheckId(citationCheckDO.getCitationCheckId());
		
		citationCheck.setComplianceID(citationCheckDO.getRoadCheck().getCompliance().getComplianceId());
		
		citationCheck.setCurrentUserName(citationCheckDO.getAuditEntry().getCreateUsername());
		
		citationCheck.setDlNo(citationCheckDO.getDlNo());
		
		citationCheck.setRegPlateNo(citationCheckDO.getRegPlateNo());
		
		citationCheck.setRoadCheckBO(new RoadCheckBO(citationCheckDO.getRoadCheck()));
		
		citationCheck.setTrnNbr(citationCheckDO.getTrnNbr());
		
		citationCheck.setIncludeTTMSResults(citationCheckDO.getIncludeTTMSResults());
		 
		
		
		return citationCheck;
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public RoadLicCheckResultBO processRoadLicenceCheck(RoadLicCheckResultBO roadLicCheckRequest, RoadLicenceBO roadLicDetails, boolean isQuickQuery, String plateRegNo) throws InvalidFieldException
	{
		
		ComplianceDO complianceDO = null;
		
		 AuditEntry audit = null;
		 
		//prepare audit entry
		 audit = new AuditEntry();

		 audit.setCreateUsername(roadLicCheckRequest.getCurrentUserName());
		 audit.setCreateDTime(Calendar.getInstance().getTime());
		 
		
		if(! isQuickQuery)
		{
				
			
			//Check if valid ComplianceID entered
			 complianceDO = daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, roadLicCheckRequest.getComplianceBO().getComplianceId());
				
			 if(complianceDO == null)
			 {throw new InvalidFieldException("Compliance ID Invalid");}
		}
		 
		 
		//create Road Check Record
		RoadCheckDO roadCheckDO = new RoadCheckDO(
				daoFactory.getRoadCompliancyDAO().find(CDRoadCheckTypeDO.class, RoadCheckType.ROAD_LICENCE) 
				,complianceDO,'Y',roadLicCheckRequest.getComment(),audit);
		
		RoadCheckDO savedRoadCheckDO = null;
		
		Integer roadCheckID = null;
		
		if(! isQuickQuery)
		{
			roadCheckID = (Integer)daoFactory.getRoadCompliancyDAO().save(roadCheckDO);
		
			savedRoadCheckDO = daoFactory.getRoadCompliancyDAO().find(RoadCheckDO.class,roadCheckID);
		}
		else
		{
			savedRoadCheckDO = roadCheckDO;
		}

		
		//populate ResultDOs and Save
		
		//1.Save General Results
//		RoadLicCheckResultDO roadLicCheckResultDO = new RoadLicCheckResultDO(savedRoadCheckDO, roadLicDetails.getLicenceNo(),
//				roadLicDetails.getExpiryDate(), roadLicDetails.getLicType(), roadLicDetails.getRouteDesc(),roadLicDetails.getVehMakeDesc(),
//				roadLicDetails.getModelDesc(),roadLicDetails.getLicPlate(), roadLicDetails.getStatusDesc(), audit);				
			
		
		RoadLicCheckResultDO roadLicCheckResultDO = new RoadLicCheckResultDO(savedRoadCheckDO, roadLicDetails.getLicenceNo(),
				roadLicDetails.getExpiryDate(), roadLicDetails.getIssueDate(),roadLicDetails.getLicType(), roadLicDetails.getRouteDesc(),roadLicDetails.getVehMakeDesc(),
				roadLicDetails.getModelDesc(),roadLicDetails.getLicPlate(), roadLicDetails.getStatusDesc(),
				roadLicDetails.getFitnessNumber(), roadLicDetails.getDepot(), roadLicDetails.getFitnessIssueDate(), roadLicDetails.getFitnessExpDate(),
				roadLicDetails.getInsuranceComp(),roadLicDetails.getInsuranceIssueDate(),roadLicDetails.getInsuranceExpDate(),roadLicDetails.getControlNo(),
				audit,roadLicDetails.getLmisApplicationBO());		

		
		Integer roadLicChkRsltID = new Integer(0);
		
		if(! isQuickQuery)
		{
			roadLicChkRsltID = (Integer)daoFactory.getRoadCompliancyDAO().save(roadLicCheckResultDO);
		}
		
		
		//2.Save Owners
		List<RoadLicenceOwnerBO> owners = new ArrayList<RoadLicenceOwnerBO>();
		
		if(roadLicDetails != null)
		{
			owners = roadLicDetails.getLicenceOwners();
			LicenceOwnerDO licenceOwnerDO = new LicenceOwnerDO();
			
			if (owners != null && ! isQuickQuery)
			{
				for (RoadLicenceOwnerBO roadLicenceOwnerBO : owners) {
					
//					licenceOwnerDO = new LicenceOwnerDO(daoFactory.getRoadCompliancyDAO().find(RoadLicCheckResultDO.class,roadLicChkRsltID),
//							roadLicenceOwnerBO.getFirstName(), null , roadLicenceOwnerBO.getLastName(), audit);
//					
		
					licenceOwnerDO = new LicenceOwnerDO(daoFactory.getRoadCompliancyDAO().find(RoadLicCheckResultDO.class,roadLicChkRsltID),
							roadLicenceOwnerBO, audit);
					daoFactory.getRoadCompliancyDAO().save(licenceOwnerDO);
				}
				
				
			}
		}
		
		//RFC-ROMS-0008
		//4.Save license driver and conductor details
		//TODO
		if(roadLicDetails != null)
		{
			List<RoadLicenceDriverConductorBO> assignedDriverConductor = roadLicDetails.getAssignedDriverConductor(); 
			
			LicenceDriverConductorDO licence_driver_cond_DO = new LicenceDriverConductorDO();
			
			if(assignedDriverConductor != null && ! isQuickQuery)
			{
				for (RoadLicenceDriverConductorBO roadLicenceDriverCondBO : assignedDriverConductor) 
				{
					
					licence_driver_cond_DO = new LicenceDriverConductorDO(daoFactory.getRoadCompliancyDAO().find(RoadLicCheckResultDO.class,roadLicChkRsltID), 
							roadLicenceDriverCondBO.getFirstName(), null, roadLicenceDriverCondBO.getLastName(), roadLicenceDriverCondBO.getBadgeNumber(),
							roadLicenceDriverCondBO.getBadgeType(), audit);
					
					daoFactory.getRoadCompliancyDAO().save(licence_driver_cond_DO);
				}
			}
		
		}
		
		
		
		//Save Event Audit
				
		EventAuditDO auditDO = new EventAuditDO();
		
	
		
		if(!isQuickQuery)
		{
			auditDO.setComment("Road Licence Number : " +roadLicCheckRequest.getLicenceNo());
		}
		

		auditDO.setAuditEntry(audit);
		
		//do event audit
		if(!isQuickQuery)
		{			
			auditDO.setEvent(daoFactory.getRoadCompliancyDAO().find(CDEventDO.class,EventCode.VERIFY_ROAD_LICENCE));			
		}
		else
		{
			auditDO.setEvent(daoFactory.getRoadCompliancyDAO().find(CDEventDO.class,EventCode.QUICK_QUERY));
			
			auditDO.setRefType2(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.PLATE_NUMBER));
			auditDO.setRefValue2(plateRegNo);
			auditDO.setRefType2Code(EventRefTypeCode.PLATE_NUMBER);
			
			daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);
		}
		
		
		//ROMS1.0-177 - there is no longer a road licence check
		//daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO); 
		
		//Return Results to User
		//RoadLicCheckResultBO roadLicCheckResult = new RoadLicCheckResultBO(roadLicCheckResultDO);
		RoadLicCheckResultBO roadLicCheckResult = 
				new RoadLicCheckResultBO(roadLicCheckResultDO, roadLicDetails);
		roadLicCheckResult.setRoadLicenceOwners(owners);
		roadLicCheckResult.setStatusCode(roadLicDetails.getStatusCode());
		
		return roadLicCheckResult;
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public BadgeCheckResultBO processBadgeCheck(BadgeCheckResultBO badgeCheckRequest,BadgeBO badgeDetails) throws InvalidFieldException
	{
		//prepare audit entry
		 AuditEntry audit = new AuditEntry();
		 audit = new AuditEntry();
		 audit.setCreateUsername(badgeCheckRequest.getCurrentUserName());
		 audit.setCreateDTime(Calendar.getInstance().getTime());
			
		 
		//Check if valid ComplianceID entered
		 ComplianceDO complianceDO = daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, badgeCheckRequest.getComplianceID());
			
		 if(complianceDO == null)
		 {throw new InvalidFieldException("Compliance ID Invalid");}
		 
			
		//create Road Check Record
		RoadCheckDO roadCheckDO = new RoadCheckDO(
				daoFactory.getRoadCompliancyDAO().find(CDRoadCheckTypeDO.class, RoadCheckType.BADGE) 
				,complianceDO,'Y',badgeCheckRequest.getComment(),audit);
		
		Integer roadCheckID = (Integer)daoFactory.getRoadCompliancyDAO().save(roadCheckDO);
		
		RoadCheckDO savedRoadCheckDO = daoFactory.getRoadCompliancyDAO().find(RoadCheckDO.class,roadCheckID);
		
		//populate ResultDO
		BadgeCheckResultDO badgeCheckResultDO = new BadgeCheckResultDO(savedRoadCheckDO, badgeCheckRequest.getBadgeNumber(), 
				badgeDetails.getBadgeDesc(),badgeDetails.getFirstName(), badgeDetails.getMidName(), badgeDetails.getLastName(),
				badgeDetails.getBiometricsID(), badgeDetails.getIssueDate(),badgeDetails.getExpiryDate(), audit);
	
		
		//Save ResultDO
		daoFactory.getRoadCompliancyDAO().save(badgeCheckResultDO);
		
		
		badgeSearchEventAudit(complianceDO,badgeCheckRequest,audit);
		
		//Return Results to User
		BadgeCheckResultBO  badgeCheckResult = new BadgeCheckResultBO(badgeCheckResultDO,badgeDetails.getPhotoImg());
		return badgeCheckResult;
	}
	
	
	
	

	@Override
	public PersonBO savePerson(PersonBO person)
	{
		
		PersonDO retrievedPerson = new PersonDO();
		retrievedPerson = personExists(person);
		
		
		if(retrievedPerson != null)
		{
			updatePerson(retrievedPerson,person);
			person.setPersonId(retrievedPerson.getPersonId());
		}else
		{
			Integer newPersonID = addPerson(person);
			
			if (newPersonID != null)
			{	
			    person.setPersonId(newPersonID);
			}
			
		}
		
		return person;
		
	}
	
	
	@Override
	public VehicleBO saveVehicle(VehicleBO vehicle)
	{
		VehicleDO retrievedVehicle = new VehicleDO();
		retrievedVehicle = vehicleExists(vehicle);
		
		
		if(retrievedVehicle != null)
		{
			updateVehicle(retrievedVehicle,vehicle);
			vehicle.setVehicleId(retrievedVehicle.getVehicleId());
		}else
		{
			Integer newVehicleID = addVehicle(vehicle);
			
			if (newVehicleID != null)
			{	
				vehicle.setVehicleId(newVehicleID);
			}
			
		}
		
		return vehicle;
		
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public ComplianceBO saveCompliance(ComplianceBO complianceDetails) throws ErrorSavingException, InvalidFieldException
	{
		Integer resultComplianceID = null;
		
		if(complianceDetails.getComplianceId() != null && complianceDetails.getComplianceId() > 0)
		{
			//get existing details and update
			ComplianceDO retrievedComplianceDO = complianceExists(complianceDetails);
			
			if(retrievedComplianceDO == null)
			{ //invalid compliance ID
				throw new InvalidFieldException("Compliance Record Invalid");
			}else
			{		
				resultComplianceID = retrievedComplianceDO.getComplianceId();
				
				updateCompliance(retrievedComplianceDO,complianceDetails);
			}
			
		}else
		{
			//create compliance record and all other necessary records within transaction
			resultComplianceID = addCompliance(complianceDetails);
		}
		
		//retrieve Updated one and return to user
		ComplianceDO compDO = (daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, resultComplianceID));
		ComplianceBO resultBO = new ComplianceBO(compDO);
		return resultBO;
		
	}
	

	
	@Override
	public List<CitationOffenceBO> getCitationOffence(String trn, String dln,String licencePlate, boolean isHandheld)
	{
		List<CitationOffenceBO> listOfCitations = new ArrayList<CitationOffenceBO>();
		
		listOfCitations = daoFactory.getRoadCompliancyDAO().findCitationOffences(trn, dln,licencePlate, isHandheld);
		
		//Call LMIS Road Licence service
//		LMISService lmisService = serviceFactory.getLMISService();
//		RoadLicenceBO roadLicDetails= null;
//		String prevPlateRegNo = null;
//		List<RoadLicenceBO> roadLicDetailList = new ArrayList<RoadLicenceBO>();
		
//		for (CitationOffenceBO citationOffenceBO : listOfCitations) 
//		{
//			
//			try 
//			{
//				
//				if(roadLicDetails == null && prevPlateRegNo == null)
//				{
//					roadLicDetails = lmisService.getRoadLicenceDetails(citationOffenceBO.getPlateRegNo());
//					
//					roadLicDetailList.add(roadLicDetails);
//				}
//				else if(prevPlateRegNo != null && citationOffenceBO.getPlateRegNo() != null && !prevPlateRegNo.equalsIgnoreCase(citationOffenceBO.getPlateRegNo()))
//				{
//					boolean roadLicFoundInList = false;
//					
//					for(RoadLicenceBO roadLicBO : roadLicDetailList)
//					{
//						
//						if(roadLicBO.getLicPlate()!=null&&roadLicBO.getLicPlate().equalsIgnoreCase(citationOffenceBO.getPlateRegNo()))
//						{
//							roadLicFoundInList = true;
//							roadLicDetails = roadLicBO;
//							
//							break;
//						}
//					}
//					
//					//only call LMIS Service is the road licience has not been previously retrieved.
//					if(! roadLicFoundInList)
//					{
//						roadLicDetails = lmisService.getRoadLicenceDetails(citationOffenceBO.getPlateRegNo());
//						
//						roadLicDetailList.add(roadLicDetails);
//					}
//				}
//				
//				
//				
//				
//				citationOffenceBO.setRoadLicDetails(roadLicDetails.getLicenceNo() != null ? roadLicDetails : null);
//				//System.err.println("Setting road licence details " + roadLicDetails.getLicenceNo());
//				
//			} 
//			catch (RequiredFieldMissingException e) 
//			{
//				//if road licence not found create a empty one in roadLicDetailList
//				RoadLicenceBO roadLicenceBO = new RoadLicenceBO();
//				roadLicenceBO.setLicPlate(citationOffenceBO.getPlateRegNo());
//				roadLicDetailList.add(roadLicenceBO);
//				roadLicDetails = roadLicenceBO;
//				prevPlateRegNo = citationOffenceBO.getPlateRegNo();
//				e.printStackTrace();
//				continue;
//			}
//			catch (NoRecordFoundException e) 
//			{
//				//if road licence not found create a empty one in roadLicDetailList
//				RoadLicenceBO roadLicenceBO = new RoadLicenceBO();
//				roadLicenceBO.setLicPlate(citationOffenceBO.getPlateRegNo());
//				roadLicDetailList.add(roadLicenceBO);
//				roadLicDetails = roadLicenceBO;
//				prevPlateRegNo = citationOffenceBO.getPlateRegNo();
//				e.printStackTrace();
//				continue;
//			}
//			
//			prevPlateRegNo = citationOffenceBO.getPlateRegNo();
//			
//		}

		
		
		
		return listOfCitations;
	}
	
	

	/******************************************************************************************
	* Supporting Methods
	*/
	/****************************************************************************************/
	private PersonDO personExists(PersonBO person) 
	{
		String trn = person.getTrnNbr();
		
		if(StringUtils.isEmpty(trn))
			return null;
		
		PersonCriteriaBO personCriteria = new PersonCriteriaBO();
		personCriteria.setTrnNbr(trn);
		
		
		List<PersonDO> retrievedPersons = daoFactory.getRoadCompliancyDAO().findPersonByCriteria(personCriteria);
		
		if(retrievedPersons.isEmpty())
		{
			return null;
		}else
		{
			return retrievedPersons.get(0);
		}
		
	}
	
	private Integer addPerson(PersonBO person)
	{
		ParishDO parishDO = new ParishDO();
		PersonDO personDO = new PersonDO();
		if(person.getParishCode()!=null){
			parishDO = daoFactory.getRoadCompliancyDAO().find(ParishDO.class, person.getParishCode());
			if(parishDO!=null){
				personDO = person.generatePersonDO(parishDO);
			}
			else{
				return null;
			}
		}else{
			return null;
		}
		
		//add audit entry
		AuditEntry audit = new AuditEntry();
		audit.setCreateUsername(person.getCurrentUserName());
		audit.setCreateDTime(Calendar.getInstance().getTime());
		personDO.setAuditEntry(audit);
		
		Integer personID = (Integer) daoFactory.getRoadCompliancyDAO().savePerson(personDO);
		return personID;
	}
	
	
	public boolean updatePerson(PersonDO retrievedPersonDO,PersonBO personBO)
	{

		/*Allow Update of all other fields except trn number*/
		retrievedPersonDO.setFirstName(personBO.getFirstName());
		retrievedPersonDO.setFirstName(personBO.getFirstName());
		retrievedPersonDO.setLastName(personBO.getLastName());
		retrievedPersonDO.setMiddleName(personBO.getMiddleName());
		if(retrievedPersonDO.getAddress() == null)
		{
			retrievedPersonDO.setAddress(new AddressDO());
		}
		retrievedPersonDO.getAddress().setMarkText(personBO.getMarkText());
		retrievedPersonDO.getAddress().setPoBoxNo(personBO.getPoBoxNo());
		retrievedPersonDO.getAddress().setPoLocationName(personBO.getPoLocationName());
		retrievedPersonDO.getAddress().setStreetName(personBO.getStreetName());
		retrievedPersonDO.getAddress().setStreetNo(personBO.getStreetNo());
		retrievedPersonDO.setTelephoneCell(personBO.getTelephoneCell());
		retrievedPersonDO.setTelephoneHome(personBO.getTelephoneHome());
		retrievedPersonDO.setTelephoneWork(personBO.getTelephoneWork());
		
		ParishDO parishDO = null;
		try{

			parishDO = daoFactory.getParishDAO().find(ParishDO.class, personBO.getParishCode());
			
		}catch (Exception e) {
			logger.info("Person parish is null");
		}
		
		retrievedPersonDO.getAddress().setParish(parishDO);
		
		
		//add audit entry
		AuditEntry audit = retrievedPersonDO.getAuditEntry();
		audit.setUpdateUsername(personBO.getCurrentUserName());
		audit.setUpdateDTime(Calendar.getInstance().getTime());
		retrievedPersonDO.setAuditEntry(audit);
		
		return daoFactory.getRoadCompliancyDAO().updatePerson(retrievedPersonDO);
		
	}
	/*************************************************************************************************/
	
	
	public VehicleDO vehicleExists(VehicleBO vehicle) 
	{
		String plateRegNo = vehicle.getPlateRegNo();
		String engineNo = vehicle.getEngineNo();
		String chassisNo = vehicle.getChassisNo();
		String colour = vehicle.getColour();
		Integer year = vehicle.getYear();
		
		VehicleCriteriaBO criteria = new VehicleCriteriaBO();
		criteria.setPlateRegNo(plateRegNo);
		criteria.setEngineNo(engineNo);
		criteria.setChassisNo(chassisNo);
		criteria.setColour(colour);
		criteria.setYear(year);
		
		List<VehicleDO> retrievedVehicles = daoFactory.getRoadCompliancyDAO().findVehiclesByCriteria(criteria);
		
		if(retrievedVehicles.isEmpty())
		{
			return null;
		}else
		{
			return retrievedVehicles.get(0);
		}
		
	}
	
	
	private boolean updateVehicle(VehicleDO retrievedVehicleDO,VehicleBO vehicleBO)
	{

		/*Allow Update of all other fields except ones forming unique constraint i.e. plate #, engine #, chassis #, color aand year*/	
		retrievedVehicleDO.setMakeDescription(vehicleBO.getMakeDescription());
		retrievedVehicleDO.setModel(vehicleBO.getModel());
		retrievedVehicleDO.setOwnerName(vehicleBO.getOwnerName());
		retrievedVehicleDO.setTypeDesc(vehicleBO.getTypeDesc());
		
		//add audit entry
		AuditEntry audit = retrievedVehicleDO.getAuditEntry();
		audit.setUpdateUsername(vehicleBO.getCurrentUserName());
		audit.setUpdateDTime(Calendar.getInstance().getTime());
		retrievedVehicleDO.setAuditEntry(audit);
		
		return daoFactory.getRoadCompliancyDAO().updateVehicle(retrievedVehicleDO);
		
	}
	
	
	private Integer addVehicle(VehicleBO vehicle)
	{
		VehicleDO vehicleDO =  vehicle.generateVehicleDO();
		
		//add audit entry
		AuditEntry audit = new AuditEntry();
		audit.setCreateUsername(vehicle.getCurrentUserName());
		audit.setCreateDTime(Calendar.getInstance().getTime());
		vehicleDO.setAuditEntry(audit);
		
		Integer personID = (Integer)daoFactory.getRoadCompliancyDAO().saveVehicle(vehicleDO);
		return personID;
	}
	
	/*************************************************************************************************/

	private ComplianceDO complianceExists(ComplianceBO compliance) 
	{
		Integer complianceID = compliance.getComplianceId();
		
		ComplianceDO retrievedCompDO = daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class,complianceID);
		
		return retrievedCompDO;
		
	}
	
	
	private boolean updateCompliance(ComplianceDO retrievedComplianceDO,ComplianceBO complianceBO)
	{
		
		//update person details
		complianceBO.getPerson().setPersonId(retrievedComplianceDO.getPerson().getPersonId());
		if(complianceBO.getPerson()!= null && complianceBO.getPerson().getPersonId()!=null){
			updatePerson(retrievedComplianceDO.getPerson(), complianceBO.getPerson());
		}
		

		//set only comments/vehicle ID / compliance status here
		if(StringUtil.isSet(complianceBO.getComment()))
		{retrievedComplianceDO.setComment(complianceBO.getComment());}

		//set compliance status
		if(StringUtil.isSet(complianceBO.getCompliant()))
		{retrievedComplianceDO.setCompliant(complianceBO.getCompliant());}

		
		//set status
		if(StringUtil.isSet(complianceBO.getStatusId()))
		{retrievedComplianceDO.setStatus(this.daoFactory.getRoadCompliancyDAO().load(StatusDO.class, complianceBO.getStatusId()));}
		
		if(complianceBO.getCourtDate() != null)
		{
			retrievedComplianceDO.setCourtDateTime(complianceBO.getCourtDate());
		}
		
		if(complianceBO.getCourt() != null)
		{
			retrievedComplianceDO.setCourt(daoFactory.getRoadCompliancyDAO().find(CourtDO.class,complianceBO.getCourt().getCourtId()));
		}
		
		//System.err.println("Retrieved Compliance " + retrievedComplianceDO);
		//if vehicle was not added at compliance level and is now being added as a result of a vehicle check carried out, then set vehicle info
		if(retrievedComplianceDO.getVehicle() == null) //no vehicle exists on record
		{
			if(complianceBO.getVehicle() != null) //a record is being sent for saving
			{
				/**
				 * Prepare Vehicle Record
				 ***/
				
				VehicleBO vehicleBO = complianceBO.getVehicle();
				vehicleBO.setCurrentUserName(complianceBO.getCurrentUserName());
				
				VehicleBO savedVehicle = saveVehicle(vehicleBO);
				
				retrievedComplianceDO.setVehicle(daoFactory.getRoadCompliancyDAO().find(VehicleDO.class,savedVehicle.getVehicleId()));
			}
		}
		else{
		
			if(complianceBO.getVehicle() != null){
				
				if(retrievedComplianceDO.getVehicle() != null)
				{
					if(StringUtils.equalsIgnoreCase(retrievedComplianceDO.getVehicle().getPlateRegNo(), complianceBO.getVehicle().getPlateRegNo()) && 
							StringUtils.equalsIgnoreCase(retrievedComplianceDO.getVehicle().getChassisNo(),complianceBO.getVehicle().getChassisNo()) &&
							StringUtils.equalsIgnoreCase(retrievedComplianceDO.getVehicle().getEngineNo(),complianceBO.getVehicle().getEngineNo()) && 
							StringUtils.equalsIgnoreCase(retrievedComplianceDO.getVehicle().getColour(),complianceBO.getVehicle().getColour()) && 
							retrievedComplianceDO.getVehicle().getYear().equals(complianceBO.getVehicle().getYear())){
						updateVehicle(retrievedComplianceDO.getVehicle(), complianceBO.getVehicle());
					}else{
						retrievedComplianceDO.setVehicle(CreateNewVehicle(complianceBO));
					}
					
				}
				
				else{
					retrievedComplianceDO.setVehicle(CreateNewVehicle(complianceBO));
				}
			}
		}
		//set vehicle id on complianceBO
		complianceBO.getVehicle().setVehicleId(retrievedComplianceDO.getVehicle().getVehicleId());
		
		/**
		 * Save Compliance Params
		 */
		if(complianceBO.getListOfComplianceParams()!=null && complianceBO.getListOfComplianceParams().size()>0){
			ComplianceParamDO paramDO = null;
			for(ComplianceParamBO paramBO: complianceBO.getListOfComplianceParams()){
				paramDO = getComplianceParamDOFromBO(paramBO);
				
				daoFactory.getRoadCompliancyDAO().save(paramDO);
			}
			
		}
		
		
		
		/**
		 * Prepare Compliance Record
		 ***/
		
		try {
			ComplianceDO complianceDO =  convertComplianceBOtoDO(complianceBO);
			retrievedComplianceDO.setRoadOperation(complianceDO.getRoadOperation());
			retrievedComplianceDO.setTaStaff(complianceDO.getTaStaff());
			retrievedComplianceDO.setCompliancyArtery(complianceDO.getCompliancyArtery());
			retrievedComplianceDO.setPersonRole(complianceDO.getPersonRole());
			retrievedComplianceDO.setOtherRole(complianceDO.getOtherRole());
			retrievedComplianceDO.setOffenceDateTime(complianceDO.getOffenceDateTime());
			retrievedComplianceDO.setVehicleInfoDifferent(complianceDO.getVehicleInfoDifferent());
						
		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		//add audit entry
		AuditEntry audit = retrievedComplianceDO.getAuditEntry();
		audit.setUpdateUsername(complianceBO.getCurrentUserName());
		audit.setUpdateDTime(Calendar.getInstance().getTime());
		retrievedComplianceDO.setAuditEntry(audit);
		
		
		
		//Save Event Audit if the road check was cancelled
		//Based on review of event audit as a spin off from UR-037
		if(StringUtil.isSet(complianceBO.getStatusId()))
		{
			if(complianceBO.getStatusId().equals(Constants.Status.ROAD_CHECK_CANCELLED))
			{
				cancelRoadCheckEventAudit(complianceBO,complianceBO.getCurrentUserName());
			}
		}
		
		
		return daoFactory.getRoadCompliancyDAO().updateCompliance(retrievedComplianceDO);
		
	}
	
	private VehicleDO CreateNewVehicle(ComplianceBO complianceBO){
		VehicleBO vehicleBO = complianceBO.getVehicle();
		vehicleBO.setCurrentUserName(complianceBO.getCurrentUserName());
		
		VehicleBO savedVehicle = saveVehicle(vehicleBO);
		
		return daoFactory.getRoadCompliancyDAO().find(VehicleDO.class,savedVehicle.getVehicleId());
	}
	
	public ComplianceParamDO getComplianceParamDOFromBO(ComplianceParamBO complianceParamBO) {
		
		ComplianceParamDO complianceParamDO = new ComplianceParamDO();
		
		ComplianceDO complianceDO =  daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, complianceParamBO.getComplianceId());
		ExcerptParamMappingDO excerptParamMappingDO =daoFactory.getRoadCompliancyDAO().find(ExcerptParamMappingDO.class, complianceParamBO.getParamMapId()) ;
		complianceParamDO.setComplianceParamKey(new ComplianceParamKey());
		complianceParamDO.getComplianceParamKey().setComplianceDO(complianceDO);
		complianceParamDO.getComplianceParamKey().setExcerptParamMapDO(excerptParamMappingDO);
		complianceParamDO.setParamValue(complianceParamBO.getParamValue());
		complianceParamDO.setAuditEntry(new AuditEntry());
		complianceParamDO.getAuditEntry().setCreateUsername("SYSTEM");
		complianceParamDO.getAuditEntry().setCreateDTime(DateTime.now().toDate());
		
		
		return complianceParamDO;
	}
	private Integer addCompliance(ComplianceBO compliance) throws InvalidFieldException
	{
		VehicleBO savedVehicle = null;
		PersonBO savedPerson = null;
		
		/**
		 * Prepare Vehicle Record
		 ***/
		if(compliance.getVehicle() != null)
		{
			VehicleBO vehicleBO = compliance.getVehicle();
			vehicleBO.setCurrentUserName(compliance.getCurrentUserName());
		
			savedVehicle = saveVehicle(vehicleBO);
		}
		/**************************/
		
	
		/**
		 * Prepare Person Record
		 ***/
		PersonBO personBO = compliance.getPerson();
		personBO.setCurrentUserName(compliance.getCurrentUserName());
		
		savedPerson = savePerson(personBO);
		/**************************/
		
		
		/////////////////////////////////////////////////////////////
		//Update BO with IDs from Saved Person and Vehicle Records
		////////////////////////////////////////////////////////////
		compliance.setPerson(savedPerson);
		compliance.setVehicle(savedVehicle);
		
		
		
		/**
		 * Prepare Compliance Record
		 ***/
		
		ComplianceDO complianceDO =  convertComplianceBOtoDO(compliance);
		
		//add audit entry
		AuditEntry audit = new AuditEntry();
		audit.setCreateUsername(compliance.getCurrentUserName());
		audit.setCreateDTime(Calendar.getInstance().getTime());
		complianceDO.setAuditEntry(audit);
		
		/**************************/
		
		Integer complianceID = (Integer)daoFactory.getRoadCompliancyDAO().saveCompliance(complianceDO);
		return complianceID;
	}
	

	
	
	private ComplianceDO convertComplianceBOtoDO(ComplianceBO complianceBO) throws InvalidFieldException
	{
		ComplianceDO complianceDO = new ComplianceDO();
		
		if(StringUtil.isSet(complianceBO.getStatusId()))
		{complianceDO.setStatus(this.daoFactory.getRoadCompliancyDAO().load(StatusDO.class, complianceBO.getStatusId()));}
		
		complianceDO.setComment(complianceBO.getComment());
		complianceDO.setCompliant(complianceBO.getCompliant());
		complianceDO.setPerson(daoFactory.getRoadCompliancyDAO().find(PersonDO.class,complianceBO.getPerson().getPersonId()));
		complianceDO.setPersonRole(complianceBO.getPersonRole());
		complianceDO.setOtherRole(complianceBO.getOtherRole());
		
		complianceDO.setVehicleInfoDifferent(complianceBO.getVehicleInfoDifferent());
		
		//Check if Road Operation ID Valid
		if(complianceBO.getRoadOperation() != null && complianceBO.getRoadOperation() > 0)
		{	RoadOperationDO roadOpDO = daoFactory.getRoadCompliancyDAO().find(RoadOperationDO.class,complianceBO.getRoadOperation());
		
			if(roadOpDO != null)
			{	complianceDO.setRoadOperation(roadOpDO);	}
			else
			{throw new InvalidFieldException("Road Operation ID Invalid");}
		}//else
		//{throw new InvalidFieldException("Road Operation ID Invalid");}
		
		//Check if artery is valid
		if(complianceBO.getCompliancyArteryid() != null && complianceBO.getCompliancyArteryid() > 0)
		{	ArteryDO arteryDO = daoFactory.getRoadCompliancyDAO().find(ArteryDO.class,complianceBO.getCompliancyArteryid());
		
			if(arteryDO != null)
			{complianceDO.setCompliancyArtery(arteryDO);}
			else
			{throw new InvalidFieldException("Artery ID Invalid");}
		}else{
			//throw new InvalidFieldException("Artery ID Invalid");
			}
		
		
		
		//Check if staff ID is valid
		if(StringUtil.isSet(complianceBO.getTaStaff()))
		{	TAStaffDO tastaffDO = daoFactory.getRoadCompliancyDAO().find(TAStaffDO.class,complianceBO.getTaStaff());
		
			if(tastaffDO != null)
			{complianceDO.setTaStaff(tastaffDO);}
			else
			{throw new InvalidFieldException("TA Staff ID Invalid");}
		}else
		{throw new InvalidFieldException("TA Staff ID Invalid");}
		
		
		if(complianceBO.getVehicle() != null)
		{	
			complianceDO.setVehicle(daoFactory.getRoadCompliancyDAO().find(VehicleDO.class,complianceBO.getVehicle().getVehicleId()));
		
		}
		
		complianceDO.setCompliant(YesNo.YES_LABEL_STR);
		
		complianceDO.setOffenceDateTime(complianceBO.getComplianceDate());
		return complianceDO;
	}
	
	
	private Vehicle convertVehChkRsltBOtoVehicle(VehicleCheckResultBO mvCheck)
	{
		Vehicle amvsVeh = new Vehicle();
		
		//set general vehicle info
		VehInfo vehInfo = new VehInfo();
		vehInfo.setChassisNo(mvCheck.getChassisNo());
		vehInfo.setEngineNo(mvCheck.getEngineNo());
		//vehInfo.setMotorVehicleID(mvCheck.get);
		vehInfo.setPlateRegistrationNo(mvCheck.getPlateRegNo());
		//vehInfo.setPlateTypeDesc(mvCheck.get);
		vehInfo.setVehicleColour(mvCheck.getColour());
		vehInfo.setVehicleMakeDesc(mvCheck.getMakeDescription());
		vehInfo.setVehicleModel(mvCheck.getModel());
		vehInfo.setVehicleYear(mvCheck.getYear() + "");
		vehInfo.setVehicleTypeDesc(mvCheck.getTypeDesc());
		
		amvsVeh.setVehInfo(vehInfo);
		
		
		//set veh mvrc info
		DateFormat formatter ; 
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		VehMvrc vehMvrc = new VehMvrc();
		vehMvrc.setMvrcNo(mvCheck.getLaNo());
		vehMvrc.setMvrcIssueDate(mvCheck.getMvrcIssueDate().toString());
		vehMvrc.setMvrcExpiryDate(mvCheck.getMvrcIssueDate().toString());
		
//		vehMvrc.setMvrcIssueDate(mvCheck.getMvrcIssueDate()!= null? formatter.parse(mvCheck.getMvrcIssueDate().toString()) : null);
//		vehMvrc.setMvrcExpiryDate(mvCheck.getMvrcExpiryDate()!= null? formatter.parse(mvCheck.getMvrcIssueDate().toString()) : null);
		
		amvsVeh.setVehMvrc(vehMvrc);
		
		
		//set vehicle fitness info
		VehFitness vehFitness = new VehFitness();
		VehicleFitnessBO vehFitnessBO = mvCheck.getFitnessInfo();
		
		vehFitness.setExamDepot(vehFitnessBO.getExamDepot());
		vehFitness.setExpDate(vehFitnessBO.getExpiryDate()+ "");
		vehFitness.setFitnessNo(vehFitnessBO.getFitnessNo());
		vehFitness.setIssueDate(vehFitnessBO.getIssueDate() + "");
		
		amvsVeh.setVehFitness(vehFitness);
		
		
		//set vehicle insurance info
	    VehInsurance vehInsurance = new VehInsurance();
	    VehicleInsuranceBO vehInsBO = mvCheck.getInsuranceInfo();
	    
	    vehInsurance.setCompName(vehInsBO.getCompanyName());
	    vehInsurance.setExpDate(vehInsBO.getExpiryDate() + "");
	    vehInsurance.setIssueDate(vehInsBO.getIssueDate() + "");
	    vehInsurance.setPolicyNo(vehInsBO.getPolicyNo());
	    
	    amvsVeh.setVehInsurance(vehInsurance);
		
	    
	    
	    //set owner information
	    ArrayOfVehOwner arrayOfOwners = new  ArrayOfVehOwner();
	    List<VehicleOwnerBO> listofOwners = mvCheck.getVehicleOwners();
	    
	    for (VehicleOwnerBO vehicleOwnerBO : listofOwners) {
			
	    	VehOwner vehOwner = new VehOwner();
	    	vehOwner.setFirstName(vehicleOwnerBO.getFirstName());
	    	vehOwner.setLastName(vehicleOwnerBO.getLastName());
	    	vehOwner.setTrnNo(vehicleOwnerBO.getTrnNbr());
	    	vehOwner.setTrnBranch(vehicleOwnerBO.getTrnNbr());
	    	
	    	arrayOfOwners.getVehOwner().add(vehOwner);
	   }
	   amvsVeh.setVehOwners(arrayOfOwners);
		
	   
		
		return amvsVeh;
	}
	
	
	
	
	private VehicleCheckResultBO convertVehChckRsltDOtoBO(VehicleCheckResultDO vehChkRsltDO)
	{
		
		VehicleCheckResultBO vehChckRsltBO = new VehicleCheckResultBO();
		
		//set general vehicle info
		vehChckRsltBO.setChassisNo(vehChkRsltDO.getChassisNo());
		vehChckRsltBO.setEngineNo(vehChkRsltDO.getEngineNo());
		
		if(vehChkRsltDO.getRoadCheck() != null)
		{
			if(vehChkRsltDO.getRoadCheck().getCompliance()!= null)
			{
				VehicleDO vehDO = vehChkRsltDO.getRoadCheck().getCompliance().getVehicle();
				if(vehDO != null)
				{
					vehChckRsltBO.setVehicleId(vehDO.getVehicleId());
				}
			}
		}
		
		vehChckRsltBO.setPlateRegNo(vehChkRsltDO.getPlateRegNo());
		vehChckRsltBO.setTypeDesc(vehChkRsltDO.getTypeDesc());
		vehChckRsltBO.setColour(vehChkRsltDO.getColour());
		vehChckRsltBO.setMakeDescription(vehChkRsltDO.getMakeDescription());
		vehChckRsltBO.setModel(vehChkRsltDO.getModel());
		vehChckRsltBO.setYear(vehChkRsltDO.getYear());
		
		
		//set vehicle fitness info
		List<VehicleFitnessDO> vehicleFitnessDOs = daoFactory.getRoadCompliancyDAO().findVehicleFitnessForCheck(vehChkRsltDO.getVehicleCheckId());
		
		VehicleFitnessBO vehFitnessBO = null;
		
		if(vehicleFitnessDOs.size() > 0)
		{
			VehicleFitnessDO vehFitnessDO = daoFactory.getRoadCompliancyDAO().findVehicleFitnessForCheck(vehChkRsltDO.getVehicleCheckId()).get(0);
			
			vehFitnessBO = new VehicleFitnessBO();
			
			vehFitnessBO.setExamDepot(vehFitnessDO.getExamDepot());
			vehFitnessBO.setExpiryDate(vehFitnessDO.getExpiryDate());
			vehFitnessBO.setFitnessNo(vehFitnessDO.getFitnessNo());
			vehFitnessBO.setIssueDate(vehFitnessDO.getIssueDate());
			
			
		}
		
		vehChckRsltBO.setFitnessInfo(vehFitnessBO);
		
		List<VehicleInsuranceDO> vehInsuranceDOs =  daoFactory.getRoadCompliancyDAO().findVehicleInsuranceForCheck(vehChkRsltDO.getVehicleCheckId());
		
		VehicleInsuranceBO vehInsBO = null;
		
		//set vehicle insurance info
		if(vehInsuranceDOs.size() > 0)
		{
		    VehicleInsuranceDO vehInsuranceDO = daoFactory.getRoadCompliancyDAO().findVehicleInsuranceForCheck(vehChkRsltDO.getVehicleCheckId()).get(0);
		    vehInsBO = new VehicleInsuranceBO();
		    
		    vehInsBO.setCompanyName(vehInsuranceDO.getCompanyName());
		    vehInsBO.setExpiryDate(vehInsuranceDO.getExpiryDate());
		    vehInsBO.setIssueDate(vehInsuranceDO.getIssueDate());
		    vehInsBO.setPolicyNo(vehInsuranceDO.getPolicyNo());
		}
	    
	    vehChckRsltBO.setInsuranceInfo(vehInsBO);
		
	    
	    
	    //set owner information
	    List<VehicleCheckResultOwnerDO> vehOwnerDOs = daoFactory.getRoadCompliancyDAO().findVehicleOwnersForCheck(vehChkRsltDO.getVehicleCheckId());
	    List<VehicleOwnerBO> listofOwners = new ArrayList<VehicleOwnerBO>();
	    
	    for (VehicleCheckResultOwnerDO vehicleOwnerDO : vehOwnerDOs) {
			
	    	VehicleOwnerBO vehOwnerBO = new VehicleOwnerBO();
	    	vehOwnerBO.setFirstName(vehicleOwnerDO.getFirstName());
	    	vehOwnerBO.setLastName(vehicleOwnerDO.getLastName());
	    	vehOwnerBO.setMidName(vehicleOwnerDO.getMidName());
	    	vehOwnerBO.setTrnBranch(vehicleOwnerDO.getTrnBranch());
	    	vehOwnerBO.setTrnNbr(vehicleOwnerDO.getTrnNbr());
	    	
	    	AddressDO vehOwnerAdd = vehicleOwnerDO.getAddress();
	    	
	    	vehOwnerBO.setAddress(new AddressBO());//jreid 2014-01-23 changed object to have embedded Adress
	    	
	    	if(vehOwnerAdd != null)
	    	{
	    		
	    		vehOwnerBO.getAddress().setMark(vehOwnerAdd.getMarkText());
	    		vehOwnerBO.getAddress().setStreetNumber(vehOwnerAdd.getStreetNo());
	    		vehOwnerBO.getAddress().setStreetName(vehOwnerAdd.getStreetName());
	    		vehOwnerBO.getAddress().setPoBoxNumber(vehOwnerAdd.getPoBoxNo());
	    		vehOwnerBO.getAddress().setPoLocation(vehOwnerAdd.getPoLocationName());
	    		if(vehOwnerAdd.getParish()!=null){
	    			vehOwnerBO.getAddress().setParish_code(vehOwnerAdd.getParish().getParishCode());
	    			vehOwnerBO.getAddress().setParish(vehOwnerAdd.getParish().getDescription());
	    		}
	    	}
	    	
	    	//Commented out by Oneal Anguin 0- 27 April 2015 - It was overriding the already set parish description value
	    	//vehOwnerBO.getAddress().setParishDesc(vehicleOwnerDO.getParishDesc());
	    	vehOwnerBO.getAddress().setCity(vehicleOwnerDO.getCity());
	    	vehOwnerBO.getAddress().setCountry(vehicleOwnerDO.getCountry());
	    	
	    	listofOwners.add(vehOwnerBO);
		}
	   
	    vehChckRsltBO.setVehicleOwners(listofOwners);
	    
	    vehChckRsltBO.setLaNo(vehChkRsltDO.getLaNo());
	    
	    vehChckRsltBO.setMvrcExpiryDate(vehChkRsltDO.getMvrcExpiryDate());
	    
	    vehChckRsltBO.setMvrcIssueDate(vehChkRsltDO.getMvrcIssueDate());
		
		return vehChckRsltBO;
	}
	
	
	private VehicleCheckResultBO convertVehChckRsltDOtoBO(VehicleCheckResultDO vehChkRsltDO,VehicleFitnessDO vehFitnessDO, VehicleInsuranceDO vehInsuranceDO, 
			List<VehicleCheckResultOwnerDO> vehOwnerDOs)
	{
		
		VehicleCheckResultBO vehChckRsltBO = new VehicleCheckResultBO();
		
		//set general vehicle info
		vehChckRsltBO.setChassisNo(vehChkRsltDO.getChassisNo());
		vehChckRsltBO.setEngineNo(vehChkRsltDO.getEngineNo());
		//vehInfo.setMotorVehicleID(mvCheck.get);
		vehChckRsltBO.setPlateRegNo(vehChkRsltDO.getPlateRegNo());
		vehChckRsltBO.setTypeDesc(vehChkRsltDO.getTypeDesc());
		vehChckRsltBO.setColour(vehChkRsltDO.getColour());
		vehChckRsltBO.setMakeDescription(vehChkRsltDO.getMakeDescription());
		vehChckRsltBO.setModel(vehChkRsltDO.getModel());
		vehChckRsltBO.setYear(vehChkRsltDO.getYear());
		
		
		//set vehicle fitness info		
		VehicleFitnessBO vehFitnessBO = new VehicleFitnessBO();
		
		if(vehFitnessDO != null)
		{
			vehFitnessBO = new VehicleFitnessBO();
			vehFitnessBO.setExamDepot(vehFitnessDO.getExamDepot());
			vehFitnessBO.setExpiryDate(vehFitnessDO.getExpiryDate());
			vehFitnessBO.setFitnessNo(vehFitnessDO.getFitnessNo());
			vehFitnessBO.setIssueDate(vehFitnessDO.getIssueDate());
		}
		
		vehChckRsltBO.setFitnessInfo(vehFitnessBO);
		
		
		//set vehicle insurance info
	    VehicleInsuranceBO vehInsBO = null;
	    
	    
	    if( vehInsuranceDO != null)
	    {
	    	
	    	vehInsBO = new VehicleInsuranceBO();
		    vehInsBO.setCompanyName(vehInsuranceDO.getCompanyName());
		    vehInsBO.setExpiryDate(vehInsuranceDO.getExpiryDate());
		    vehInsBO.setIssueDate(vehInsuranceDO.getIssueDate());
		    vehInsBO.setPolicyNo(vehInsuranceDO.getPolicyNo());
	    }
	    
	    vehChckRsltBO.setInsuranceInfo(vehInsBO);
		
	    
	    
	    //set owner information	   
	    List<VehicleOwnerBO> listofOwners = new ArrayList<VehicleOwnerBO>();
	    
	    for (VehicleCheckResultOwnerDO vehicleOwnerDO : vehOwnerDOs) {
			
	    	VehicleOwnerBO vehOwnerBO = new VehicleOwnerBO();
	    	vehOwnerBO.setFirstName(vehicleOwnerDO.getFirstName());
	    	vehOwnerBO.setLastName(vehicleOwnerDO.getLastName());
	    	vehOwnerBO.setMidName(vehicleOwnerDO.getMidName());
	    	vehOwnerBO.setTrnBranch(vehicleOwnerDO.getTrnBranch());
	    	vehOwnerBO.setTrnNbr(vehicleOwnerDO.getTrnNbr());
	    	
	    	AddressDO vehOwnerAdd = vehicleOwnerDO.getAddress();
	    	
	    	vehOwnerBO.setAddress(new AddressBO());//jreid 2014-01-23 changed object to have embedded Adress
	    	
	    	if(vehOwnerAdd != null)
	    	{
	    		
	    		vehOwnerBO.getAddress().setMark(vehOwnerAdd.getMarkText());
	    		vehOwnerBO.getAddress().setStreetNumber(vehOwnerAdd.getStreetNo());
	    		vehOwnerBO.getAddress().setStreetName(vehOwnerAdd.getStreetName());
	    		vehOwnerBO.getAddress().setPoBoxNumber(vehOwnerAdd.getPoBoxNo());
	    		vehOwnerBO.getAddress().setPoLocation(vehOwnerAdd.getPoLocationName());
	    		vehOwnerBO.getAddress().setParish_code(vehOwnerAdd.getParish() != null ? vehOwnerAdd.getParish().getParishCode() : null);
	    		vehOwnerBO.getAddress().setParish(vehOwnerAdd.getParish() != null ? vehOwnerAdd.getParish().getDescription() : null);
	    	}
	    	
	    	//Commented out by Oneal Anguin 0- 27 April 2015 - It was overriding the already set parish description value
	    	//vehOwnerBO.getAddress().setParishDesc(vehicleOwnerDO.getParishDesc());
	    	vehOwnerBO.getAddress().setCity(vehicleOwnerDO.getCity());
	    	vehOwnerBO.getAddress().setCountry(vehicleOwnerDO.getCountry());
	    	
	    	listofOwners.add(vehOwnerBO);
		}
	   
	    vehChckRsltBO.setVehicleOwners(listofOwners);
	    
	    vehChckRsltBO.setLaNo(vehChkRsltDO.getLaNo());
	    
	    vehChckRsltBO.setMvrcExpiryDate(vehChkRsltDO.getMvrcExpiryDate());
	    
	    vehChckRsltBO.setMvrcIssueDate(vehChkRsltDO.getMvrcIssueDate());
		
		return vehChckRsltBO;
	}
	
	private void createWitnesses(RoadCheckOffenceOutcomeBO roadChkOffOutcome, Integer wNoticeID, AuditEntry audit) throws RequiredFieldMissingException
	{

		List<PersonBO> witnesses = roadChkOffOutcome.getWitnesses();
		
		if(witnesses == null || witnesses.isEmpty())
		{
			/**
			 * Witness is optional for road check. see RFC-0004
			 */
			//throw new RequiredFieldMissingException("There Must Be At Least One Witness per Warning Notice");
		}else
		{
			
			for (PersonBO personBO : witnesses) 
			{
				//save person
				PersonBO witnessPersonBO = savePerson(personBO);
				
				PersonDO savedWitnessPersonDO = daoFactory.getRoadCompliancyDAO().find(PersonDO.class, witnessPersonBO.getPersonId());
				
				WarningNoticeDO savedWNotice = daoFactory.getRoadCompliancyDAO().find(WarningNoticeDO.class, wNoticeID);
				
				//create witness record
				WitnessWarningNoticeKey wNoticeKey = new WitnessWarningNoticeKey(savedWitnessPersonDO, savedWNotice);
				WitnessWarningNoticeDO witnessWNoticeDO = new WitnessWarningNoticeDO(wNoticeKey, audit);
				daoFactory.getRoadCompliancyDAO().save(witnessWNoticeDO);
			}
		}
	}
	
	
	@Transactional(propagation=Propagation.NEVER,isolation=Isolation.REPEATABLE_READ,readOnly=true)
	public List<ComplianceBO> lookupRoadComplianceActivities(RoadCompliancyCriteriaBO roadCompliancyCriteriaBO) {
		
		logger.info("In lookupRoadComplianceActivities web service function.");
		List<ComplianceBO>  result = new ArrayList<ComplianceBO>();
		
		result = daoFactory.getRoadCompliancyDAO().lookupRoadComplianceActivities(roadCompliancyCriteriaBO);
		
		List<OffenceBO> listOfOffences = new ArrayList<OffenceBO>();
		
		List<RoadCheckBO> listOfRoadChecks = new ArrayList<RoadCheckBO>();
		
		for (ComplianceBO complianceBO : result) 
		{
			listOfOffences= daoFactory.getRoadCompliancyDAO().getOffencesforCompliance(complianceBO.getComplianceId());
			listOfRoadChecks = daoFactory.getRoadCompliancyDAO().getRoadChecksforCompliance(complianceBO.getComplianceId());
			
			complianceBO.setListOfOffences(listOfOffences);
			complianceBO.setListOfRoadChecks(listOfRoadChecks);
			
			listOfOffences=null;
			listOfRoadChecks = null;
		}
		
		
		return result;
	}
	
	
	public ComplianceDetailsBO findComplianceDetails(Integer complianceId)
	{
		ComplianceDetailsBO complianceDetailsBO = new ComplianceDetailsBO();
		
		//get outcomes
		List<RoadCheckOffenceOutcomeBO> rdChkOffOutcomes = daoFactory.getRoadCompliancyDAO().getOffenceOutcomeforCompliance(complianceId);
		complianceDetailsBO.setListOfOutcomes(rdChkOffOutcomes);
		
		
		//If road operation is null or operation is special get court date from outcome
		
		//get documents
		List<DocumentViewBO> documents = daoFactory.getRoadCompliancyDAO().getDocumentsforCompliance(complianceId);
		
		
		complianceDetailsBO.setListOfDocuments(documents);
		
		//TODO - loop through documents and set offences
		
		for (DocumentViewBO documentViewBO : documents) {
			
			List<OffenceBO> offences = new ArrayList<OffenceBO>();
			offences = daoFactory.getRoadCompliancyDAO().getOffencesForDocument(documentViewBO.getAutomaticSerialNo(), 
														documentViewBO.getDocumentTypeCode(),complianceId);
			documentViewBO.setListOfOffences(offences);
		}
		
		
		//get pound Info
		WreckingCompanyBO wreckingCo = daoFactory.getRoadCompliancyDAO().getWreckingCoforCompliance(complianceId);
		complianceDetailsBO.setWreckingCompanyDetails(wreckingCo);
				
		
		//get wrecking company info
		PoundBO pound = daoFactory.getRoadCompliancyDAO().getPoundforCompliance(complianceId);
		complianceDetailsBO.setPoundDetails(pound);
		
		
		//get witnesses
		List<PersonBO> witnesses = daoFactory.getRoadCompliancyDAO().getWitnessesforCompliance(complianceId);
		complianceDetailsBO.setListOfWitnesses(witnesses);
		
		
		//get court cases
		List<CourtBO> courts = daoFactory.getRoadCompliancyDAO().getCourtforCompliance(complianceId);
		complianceDetailsBO.setListOfCourts(courts);
		
		//get badge check result
		complianceDetailsBO.setBadgeCheckResult(daoFactory.getRoadCompliancyDAO().getBadgeCheckResult(complianceId));
		
		//get road licence check result
		complianceDetailsBO.setRoadLicCheckResult(daoFactory.getRoadCompliancyDAO().getRoadLicCheckResult(complianceId));
		
		//get vehicle check result
		VehicleCheckResultDO vehicleCheckResultDO = daoFactory.getRoadCompliancyDAO().getVehicleCheckResult(complianceId);
		if(vehicleCheckResultDO != null)
			complianceDetailsBO.setVehicleCheckResult(this.convertVehChckRsltDOtoBO(daoFactory.getRoadCompliancyDAO().getVehicleCheckResult(complianceId)));
		
		//get driver licence check result
		complianceDetailsBO.setDlCheckResult(daoFactory.getRoadCompliancyDAO().getDLCheckResult(complianceId));
	
		
		//Get Details about vehicle mover from warning notice
		WarningNoticeDO warningNoticeDO = daoFactory.getRoadCompliancyDAO().getWarningNoticeForRoadCheck(complianceId);
		
		//Get Doc to pull allegation details
		WarningNoProsecutionDO warningNoProsecutionDO = daoFactory.getRoadCompliancyDAO().getWarningNoProsecutionForRoadCheck(complianceId);
		
		if(warningNoticeDO != null)
		{
			complianceDetailsBO.setVehicleMoverType(warningNoticeDO.getVehicleDeliveredByPersonType() != null ? warningNoticeDO.getVehicleDeliveredByPersonType().getPersonTypeId(): null);
			
			complianceDetailsBO.setVehicleMover(warningNoticeDO.getVehicleDeliveredByPerson() != null ? new PersonBO(warningNoticeDO.getVehicleDeliveredByPerson()): null);
			
			complianceDetailsBO.setWreckerVehicle(warningNoticeDO.getWreckerVehicle() != null ? new VehicleBO(warningNoticeDO.getWreckerVehicle()): null);
			
			
		}
		
		if(warningNoticeDO != null){
			complianceDetailsBO.setAllegation(warningNoticeDO.getAllegation());
		}else{
			if(warningNoProsecutionDO != null){
				complianceDetailsBO.setAllegation(warningNoProsecutionDO.getAllegation());
			}
		}
		
		
		List<ComplianceParamBO> complianceParamBOs = daoFactory.getRoadCompliancyDAO().getCompliancePamsforCompliance(complianceId);
		
		complianceDetailsBO.setListOfComplianceParams(complianceParamBOs);
		
		
		return complianceDetailsBO;
	}
	
	
	
	public List<ExcerptParamMappingBO> findParamsforOffence(List<Integer> offences) {
		StringBuffer strOfOffences = new StringBuffer();
		
		int cnt=1;
		for (Integer offence : offences) {
			
			if(cnt > 1)
			{strOfOffences.append("," + offence);
			}else
			{
				strOfOffences.append(offence);
			}
			
			cnt++;
		}
		
		return  daoFactory.getRoadCompliancyDAO().findParamsforOffence(strOfOffences.toString());
	}



	@Override
	public List<OffenceMandatoryOutcomeBO> getActiveOffenceMandatoryOutcomeList() {
		return  daoFactory.getRoadCompliancyDAO().getActiveOffenceMandatoryOutcomeList();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public void  createOtherCheck(Integer complianceId, String currentUserName) throws InvalidFieldException 
	{
		//prepare audit entry
		 AuditEntry audit = new AuditEntry();
		 audit = new AuditEntry();
		 audit.setCreateUsername(currentUserName);
		 audit.setCreateDTime(Calendar.getInstance().getTime());
			
		 
		 //Check if valid ComplianceID entered
		 ComplianceDO complianceDO = daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, complianceId);
			
		 if(complianceDO == null)
		 {throw new InvalidFieldException("Compliance ID Invalid");}
		 
			
		//create Road Check Record
		RoadCheckDO roadCheckDO = new RoadCheckDO(
				daoFactory.getRoadCompliancyDAO().find(CDRoadCheckTypeDO.class, RoadCheckType.OTHER) 
				,complianceDO,
				'Y',"",audit);
		
		daoFactory.getRoadCompliancyDAO().save(roadCheckDO);
		
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	/**
	 * This function creates a road check stub. This is to be used in cases where no results were returned from query.
	 * This is needed so that offense can still be recorded against the compliancy activity.
	 * @param roadCheckType
	 * @param complianceId
	 * @author oneal anguin
	 * @created 4 March 2014
	 */
	
	
	public void createRoadCheckRecord(String roadCheckType, Integer complianceId, String currentUserName, String comment) throws InvalidFieldException
	{
		//prepare audit entry
		 AuditEntry audit = new AuditEntry();
		 audit = new AuditEntry();
		 audit.setCreateUsername(currentUserName);
		 audit.setCreateDTime(Calendar.getInstance().getTime());
			
		
		//Check if valid ComplianceID entered
		 ComplianceDO complianceDO = daoFactory.getRoadCompliancyDAO().find(ComplianceDO.class, complianceId);
			
		 if(complianceDO == null)
		 {throw new InvalidFieldException("Compliance ID Invalid");}
		 
		 
		//create Road Check Record
		RoadCheckDO roadCheckDO = new RoadCheckDO(
				daoFactory.getRoadCompliancyDAO().find(CDRoadCheckTypeDO.class, roadCheckType) 
				,complianceDO,'Y',comment,audit);
		
		Integer roadCheckID = (Integer)daoFactory.getRoadCompliancyDAO().save(roadCheckDO);
	}

	@Override
	/**
	 * 
	 * @param dlNo
	 * @return
	 * 
	 * This function checks the database if a person exists with a drivers license number. If they are not found then the DL web service
	 * is used to find the person and create a new person object and save that person to the database as well as return the person object.
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public PersonBO getPersonFromDriversLicense(String dlNo,String currentUserName) throws Exception
	{
		try
		{
			PersonDO personDO = daoFactory.getRoadCompliancyDAO().getPersonFromDriversLicense(dlNo);
			
			PersonBO personBO = null;
			
			if(personDO == null)
			{
				//Check drivers license number web service to see if you can find the person. Save them to database if you find them.
				DriverLicenceDetails dlDetails = dlWebservice.getDriversLicenceI(dlNo);
				
				/*Check if a person with that TRN exists in the database if so just update the license number*/
				if(this.daoFactory.getPersonDAO().personExistsByTRN(dlDetails.getTrnNo()))
				{
					
					personDO = this.daoFactory.getPersonDAO().findPersonByTRN(dlDetails.getTrnNo());
				}
				
				
				
				if(personDO == null)
				{
					personBO = new PersonBO(dlDetails, currentUserName);
					
					this.daoFactory.getPersonDAO().savePerson(personBO);
				}
				else
				{
					personDO.setDlNo(dlDetails.getDriversLicenceNo());
					
					this.daoFactory.getPersonDAO().update(personDO);
					
					personBO = new PersonBO(personDO);
				}
			}
			else
			{
				personBO = new PersonBO(personDO);
			}
			
			return personBO;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class,RuntimeException.class})
	public VehicleBO getWreckerVehicle(String regPlateNo) throws Exception
	{
		try
		{
			if(StringUtil.isSet(regPlateNo))
			{
				
				AMVSWebService amvsWS = new AMVSWebService();
				
				VehicleBO vehicleBO = amvsWS.getVehicleBOByPlateNumber(regPlateNo);
				
				this.saveVehicle(vehicleBO);
				
				return vehicleBO;
				
			}
			else
			{
				throw new RequiredFieldMissingException();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
			
		}
	}

	@Override
	public void setMaxOffenceValues(CitationCheckResultBO citationCheck)
	{
		try
		{
			List<ConfigurationBO> configBOs = this.daoFactory.getRoadCompliancyDAO().getMaxCitationValues();
			
			int maxROMSOffences = 100;
			
			int maxTTMSOffences = 50;
			
			for(ConfigurationBO configBO : configBOs)
			{
				if(configBO.getCfgCode().equalsIgnoreCase(Constants.Configuration.MAX_ROMS_OFFENCES))
				{
					maxROMSOffences = Integer.parseInt(configBO.getValue());
				}
				else if(configBO.getCfgCode().equalsIgnoreCase(Constants.Configuration.MAX_TTMS_OFFENCES))
				{
					maxTTMSOffences = Integer.parseInt(configBO.getValue());
				}
			}
			
			citationCheck.setMaxRomsOffences(maxROMSOffences);
			
			citationCheck.setMaxTtmsOffences(maxTTMSOffences);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			citationCheck.setMaxRomsOffences(100);
			citationCheck.setMaxTtmsOffences(50);
		}
		
	}

	@Override
	public void setTotalROMSOffences(CitationCheckResultBO citationCheck,String trn, String dln){
		
		citationCheck.setTotalAmountOfROMSOffences(this.daoFactory.getRoadCompliancyDAO().getTotalROMSOffences(trn, dln));
		
		citationCheck.setTotalAmountOfROMSOffencesVehicle(this.daoFactory.getRoadCompliancyDAO().getTotalROMSOffences(null, null, citationCheck.getRegPlateNo()));
	}
	
	
	
	private AddressDO getPersonAddress(String trnNo,String branch) 
	{
		PersonDO retrievedPerson = null;
		AddressDO retrievedAddress = null;
		
		retrievedPerson = daoFactory.getPersonDAO().findPersonByTRN(trnNo);
		
		if(retrievedPerson == null)
		{
			List<VehicleOwnerDO> vehOwners = this.daoFactory.getRoadCompliancyDAO().findVehicleOwnersByTRN(trnNo,branch);
			
			if(vehOwners != null )
			{
				if(!vehOwners.isEmpty() || vehOwners.size() > 0)
				{
					VehicleOwnerDO vehO = vehOwners.get(0);
					
					retrievedAddress = vehO.getAddress();
				}
				
			}
		}else
		{
			retrievedAddress = retrievedPerson.getAddress();
		}
		
		return retrievedAddress;
		
	}
	
	
	
	private void badgeSearchEventAudit(ComplianceDO complianceDO, BadgeCheckResultBO badgeCheckRequest, AuditEntry audit) {
		
				StringBuffer comment = new StringBuffer();
				EventAuditDO auditDO = new EventAuditDO();
				auditDO.setEvent(daoFactory.getRoadCompliancyDAO().find(CDEventDO.class,EventCode.LOOKUP_DRIVER_CONDUCTOR_BADGE_INFORMATION));
			
				auditDO.setRefType1Code(Constants.EventRefTypeCode.BADGE_NO);
				auditDO.setRefType1(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.BADGE_NO));
				auditDO.setRefValue1(badgeCheckRequest.getBadgeNumber());
					
				auditDO.setRefType2Code(Constants.EventRefTypeCode.BADGE_TYPE);
				auditDO.setRefType2(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.BADGE_TYPE));
				auditDO.setRefValue2(StringUtil.isSet(badgeCheckRequest.getBadgeType())? 
						badgeCheckRequest.getBadgeType().equals("D")?"Driver": 
						badgeCheckRequest.getBadgeType().equals("C")?"Conductor":badgeCheckRequest.getBadgeType():	
						"");
				
				comment.append(StringUtil.appendSemiColon(comment.toString(), "Operation Name: "));
				comment.append(complianceDO.getRoadOperation() != null? 
						StringUtil.isSet(complianceDO.getRoadOperation().getOperationName())? complianceDO.getRoadOperation().getOperationName(): "Unscheduled Operation": 
						"Unscheduled Operation");
				
				
				auditDO.setComment(comment.toString());
				
				auditDO.setAuditEntry(audit);

				    
				daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);
		
	}
	
	
	private void cancelRoadCheckEventAudit(ComplianceBO complianceBO, String user) {
		
		EventAuditDO auditDO = new EventAuditDO();
		StringBuffer comment = new StringBuffer();
		
		 AuditEntry audit = new AuditEntry();
		 audit = new AuditEntry();
		 audit.setCreateUsername(user);
		 audit.setCreateDTime(Calendar.getInstance().getTime());
		
		 
		auditDO.setEvent(daoFactory.getRoadCompliancyDAO().find(CDEventDO.class,EventCode.CANCEL_ROAD_CHECK));
	
		if(complianceBO.getComplianceDate() != null)
		{
			String offenceDateTime = DateUtils.formatDate("yyyy-MM-dd hh:mm a",  complianceBO.getComplianceDate());
			auditDO.setRefType1(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.OFFENCE_DATE));
			auditDO.setRefType1Code(Constants.EventRefTypeCode.OFFENCE_DATE);
			auditDO.setRefValue1(offenceDateTime + " ");
		}
		
		if(complianceBO.getRoadOperationBO() != null)
		{
			auditDO.setRefType2Code(Constants.EventRefTypeCode.OPERATION_NAME);
			auditDO.setRefType2(daoFactory.getRoadCompliancyDAO().find(CDEventRefTypeDO .class, EventRefTypeCode.OPERATION_NAME));
			auditDO.setRefValue2(complianceBO.getRoadOperationBO() != null? 
					StringUtil.isSet(complianceBO.getRoadOperationBO().getOperationName())? complianceBO.getRoadOperationBO().getOperationName(): "Unscheduled Operation": 
					"Unscheduled Operation");

		}
		
		if(complianceBO.getPerson() != null)
		{
			if(StringUtil.isSet(complianceBO.getPerson().getTrnNbr()))
				comment.append("TRN: "+ complianceBO.getPerson().getTrnNbr());
			
		
			if(complianceBO.getPerson() != null)
				comment.append("; Offender Name: "+ complianceBO.getPerson().getFullName());
		}
		
		
		if(complianceBO.getReasonId() != null)
		{
			ReasonDO reasonDO = daoFactory.getRoadCompliancyDAO().find(ReasonDO.class, complianceBO.getReasonId());
		
			if(reasonDO != null)
				comment.append("; Reason: "+ reasonDO.getDescription());
		}
		
		if (StringUtil.isSet(complianceBO.getComment()))
			comment.append("; Comment: "+ complianceBO.getComment());
		
		
		auditDO.setComment(comment.toString());
		
		auditDO.setAuditEntry(audit);

		    
		daoFactory.getEventAuditDAO().saveEventAuditDO(auditDO);

}
	
}
