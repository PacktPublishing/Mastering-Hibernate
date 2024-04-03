package com.packt.hibernate.ch8.listing02;

import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

import com.packt.hibernate.model.Address;
import com.packt.hibernate.model.Child;
import com.packt.hibernate.model.Child.Gender;
import com.packt.hibernate.model.Person;

public class MainIsolation {
	private Logger logger = Logger.getLogger(MainIsolation.class);

	public static final SimpleDateFormat sdf = new SimpleDateFormat(
			"hh:mm:ss,SSS");

	public static int sync = 0;

	public static final String[] maleName = { "John", "Steve", "Kevin",
			"Robert", "Dillon" };

	public static final String[] femaleName = { "Jill", "Sara", "Elizabeth",
			"Mary", "Maria", "Sherry" };

	public static final String[] lastnames = { "Smith", "Robertson", "Johnson",
			"Dillon", "Stein", "Morgan", "Duncan", "Madison", "Ashcroft",
			"Shield", "Weinstein" };

	public static final String[] street = { "Main Street", "Eastern Avenue",
			"Maple Avenue", "Park Avenue", "Wisconsin Avenue", "Elm Street",
			"Willow Lane" };

	public static final String[] city = { "Washington", "Fairfax", "Arlington",
			"Alexandria", "Falls Church" };

	public static int randomNumberBetween(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}

	public static Date randomBirthdate() {
		GregorianCalendar calendar = new GregorianCalendar();
		int year = randomNumberBetween(1960, 1999);
		int day = randomNumberBetween(1,
				calendar.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));
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
			person.setFirstname(femaleName[randomNumberBetween(0,
					femaleName.length)]);
		} else {
			person.setFirstname(maleName[randomNumberBetween(0, maleName.length)]);
		}

		person.setLastname(lastnames[randomNumberBetween(0, lastnames.length)]);
		person.setBirthdate(randomBirthdate());
		person.setSsn(randomSsn());

		long childCount = randomNumberBetween(0, 6);
		for (int c = 0; c < childCount; c++) {
			person.addChild(randomChild());
		}

		person.setAddress(randomAddress());

		return person;
	}

	public static Child randomChild() {
		Child child = new Child();
		child.setGender(randomGender());
		if (child.getGender().equals(Gender.Female)) {
			child.setFirstname(femaleName[randomNumberBetween(0,
					femaleName.length)]);
		} else {
			child.setFirstname(maleName[randomNumberBetween(0, maleName.length)]);
		}
		return child;
	}

	public static Address randomAddress() {
		Address address = new Address();
		Random rand2 = new Random();
		int streetLine = rand2.nextInt(street.length);
		int cityLine = rand2.nextInt(city.length);

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

	public void execute(final String threadId, boolean first) {
		StringBuffer sb = new StringBuffer("");
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.doWork(new Work() {

				@Override
				public void execute(Connection connection) throws SQLException {
					logger.info(threadId
							+ ": ***** Connection isolation level: "
							+ connection.getTransactionIsolation());
					connection.setTransactionIsolation(8);
				}
			});
			Transaction transaction = session.beginTransaction();
			try {
				logMessage(sb, "****** " + threadId + " fetching data.");
				Person person = (Person) session
						.get(Person.class, new Long(18));
				logger.info(threadId + " - acquiring lock");

				MainIsolation.sync = 1;

				logMessage(sb, "****** " + threadId + " --  person: " + person);

				person = randomChange(person);
				person.setFirstname(person.getFirstname() + "-" + threadId);
				session.save(person);
				logMessage(sb, "****** " + threadId + " --  saved new person: "
						+ person);
				session.flush();
				logMessage(sb, "****** " + threadId + " --  flushed");

				if (first)
					Thread.sleep(1000);
				transaction.commit();
				logMessage(sb, "****** " + threadId + " --  committed");
			} catch (Exception e) {
				transaction.rollback();
				throw e;
			} finally {
				if (session.isOpen())
					session.close();
			}
			logMessage(sb, "****** " + threadId + " --  done");

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.error(threadId + "\n" + sb.toString());
	}

	public static void main(String[] args) {

		Thread t1 = new Thread() {

			@Override
			public void run() {
				MainIsolation main = new MainIsolation();
				main.execute("t1", true);
			}
		};

		Thread t2 = new Thread() {

			@Override
			public void run() {
				MainIsolation main = new MainIsolation();
				main.execute("t2", false);
			}
		};

		t1.start();
		while (MainIsolation.sync == 0) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		t2.start();

		while (true) {
			if (t1.getState().equals(State.TERMINATED) && t2.getState().equals(State.TERMINATED)) {
				break;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		HibernateUtil.stopService();

	}

}
