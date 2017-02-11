DROP KEYSPACE IF EXISTS Tweet;

CREATE KEYSPACE IF NOT EXISTS Tweet WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1 };

USE Tweet;

DROP TABLE IF EXISTS tweets;

CREATE TABLE IF NOT EXISTS tweets(
	
	name text,
	message text,
	date text,
	time text,
	
	PRIMARY KEY((name),date,time);
