package com.packt.hibernate.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

public class MBeanManager {

	private HibernateStats hibernateStats;
	private ObjectName objectName;

	private static MBeanManager manager = new MBeanManager();

	private MBeanManager() {
	}

	public static MBeanManager instance() {
		return manager;
	}

	public synchronized void initialize(HibernateStats hibernateStatsBean) {
		if (hibernateStats != null) {
			return;
		}
		this.hibernateStats = hibernateStatsBean;

		final MBeanServer mbeanServer = ManagementFactory
				.getPlatformMBeanServer();
		try {
			objectName = new ObjectName("com.packt.hibernate.jmx:type=hibernateStats");
			mbeanServer.registerMBean(hibernateStats, objectName);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void destroy() {
		final MBeanServer mbeanServer = ManagementFactory
				.getPlatformMBeanServer();
		try {
			mbeanServer.unregisterMBean(objectName);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		finally {
			hibernateStats = null;
		}
	}
}
