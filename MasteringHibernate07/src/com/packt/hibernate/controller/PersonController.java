package com.packt.hibernate.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.packt.hibernate.dao.PersonDAO;
import com.packt.hibernate.datatable.Payload;
import com.packt.hibernate.model.Person;

@Controller
public class PersonController {

	Logger logger = Logger.getLogger(PersonController.class);
	
	@Autowired
	private PersonDAO personDAO;
	
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String home() {
		return "static/index.html";
	}

	@RequestMapping(value = "/person", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Payload<Person> listPersons() {
		logger.info("list person resource!");
		return new Payload<Person>(personDAO.list());
	}

	@RequestMapping(value = "/person/{personId}", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Person findById(@PathVariable long personId) {
		logger.info("find person by id: " + personId);
		return personDAO.findById(personId);
	}

	@RequestMapping(value = "/person/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody String addPerson(@RequestBody Person person) {
		logger.info("adding new person: " + person);
		personDAO.save(person);
		return "SUCCESS";
	}

	@RequestMapping(value = "/person/delete/{personId}", method=RequestMethod.POST)
	public @ResponseBody String deletePerson(@PathVariable long personId) {
		logger.info("deleting person: " + personId);
		personDAO.delete(personId);
		return "SUCCESS";
	}

	@RequestMapping(value = "/person/update/{personId}", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody String updatePerson(@PathVariable long personId, @RequestBody Person person) {
		logger.info("updating person by id: " + personId + "\n" + person);
		personDAO.update(person);
		return "SUCCESS";
	}

}
