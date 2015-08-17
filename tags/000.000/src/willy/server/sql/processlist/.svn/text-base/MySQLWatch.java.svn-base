package willy.server.sql.processlist;

import java.util.*;

import willy.server.sql.extra.*;
import willy.server.sql.extra.ListSlaves.Slave;
import willy.server.sql.extra.generics.DBStatus;
import willy.server.sql.extra.generics.DBVariables;

//cosa sql
//http://www.dotnetfunda.com/articles/article1949-tricks-to-speed-up-your-sql-query.aspx

public class MySQLWatch extends DBWatch {
	
	/*
	 * Només te en compte l'estat del MySQL
	 */
	
	//TODO: obtindre subconjunts de mostres segons el timestamp
	protected LinkedList<MySQLState> ldbs=null;
	
	/**
	 * llistat de slow queries actualitzades
	 */
	protected LinkedList<DBQuery> ldbq=null;
	
	/**
	 * llista global de slaves
	 */
	protected ListSlaves dbslaves=null;
	
	/**
	 * llista global de slow queries
	 */
	protected ListSlowQueries lslows=null;
	protected boolean hasSlaves=false;
	protected boolean isSlave=false;
	
	//TODO: subconjunts dels vectors de PL i Status
	
	public boolean isSlave() {
		return isSlave;
	}

	public boolean hasSlaves() {
		return this.hasSlaves;
	}

	public MySQLWatch()
	{
		this.ldbs=new LinkedList<MySQLState>();
		this.lslows=new ListSlowQueries();
		this.dbslaves=new ListSlaves();
		
	}
	
	/**
	 * analitza el processlist que tenim extraient:
	 * - genera llistat de slaves vistos (firstseen i lastseen) amb les mostres anteriors
	 */
	public int Do()
	{
		//proceso l'últim processlist:		
		Iterator<DBProcessItem> itdbpi=this.ldbs.getLast().getDBProcessList().iterator();
		
		ldbq=new LinkedList<DBQuery>();
		
		while(itdbpi.hasNext())
		{
			DBProcessItem dbpi=itdbpi.next();

			/*
			 * TODO: queries locked
+-----------+-----------+---------------------+---------------------+---------+------+----------------------+------------------------------------------------------------------------------------------------------+
| Id        | User      | Host                | db                  | Command | Time | State                | Info                                                                                                 |
+-----------+-----------+---------------------+---------------------+---------+------+----------------------+------------------------------------------------------------------------------------------------------+
| 356267544 | artesanum | 172.25.10.142:50574 | NULL                | Sleep   |    5 |                      | NULL                                                                                                 |
| 356544369 | artesanum | 172.25.10.146:55016 | artesanum           | Query   |  216 | Copying to tmp table | select distinct producto.producto_id from producto join producto_envio
on producto.producto_id=prod |
| 356544440 | artesanum | 10.12.16.9:49742    | artesanum           | Query   |  210 | Locked               | UPDATE producto SET producto_nombre = "" WHERE producto_id = "206287"                                |
| 356544441 | artesanum | 10.12.16.9:49744    | artesanum           | Query   |  210 | Locked               | select usuario_id, producto_id, producto_nombre, producto_ref, producto_desc, producto_precio, produ |
| 356544442 | artesanum | 10.12.16.9:49753    | artesanum           | Query   |  209 | Locked               | select * from producto where producto_id=12522                                                       |
(...)
			 */
			
			
			//Query executantse, la proceso al llistat de slows
			if(dbpi.getCommand().compareToIgnoreCase(DBWatch.MYSQL_PL_QUERY_STRING)==0)
			{
				this.hasSlaves=true;
				DBQuery dbq=this.lslows.seen(dbpi);
				
				//actualitzo la llista per la GUI
				if(dbq!=null)
					ldbq.add(dbq);
				
				//System.out.println("DEBUG query: "+dbpi.getInfo());
			}

			if(dbpi.getCommand().compareToIgnoreCase(DBWatch.SLAVE_WAITING_MASTER_MYSQL_PL_STRING)==0)
			{
				this.isSlave=true;
			}

			//si es slave, actualitzo el llistat de slaves que veig
			if(dbpi.getCommand().compareToIgnoreCase(DBWatch.MASTER_SLAVE_SEEN_MYSQL_PL_STRING)==0)
			{
				//paso el slave vist, actualitza si escau
				dbslaves.seen(dbpi);
			}

		}

		
		/*
		//recorregut de cada processlist analitzant les dades en global
		Iterator<DBProcessList> itdbpl=ldbpl.iterator();
		while(itdbpl.hasNext())
		{
			DBProcessList dbpl=itdbpl.next();
			
			//recorregut per cada process list (variable definida avans com local)
			itdbpi=dbpl.iterator();
			while(itdbpi.hasNext())
			{
				//TODO: fer algo en global
				DBProcessItem dbpi=itdbpi.next();
				
				//tonteria per treure el warning :P
				dbpi.getClass();
			}
			
		}
		*/
		//debug
		//this.lslows.dumpSlowQueries();
		
		return 0;
	}
	
