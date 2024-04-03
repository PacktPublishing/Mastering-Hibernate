package com.packt.hibernate.ch9.ejb;

import java.util.List;

import javax.ejb.Remote;

import com.packt.hibernate.ch9.model.Person;


@Remote
public interface PersonDAO {

	public Person findById(long id);
	public List<Person> findAll();
	public List<Person> findByLastname(String lastname);
	public void save(Person person);
	public void update(Person person);
	public void delete(long id);
	public void delete(Object entity);
}
