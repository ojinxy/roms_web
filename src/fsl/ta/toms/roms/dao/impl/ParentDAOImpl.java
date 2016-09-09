/**
 * Created By: oanguin
 * Date: Apr 19, 2013
 *
 */
package fsl.ta.toms.roms.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import antlr.StringUtils;

import fsl.dao.SpringHibernateDAOSupport;
import fsl.ta.toms.roms.dao.ParentDAO;
import fsl.ta.toms.roms.dataobjects.ArteryDO;
import fsl.ta.toms.roms.dataobjects.LMIS_TAOfficeLocationViewDO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.util.StringUtil;

/**
 * @author oanguin Created Date: Apr 19, 2013
 * @param <T>
 */
public class ParentDAOImpl implements ParentDAO , SpringHibernateDAOSupport {
	
	protected Logger logger = LogManager.getLogger(this.getClass().getName());
	
	protected HibernateTemplate hibernateTemplate;
	
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		hibernateTemplate = new HibernateTemplate(sessionFactory);

	}

	@Override
	public Session getSession() {

		return this.hibernateTemplate.getSessionFactory().openSession();
	}

	@Override
	protected void finalize() throws Throwable {

		if (this.hibernateTemplate != null) {
			if (this.hibernateTemplate.getSessionFactory() != null) {
				if (!this.hibernateTemplate.getSessionFactory().isClosed()) {
					if (this.hibernateTemplate.getSessionFactory()
							.getCurrentSession() != null) {
						//System.out.println("Closing Current Session");
						this.hibernateTemplate.getSessionFactory()
								.getCurrentSession().close();
					}

					//System.out.println("Closing Session Factory");
					this.getSession().getSessionFactory().close();
				}
			}

			super.finalize();
		}
	}

	
	
	@Override
	public Serializable save(Object entity) throws DataAccessException
	{
		//Session session = getSession();
		//Serializable id =session.save(entity);
		//session.getTransaction().
		//return id;
		return hibernateTemplate.save(entity);
	}

	@Override
	public void update(Object entity) throws DataAccessException {
		hibernateTemplate.update(entity);

	}

	@Override
	public void delete(Object entity) throws DataAccessException {
		hibernateTemplate.delete(entity);

	}

	@Override
	public <T> List<T> findAll(Class<T> clazz) throws DataAccessException {

		return ((HibernateTemplate) getSession()).loadAll(clazz);
	}
	
	@Override
	public void saveOrUpdate(Object entity) throws DataAccessException
	{
		getSession().saveOrUpdate(entity);
	}


	
	@Override
	public <T> T find(Class<T> clazz, Serializable id)
			throws DataAccessException {
		return hibernateTemplate.get(clazz, id);
	}

	@Override
	public <T> T load(Class<T> clazz, Serializable id)
			throws DataAccessException {
		return (T) getSession().load(clazz, id);
	}
	
	/**
	 * This class is a generic function which gets all table reference codes based on the filter hashmap and status passed into function.
	 * @param objectClass
	 * @param status
	 * @param filter
	 * @return
	 * @throws InvalidFieldException
	 */
	public List<?> getReference(Class objectClass, String status, HashMap<String,String> filter) throws InvalidFieldException
	{
		Criteria criteria = this.getSession().createCriteria(objectClass,
				"table");
		
		if(StringUtil.isSet(status))
		{
			if(objectClass.equals(LMIS_TAOfficeLocationViewDO.class))
				criteria.add(Restrictions.eq("table.statusCode", status).ignoreCase());
			else
				criteria.add(Restrictions.eq("table.status.statusId", status).ignoreCase());
		}

		if(filter != null)
		{
			
			
			char fieldType = ' ';
	
			for (Map.Entry<String, String> entry : filter.entrySet()) 
			{
				if(! StringUtil.isSet(entry.getKey()) || ! StringUtil.isSet(entry.getValue()))
					throw new InvalidFieldException();
	
				try 
				{	
					
					//If String starts with trailing zeros assume string
					if(entry.getValue().startsWith("0"))
						throw new NumberFormatException();
					
					Integer.parseInt(entry.getValue());
					fieldType = 'i';
				} catch (NumberFormatException NumExecption) {
					fieldType = 's';
				}
	
				if (fieldType == 's')/* string input */
				{
					criteria.add(Restrictions.sqlRestriction("lower({alias}."
							+ entry.getKey() + ") = ?", entry.getValue()
							.toLowerCase(), Hibernate.STRING));
				} else/* integer input */
				{
					criteria.add(Restrictions.sqlRestriction(
							"{alias}." + entry.getKey() + " = ?",
							Integer.parseInt(entry.getValue()), Hibernate.INTEGER));
				}
			}
		}
		
		List<?> objectList = null;
		
		try
		{
			objectList = criteria.list();
		}
		catch(Exception exe)
		{
			
			exe.printStackTrace();
			throw new InvalidFieldException();
		}

		
		return objectList;
	}

	@Override
	public Object merge(Object entity) {
		return this.getSession().getSessionFactory().getCurrentSession().merge(entity);
		
	}
	
	public String usernameToFullName(String username,Session... session){
		String newUserName = "";
		//if(!username.equals("") && username != null){
			Query query = this.getSession().createSQLQuery(
					"CALL sp_usernametoFullname(:username)")
					.setParameter("username", username);
			@SuppressWarnings("unchecked")
			List<String> l = query.list();
			newUserName = l.get(0).toString();
		//}
		return newUserName;
	}
}
