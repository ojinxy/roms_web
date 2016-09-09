package fsl.ta.toms.roms.service.impl;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.impl.dom.SOAPHeaderBlockImpl;
import org.apache.axiom.soap.impl.dom.soap11.SOAP11Factory;
import org.apache.axiom.soap.impl.dom.soap11.SOAP11HeaderBlockImpl;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.xerces.parsers.DOMParser;
import org.fsl.webservice.codes.FslCodesServiceStub;
import org.fsl.webservice.codes.FslWebServiceException;
import org.fsl.webservice.codes.GetCodeDescription;
import org.fsl.webservice.codes.GetCodeDescriptionResponse;
import org.fsl.webservice.codes.GetCodes;
import org.fsl.webservice.codes.GetCodesResponse;
import org.fsl.webservice.codes.GetUpdatedCodes;
import org.fsl.webservice.codes.GetUpdatedCodesResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fsl.ta.toms.roms.constants.Constants;
import fsl.ta.toms.roms.constants.Constants.TTMSCodeTypes;
import fsl.ta.toms.roms.constants.Constants.WebServiceConfig;
import fsl.ta.toms.roms.dao.DAOFactory;
import fsl.ta.toms.roms.dataobjects.ConfigurationDO;
import fsl.ta.toms.roms.dataobjects.EventAuditDO;
import fsl.ta.toms.roms.dataobjects.ProcessControlDO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.service.ServiceFactory;
import fsl.ta.toms.roms.service.TTMSCodeService;
import fsl.ta.toms.roms.util.DateUtils;
import fsl.ta.toms.roms.util.StringUtil;


/**
 * 
 * @author rbrooks
 * Created Date: May 30, 2013
 * This class implements calls to the TTMS Code Web Service
 * 
 */
public class TTMSCodeServiceImpl implements TTMSCodeService{

