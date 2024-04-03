package com.packt.hibernate.ch6.listing03;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.transaction.spi.LocalStatus;
import org.hibernate.type.Type;

public class MyInterceptor extends EmptyInterceptor {

	@Override
	public boolean onLoad(Object entity,
			Serializable id,
			Object[] state,
			String[] propertyName,
			Type[] types) {

		if (entity instanceof Person) {
			AuditTrail audit = new AuditTrail();
			audit.setUsername(ServiceContext.getUsername());
			audit.setEntityId((Long) id);
			
			boolean transactionStarted = false;

			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction transaction = session.getTransaction();
			try {
				if (!(transaction.isActive() 
						|| transaction.getLocalStatus().equals(LocalStatus.ACTIVE))) {
					System.out.println("*** starting my own transaction");
					transaction.begin();
					transactionStarted = true;
					session.save(audit);
					transaction.commit();
				}
				else {
					System.out.println("*** using existing transaction");
					session.save(audit);
				}
			}
			catch (Exception e) {
				if (transactionStarted) {
					transaction.rollback();
				}
			}			
		}
		
		return false;
	}
}
