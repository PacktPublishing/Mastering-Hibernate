package com.packt.hibernate.ch8.listing01;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

import com.packt.hibernate.model.Address;
import com.packt.hibernate.model.Child;
import com.packt.hibernate.model.Child.Gender;
import com.packt.hibernate.model.Person;

public class MainTransaction {
	
	public static final String[] maleName = {
		"John",
		"Steve",
		"Kevin",
		"Robert",
		"Dillon"
	};

	public static final String[] femaleName = {
		"Jill",
		"Sara",
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
	
	public static int randomNumberBetween(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min; 
	}
	
	public static Date randomBirthdate() {
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
	
	public static Gender randomGender() {
		if (randomNumberBetween(0, 2) == 1) {
			return Gender.Male;
		} else {
			 return Gender.Female;
		}
	}
	
	public static Person randomPerson() {
		Person person = new Person();
		if (randomGender().equals(Gender.Female)) {
			person.setFirstname(femaleName[randomNumberBetween(0, femaleName.length)]);			
		}
		else {
			person.setFirstname(maleName[randomNumberBetween(0, maleName.length)]);			
		}
		
		person.setLastname(lastnames[randomNumberBetween(0, lastnames.length)]);
		person.setBirthdate(randomBirthdate());
		person.setSsn(randomSsn());
		
		long childCount = randomNumberBetween(0, 6);
		for (int c = 0; c <childCount; c++) {
			person.addChild(randomChild());
		}
		
		person.setAddress(randomAddress());
		
		return person;
	}

	public static Child randomChild() {
		Child child = new Child();
		child.setGender(randomGender());
		if (child.getGender().equals(Gender.Female)) {
			child.setFirstname(femaleName[randomNumberBetween(0, femaleName.length)]);			
		}
		else {
			child.setFirstname(maleName[randomNumberBetween(0, maleName.length)]);			
		}
		return child;
	}
	
	public static Address randomAddress() {
		Address address = new Address();
		Random rand2 = new Random();
		int streetLine = rand2.nextInt(street.length);
		int cityLine =  rand2.nextInt(city.length);
		
		int houseNumber = randomNumberBetween(200, 800);
		address.setStreet(houseNumber + " " + street[streetLine]);
		address.setCity(city[cityLine]);
		
		return address;
	}
	
	public static void main(String[] args) {

		try {
			
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			try {
				Person person1 = randomPerson();
				session.save(person1);
				for(Child child:person1.getChildren()) {
					session.save(child);
				}
				session.save(person1.getAddress());
				session.flush();				
				session.doWork(new Work() {
					@Override
					public void execute(Connection connection)
							throws SQLException {
						connection.commit();
					}					
				});
				
				Person person2 = randomPerson();
				session.save(person2);
				for(Child child:person2.getChildren()) {
					session.save(child);
				}
				session.save(person2.getAddress());
				
				transaction.rollback();
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
		finally {
			HibernateUtil.stopService();
		}
	}

}
