package willy.server.sql.processlist;

import java.util.LinkedList;

import willy.server.sql.extra.*;
import willy.server.sql.extra.generics.DBState;
import willy.server.sql.mysql.MySQLTables;

public abstract class DBWatch {
	
	protected static final String MASTER_SLAVE_SEEN_MYSQL_PL_STRING="Binlog Dump"; 
	protected static final String SLAVE_WAITING_MASTER_MYSQL_PL_STRING="Waiting for master to send event";
	protected static final String QUERY_LOCKED_WAITING_LOCK_STRING="Waiting for table level lock";
	protected static final String QUERY_LOCKED_STATE_STRING="Locked";
	protected static final String MYSQL_PL_QUERY_STRING="Query";
	protected static final String MYSQL_PL_SLEEP_STRING="Sleep";
	
	//public abstract void add(DBProcessList dbpl, DBStatus dbs, DBVariables dbv, DBGrants grants);
	
	public abstract int getStateCount();
	
	public abstract int getLastProcessListCount();
	public abstract int getLastProcessListStateCount(String state);
	
	public abstract double[] getGlobalProcessListStateCount(String state);
	
	public abstract int getActiveSlavesCount();
	public abstract int getInactiveSlavesCount();
	
	public abstract ListSlowQueries getSlowQueriesList();
	public abstract LinkedList<DBQuery> getUpdatedQueries();
	
	public abstract DBState getLastState();
	public abstract DBState getFirstState();
	
	//TODO: canviar a classe genèrica
	public abstract void setDBItems(MySQLTables dbitems);
	
	//idees:
	//getTopQueries?
	
	public abstract int Do();

}
