package com.packt.hibernate.ch9.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Transactional;

import com.packt.hibernate.ch9.model.Person;

public class PersonDAOImpl implements PersonDAO {
	Logger logger = Logger.getLogger(PersonDAOImpl.class);
	
    private SessionFactory sessionFactory;

    public PersonDAOImpl(SessionFactory sessionFactory) {
    	this.sessionFactory = sessionFactory;
    }
    
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Person> list() {
		logger.debug("listing person entities");
		return sessionFactory.getCurrentSession()
				.createCriteria(Person.class)
				.addOrder(Order.asc("id"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	@Override
	@Transactional
	public Person findById(long personId) {
		return (Person) sessionFactory.getCurrentSession().get(Person.class, personId);
	}

	@Override
	@Transactional
	public void save(Person person) {
		sessionFactory.getCurrentSession().save(person);
	}
	
	@Override
	@Transactional
	public void update(Person person) {
		sessionFactory.getCurrentSession().saveOrUpdate(person);
	}

	@Override
	@Transactional
	public void delete(long personId) {
		Person person = (Person) sessionFactory.getCurrentSession().get(Person.class, personId);
		sessionFactory.getCurrentSession().delete(person);
	}
}
