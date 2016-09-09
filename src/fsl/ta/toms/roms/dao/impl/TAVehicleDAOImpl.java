/**
 * Created By: oanguin
 * Date: Apr 20, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.TAVehicleDAO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.TAVehicleDO;
import fsl.ta.toms.roms.dataobjects.WreckingCompanyDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.search.criteria.impl.TAVehicleCriteriaBO;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin
 * Created Date: Apr 20, 2013
 */
public class TAVehicleDAOImpl extends ParentDAOImpl implements
		TAVehicleDAO {

	


	@SuppressWarnings("unchecked")
	@Override
	public List<TAVehicleDO> getTAVehicleReference(
			HashMap<String, String> filter, String status) throws InvalidFieldException 
	{
		return (List<TAVehicleDO>)super.getReference(TAVehicleDO.class, status, filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TAVehicleBO> lookupTAVehicle(
			TAVehicleCriteriaBO taVehicleCriteriaBO) {
		
		List<TAVehicleBO> taVehicleList = new ArrayList<TAVehicleBO>();
		
		StringBuffer queryString = new StringBuffer("select new fsl.ta.toms.roms.bo.TAVehicleBO(o) from TAVehicleDO o");
		queryString.append(" where (o.taVehicleId is not null)");
		
		if(taVehicleCriteriaBO.getTaVehicleId()!= null && taVehicleCriteriaBO.getTaVehicleId()>0){
			queryString.append(" and o.taVehicleId = ").append(taVehicleCriteriaBO.getTaVehicleId());
		}
		
		if(StringUtils.trimToNull(taVehicleCriteriaBO.getVehicle().getPlateRegNo()) != null){
			queryString.append(" and upper(o.vehicle.plateRegNo) like '%").append(StringUtil.quoteReplace(taVehicleCriteriaBO.getVehicle().getPlateRegNo().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(taVehicleCriteriaBO.getVehicle().getChassisNo()) != null){
			queryString.append(" and upper(o.vehicle.chassisNo) like '%").append(StringUtil.quoteReplace(taVehicleCriteriaBO.getVehicle().getChassisNo().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(taVehicleCriteriaBO.getVehicle().getEngineNo()) != null){
			queryString.append(" and upper(o.vehicle.engineNo) like '%").append(StringUtil.quoteReplace(taVehicleCriteriaBO.getVehicle().getEngineNo().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(taVehicleCriteriaBO.getVehicle().getColour()) != null){
			queryString.append(" and upper(o.vehicle.colour) like '%").append(StringUtil.quoteReplace(taVehicleCriteriaBO.getVehicle().getColour().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(taVehicleCriteriaBO.getVehicle().getMakeDescription()) != null){
			queryString.append(" and upper(o.vehicle.makeDescription) like '%").append(StringUtil.quoteReplace(taVehicleCriteriaBO.getVehicle().getMakeDescription().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(taVehicleCriteriaBO.getVehicle().getModel()) != null){
			queryString.append(" and upper(o.vehicle.model) like '%").append(StringUtil.quoteReplace(taVehicleCriteriaBO.getVehicle().getModel().trim()).toUpperCase()).append("%'");
		}
		

		if(StringUtils.trimToNull(taVehicleCriteriaBO.getVehicle().getTypeDesc()) != null){
			queryString.append(" and upper(o.vehicle.typeDesc) like '%").append(StringUtil.quoteReplace(taVehicleCriteriaBO.getVehicle().getTypeDesc().trim()).toUpperCase()).append("%'");
		}
		
		if(StringUtils.trimToNull(taVehicleCriteriaBO.getVehicle().getOwnerName()) != null){
			queryString.append(" and upper(o.vehicle.ownerName) like '%").append(StringUtil.quoteReplace(taVehicleCriteriaBO.getVehicle().getOwnerName().trim()).toUpperCase()).append("%'");
		}
		
		if(taVehicleCriteriaBO.getVehicle().getYear() != null && taVehicleCriteriaBO.getVehicle().getYear()>0){
			queryString.append(" and o.vehicle.year = ").append(taVehicleCriteriaBO.getVehicle().getYear());
		}
		
		if(StringUtils.trimToNull(taVehicleCriteriaBO.getOfficeLocationCode()) != null){
			queryString.append(" and upper(o.officeLocationCode) = '").append(StringUtil.quoteReplace(taVehicleCriteriaBO.getOfficeLocationCode().trim()).toUpperCase()).append("'");
		}
		
		if(StringUtils.trimToNull(taVehicleCriteriaBO.getStatusId()) != null){
			queryString.append(" and upper(o.status.statusId) = '").append(StringUtil.quoteReplace(taVehicleCriteriaBO.getStatusId().trim()).toUpperCase()).append("'");
		}
		
		queryString.append(" order by o.vehicle.plateRegNo");
		
		taVehicleList =  hibernateTemplate.find(queryString.toString());
		 
		if(taVehicleList==null || taVehicleList.size()<1){
			return null;
		}
		
		for(TAVehicleBO taVehicleBO: taVehicleList){
			if(taVehicleBO.getAuditEntryBO().getUpdateUsername() != null)
				taVehicleBO.getAuditEntryBO().setUpdateUsername(usernameToFullName(taVehicleBO.getAuditEntryBO().getUpdateUsername()));
			else
				taVehicleBO.getAuditEntryBO().setUpdateUsername(usernameToFullName(taVehicleBO.getAuditEntryBO().getCreateUsername()));
		}
		
		return taVehicleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TAVehicleDO findTAVehicleByVehicleId(Integer vehicleId) {
		List<TAVehicleDO> taVehicleList = new ArrayList<TAVehicleDO>();
		
		String queryString = "from TAVehicleDO o" +
				" where o.vehicle.vehicleId = :vehicleId";
		
		taVehicleList = hibernateTemplate.findByNamedParam(queryString, "vehicleId", vehicleId);
			
		if(taVehicleList!=null &&  taVehicleList.size()==1){
			return taVehicleList.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean activeTAVehicleWithPlateRegNoExists(String plateRegNo) {
		List<TAVehicleDO> taVehicleList = new ArrayList<TAVehicleDO>();
		
		String queryString = "from TAVehicleDO o" +
				" where upper(o.vehicle.plateRegNo) = :plateRegNo and o.status.statusId = :activeStatus";
		
		String[] paramNames = {"plateRegNo", "activeStatus"};
		Object[] values = {StringUtil.quoteReplace(plateRegNo).toUpperCase(), Constants.Status.ACTIVE};
		taVehicleList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
		
			
		if(taVehicleList!=null &&  taVehicleList.size()>0){
			return true;
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean taVehiclePlateRegNoExists(String plateRegNo) {
		List<TAVehicleDO> taVehicleList = new ArrayList<TAVehicleDO>();
		
		String queryString = "from TAVehicleDO o" +
				" where upper(o.vehicle.plateRegNo) = :plateRegNo";
		
		String[] paramNames = {"plateRegNo"};
		Object[] values = {StringUtil.quoteReplace(plateRegNo).toUpperCase()};
		taVehicleList = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
			
		if(taVehicleList!=null &&  taVehicleList.size()>0){
			return true;
		}
		
		return false;
	}

	@Override
	public Integer saveTAVehicleDO(TAVehicleDO taVehicleDO) {
		Integer taVehicleId = (Integer) (hibernateTemplate.save(taVehicleDO));
		
		return taVehicleId;
	}

}
