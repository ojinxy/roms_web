package org.fsl.datatable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import org.springframework.faces.model.OneSelectionTrackingListDataModel;


public class DataTableMemory implements Serializable
{

	private static final long serialVersionUID = 1L;

	public OneSelectionTrackingListDataModel pageElements;
	
	public ArrayList dataList;
	
	public ArrayList pagedList;
	
	public Integer rowsPerPage;
	
	private Integer amountOfRecords;
	
	private Integer currentFirstRow;
	
	private Integer currentLastRow;
	
	private String pagingDetails;
	
	public String currentPageHeader;
	
	private String pagingItem;
	
	private String sortOrder;
	
	private String sortBy;
	
	
	private static enum MathOperator
	{
		ADD,
		SUB
	}
	
	/**
	 * 
	 * @param dataList
	 * @param rowsPerPage
	 * @param amountOfRecords
	 * @param pagingItem -> This is the text which will be displayed in the paging details
	 */
	public DataTableMemory(List dataList,
			Integer rowsPerPage, Integer amountOfRecords, String pagingItem) 
	{
		
		this.pagingItem = pagingItem;
		this.dataList = new ArrayList(dataList);
		this.rowsPerPage = rowsPerPage;
		this.amountOfRecords = amountOfRecords;
		this.currentFirstRow = 0;
		this.currentLastRow = this.calculateCurrentLastRow();
		this.sortOrder = "asc";
		
		this.setPagedDataList();
		
		
	}
	
	public DataTableMemory()
	{
		this.sortOrder = "asc";
	}

	public DataTableMemory(List dataList,
			Integer rowsPerPage, Integer amountOfRecords) 
	{
		
		this.dataList = new ArrayList(dataList);
		this.rowsPerPage = rowsPerPage;
		this.amountOfRecords = amountOfRecords;
		this.currentFirstRow = 0;
		this.currentLastRow = this.calculateCurrentLastRow();
		this.sortOrder = "asc";
		
		this.setPagedDataList();
		
		
	}
	
	public DataTableMemory(List dataList,
			Integer rowsPerPage) 
	{
		
		
		this.dataList = new ArrayList(dataList);
		this.rowsPerPage = dataList.size();
		this.amountOfRecords = amountOfRecords;
		this.currentFirstRow = 0;
		this.currentLastRow = this.calculateCurrentLastRow();
		this.sortOrder = "asc";
		
		this.setPagedDataList();
		
		
	}
	
	
	public void setPagedDataList()
	{
		if(this.dataList != null)
			if(this.rowsPerPage != null && this.rowsPerPage > 0)
				if(this.amountOfRecords != null && this.amountOfRecords > 0)
					if(this.currentFirstRow != null)
						if(this.currentLastRow != null)
						{
							this.pagedList = new ArrayList(this.dataList.subList(this.currentFirstRow,this.currentLastRow + 1));
						
							this.pageElements = new OneSelectionTrackingListDataModel(new ArrayList(pagedList));
						}
		
		this.setPagingDetails();
	}
	
	
	private Integer calculateCurrentLastRow()
	{
		if(this.currentFirstRow != null && this.currentFirstRow >= 0 
				&& this.dataList != null && this.dataList.size() > 0
				&& this.rowsPerPage != null && this.rowsPerPage > 0)
		{
			
				Integer proposedCurrentLastRow = this.currentFirstRow + this.rowsPerPage -1;
				
				if(proposedCurrentLastRow + 1  > this.dataList.size())
					return this.dataList.size() - 1;
				else
					return proposedCurrentLastRow;
		}
		else
		{
			return 0;
		}
		
		
	}
	
