package willy.server.sql.extra;

import java.sql.ResultSet;
import java.sql.SQLException;

import willy.server.sql.extra.generics.DBVariables;

public class MySQLVariables extends DBVariables
{
	//si timestamp (DBVariables) es 0, ignorar valors de variables
	//TODO: fer un comparador per evaluar si s'ha variat de la vegada anterior
	
	//http://code.openark.org/blog/mysql/mysql-parameters-configuration-sample-file
	
	protected boolean log_bin=false;
	protected boolean read_only=false;
	protected boolean slow_query_log=false;
	protected boolean innodb_file_per_table=false;
	protected boolean relay_log_purge=false;
	protected boolean have_query_cache=false;
	protected boolean old_passwords=false;
	
	protected long max_connections=0;
	protected long max_user_connections=0;
	
	//engines
	protected boolean have_archive=false;
	protected boolean have_bdb=false;
	protected boolean have_federated=false;
	protected boolean have_innodb=false;
	protected boolean have_isam=false;
	protected boolean have_ndbcluster=false;
	protected boolean have_csv=false;
	
	protected long expire_logs_days=0;
	protected double long_query_time=0.0;
	protected long innodb_flush_log_at_trx_commit=0;
	protected long query_cache_size=0;
	
	protected String version=null;
	protected String version_comment=null;
	protected String version_compile_machine=null;
	protected String version_compile_os=null;
	
	public String getVersion() { return this.version; }
	public String getVersionComment() { return this.version_comment; }
	public String getVersionCompileMachine() { return this.version_compile_machine; }
	public String getVersionCompileOS() { return this.version_compile_os; }
	
	public MySQLVariables(ResultSet rs) throws SQLException
	{
		timestamp = System.currentTimeMillis()/1000;
		
		while(rs.next())
		{
			String key=rs.getString(1);
			String value=rs.getString(2);
			
			this.getReplicationVariables(key, value);
			this.getSlowQueriesVariables(key, value);
			this.getInnoDBVariables(key, value);
			this.getQueryCacheVariables(key, value);
			this.getEnginesVariables(key, value);
			this.getMaxConnections(key, value);
			this.getSecurityDetails(key, value);
			this.getVersionDetails(key, value);
				
		}
	}
	
	private void getVersionDetails(String key, String value)
	{
		if(key.compareTo("version")==0)
			this.version=value;
		
		if(key.compareTo("version_comment")==0)
			this.version_comment=value;
		
		if(key.compareTo("version_compile_machine")==0)
			this.version_compile_machine=value;
		
		if(key.compareTo("version_compile_os")==0)
			this.version_compile_os=value;
	}
	
	
	
	
	/*
	 * old_passwords
	 
	mysql> SHOW VARIABLES LIKE 'old_passwords';
	+---------------+-------+
	| Variable_name | Value |
	+---------------+-------+
	| old_passwords | OFF   | 
	+---------------+-------+
	1 row in set (0.00 sec)
	
	What's the deal with old_passwords?

	compatibilitat amb llibreries de mysql 4.0 (fa 10 anys)
	
*/
	
	private void getSecurityDetails(String key, String value)
	{
		if(key.compareTo("old_passwords")==0)
			this.old_passwords=value.compareTo("YES")==0;
	}
	
//	TODO: log-output is given with a value, the value should be a comma-separated 
//	list of one or more of the words TABLE (log to tables), FILE (log to files), 
//	or NONE (do not log to tables or files
//	
//	As of MySQL 5.1.6, MySQL Server provides flexible control over the 
//	destination of output to the general query log and the slow query log, 
//	if those logs are enabled. Possible destinations for log entries are log 
//	files or the the general_log and slow_log tables in the mysql database. 
//	Either or both destinations can be selected. (Before MySQL 5.1.6, 
//	the server uses only log files as the destination for general query 
//	log and slow query log entries.) 
//	
//	mysql> select count(*) from  mysql.slow_log;
//	+----------+
//	| count(*) |
//	+----------+
//	|        0 |
//	+----------+
//	1 row in set (0.01 sec)
//
//	mysql> select count(*) from  mysql.general_log;
//	+----------+
//	| count(*) |
//	+----------+
//	|        0 |
//	+----------+
//	1 row in set (0.00 sec)
//
//	mysql> 
	
