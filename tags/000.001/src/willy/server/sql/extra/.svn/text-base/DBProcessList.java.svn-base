package willy.server.sql.extra;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import willy.server.sql.extra.generics.DBStatus;

public class DBProcessList {
	
	protected long timestamp=0;
	//protected List<DBProcessItem> ldbi= null;
	protected HashMap<Integer, DBProcessItem> hmdbpi=null;
	protected HashMap<String, Integer> states=null;
	protected List<DBStatus> ldbs=null;
	protected boolean hasSlaves=false;
	protected boolean isSlave=false;
	protected int lockedcount=0;
	
	public void seenLockedQuery()
	{
		this.lockedcount++;
	}
	
	public int getLockedCount()
	{
		return this.lockedcount;
	}

	public boolean isSlave() {
		return isSlave;
	}

	public boolean hasSlaves() {
		return this.hasSlaves;
	}
	
	public void setIsSlave(boolean isSlave)
	{
		this.isSlave=isSlave;
	}
	
	public void setHasSlave(boolean hasSlaves)
	{
		this.hasSlaves=hasSlaves;
	}

	public DBProcessList(ResultSet rs) throws SQLException
	{
		this.timestamp = System.currentTimeMillis()/1000;
		//this.ldbi = new Vector<DBProcessItem>();
		
		this.hmdbpi = new HashMap<Integer, DBProcessItem>();
		this.ldbs=new Vector<DBStatus>();
		this.states = new HashMap<String, Integer>();
		
		while (rs.next())
		{
			DBProcessItem dbpi=new DBProcessItem(rs, timestamp, this);
			
			// debug
			//System.out.println(this.getClass().getName()+": "+dbpi.getUser()+" "+dbpi.getState());
			
			//obtindre valor anterior (si existeix)
			Integer statecount=this.states.get(dbpi.getCommand());
			if(statecount==null) statecount=new Integer(0);
			
			//set nou valor +1
			this.states.put(dbpi.getCommand(), Integer.valueOf(statecount.intValue()+1));
			
			//ldbi.add(dbpi);
			this.hmdbpi.put(Integer.parseInt(dbpi.getId()), dbpi);
		}
	}
	
	public DBProcessItem getDBProcessItem(Integer id)
	{
		return this.hmdbpi.get(id);
	}
	
	public void addStatus(DBStatus dbs)
	{
		this.ldbs.add(dbs);
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public int getCount()
	{
		//TODO: -1 per la meva conexió, fer millor amb filtres
		return this.hmdbpi.size()-1;
	}
	
	public Iterator<DBProcessItem> iterator()
	{
		return this.hmdbpi.values().iterator();
	}
	
	public int getStateCount(String key)
	{
		//this.dumpStateCount();
		Integer statecount=((Integer)this.states.get(key));
		
		if(statecount==null) return 0;
		else return statecount.intValue();
	}
	
	/**
	 * @deprecated no s'hauria de fer servir, es per debug
	 */
	public void dumpStateCount()
	{
		System.out.println(this.states);
	}
}
