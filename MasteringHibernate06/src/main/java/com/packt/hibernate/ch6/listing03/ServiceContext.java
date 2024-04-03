package com.packt.hibernate.ch6.listing03;

public class ServiceContext {

	private static ThreadLocal<String> username = new ThreadLocal<String>();

	public static String getUsername() {
		return username.get();
	}

	public static void setUsername(String user) {
		username.set(user);
	}
}
