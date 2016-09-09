/**
 * Created By: oanguin
 * Date: Apr 26, 2013
 *
 */
package fsl.ta.toms.roms.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fsl.ta.toms.roms.bo.ComplianceBO;
import fsl.ta.toms.roms.bo.CourtAppearanceBO;
import fsl.ta.toms.roms.bo.CourtCaseBO;
import fsl.ta.toms.roms.bo.SummonsBO;
import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.CourtCaseDAO;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dao.CourtAppearanceDAO;
import fsl.ta.toms.roms.dataobjects.AuditEntry;
import fsl.ta.toms.roms.dataobjects.ComplianceDO;
import fsl.ta.toms.roms.dataobjects.CourtDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.StatusDO;
import fsl.ta.toms.roms.dataobjects.SummonsDO;
import fsl.ta.toms.roms.dataobjects.CourtAppearanceDO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.CourtAppearanceCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.CourtCaseCriteriaBO;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.CourtAppearanceService;

/**
 * @author jreid Created Date: Apr 26, 2013
 */
public class CourtAppearanceServiceImpl implements CourtAppearanceService {

	@Autowired
	private DAOFactory daoFactory;

	
	
	@Autowired
	ServiceFactory serviceFactory;

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Serializable saveCourtAppearance(CourtAppearanceBO appearance) {

		CourtAppearanceDAO courtAppearanceDAO = this.daoFactory.getCourtAppearanceDAO();	
					
		return courtAppearanceDAO.save(appearance);
	}

	

	@Override
	/**
	 * @author oanguin
	 * @throws ErrorSavingException
	 * @param <code>CourtAppearanceBO currentCourtAppearance</code> Contains information of appearance to be updated.
	 * @param <code>CourtAppearanceBO mewCourtAppearance</code> Contains information on the new appearance date.
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateCourtAppearance(CourtAppearanceBO currentCourtAppearance, CourtAppearanceBO newCourtAppearance)
			throws ErrorSavingException, RequiredFieldMissingException {
	
		CourtAppearanceDAO courtAppearanceDAO = this.daoFactory.getCourtAppearanceDAO();
		try {
			courtAppearanceDAO.updateCourtAppearance(currentCourtAppearance, newCourtAppearance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void overrideCourtAppearance(CourtAppearanceBO currentCourtAppearance)
			throws ErrorSavingException, RequiredFieldMissingException {
	
		CourtAppearanceDAO courtAppearanceDAO = this.daoFactory.getCourtAppearanceDAO();
		try {
			courtAppearanceDAO.overrideCourtAppearance(currentCourtAppearance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}

	/*@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Integer createNewCourtAppearance(CourtAppearanceBO courtAppearanceBO) throws ErrorSavingException {

		CourtAppearanceDAO courtAppearanceDAO = this.daoFactory.getCourtAppearanceDAO();

		try {

			String court_date_part = DateUtils.formatDate("yyyy-mm-dd",
					CourtAppearanceBO.getCourtDate());

			java.util.Date court_date = new SimpleDateFormat(
					"yyyy-mm-dd hh:mm:ss").parse(court_date_part + " "	+ CourtAppearanceBO.getCourtTime());

			java.sql.Date court_date_time = new java.sql.Date(
					court_date.getTime());

			CourtAppearanceDO courtAppearanceDO = new CourtAppearanceDO(courtAppearanceBO);

			courtAppearanceDO.setCourt((CourtDO) courtAppearanceDAO.find(CourtDO.class,	courtAppearanceBO.getCourtId()));
			//CourtAppearanceDO.setCourtDtime(court_date_time);
			
			StatusDO status = (StatusDO) courtAppearanceDAO.find(StatusDO.class,Constants.Status.COURT_CASE_OPEN);
			
			//System.err.println(" appearance status :" + status);
			courtAppearanceDO.setStatus(status);
			
			

			SummonsDO summons = (SummonsDO) courtAppearanceDAO.find(SummonsDO.class,courtAppearanceBO.getSummonsId());

			//courtAppearanceDO.setSummons(summons);
			
			//set audit entry 
			AuditEntry auditEntry = new AuditEntry();
			auditEntry.setCreateUsername(courtAppearanceBO.getCurrentUserName());
			auditEntry.setCreateDTime(Calendar.getInstance().getTime());

			courtAppearanceDO.setAuditEntry(auditEntry);
			
			//save appearance
			Integer id = (Integer) daoFactory.getSummonsDAO().save(courtAppearanceDO);
		
			//set id on business object
			courtAppearanceBO.setCourtAppearanceId(id);

			EventAuditDO event = createCourtAppearanceEventAudit(courtAppearanceBO, auditEntry);

			serviceFactory.getEventAuditService().saveEventAuditDO(event);
			
			return id;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ErrorSavingException(e.getMessage());
		}

	}*/
	
	

