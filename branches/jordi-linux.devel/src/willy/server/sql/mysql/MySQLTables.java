package willy.server.sql.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import willy.server.sql.MySQLAccess;
import willy.server.sql.mysql.extra.DBItem;
import willy.server.sql.mysql.extra.MySQLEngine;
import willy.server.sql.mysql.extra.MySQLSchema;

//REFACTOR DBITEMS
public class MySQLTables implements Runnable{
	
	protected MySQLAccess db=null;
	protected volatile boolean done=false;
	protected volatile boolean failed=false;
	protected volatile boolean running=false;
	
	public boolean isRunning() { return this.running; }
	
	protected long timestamp;
	/**
	 * donat el nom del engine, obtinc el count
	 */
	protected HashMap<String, Integer> taulesDT_per_engine=null;
	
	/**
	 * donat el schema.taula obtinc l'estructura de dades
	 */
	protected HashMap<String, DBItem> taulesDT_per_schemataula=null;
	
	/**
	 * donat un schema, obtinc un llistat de taules
	 */
	protected HashMap<String,LinkedList<DBItem>> taulesDT_per_schema=null;
	
	/**
	 * dades del schema
	 */
	protected HashMap<String, MySQLSchema> schema_data=null;
	
	/**
	 * dades del engine
	 */
	protected HashMap<String, MySQLEngine> engine_data=null;
	
	/**
	 * taules que tenen algún error
	 */
	protected HashMap<String[], DBItem> taules_enfallo=null;
	
	public int getItemCountByEngine(String engine)
	{
		if(taulesDT_per_engine==null) return 0; //potser throw exception?
		
		Integer value=taulesDT_per_engine.get(engine);
		
		if(value==null) return 0;
		else return value.intValue();
	}
	
	//SELECT COUNT(TABLE_NAME) 
	//FROM information_schema.TABLES 
	//WHERE TABLE_SCHEMA NOT IN ('information_schema','mysql') AND Data_free > 0"
	//
	//		SELECT table_schema,TABLE_NAME,engine,DATA_LENGTH,Data_free
	//		FROM information_schema.TABLES 
	//		WHERE TABLE_SCHEMA NOT IN ('information_schema','mysql','performance_schema') 
	//		AND Data_free is not null;
	//
	//With innodb_file_per_table=OFF all InnoDB tables are stored in the same tablespace.
	//DATA_FREE (the number of allocated but unused bytes) is reported for that single tablespace.
	//
	//Read http://dev.mysql.com/doc/refman/5.0/en/innodb-file-space.html:
	
	
//	mysql> SELECT engine,sum(DATA_LENGTH),sum(Data_free) 
//	FROM information_schema.TABLES  
//	WHERE TABLE_SCHEMA NOT IN ('information_schema','mysql') 
//	GROUP BY engine;
//	+--------------------+------------------+----------------+
//	| engine             | sum(DATA_LENGTH) | sum(Data_free) |
//	+--------------------+------------------+----------------+
//	| ARCHIVE            |          2027783 |              0 |
//	| InnoDB             |        417071104 |      192937984 |
//	| MEMORY             |                0 |              0 |
//	| MyISAM             |       2855729030 |      271436768 |
//	| PERFORMANCE_SCHEMA |                0 |              0 |
//	+--------------------+------------------+----------------+
//	5 rows in set (1.38 sec)
	
	public MySQLTables(MySQLAccess db)
	{
		//Com que soc un thread, ho faig amb una conexió independent
		this.db=(MySQLAccess) db.newConnection();
		this.done=false;
		this.taulesDT_per_schemataula=new HashMap<String, DBItem>();
		this.taulesDT_per_engine=new HashMap<String, Integer>();
		this.taulesDT_per_schema=new HashMap<String,LinkedList<DBItem>>();
		this.schema_data=new HashMap<String, MySQLSchema>();
		this.engine_data=new HashMap<String, MySQLEngine>();
	}
	
	public MySQLEngine getEngineDetails(String engine)
	{
		return this.engine_data.get(engine);
	}
	
	public Set<String> getSchemas()
	{
		return this.schema_data.keySet();
	}
	
	public LinkedList<DBItem> getTables(String key)
	{
		return this.taulesDT_per_schema.get(key);
	}
	
	public MySQLSchema getSchema(String name)
	{
		return this.schema_data.get(name);
	}
	
