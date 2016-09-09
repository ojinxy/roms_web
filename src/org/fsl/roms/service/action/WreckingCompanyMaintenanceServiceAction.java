package org.fsl.roms.service.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.view.AddressView;
import org.fsl.roms.view.WreckingCompanyBean;
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
import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.WreckingCompanyCriteriaBO;
import fsl.ta.toms.roms.trnwebservice.InvalidTrnBranchException_Exception;
import fsl.ta.toms.roms.trnwebservice.NotValidTrnTypeException_Exception;
import fsl.ta.toms.roms.trnwebservice.RetiredTRNException_Exception;
import fsl.ta.toms.roms.trnwebservice.SystemErrorException_Exception;
import fsl.ta.toms.roms.trnwebservice.TrnDTO;
import fsl.ta.toms.roms.webservices.Maintenance;
/**
 * 
 * @author bssintern
 *
 */

@Service
public class WreckingCompanyMaintenanceServiceAction extends BaseServiceAction {

	@Autowired
	private Maintenance wreckingcompanyMaintenanceService;
	private String trnHitVal;
	public WreckingCompanyMaintenanceServiceAction() {
		super();
	}
	
	public WreckingCompanyMaintenanceServiceAction(
			Maintenance wreckingcompanyMaintenanceService) {
		this.wreckingcompanyMaintenanceService = wreckingcompanyMaintenanceService;
	}
	
	/**
	 * This function searches based on Wrecking Company criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {

		List<WreckingCompanyBO> wreckingCompanies = null;

		try {
			String trnNbr = "";
			WreckingCompanyCriteriaBO criteria = (WreckingCompanyCriteriaBO) context
					.getFlowScope().get("criteria");

			System.err.println("Criteria" + criteria.getParishCode() + "stat " + criteria.getStatusId() );
			// do the search
			
					
			wreckingCompanies = getMaintenanceService().lookupWreckingCompany(criteria);
			
		
			
			System.err.println("after look up");
			
			if (wreckingCompanies == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordfound")
								.build());
				context.getFlowScope().put("wreckingCompanies",
						new WreckingCompanyBean());
				return success();

			} else if (wreckingCompanies.size() == 1) {
				trnNbr = wreckingCompanies.get(0).getTrnNbr();
				wreckingCompanies.get(0).setTrnNbr(doTRNBumpUp(trnNbr));
				
				context.getFlowScope().put("wreckingCompany", wreckingCompanies.get(0));

				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("wreckingCompanies",
						new WreckingCompanyBean(wreckingCompanies));
				System.err.println("WRECKING COMPANY INFO: " + wreckingCompanies.get(0).getCompanyName());
				return yes();
			}
			
			for(WreckingCompanyBO comp : wreckingCompanies)
			{
				trnNbr = comp.getTrnNbr();
				comp.setTrnNbr(doTRNBumpUp(trnNbr));
			}
			// set the entire list in datatable
			context.getFlowScope().put("wreckingCompanies",
					new WreckingCompanyBean(wreckingCompanies));

		}catch(NoRecordFoundException nRec)
		{
			context.getMessageContext()
			.addMessage(
					new MessageBuilder().error().code("Norecordfound")
							.build());
			
			context.getFlowScope().put("wreckingCompanies",
					new WreckingCompanyBean());
			return error();
		}
		catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("wreckingCompanies",
					new WreckingCompanyBean());
			return error();
		}
		return success();
	}
	
	
	public Event searchTrn(RequestContext context){
		try{
			WreckingCompanyBO wrecking = (WreckingCompanyBO) context.getFlowScope().get("wreckingCompany");
			String onUpdateAction = (String)context.getFlowScope().get("updatehideBtn");
			System.err.println("wrecking from Criteria " + wrecking.getTrnNbr() +" branch: "+wrecking.getTrnBranch());
			String origTRN = wrecking.getTrnNbr();
			Integer trnNbr;
			short branchNbr;
			
			if(StringUtils.isBlank(wrecking.getTrnNbr()) && StringUtils.isBlank(wrecking.getTrnBranch())){
				addErrorMessageWithParameter(context,  "RequiredFields.Maintenance", "TRN");
				addErrorMessageWithParameter(context,  "RequiredFields.Maintenance", "TRN Branch");
				return error();
			}
			
			if(StringUtils.isBlank(wrecking.getTrnNbr())){
				addErrorMessageWithParameter(context,  "RequiredFields.Maintenance", "TRN");
				return error();
			}else{
				trnNbr = Integer.parseInt(wrecking.getTrnNbr());
			}
			
			if(StringUtils.isBlank(wrecking.getTrnBranch())){	
				addErrorMessageWithParameter(context,  "RequiredFields.Maintenance", "TRN Branch");
				return error();
			}else{
				branchNbr = Short.parseShort(wrecking.getTrnBranch());
			}
			
			
			//Checks if TRN & Branch Exists in ROMS when doing an add
			if(trnAndBranchExistsInROMS(wrecking) && !StringUtils.isEmpty(onUpdateAction)){
				addErrorMessageText(context,"Another Wrecker Company exists with selected TRN and Branch");
				add(context);
				return error();
			}
			
			System.err.println("wrecking trn " + trnNbr +" branch: "+branchNbr + " Does it exists in ROMS " + trnAndBranchExistsInROMS(wrecking));
			
			
			
			WreckingCompanyBO wreTrnBO;
			wreTrnBO = getTRNWebService().getWreckingBOByTrn(trnNbr, branchNbr);
			
			
			TrnDTO trnDTO = getTRNWebService().getrnDTOByTrn(trnNbr);
			
			
			
			if(trnDTO==null || wreTrnBO == null){
				addErrorMessage(context, "Norecordfound");
				add(context);
				return error();
			}
			else{
			
				if((trnDTO.isIndividualTrn()) && (branchNbr == 0)){
					addErrorMessageText(context, "Invalid TRN");
					add(context);
					//addErrorMessageText(context, new MessageBuilder().defaultText(""));
					return error();
				}
			
				if(wreTrnBO.getCompanyName()!=null){
					wrecking.setCompanyName(WordUtils.capitalize(wreTrnBO.getCompanyName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
					setTrnHitVal(origTRN);//Set TRN value for search for name if valid user is returned
				}
				
				if(wreTrnBO.getTrnBranch()!=null){
					if(!onUpdateAction.isEmpty())
						wrecking.setTrnBranch(WordUtils.capitalize(wreTrnBO.getTrnBranch().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				
				/*if(wreTrnBO.getShortDesc()!=null){
					wrecking.setShortDesc(WordUtils.capitalize(wreTrnBO.getShortDesc().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}*/
				
