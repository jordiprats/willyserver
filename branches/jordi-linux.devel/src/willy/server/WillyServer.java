package willy.server;

import java.util.*;
import willy.server.dba.*;
import willy.server.gui.WillyGUI;
import willy.server.sql.DBParam;


//trilead-ssh2: conexió ssh a servers
//https://github.com/jenkinsci/trilead-ssh2
//BSD license, fuck yeah! http://www.jcraft.com/jsch/

//TODO: leak de conexions si no te permisos

//Per si mai faig la versió oracle:
//http://www.shutdownabort.com/dbaqueries/Administration_Status.php

//framwork RPC-like
//http://code.google.com/p/java-gearman-service/

//TODO: guardar deadlocks amb el timestamp q s'ha produit (copiar amb GUI :P)
//http://www.mysqlperformanceblog.com/2012/09/19/logging-deadlocks-errors/

//pel thread auto-killer:
//http://www.tutorialspoint.com/java/java_thread_communication.htm

public class WillyServer {
	
	protected static final String jetprofiler="Good artists copy, great artists steal. Pablo Picasso";
	
	/**
	 * llistat de parametres de la db. caldrà carregarse d'algun lloc
	 */
	protected static List<DBParam> ldbp=new Vector<DBParam>();
	
	/**
	 * llistat de DBAs, un per db a la que conectem
	 */
	protected static LinkedList<DBA> ldba=new LinkedList<DBA>();
	

	public static void main(String[] args) 
	{
		
		/*
		 * mysql> grant REPLICATION CLIENT,select,process,super on *.* to testro@'%' identified by 'testroncdcdcweq721984';
		 * Query OK, 0 rows affected (0.01 sec)
		 */
		
		/*
		ldbp.add(new DBParam(	"testro",
				"testroncdcdcweq721984",
				"penny")
		);
		
		ldbp.add(new DBParam(	"testro",
				"testroncdcdcweq721984",
				"zarek")
		);
		*/
		ldbp.add(new DBParam(	"testro",
								"testroncdcdcweq721984",
								"caronte")
		);
		/*
		ldbp.add(new DBParam(	"testro",
				"testroncdcdcweq721984",
				"sparrow")
		);
		
		ldbp.add(new DBParam(	"testro",
				"testroncdcdcweq721984",
				"harlock")
		);
		
		ldbp.add(new DBParam(	"testro",
				"testroncdcdcweq721984",
				"starbuck")
		);
		
		ldbp.add(new DBParam(	"testro",
				"testroncdcdcweq721984",
				"huargo")
		);
		
		ldbp.add(new DBParam(	"testro",
				"testroncdcdcweq721984",
				"nenuco")
		);
		
		ldbp.add(new DBParam(	"testro",
				"testroncdcdcweq721984",
				"localhost")
		);
		*/
		
		Iterator<DBParam> itdbp=ldbp.iterator();
		while(itdbp.hasNext())
		{
			DBA dba=new MySQLDBA(itdbp.next());
			ldba.add(dba);
			
			WillyGUI gui=new WillyGUI();
			new Thread(gui).start();
			
			WillyWorker ww=new WillyWorker(dba, gui);
			while(!gui.isOpen());
			
			while(true)
			{	
				gui.addRunnable(ww);
				//gui.addRunnable(mytables);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(!gui.isOpen()) break;
			}
			
		}

		//tancar conexions a les dbs
		Iterator<DBA> itdba=ldba.iterator();
		while(itdba.hasNext())
		{
			DBA dba=itdba.next();
			System.out.println("@warnings globals: "+dba.getGlobalWarningsList());
			
			//tancar
			dba.close();
		}
		
	}
}
