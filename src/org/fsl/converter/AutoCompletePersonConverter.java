package org.fsl.converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.fsl.application.ApplicationProperties;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.service.action.BaseServiceAction;
import fsl.ta.toms.roms.webservices.Maintenance;
import fsl.ta.toms.roms.webservices.ReferenceCode;
import fsl.ta.toms.roms.webservices.RoadOperation;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.search.criteria.impl.ITAExaminerCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RefCodeCriteriaBO;


public class AutoCompletePersonConverter implements Converter, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9101388845228519176L;
	
	List<PersonBOForRoadCompConverter> staffList = new ArrayList<PersonBOForRoadCompConverter>();
	
	public AutoCompletePersonConverter(){
		super();
		this.getAllStaff();
	}

	private void getAllStaff()
	{
		
		RoadOperation roadOpService = new RoadOperation();
		
		Maintenance maintenanceService = new Maintenance();
		
		ReferenceCode refCodeService = new ReferenceCode();
		
		ITAExaminerCriteriaBO itaCriteria = new ITAExaminerCriteriaBO();
		
		RefCodeCriteriaBO codes = new RefCodeCriteriaBO();
				
		itaCriteria.setStatusId(Constants.Status.ACTIVE);
		codes.setTableName("roms_police_officer");
		codes.setStatus(Constants.Status.ACTIVE);
		
		List<ITAExaminerBO> itaExaminerBOList = null;
		List<TAStaffBO> taStaffBOList = null;
		List<RefCodeBO> refcodes = null;
		
		try{
			itaExaminerBOList = maintenanceService.lookupITAExaminer(itaCriteria);
			
			for(ITAExaminerBO staff : itaExaminerBOList)
			{
				PersonBOForRoadCompConverter person = new PersonBOForRoadCompConverter();
				
					person.setFullName(staff.getPersonBO().getFullName() + " [" + staff.getExaminerId() + "]");
					person.setPersonId(staff.getPersonBO().getPersonId());

					this.staffList.add(person);
					
				
			}
			
			taStaffBOList = roadOpService.getAllROMSStaff();
			for(TAStaffBO staff : taStaffBOList)
			{
				PersonBOForRoadCompConverter person = new PersonBOForRoadCompConverter();
				
					person.setFullName(staff.getFullName() + " [" + staff.getStaffId() + "]");
					person.setPersonId(staff.getPersonId());

					this.staffList.add(person);	
			}
			
			refcodes = refCodeService.getReferenceCode(codes);
			for(RefCodeBO staff : refcodes)
			{
				PersonBOForRoadCompConverter person = new PersonBOForRoadCompConverter();
				
					person.setFullName(staff.getDescription() + " - " + staff.getShortDescription() + " [" + staff.getAltId() + "]");
					person.setPersonId(Integer.parseInt(staff.getCode()));

					this.staffList.add(person);	
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		 
	}
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException 
	{
		PersonBOForRoadCompConverter foundStaff = new PersonBOForRoadCompConverter();
		
		
		if(staffList!=null){
			for(PersonBOForRoadCompConverter staff : staffList)
			{
				if(staff.getFullName().equalsIgnoreCase(value))
				{
					foundStaff = staff;
					
					continue;
				}
			}
		}
		return foundStaff;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException 
	{
		
		return ((PersonBOForRoadCompConverter)value).getFullName();
	}

}
