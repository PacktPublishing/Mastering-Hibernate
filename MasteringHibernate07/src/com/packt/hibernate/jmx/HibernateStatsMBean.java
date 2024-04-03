package com.packt.hibernate.jmx;

public interface HibernateStatsMBean {
	public void resetStatistics();
	public void enableStatistic();
	public void disableStatistic();
	public boolean isStatisticsEnabled();
	public String getStartTime();
	public long getCounnectionCount();
	public long getSessionOpenCount();
	public long getSessionCloseCount();
	public long getEntityLoadCount();
	public long getEntityFetchCount();
	public long getEntityDeleteCount();
	
}
