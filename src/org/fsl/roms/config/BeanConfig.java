package org.fsl.roms.config;

//import org.fsl.roms.service.action.ArteryMaintenanceServiceAction;
import org.fsl.roms.service.action.ArteryMaintenanceServiceAction;
import org.fsl.roms.service.action.ConfigurationMaintenanceServiceAction;
import org.fsl.roms.service.action.CourtMaintenanceServiceAction;
import org.fsl.roms.service.action.DocumentManagerServiceAction;
import org.fsl.roms.service.action.DownloadsMaintenanceAction;
import org.fsl.roms.service.action.GoverningLawMaintenanceServiceAction;
import org.fsl.roms.service.action.ItaExaminerMaintenanceServiceAction;
import org.fsl.roms.service.action.JpMaintenanceServiceAction;
import org.fsl.roms.service.action.LocationMaintenanceServiceAction;
import org.fsl.roms.service.action.OffenceMaintenanceServiceAction;
import org.fsl.roms.service.action.ParishMaintenanceServiceAction;
import org.fsl.roms.service.action.PoundMaintenanceServiceAction;
import org.fsl.roms.service.action.ReasonMaintenanceServiceAction;
import org.fsl.roms.service.action.RoadCheckServiceAction;
import org.fsl.roms.service.action.RoadOperationsServiceAction;
import org.fsl.roms.service.action.StrategyMaintenanceServiceAction;
import org.fsl.roms.service.action.TaVehicleMaintenanceServiceAction;
import org.fsl.roms.service.action.UserSetupServiceAction;
import org.fsl.roms.service.action.WreckingCompanyMaintenanceServiceAction;
import org.fsl.roms.view.ArteryBean;
import org.fsl.roms.view.CourtBean;
import org.fsl.roms.view.GoverningLawBean;
import org.fsl.roms.view.ReasonBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import fsl.ta.toms.roms.webservices.Maintenance;
import fsl.ta.toms.roms.webservices.ReferenceCode;


@Configuration
//@EnableCaching
public class BeanConfig {

	@Bean(name = "messageSource")
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        return source;
    }
	 
   /* @Bean
    public ArteryMaintenanceServiceAction arteryMaintenanceServiceAction() {
    	
        return new ArteryMaintenanceServiceAction(maintenance());
    }
    
    @Bean
    public ConfigurationMaintenanceServiceAction configurationMaintenanceServiceAction(){
    	return new ConfigurationMaintenanceServiceAction(maintenance());
    }
    
    
    @Bean
    public CourtMaintenanceServiceAction courtMaintenanceServiceAction(){
    	return new CourtMaintenanceServiceAction(maintenance());
    }
    
    
    @Bean
    public GoverningLawMaintenanceServiceAction governinglawMaintenanceServiceAction(){
    	return new GoverningLawMaintenanceServiceAction(maintenance());
    }
    
   
    @Bean
    public ItaExaminerMaintenanceServiceAction itaexaminerMaintenanceServiceAction(){
    	return new ItaExaminerMaintenanceServiceAction(maintenance());
    }
    
   
    @Bean
    public JpMaintenanceServiceAction jpMaintenanceServiceAction(){
    	return new JpMaintenanceServiceAction(maintenance());
    }
    
    
    @Bean
    public LocationMaintenanceServiceAction locationMaintenanceServiceAction(){
    	return new LocationMaintenanceServiceAction(maintenance());
    }
    
   
    @Bean
    public OffenceMaintenanceServiceAction offenceMaintenanceServiceAction(){
    	return new OffenceMaintenanceServiceAction(maintenance());
    }
    
    
    @Bean
    public ParishMaintenanceServiceAction parishMaintenanceServiceAction(){
    	return new ParishMaintenanceServiceAction(maintenance());
    }
    
   
    @Bean
    public PoundMaintenanceServiceAction poundMaintenanceServiceAction(){
    	return new PoundMaintenanceServiceAction(maintenance());
    }
    
    
    @Bean
    public ReasonMaintenanceServiceAction reasonMaintenanceServiceAction(){
    	return new ReasonMaintenanceServiceAction(maintenance());
    }
    
    @Bean
    public StrategyMaintenanceServiceAction strategyMaintenanceServiceAction(){
    	return new StrategyMaintenanceServiceAction(maintenance());
    }
    
     
    @Bean
    public TaVehicleMaintenanceServiceAction tavehicleMaintenanceServiceAction(){
    	return new TaVehicleMaintenanceServiceAction(maintenance());
    }
    
    
    @Bean
    public WreckingCompanyMaintenanceServiceAction wreckingcompanyMaintenanceServiceAction(){
    	return new WreckingCompanyMaintenanceServiceAction(maintenance());
    }
    
    @Bean
    public Maintenance maintenance(){
    	return new Maintenance();
    }
    
    @Bean
    public ReferenceCode referenceCodeService(){
    	return new ReferenceCode();
    }
    
    
    @Bean
    public RoadOperationsServiceAction roadOperationsServiceAction()
    {
    	return new RoadOperationsServiceAction();
    }
    
    @Bean
    public DocumentManagerServiceAction documentManagerServiceAction()    {
    	
			
    	return  new DocumentManagerServiceAction() ;
    }
    
    
    @Bean
    public RoadCheckServiceAction roadCheckServiceAction()
    {
    	return new RoadCheckServiceAction();
    }
   
    @Bean
    public ArteryBean arteryBean()
    {
    	return new ArteryBean();
    }
    
    @Bean
    public CourtBean courtBean()
    {
    	return new CourtBean();
    }
    
    @Bean
    public GoverningLawBean governingLawBean()
    {
    	return new GoverningLawBean();
    }
    
    @Bean
    public ReasonBean reasonBean()
    {
    	return new ReasonBean();
    }
   
    @Bean
    public UserSetupServiceAction userSetupServiceAction()
    {
    	return new UserSetupServiceAction();
    }
    
	@Bean
	public DownloadsMaintenanceAction downloadsMaintenanceAction() {
		return new DownloadsMaintenanceAction();
	}*/

}     