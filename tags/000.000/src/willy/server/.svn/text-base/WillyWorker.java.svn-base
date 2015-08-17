package willy.server;

import java.util.Iterator;
import java.util.LinkedList;

import willy.server.dba.DBA;
import willy.server.sql.extra.MySQLState;

public class WillyWorker implements Runnable{
	
	protected LinkedList<DBA> ldba=null;
	protected WillyGUI gui=null;
	
	public WillyWorker(LinkedList<DBA> ldba, WillyGUI gui)
	{
		this.ldba=ldba;
		this.gui=gui;
	}
	
	public void run()
	{
		if(ldba==null) return;
		
		Iterator<DBA> itdba=ldba.iterator();
		while(itdba.hasNext())
		{
			DBA dba=itdba.next();
			
			//TODO: nomes una vegada o algo així
			dba.enableGlobalWarnings();
			
			//analitza stat db
			dba.Do();
			
			/*
			System.out.println("//////////////////////////////////////");
			System.out.println(dba.getHostname()+" "+(dba.getType()==DBA.TYPE_MYSQL?"MySQL":"WTF?")+" "+dba.getVersion());		
			System.out.println("#stats: "+dba.getStateCount());
			System.out.println("#sleeps: "+dba.getLastProcessListStateCount(DBA.MYSQL_STATUS_SLEEP));
			System.out.println("#PL: "+dba.getLastProcessListCount());
			System.out.println("#slaves: "+dba.getActiveSlavesCount());
			System.out.println("//////////////////////////////////////");
			*/
			
			//informa a la GUI 
			//TODO: que pasa si hi ha més d'un host?
			//TODO: refactor pq sigui més semblant a gui.updateValues()
			gui.addSleepSample(dba.getLastProcessListStateCount(DBA.MYSQL_STATUS_SLEEP));
			gui.addQuerySample(dba.getLastProcessListStateCount(DBA.MYSQL_STATUS_QUERY));
			
			gui.updateValues((MySQLState)dba.getLastState());
			gui.updateQueries(dba.getUpdatedQueries());
			
			gui.setTitle(dba.getHostname()+" "+(dba.getType()==DBA.TYPE_MYSQL?"MySQL":"WTF?")+" "+dba.getVersion());
		}
	}

}
