package com.packt.hibernate.shard;

import org.hibernate.shards.ShardId;
import org.hibernate.shards.strategy.selection.ShardSelectionStrategy;

import com.packt.hibernate.model.Person;

public class LastnameBasedShardSelection implements ShardSelectionStrategy {
	public ShardId selectShardIdForNewObject(Object obj) {
		if (obj instanceof Person) {
			Person person = (Person) obj;
			String id = person.getId();
			if (id != null) {
				String[] s = person.getLastname().split(":");
				Integer shardId = new Integer(s[0]);
				return new ShardId(shardId);
			}
			// Shard ID is based on first letter of last name.
			String lastname = ((Person) obj).getLastname();
			char firstLetter = lastname.toUpperCase().charAt(0);
			
			Integer shardId = 1;
			// if first letter begins with letter M or prior
			if (firstLetter <= 0x4D) {
				shardId = 0;
			}
			return new ShardId(shardId);			
		}
		
		throw new IllegalArgumentException("Only Person entities are supported");
	}
}
