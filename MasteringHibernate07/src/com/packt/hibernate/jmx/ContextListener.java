package com.packt.hibernate.jmx;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

//@WebListener
public class ContextListener implements ServletContextListener {

	@Override
    public void contextInitialized(final ServletContextEvent sce) {    	
    	ApplicationContext appContext = WebApplicationContextUtils
    			.getRequiredWebApplicationContext(sce.getServletContext());
    	
    	try {
        	HibernateStats hibernateStats = (HibernateStats) appContext.getBean("hibernateStats");
        	if (hibernateStats == null) {
            	System.err.println("***** null bean!!!!");
            	return;
        	}
        	MBeanManager.instance().initialize(hibernateStats);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
    	MBeanManager.instance().destroy();    	
	}

}
