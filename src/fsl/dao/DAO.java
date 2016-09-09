package fsl.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;


public interface DAO {
	
	public Serializable save(Object entity); 
	
	public void update(Object entity);
	
	public void delete(Object entity);
	
	public <T> List<T> findAll(Class<T> clazz);
	
	public <T> T find(Class<T> clazz, Serializable id );
	
	public void saveOrUpdate(Object entity);
	
	public Object merge(Object entity);
	
	public <T> T load(Class<T> clazz, Serializable id) throws DataAccessException;

	

}
