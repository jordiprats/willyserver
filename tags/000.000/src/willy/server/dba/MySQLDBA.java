package willy.server.dba;

import java.sql.SQLException;
import java.util.LinkedList;

import willy.server.sql.*;
import willy.server.sql.extra.*;
import willy.server.sql.extra.grants.MySQLGrants;
import willy.server.sql.processlist.MySQLWatch;
import willy.server.warnings.WarningList;

/*
 * mysql> show open tables;
 * +---------------+----------------------------+--------+-------------+
 * | Database      | Table                      | In_use | Name_locked |
 * +---------------+----------------------------+--------+-------------+
 * | rememori      | places                     |      0 |           0 |
 * | rememori      | roles                      |      0 |           0 |
 * | rememori_uy   | companies                  |      0 |           0 |
 * | rememori_uy   | condolences                |      0 |           0 |
 * | rememori_arg  | condolences                |      0 |           0 |
 * | rememori_uy   | contact_tickets            |      0 |           0 |
 */


public class MySQLDBA extends DBA {
	/*
	 * No només ha de tindre en compte l'estat del MySQL
	 * sino també del sistema en que s'executa
	 */
	
	public MySQLDBA(DBParam dbp)
	{
		this.timestamp_start = System.currentTimeMillis()/1000;
		this.db=new MySQLAccess(dbp);
		this.dbwatch=new MySQLWatch();
		this.global_warnings=new WarningList();
	}
	
	/**
	 * obte stat de la db i analitza (si pot) amb les mostres anteriors
	 */
	public int Do()
	{	
		//TODO: potser alguna condició pq es fagi periodicament?
		if(dbv==null) 
		{
			//obtindre les variables
			this.dbv=db.getVariables();			
		}
		
		//TODO: potser alguna condició pq es fagi periodicament?
		if(this.grants==null)
		{
			this.grants=new MySQLGrants();
			
			try
			{
				this.grants.addGrants(db);
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
				this.grants=null;
			}

			if(this.grants!=null)
			{
				//TODO: check usuaris sense contrasenya
				this.global_warnings.add("WARNING sense password:"+this.grants.getByPassword(""));
				
				//TODO: check passwords de 16 caracters
				this.global_warnings.add("WARNING oldpasswords:"+this.grants.getWeakPasswords());
				
				//TODO: check usuaris invàlids
				this.global_warnings.add("WARNING invalids:"+this.grants.getInvalidUsers());
				
			}
		}
		
		MySQLStatus dbstatus=(MySQLStatus) db.getStatus();
		
		//obtindre dades del slave, si escau
		if(dbstatus.slave_running())
			dbstatus.setSlaveStatus((MySQLSlaveStatus)db.getSlaveStatus());
		
		//TODO: open files
		//$mycalc{'pct_files_open'} = int($mystat{'Open_files'}*100/$myvar{'open_files_limit'});

		//TODO: SHOW OPEN TABLES
		//número de locks i en quina taula
		//http://dev.mysql.com/doc/refman/5.5/en/show-open-tables.html
		// if (((P)localObject2).a("4.0.0") >= 0) 
		//((aI)localObject1).a("SHOW OPEN TABLES", new Object[0]); 
		//else a.warn("Open tables not supported before 4.0.0, current version is " + localObject2);
		
		
		//afegin un nou processlist
		this.dbwatch.add(db.getProcessList(), dbstatus, this.dbv);
		
		//procesem stats procesos 
		dbwatch.Do();
		
		//aqui podem comparar lo vist als processos amb les variables
		if(this.enable_global_warnings)
		{
			//generem i deshabilitem
			this.enable_global_warnings=false;
			
			this.DoGlobalWarnings();
		}
		
		//TODO: aqui analitzar status + variables
		
		return 0;
	}
	
	/**
	 * Versió del MySQL
	 */
	public String getVersion()
	{
		return db.getVersion();
	}
	
	/**
	 * Neteja conexions al tancar la clase
	 */
	public void close()
	{
		System.out.println(this.dbwatch);
		System.out.println(this.grants);
		this.db.close();
	}
	
	public int getStateCount()
	{
		return this.dbwatch.getStateCount();
	}
	
	/**
	 * conta el nombre de vegades apareix el stat 
	 * @param stat que volem contar a lúltim processlist
	 * @return count
	 */
	public int getLastProcessListStateCount(String state)
	{
		return this.dbwatch.getLastProcessListStateCount(state);
	}
	
	public int getLastProcessListCount()
	{
		return this.dbwatch.getLastProcessListCount();
	}
	
	public String getHostname()
	{
		return this.db.getParam().getHostname();
	}
	
	public int getActiveSlavesCount()
	{
		return this.dbwatch.getActiveSlavesCount();
	}
	
	/**
	 * Tipus de DB
	 */
	public int getType()
	{
		return DBA.TYPE_MYSQL;
	}
	
	/**
	 * Helper per generar els warnings globals
	 */
	protected void DoGlobalWarnings()
	{
		//TODO: haurien de ser classes amb codi per tindre una llista de warnings "clearejats"
		
		//sense slaves pero amb binary logs
		if(!((MySQLWatch)this.dbwatch).hasSlaves()&&((MySQLVariables)this.dbv).logbin())
		{
			this.global_warnings.add("No te slaves pero te el binary log habilitat");
		}
		
		//es un slave, pero no esta en read_only
		if(((MySQLWatch)this.dbwatch).isSlave()&&
				!((MySQLVariables)this.dbv).read_only())
		{
			this.global_warnings.add("Es slave pero no esta en read_only");
		}
		
		//no es neteja el relay log
		if(!((MySQLVariables)this.dbv).relay_log_purge())
		{
			this.global_warnings.add("No es neteja el relay log",
								WarningList.WARNING_TYPE_DB_MYSQL,
								WarningList.WARNING_TYPE_GLOBAL_WARNING,
								WarningList.WARNING_TYPE_SLAVE_WARNING,
								WarningList.WARNING_SLAVE_RELAYLOG_NONETEJA
								);
		}
		
		this.global_warnings.add("test", 1);
		//TODO: si no es fa servir InnoDB, MyISAM o BDB recomenar treure (a partir tamanys)
	}
	
	public double[] getGlobalProcessListStateCount(String state)
	{
		return this.dbwatch.getGlobalProcessListStateCount(state);
	}
	
	public MySQLState getLastState()
	{
		return (MySQLState) this.dbwatch.getLastState();
	}
	
	public MySQLState getFistState()
	{
		return (MySQLState) this.dbwatch.getFirstState();
	}
	
	public ListSlowQueries getSlowQueriesList()
	{
		return this.dbwatch.getSlowQueriesList();
	}
	
	public LinkedList<DBQuery> getUpdatedQueries()
	{
		return this.dbwatch.getUpdatedQueries();
	}
}
