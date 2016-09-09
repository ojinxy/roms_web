package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.GoverningLawBO;
//import com.ibm.ws.sip.stack.context.RequestContext;



public class GoverningLawBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5537231439841168905L;
	
	private List<GoverningLawBO> governingLawList;
	//private List<GoverningLawBO> target;
	//private DualListModel<GoverningLawBO> listofGoverningLaw;
	private DualListModel<String> cities;
	private String words = "";
	String nword = "";
	private GoverningLawBO selectedgoverningLaw;
	
	
	public GoverningLawBean() {
		
		governingLawList = new ArrayList<GoverningLawBO>();
		selectedgoverningLaw = new GoverningLawBO();
		
		List<String> citiesSource = new ArrayList<String>();  
        List<String> citiesTarget = new ArrayList<String>();  
        
        
        citiesSource.add("Kingston");  
        citiesSource.add("MontegoBay");  
        citiesSource.add("Boston");  
        citiesSource.add("Toronto");  
        citiesSource.add("London");  
          
        cities = new DualListModel<String>(citiesSource, citiesTarget);  
		//target = new ArrayList<GoverningLawBO>();
       
	}
	
	public void concatWord(TransferEvent event)
	{
		//GoverningLawBean governingLaw = (GoverningLawBean)RequestContextHolder.getRequestContext().getFlowScope().get("governingLaw");
		
		FacesContext context = FacesContext.getCurrentInstance();
		GoverningLawBean governingLaw = context.getApplication().evaluateExpressionGet(context, "#{governingLaw}", GoverningLawBean.class);
		//Long id = employee.getId();
		
		nword = governingLaw.getWords();
		
		for (Object item : event.getItems()) {
			System.out.println("this is inside concatWord");
			System.out.println("nword before " + nword);
			nword += " " + (String) item;
			System.out.println("after " + nword);
			governingLaw.setWords(nword);
		}
		governingLaw.setWords(nword);
		
		//RequestContextHolder.getRequestContext().getFlowScope().put("governingLaw",governingLaw);
	}
	
	public void wordChanged(AjaxBehaviorEvent event) {
	   System.out.println("some thing changed");
	   GoverningLawBean governingLaw = (GoverningLawBean)RequestContextHolder.getRequestContext().getFlowScope().get("governingLaw");
	   nword = governingLaw.getWords();
	   //nword =(String)((UIOutput)event.getSource()).getValue();
	}
	
	
	public GoverningLawBean(List<GoverningLawBO> var) {
		governingLawList = var;
		selectedgoverningLaw = new GoverningLawBO();
		//target = new ArrayList<GoverningLawBO>();
		//listofGoverningLaw = new DualListModel<GoverningLawBO>(governingLawList, target);
	}

	public List<GoverningLawBO> getGoverningLawList() {
		return governingLawList;
	}

	public void setGoverningLawList(List<GoverningLawBO> governingLawList) {
		this.governingLawList = governingLawList;
	}

	public GoverningLawBO getSelectedgoverningLaw() {
		return selectedgoverningLaw;
	}

	public void setSelectedgoverningLaw(GoverningLawBO selectedgoverningLaw) {
		this.selectedgoverningLaw = selectedgoverningLaw;
	}

	public DualListModel<String> getCities() {
		return cities;
	}

	public void setCities(DualListModel<String> cities) {
		this.cities = cities;
	}

	public String getWords() {
		return words;	}

	public void setWords(String words) {
		this.words = words;
	}

	
		
	/*public DualListModel<GoverningLawBO> getListofGoverningLaw() {
		return listofGoverningLaw;
	}

	public void setListofGoverningLaw(
			DualListModel<GoverningLawBO> listofGoverningLaw) {
		this.listofGoverningLaw = listofGoverningLaw;
	}

	public List<GoverningLawBO> getTarget() {
		return target;
	}

	public void setTarget(List<GoverningLawBO> target) {
		this.target = target;
	}*/
	
	
	
}
