package org.fsl.roms.service.action;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.view.AddressView;
import org.fsl.roms.view.JpBean;
import org.fsl.trn.exceptions.InvalidTaxPayerException;
import org.fsl.trn.exceptions.TaxPayerClosedException;
import org.fsl.trn.exceptions.TaxPayerDeceasedException;
import org.fsl.trn.exceptions.TaxPayerRetiredException;
import org.fsl.trn.exceptions.TaxPayerUnintendedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.Message;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.JPCriteriaBO;
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
public class JpMaintenanceServiceAction extends BaseServiceAction {

	@Autowired
	private Maintenance jpMaintenanceService;
	private String trnHitVal;
	public String concatName;
	public JpMaintenanceServiceAction() {
		super();
	}
	
	public JpMaintenanceServiceAction(
			Maintenance jpMaintenanceService) {
		this.jpMaintenanceService = jpMaintenanceService;
	}
	
	/**
	 * This function searches based on JP criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {

		List<JPBO> jps = null;
		String text="";
		String[] name;
		String lname = "";
		String fname = "";
		String mname = "";

		try {

			JPCriteriaBO criteria = (JPCriteriaBO) context
					.getFlowScope().get("criteria");
			
			String fullname = (String) context.getFlowScope().get("fullName");
			System.out.println("Fullname :"+fullname);
			/*System.err.println("Criteria " + criteria.getTrnNbr().length() + "Stat: " + criteria.getStatusId() + "TRN: " + criteria.getTrnNbr() + "parish: " + criteria.getParishCode() + "Reg: " + criteria.getRegNumber());*/
			// do the search
			/*if(criteria.getTrnNbr().length() > 0 && criteria.getTrnNbr().length() < 9){
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
				return error();
			}*/
			
