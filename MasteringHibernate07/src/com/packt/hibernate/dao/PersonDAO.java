package com.packt.hibernate.dao;

import java.util.List;

import com.packt.hibernate.model.Person;

public interface PersonDAO {
    public List<Person> list();
	public Person findById(long personId);
	public void save(Person person);
	public void update(Person person);
	public void delete(long personId);
}
