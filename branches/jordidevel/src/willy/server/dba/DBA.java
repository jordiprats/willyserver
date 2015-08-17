package willy.server.dba;

import java.util.LinkedList;

import willy.server.sql.*;
import willy.server.sql.mysql.MySQLTables;
import willy.server.sql.processlist.*;
import willy.server.sql.extra.DBQuery;
import willy.server.sql.extra.ListSlowQueries;
import willy.server.sql.extra.generics.*;
import willy.server.sql.extra.grants.*;
import willy.server.warnings.WarningList;





public abstract class DBA {
	public static final int TYPE_MYSQL=1;
	
	public static final String MYSQL_STATUS_SLEEP="Sleep";
	public static final String MYSQL_STATUS_QUERY="Query";
	
	protected DBAccess db=null;
	protected DBWatch dbwatch=null;
	protected DBVariables dbv=null;
	protected DBGrants grants=null;
	
	public DBAccess getDBAccess()
	{
		return this.db;
	}
	
	/**
	 * timestamp inici recolecció dades
	 */
	protected long timestamp_start=0;
	
	/**
	 * timestamp fi recolecció dades (per quan es serialitzi a disc)
	 */
	protected long timestamp_end=0;
	
	protected boolean enable_global_warnings=false;
	protected WarningList global_warnings=null;
	
	/**
	 * netejar globalwarnings i habilitar al proper Do()
	 */
	public void enableGlobalWarnings()
	{
		this.enable_global_warnings=true;
		this.global_warnings=new WarningList();
	}
	
	/**
	 * deshabilitar i netejar els Global Warnings
	 */
	public void clearGlobalWarnings()
	{
		this.enable_global_warnings=false;
		this.global_warnings=null;
	}
	
	public WarningList getGlobalWarningsList()
	{
		return this.global_warnings;
	}
	
	public abstract int getType();
	public abstract String getVersion();
	public abstract String getVersionExtraInfo();
	public abstract String getHostname();
	public abstract String getOS();
	public abstract String getArchitecture();
	
	public abstract int getStateCount();
	public abstract int getActiveSlavesCount();
	
	public abstract int getLastProcessListStateCount(String state);
	public abstract int getLastProcessListCount();
	
	public abstract ListSlowQueries getSlowQueriesList();
	public abstract LinkedList<DBQuery> getUpdatedQueries();
	
	public abstract double[] getGlobalProcessListStateCount(String state);
	
	public abstract DBState getLastState();
	public abstract DBState getFistState();
	
	public abstract int Do();
	protected abstract void DoGlobalWarnings(); //helper obligatori
	
	public abstract boolean hasTablesInfo();
	//TODO: fer clase genèrica
	public abstract MySQLTables getTablesInfo();

	
	public abstract void close();
	
	//idees:
	//isMaster() --> show master status pel mysql
	//isSlave() --> show slave status pel mysql
	
	//hauria de tindre algo per pasar a disc les dades i recuperarles
	
	//interficie NRPE
	//https://sourceforge.net/projects/jnrpe/
	
	//interficie SNMP
	//http://www.snmp4j.org/
	
	//# Checks for 32-bit boxes with more than 2GB of RAM
	
	//tamanys:
    //# MySQL < 5 servers take a lot of work to get table sizes
	//MySQL >= 5 "SELECT ENGINE,SUM(DATA_LENGTH),COUNT(ENGINE) FROM information_schema.TABLES WHERE TABLE_SCHEMA NOT IN ('information_schema','mysql') GROUP BY ENGINE ORDER BY ENGINE ASC;"
}
