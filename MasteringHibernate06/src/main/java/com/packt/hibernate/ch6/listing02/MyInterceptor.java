package com.packt.hibernate.ch6.listing02;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class MyInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean onLoad(Object entity,
			Serializable id,
			Object[] state,
			String[] propertyName,
			Type[] types) {
		
		System.out.println("**** Interceptor: id:" + id + ", state: " + state.length + ", propertyName: " + propertyName.length + ", types: " + types.length);
		
		for(String s:propertyName) {
			System.out.println("---------> property: " + s);
		}
		
		for(Object o:state) {
			System.out.println("---------> state: " + o);
		}
		
		return false;
	}
}
