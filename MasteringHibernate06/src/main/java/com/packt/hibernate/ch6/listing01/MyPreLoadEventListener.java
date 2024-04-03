package com.packt.hibernate.ch6.listing01;

import org.hibernate.event.spi.PreLoadEvent;
import org.hibernate.event.spi.PreLoadEventListener;

public class MyPreLoadEventListener implements PreLoadEventListener {

	public void onPreLoad(PreLoadEvent event) {
		System.out.println("**** About to load entity with ID:" + event.getId());
	}

}
