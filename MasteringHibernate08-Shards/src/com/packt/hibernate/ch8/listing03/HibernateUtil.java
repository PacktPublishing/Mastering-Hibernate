package com.packt.hibernate.ch8.listing03;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.shards.ShardId;
import org.hibernate.shards.ShardedConfiguration;
import org.hibernate.shards.cfg.ConfigurationToShardConfigurationAdapter;
import org.hibernate.shards.cfg.ShardConfiguration;
import org.hibernate.shards.strategy.ShardStrategy;
import org.hibernate.shards.strategy.ShardStrategyFactory;
import org.hibernate.shards.strategy.ShardStrategyImpl;
import org.hibernate.shards.strategy.access.SequentialShardAccessStrategy;
import org.hibernate.shards.strategy.access.ShardAccessStrategy;
import org.hibernate.shards.strategy.resolution.ShardResolutionStrategy;
import org.hibernate.shards.strategy.selection.ShardSelectionStrategy;

import com.packt.hibernate.model.Address;
import com.packt.hibernate.model.Child;
import com.packt.hibernate.model.Person;
import com.packt.hibernate.shard.LastnameBasedResolutionStrategy;
import com.packt.hibernate.shard.LastnameBasedShardSelection;

public class HibernateUtil {

	private static SessionFactory sessionFactory = buildSessionFactory();
//	private static ServiceRegistry serviceRegistry;

	private static synchronized SessionFactory buildSessionFactory() {
		try {
	Configuration configuration = new Configuration()
			.configure("hibernate.shard0.cfg.xml")
			.addAnnotatedClass(Person.class)
			.addAnnotatedClass(Child.class)
			.addAnnotatedClass(Address.class);

	List<ShardConfiguration> shardConfigs = new ArrayList<ShardConfiguration>();

	ShardConfiguration configShard0 = buildShardConfig("hibernate.shard0.cfg.xml");
	ShardConfiguration configShard1 = buildShardConfig("hibernate.shard1.cfg.xml");

	shardConfigs.add(configShard0);
	shardConfigs.add(configShard1);
	ShardStrategyFactory shardStrategyFactory = buildShardStrategyFactory();
	ShardedConfiguration shardedConfig = new ShardedConfiguration(
			configuration, shardConfigs, shardStrategyFactory);

	return shardedConfig.buildShardedSessionFactory();

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static ShardConfiguration buildShardConfig(String configFile) {
		Configuration config = new Configuration().configure(configFile);
		return new ConfigurationToShardConfigurationAdapter(config);
	}

	private static ShardStrategyFactory buildShardStrategyFactory() {
		ShardStrategyFactory shardStrategyFactory = new ShardStrategyFactory() {
			public ShardStrategy newShardStrategy(List<ShardId> shardIds) {
				ShardSelectionStrategy selection = new LastnameBasedShardSelection();
				ShardResolutionStrategy resolution = new LastnameBasedResolutionStrategy();
				ShardAccessStrategy access = new SequentialShardAccessStrategy();				
				return new ShardStrategyImpl(selection, resolution, access);
			}
		};
		return shardStrategyFactory;
	}

	public static synchronized SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void stopService() {
//		StandardServiceRegistryBuilder.destroy(serviceRegistry);
	}
}