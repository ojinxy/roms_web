package org.fsl.datatable;

import org.hibernate.criterion.DetachedCriteria;

/**
 * @author jreid
 */
public interface PagerService {

	public Integer countItems(String query);

	Integer countItemsByCriteria(DetachedCriteria criteria);

	DataTable sortPageByCriteria(DetachedCriteria criteria, int start,
			int size, String sortOrder, String sortOn);

	DataTable getPage(String queryString, int start, int size,
			String sortOrder, String sortOn);

	DataTable getPageByCriteria(DetachedCriteria criteria, int start, int size,
			String sortOrder, String sortOn);

	DataTable sortPageByQuery(String query, int start, int size,
			String sortOrder, String sortOn);

}
