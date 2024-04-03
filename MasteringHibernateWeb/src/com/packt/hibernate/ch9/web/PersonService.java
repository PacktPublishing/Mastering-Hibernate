package com.packt.hibernate.ch9.web;

import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.packt.hibernate.ch9.ejb.PersonDAO;
import com.packt.hibernate.ch9.model.Person;

@Path("/person")
public class PersonService {
	private Logger logger = Logger.getLogger(PersonService.class);

	@EJB(beanName = "personBean")
	private PersonDAO personDAO;

	public PersonService() {
		init();
	}

	public synchronized void init() {
		if (personDAO != null) {
			return;
		}

		try {
			personDAO = (PersonDAO) InitialContext.doLookup("java:app/MasteringHibernateEJB-1/personBean");
		} catch (NamingException e) {
			logger.error(e);
		}
	}

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Person> list() {
		 List<Person> persons = personDAO.findAll();
		return persons;
	}

	@GET
	@Path("/view/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Person view(@PathParam("param") long personId) {
		Person person = personDAO.findById(personId);
		return person;
	}

	@POST
	@Path("/add")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String add(Person person) {
		logger.info("**** saving person: " + person);
		personDAO.save(person);
		return "SUCCESS";
	}
	
	@POST
	@Path("/update")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String update(Person person) {
		logger.info("**** updating person: " + person);
		personDAO.update(person);
		return "SUCCESS";
	}
	
	@GET
	@Path("/delete/{param}")
	@Produces(MediaType.TEXT_PLAIN)
	public String delete(@PathParam("param") long personId) {
		personDAO.delete(personId);
		return "SUCCESS";
	}
}
