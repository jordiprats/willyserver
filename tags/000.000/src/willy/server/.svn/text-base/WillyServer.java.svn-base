package willy.server;

import java.util.*;



import willy.server.dba.*;
import willy.server.sql.DBParam;

//trilead-ssh2: conexió ssh a servers
//https://github.com/jenkinsci/trilead-ssh2

//TODO: leak de conexions si no te permisos

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
		 * mysql> grant REPLICATION CLIENT,select,process on *.* to testro@'%' identified by 'testroncdcdcweq721984';
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
		*/
		
		WillyGUI gui=new WillyGUI();
		new Thread(gui).start();
		
		Iterator<DBParam> itdbp=ldbp.iterator();
		while(itdbp.hasNext())
		{
			ldba.add(new MySQLDBA(itdbp.next()));
		}

		while(!gui.isOpen());
		
		WillyWorker ww=new WillyWorker(ldba, gui);
		
		for(int i=0;i<600;i++)
		{	
			gui.addData(ww);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(!gui.isOpen()) break;
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