	public void add(DBProcessList dbpl, DBStatus dbs, DBVariables dbv)
	{
		this.ldbs.add(new MySQLState(dbpl, (MySQLStatus)dbs, (MySQLVariables)dbv));
	}
	
	/**
	 * número d'estats del mysql que tenim guardats
	 */
	public int getStateCount()
	{
		//el tamany del llistat d'estats es el número de processlist q tenim
		return ldbs.size();
	}
	
	public MySQLState getLastState()
	{
		return this.ldbs.getLast();
	}
	
	public MySQLState getFirstState()
	{
		return this.ldbs.getFirst();
	}
	
	public int getLastProcessListStateCount(String state)
	{
		return this.ldbs.getLast().getDBProcessList().getStateCount(state);
	}
	
	public double[] getGlobalProcessListStateCount(String state)
	{
		double[] a=new double[this.ldbs.size()];
		
		Iterator<MySQLState> it=this.ldbs.iterator();
		
		int i=0;
		while(it.hasNext())
		{
			a[i]=(double)it.next().getDBProcessList().getStateCount(state);
			System.out.println(""+i+"="+a[i]);
			i++;
		}
		
		return a;
	}
	
	
	public int getLastProcessListCount()
	{
		return ldbs.getLast().getDBProcessList().getCount();
	}
	
	public int getSlavesCount()
	{
		return this.dbslaves.getCount();
	}

	public int getActiveSlavesCount() 
	{
		Iterator<Slave> itsl=this.dbslaves.getIterator();
		int activeslaves=0;
		
		while(itsl.hasNext())
		{
			Slave item=itsl.next();
			
			//System.out.println(item.getLastseen()+" vs "+this.ldbpl.lastElement().getTimestamp());
			
			if(item.getLastseen()<this.ldbs.getLast().getDBProcessList().getTimestamp())
				activeslaves++;
		}
		
		return activeslaves;
	}
	
	public int getInactiveSlavesCount()
	{
		return this.dbslaves.getCount()-this.getActiveSlavesCount();
	}
	
	public ListSlaves getListSlaves()
	{
		return this.dbslaves;
	}

	/**
	 * @deprecated
	 * @return cadena ascii de la representació del PL
	 */
	public String ProcessListRunningToASCII()
	{
		String out="Threads running * Threads sleep #\n\n";
		
		Iterator<MySQLState> it=this.ldbs.iterator();
		while(it.hasNext())
		{
			DBProcessList dbpl=it.next().getDBProcessList();
			
			//resto 1 pel show full processlist que sempre esta runing
			for(int i=0;i<dbpl.getStateCount(MYSQL_PL_QUERY_STRING)-1;i++)
				out+="*";
			for(int i=0;i<dbpl.getStateCount(MYSQL_PL_SLEEP_STRING);i++)
				out+="#";
			
			out+="\n";
		}
		
		return out;
	}
	
	public String toString()
	{
		return 
				this.ProcessListRunningToASCII()+
				"\nllistat complet queries vistes:"+this.lslows.toString()+
				"\nllistat slows amb maxtime de més de 1 segon:"+this.lslows.getSlowQueriesMaxTime(1)+
				"";
	}
	
	public ListSlowQueries getSlowQueriesList()
	{
		return this.lslows;
	}
	
	public LinkedList<DBQuery> getUpdatedQueries()
	{
		return this.ldbq;
	}

}
