package com.packt.hibernate.jmx;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

public class HibernateStats implements HibernateStatsMBean {

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public String getStartTime() {
		if (sessionFactory == null) {
			return "error! session factory is null!";
		}

		Statistics stats = sessionFactory.getStatistics();
		Date startTime = new Date(stats.getStartTime());
		return startTime.toString();
	}
		
	@Override
	public void resetStatistics() {
		if (sessionFactory == null) {
			return;
		}
		
		sessionFactory.getStatistics().clear();
	}

	@Override
	public void enableStatistic() {
		if (sessionFactory == null) {
			return;
		}
		
		sessionFactory.getStatistics().setStatisticsEnabled(true);
	}

	@Override
	public void disableStatistic() {
		if (sessionFactory == null) {
			return;
		}
		
		sessionFactory.getStatistics().setStatisticsEnabled(false);
	}

	@Override
	public boolean isStatisticsEnabled() {
		if (sessionFactory == null) {
			return false;
		}
		
		return sessionFactory.getStatistics().isStatisticsEnabled();
	}

	
	@Override
	public long getCounnectionCount() {
		if (sessionFactory == null) {
			return 0;
		}

		Statistics stats = sessionFactory.getStatistics();
		return stats.getConnectCount();
	}

	@Override
	public long getSessionOpenCount() {
		if (sessionFactory == null) {
			return 0;
		}

		Statistics stats = sessionFactory.getStatistics();
		return stats.getSessionOpenCount();
	}

	@Override
	public long getSessionCloseCount() {
		if (sessionFactory == null) {
			return 0;
		}

		Statistics stats = sessionFactory.getStatistics();
		return stats.getSessionCloseCount();
	}

	@Override
	public long getEntityLoadCount() {
		if (sessionFactory == null) {
			return 0;
		}

		Statistics stats = sessionFactory.getStatistics();
		return stats.getEntityLoadCount();
	}

	@Override
	public long getEntityFetchCount() {
		if (sessionFactory == null) {
			return 0;
		}

		Statistics stats = sessionFactory.getStatistics();
		return stats.getEntityFetchCount();
	}

	@Override
	public long getEntityDeleteCount() {
		if (sessionFactory == null) {
			return 0;
		}

		Statistics stats = sessionFactory.getStatistics();
		return stats.getEntityDeleteCount();
	}

}
