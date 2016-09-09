package fsl.ta.toms.roms.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.bo.PersonBO;
import fsl.ta.toms.roms.dao.PersonDAO;
import fsl.ta.toms.roms.dataobjects.ParishDO;
import fsl.ta.toms.roms.dataobjects.PersonDO;

/**
 * 
 * @author jreid
 * Created Date: Feb 27, 2014
 */
public class PersonDAOImpl extends ParentDAOImpl implements PersonDAO {

	@Override
	public Integer savePersonDO(PersonDO personDO) {
		Integer personId = (Integer) (hibernateTemplate.save(personDO));

		return personId;
	}

	@Override
	public boolean personExistsById(Integer personId) {
		List<PersonDO> personList = new ArrayList<PersonDO>();

		String queryString = " from PersonDO p where p.personId = :personId";

		personList = hibernateTemplate.findByNamedParam(queryString, "personId", personId);

		if (personList != null && personList.size() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public boolean personExistsByTRN(String trn) {
		List<PersonDO> personList = new ArrayList<PersonDO>();

		String queryString = " from PersonDO p where p.trnNbr = :trn";

		personList = hibernateTemplate.findByNamedParam(queryString, "trn", trn);

		if (personList != null && personList.size() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public PersonDO findPersonByTRN(String trn) {
		List<PersonDO> personList = new ArrayList<PersonDO>();

		String queryString = " from PersonDO p where p.trnNbr = :trn";

		personList = hibernateTemplate.findByNamedParam(queryString, "trn", trn);

		if (personList != null && personList.size() > 0) {
			return personList.get(0);
		}

		return null;
	}

	@Override
	public PersonDO findPersonById(Integer id) {
		List<PersonDO> personList = new ArrayList<PersonDO>();

		String queryString = " from PersonDO p where p.personId = :personId";

		personList = hibernateTemplate.findByNamedParam(queryString, "personId", id);

		if (personList != null && personList.size() > 0) {
			return personList.get(0);
		}

		return null;
	}

	@Override
	public Integer savePerson(PersonBO personBO) {
		PersonDO personDO = new PersonDO(personBO);
		personDO.getAuditEntry().setCreateDTime(Calendar.getInstance().getTime());
		personDO.getAuditEntry().setCreateUsername(personBO.getCurrentUserName());
		ParishDO parish;
		parish = this.load(ParishDO.class, personBO.getParishCode());
		personDO.getAddress().setParish(parish);
		Integer personId = (Integer) (hibernateTemplate.save(personDO));
		
		return personId;
	}

	@Override
	public PersonDO findPersonByDriversLicence(String dlNo)
	{
		Criteria criteria = this.hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(PersonDO.class);
		
		criteria.add(Restrictions.eq("dlNo", dlNo));
		
		List<PersonDO> personDOs = criteria.list();
		
		if(personDOs.size() > 0)
			return personDOs.get(0);
		else
			return null;
	}

}
