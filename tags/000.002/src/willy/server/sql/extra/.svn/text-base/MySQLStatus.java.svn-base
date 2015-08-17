package willy.server.sql.extra;

import java.sql.ResultSet;
import java.sql.SQLException;

import willy.server.sql.extra.generics.DBStatus;
import willy.singletons.DBLink;

/**
 * @author jprats
 *
 */
public class MySQLStatus extends DBStatus {

	protected boolean slave_running=false;
	protected long uptime=0;
	protected long uptime_since_flush_status=0;
	
	public long getUptime() { return this.uptime; }
	public long getUptimeSinceFlushStatus() { return this.uptime_since_flush_status; }
	
	//query stats
	protected long select_range_check=0;
	protected long select_full_join=0;
	
	//procesos
	protected long threads_created=0;
	protected long threads_running=0;
	protected long threads_connected=0;
	protected long threads_cached=0;
	protected long connections=0;
	protected long queries=0;
	public long getQueriesCount() { return this.queries; }
	protected long max_used_connections=0;
	
	//resum setejat per DBA
	protected double queries_per_sec=0;
	protected boolean dx_valid=false;
	public double getQueriesPerSec() { return this.queries_per_sec; }
	public boolean hasDiferentialCounters() { return this.dx_valid; }
	
	//resums
	protected long joins_without_indexes=0;
	protected double joins_without_indexes_per_day=0;
	protected double joins_without_indexes_per_hour=0;
	protected double joins_without_indexes_per_min=0;
	
	public long getJoinsWithoutIndexesCount() { return this.joins_without_indexes; }
	//desde uptime
	public double getJoinsWithoutIndexesDay () { return this.joins_without_indexes_per_day; }
	public double getJoinsWithoutIndexesHour() { return this.joins_without_indexes_per_hour; }
	public double getJoinsWithoutIndexesMin() { return this.joins_without_indexes_per_min; }
	
	//trafic (contadors)
	protected long bytes_received=0;
	protected long bytes_sent=0;
	
	public long getBytesReceived() { return this.bytes_received; }
	public long getBytesSent() { return this.bytes_sent; }
	
	//trafic (diferències) 
	
	protected long dx_bytes_received=0;
	protected long dx_bytes_sent=0;
	
	public long getDiferentialBytesReceived() { return this.dx_bytes_received; }
	public long getDiferentialBytesSent() { return this.dx_bytes_sent; }
	
	/**
	 * estat del slave, pot ser null
	 */
	protected MySQLSlaveStatus slave_status=null;

	public void setDiferentialCounters(MySQLStatus laststatus)
	{
		if((this.timestamp-laststatus.getTimestamp())<=0) return;
		
		if(this.queries-laststatus.getQueriesCount()-DBLink.getInstance().getQueryCounter()<0)
		{
			DBLink.getInstance().resetQueryCounter();
			return;
		}
		//System.out.println("resta: "+this.timestamp+"-"+laststatus.getTimestamp()+"="+(this.timestamp-laststatus.getTimestamp()));
		
		this.dx_valid=true;
		this.queries_per_sec=(
								(double)(this.queries-laststatus.getQueriesCount()-DBLink.getInstance().getQueryCounter())
								/
								(double)(this.timestamp-laststatus.getTimestamp())
								);

		System.out.println(this.queries+"-"+laststatus.getQueriesCount()+"-"+DBLink.getInstance().getQueryCounter()+
				"/"+this.timestamp+"-"+laststatus.getTimestamp()+"="+this.queries_per_sec
				);
		
		this.dx_bytes_received=this.bytes_received-laststatus.getBytesReceived();
		this.dx_bytes_sent=this.bytes_sent-laststatus.getBytesSent();
		
		//reset per calcular fins la propera
		DBLink.getInstance().resetQueryCounter();
	}
	
	public MySQLStatus(ResultSet rs) throws SQLException
	{
		super.timestamp = System.currentTimeMillis()/1000;
		
		while(rs.next())
		{
			String key=rs.getString(1);
			String value=rs.getString(2);
			
			this.getSlaveStatus(key, value);
			this.getUptime(key, value);
			this.getThreadsCounters(key, value);
			this.getJoinCounters(key, value);
			this.getTrafficCounters(key, value);
			
			//System.out.println("k:"+key+"v:"+value);
		}
		
		//calcular sumes
		this.calcJoinCounters();
	}
	
	public long getThreads_created() {
		return threads_created;
	}

	public long getConnections() {
		return connections;
	}