	private DAOFactory daoFactory;
	private ServiceFactory serviceFactory;
	 
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	
	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}


	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
	
	
	@Override
	public String updateTTMSCode(String currentUser, String ttmsCodeType, Integer eventCode ) throws NoRecordFoundException{
		
		String results = "";
		Date lastRun = null;
		
		if(!StringUtil.isSet(currentUser))
		{
			currentUser = "SYSTEM";
		}
		
		//initialize service by adding username, pass word, enpoint url, etc
		FslCodesServiceStub serviceStub = initTTMSService();

		
		//Establish of this event was previously run successfully or not
		lastRun = getLastRunDate(eventCode);
		 //had to subtract one day here because the TTMS Service  
		String lastRunStr = "";					   //picks up changes after the last run date. so would have missed changes	
		if (lastRun != null)					   //made on the same day as last run.	
		{	lastRun = DateUtils.minusDays(lastRun, 1);	
			lastRunStr = lastRun.toString().substring(0, 10);	
			lastRun = DateUtils.addDays(lastRun, 1);	
		}
		
		
		try{
		
			//process web service response
			GetCodesResponse codesResponse = new GetCodesResponse();
			GetUpdatedCodesResponse updatedCodesResponse = new GetUpdatedCodesResponse();
			
			
			
			//IF there was no previous run, just do a general run
			if(lastRun == null)
			{
				GetCodes getCodesRequest = new GetCodes();
				getCodesRequest.setCodeTypeId(ttmsCodeType);
				codesResponse = serviceStub.getCodes(getCodesRequest);
				results = codesResponse.getGetCodesReturn();
			}else //otherwise, get only ones updated since last run
			{	
				GetUpdatedCodes getUpdatedCodesRequest = new GetUpdatedCodes();
				getUpdatedCodesRequest.setCodeTypeId(ttmsCodeType);
				getUpdatedCodesRequest.setSinceDate(lastRunStr);
				updatedCodesResponse = serviceStub.getUpdatedCodes(getUpdatedCodesRequest);
				results = updatedCodesResponse.getGetUpdatedCodesReturn();
				
			}
		
		} catch (IOException e) 
		{
			e.printStackTrace();
		    // handle IOException 
		} catch (FslWebServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NoRecordFoundException("No Records Found for TTMS Code " + ttmsCodeType);
		}
			
		
		return results;
}
	
	
	
	
	@Override
	public String getTTMSCodeDescription(String currentUser, String ttmsCodeType, String code) throws NoRecordFoundException{
		
		String codeDesc = null;
		String result = null;
		
		//initialize service by adding username, pass word, enpoint url, etc
		FslCodesServiceStub serviceStub = initTTMSService();

		
		try{
		
			//process web service response
			GetCodeDescriptionResponse codesResponse = new GetCodeDescriptionResponse();
			
			
			GetCodeDescription getCodeDescRequest = new GetCodeDescription();
			getCodeDescRequest.setCodeTypeId(ttmsCodeType);
			getCodeDescRequest.setCode(code);
			codesResponse = serviceStub.getCodeDescription(getCodeDescRequest);
			result = codesResponse.getGetCodeDescriptionReturn();
			
			//need to replace ampersand as it causes DOM Parser to fail
			result = result.replace("&", "and");
					
			//Process results
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new java.io.StringReader(result)));
			Document doc = parser.getDocument();
			
			NodeList codeDescs = doc.getElementsByTagName("code_record");
			   
		  
		    for(int i=0; i<codeDescs.getLength(); i++) 
		    {
    		   Element eachDesc = (Element)codeDescs.item(i);
           
    		   if (ttmsCodeType.equals(TTMSCodeTypes.POLICE_STATION))
    			   codeDesc = getChildValue(eachDesc, "polStationDesc", 0);
    		   
    		   if (ttmsCodeType.equals(TTMSCodeTypes.POLICE_RANK))
    			   codeDesc = getChildValue(eachDesc, "description", 0);
		    }
			
		} catch (IOException e) {
		    // handle IOException 
		} catch (FslWebServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NoRecordFoundException("No Records Found for TTMS Code " + ttmsCodeType);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		return codeDesc;
	}
	


	@Override
	public HashMap<String,String> getTTMSPoliceRanks() throws NoRecordFoundException{
		
		String results = "";
		Date lastRun = null;
		HashMap<String,String> ranks = new HashMap<String,String>();
		
		//initialize service by adding username, pass word, enpoint url, etc
		FslCodesServiceStub serviceStub = initTTMSService();
		
		try{
		
			//process web service response
			GetCodesResponse codesResponse = new GetCodesResponse();
			GetUpdatedCodesResponse updatedCodesResponse = new GetUpdatedCodesResponse();
			
			
			
			//IF there was no previous run, just do a general run
			GetCodes getCodesRequest = new GetCodes();
			getCodesRequest.setCodeTypeId(Constants.TTMSCodeTypes.POLICE_RANK);
			codesResponse = serviceStub.getCodes(getCodesRequest);
			results = codesResponse.getGetCodesReturn();

			
			
			if(StringUtil.isSet(results))
			{
				results = results.replace("&", "and");
			
				//Process results
				DOMParser parser = new DOMParser();
				parser.parse(new InputSource(new java.io.StringReader(results)));
				Document doc = parser.getDocument();
				String codeDesc;
				String code;
				
				NodeList codeDescs = doc.getElementsByTagName("code_record");
				   
			  
			    for(int i=0; i<codeDescs.getLength(); i++) 
			    {
	    		   Element eachDesc = (Element)codeDescs.item(i);
	           
	    		   	code = getChildValue(eachDesc, "code", 0);
	    		   	codeDesc = getChildValue(eachDesc, "description", 0);
	    		   
	    		   	ranks.put(code, codeDesc);
			    }
			}else
			{
				return null;
			}
			
			
		} catch (IOException e) {
		    // handle IOException 
		} catch (FslWebServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NoRecordFoundException("No Records Found for TTMS Code " + Constants.TTMSCodeTypes.POLICE_RANK);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		return ranks;
}
	
	
	
	
	
	@Override
	public HashMap<String,String> getTTMSPoliceStations() throws NoRecordFoundException{
		
		String results = "";
		Date lastRun = null;
		HashMap<String,String> stations = new HashMap<String,String>();
		
		//initialize service by adding username, pass word, enpoint url, etc
		FslCodesServiceStub serviceStub = initTTMSService();
		
		try{
		
			//process web service response
			GetCodesResponse codesResponse = new GetCodesResponse();
			GetUpdatedCodesResponse updatedCodesResponse = new GetUpdatedCodesResponse();
			
			
			
			//IF there was no previous run, just do a general run
			GetCodes getCodesRequest = new GetCodes();
			getCodesRequest.setCodeTypeId(Constants.TTMSCodeTypes.POLICE_STATION);
			codesResponse = serviceStub.getCodes(getCodesRequest);
			results = codesResponse.getGetCodesReturn();

			
			
			if(StringUtil.isSet(results))
			{
				results = results.replace("&", "and");
			
				
				//Process results
				DOMParser parser = new DOMParser();
				parser.parse(new InputSource(new java.io.StringReader(results)));
				Document doc = parser.getDocument();
				String codeDesc;
				String code;
				
				NodeList codeDescs = doc.getElementsByTagName("code_record");
				   
			  
			    for(int i=0; i<codeDescs.getLength(); i++) 
			    {
	    		   Element eachDesc = (Element)codeDescs.item(i);
	           
	    		   	code = getChildValue(eachDesc, "polStationCode", 0);
	    		    codeDesc = getChildValue(eachDesc, "polStationDesc", 0);
	    		   
	    		    stations.put(code, codeDesc);
		    }
			}else
			{
				return null;
			}
			
			
		} catch (IOException e) {
		    // handle IOException 
		} catch (FslWebServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NoRecordFoundException("No Records Found for TTMS Code " + Constants.TTMSCodeTypes.POLICE_STATION);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		return stations;
}
	
	
	
	private FslCodesServiceStub initTTMSService()
	{
		FslCodesServiceStub serviceStub = null;
		
		try{
			//retrieve web service settings, url, uname, pwd, etc
			String username = ((ConfigurationDO)daoFactory.getConfigurationDAO().find(ConfigurationDO.class, WebServiceConfig.TTMS_WS_USER)).getValue();
			String password = ((ConfigurationDO)daoFactory.getConfigurationDAO().find(ConfigurationDO.class, WebServiceConfig.TTMS_WS_PASSWD)).getValue();
			String endPoint = ((ConfigurationDO)daoFactory.getConfigurationDAO().find(ConfigurationDO.class, WebServiceConfig.TTMS_WS_URL)).getValue();
			
			serviceStub = new FslCodesServiceStub(endPoint);
			
			//add security header to soap message
			ServiceClient client = serviceStub._getServiceClient();
						
			SOAP11Factory factory = new SOAP11Factory();
			OMNamespace SecurityElementNamespace = factory.createOMNamespace("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse");
	
			OMElement usernameTokenEl = factory.createOMElement("UsernameToken", SecurityElementNamespace);
			OMElement usernameEl = factory.createOMElement("Username", SecurityElementNamespace);
			OMElement passwordEl = factory.createOMElement("Password", SecurityElementNamespace);
			usernameEl.setText(username);
			passwordEl.setText(password);
			usernameTokenEl.addChild(usernameEl);
			usernameTokenEl.addChild(passwordEl);
	
			SOAPHeaderBlockImpl block = new SOAP11HeaderBlockImpl("Security", SecurityElementNamespace, factory);
			block.addChild(usernameTokenEl);
	
			client.addHeader(block); 
			
			org.apache.axis2.client.Options options = client.getOptions();
			options.setTimeOutInMilliSeconds(600000);
			
			
		}	catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serviceStub;
	}
	
	
	
	private Date getLastRunDate(Integer event)
	{
		Date createDateStr  = null;
		
		List<ProcessControlDO> events = daoFactory.getSchedulerDAO().findLastEventByCode(event);
		
	
		if(!events.isEmpty())
		{	int lastPos = (events.size()-1);
			createDateStr = ((ProcessControlDO)(events.get(lastPos))).getRunStartDtime();
			
		}
		
				
		return createDateStr;
		
	}
	
	
	private String getChildValue(Element parent, String childNodeName, int index)
	{
		String response = null;
		
		NodeList list = parent.getElementsByTagName(childNodeName);
		
		if(list.getLength() > 0)
		{
			Node node = list.item(index);
			if(node.hasChildNodes())
			{ 
				response = node.getFirstChild().getNodeValue().trim();
			} 
		}
		
		return response;
	}
	
}
