package org.fsl.roms.service.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.view.AddressView;
import org.fsl.roms.view.ITA_ExaminerBean;
import org.fsl.trn.exceptions.InvalidTaxPayerException;
import org.fsl.trn.exceptions.TaxPayerClosedException;
import org.fsl.trn.exceptions.TaxPayerDeceasedException;
import org.fsl.trn.exceptions.TaxPayerRetiredException;
import org.fsl.trn.exceptions.TaxPayerUnintendedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.ITAExaminerCriteriaBO;
import fsl.ta.toms.roms.trnwebservice.InvalidTrnBranchException_Exception;
import fsl.ta.toms.roms.trnwebservice.NotValidTrnTypeException_Exception;
import fsl.ta.toms.roms.trnwebservice.SystemErrorException_Exception;
import fsl.ta.toms.roms.trnwebservice.TrnDTO;
import fsl.ta.toms.roms.webservices.Maintenance;

/**
 * 
 * @author bssintern
 *
 */

@Service
public class ItaExaminerMaintenanceServiceAction extends BaseServiceAction {

	@Autowired
	private Maintenance itaExaminerMaintenanceService;
	private String trnHitVal;
	public ItaExaminerMaintenanceServiceAction() {
		super();
	}
	
	public ItaExaminerMaintenanceServiceAction(
			Maintenance itaExaminerMaintenanceService) {
		this.itaExaminerMaintenanceService = itaExaminerMaintenanceService;
	}
	
	/**
	 * This function searches based on ITA Examiner criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {

		List<ITAExaminerBO> itaExaminers = null;

		String text="";
		String[] name;
		
		
		try {

			ITAExaminerCriteriaBO criteria = (ITAExaminerCriteriaBO) context
					.getFlowScope().get("criteria");
			String fullname = (String) context.getFlowScope().get("fullName");
			System.err.println("Criteria "+" fullname: "+ fullname + " LastName: " + criteria.getLastName() + "Stat: " + criteria.getStatusId() + "TRN: " + criteria.getTrnNbr() + "parish: " + "Reg: " + criteria.getOfficeLocationCode());
			// do the search
			
			/*MaintenancePortProxy proxy = new MaintenancePortProxy();
			
			proxy._getDescriptor().setEndpoint("http://devwas85:9080/ROMSWS/MaintenanceService");*/
			
			if(fullname!= null && fullname != "")
			{
				if(fullname.contains(","))
				{ 
					if(fullname.contains(", "))
					{
						text = fullname;
						text = text.replaceAll(",","");
						name = text.split(" ");
						System.err.println(String.format("Firstname: %s Lastname:%s", name[0],name[1]));
						if(name.length > 2)
						{
						criteria.setFirstName(name[1]);
						criteria.setLastName(name[0]);
						criteria.setMiddleName(name[2]);
						System.err.println(String.format("Firstname: %s MiddleName:%s Lastname:%s", criteria.getFirstName(),criteria.getMiddleName(), criteria.getLastName()));
						}
						else
						{
							criteria.setFirstName(name[1]);
							criteria.setLastName(name[0]);
							System.err.println(String.format("Firstname: %s  Lastname:%s", criteria.getFirstName(), criteria.getLastName()));
						}
						
					}
					else
					{
						text = fullname;
						text = text.replaceAll(","," ");
						name = text.split(" ");
						System.err.println(String.format("Firstname: %s Lastname:%s", name[0],name[1]));
						if(name.length > 2)
						{
						criteria.setFirstName(name[1]);
						criteria.setLastName(name[0]);
						criteria.setMiddleName(name[2]);
						System.err.println(String.format("Firstname: %s MiddleName:%s Lastname:%s", criteria.getFirstName(),criteria.getMiddleName(), criteria.getLastName()));
						}
						else
						{
							criteria.setFirstName(name[1]);
							criteria.setLastName(name[0]);
							System.err.println(String.format("Firstname: %s  Lastname:%s", criteria.getFirstName(), criteria.getLastName()));
						}
					}
				}else{
					criteria.setLastName(fullname);
				}
			}else{
				criteria.setFirstName("");
				criteria.setLastName("");
				criteria.setMiddleName("");
			}
				
			
			
