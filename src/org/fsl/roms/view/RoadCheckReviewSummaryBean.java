package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.DocumentViewBO;
import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.bo.WreckingCompanyBO;

public class RoadCheckReviewSummaryBean implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Boolean showAllQueriesPerformed;
	
	public boolean isMobile;
	
	public boolean reviewScreen;
	
	private boolean shouldYouUpload;
	
	private boolean reprintReasonNeeded;
	
	private PoundBO pound;
	
	private WreckingCompanyBO wreckingCompany;
	
	private boolean shouldGoToPrepareAndPrint = false;
	
	public boolean openReportWindow = false;
	
	public boolean summonsPresent = false;
	
	public boolean shouldYouPrint = false;
	
	public boolean alreadyPrinted = false;
	
	public boolean isViewDetailsScreen = false;
	
	public boolean allSelectedSummonsSigned = false;
	
	private CourtBO court;
	
	public List<AssociatedDocView> associatedDocViews;
	
	public List<AssociatedDocView> selectedAssociatedDocViews;
	
	public AssociatedDocView selectedAssociatedDocView;
	
	public List<DocumentViewBO> docViews;
	
	//public String jPRegNum;
	
	public Integer jPIdNum;
	
	public String jPPin;
	
	public List<DocumentViewBO> selectedDocViews = new ArrayList<DocumentViewBO>();
	
	protected Date complianceDate;
	
	public String roadCheckCancelReasonCode;
	 
	public String roadCheckCancelComment;
	
	public String roadCheckVerifyComment;
	
	public String roadCheckReprintComment;
	
	public String roadCheckReprintReasonCode;
	 
	public VerifyDetailsSecurityView verifyView;
	
	public ServedDetails servedByDetails;
	
	public String roleObserved;
	
	public String expandAllAccordTabs;
	
	
	public String getExpandAllAccordTabs() {
		return expandAllAccordTabs;
	}

	public void setExpandAllAccordTabs(String expandAllAccordTabs) {
		this.expandAllAccordTabs = expandAllAccordTabs;
	}

	public CourtBO getCourt() {
		return court;
	}

	public void setCourt(CourtBO court) {
		this.court = court;
	}

	public WreckingCompanyBO getWreckingCompany() {
		return wreckingCompany;
	}

	public void setWreckingCompany(WreckingCompanyBO wreckingCompany) {
		this.wreckingCompany = wreckingCompany;
	}

	public PoundBO getPound() {
		return pound;
	}

	public void setPound(PoundBO pound) {
		this.pound = pound;
	}

	public boolean isShouldYouUpload() {
		return shouldYouUpload;
	}

	public void setShouldYouUpload(boolean shouldYouUpload) {
		this.shouldYouUpload = shouldYouUpload;
	}

	public boolean isMobile() {
		return isMobile;
	}
	
	public boolean getIsMobile() {
		return isMobile;
	}

	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}
	
	

	public AssociatedDocView getSelectedAssociatedDocView() {
		return selectedAssociatedDocView;
	}

	public void setSelectedAssociatedDocView(
			AssociatedDocView selectedAssociatedDocView) {
		this.selectedAssociatedDocView = selectedAssociatedDocView;
	}

	public Boolean getShowAllQueriesPerformed() {
		return showAllQueriesPerformed;
	}

	public void setShowAllQueriesPerformed(Boolean showAllQueriesPerformed) {
		this.showAllQueriesPerformed = showAllQueriesPerformed;
	}
	
	

	public String getRoadCheckReprintComment() {
		return roadCheckReprintComment;
	}

	public void setRoadCheckReprintComment(String roadCheckReprintComment) {
		this.roadCheckReprintComment = roadCheckReprintComment;
	}

	public String getRoadCheckReprintReasonCode() {
		return roadCheckReprintReasonCode;
	}

	public void setRoadCheckReprintReasonCode(String roadCheckReprintReasonCode) {
		this.roadCheckReprintReasonCode = roadCheckReprintReasonCode;
	}

	public RoadCheckReviewSummaryBean()
	{
		super();
		
		associatedDocViews = new ArrayList<AssociatedDocView>();
		
		selectedAssociatedDocViews = new ArrayList<AssociatedDocView>();
		
		showAllQueriesPerformed = false;
		
		selectedAssociatedDocView = new AssociatedDocView(); 
		
		this.verifyView = new VerifyDetailsSecurityView();
		
		this.servedByDetails = new ServedDetails();
		
		this.reprintReasonNeeded = false;
		
		this.expandAllAccordTabs = "true";
	}
	
	public RoadCheckReviewSummaryBean(boolean reviewScreen)
	{
		super();
		
		associatedDocViews = new ArrayList<AssociatedDocView>();
		
		selectedAssociatedDocViews = new ArrayList<AssociatedDocView>();
		
		showAllQueriesPerformed = false;
		
		selectedAssociatedDocView = new AssociatedDocView(); 
		
		this.verifyView = new VerifyDetailsSecurityView();
		
		this.reviewScreen = reviewScreen;
		
		this.servedByDetails = new ServedDetails();
		
		this.reprintReasonNeeded = false;
		
		this.expandAllAccordTabs = "true";
	}


	public List<AssociatedDocView> getSelectedAssociatedDocViews() {
		return selectedAssociatedDocViews;
	}

	public void setSelectedAssociatedDocViews(
			List<AssociatedDocView> selectedAssociatedDocViews) {
		this.selectedAssociatedDocViews = selectedAssociatedDocViews;
	}

	public List<AssociatedDocView> getAssociatedDocViews() {
		return associatedDocViews;
	}

	public void setAssociatedDocViews(List<AssociatedDocView> associatedDocViews) {
		this.associatedDocViews = associatedDocViews;
	}


	public boolean isReviewScreen() {
		return reviewScreen;
	}

	public void setReviewScreen(boolean reviewScreen) {
		this.reviewScreen = reviewScreen;
	}

	public boolean isShouldGoToPrepareAndPrint() {
		return shouldGoToPrepareAndPrint;
	}

	public void setShouldGoToPrepareAndPrint(boolean shouldGoToPrepareAndPrint) {
		this.shouldGoToPrepareAndPrint = shouldGoToPrepareAndPrint;
	}

	public boolean isOpenReportWindow() {
		return openReportWindow;
	}

	public void setOpenReportWindow(boolean openReportWindow) {
		this.openReportWindow = openReportWindow;
	}

	public List<DocumentViewBO> getDocViews() {
		return docViews;
	}

	public void setDocViews(List<DocumentViewBO> docViews) {
		this.docViews = docViews;
	}

	/*public String getjPRegNum() {
		return jPRegNum;
	}

	public void setjPRegNum(String jPRegNum) {
		this.jPRegNum = jPRegNum;
	}*/

	public String getjPPin() {
		return jPPin;
	}

	public void setjPPin(String jPPin) {
		this.jPPin = jPPin;
	}

	public boolean isSummonsPresent() {
		return summonsPresent;
	}

	public void setSummonsPresent(boolean summonsPresent) {
		this.summonsPresent = summonsPresent;
	}

	public boolean isShouldYouPrint() {
		return shouldYouPrint;
	}

	public void setShouldYouPrint(boolean shouldYouPrint) {
		this.shouldYouPrint = shouldYouPrint;
	}

	public boolean isAlreadyPrinted() {
		return alreadyPrinted;
	}

	public void setAlreadyPrinted(boolean alreadyPrinted) {
		this.alreadyPrinted = alreadyPrinted;
	}

	public boolean isViewDetailsScreen() {
		return isViewDetailsScreen;
	}

	public void setViewDetailsScreen(boolean isViewDetailsScreen) {
		this.isViewDetailsScreen = isViewDetailsScreen;
	}

	public List<DocumentViewBO> getSelectedDocViews() {
		return selectedDocViews;
	}

	public void setSelectedDocViews(
			List<DocumentViewBO> selectedDocViews) {
		this.selectedDocViews = selectedDocViews;
	}

	public boolean isAllSelectedSummonsSigned() {
		return allSelectedSummonsSigned;
	}

	public void setAllSelectedSummonsSigned(boolean allSelectedSummonsSigned) {
		this.allSelectedSummonsSigned = allSelectedSummonsSigned;
	}

	public Date getComplianceDate() {
		return complianceDate;
	}

	public void setComplianceDate(Date complianceDate) {
		this.complianceDate = complianceDate;
	}

	public String getRoadCheckCancelReasonCode() {
		return roadCheckCancelReasonCode;
	}

	public void setRoadCheckCancelReasonCode(String roadCheckCancelReasonCode) {
		this.roadCheckCancelReasonCode = roadCheckCancelReasonCode;
	}

	public String getRoadCheckCancelComment() {
		return roadCheckCancelComment;
	}

	public void setRoadCheckCancelComment(String roadCheckCancelComment) {
		this.roadCheckCancelComment = roadCheckCancelComment;
	}

	public String getRoadCheckVerifyComment() {
		return roadCheckVerifyComment;
	}

	public void setRoadCheckVerifyComment(String roadCheckVerifyComment) {
		this.roadCheckVerifyComment = roadCheckVerifyComment;
	}

	public VerifyDetailsSecurityView getVerifyView() {
		return verifyView;
	}

	public void setVerifyView(VerifyDetailsSecurityView verifyView) 
	{
		this.verifyView = verifyView;
	}

	public ServedDetails getServedByDetails() {
		return servedByDetails;
	}

	public void setServedByDetails(ServedDetails servedByDetails) {
		this.servedByDetails = servedByDetails;
	}
	
	


	public boolean isReprintReasonNeeded() {
		return reprintReasonNeeded;
	}

	public void setReprintReasonNeeded(boolean reprintReasonNeeded) {
		this.reprintReasonNeeded = reprintReasonNeeded;
	}



	public String getRoleObserved() {
		return roleObserved;
	}

	public void setRoleObserved(String roleObserved) {
		this.roleObserved = roleObserved;
	}





	public class ServedDetails implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String servedByUserID;
		public Date servedOnDate;
		public AddressView servedAtAddressBO;
		
		
		
		public ServedDetails() 
		{
			super();
			this.servedAtAddressBO = new AddressView();
		}
	
		
		public String getServedByUserID() {
			return servedByUserID;
		}


		public void setServedByUserID(String servedByUserID) {
			this.servedByUserID = servedByUserID;
		}

		
	
		public Date getServedOnDate() {
			return servedOnDate;
		}


		public void setServedOnDate(Date servedOnDate) {
			this.servedOnDate = servedOnDate;
		}


		public AddressView getServedAtAddressBO() {
			return servedAtAddressBO;
		}


		public void setServedAtAddressBO(AddressView servedAtAddressBO) {
			this.servedAtAddressBO = servedAtAddressBO;
		}

		
		
		
	}





	public Integer getjPIdNum() {
		return jPIdNum;
	}

	public void setjPIdNum(Integer jPIdNum) {
		this.jPIdNum = jPIdNum;
	}
}
