package fsl.ta.toms.roms.service;

import java.util.List;

import fsl.ta.toms.roms.bo.BIMSStaffViewBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAStaffTypeBO;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;

public interface StaffUserMappingService {
	
	public List<BIMSStaffViewBO> lookupAllStaff();
	
	public List<BIMSStaffViewBO> lookupActiveStaff();
	
	public List<BIMSStaffViewBO> lookupAllUnmappedStaff();
	
	public List<BIMSStaffViewBO> lookupActiveUnmappedStaff();
	
	public List<LMISUserViewBO> lookupAllUsers();
	
	public List<LMISUserViewBO> lookupActiveUsers();
	
	public List<LMISUserViewBO> lookupAllUnmappedUsers();
	
	public List<LMISUserViewBO> lookupActiveUnmappedUsers();
	
	public List<StaffUserMappingBO> lookupStaffUserMappings(StaffUserMappingCriteriaBO staffUserMappingCriteriaBO);
		
	public boolean isUserMapped(String username);
	
	public boolean staffExists(String staffId);
	
	public boolean isStaffMapped(String staffId);
	
	public TAStaffBO getStaffByUsername(String username);
	
	public boolean saveStaffUserMapping(StaffUserMappingBO staffUserMappingBO, BIMSStaffViewBO bimsStaffViewBO);
	
	public boolean updateStaffUserMapping(StaffUserMappingBO staffUserMappingBO);
	
	public BIMSStaffViewBO findTAStaffByStaffId(String staffId);
	
	public boolean findUserByUsername(String username);
	
	public boolean taStaffExistWithTRN(String trnNbr);
	
	public List<TAStaffBO> getAllROMSStaff();
	
	public List<StaffUserMappingBO> lookupTAStaff(StaffUserMappingCriteriaBO staffUserMappingCriteriaBO);
	
	public List<TAStaffTypeBO> lookupBIMSStaffType();
	
	public List<LMISUserViewBO> lookupUsersByCriteria(String term);
	public List<LMISUserViewBO> lookupAllUnmappedUsersByCriteria(String term);
	public List<LMISUserViewBO> lookupAllUnmappedUsersByCriteriaExceptSpecificUser(String term, String staffId);
	
	public LMISUserViewBO lookupLMISUser(String username);
}