	/* 
	 * TODO: wait timepouts
mysql> show session variables like '%wait%';
+--------------------------+-------+
| Variable_name            | Value |
+--------------------------+-------+
| innodb_lock_wait_timeout | 50    |
| table_lock_wait_timeout  | 50    |
| wait_timeout             | 60    |
+--------------------------+-------+
3 rows in set (0.00 sec)
	 */
	
	private void getMaxConnections(String key, String value)
	{	
		//	| max_connections      | 151   |
		//	| max_user_connections | 0     |
		
		if(key.compareTo("max_connections")==0)
			this.max_connections=Long.parseLong(value);
		
		if(key.compareTo("max_user_connections")==0)
			this.max_user_connections=Long.parseLong(value);
		
	}
	
	private void getEnginesVariables(String key, String value)
	{		
		if(key.compareTo("have_archive")==0)
			this.have_archive=value.compareTo("YES")==0;
		
		if(key.compareTo("have_bdb")==0)
			this.have_bdb=value.compareTo("YES")==0;
		
		if(key.compareTo("have_federated")==0)
			this.have_federated=value.compareTo("YES")==0;
		
		if(key.compareTo("have_innodb")==0)
			this.have_innodb=value.compareTo("YES")==0;
		
		//TODO: que pasa amb això?
		if(key.compareTo("have_isam")==0)
			this.have_isam=value.compareTo("YES")==0;
		
		if(key.compareTo("have_ndbcluster")==0)
			this.have_ndbcluster=value.compareTo("YES")==0;
		
		if(key.compareTo("have_csv")==0)
			this.have_csv=value.compareTo("YES")==0;
		
		
	}
	
	private void getQueryCacheVariables(String key, String value)
	{
		if(key.compareTo("have_query_cache")==0)
			this.have_query_cache=value.compareTo("YES")==0;
		
		if(key.compareTo("query_cache_size")==0)
			this.query_cache_size=Long.parseLong(value);
	}
	
	private void getReplicationVariables(String key, String value)
	{
		if(key.compareTo("log_bin")==0)
			this.log_bin=value.compareTo("ON")==0;
		
		if(key.compareTo("read_only")==0)
			this.read_only=value.compareTo("ON")==0;
		
		if(key.compareTo("relay_log_purge")==0)
			this.relay_log_purge=value.compareTo("ON")==0;
		
		if(key.compareTo("expire_logs_days")==0)
			this.expire_logs_days=Long.parseLong(value);
	}
	
	private void getSlowQueriesVariables(String key, String value)
	{
		if(key.compareTo("slow_query_log")==0 ||
				key.compareTo("log_slow_queries")==0) //aquesta es deprecada
			this.slow_query_log=value.compareTo("ON")==0;
		
		if(key.compareTo("long_query_time")==0)
			this.long_query_time=Double.parseDouble(value);
	}
	
	private void getInnoDBVariables(String key, String value)
	{
		if(key.compareTo("innodb_file_per_table")==0)
			this.innodb_file_per_table=value.compareTo("ON")==0;
		
		if(key.compareTo("innodb_flush_log_at_trx_commit")==0)
			this.innodb_flush_log_at_trx_commit=Long.parseLong(value);
	}
	
	/*
	 * ========== GETTERS i SETTERS a partir d'aquí ==========
	 */
	
	/**
	 * habilitat slow query log?
	 */
	public boolean slow_query_log() { return this.slow_query_log; }

	/**
	 * MySQL en mode read_only? 
	 */
	public boolean read_only() { return this.read_only;	}
	
	/**
	 * binary logs habilitats?
	 */
	public boolean logbin() { return this.log_bin; }
	
	/**
	 * innodb_file_per_table?
	 */
	public boolean innodb_file_per_table() { return this.innodb_file_per_table;	}
	
	/**
	 * neteja el relay log quan ja no els necesita?
	 */
	public boolean relay_log_purge() { return this.relay_log_purge;	}
	
	/**
	 * te query cache?
	 */
	public boolean have_query_cache() {	return this.have_query_cache; }
	
	public long max_connections() {	return this.max_connections; }

	public long max_user_connections() { return this.max_user_connections; }
	
	/*
	 * ENGINES
	 */
	
	public boolean have_archive() { return this.have_archive; }
	public boolean have_bdb() { return this.have_bdb; }
	public boolean have_federated() { return this.have_federated; }
	public boolean have_innodb() { return this.have_innodb; }
	public boolean have_isam() { return this.have_isam; }
	public boolean have_ndbcluster() { return this.have_ndbcluster; }
	public boolean have_csv() { return this.have_csv; }
}
