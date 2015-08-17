package willy.server.sql.mysql.engines;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import willy.server.regex.RegexParser;

public class InnoDBStatus {

	/**
	 * si timestamp (DBVariables) es 0, ignorar valors de variables
	 */
	protected long timestamp;
	
	/**
	 * @return true si la classe conté informacio vàlida
	 */
	public boolean isValid()
	{
		return timestamp!=0;
	}
	
	//http://www.exampledepot.com/egs/java.util.regex/tokenize.html
	
	//temporal
	protected HashMap<Integer, InnoDBTransaction> transaccions=null;
	
	public InnoDBStatus(ResultSet rs) throws SQLException
	{
		timestamp = System.currentTimeMillis()/1000;
		
		this.transaccions=new HashMap<Integer, InnoDBTransaction>();
		
		//String patternStr = "-[-]?\n[^a-z]?\n-[-]?\n";
		//TODO: xifrar les regex amb RC4?
		//http://www.koders.com/java/fidC4F1FFB5D88C4531D7C8E1B40B3535DACCFCE889.aspx
		String patternStr = "-[-]*\n[^a-z]*\n-[-]*";
		String innodbstatus=null;
		
		Pattern pattern = Pattern.compile(patternStr);
		
		if(rs.next())
		{
//		mysql> show innodb status\G
//		*************************** 1. row ***************************
//		  Type: InnoDB
//		  Name: 
//		Status: 
			innodbstatus=rs.getString(3);
		}
		else return; //TODO: AQUI potser llençar excepció o algo?
		
		//Iterator tokenizer = new RETokenizer(innodbstatus, patternStr, true);
		Matcher matcher = pattern.matcher(innodbstatus);
		String titolseccio=null;
		String dataseccio=null;
		String titolnext=null;
		
		//System.out.println("iniPARSER");
		
		int lastEnd = 0;
		while (matcher.find())
		{
			dataseccio = innodbstatus.subSequence(lastEnd, matcher.start()).toString();
			titolseccio=titolnext;
			titolnext = matcher.group();
            lastEnd = matcher.end();
            
            //System.out.println("***");
            if(titolseccio==null)
            {
            	//TODO: descarto el títol i el temps de càlcul???
            	
//            	=====================================
//            	120928  8:54:53 INNODB MONITOR OUTPUT
//            	=====================================
//            	Per second averages calculated from the last 5 seconds

            	
            	continue; 
            }
            //System.out.println("A: "+titolseccio);
            //System.out.println("B: "+dataseccio);
            //System.out.println("***");
            
            this.getThreadInfo(titolseccio, dataseccio);
            this.getLatestDeadLock(titolseccio, dataseccio);
		}
		
		System.out.println("fiPARSER");
	}
	
	private void getThreadInfo(String titol, String dades)
	{
		Integer i=0;
		if(titol!=null && titol.contains("TRANS"))
		{
			System.out.println("####################Transaccions: "+dades);
			
			//TODO: trocejar per "---TRANSACTION "
			
			String patternStr = "---TRANSACTION ";
			
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(dades);
			String titolseccio=null;
			String dataseccio=null;
			String titolnext=null;
			
			//System.out.println("iniPARSERtransaccions");
			
			int lastEnd = 0;
			InnoDBTransaction idbt=null;
			while (matcher.find())
			{
				dataseccio = dades.subSequence(lastEnd, matcher.start()).toString();
				titolseccio=titolnext;
				titolnext = matcher.group();
	            lastEnd = matcher.end();
	            
	            //System.out.println("***");
	            if(titolseccio!=null)
	            {
	            	/*
	            	System.out.println("X: "+titolseccio);
	            	System.out.println("Y: "+dataseccio);
	            	System.out.println("Z: "+titolnext);
	            	System.out.println("***");
	            	*/
	            	idbt= new InnoDBTransaction(dataseccio);
	            	this.transaccions.put(i, idbt);
	            	i++;
	            }
	            
	            //WTF? this.getThreadInfo(titolseccio, dataseccio);
			}
			
			idbt=new InnoDBTransaction(dades.subSequence(lastEnd, dades.length()).toString());
			this.transaccions.put(i, idbt);
			
			//System.out.println("fiPARSERtransaccions");
			
			System.out.println("TOTAL TRANSACCIONS: "+this.transaccions.toString());
		}
	}