	/**
	 * (non-Javadoc)
	 * 
	 * @see fsl.ta.toms.roms.dao.CourtAppearanceDAO#allCourtAppearancesForSummonsClosed(fsl.ta.toms.roms.dataobjects.SummonsDO)
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean allCourtAppearancesForSummonsClosed(Integer summonsId) {
		CourtAppearanceDAO courtAppearanceDAO = this.daoFactory.getCourtAppearanceDAO();
		return courtAppearanceDAO.allCourtAppearancesForSummonsClosed(summonsId);

	}

	/**
	 * @author jreid
	 * @param summonsID
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public CourtAppearanceDO getMostRecentCourtAppearanceBySummonsID(Integer summonsID) {
		CourtAppearanceDAO courtAppearanceDAO = this.daoFactory.getCourtAppearanceDAO();
		return courtAppearanceDAO.getMostRecentCourtAppearanceBySummonsID(summonsID);
	}
	
	
	/**
	 * @author jreid
	 * @param summonsID
	 * @return The very first court appearance created with this summons
	 */
	@Override
	@Transactional(readOnly=true)
	public CourtAppearanceDO getFirstCourtAppearanceBySummonsID(Integer summonsID) {
		CourtAppearanceDAO courtAppearanceDAO = this.daoFactory.getCourtAppearanceDAO();
		return courtAppearanceDAO.getFirstCourtAppearanceBySummonsID(summonsID);
	}
	
	
	

	@Override
	@Transactional(readOnly=true)
	public List<CourtAppearanceDO> getCourtAppearanceReference(HashMap<String, String> filter,
			String status) throws InvalidFieldException {
		CourtAppearanceDAO courtAppearanceDAO = this.daoFactory.getCourtAppearanceDAO();
		return courtAppearanceDAO.getCourtAppearanceReference(filter, status);
	}
	
	@Override
	public List<CourtAppearanceBO> getCourtAppearancesBySummonsId(Integer summonsId) throws RequiredFieldMissingException 
	{
		
		if(summonsId == null)
			throw new RequiredFieldMissingException();
		//add trial details
		CourtAppearanceCriteriaBO courtCriteria = new CourtAppearanceCriteriaBO();
		courtCriteria.setSummonsId(summonsId);
		
		
		CourtAppearanceDAO courtAppearanceDAO = this.daoFactory.getCourtAppearanceDAO();
		
		List<CourtAppearanceBO> courtAppearanceList = null;
		try {
			courtAppearanceList = courtAppearanceDAO.lookUpCourtAppearance(courtCriteria);
		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			return courtAppearanceList;
		
	}



	@Override
	@Transactional(readOnly=true)
	public Date getInitialCourtDateByCourtCaseId(Integer courtCaseId) {
		
		Date initialCourtDate = this.daoFactory.getCourtAppearanceDAO().getInitialCourtDateByCourtCaseId(courtCaseId);
		if(initialCourtDate!=null){
			
			return initialCourtDate;
		}
		
		
		return null;
	}


}
