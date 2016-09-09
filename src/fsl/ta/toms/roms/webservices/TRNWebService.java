/**
 * Created By: oanguin
 * Date: May 16, 2013
 *
 */
package fsl.ta.toms.roms.webservices;

import java.io.Serializable;

import org.fsl.trn.exceptions.InvalidTaxPayerException;
import org.fsl.trn.exceptions.TaxPayerClosedException;
import org.fsl.trn.exceptions.TaxPayerDeceasedException;
import org.fsl.trn.exceptions.TaxPayerRetiredException;
import org.fsl.trn.exceptions.TaxPayerUnintendedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.bo.TrnBoDTO;
import fsl.ta.toms.roms.bo.WreckingCompanyBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.trnwebservice.BusinessDTO;
import fsl.ta.toms.roms.trnwebservice.InvalidTrnBranchException_Exception;
import fsl.ta.toms.roms.trnwebservice.NotValidTrnTypeException_Exception;
import fsl.ta.toms.roms.trnwebservice.RetiredTRNException_Exception;
import fsl.ta.toms.roms.trnwebservice.SystemErrorException_Exception;
import fsl.ta.toms.roms.trnwebservice.TrnDTO;
import fsl.ta.toms.roms.trnwebservice.TrnValidatorSEI;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: May 16, 2013
 * 
 * 
 * 
 */
