package com.packt.hibernate.ch9.servlet;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.packt.hibernate.ch9.ejb.PersonDAO;
import com.packt.hibernate.ch9.model.Person;

@WebServlet("/PersonServlet")
public class PersonServlet extends HttpServlet {
	public Logger logger = Logger.getLogger(PersonServlet.class);
	private static final long serialVersionUID = 1L;

	@EJB(beanName="personBean")
	private PersonDAO personDAO;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Person> persons = personDAO.findAll();
		
		if (persons != null) {
			logger.info("**** found persons: " + persons.size());
		}
		response.getWriter().println("Read!");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}