	private Integer calculateCurrentFirstRow(MathOperator op)
	{
		
		if(this.currentFirstRow != null && this.currentFirstRow >= 0 
				&& this.dataList != null && this.dataList.size() >0
				&& this.rowsPerPage != null && this.rowsPerPage > 0)
		{
			Integer proposedCurrentFirstRow = 0;
			
			switch(op)
			{
				case ADD:
				{
					proposedCurrentFirstRow = this.currentFirstRow + this.rowsPerPage;
					break;
				}
				case SUB:
				{
					proposedCurrentFirstRow = this.currentFirstRow - this.rowsPerPage;
					break;
				}
				
				
			}
			
			if(proposedCurrentFirstRow > 0 && proposedCurrentFirstRow - 1 < this.dataList.size())
				return proposedCurrentFirstRow;
			else
				return 0;
		}
		else
		{
			return 0;
		}
	}
	
	
	public void previousPage()
	{
		this.currentFirstRow = this.calculateCurrentFirstRow(MathOperator.SUB);
		
		this.currentLastRow = this.calculateCurrentLastRow();
		
		this.setPagedDataList();
	}
	
	public void nextPage()
	{
		System.out.println("In next page function.");
		
		this.currentFirstRow = this.calculateCurrentFirstRow(MathOperator.ADD);
		
		this.currentLastRow = this.calculateCurrentLastRow();
		
		this.setPagedDataList();
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = new ArrayList(dataList);
	}

	public List getPagedList() {
		return pagedList;
	}

	public void setPagedList(List pagedList) {
		this.pagedList = new ArrayList(pagedList);
	}

	public OneSelectionTrackingListDataModel getPageElements() {
		return pageElements;
	}

	public void setPageElements(OneSelectionTrackingListDataModel pageElements) {
		this.pageElements = pageElements;
	}

	public Integer getRowsPerPage() {
		return rowsPerPage;
	}

	public Integer getAmountOfRecords() {
		return amountOfRecords;
	}

	public Integer getCurrentFirstRow() {
		return currentFirstRow;
	}

	public Integer getCurrentLastRow() {
		return currentLastRow;
	}
	
	public void firstPage()
	{
		this.currentFirstRow = 0;
		
		this.currentLastRow = this.calculateCurrentLastRow();
		
		this.setPagedDataList();
	}
	
	
	public void lastPage()
	{
		this.currentFirstRow = this.calculateLastPageFirstRow();
		
		this.currentLastRow = this.calculateCurrentLastRow();
		
		this.setPagedDataList();
	}
	
	private Integer calculateLastPageFirstRow()
	{
		if(this.rowsPerPage != null && this.rowsPerPage > 0
			&& this.amountOfRecords != null && this.amountOfRecords > 0
				&&this.currentFirstRow != null
					&&this.currentLastRow != null)
					{

			
						Integer currRowForLastPage = (this.amountOfRecords / this.rowsPerPage) * this.rowsPerPage;
						
						if(currRowForLastPage > this.amountOfRecords)
							return 0;
						else
							return currRowForLastPage;
						
					}
					else
					{
						return 0;
					}
	}

	public void setRowsPerPage(Integer rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	
	public void changePageSize(Integer rowsPerPage)
	{
		
		this.rowsPerPage = rowsPerPage;
		this.currentFirstRow = 0;
		
		this.currentLastRow = this.calculateCurrentLastRow();
		this.setPagedDataList();
		
		
	}
	
	public void refreshPageElements()
	{
		this.currentFirstRow = 0;
		
		this.currentLastRow = this.calculateCurrentLastRow();
		this.setPagedDataList();
		
	}
	
	private void setPagingDetails()
	{
		
		StringBuilder stringBuilder = new StringBuilder("");
		
		stringBuilder.append((this.currentFirstRow + 1) + " - " + (this.currentLastRow + 1));
		
		stringBuilder.append(" of " + this.amountOfRecords);
		
		stringBuilder.append(" " + this.pagingItem);
		
		this.pagingDetails = stringBuilder.toString();
	}

	public String getPagingDetails() {
		return pagingDetails;
	}
	
	public String getCurrentPageHeader() {
		return currentPageHeader;
	}

	public void setCurrentPageHeader(String currentPageHeader) {
		this.currentPageHeader = currentPageHeader;
	}

	public String getPagingItem() {
		return pagingItem;
	}

	public void setPagingItem(String pagingItem) {
		this.pagingItem = pagingItem;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
	
}
