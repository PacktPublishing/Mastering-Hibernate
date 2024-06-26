package com.packt.hibernate.ch8.listing02;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.packt.hibernate.model.Address;
import com.packt.hibernate.model.Child;
import com.packt.hibernate.model.Person;

public class HibernateUtil {

    private static SessionFactory sessionFactory = buildSessionFactory();
    private static ServiceRegistry serviceRegistry;
    
    private static synchronized SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml in resources directory
        	Configuration configuration = new Configuration().configure()
                	.addAnnotatedClass(Person.class)
                	.addAnnotatedClass(Child.class)
                	.addAnnotatedClass(Address.class);
        	StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
        	applySettings(configuration.getProperties());
        	serviceRegistry = builder.build();
        	return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static synchronized SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void stopService() {
    	StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }
}