@Controller
@RequestMapping("/roms_rest/trn")
public class TRNWebService extends SpringBeanAutowiringSupport implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	TrnValidatorSEI trnService;
	
	@RequestMapping("/getPersonBOByTrn")
	public @ResponseBody PersonBO getPersonBOByTrn(@RequestParam Integer trn)
			throws InvalidTrnBranchException_Exception,
			NotValidTrnTypeException_Exception, SystemErrorException_Exception,
			RequiredFieldMissingException, TaxPayerClosedException,
			TaxPayerDeceasedException, TaxPayerRetiredException,
			TaxPayerUnintendedException, InvalidTaxPayerException
	{
		
		this.trnNotNull(trn);
		
		TrnDTO personDTO = trnService.validateTRN(trn,(short) 0);
		
		validateTrnStatus(personDTO);
	
		PersonBO personBO = new PersonBO(personDTO.getNbrTrn(), 
									   personDTO.getFirstName(), 
									   personDTO.getMiddleName(), 
									   personDTO.getLastName(), 
									   ""/*telephoneHome*/, 
									   ""/*telephoneCell*/, 
									   ""/*telephoneWork*/, 
									   personDTO.getAddrMarkText()/*markText*/, 
									   personDTO.getAddrStreetNo()/*streetNo*/, 
									   personDTO.getAddrStreetName()/*streetName*/, 
									   personDTO.getAddrPoLocName()/*poLocationName*/, 
									   personDTO.getAddrPoBoxNo()/*poBoxNo*/, 
									   personDTO.getAddrParishCode()/*parishCode*/, 
									   ""/*currentUser*/,
									   personDTO.getAddrParishDesc()/*Parish Desc.*/);
		
		return personBO;
	}
	
	@RequestMapping("/getWreckingBOByTrn")
	public @ResponseBody WreckingCompanyBO getWreckingBOByTrn(@RequestParam Integer trnNbr, @RequestParam short branchNbr)
			throws InvalidTrnBranchException_Exception,
			NotValidTrnTypeException_Exception, SystemErrorException_Exception,
			RetiredTRNException_Exception, TaxPayerClosedException,
			TaxPayerDeceasedException, TaxPayerRetiredException,
			TaxPayerUnintendedException, InvalidTaxPayerException
	{
		BusinessDTO wreDto = trnService.getBusiness(trnNbr, branchNbr);
		validateWreckingTrnStatus(wreDto);
		
		//TrnDTO personDTO = trnService.validateTRN(trnNbr,(short) 0);
		//BusinessDetailsDTO busDetails = trnService.getBusinessDetails(trnNbr);
		
		WreckingCompanyBO wreckingBO = new WreckingCompanyBO();
		wreckingBO.setCompanyName(wreDto.getBusinessName());
		
		if(StringUtil.isSet(wreDto.getTradeName())) //needed to get the trade name of the business if set
		{
				wreckingBO.setCompanyName(wreDto.getTradeName());
			
		}
		
		
		wreckingBO.setTrnBranch(wreDto.getNbrBranch());
		wreckingBO.setTrnNbr(wreDto.getNbrTrn());
		wreckingBO.setShortDesc(wreDto.getNatureOfBusiness());
		
		wreckingBO.setParishCode(wreDto.getHmParishCode());
		wreckingBO.setStreetName(wreDto.getHmStreetName());
		wreckingBO.setStreetNo(wreDto.getHmStreetNo());
		wreckingBO.setPoBoxNo(wreDto.getHmPoBoxNo());
		wreckingBO.setMarkText(wreDto.getHmMarkText());
		wreckingBO.setPoLocationName(wreDto.getHmPoLocName());
		
		
		return wreckingBO;
	}
	
	@RequestMapping("/getrnDTOByTrn")
	public @ResponseBody TrnDTO getrnDTOByTrn( @RequestParam Integer trn)
			throws InvalidTrnBranchException_Exception,
			NotValidTrnTypeException_Exception, SystemErrorException_Exception,
			RequiredFieldMissingException, TaxPayerClosedException,
			TaxPayerDeceasedException, TaxPayerRetiredException,
			TaxPayerUnintendedException, InvalidTaxPayerException
	{
		this.trnNotNull(trn);
		
		TrnDTO trnDTO = trnService.validateTRN(trn,(short) 0); 
		
		validateTrnStatus(trnDTO);
		
		return trnDTO;
	
		
	}
	
	@RequestMapping("/getrnBoDTOByTrn")
	public @ResponseBody TrnBoDTO getrnBoDTOByTrn( @RequestParam Integer trn)
			throws InvalidTrnBranchException_Exception,
			NotValidTrnTypeException_Exception, SystemErrorException_Exception,
			RequiredFieldMissingException, TaxPayerClosedException,
			TaxPayerDeceasedException, TaxPayerRetiredException,
			TaxPayerUnintendedException, InvalidTaxPayerException
	{
		this.trnNotNull(trn);
		
		TrnDTO trnDTO = trnService.validateTRN(trn,(short) 0); 
		
		validateTrnStatus(trnDTO);
		
		TrnBoDTO trnBoDTO = new TrnBoDTO();
		trnBoDTO.setAddrCountryCode(trnDTO.getAddrCountryCode());
		trnBoDTO.setAddrCountryDesc(trnDTO.getAddrCountryDesc());
		trnBoDTO.setAddress(trnDTO.getAddress());
		trnBoDTO.setAddrMarkText(trnDTO.getAddrMarkText());
		trnBoDTO.setAddrParishCode(trnDTO.getAddrParishCode());
		trnBoDTO.setAddrParishDesc(trnDTO.getAddrParishDesc());
		trnBoDTO.setAddrPoBoxNo(trnDTO.getAddrPoBoxNo());
		trnBoDTO.setAddrPoLocName(trnDTO.getAddrPoLocName());
		trnBoDTO.setAddrStreetName(trnDTO.getAddrStreetName());
		trnBoDTO.setAddrStreetNo(trnDTO.getAddrStreetNo());
		trnBoDTO.setBadChequeId(trnDTO.getBadChequeId());
		trnBoDTO.setBirthFirstName(trnDTO.getBirthFirstName());
		trnBoDTO.setBirthLastName(trnDTO.getBirthLastName());
		trnBoDTO.setBirthMiddleName(trnDTO.getBirthMiddleName());
		trnBoDTO.setBranchRefNbr(trnDTO.getBranchRefNbr());
		trnBoDTO.setBusinessTrn(trnDTO.isBusinessTrn());
		trnBoDTO.setFirstName(trnDTO.getFirstName());
		trnBoDTO.setFlgStatus(trnDTO.getFlgStatus());
		trnBoDTO.setFlgTrnType(trnDTO.getFlgTrnType());
		trnBoDTO.setFormattedName(trnDTO.getFormattedName());
		trnBoDTO.setGenderType(trnDTO.getGenderType());
		trnBoDTO.setIndividualTrn(trnDTO.isIndividualTrn());
		trnBoDTO.setLastName(trnDTO.getLastName());
		trnBoDTO.setLastUpdate(trnDTO.getLastUpdate() != null ? trnDTO.getLastUpdate().toGregorianCalendar().getTime() : null);
		trnBoDTO.setMaritalStat(trnDTO.getMaritalStat());
		trnBoDTO.setMiddleName(trnDTO.getMiddleName());
		trnBoDTO.setNbrBranch(trnDTO.getNbrBranch());
		trnBoDTO.setNbrReferenceTrn(trnDTO.getNbrReferenceTrn());
		trnBoDTO.setNbrTrn(trnDTO.getNbrTrn());
		trnBoDTO.setRegistered(trnDTO.getRegistered());
		trnBoDTO.setRpdAlert(trnDTO.getRpdAlert());
		trnBoDTO.setTransId(trnDTO.getTransId());
		trnBoDTO.setTrnEntryDate(trnDTO.getTrnEntryDate() != null ? trnDTO.getTrnEntryDate().toGregorianCalendar().getTime() : null);
		trnBoDTO.setTrnEntryStatus(trnDTO.getTrnEntryStatus());
		
		return trnBoDTO;
	
		
	}
	
	private void trnNotNull(Integer trn) throws RequiredFieldMissingException
	{
		if(trn == null)
			throw new  RequiredFieldMissingException();
	}
	
	private void validateTrnStatus(TrnDTO trnDTO) throws TaxPayerClosedException, TaxPayerDeceasedException, 
												TaxPayerRetiredException, TaxPayerUnintendedException, InvalidTaxPayerException{
		
		//String message = "Invalid TRN status: ";
		
		//System.err.println("Trn-Status :"+Constants.TRNStatus.CLOSED+" trn Status: "+trnDTO.getFlgStatus());
		if(Constants.TRNStatus.CLOSED.equalsIgnoreCase(trnDTO.getFlgStatus())){
			throw new TaxPayerClosedException();
		}
		else if(Constants.TRNStatus.DECEASED.equalsIgnoreCase(trnDTO.getFlgStatus())){
			throw new TaxPayerDeceasedException();
			}
		else if(Constants.TRNStatus.EXPIRED.equalsIgnoreCase(trnDTO.getFlgStatus())){
			throw new InvalidTaxPayerException();
		}
		else if(Constants.TRNStatus.RETIRED.equalsIgnoreCase(trnDTO.getFlgStatus())){
			throw new TaxPayerRetiredException();
		}
		else if(Constants.TRNStatus.UNINTENDED.equalsIgnoreCase(trnDTO.getFlgStatus())){
			throw new TaxPayerUnintendedException();
		}
		
		
	}
	
	private void validateWreckingTrnStatus(BusinessDTO btrnDTO)
			throws TaxPayerClosedException, TaxPayerDeceasedException,
			TaxPayerRetiredException, TaxPayerUnintendedException,
			InvalidTaxPayerException {

		// String message = "Invalid TRN status: ";

		// System.err.println("Trn-Status :"+Constants.TRNStatus.CLOSED+" trn Status: "+trnDTO.getFlgStatus());
		if (Constants.TRNStatus.CLOSED.equalsIgnoreCase(btrnDTO.getFlgStatus())) {
			throw new TaxPayerClosedException();
		} else if (Constants.TRNStatus.DECEASED.equalsIgnoreCase(btrnDTO
				.getFlgStatus())) {
			throw new TaxPayerDeceasedException();
		} else if (Constants.TRNStatus.EXPIRED.equalsIgnoreCase(btrnDTO
				.getFlgStatus())) {
			throw new InvalidTaxPayerException();
		} else if (Constants.TRNStatus.RETIRED.equalsIgnoreCase(btrnDTO
				.getFlgStatus())) {
			throw new TaxPayerRetiredException();
		} else if (Constants.TRNStatus.UNINTENDED.equalsIgnoreCase(btrnDTO
				.getFlgStatus())) {
			throw new TaxPayerUnintendedException();
		}

	}
	
}
