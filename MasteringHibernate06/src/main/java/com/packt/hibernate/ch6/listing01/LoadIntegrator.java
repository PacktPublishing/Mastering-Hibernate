package com.packt.hibernate.ch6.listing01;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class LoadIntegrator implements Integrator {

	public void integrate(Configuration configuration,
			SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {

		final EventListenerRegistry listenerRegistry = 
				serviceRegistry.getService(EventListenerRegistry.class);

		listenerRegistry.appendListeners(EventType.PRE_LOAD, 
										 new MyPreLoadEventListener());
		
		listenerRegistry.appendListeners(EventType.POST_LOAD, 
										 new MyPostLoadEventListener());
	}

	public void integrate(MetadataImplementor metadata,
			SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {
	}

	public void disintegrate(SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {
	}
}