	private void getLatestDeadLock(String titol, String dades)
	{
		RegexParser regexp=null;
		
		if(titol!=null && titol.contains("LATEST DETECTED DEADLOCK"))
		{
			
			//Data del deadlock:
			//120920 1:14:22
			//^\\d+[ ]+\\d+:\\d+:\\d+$
			
			//substring ?
			regexp=new RegexParser(dades.substring(0, 40), "\n(\\d+[ ]+\\d+:\\d+:\\d+)\n");
			System.out.println("@ULTIMDEADLOCK: "+regexp.getMatch(1));
			System.out.println(dades.substring(0, 20));
			
	//LATEST DETECTED DEADLOCK
	
	//DEADLOCKS
	//http://www.chriscalender.com/?p=426
//	------------------------
//	LATEST DETECTED DEADLOCK
//	------------------------
//	120920 1:14:22
//	*** (1) TRANSACTION:
//	TRANSACTION 35236C7EC, ACTIVE 5 sec starting index read
//	mysql tables in use 2, locked 2
//	LOCK WAIT 5 lock struct(s), heap size 1248, 4 row lock(s)
//	MySQL thread id 128825727, OS thread handle 0x4bbb5940, query id 10972932601 10.12.50.115 niumba Sending data
//	UPDATE tbl_apartamentos INNER JOIN tmp_tbl_modificacion_apartamentos ON tbl_apartamentos.id =tmp_tbl_modificacion_apartamentos.fk_id_tbl_apartamentos SET tbl_apartamentos.fec_ultima_modificacion=tmp_tbl_modificacion_apartamentos.fec_ultima_modificacion WHERE tbl_apartamentos.id IN (204055,87800,451205,50423,354976,473662,77580,401640,293710,219793,76481,228630,233792,41081,231037,72013,259362,203034,331998,436392,465135,73735,56011,40504,73370,341087,438402,443632,175027,42084,315868,453980,321952,80393,82615,178365,469692,235522,100087,133386,221204,381415,470592,468680,439054,468677,468137,468000)
//	*** (1) WAITING FOR THIS LOCK TO BE GRANTED:
//	RECORD LOCKS space id 27610 page no 6382 n bits 96 index `PRIMARY` of table `niumba_utf8`.`tbl_apartamentos` trx id 35236C7EC lock_mode X locks rec but not gap waiting
//	Record lock, heap no 18 PHYSICAL RECORD: n_fields 87; compact format; info bits 0
//
//	*** (2) TRANSACTION:
//	TRANSACTION 35236B734, ACTIVE 31 sec fetching rows, thread declared inside InnoDB 367
//	mysql tables in use 1, locked 1
//	9443 lock struct(s), heap size 850360, 294897 row lock(s), undo log entries 71890
//	MySQL thread id 128823725, OS thread handle 0x5abe7940, query id 10972926218 10.12.50.115 niumba Updating
//	UPDATE tbl_apartamentos SET fk_ids_mask_productos = COALESCE(fk_ids_mask_productos,0)+65536 WHERE fk_id_tbl_paises NOT IN ('ES', 'AD') AND (COALESCE(fk_ids_mask_productos,0) & 65536) = 0 and fks_id_tbl_mask_apartamentos = 1073676289
//	*** (2) HOLDS THE LOCK(S):
//	RECORD LOCKS space id 27610 page no 6382 n bits 96 index `PRIMARY` of table `niumba_utf8`.`tbl_apartamentos` trx id 35236B734 lock_mode X
//	Record lock, heap no 1 PHYSICAL RECORD: n_fields 1; compact format; info bits 0
//	 0: len 8; hex 73757072656d756d; asc supremum;;
//
//	Record lock, heap no 2 PHYSICAL RECORD: n_fields 87; compact format; info bits 0
//
//	*** WE ROLL BACK TRANSACTION (1)

	
		}
	}

}
