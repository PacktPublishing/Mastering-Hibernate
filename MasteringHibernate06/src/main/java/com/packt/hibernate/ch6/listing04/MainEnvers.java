package com.packt.hibernate.ch6.listing04;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.configuration.spi.AuditConfiguration;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.order.AuditOrder;
import org.hibernate.envers.tools.Pair;

public class MainEnvers {
	
	public static final String[] firstnames = {
		"John",
		"Steve",
		"Kevin",
		"Jill",
		"Sara",
		"Robert",
		"Dillon",
		"Elizabeth",
		"Mary",
		"Maria",
		"Sherry"
	};

	public static final String[] lastnames = {
		"Smith",
		"Robertson",
		"Johnson",
		"Dillon",
		"Stein",
		"Morgan",
		"Duncan",
		"Madison",
		"Ashcroft",
		"Shield",
		"Weinstein"
	};

	public static final String[] street = {
		"Main Street",
		"Eastern Avenue",
		"Maple Avenue",
		"Park Avenue",
		"Wisconsin Avenue",
		"Elm Street",
		"Willow Lane"
	};

	public static final String[] city = {
		"Washington",
		"Fairfax",
		"Arlington",
		"Alexandria",
		"Falls Church"
	};
	
	public static final String[] employer = {
		"IBM",
		"GOOGLE",
		"APPLE",
		"MICROSOFT",
		"ORACLE"
	};
	
	public static int randomNumberBetween(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min; 
	}
	
	public static Date randomDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        int year = randomNumberBetween(1960, 1999);
        int day = randomNumberBetween(1, calendar.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));
        calendar.set(GregorianCalendar.YEAR, year);
        calendar.set(GregorianCalendar.DAY_OF_YEAR, day);
		return calendar.getTime();
	}
	
	public static String randomSsn() {
		int first = randomNumberBetween(100, 999);
		int second = randomNumberBetween(10, 99);
		int third = randomNumberBetween(1000, 9999);
		
		return first + "-" + second + "-" + third;
	}
	
	
	public static EmploymentHistory randomWorkHistory() {
		EmploymentHistory workHistory = new EmploymentHistory();
		workHistory.setEmployerName(employer[randomNumberBetween(0, employer.length)]);
		workHistory.setStartDate(randomDate());
		workHistory.setEndDate(randomDate()); // oh well
		return workHistory;
	}
	
	public static Person randomPerson() {
		Person person = new Person();
		person.setFirstname(firstnames[randomNumberBetween(0, firstnames.length)]);
		person.setLastname(lastnames[randomNumberBetween(0, lastnames.length)]);
		person.setBirthdate(randomDate());
		person.setSsn(randomSsn());
		return person;
	}

	public static void main(String[] args) {
				
		try {
			List<Long> personIdList = new ArrayList<Long>();

			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction transaction = session.beginTransaction();

			// populate the database
			try {
				for (int i=0; i < 5; i++) {
					Person person = randomPerson();
					person.addWorkHistory(randomWorkHistory());
					person.addWorkHistory(randomWorkHistory());
					person.addWorkHistory(randomWorkHistory());
					session.save(person);
					personIdList.add(person.getId());
				}
				
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				throw e;
			} finally {
				if (session.isOpen())
					session.close();
			}

			Long personId = personIdList.get(randomNumberBetween(0, personIdList.size() - 1));
			for (int j = 0; j < 5; j++) {
				// start a new session.
				Thread.sleep(3000); // sleep to revision timestamps are far apart
				session = HibernateUtil.getSessionFactory()
						.openSession();
				transaction = session.beginTransaction();
				try {
					Person person = (Person) session.get(Person.class, personId);
					person.setSsn(randomSsn());
					session.save(person);
					transaction.commit();
				} catch (Exception e) {
					transaction.rollback();
					throw e;
				} finally {
					if (session.isOpen())
						session.close();
				}
			}
			
			// get the revisions.
			session = HibernateUtil.getSessionFactory()
					.openSession();
			transaction = session.beginTransaction();
			try {
				AuditReader reader = AuditReaderFactory.get(session);
				
				AuditQuery query = reader
						.createQuery()
						.forRevisionsOfEntity(Person.class, true, true)
						.add(AuditEntity.id().eq(personId))
						.addOrder(AuditEntity.property("ssn").desc());
				List<Person> persons = query.getResultList();
				
				for(Person person:persons) {
					System.out.println(person);
				}

				
List<Number> revisions = reader.getRevisions(Person.class, personId);

for (Number revNum: revisions) {
	Person person = reader.find(Person.class, personId, revNum);
	System.out.println("**** revision " 
			+ revNum.intValue() + " at time: " 
			+ reader.getRevisionDate(revNum));
	System.out.println("------- " + person);
}
				
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				throw e;
			} finally {
				if (session.isOpen())
					session.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		HibernateUtil.stopService();
	}

}
