package com.packt.hibernate.ch9.controller;

import java.util.List;

/**
 * Used to package a list to be returned as JSON object to jQuery Data table. Used by Spring MVC Controllers.
 * 
 */
public class Payload<T> {
	
	private List<T> data;

	public Payload(List<T> payload) {
		data = payload;
	}
	
	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
