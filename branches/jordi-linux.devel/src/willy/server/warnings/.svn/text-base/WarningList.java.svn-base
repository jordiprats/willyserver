package willy.server.warnings;

import java.util.List;
import java.util.Vector;

public class WarningList {
	
	public static final int WARNING_TYPE_DB_MYSQL              = 100000;
	
	public static final int WARNING_TYPE_GLOBAL_WARNING        = 010000;
	
	public static final int WARNING_TYPE_SLAVE_WARNING         = 001000;
	
	//sense slaves pero amb binary logs
	public static final int WARNING_SLAVE_NOSLAVE_BINLOG       = 000001;
	// es un slave, pero no esta en read_only
	public static final int WARNING_SLAVE_SI_SLAVE_NO_READONLY = 000002;
	//no es neteja el relay log
	public static final int WARNING_SLAVE_RELAYLOG_NONETEJA    = 000003;
	
	protected List<Warning> lwarnings=null;
	
	public WarningList()
	{
		lwarnings=new Vector<Warning>();
	}
	
	public void add(String text, int ... id)
	{
		int idwarning=0;
		for (int i = 0; i < id.length; i++)
		{
			System.out.println(""+i+"="+id[i]+"\n");
			idwarning+=id[i];
		}
		
		this.lwarnings.add(new MySQLWarning(idwarning,text));
		
	}

	public String toString()
	{
		return this.lwarnings.toString();
	}
}
