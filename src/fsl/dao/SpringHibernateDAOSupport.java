package fsl.dao;

import org.hibernate.SessionFactory;


public interface SpringHibernateDAOSupport {
		
	public void setSessionFactory(SessionFactory sessionFactory);

}
