package com.packt.hibernate.shard;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.shards.ShardId;
import org.hibernate.shards.id.ShardEncodingIdentifierGenerator;

import com.packt.hibernate.model.Person;

public class LastnameBasedIdGenerator implements 
	IdentifierGenerator,
	ShardEncodingIdentifierGenerator {

	public ShardId extractShardId(Serializable identifier) {
		String[] s = ((String) identifier).split(":");
		Integer si = new Integer(s[0]);
		return new ShardId(si);
	}

	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		// Shard ID is based on first letter of last name.
		String lastname = ((Person) object).getLastname();
		char firstLetter = lastname.toUpperCase().charAt(0);
		
		Integer shardId = 1;
		// if first letter begins with letter M or prior
		if (firstLetter <= 0x4D) {
			shardId = 0;
		}
		
		String objectID = shardId 
				+ ":" 
				+ java.util.UUID.randomUUID().toString();
		
		return objectID;
	}
}
