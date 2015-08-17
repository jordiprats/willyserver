package willy.server.sql.extra;

import java.util.*;

/*
 * 
 * ftp debugger
 */


public class ListSlaves {
	
	public class Slave
	{
		public long getLastseen() {
			return lastseen;
		}

		public void setLastseen(long lastseen) {
			this.lastseen = lastseen;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public void setPort(int port) {
			this.port = port;
		}

		//comparar objectes iguals
		public boolean equals(Object obj)
		{
			//si es la mateixa referencia, es el mateix objecte
			if(this == obj) return true;
			
			Slave other=(Slave)obj;
			
			return (
					(this.getHost().compareTo(other.getHost())==0)
					&&(this.getPort()==other.getPort())
					);
		}
		
		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		public long getFirstseen() {
			return firstseen;
		}

		public void setFirstseen(long firstseen) {
			this.firstseen = firstseen;
		}

		protected String host;
		protected int port;
		protected long firstseen=0;
		protected long lastseen=0;
		
		public Slave(String[] data, long seen) throws Exception
		{
			this.host=data[0];
			this.lastseen=this.firstseen=seen;
		}
	}

	//TODO: potser canviar la llista per un HashMap?
	protected Vector<Slave> lslaves=null;
	
	public ListSlaves()
	{
		this.lslaves=new Vector<Slave>();
	}
	
	public Iterator<Slave> getIterator()
	{
		return lslaves.iterator();
	}
	
	public int getCount()
	{
		return lslaves.size();
	}
	
	public void seen(DBProcessItem dbpi)
	{
		String[] slavedata=dbpi.getHost().split(":");
		Slave slaveseen=null;
		
		try {
			slaveseen = new Slave(slavedata,dbpi.getTimestamp());
		} catch (Exception e1) {
			// TODO que fer si hi ha algo no esperat al veure un slave?
			e1.printStackTrace();
		}

		if(lslaves.size()==0)
		{
			lslaves.add(slaveseen);
		}
		else
		{
			Iterator<Slave> itslave=lslaves.iterator();
			while(itslave.hasNext())
			{
				Slave slave=itslave.next();
				
				if(slave==slaveseen)
				{
					// modifico el firstseen, el lastseen ja esta definit pel constructor
					slaveseen.setFirstseen(slave.getFirstseen());
					
					//actualitzo la llista (l'ordre no m'importa)
					itslave.remove();
					lslaves.add(slaveseen);
				}
			}
		}
	}

}
