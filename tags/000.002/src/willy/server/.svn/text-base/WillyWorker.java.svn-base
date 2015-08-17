package willy.server;

import willy.server.dba.DBA;
import willy.server.gui.WillyGUI;

//CAJO: http://en.wikipedia.org/wiki/Cajo_project

public class WillyWorker implements Runnable{

	protected DBA dba=null;
	protected WillyGUI gui=null;

	public WillyWorker(DBA dba, WillyGUI gui)
	{
		this.dba=dba;
		this.gui=gui;
	}

	public void run()
	{
		//TODO: nomes una vegada o algo aix�
		dba.enableGlobalWarnings();

		try
		{
			//analitza stat db
			dba.Do();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		gui.setTitle(dba.getHostname()+" - "+(dba.getType()==DBA.TYPE_MYSQL?"MySQL":"WTF?")+" "+
						dba.getVersion()+" ["+dba.getVersionExtraInfo()+"] "+
						"for "+dba.getOS()+" ("+dba.getArchitecture()+")");
		
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
		//cada host, una Shell() diferent
		// refactor pq sigui m�s semblant a gui.updateValues()

		gui.updateGUI(dba);

	}


}