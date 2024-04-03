package com.packt.hibernate.ch7.listing01;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.CollectionStatistics;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.Statistics;

import com.packt.hibernate.model.Address;
import com.packt.hibernate.model.Child;
import com.packt.hibernate.model.Child.Gender;
import com.packt.hibernate.model.Person;

public class MainStatistics {
	
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
				Query query = session.createQuery("from Person where firstname like 'J%'")
						.setCacheable(true);
				
				List<Person> persons = query.list();
				for (Person person: persons) {
					person.getId();
					person.getFirstname();
				}
				
				query = session.createQuery("from Person where firstname like 'J%'")
						.setCacheable(true);
				
				persons = query.list();
				for (Person person: persons) {
					person.getId();
					person.getFirstname();
				}
				
				System.out.println("###### All persons: " + persons.size());
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				throw e;
			} finally {
				if (session.isOpen())
					session.close();
			}
			
			
			System.out.println("*** fetching all persons whose first name begins with 'J'");

			
			// keep a list of all person IDs
			List<Long> personIdList = new ArrayList<Long>();
			
			// start a new session.
			
			session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			transaction = session.beginTransaction();
			try {
				Query query = session.createQuery("from Person where firstname like 'J%'")
						.setCacheable(true);
				
				List<Person> persons = query.list();
				System.out.println("###### query returned num person: " + persons.size());
				for (Person person: persons) {
					personIdList.add(person.getId());
					person.getFirstname();
				}
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				throw e;
			} finally {
				if (session.isOpen())
					session.close();
			}
			
			// start one session and fetch a person
			
			if (personIdList.size() > 1) {
				
				Long randomPersonId = personIdList.get(randomNumberBetween(0, personIdList.size() - 1));
				
				session = HibernateUtil.getSessionFactory()
						.getCurrentSession();
				transaction = session.beginTransaction();
				try {				
					System.out.println("**** > loaded from cache?");
					Person person = (Person) session.get(Person.class, randomPersonId);
					System.out.println(person);
					
					System.out.println("**** > should load from cache");
					person = (Person) session.load(Person.class, randomPersonId);
					System.out.println("**** > " + person);
					for (Child child: person.getChildren()) {
						System.out.println("child: " + child);
					}
					
					for(Child child:person.getChildren()) {
						if (child.getGender().equals(Gender.Female)) {
							child.setFirstname(femaleName[randomNumberBetween(0, femaleName.length)]);			
						}
						else {
							child.setFirstname(maleName[randomNumberBetween(0, maleName.length)]);			
						}
						session.save(child);
					}
					
					
					
					System.out.println("***** address: " + person.getAddress());
					HibernateUtil.getSessionFactory().getCache().evictEntity(Person.class, randomPersonId);
					person = (Person) session.load(Person.class, randomPersonId);
					System.out.println("**** > " + person);
					for (Child child: person.getChildren()) {
						System.out.println("child: " + child);
					}
					
					// load a different Person, but using session.load
					Long randomPersonId2 = new Long(randomPersonId.longValue());
					while (randomPersonId2.longValue() == randomPersonId) {
						randomPersonId2 = personIdList.get(randomNumberBetween(0, personIdList.size() - 1));
					}
					HibernateUtil.getSessionFactory().getCache().evictEntity(Person.class, randomPersonId2);

					Person person2 = (Person) session.load(Person.class, randomPersonId2);
					System.out.println("-=-=-=-=- loaded person with id " + randomPersonId2);
					System.out.println(person2);
					
					transaction.commit();
				} catch (Exception e) {
					transaction.rollback();
					throw e;
				} finally {
					if (session.isOpen())
						session.close();
				}
			}

			Statistics stats = HibernateUtil.getSessionFactory().getStatistics();
			Date startTime = new Date(stats.getStartTime());
			System.out.println("session started on: " + startTime);
			System.out.println(
					"connect count: " + stats.getConnectCount() +
					"\nsession open count: " + stats.getSessionOpenCount() +
					"\nsession close count: " + stats.getSessionCloseCount() +
					"\nentity load: " + stats.getEntityLoadCount() +
					"\nentity fetch: " + stats.getEntityFetchCount() +
					"\nentity insert: " + stats.getEntityInsertCount() +
					"\nentity delete: " + stats.getEntityDeleteCount()
					);
			
			String[] entityNames = stats.getEntityNames();
			for(String entityName: entityNames) {
				System.out.println("entity name: " + entityName);
				EntityStatistics es = stats.getEntityStatistics(entityName);
				System.out.println("Delete: " + es.getDeleteCount());
				System.out.println("Insert: " + es.getInsertCount());
				System.out.println("Update: " + es.getUpdateCount());
				System.out.println("Load: " + es.getLoadCount());
				System.out.println("Fetch: " + es.getFetchCount());
			}
			
			String[] roleNames = stats.getEntityNames();
			for(String roleName: roleNames) {
				System.out.println("role name: " + roleName);
				CollectionStatistics cs = stats.getCollectionStatistics(roleName);
				System.out.println("Delete: " + cs.getRemoveCount());
				System.out.println("Recreate: " + cs.getRecreateCount());
				System.out.println("Update: " + cs.getUpdateCount());
				System.out.println("Load: " + cs.getLoadCount());
				System.out.println("Fetch: " + cs.getFetchCount());
			}
			
			String[] queryNames = stats.getQueries();
			for(String queryName: queryNames) {
				System.out.println("query name: " + queryName);
				QueryStatistics qs = stats.getQueryStatistics(queryName);
				System.out.println("Execution Count: " + qs.getExecutionCount());
				System.out.println("Row Count: " + qs.getExecutionRowCount());
				System.out.println("Avg Time: " + qs.getExecutionAvgTime());
				System.out.println("Cache Puts: " + qs.getCachePutCount());
				System.out.println("Cache Hits: " + qs.getCacheHitCount());
				System.out.println("Cache Miss: " + qs.getCacheMissCount());
			}
			
			
			// overall counts
			System.out.println("stats - second level cache puts: " + 
					stats.getSecondLevelCachePutCount());
			System.out.println("stats - second level cache hits: " + 
					stats.getSecondLevelCacheHitCount());
			System.out.println("stats - second level cache miss: " + 
					stats.getSecondLevelCacheMissCount());
			
			for(String regionName: stats.getSecondLevelCacheRegionNames()) {
				// count per region
				System.out.println("cache region: " + regionName);				
				System.out.println("stats - " + regionName + ":\n" + 
						HibernateUtil.getSessionFactory()
							.getStatistics()
							.getSecondLevelCacheStatistics(regionName));
			
				// entries per region
				Map entries = HibernateUtil.getSessionFactory()
						.getStatistics()
						.getSecondLevelCacheStatistics("personCache")
						.getEntries();				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		HibernateUtil.stopService();
	}

}