	private void getSlaveStatus(String key, String value)
	{
		if(key.compareTo("Slave_running")==0)
		{
			this.slave_running=value.compareTo("ON")==0;
		}
	}
	
//	*| Threads_cached                           | 1099          |
//	*| Threads_connected                        | 5             |
//	*| Threads_created                          | 1104          |
//	*| Threads_running                          | 3             |
	private void getThreadsCounters(String key, String value)
	{
		if(key.compareTo("Threads_cached")==0)
			this.threads_cached=Long.parseLong(value);
		
		if(key.compareTo("Threads_connected")==0)
			this.threads_connected=Long.parseLong(value);
		
		if(key.compareTo("Threads_running")==0)
			this.threads_running=Long.parseLong(value);
		
		if(key.compareTo("Threads_created")==0)
			this.threads_created=Long.parseLong(value);
		
		if(key.compareTo("Connections")==0)
			this.connections=Long.parseLong(value);
		
		if(key.compareTo("Queries")==0)
			this.queries=Long.parseLong(value);
		
		if(key.compareTo("Max_used_connections")==0)
			this.max_used_connections=Long.parseLong(value);
	}
	
	//TODO: table locks
//	| Table_locks_immediate                    | 1083725122    |
//	| Table_locks_waited                       | 136305        |

	//TODO: query cache
//	| Qcache_free_blocks      | 144608    |
//	| Qcache_free_memory      | 545429704 |
//	| Qcache_hits             | 598213589 |
//	| Qcache_inserts          | 399321104 |
//	| Qcache_lowmem_prunes    | 14659790  |
//	| Qcache_not_cached       | 13650472  |
//	| Qcache_queries_in_cache | 284427    |
//	| Qcache_total_blocks     | 713806    |

	// queries per calcular q/s
//	| Queries                           | 3941885267   |

	// max used connections
//	| Max_used_connections | 152   |

	// joins sense indexos
//    $mycalc{'joins_without_indexes'} = $mystat{'Select_range_check'} + 
//										 $mystat{'Select_full_join'};
//    $mycalc{'joins_without_indexes_per_day'} = int($mycalc{'joins_without_indexes'} / 
//													($mystat{'Uptime'}/86400));

	//TODO: bytes enviats/rebuts
//	| Bytes_received                    | 208          |
//	| Bytes_sent                        | 8472         |
	private void getTrafficCounters(String key, String value)
	{
		if(key.compareTo("Bytes_received")==0)
		{
			this.bytes_received=Long.parseLong(value);
		}
		if(key.compareTo("Bytes_sent")==0)
		{
			this.bytes_sent=Long.parseLong(value);
		}
	}

	
	//TODO: handlers
//	mysql> show global status like 'Handler%';
//	+----------------------------+--------------+
//	| Variable_name              | Value        |
//	+----------------------------+--------------+
//	| Handler_commit             | 195267196    |
//	| Handler_delete             | 80495        |
//	| Handler_discover           | 0            |
//	| Handler_prepare            | 0            |
//	| Handler_read_first         | 23113506     |
//	| Handler_read_key           | 10086949532  |
//	| Handler_read_last          | 22738        | //mysql 5.5
//	| Handler_read_next          | 56863175709  |
//	| Handler_read_prev          | 12192797     |
//	| Handler_read_rnd           | 845716059    |
//	| Handler_read_rnd_next      | 262711530912 |
//	| Handler_rollback           | 3163         |
//	| Handler_savepoint          | 0            |
//	| Handler_savepoint_rollback | 0            |
//	| Handler_update             | 211544343    |
//	| Handler_write              | 2486911382   |
//	+----------------------------+--------------+
//	16 rows in set (0.00 sec)
	//http://www.fromdual.ch/mysql-handler-read-status-variables

	
	private void calcJoinCounters()
	{
		joins_without_indexes=select_range_check+select_full_join;
		
		joins_without_indexes_per_day=((double)joins_without_indexes)/
										(((double)this.uptime)/86400.0);
		joins_without_indexes_per_hour=((double)joins_without_indexes)/
										(((double)this.uptime)/3600.0);
		joins_without_indexes_per_min=((double)joins_without_indexes)/
										(((double)this.uptime)/60.0);
	}
	
	private void getJoinCounters(String key, String value)
	{
		if(key.compareTo("Select_range_check")==0)
		{
			this.select_range_check=Long.parseLong(value);
		}
		if(key.compareTo("Select_full_join")==0)
		{
			this.select_full_join=Long.parseLong(value);
		}
	}
	
	
	private void getUptime(String key, String value)
	{
		//TODO: capturar excepcions?
		
		if(key.compareTo("Uptime")==0)
		{
			this.uptime=Long.parseLong(value);
		}
		if(key.compareTo("Uptime_since_flush_status")==0)
		{
			this.uptime_since_flush_status=Long.parseLong(value);
		}
		
	}
	
	/*
	 * ========== GETTERS i SETTERS a partir d'aquí ==========
	 */
	
	/**
	 * @return TRUE if this server is a replication slave that is connected to a replication master, and both the I/O and SQL threads are running
	 */
	public boolean slave_running()
	{
		return this.slave_running;
	}
	
	/**
	 * establir stat del slave
	 */
	public void setSlaveStatus(MySQLSlaveStatus ss)
	{
		this.slave_status=ss;
	}
	
	/**
	 * 
	 * @return estat del slave, pot ser null
	 */
	public MySQLSlaveStatus getSlaveStatus()
	{
		return this.slave_status;
	}
}
