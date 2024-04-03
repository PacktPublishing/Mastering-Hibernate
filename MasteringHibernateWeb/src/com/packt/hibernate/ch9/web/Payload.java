package com.packt.hibernate.ch9.web;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.packt.hibernate.ch9.model.Person;

@XmlRootElement
@XmlSeeAlso({Person.class})
public class Payload<T> {
	
	private List<T> data;

	public Payload() {}
	
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