			if(!StringUtils.isEmpty(fullname))
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
			}
			else{
				criteria.setFirstName("");
				criteria.setLastName("");
				criteria.setMiddleName("");
			}
			
			System.err.println(String.format("Fields for Search Firstname: %s MiddleName:%s Lastname:%s RegNum %s Parish %s", criteria.getFirstName(),criteria.getMiddleName(), criteria.getLastName(),criteria.getRegNumber(),criteria.getParishCode()));
			jps = getMaintenanceService().lookupJP(criteria);
			
			if (jps == null) {
				System.out.println("null");
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordsfound")
								.build());
				context.getFlowScope().put("jps",
						new JpBean());
				return success();

			} else if (jps.size() == 1) {
				//setConcatName(jps.get(0).getPersonBO().getFullName());
				context.getFlowScope().put("jp", jps.get(0));
				
				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("jps",
						new JpBean(jps));
				return yes();
			}

			// set the entire list in datatable
			context.getFlowScope().put("jps",
					new JpBean(jps));

		}catch (ArrayIndexOutOfBoundsException e) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("Norecordsfound")
							.build());
			context.getFlowScope().put("jps",
					new JpBean());
			return error();
		}catch(NoRecordFoundException nRec)
		{System.out.println("exception");
		context.getMessageContext().addMessage(
					new MessageBuilder().error().code("Norecordsfound")
							.build());
			context.getFlowScope().put("jps",
					new JpBean());
			return error();
		}
		
		catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("jps",
					new JpBean());
			return error();
		}
		return success();
	}
	
	public Event searchTrn(RequestContext context){
		try{
			JPBO jp = (JPBO) context.getFlowScope().get("jp");
			String onUpdateAction = (String)context.getFlowScope().get("displayUpdateBtn");
			System.err.println("TRN from Criteria " + jp.getPersonBO().getTrnNbr());
			if(StringUtils.isBlank(jp.getPersonBO().getTrnNbr())){
				addErrorMessageWithParameter(context,  "RequiredFields.Maintenance", "TRN");
				return error();
			}
			
			if(jp.getPersonBO().getTrnNbr().length() > 0 && jp.getPersonBO().getTrnNbr().length() < 9){
				context.getMessageContext().addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
				return error();
			}
			//fsl.ta.toms.roms.webservices.trn.PersonBO personTRNBO;
			//personTRNBO = getTRNWebServicePortProxy().getPersonBOByTrn(Integer.parseInt(jp.getPersonBO().getTrnNbr()));
			
			System.err.println("On Search TRN updateValue is " + onUpdateAction);
			//Checks if TRN Exists in ROMS when doing an add
			if(trnExistsInROMS(jp.getPersonBO().getTrnNbr()) && !StringUtils.isEmpty(onUpdateAction)){
				addErrorMessageText(context,"JP Record already exist with the TRN entered.");
				add(context);
				return error();
			}
			
			
			TrnDTO trnDTO = getTRNWebService().getrnDTOByTrn(Integer.parseInt(jp.getPersonBO().getTrnNbr()));
			
			
			
			if(trnDTO==null){
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
				
				if(trnDTO.getFirstName()!=null){
					jp.getPersonBO().setFirstName(WordUtils.capitalize(trnDTO.getFirstName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
					setTrnHitVal(jp.getPersonBO().getTrnNbr());//Set TRN value for search for name if valid user is returned
				}
				
				if(trnDTO.getMiddleName()!=null){
					jp.getPersonBO().setMiddleName(WordUtils.capitalize(trnDTO.getMiddleName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(trnDTO.getLastName()!=null){
					jp.getPersonBO().setLastName(WordUtils.capitalize(trnDTO.getLastName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				
				if(trnDTO.getAddrStreetNo()!=null){
					if(!onUpdateAction.isEmpty())
						jp.getPersonBO().setStreetNo(WordUtils.capitalize(trnDTO.getAddrStreetNo().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				
				if(trnDTO.getAddrStreetName()!=null){
					if(!onUpdateAction.isEmpty())
						jp.getPersonBO().setStreetName(WordUtils.capitalize(trnDTO.getAddrStreetName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(trnDTO.getAddrParishCode()!=null){
					if(!onUpdateAction.isEmpty())
						jp.getPersonBO().setParishCode(WordUtils.capitalize(trnDTO.getAddrParishCode().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(trnDTO.getAddrPoBoxNo()!=null){
					if(!onUpdateAction.isEmpty())
						jp.getPersonBO().setPoBoxNo(WordUtils.capitalize(trnDTO.getAddrPoBoxNo().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(trnDTO.getAddrPoLocName()!=null){
					if(!onUpdateAction.isEmpty())
						jp.getPersonBO().setPoLocationName(WordUtils.capitalize(trnDTO.getAddrPoLocName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(trnDTO.getAddrMarkText()!=null){
					if(!onUpdateAction.isEmpty())
						jp.getPersonBO().setMarkText(WordUtils.capitalize(trnDTO.getAddrMarkText().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(trnDTO.getAddrParishCode()!=null)
					if(!onUpdateAction.isEmpty())
						jp.setParishCode(trnDTO.getAddrParishCode());
				
				
				System.out.println("PersonBO via jp " + jp.getPersonBO().getFirstName() +" "+jp.getPersonBO().getLastName() +"Street Nam" + jp.getPersonBO().getStreetName());
				
				context.getFlowScope().put("jp", jp);
				
			}
			
			
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
				addErrorMessageText(context, "Invalid TRN");
				add(context);
				e.printStackTrace();
				return error();
		}  catch (SystemErrorException_Exception e) {
				addErrorMessage(context,  "TRN.Status.Invalid");
				add(context);
				e.printStackTrace();
				return error();
		}catch (TaxPayerClosedException e) {
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
			boolean answer = getMaintenanceService().jpExistWithTRN(trn);
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
			JpBean jpBean = (JpBean)context.getFlowScope().get("jps");
			System.err.println("JP: "+jpBean.getSelectedJP().getPersonBO().getFirstName()+" id:"+jpBean.getSelectedJP().getRegNumber() +"pin "+ jpBean.getSelectedJP().getNewPin());
			setTrnHitVal(jpBean.getSelectedJP().getPersonBO().getTrnNbr());
			context.getFlowScope().put("jp",jpBean.getSelectedJP());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void back(RequestContext context){
		JPCriteriaBO criteria = (JPCriteriaBO) context
				.getFlowScope().get("criteria");
		context.getFlowScope().put("criteria",criteria);
		
		JpBean jpBean = (JpBean) context.getFlowScope().get("jps");
		
		
		if(jpBean.getJpList().size() > 0)
		{
			search(context);
		}
	}
	
	public void add(RequestContext context){
		System.err.println("add called");
		
		try {
			//WreckingCompanyBean wreckingCompanyBean = (WreckingCompanyBean)context.getFlowScope().get("wreckingCompanies");
			//System.err.println("Wrecking: "+wreckingCompanyBean.getSelectedWreckingCompany().getCompanyName()+" id:"+wreckingCompanyBean.getSelectedWreckingCompany().getWreckingCompanyId() +"trn "+ wreckingCompanyBean.getSelectedWreckingCompany().getTrnNbr());
			JPBO JPBO = new JPBO();
			JPBO.setStatusId("ACT");
			JPBO.setPersonBO(new PersonBO());
			System.err.println("PersonBO on add " + JPBO.getNewPin() +" work "+ JPBO.getPersonBO().getTelephoneWork());
			context.getFlowScope().put("jp", JPBO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Event updatejp(RequestContext context) {

		try {
			JPBO jp = (JPBO) context.getFlowScope().get("jp");
			JpBean jpBean = (JpBean)context.getFlowScope().get("jps");
			
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			if (jp == null)
				throw new Exception("Please enter valid details.");
			System.err.println("Old pin " + jp.getOldPin() +"new pin " + jp.getNewPin());
			
			System.err.println("JP: status: "+jp.getStatusId());
			
			
			jp.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			
			if (validateArgs(jp, context)) {
				return error();
			} 
			
			
			if(jp.getPersonBO().getTelephoneCell()== null || jp.getPersonBO().getTelephoneCell().equals("(___)___-____")){
				jp.getPersonBO().setTelephoneCell("");
			}
			if(jp.getPersonBO().getTelephoneHome() == null || jp.getPersonBO().getTelephoneHome().equals("(___)___-____")){
				jp.getPersonBO().setTelephoneHome("");
			}
			if(jp.getPersonBO().getTelephoneWork() == null || jp.getPersonBO().getTelephoneWork().equals("(___)___-____")){
				jp.getPersonBO().setTelephoneWork("");
			}
			
			
			getMaintenanceService().updateJP(jp);
			jp.getPersonBO().setFirstName(jp.getPersonBO().getFirstName());
			jp.getPersonBO().setLastName(jp.getPersonBO().getLastName());
			jp.getPersonBO().setMiddleName(jp.getPersonBO().getMiddleName());
			jp.setRegNumber(jp.getRegNumber());
			jp.setParishDescription(getParishDesc(jp.getParishCode()));
			System.out.println("Parish Name " + jp.getParishDescription() + " jp " + jp.getPersonBO().getFirstName());
			jp.setAuditEntryBO(new AuditEntryBO());
			jp.getAuditEntryBO().setUpdateUsername(usernameToFullName);
			jp.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
			JPCriteriaBO criteriaX = (JPCriteriaBO) context.getFlowScope().get("criteria");
			if(jpBean.getJpList().size() >= 1){
				
					System.out.println("Criteria from update " + criteriaX.getTrnNbr() + " name "+ criteriaX.getFirstName() + " parish "+ criteriaX.getParishCode());
					if(jpBean.getJpList().size() == 1){
						JPCriteriaBO newJp = new JPCriteriaBO();
						
						if((!StringUtils.isBlank(criteriaX.getTrnNbr())) && ((!StringUtils.isBlank(criteriaX.getLastName()) || !StringUtils.isBlank(criteriaX.getParishCode())) || (StringUtils.isBlank(criteriaX.getLastName()) && StringUtils.isBlank(criteriaX.getParishCode())))){
							newJp.setTrnNbr(criteriaX.getTrnNbr());
							System.err.println("Setting TRN for Search");
						}else if(!StringUtils.isBlank(criteriaX.getLastName()) && (!StringUtils.isBlank(criteriaX.getTrnNbr()) || !StringUtils.isBlank(criteriaX.getParishCode()))){
							newJp.setLastName(criteriaX.getLastName());
							newJp.setFirstName(criteriaX.getFirstName());
							System.err.println("Setting Name for Search");
						}else if(!StringUtils.isBlank(criteriaX.getRegNumber())){
							newJp.setRegNumber(criteriaX.getRegNumber());
							System.err.println("Setting Reg Number for Search");
						}else if(!StringUtils.isBlank(criteriaX.getParishCode()) && (StringUtils.isBlank(criteriaX.getTrnNbr())) && StringUtils.isBlank(criteriaX.getLastName())){
							newJp.setParishCode(criteriaX.getParishCode());
							System.err.println("Setting Parish for Search");
						}
						
						context.getFlowScope().put("criteria", newJp);
						search(context);
						
					}else{
						context.getFlowScope().put("criteria", criteriaX);
						search(context);
					}
					Message[] msgs = context.getMessageContext().getAllMessages();
					for(Message m : msgs){
						if(m.getText().equals("No record(s) found.")){
							context.getMessageContext().clearMessages();
						}
					}
			}

			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordUpdated")
							.arg("JP").build());

			// retrieve results from database again
			// search(context);
			return success();
		}catch(InvalidFieldException e) 
		{
			e.printStackTrace();
			context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
			
		}catch(RequiredFieldMissingException e)
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

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("JP ").build());
			return error();
		}

	}

	public Event savejp(RequestContext context) {

		try {
			JPBO jp = (JPBO) context.getFlowScope().get("jp");
			JpBean jpBean = (JpBean)context.getFlowScope().get("jps");
			JPCriteriaBO criteria = (JPCriteriaBO) context
					.getFlowScope().get("criteria");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			if (jp == null)
				throw new Exception("Please enter details.");

			// get user details
			jp.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			
			if (validateArgs(jp, context)) {
				return error();
			} 
			
			if(jp.getPersonBO().getTelephoneCell().equals("(___)___-____")){
				jp.getPersonBO().setTelephoneCell("");
			}
			if(jp.getPersonBO().getTelephoneHome().equals("(___)___-____")){
				jp.getPersonBO().setTelephoneHome("");
			}
			if(jp.getPersonBO().getTelephoneWork().equals("(___)___-____")){
				jp.getPersonBO().setTelephoneWork("");
			}
			
			jp.setStatusId(Constants.Status.ACTIVE);
			
			
			getMaintenanceService().saveJP(jp);
			jp.getPersonBO().setFirstName(jp.getPersonBO().getFirstName());
			jp.getPersonBO().setLastName(jp.getPersonBO().getLastName());
			jp.getPersonBO().setMiddleName(jp.getPersonBO().getMiddleName());
			jp.setRegNumber(jp.getRegNumber());
			jp.setParishDescription(getParishDesc(jp.getParishCode()));
			System.out.println("Parish Name " + jp.getParishDescription() + " jp " + jp.getPersonBO().getFirstName());
			jp.setAuditEntryBO(new AuditEntryBO());
			jp.getAuditEntryBO().setUpdateUsername(usernameToFullName);
			jp.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
			
			if(jpBean.getJpList().size() >= 1){
				
				if(jpBean.getJpList().size() == 1){
					JPCriteriaBO newJp = new JPCriteriaBO();
					
					if((!StringUtils.isBlank(criteria.getTrnNbr())) && ((!StringUtils.isBlank(criteria.getLastName()) || !StringUtils.isBlank(criteria.getParishCode())) || (StringUtils.isBlank(criteria.getLastName()) && StringUtils.isBlank(criteria.getParishCode())))){
						newJp.setTrnNbr(criteria.getTrnNbr());
						System.err.println("Setting TRN for Search");
					}else if(!StringUtils.isBlank(criteria.getLastName()) && (!StringUtils.isBlank(criteria.getTrnNbr()) || !StringUtils.isBlank(criteria.getParishCode()))){
						newJp.setLastName(criteria.getLastName());
						newJp.setFirstName(criteria.getFirstName());
						System.err.println("Setting Name for Search");
					}else if(!StringUtils.isBlank(criteria.getRegNumber())){
						newJp.setRegNumber(criteria.getRegNumber());
						System.err.println("Setting Reg Number for Search");
					}else if(!StringUtils.isBlank(criteria.getParishCode()) && (StringUtils.isBlank(criteria.getTrnNbr())) && StringUtils.isBlank(criteria.getLastName())){
						newJp.setParishCode(criteria.getParishCode());
						System.err.println("Setting Parish for Search");
					}
					
					context.getFlowScope().put("criteria", newJp);
					search(context);
					
				}else{
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}
				
				Message[] msgs = context.getMessageContext().getAllMessages();
				for(Message m : msgs){
					if(m.getText().equals("No record(s) found.")){
						context.getMessageContext().clearMessages();
					}
				}
			}
			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("JP ").build());

			return success();
		}catch(InvalidFieldException e) 
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
							.arg("JP ").build());
			return error();
		}

	}

	//Used to check if required fields are null
	//Returns message to indicate field is required if left null	
	private Boolean validateArgs(JPBO jp, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();
		
		
		if (StringUtils.isBlank(jp.getPersonBO().getTrnNbr())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("TRN").build());
			error = true;

		}else{
			try{
				/*if(jp.getPersonBO().getTrnNbr().length() > 0 && jp.getPersonBO().getTrnNbr().length() < 9){
					messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
					error = true;
					return error;
				}*/
				
				TrnDTO trnDTO = getTRNWebService().getrnDTOByTrn(Integer.parseInt(jp.getPersonBO().getTrnNbr()));
				
					if(trnDTO==null){
						messages.addMessage(new MessageBuilder().error().code("Norecordfound").build());
						error = true;
					}
				else{
						if(trnDTO.isBusinessTrn()){
							messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
							error = true;
						}
					}
				
				
							
			} catch (NumberFormatException e) {
				messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
				e.printStackTrace();
				error = true;
			} catch (InvalidTrnBranchException_Exception e) {
				messages.addMessage(new MessageBuilder().error().code("Norecordfound").build());
					e.printStackTrace();
					error = true;
			} catch (NotValidTrnTypeException_Exception e) {
					messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
					e.printStackTrace();
					error = true;
			}  catch (SystemErrorException_Exception e) {
					messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
					e.printStackTrace();
					error = true;
				}
			 catch (TaxPayerClosedException e) {
				 	addErrorMessage(context, "TRN.Status.Closed");	
					e.printStackTrace();
					error = true;
				} catch (TaxPayerUnintendedException e) {
					addErrorMessage(context, "TRN.Status.Invalid");
					e.printStackTrace();
					error = true;
				} catch (TaxPayerDeceasedException e) {
					addErrorMessage(context, "TRN.Status.Deceased");
					e.printStackTrace();
					error = true;
				} catch (TaxPayerRetiredException e) {
					addErrorMessage(context, "TRN.Status.Retired");					
					e.printStackTrace();
					error = true;
				} catch (InvalidTaxPayerException e) {
					addErrorMessage(context, "TRN.Status.Invalid");
					e.printStackTrace();
					error = true;
				}
			catch(Exception ex)
			{
				messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
				ex.printStackTrace();
				error = true;
			}
		}
		
		if(!StringUtils.isBlank(getTrnHitVal())) {
			if(getTrnHitVal().length() > 1 && jp.getPersonBO().getTrnNbr().length() > 1)
			{
				if(!getTrnHitVal().equals(jp.getPersonBO().getTrnNbr())){
					Message[] msgs = messages.getAllMessages();
					for(Message m : msgs){
						if(m.getText().equals("No record found.")){
							messages.clearMessages();
						}
					}					
					messages.addMessage(new MessageBuilder().error().defaultText("TRN does not match user details").build());
					error = true;
				}
			}
		}
		
		
		if (StringUtils.isBlank(jp.getPersonBO().getFirstName()) && StringUtils.isBlank(jp.getPersonBO().getLastName())) {
			messages.addMessage(new MessageBuilder().error()
					.defaultText("Last Name and First Name are required.").build());
			/*messages.addMessage(new MessageBuilder().error()
					.defaultText("Please search using the TRN.").build());*/
			error = true;
		}
		
		if (StringUtils.isBlank(jp.getRegNumber())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Registration Number").build());
			error = true;

		} 
		
				
		if (!(StringUtils.isBlank(jp.getPersonBO().getTelephoneHome())) && (jp.getPersonBO().getTelephoneHome().length() < 13)) {
			messages.addMessage(new MessageBuilder().error()
					.code("Telephone").arg("Home").build());
			error = true;

		} 
		
		if (!(StringUtils.isBlank(jp.getPersonBO().getTelephoneCell())) && (jp.getPersonBO().getTelephoneCell().length() < 13)) {
			messages.addMessage(new MessageBuilder().error()
					.code("Telephone").arg("Cell").build());
			error = true;

		} 
		
		if (!(StringUtils.isBlank(jp.getPersonBO().getTelephoneWork())) && (jp.getPersonBO().getTelephoneWork().length() < 13)) {
			messages.addMessage(new MessageBuilder().error()
					.code("Telephone").arg("Work").build());
			error = true;

		} 
					
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView jpAddress = new AddressView(jp.getPersonBO().getMarkText(), jp.getParishCode(), jp.getPersonBO().getPoBoxNo(),
				jp.getPersonBO().getPoLocationName(), jp.getPersonBO().getStreetName(), jp.getPersonBO().getStreetNo(),
				null, null, null,jp.getParishDescription());
		
		//global address validation - kpowell
		boolean errorFoundInAddress  = validateAddress(context, jpAddress);
        
        if(errorFoundInAddress){
              error = true;
        }
		//
        
		if (StringUtils.isBlank(jp.getCurrentUsername())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("User Name ").build());
			error = true;
		}
		
		if (StringUtils.isBlank(jp.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status ").build());
			error = true;
		}
		
		if ((!StringUtils.isBlank(jp.getNewPin())) || (!StringUtils.isBlank(jp.getConfirmedPin()))) {
			if(!jp.getNewPin().equals(jp.getConfirmedPin()))
			{
				System.out.println("PiN 1" + jp.getNewPin() +" "+jp.getConfirmedPin());
				messages.addMessage(new MessageBuilder().error()
						.code("PinMisMatch").build());
				jp.setNewPin("");
				jp.setConfirmedPin("");
				context.getFlowScope().put("jp", jp);
				error = true;
			}
		}
		return error;
	}

	public void canceljp() {
		System.out.println("cancel has fired");
		//context.getFlowScope().put("criteria", new ArteryCriteriaBO());
		//return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		context.getViewScope().clear();
		return success();

	}
	
	
	public void clearDetails(RequestContext context) {
		JPBO JPBO = (JPBO) context.getFlowScope().get("jp");
		JPBO.setStatusId("ACT");
		
		PersonBO newPerson = new PersonBO();
		
		newPerson.setTrnNbr("");
		newPerson.setFirstName("");
		newPerson.setLastName("");
		newPerson.setMiddleName("");
		
		JPBO.setPersonBO(newPerson);
		
		JPBO.setParishCode("");
		JPBO.setRegNumber("");
		context.getFlowScope().put("jp", JPBO);
	}

	public Maintenance getMaintenanceService() {
		return jpMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance jpMaintenanceService) {
		this.jpMaintenanceService = jpMaintenanceService;
	}

	public String getTrnHitVal() {
		return trnHitVal;
	}

	public void setTrnHitVal(String trnHitVal) {
		this.trnHitVal = trnHitVal;
	}

	public String getConcatName() {
		return concatName;
	}

	public void setConcatName(String concatName) {
		this.concatName = concatName;
	}
	
	
	
}
