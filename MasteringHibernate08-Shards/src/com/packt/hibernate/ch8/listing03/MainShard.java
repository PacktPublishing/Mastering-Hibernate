package com.packt.hibernate.ch8.listing03;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.packt.hibernate.model.Address;
import com.packt.hibernate.model.Child;
import com.packt.hibernate.model.Child.Gender;
import com.packt.hibernate.model.Person;

public class MainShard {
	private static Logger logger = Logger.getLogger(MainShard.class);
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss,SSS");
	
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
		"Adams",
		"Charles",
		"Ashcroft",
		"Shield",
		"Nicholson",
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
	
	public static Person randomChange(Person person) {
		Person p = randomPerson();
		while (p.getFirstname().equals(person.getFirstname())) {
			p = randomPerson();
		}
		person.setFirstname(p.getFirstname());
		return person;
	}
	
	public static void logMessage(StringBuffer sb, String msg) {
		sb.append("[");
		sb.append(sdf.format(new Date()));
		sb.append("] ");
		sb.append(msg).append("\n");
	}
	
	public static void main(String[] args) {
//		try {
//			Session session = HibernateUtil.getSessionFactory()
//					.openSession();
//			Transaction transaction = session.beginTransaction();
//			try {
//				for (int i=0; i < 30; i++) {
//					Person person = randomPerson();
//					session.save(person);
//				}
//				transaction.commit();
//			} catch (Exception e) {
//				transaction.rollback();
//				throw e;
//			} finally {
//				if (session.isOpen())
//					session.close();
//			}			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

//		try {
//			Session session = HibernateUtil.getSessionFactory()
//					.openSession();
//			Transaction transaction = session.beginTransaction();
//			try {
//				List<Person> persons = (List<Person>) session.createQuery("from Person p where p.lastname like 'N%' or p.lastname like 'A%'").list();
//				for(Person p:persons) {
//					System.out.println(p);
//				}
//				transaction.commit();
//			} catch (Exception e) {
//				transaction.rollback();
//				throw e;
//			} finally {
//				if (session.isOpen())
//					session.close();
//			}			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		id=0:042d64b2-a416-4ce7-865b-6237abb9f669
//		id=1:2747f168-ce2f-48bc-9f5a-bd9649e0d47e
		
		try {
			Session session = HibernateUtil.getSessionFactory()
					.openSession();
			Transaction transaction = session.beginTransaction();
			try {				
				String[] idList = {
						"0:042d64b2-a416-4ce7-865b-6237abb9f669",
						"1:2747f168-ce2f-48bc-9f5a-bd9649e0d47e"
				};
				
				for (String id:idList) {
					Person p = (Person) session.get(Person.class, id);
					System.out.println(p);
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
