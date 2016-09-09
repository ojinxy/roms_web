package org.fsl.datatable;

import java.io.Serializable;
import java.util.List;

import org.springframework.faces.model.OneSelectionTrackingListDataModel;

/**
 * @since
 * @author jreid
 * 
 */
public class DataTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// the page we are on
	private int currentPageNumber;

	// number or pages in datatable
	private Integer totalPages;

	// total number of records returned
	private Integer totalNumberOfElements;

	// element containing results to be displayed on screen
	private OneSelectionTrackingListDataModel pageElements;

	// number of elements per page
	private int pageSize;

	/*
	 * // the sort order ASC DESC private String sortOrder;
	 * 
	 * // the field we are sorting by private String sortOn;
	 */
	// first item to be retrieved from database
	private int indexOfFirstItem;

	// first item to be retrieved from database
	private int indexOfLastItem;

	// private int currentPage;
	// private int previousPage;
	// private int nextPage;

	public DataTable() {
		super();
		pageElements = new OneSelectionTrackingListDataModel();
		currentPageNumber = 0;
		totalNumberOfElements = null;
		totalPages = null;
		pageSize = 0;

	}

	public DataTable(final List<?> list, Integer pageSize) {
		super();
		if (list != null && !list.isEmpty()) {
			totalNumberOfElements = list.size();
			currentPageNumber = 1;
			// pageSize = 10;
			indexOfFirstItem = 0;
			indexOfLastItem = pageSize;

			if (list.size() < pageSize) {
				totalPages = 1;
				indexOfLastItem = list.size();
				pageElements = new OneSelectionTrackingListDataModel(list);
			} else {
				totalPages = list.size() / pageSize;
				indexOfLastItem = list.size();
				pageElements = new OneSelectionTrackingListDataModel(
						list.subList(0, pageSize));
			}
		}
	}

	/**
	 * 
	 * @param list
	 */
	public DataTable(OneSelectionTrackingListDataModel list) {
		super();
		if (list != null && list.isRowAvailable())
			totalNumberOfElements = list.getRowCount();

		pageElements = list;
		
	}

	/**
	 * @param list
	 */
	public void setListOfItems(List list) {

		if (list != null && !list.isEmpty())
			this.totalNumberOfElements = list.size();

		this.pageElements = new OneSelectionTrackingListDataModel(list);
	}

	public void clear() {

		pageElements = new OneSelectionTrackingListDataModel();
		currentPageNumber = 0;
		totalNumberOfElements = null;
		totalPages = null;
		pageSize = 0;

	}

	/**
	 * @return the currentPageNumber
	 */
	public int getCurrentPageNumber() {
		return this.currentPageNumber;
	}

	/**
	 * @param currentPageNumber
	 *            the currentPageNumber to set
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	/**
	 * @return the totalPages
	 */
	public Integer getTotalPages() {
		return this.totalPages;
	}

	/**
	 * @param totalPages
	 *            the totalPages to set
	 */
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the totalNumberOfElements
	 */
	public Integer getTotalNumberOfElements() {
		return this.totalNumberOfElements;
	}

	/**
	 * @param totalNumberOfElements
	 *            the totalNumberOfElements to set
	 */
	public void setTotalNumberOfElements(Integer totalNumberOfElements) {
		this.totalNumberOfElements = totalNumberOfElements;
	}

	/**
	 * @return the pageElements
	 */
	public OneSelectionTrackingListDataModel getPageElements() {
		return this.pageElements;
	}

	/**
	 * @param pageElements
	 *            the pageElements to set
	 */
	public void setPageElements(OneSelectionTrackingListDataModel pageElements) {
		this.pageElements = pageElements;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the indexOfFirstItem
	 */
	public int getIndexOfFirstItem() {
		return this.indexOfFirstItem;
	}

	/**
	 * @param indexOfFirstItem
	 *            the indexOfFirstItem to set
	 */
	public void setIndexOfFirstItem(int indexOfFirstItem) {
		this.indexOfFirstItem = indexOfFirstItem;
	}

	/**
	 * @return the indexOfLastItem
	 */
	public int getIndexOfLastItem() {
		return this.indexOfLastItem;
	}

	/**
	 * @param indexOfLastItem
	 *            the indexOfLastItem to set
	 */
	public void setIndexOfLastItem(int indexOfLastItem) {
		this.indexOfLastItem = indexOfLastItem;
	}

	@Override
	public String toString() {
		String s = "pageElements  : " + pageElements + '\n'
				+ "currentPageNumber  : " + currentPageNumber + '\n'
				+ "pageSize  : "
				+ pageSize
				+ '\n'
				+
				// "previousPage  : " + previousPage+ '\n' +
				// " currentPage : " + currentPage + '\n' +
				// " nextPage : " + nextPage + '\n' +
				"totalNumberElements  : " + totalNumberOfElements + '\n'
				+ "totalPages  : " + totalPages + '\n'; /*
														 * + "sortOn  : " +
														 * sortOn + '\n' +
														 * "sortOrder  : " +
														 * sortOrder;
														 */
		return s;

	}

}
