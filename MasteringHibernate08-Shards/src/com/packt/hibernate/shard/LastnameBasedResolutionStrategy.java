package com.packt.hibernate.shard;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.shards.ShardId;
import org.hibernate.shards.strategy.resolution.ShardResolutionStrategy;
import org.hibernate.shards.strategy.selection.ShardResolutionStrategyData;

import com.packt.hibernate.model.Person;

public class LastnameBasedResolutionStrategy implements ShardResolutionStrategy {
	public List<ShardId> selectShardIdsFromShardResolutionStrategyData(
			ShardResolutionStrategyData shardResolutionStrategyData) {
		if (shardResolutionStrategyData.getEntityName().equals(Person.class.getName())) {
			String identifier = (String) shardResolutionStrategyData.getId();
			String[] s = ((String) identifier).split(":");
			Integer si = new Integer(s[0]);
			List<ShardId> shardIdList = new ArrayList<ShardId>();
			shardIdList.add(new ShardId(si));
			return shardIdList;
		}
		
		throw new IllegalArgumentException("Only Person entities are supported");
	}
}
