package willy.server.sql.extra;

import java.sql.ResultSet;
import java.sql.SQLException;

import willy.server.sql.extra.generics.DBVariables;

public class MySQLVariables extends DBVariables
{
	//si timestamp (DBVariables) es 0, ignorar valors de variables
	
	protected boolean log_bin=false;
	protected boolean read_only=false;
	protected boolean slow_query_log=false;
	protected boolean innodb_file_per_table=false;
	protected boolean relay_log_purge=false;
	protected boolean have_query_cache=false;
	
	protected long max_connections=0;
	protected long max_user_connections=0;
	
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
				
		}
	}
	
	/*
	 * 				TODO: old_passwords
	 
	mysql> SHOW VARIABLES LIKE 'old_passwords';
	+---------------+-------+
	| Variable_name | Value |
	+---------------+-------+
	| old_passwords | OFF   | 
	+---------------+-------+
	1 row in set (0.00 sec)
*/
	
	
	private void getMaxConnections(String key, String value)
	{	
		//	| max_connections      | 151   |
		//	| max_user_connections | 0     |
		
		if(key.compareTo("max_connections")==0)
		{
			this.max_connections=Long.parseLong(value);
		}
		if(key.compareTo("max_user_connections")==0)
		{
			this.max_user_connections=Long.parseLong(value);
		}
	}
	
	private void getEnginesVariables(String key, String value)
	{
		//TODO: engines
//		 *  $engines .= (defined $myvar{'have_archive'} && $myvar{'have_archive'} eq "YES")? greenwrap "+Archive " : redwrap "-Archive " ;
//		 *  $engines .= (defined $myvar{'have_bdb'} && $myvar{'have_bdb'} eq "YES")? greenwrap "+BDB " : redwrap "-BDB " ;
//		 *  $engines .= (defined $myvar{'have_federated'} && $myvar{'have_federated'} eq "YES")? greenwrap "+Federated " : redwrap "-Federated " ;
//		 *  $engines .= (defined $myvar{'have_innodb'} && $myvar{'have_innodb'} eq "YES")? greenwrap "+InnoDB " : redwrap "-InnoDB " ;
//		 *  $engines .= (defined $myvar{'have_isam'} && $myvar{'have_isam'} eq "YES")? greenwrap "+ISAM " : redwrap "-ISAM " ;
//		 *  $engines .= (defined $myvar{'have_ndbcluster'} && $myvar{'have_ndbcluster'} eq "YES")? greenwrap "+NDBCluster " : redwrap "-NDBCluster " ;  
	}
	
	private void getQueryCacheVariables(String key, String value)
	{
		if(key.compareTo("have_query_cache")==0)
		{
			this.have_query_cache=value.compareTo("YES")==0;
		}
		//TODO: valor query_cache_size
	}
	
	private void getReplicationVariables(String key, String value)
	{
		if(key.compareTo("log_bin")==0)
		{
			this.log_bin=value.compareTo("ON")==0;
		}
		if(key.compareTo("read_only")==0)
		{
			this.read_only=value.compareTo("ON")==0;
		}
		if(key.compareTo("relay_log_purge")==0)
		{
			this.relay_log_purge=value.compareTo("ON")==0;
		}
		//TODO: valors:
		//expire_logs_days
	}
	
	private void getSlowQueriesVariables(String key, String value)
	{
		//TODO: valor: long_query_time
		if(key.compareTo("slow_query_log")==0 ||
				key.compareTo("log_slow_queries")==0) //aquesta es deprecada
		{
			this.slow_query_log=value.compareTo("ON")==0;
		}
	}
	
	private void getInnoDBVariables(String key, String value)
	{
		if(key.compareTo("innodb_file_per_table")==0)
		{
			this.innodb_file_per_table=value.compareTo("ON")==0;
		}
		//TODO: valors:
		//innodb_flush_log_at_trx_commit
	}
	
	/*
	 * ========== GETTERS i SETTERS a partir d'aquí ==========
	 */
	
	/**
	 * habilitat slow query log?
	 */
	public boolean slow_query_log()
	{
		return this.slow_query_log;
	}

	/**
	 * MySQL en mode read_only? 
	 */
	public boolean read_only()
	{
		return this.read_only;
	}
	
	/**
	 * binary logs habilitats?
	 */
	public boolean logbin()
	{
		return this.log_bin;
	}
	
	/**
	 * innodb_file_per_table?
	 */
	public boolean innodb_file_per_table()
	{
		return this.innodb_file_per_table;
	}
	
	/**
	 * neteja el relay log quan ja no els necesita?
	 */
	public boolean relay_log_purge()
	{
		return this.relay_log_purge;
	}
	
	/**
	 * te query cache?
	 */
	public boolean have_query_cache()
	{
		return this.have_query_cache;
	}
	
	public long max_connections() {
		return this.max_connections;
	}

	public long max_user_connections() {
		return this.max_user_connections;
	}
}
