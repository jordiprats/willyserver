package willy.server.dba;

import java.sql.SQLException;
import java.util.LinkedList;

import willy.server.sql.*;
import willy.server.sql.extra.*;
import willy.server.sql.extra.grants.MySQLGrants;
import willy.server.sql.mysql.MySQLTables;
import willy.server.sql.mysql.engines.InnoDBStatus;
import willy.server.sql.processlist.MySQLWatch;
import willy.server.warnings.WarningList;
import willy.singletons.DBLink;

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
	
	protected MySQLTables dbtbls=null;
	
	public MySQLDBA(DBParam dbp)
	{
		this.timestamp_start = System.currentTimeMillis()/1000;
		this.db=new MySQLAccess(dbp);
		this.dbwatch=new MySQLWatch();
		this.global_warnings=new WarningList();
		this.dbtbls=new MySQLTables((MySQLAccess)this.db);
		
		DBLink.getInstance().setDBAccess(this.db);
	}
	
	public boolean hasTablesInfo()
	{
		return dbtbls.isDone();
	}
	
	public MySQLTables getTablesInfo()
	{
		return this.dbtbls;
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
				//obtinc les dades
				this.grants.getGrants(db);
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
				this.grants=null;
			}
			/*
			if(this.grants!=null)
			{
				//check usuaris sense contrasenya
				this.global_warnings.add("WARNING sense password:"+this.grants.getByPassword(""));
				//check passwords de 16 caracters
				this.global_warnings.add("WARNING oldpasswords:"+this.grants.getWeakPasswords());
				//check usuaris invàlids
				this.global_warnings.add("WARNING invalids:"+this.grants.getInvalidUsers());
				
			}
			*/
		}
		
		
		
		//detalls dels items de la inst�ncia
		//amb un thread pq pot tardar un ou
		//this.dbe.DoSomething();
		//potser tindre un llistat de thread a llençar?
		if(
				(!dbtbls.isValid())&&
				(!dbtbls.isRunning())
				)
		{
			dbtbls=new MySQLTables((MySQLAccess) db);
			new Thread(dbtbls).start();
		}
		
		//obtindre show status
		MySQLStatus dbstatus=(MySQLStatus) db.getGlobalStatus();
		
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
		
		MySQLState laststate=null;
		
		if(this.dbwatch.getStateCount()>0)
		{
			laststate=(MySQLState) this.dbwatch.getLastState();
			dbstatus.setDiferentialCounters(laststate.getMySQLStatus());
			
			//System.out.println("timestamp: "+laststate.getDBProcessList().getTimestamp());
			System.out.println("q/s: "+dbstatus.getQueriesPerSec());
			System.out.println("recieved "+dbstatus.getDiferentialBytesReceived()/1024+"kB/s");
			System.out.println("sent "+dbstatus.getDiferentialBytesSent()/1024+"kB/s");
		}
		
		
		//afegin un nou processlist i dem�s
		DBProcessList dbpl=db.getProcessList();
		
		InnoDBStatus idbs=((MySQLAccess)db).getInnoDBStatus();
		
		//TODO: ajuntar DBA amb DBWatch d'una puta vegada
		((MySQLWatch)this.dbwatch).add(dbpl, dbstatus, this.dbv, this.grants, idbs);
		
		if(dbtbls.isDone())
			this.dbwatch.setDBItems(dbtbls);
		
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
		return ((MySQLState)this.dbwatch.getLastState()).getMySQLVariables().getVersion();
	}
	
	/**
	 * extra versi�
	 */
	public String getVersionExtraInfo()
	{
		return ((MySQLState)this.dbwatch.getLastState()).getMySQLVariables().getVersionComment();
	}
	
	/**
	 * versió del OS - falta implementar
	 */
	public String getOS()
	{
		return ((MySQLState)this.dbwatch.getLastState()).getMySQLVariables().getVersionCompileOS();
	}
	
	/**
	 * arquitectura hardware
	 */
	public String getArchitecture()
	{
		return ((MySQLState)this.dbwatch.getLastState()).getMySQLVariables().getVersionCompileMachine();
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
	
	/**
	 * @deprecated
	 */
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