				if(wreTrnBO.getStreetName()!=null){
					if(!onUpdateAction.isEmpty())
						wrecking.setStreetName(WordUtils.capitalize(wreTrnBO.getStreetName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(wreTrnBO.getStreetNo()!=null){
					if(!onUpdateAction.isEmpty())
						wrecking.setStreetNo(WordUtils.capitalize(wreTrnBO.getStreetNo().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(wreTrnBO.getPoBoxNo()!=null){
					if(!onUpdateAction.isEmpty())
						wrecking.setPoBoxNo(WordUtils.capitalize(wreTrnBO.getPoBoxNo().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(wreTrnBO.getParishCode()!=null){
					if(!onUpdateAction.isEmpty())
						wrecking.setParishCode(WordUtils.capitalize(wreTrnBO.getParishCode().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(wreTrnBO.getMarkText()!=null){
					if(!onUpdateAction.isEmpty())
						wrecking.setMarkText(WordUtils.capitalize(wreTrnBO.getMarkText().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				if(wreTrnBO.getPoLocationName()!=null){
					if(!onUpdateAction.isEmpty())
						wrecking.setPoLocationName(WordUtils.capitalize(wreTrnBO.getPoLocationName().toLowerCase(), Constants.STRING_DELIM_ARRAY));
				}
				
				
				
				
			}
				System.out.println("Wrecking  " + wrecking.getCompanyName() +" "+wrecking.getTrnBranch());
				
				context.getFlowScope().put("wreckingCompany", wrecking);
			
			
				return success();
				
			} catch (NumberFormatException e) {
				addErrorMessageText(context, "Invalid TRN");
				add(context);
				e.printStackTrace();
				return error();
			} catch (InvalidTrnBranchException_Exception e) {
					addErrorMessageText(context, "Invalid TRN");
					add(context);
					e.printStackTrace();
					return error();
			} catch (NotValidTrnTypeException_Exception e) {
					addErrorMessageText(context, "Invalid TRN");
					add(context);
					e.printStackTrace();
					return error();
			}  catch (SystemErrorException_Exception e) {
					addErrorMessageText(context, "TRN validation failed");
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
	
	public Boolean trnAndBranchExistsInROMS(WreckingCompanyBO wreckerCoBO){
		//Checks if TRN Exists in ROMS
		try {
			boolean answer = getMaintenanceService().wreckerCoTRNAndBranchExists(wreckerCoBO);
			if(answer)
				return true;
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Event updatewreckingCompany(RequestContext context) {

		try {
			WreckingCompanyBO wreckingcompany = (WreckingCompanyBO) context.getFlowScope().get("wreckingCompany");
			WreckingCompanyCriteriaBO criteria = (WreckingCompanyCriteriaBO) context
					.getFlowScope().get("criteria");
			WreckingCompanyBean wreckingCompanyBean = (WreckingCompanyBean)context.getFlowScope().get("wreckingCompanies");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			if (wreckingcompany == null)
				throw new Exception("Please enter valid details.");

			// get user details
			wreckingcompany.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			System.err.println("Telephone " + wreckingcompany.getTelephoneWork() + "Telephone size" + wreckingcompany.getTelephoneWork().length());
			
			
			if (validateArgs(wreckingcompany, context)) {
				return error();
			} 
			
			
			if(wreckingcompany.getTrnNbr().length() < 9){
				wreckingcompany.setTrnNbr(doTRNBumpUp(wreckingcompany.getTrnNbr()));
				System.err.println("TRN to be saved after bump up " + wreckingcompany.getTrnNbr());
			}
			getMaintenanceService().updateWreckingCompany(wreckingcompany);
			wreckingcompany.setAuditEntryBO(new AuditEntryBO());
			wreckingcompany.getAuditEntryBO().setUpdateUsername(usernameToFullName);
			if(wreckingCompanyBean.getWreckingList().size() > 1){
				context.getFlowScope().put("criteria", criteria);
				search(context);
			}else{
				//WreckingCompanyCriteriaBO wreckingCrit = new WreckingCompanyCriteriaBO();
				//wreckingCrit.setWreckingCompanyId(wreckingcompany.getWreckingCompanyId());
				//context.getFlowScope().put("criteria", wreckingCrit);
				//search(context);
			}

			context.getFlowScope().put("wreckingCompany", wreckingcompany);
			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordUpdated")
							.arg("Wrecker Company").build());

			// retrieve results from database again
			// search(context);
			return success();
		}catch (RequiredFieldMissingException ex){
			ex.printStackTrace();
			context.getMessageContext().addMessage(					
					new MessageBuilder().error().defaultText(ex.getMessage()).build());
			return error();
		}catch (Exception e) {

			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("Wrecker Company").build());
			return error();
		}

	}
	
	public void select(RequestContext context){
		System.err.println("select called");
		
		try {
			WreckingCompanyBean wreckingCompanyBean = (WreckingCompanyBean)context.getFlowScope().get("wreckingCompanies");
			System.err.println("Wrecking: "+wreckingCompanyBean.getSelectedWreckingCompany().getCompanyName()+" id:"+wreckingCompanyBean.getSelectedWreckingCompany().getWreckingCompanyId() +"trn "+ wreckingCompanyBean.getSelectedWreckingCompany().getTrnNbr());
			
			String trnNbr = wreckingCompanyBean.getSelectedWreckingCompany().getTrnNbr();
			wreckingCompanyBean.getSelectedWreckingCompany().setTrnNbr(doTRNBumpUp(trnNbr));
			setTrnHitVal(wreckingCompanyBean.getSelectedWreckingCompany().getTrnNbr());
			context.getFlowScope().put("wreckingCompany", wreckingCompanyBean.getSelectedWreckingCompany());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void back(RequestContext context){
		WreckingCompanyCriteriaBO criteria = (WreckingCompanyCriteriaBO) context
				.getFlowScope().get("criteria");
		context.getFlowScope().put("criteria",criteria);
		
		WreckingCompanyBean wcBean = (WreckingCompanyBean) context.getFlowScope().get("wreckingCompanies");
		
		if(wcBean.getWreckingList().size() > 0)
		{
			search(context);
		}
	}
	
	public String doTRNBumpUp(String trnNbr) {
		int trnLen = trnNbr.length();
		int loopLen = 0;
		StringBuilder bumpUp = new StringBuilder();
		String[] incrementVal = {"0","0","0","0","0","0","0","0"};
		//Check if TRN is less than 9 digits
		if(trnLen < 9 ){
			loopLen = 9 - trnLen;
			for(int x = 0; x < loopLen;x++){
				//Add the missing zeros
				bumpUp.append(incrementVal[x]);
			}
			//Append the missing zeros to TRN
			bumpUp.append(trnNbr);
			//set new TRN value
			trnNbr = bumpUp.toString();
		}
		//returns trn value 
		return trnNbr;
	}

	public void add(RequestContext context){
		System.err.println("add called");
		
		try {
			//WreckingCompanyBean wreckingCompanyBean = (WreckingCompanyBean)context.getFlowScope().get("wreckingCompanies");
			//System.err.println("Wrecking: "+wreckingCompanyBean.getSelectedWreckingCompany().getCompanyName()+" id:"+wreckingCompanyBean.getSelectedWreckingCompany().getWreckingCompanyId() +"trn "+ wreckingCompanyBean.getSelectedWreckingCompany().getTrnNbr());
			WreckingCompanyBO wreckingCompanyBO = new WreckingCompanyBO();
			wreckingCompanyBO.setTrnBranch("0");
			wreckingCompanyBO.setStatusId("ACT");
			context.getFlowScope().put("wreckingCompany", wreckingCompanyBO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Event savewreckingCompany(RequestContext context) {

		try {
			WreckingCompanyBO wreckingcompany = (WreckingCompanyBO) context.getFlowScope().get("wreckingCompany");
			WreckingCompanyCriteriaBO criteria = (WreckingCompanyCriteriaBO) context
					.getFlowScope().get("criteria");
			WreckingCompanyBean wreckingCompanyBean = (WreckingCompanyBean)context.getFlowScope().get("wreckingCompanies");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			if (wreckingcompany == null)
				throw new Exception("Please enter details.");

			// get user details
			wreckingcompany.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			wreckingcompany.setStatusId(Constants.Status.ACTIVE);
			
			if (validateArgs(wreckingcompany, context)) {
				return error();
			} 
			
			
				System.err.println("Com name:" + wreckingcompany.getCompanyName() + "descript: " + wreckingcompany.getShortDesc() + "name "+ wreckingcompany.getContactPersonFirstName() +" "+wreckingcompany.getContactPersonLastName()+"trn "+wreckingcompany.getTrnNbr() + "branch " + wreckingcompany.getTrnBranch() +"parish "+wreckingcompany.getParishCode());
			if(wreckingcompany.getTrnNbr().length() < 9){
				wreckingcompany.setTrnNbr(doTRNBumpUp(wreckingcompany.getTrnNbr()));
				System.err.println("TRN to be saved after bump up " + wreckingcompany.getTrnNbr());
			}
				
				getMaintenanceService().saveWreckingCompany(wreckingcompany);
				wreckingcompany.setAuditEntryBO(new AuditEntryBO());
				wreckingcompany.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				if(wreckingCompanyBean.getWreckingList().size() >= 1){
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}else{
					WreckingCompanyCriteriaBO wreckingCrit = new WreckingCompanyCriteriaBO();
					wreckingCrit.setWreckingCompanyId(wreckingcompany.getWreckingCompanyId());
					context.getFlowScope().put("criteria", wreckingCrit);
					search(context);
				}
			
			context.getFlowScope().put("wreckingCompany", wreckingcompany);
			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("Wrecker Company").build());

			return success();
		}catch(RequiredFieldMissingException e){
			e.printStackTrace();
			context.getMessageContext().addMessage(

					new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
		}
		
		catch (Exception e) {
			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("Wrecker Company").build());
			return error();
		}

	}

	//Used to check if required fields are null
	//Returns message to indicate field is required if left null
	private Boolean validateArgs(WreckingCompanyBO wreckingcompany, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();
		int trn = 0;
		Short branchNbr = 0;
				
		if (StringUtils.isBlank(wreckingcompany.getTrnNbr())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("TRN").build());
			error = true;

		}else if (StringUtils.isBlank(wreckingcompany.getTrnBranch())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("TRN Branch").build());
			error = true;

		}else{
				
			try {
				trn = Integer.parseInt(wreckingcompany.getTrnNbr());
				branchNbr = Short.parseShort(wreckingcompany.getTrnBranch()); 
				
				WreckingCompanyBO wreTrnBO;
				wreTrnBO = getTRNWebService().getWreckingBOByTrn(trn, branchNbr);
				
				
				TrnDTO trnDTO = getTRNWebService().getrnDTOByTrn(trn);
				
				System.out.println("Inside TRN else " + trn +" "+branchNbr +" trn trn" +  trnDTO.getNbrTrn()+ " wrecking branch" + wreTrnBO.getTrnBranch());
				
				if(trnDTO==null || wreTrnBO == null){
					messages.addMessage(new MessageBuilder().error().arg("Norecordfound").build());
					error = true;
				}
				else{
				
					if((trnDTO.isIndividualTrn()) && (branchNbr == 0)){
						messages.addMessage(new MessageBuilder().error()
								.defaultText("Invalid TRN").build());
						error = true;
					}
				}
			} catch (InvalidTrnBranchException_Exception e) {
				// TODO Auto-generated catch block
				messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
				e.printStackTrace();
				error = true;
			} catch (NotValidTrnTypeException_Exception e) {
				// TODO Auto-generated catch block
				messages.addMessage(new MessageBuilder().error().arg("Norecordfound").build());
				e.printStackTrace();
				error = true;
			} catch (SystemErrorException_Exception e) {
				// TODO Auto-generated catch block
				messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
				e.printStackTrace();
				error = true;
			} catch (RequiredFieldMissingException e) {
				// TODO Auto-generated catch block
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
			catch (RetiredTRNException_Exception e) {
				// TODO Auto-generated catch block
				 messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
				e.printStackTrace();
				error = true;
			}catch  (Exception ex){
				 messages.addMessage(new MessageBuilder().error().defaultText("Invalid TRN").build());
				 ex.printStackTrace();
				 error = true;
			 }
			
		}
		System.out.println("TRN HIT Val " + getTrnHitVal() +" wrecking TRN "+wreckingcompany.getTrnNbr());
		if(!StringUtils.isBlank(getTrnHitVal())) {
			if(getTrnHitVal().length() > 1 && wreckingcompany.getTrnNbr().length() > 1)
			{
				if(!doTRNBumpUp(getTrnHitVal()).equals(doTRNBumpUp(wreckingcompany.getTrnNbr()))){
					System.out.println("TRN HIT Val " + getTrnHitVal() +" wrecking TRN "+wreckingcompany.getTrnNbr());
					messages.addMessage(new MessageBuilder().error().defaultText("TRN does not match company details").build());
					error = true;
				}
			}
		}
		
		
		
		if (StringUtils.isBlank(wreckingcompany.getCompanyName())) {
			messages.addMessage(new MessageBuilder().error()
					.defaultText("Company Name is required.").build());
			/*messages.addMessage(new MessageBuilder().error()
					.defaultText("Please search using the TRN and TRN Branch.").build());*/
			error = true;
		}
				
		if (StringUtils.isBlank(wreckingcompany.getContactPersonFirstName())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Contact Person's First Name").build());
			error = true;
		} 
		if (StringUtils.isBlank(wreckingcompany.getContactPersonLastName())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Contact Person's Last Name").build());
			error = true;
		} 
		
		/*if (StringUtils.isBlank(wreckingcompany.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status").build());
			error = true;
		} */
	

		System.err.println("Telephone " + wreckingcompany.getTelephoneWork());	
		if (!StringUtils.isBlank(wreckingcompany.getTelephoneCell()) && wreckingcompany.getTelephoneCell().length() < 13) {
			messages.addMessage(new MessageBuilder().error()
					.code("Telephone").arg("Cell").build());
			error = true;
		} 
		
		if (StringUtils.isBlank(wreckingcompany.getTelephoneWork())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Work Number").build());
			error = true;
		}else if(wreckingcompany.getTelephoneWork().length() < 13){
			messages.addMessage(new MessageBuilder().error()
					.code("Telephone").arg("Work").build());
			error = true;
		}
		
		
		if (StringUtils.isBlank(wreckingcompany.getCurrentUsername())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("User Name ").build());
			error = true;
		} 
		
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView wreckingcompAddress = new AddressView(wreckingcompany.getMarkText(), wreckingcompany.getParishCode(), wreckingcompany.getPoBoxNo(),
				wreckingcompany.getPoLocationName(), wreckingcompany.getStreetName(), wreckingcompany.getStreetNo(),
				null, null, null,null);
		
		//global address validation - kpowell
		boolean errorFoundInAddress  = validateAddress(context, wreckingcompAddress);
        
        if(errorFoundInAddress){
              error = true;
        }
		return error;
	}
	
	
	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("wreckingCompanies");
		/*
		 * context.getFlowScope().put("wreckingCompanies", new
		 * WreckingCompanyBean());
		 */
		// wreckingCompanies= new ArrayList<WreckingCompanyBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		context.getViewScope().clear();
		return success();

	}
	
	public Maintenance getMaintenanceService() {
		return wreckingcompanyMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance wreckingcompanyMaintenanceService) {
		this.wreckingcompanyMaintenanceService = wreckingcompanyMaintenanceService;
	}

	public String getTrnHitVal() {
		return trnHitVal;
	}

	public void setTrnHitVal(String trnHitVal) {
		this.trnHitVal = trnHitVal;
	}
	
	
	
}
