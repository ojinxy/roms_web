package org.fsl.datatable;

/**
 * All objects that will be used as search criteria should implement this
 * interface. <code>SearchCriteria</code> serves as a marker identifying an
 * implementing object as a search object.
 * 
 * @author othomas
 * 
 */
public class SearchCriteria {

	// the sort order ASC DESC
	String sortOrder = "";

	// the field we are sorting by
	String sortOn = "";

	String currentSortColumn = "";

	String previousSortColumn = "";

	static final String SORT_ASC = "ASC";

	static final String SORT_DSC = "DSC";

	/**
	 * @return the sortOrder
	 */
	public String getSortOrder() {
		return this.sortOrder;
	}

	/**
	 * @param sortOrder
	 *            the sortOrder to set
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the sortOn
	 */
	public String getSortOn() {
		return this.sortOn;
	}

	/**
	 * @param sortOn
	 *            the sortOn to set
	 */
	public void setSortOn(String sortOn) {
		this.sortOn = sortOn;
	}

	/**
	 * @return the currentSortColumn
	 */
	public String getCurrentSortColumn() {
		return this.currentSortColumn;
	}

	/**
	 * @param currentSortColumn
	 *            the currentSortColumn to set
	 */
	public void setCurrentSortColumn(String currentSortColumn) {
		this.currentSortColumn = currentSortColumn;
	}

	/**
	 * @return the previousSortColumn
	 */
	public String getPreviousSortColumn() {
		return this.previousSortColumn;
	}

	/**
	 * @param previousSortColumn
	 *            the previousSortColumn to set
	 */
	public void setPreviousSortColumn(String previousSortColumn) {
		this.previousSortColumn = previousSortColumn;
	}

}
