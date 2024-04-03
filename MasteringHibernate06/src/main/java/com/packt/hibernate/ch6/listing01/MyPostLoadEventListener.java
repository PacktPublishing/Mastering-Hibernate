package com.packt.hibernate.ch6.listing01;

import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;

public class MyPostLoadEventListener implements PostLoadEventListener {
	public void onPostLoad(PostLoadEvent event) {
		Object entity = event.getEntity();
		System.out.println("**** Just loaded entity with ID:" + event.getId());
		System.out.println(entity);		
	}
}
