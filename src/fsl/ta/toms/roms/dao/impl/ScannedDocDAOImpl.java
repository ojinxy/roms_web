package fsl.ta.toms.roms.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.dao.ScannedDocDAO;
import fsl.ta.toms.roms.dataobjects.ScannedDocDO;
import fsl.ta.toms.roms.dataobjects.TAStaffDO;

public class ScannedDocDAOImpl extends ParentDAOImpl implements ScannedDocDAO {

	
	@Override
	public boolean updateScannedDocDO(ScannedDocDO scannedDoc) {
		try {
			hibernateTemplate.update(scannedDoc);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	

	@Override
	public List<ScannedDocDO> findScannedDocsByOutcomeId(Integer outcomeId) {		
		Criteria criteriaS = this.getSession().createCriteria(ScannedDocDO.class);
		List<ScannedDocDO> scannedDocs = new ArrayList<ScannedDocDO>();
		
		if(outcomeId != null) {
			//for summons 
			criteriaS.add(Restrictions.eq("roadCheckOffenceOutcome.roadCheckOffenceOutcomeId", outcomeId));						
		}
		scannedDocs = criteriaS.list();

		if (scannedDocs == null || scannedDocs.size() < 1) {
			return null;
		}

		return scannedDocs;
	}
	
	
	@Override
	public ScannedDocDO findManualScannedDocumentByOutcomeId(Integer outcomeId) {		
		Criteria criteriaS = this.getSession().createCriteria(ScannedDocDO.class);
		List<ScannedDocDO> scannedDocs = new ArrayList<ScannedDocDO>();
		
		if(outcomeId != null) {
			criteriaS.add(Restrictions.eq("roadCheckOffenceOutcome.roadCheckOffenceOutcomeId", outcomeId));	
			criteriaS.add(Restrictions.eq("docType", Constants.ScannedDocumentTypeShort.MANUAL_DOCUMENT));	
		}
		
		scannedDocs = criteriaS.list();

		if (scannedDocs != null && scannedDocs.size() > 0) {
			return scannedDocs.get(0);
		}

		return null;
	}

	@Override
	public Serializable saveScannedDoc(ScannedDocDO scannedDocDo) {		
		return save(scannedDocDo);
	}

	@Override
	public ScannedDocDO findScannedDocById(Integer scannedDocId) {
		return find(ScannedDocDO.class, scannedDocId);
	}

	@Override
	public boolean deleteScannedDocById(Integer scannedDocId) {
		try {
			ScannedDocDO scannedDoc = hibernateTemplate.load(
					ScannedDocDO.class, scannedDocId);
			
			if(scannedDoc != null)
				hibernateTemplate.delete(scannedDoc);

		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}
	
	
	

	
}
