package com.packt.hibernate.ch9.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

import com.packt.hibernate.ch9.model.Child;
import com.packt.hibernate.ch9.model.Person;

@Stateless(name="personBean")
public class PersonBean implements PersonDAO {
	private static Logger logger = Logger.getLogger(PersonBean.class.getName());
	
	@PersistenceContext(unitName = "MasteringHibernate")
//	private Session session;
	private EntityManager entityManager;
	
	@Override
	public Person findById(long id) {
		logger.info("finding person by ID");
		Session session = entityManager.unwrap(Session.class);
		return (Person) session.get(Person.class, id);
//		return entityManager.find(Person.class, id);
	}

	@Override
	public List<Person> findAll() {
		logger.info("finding all persons");
//		List<Person> persons = session.createQuery("from Person").list();
		List<Person> persons = entityManager.createQuery("from Person", Person.class).getResultList();
		return persons;
	}

	@Override
	public void save(Person person) {
		logger.info("saving person: " + person);
//		session.save(person);
		entityManager.persist(person);
	}

	@Override
	public void update(Person person) {
		logger.info("updating person: " + person);
//		session.saveOrUpdate(person);
		entityManager.merge(person);
	}

	@Override
	public List<Person> findByLastname(String lastname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long id) {
		logger.info("deleting person with id: " + id);

//		Person person = (Person) session.get(Person.class, id);
		Person person = entityManager.find(Person.class, id);
		if (person == null) {
			throw new RuntimeException("Object not found!");
		}
		for(Child child: person.getChildren()) {
//			session.delete(child);
			delete(child);
		}
		delete(person);
	}

	@Override
	public void delete(Object entity) {
		logger.info("deleting entity: " + entity.toString());
//		session.delete(person);
		entityManager.remove(entity);
	}

}