			itaExaminers = getMaintenanceService().lookupITAExaminer(criteria);


			if (itaExaminers == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordsfound")
								.build());
				context.getFlowScope().put("itaExaminers",
						new ITA_ExaminerBean());
				return success();

			} else if (itaExaminers.size() == 1) {
				
				//itaExaminers.get(0).getPersonBO().setLastName(criteria.getLastName()+", "+criteria.getFirstName()+" "+criteria.getMiddleName());
				context.getFlowScope().put("itaExaminer", itaExaminers.get(0));

				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("itaExaminers",
						new ITA_ExaminerBean(itaExaminers));
				return yes();
			}

			// set the entire list in datatable
			context.getFlowScope().put("itaExaminers",
					new ITA_ExaminerBean(itaExaminers));

		}catch (ArrayIndexOutOfBoundsException e) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("Norecordsfound")
							.build());
			context.getFlowScope().put("itaExaminers",
					new ITA_ExaminerBean());
			return error();
		}
		catch (NoRecordFoundException nRex) {
			
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("Norecordsfound")
							.build());
			context.getFlowScope().put("itaExaminers",
					new ITA_ExaminerBean());
			return error();
		}
		
		catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("itaExaminers",
					new ITA_ExaminerBean());
			return error();
		}
		return success();
	}
	
	
	public Event searchTrn(RequestContext context){
		try{
			ITAExaminerBO ita = (ITAExaminerBO) context.getFlowScope().get("itaExaminer");
			String onUpdateAction = (String)context.getFlowScope().get("updatehideBtn");
			System.err.println("examiner trn from Criteria " + ita.getPersonBO().getTrnNbr());
			if(StringUtils.isBlank(ita.getPersonBO().getTrnNbr())){
				addErrorMessageWithParameter(context,  "RequiredFields.Maintenance", "TRN");
				return error();
			}
			System.err.println("On Search TRN updateValue is " + onUpdateAction);
			//Checks if TRN Exists in ROMS when doing an add
			if(trnExistsInROMS(ita.getPersonBO().getTrnNbr()) && !StringUtils.isEmpty(onUpdateAction)){
				addErrorMessageText(context,"Another ITA Examiner exists with selected TRN");
				add(context);
				return error();
			}
			
			//Retrieves Person TRN Details
			PersonBO personTRNBO;
			personTRNBO = getTRNWebService().getPersonBOByTrn(Integer.parseInt(ita.getPersonBO().getTrnNbr()));
			
			//Uses to check if the trn entered belongs to business
			TrnDTO trnDTO = getTRNWebService().getrnDTOByTrn(Integer.parseInt(ita.getPersonBO().getTrnNbr()));
			
			
			
			if(trnDTO==null || personTRNBO == null){
				addErrorMessage(context, "Norecordfound");
				add(context);
				return error();
			}
			else{
			
				if(trnDTO.isBusinessTrn()){
					addErrorMessageText(context,  "Invalid TRN");
					add(context);
					return error();
				}
				
				if(personTRNBO.getPersonId()!=null){
					if(!onUpdateAction.isEmpty())
						ita.getPersonBO().setPersonId(personTRNBO.getPersonId());
				}
				if(personTRNBO.getFirstName()!=null){
					ita.getPersonBO().setFirstName(WordUtils.capitalize(personTRNBO.getFirstName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
					setTrnHitVal(ita.getPersonBO().getTrnNbr());//Set TRN value for search for name if valid user is returned
				}
				if(personTRNBO.getMiddleName()!=null){
					ita.getPersonBO().setMiddleName(WordUtils.capitalize(personTRNBO.getMiddleName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(personTRNBO.getLastName()!=null){
					ita.getPersonBO().setLastName(WordUtils.capitalize(personTRNBO.getLastName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(personTRNBO.getStreetName()!=null){
					if(!onUpdateAction.isEmpty())
						ita.getPersonBO().setStreetName(WordUtils.capitalize(personTRNBO.getStreetName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(personTRNBO.getStreetNo()!=null){
					if(!onUpdateAction.isEmpty())
						ita.getPersonBO().setStreetNo(WordUtils.capitalize(personTRNBO.getStreetNo().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(personTRNBO.getParishCode()!=null){
					if(!onUpdateAction.isEmpty())
						ita.getPersonBO().setParishCode(WordUtils.capitalize(personTRNBO.getParishCode().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(personTRNBO.getPoBoxNo()!=null){
					if(!onUpdateAction.isEmpty())
						ita.getPersonBO().setPoBoxNo(WordUtils.capitalize(personTRNBO.getPoBoxNo().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(personTRNBO.getPoLocationName()!=null){
					if(!onUpdateAction.isEmpty())
						ita.getPersonBO().setPoLocationName(WordUtils.capitalize(personTRNBO.getPoLocationName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(personTRNBO.getMarkText()!=null){
					if(!onUpdateAction.isEmpty())
						ita.getPersonBO().setMarkText(WordUtils.capitalize(personTRNBO.getMarkText().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(personTRNBO.getTelephoneHome()!=null){
					if(!onUpdateAction.isEmpty())
						ita.getPersonBO().setTelephoneHome(WordUtils.capitalize(personTRNBO.getTelephoneHome().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				
				if(personTRNBO.getTelephoneCell()!=null){
					if(!onUpdateAction.isEmpty())
						ita.getPersonBO().setTelephoneCell(WordUtils.capitalize(personTRNBO.getTelephoneCell().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				
				if(personTRNBO.getTelephoneWork()!=null){
					if(!onUpdateAction.isEmpty())
						ita.getPersonBO().setTelephoneWork(WordUtils.capitalize(personTRNBO.getTelephoneWork().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				
				
				
			}
				System.out.println("PersonBO via ita " + ita.getPersonBO().getFirstName() +" "+ita.getPersonBO().getLastName());
				
				context.getFlowScope().put("itaExaminer", ita);
			
			
				return success();
				
			} catch (NumberFormatException e) {
				addErrorMessageText(context, "Invalid Trn");
				add(context);
				e.printStackTrace();
				return error();
			} catch (InvalidTrnBranchException_Exception e) {
					addErrorMessage(context, "Norecordfound");
					add(context);
					e.printStackTrace();
					return error();
			} catch (NotValidTrnTypeException_Exception e) {
					addErrorMessage(context, "Invalid TRN");
					add(context);
					e.printStackTrace();
					return error();
			}  catch (SystemErrorException_Exception e) {
					addErrorMessage(context,  "search.failure");
					add(context);
					e.printStackTrace();
					return error();
				}
		 	catch (TaxPayerClosedException e) {
			 	addErrorMessage(context, "TRN.Status.Closed");	
				add(context);
				e.printStackTrace();
				return error();
			} catch (TaxPayerUnintendedException e) {
				addErrorMessage(context, "TRN.Status.Invalid");
				add(context);
				e.printStackTrace();
				return error();
			} catch (TaxPayerDeceasedException e) {
				addErrorMessage(context, "TRN.Status.Deceased");
				add(context);
				e.printStackTrace();
				return error();
			} catch (TaxPayerRetiredException e) {
				addErrorMessage(context, "TRN.Status.Retired");
				add(context);
				e.printStackTrace();
				return error();
			} catch (InvalidTaxPayerException e) {
				addErrorMessage(context, "TRN.Status.Invalid");
				add(context);
				e.printStackTrace();
				return error();
			}
			catch(Exception ex)
			{
				addErrorMessage(context,  "search.failure");
				add(context);
				ex.printStackTrace();
				return error();
			}
		}
		
	public Boolean trnExistsInROMS(String trn){
		//Checks if TRN Exists in ROMS
		try {
			boolean answer = getMaintenanceService().itaExaminerExistWithTRN(trn);
			if(answer)
				return true;
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	public void select(RequestContext context){
		System.err.println("select called");
		
		try {
			ITA_ExaminerBean itaExaminerBean = (ITA_ExaminerBean)context.getFlowScope().get("itaExaminers");
			System.err.println("ITA: firstname "+itaExaminerBean.getSelectedItaExaminer().getPersonBO().getFirstName()+" id:"+itaExaminerBean.getSelectedItaExaminer().getExaminerId() +"trn "+ itaExaminerBean.getSelectedItaExaminer().getPersonBO().getTrnNbr());
			setTrnHitVal(itaExaminerBean.getSelectedItaExaminer().getPersonBO().getTrnNbr());
			context.getFlowScope().put("itaExaminer", itaExaminerBean.getSelectedItaExaminer());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void back(RequestContext context){
		ITAExaminerCriteriaBO criteria = (ITAExaminerCriteriaBO) context
				.getFlowScope().get("criteria");
		context.getFlowScope().put("criteria",criteria);
		
		ITA_ExaminerBean itaExaminerBean = (ITA_ExaminerBean) context.getFlowScope().get("itaExaminers");
		
		if(itaExaminerBean.getItaExaminerList().size() > 0)
		{
			search(context);
		}
	}
	
	public void add(RequestContext context){
		System.err.println("add called");
		
		try {
			ITAExaminerBO ita = new ITAExaminerBO();
			ita.setPersonBO(new PersonBO());
			ita.setStatusId("ACT");
			context.getFlowScope().put("itaExaminer",ita);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Event updateitaExaminer(RequestContext context) {

		try {
			ITAExaminerBO itaExaminer = (ITAExaminerBO) context.getFlowScope().get("itaExaminer");
			ITAExaminerCriteriaBO criteria = (ITAExaminerCriteriaBO) context
					.getFlowScope().get("criteria");
			ITA_ExaminerBean itaExaminerBean = (ITA_ExaminerBean)context.getFlowScope().get("itaExaminers");
			
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			if (itaExaminer == null)
				throw new Exception("Please enter valid details.");

			// get user details
			itaExaminer.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			System.out.println(" street Name " + itaExaminer.getPersonBO().getStreetName());
			
			System.err.println("ITA Examiner ID " + itaExaminer.getExaminerId());
			//searchTrn(context);//search if trn is valid 
			if (validateArgs(itaExaminer, context)) {
				return error();
			} 
			
			/*if(!validateTRN(Integer.parseInt(itaExaminer.getPersonBO().getTrnNbr()), context)){
				return error();
			}*/
			
				
			getMaintenanceService().updateITAExaminer(itaExaminer);
			System.err.println("search list size " + itaExaminerBean.getItaExaminerList().size());
			itaExaminer.setAuditEntryBO(new AuditEntryBO());
			itaExaminer.getAuditEntryBO().setUpdateUsername(usernameToFullName);
			/*if(itaExaminerBean.getItaExaminerList().size() > 1){
				
				context.getFlowScope().put("criteria", criteria);
				search(context);
			}else{
				ItaExaminerCriteriaBO itaExamCrit = new ItaExaminerCriteriaBO();
				jpCrit.setRegNumber(jp.getRegNumber());
				jpCrit.setTrnNbr(jp.getPersonBO().getTrnNbr());
				jpCrit.setFirstName(jp.getPersonBO().getFirstName());
				jpCrit.setLastName(jp.getPersonBO().getLastName());
				jpCrit.setMiddleName(jp.getPersonBO().getMiddleName());
				jpCrit.setParishCode(jp.getParishCode());
				context.getFlowScope().put("criteria", itaExamCrit);
				search(context);
			}*/
			
			context.getFlowScope().put("itaExaminer", itaExaminer);
			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("ITA Examiner ").build());

			// retrieve results from database again
			// search(context);
			return success();
		}catch (InvalidFieldException e) {
			e.printStackTrace();
			context.getMessageContext().addMessage(
			new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
		}catch (ErrorSavingException e) {
			e.printStackTrace();
			context.getMessageContext().addMessage(
			new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
		}catch (Exception e) {

			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("ITA Examiner ").build());
			return error();
		}

	}

	public Event saveitaExaminer(RequestContext context) {
			System.err.println("Save fired");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
		try {
			ITAExaminerBO itaExaminer = (ITAExaminerBO) context.getFlowScope().get("itaExaminer");
			ITAExaminerCriteriaBO criteria = (ITAExaminerCriteriaBO) context
					.getFlowScope().get("criteria");
			ITA_ExaminerBean itaExaminerBean = (ITA_ExaminerBean)context.getFlowScope().get("itaExaminers");
			
			
			itaExaminer.getPersonBO().setMarkText(itaExaminer.getPersonBO().getMarkText());
			System.err.println("ITA: trn" + itaExaminer.getPersonBO().getTrnNbr() +"mark Text "+ itaExaminer.getPersonBO().getMarkText() +"first name"+ itaExaminer.getPersonBO().getFirstName() + "EXaminer ID " + itaExaminer.getExaminerId());
			
			if (itaExaminer == null)
				throw new Exception("Please enter details.");

			// get user details
			itaExaminer.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			itaExaminer.setStatusId(Constants.Status.ACTIVE);
			//searchTrn(context);//search if trn is valid 
			if (validateArgs(itaExaminer, context)) {
				return error();
			} 
			
			//validateTRN(Integer.parseInt(itaExaminer.getPersonBO().getTrnNbr()), context);
			/*if(!){
				return error();
			}*/
			
			
			getMaintenanceService().saveITAExaminer(itaExaminer);
			itaExaminer.setAuditEntryBO(new AuditEntryBO());
			itaExaminer.getAuditEntryBO().setUpdateUsername(usernameToFullName);
			/*if(itaExaminerBean.getItaExaminerList().size() >= 1){
				context.getFlowScope().put("criteria", criteria);
				search(context);
			}else{
				ItaExaminerCriteriaBO itaExamCrit = new ItaExaminerCriteriaBO();
				jpCrit.setRegNumber(jp.getRegNumber());
				jpCrit.setTrnNbr(jp.getPersonBO().getTrnNbr());
				jpCrit.setFirstName(jp.getPersonBO().getFirstName());
				jpCrit.setLastName(jp.getPersonBO().getLastName());
				jpCrit.setMiddleName(jp.getPersonBO().getMiddleName());
				jpCrit.setParishCode(jp.getParishCode());
				//context.getFlowScope().put("criteria", itaExamCrit);
				//search(context);
			}*/
			
			context.getFlowScope().put("itaExaminer", itaExaminer);
			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("ITA Examiner ").build());

			return success();
		}catch (InvalidFieldException e) {
			e.printStackTrace();
			context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
		} 
		catch(ErrorSavingException e)
		{
			e.printStackTrace();
			context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
		}
		
		catch (Exception e) {
			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("ITA Examiner ").build());
			return error();
		}

	}
	
	
	//Used to check if required fields are null
	//Returns message to indicate field is required if left null	
	private Boolean validateArgs(ITAExaminerBO itaExaminer, RequestContext context) {

		Boolean error = false;
		int trn = 0;
		MessageContext messages = context.getMessageContext();
		
		if(!StringUtils.isEmpty(itaExaminer.getPersonBO().getTrnNbr()))
			trn = Integer.parseInt(itaExaminer.getPersonBO().getTrnNbr());
		
		if (StringUtils.isBlank(itaExaminer.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status").build());
			error = true;
		}
		
		//Validation for TRN
		if (StringUtils.isBlank(itaExaminer.getPersonBO().getTrnNbr())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("TRN").build());
			error = true;
		}else{
				try {
					PersonBO personTRNBO;
					personTRNBO = getTRNWebService().getPersonBOByTrn(trn);
					TrnDTO trnDTO = getTRNWebService().getrnDTOByTrn(trn);
					
					//Uses to check if the trn entered belongs to business
					
					if(trnDTO==null || personTRNBO == null){
						messages.addMessage(new MessageBuilder().error().code("Norecordfound").build());
						System.err.println("TRNDTO is null");
						error = true;
					}
					else{
						System.err.println("TRNDTO not null");
						if(trnDTO.isBusinessTrn()){
							messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
							error = true;
						}
					}
				
					}catch (InvalidTrnBranchException_Exception e) {
					// TODO Auto-generated catch block
						messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
					e.printStackTrace();
					error = true;
					} catch (NotValidTrnTypeException_Exception e) {
						// TODO Auto-generated catch block
						messages.addMessage(new MessageBuilder().error().code("Norecordfound").build());
						e.printStackTrace();
						error = true;
					} catch (RequiredFieldMissingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						error = true;
					} catch (SystemErrorException_Exception e) {
						// TODO Auto-generated catch block
						messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
						e.printStackTrace();
						error = true;
					}
				
				 	catch (TaxPayerClosedException e) {
				 		messages.addMessage(new MessageBuilder().error().code("TRN.Status.Closed").build());
					 	//addErrorMessage(context, "TRN.Status.Closed");	
						e.printStackTrace();
						error = true;
					} catch (TaxPayerUnintendedException e) {
						messages.addMessage(new MessageBuilder().error().code("TRN.Status.Invalid").build());
						//addErrorMessage(context, "TRN.Status.Invalid");
						e.printStackTrace();
						error = true;
					} catch (TaxPayerDeceasedException e) {         
						messages.addMessage(new MessageBuilder().error().code("TRN.Status.Deceased").build());
						//addErrorMessage(context, "TRN.Status.Deceased");
						e.printStackTrace();
						error = true;
					} catch (TaxPayerRetiredException e) {
						messages.addMessage(new MessageBuilder().error().code("TRN.Status.Retired").build());
						//addErrorMessage(context, "TRN.Status.Retired");					
						e.printStackTrace();
						error = true;
					} catch (InvalidTaxPayerException e) {
						messages.addMessage(new MessageBuilder().error().code("TRN.Status.Invalid").build());
						//addErrorMessage(context, "TRN.Status.Invalid");
						e.printStackTrace();
						error = true;
					}
				
					 catch  (Exception ex){
						 messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
						 ex.printStackTrace();
						 error = true;
					 }
		}
		
		if(StringUtils.isBlank(getTrnHitVal())) {
			/*messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("TRN Number").build());*/
			error = true;
		}else if(!getTrnHitVal().equals(itaExaminer.getPersonBO().getTrnNbr()))
		{
			messages.addMessage(new MessageBuilder().error().defaultText("TRN does not match user details").build());
			error = true;
		}
		
		
		if (StringUtils.isBlank(itaExaminer.getPersonBO().getLastName()) && StringUtils.isBlank(itaExaminer.getPersonBO().getFirstName())) {
			messages.addMessage(new MessageBuilder().error()
					.defaultText("Last Name and First Name is required.").build());
			messages.addMessage(new MessageBuilder().error()
					.defaultText("Please search using TRN.").build());
			error = true;
		}
		
		if (StringUtils.isBlank(itaExaminer.getExaminerId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("ID Number").build());
			error = true;
		}
		
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView itaExaminerAddress = new AddressView(itaExaminer.getPersonBO().getMarkText(), itaExaminer.getPersonBO().getParishCode(), itaExaminer.getPersonBO().getPoBoxNo(),
				itaExaminer.getPersonBO().getPoLocationName(), itaExaminer.getPersonBO().getStreetName(), itaExaminer.getPersonBO().getStreetNo(),
				null, null, null,itaExaminer.getPersonBO().getParishDescription());
		
		//global address validation - kpowell
		boolean errorFoundInAddress  = validateAddress(context, itaExaminerAddress);
        
        if(errorFoundInAddress){
              error = true;
        }
        /**
         * 
         */
		if (!(StringUtils.isBlank(itaExaminer.getPersonBO().getTelephoneHome())) && (itaExaminer.getPersonBO().getTelephoneHome().length() < 13)) {
			messages.addMessage(new MessageBuilder().error()
					.code("Telephone").arg("Home").build());
			error = true;

		} 
		
		if (!(StringUtils.isBlank(itaExaminer.getPersonBO().getTelephoneCell())) && (itaExaminer.getPersonBO().getTelephoneCell().length() < 13)) {
			messages.addMessage(new MessageBuilder().error()
					.code("Telephone").arg("Cell").build());
			error = true;

		} 
		
		if (!(StringUtils.isBlank(itaExaminer.getPersonBO().getTelephoneWork())) && (itaExaminer.getPersonBO().getTelephoneWork().length() < 13)) {
			messages.addMessage(new MessageBuilder().error()
					.code("Telephone").arg("Work").build());
			error = true;

		} 
		
		if (StringUtils.isBlank(itaExaminer.getOfficeLocationCode())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Region").build());
			error = true;
		}else if(!isRegionActive()){
			String codeDescription = getOfficeLocationDesc(itaExaminer.getOfficeLocationCode());
			messages.addMessage(new MessageBuilder().error()
					.code("RegionNotActive").arg(codeDescription).build());
			error = true;
		}
		
		/*if (StringUtils.isEmpty(itaExaminer.getPersonBO().getParishCode())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Parish").build());
			error = true;

		} */
		if (StringUtils.isBlank(itaExaminer.getCurrentUsername())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("User Name ").build());
			error = true;
		}
		
	
		return error;
	}
	
	/*public Boolean validateTRN(int trn,RequestContext context){
		//Retrieves Person TRN Details
		System.err.println("validate TRN");
		
		
		return true;
				
	}*/
	
	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("itaExaminers");
		/*
		 * context.getFlowScope().put("itaExaminers", new
		 * ITA_ExaminerBean());
		 */
		// itaExaminers= new ArrayList<ItaExaminerBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		context.getViewScope().clear();
		return success();

	}

	public void clearDetail(RequestContext context) {
		
		System.err.println("Clear detail called");
		ITAExaminerBO itaExaminer = (ITAExaminerBO) context.getFlowScope().get("itaExaminer");
		
		itaExaminer.setExaminerId("");
		
		PersonBO newPerson = new PersonBO();
		
		//System.err.println("Person: "+itaExaminer.getPersonBO().getFullName());
		//itaExaminer.getPersonBO().getTrnNbr()
		newPerson.setTrnNbr("");
		
		newPerson.setFirstName("");
		
		newPerson.setLastName("");
		
		newPerson.setMiddleName("");
		
		newPerson.setPersonId(itaExaminer.getPersonBO().getPersonId());
		
		itaExaminer.setPersonBO(newPerson);
		
		itaExaminer.setOfficeLocationCode("");
		
		itaExaminer.setStatusId("ACT");
		context.getFlowScope().put("itaExaminer",itaExaminer);
	}

	public Maintenance getMaintenanceService() {
		return itaExaminerMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance itaExaminerMaintenanceService) {
		this.itaExaminerMaintenanceService = itaExaminerMaintenanceService;
	}

	public String getTrnHitVal() {
		return trnHitVal;
	}

	public void setTrnHitVal(String trnHitVal) {
		this.trnHitVal = trnHitVal;
	}
	
	
}