	public void run()
	{
		this.running=true;
		this.taules_enfallo=null;
		
		/*

| information_schema | VIEWS                                 | SYSTEM VIEW | MyISAM |            0 |         0 |
| jobisjob           | web__uk__user_search_per_hour         | VIEW        | NULL   |         NULL |      NULL |
| jobisjob           | wkf_workflow                          | BASE TABLE  | MyISAM |      1609728 |         0 |
| jobisjob           | wsn_wikipedia_section_name            | BASE TABLE  | InnoDB |        16384 |         0 |
| mysql              | slow_log                              | BASE TABLE  | CSV    |            0 |         0 |

		 */
		
		ResultSet rs=null;
		
		try {
			
			rs=db.runQuery(/*
					"SELECT engine,sum(DATA_LENGTH),sum(Data_free) " +
					"FROM information_schema.TABLES  " +
					"WHERE TABLE_SCHEMA NOT IN ('information_schema','mysql') " +
					"GROUP BY engine;"*/
					"select table_schema,table_name,table_type,engine,data_length,data_free,table_comment " +
					"from information_schema.tables " +
					"where " +
					"table_schema not in ('information_schema','mysql','performance_schema')" 
					);
			
			DBItem dbt=null;
			Integer enginecount=null;
			LinkedList<DBItem> llistataules=null;
			
			while(rs.next())
			{
				dbt=new DBItem(rs);
				
				//actualitzo acces schema+taula
				this.taulesDT_per_schemataula.put(dbt.getSchemaAndTableName(), dbt);
				
				
				//actualizo acces per engine
				enginecount=this.taulesDT_per_engine.get(dbt.getEngine());
				
				if(enginecount==null)
					this.taulesDT_per_engine.put(dbt.getEngine(), new Integer(1));
				else
					this.taulesDT_per_engine.put(dbt.getEngine(), enginecount+1);
				
				//actualizo acces per schema
				llistataules=this.taulesDT_per_schema.get(dbt.getSchema());
				
				if(llistataules==null)
				{
					llistataules=new LinkedList<DBItem>();
					llistataules.add(dbt);
					this.taulesDT_per_schema.put(dbt.getSchema(), llistataules);
				}
				else
					llistataules.add(dbt);
				
				// mirar si hi ha taules xungues :
				//un altre hashmap que si es null indica q no hi ha taules xungues
				//GA: 78001980X
				
				/*
				 VISTA:

mysql> show table status like 'productossphinx'\G
				 *************************** 1. row ***************************
           Name: productossphinx
         Engine: NULL
        Version: NULL
     Row_format: NULL
           Rows: NULL
 Avg_row_length: NULL
    Data_length: NULL
Max_data_length: NULL
   Index_length: NULL
      Data_free: NULL
 Auto_increment: NULL
    Create_time: NULL
    Update_time: NULL
     Check_time: NULL
      Collation: NULL
       Checksum: NULL
 Create_options: NULL
        Comment: VIEW
1 row in set (0.00 sec)

mysql> select * from information_schema.TABLES where engine is null\G
				 *************************** 1. row ***************************
  TABLE_CATALOG: def
   TABLE_SCHEMA: mundoanimalia
     TABLE_NAME: productossphinx
     TABLE_TYPE: VIEW
         ENGINE: NULL
        VERSION: NULL
     ROW_FORMAT: NULL
     TABLE_ROWS: NULL
 AVG_ROW_LENGTH: NULL
    DATA_LENGTH: NULL
MAX_DATA_LENGTH: NULL
   INDEX_LENGTH: NULL
      DATA_FREE: NULL
 AUTO_INCREMENT: NULL
    CREATE_TIME: NULL
    UPDATE_TIME: NULL
     CHECK_TIME: NULL
TABLE_COLLATION: NULL
       CHECKSUM: NULL
 CREATE_OPTIONS: NULL
  TABLE_COMMENT: VIEW
1 row in set (0.21 sec)

pertant, si tot es NULL i el comment es VIEW... es una vista
				 */

				//| comentario_seguridad                     | NULL   |    NULL | NULL       |    NULL | 
				//NULL |        NULL |              NULL |         NULL |      NULL |           NULL | 
				//NULL                | NULL                | NULL                | NULL              | 
				//NULL | NULL           | 
				//Table './mundoanimalia/comentario_seguridad' is marked as crashed and should be repaired |
				if(dbt.isFailed()) 
				{
					if(taules_enfallo==null) taules_enfallo=new HashMap<String[], DBItem>();
					
					taules_enfallo.put(new String[] {dbt.getSchema(), dbt.getTableName()}, dbt);
				}
			}
			
			//rs.close();
			db.nextQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
			this.failed=true;
		}
		
		
		/*

mysql> 
SELECT CATALOG_NAME,SCHEMA_NAME,DEFAULT_CHARACTER_SET_NAME,DEFAULT_COLLATION_NAME,SQL_PATH 
FROM schemata 
WHERE schema_name NOT IN ('information_schema','performance_schema','mysql');
		  
+--------------+-------------+----------------------------+------------------------+----------+
| CATALOG_NAME | SCHEMA_NAME | DEFAULT_CHARACTER_SET_NAME | DEFAULT_COLLATION_NAME | SQL_PATH |
+--------------+-------------+----------------------------+------------------------+----------+
| def          | test        | utf8                       | utf8_general_ci        | NULL     |
+--------------+-------------+----------------------------+------------------------+----------+
1 row in set (0.00 sec)

mysql>
		 */ 
		
		try
		{
			rs=db.runQuery(
					"SELECT CATALOG_NAME,SCHEMA_NAME,DEFAULT_CHARACTER_SET_NAME," +
					"DEFAULT_COLLATION_NAME,SQL_PATH " +
					"FROM information_schema.schemata " +
					"WHERE schema_name NOT IN ('information_schema','performance_schema','mysql');"
					);
			
			MySQLSchema schema=null;
			while(rs.next())
			{
				schema=new MySQLSchema(rs);
				this.schema_data.put(schema.getSchemaName(), schema);
			}
			
			db.nextQuery();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			this.failed=true;
		}
		
		/*
		mysql> show engines;
		+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
		| Engine             | Support | Comment                                                        | Transactions | XA   | Savepoints |
		+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
		| FEDERATED          | NO      | Federated MySQL storage engine                                 | NULL         | NULL | NULL       |
		| MRG_MYISAM         | YES     | Collection of identical MyISAM tables                          | NO           | NO   | NO         |
		| MyISAM             | YES     | MyISAM storage engine                                          | NO           | NO   | NO         |
		| BLACKHOLE          | YES     | /dev/null storage engine (anything you write to it disappears) | NO           | NO   | NO         |
		| CSV                | YES     | CSV storage engine                                             | NO           | NO   | NO         |
		| MEMORY             | YES     | Hash based, stored in memory, useful for temporary tables      | NO           | NO   | NO         |
		| ARCHIVE            | YES     | Archive storage engine                                         | NO           | NO   | NO         |
		| InnoDB             | DEFAULT | Supports transactions, row-level locking, and foreign keys     | YES          | YES  | YES        |
		| PERFORMANCE_SCHEMA | YES     | Performance Schema                                             | NO           | NO   | NO         |
		+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
		9 rows in set (0.00 sec)
		*/
		
		try
		{
			rs=db.runQuery("show engines;");
			
			MySQLEngine engine=null;
			while(rs.next())
			{
				engine=new MySQLEngine(rs);
				this.engine_data.put(engine.getEngineName(), engine);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			this.failed=true;
		}
		
		System.out.println("table info DONE!!!"+this.taulesDT_per_engine);
		
		if(this.taules_enfallo!=null)
			System.out.println("en fallo:"+this.taules_enfallo.toString());
		
		db.close();
		this.done=true;
		this.timestamp = System.currentTimeMillis()/1000;
	}

	public boolean hasFailedTables()
	{
		return this.taules_enfallo!=null;
	}
	
	public String toString()
	{
		//return "taules: "+taulesDT_per_schemataula.toString()+"\n"+
		return		"engines: {"+this.taulesDT_per_engine +"}\n";
				//"taules per schema {"+this.taulesDT_per_schema+"}";
	}
	
	public boolean isDone() {
		return done;
	}
	
	public boolean isValid()
	{
		return timestamp!=0;
	}
	
	/*
	public boolean isReady()
	{
		
	}
	
	public boolean isStillRunning()
	{
		return running?!done:
	}
	*/
}
