package willy.server.sql.mysql.engines;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
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
	
	/**
	 * transaccions per MySQLID
	 */
	protected HashMap<Long, InnoDBTransaction> transaccions=null;
	public InnoDBTransaction getTransaction(Long mysqlid) { return this.transaccions.get(mysqlid); }
	
	public Iterator<Long> getMySQLIDs()
	{
		return this.transaccions.keySet().iterator();
	}
	
	public InnoDBStatus(ResultSet rs) throws SQLException
	{
		timestamp = System.currentTimeMillis()/1000;
		
		this.transaccions=new HashMap<Long, InnoDBTransaction>();
		
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
		if(titol!=null && titol.contains("TRANS"))
		{
			System.out.println("####################Transaccions: "+dades);
			
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
	            	this.transaccions.put(new Long(idbt.getMySQLThreadID()), idbt);
	            }
	            
	            //WTF? this.getThreadInfo(titolseccio, dataseccio);
			}
			
			idbt=new InnoDBTransaction(dades.subSequence(lastEnd, dades.length()).toString());
			this.transaccions.put(new Long(idbt.getMySQLThreadID()), idbt);
			
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


//SLAVE ( jobisjob ) > show engine innodb status\G
//*************************** 1. row ***************************
//  Type: InnoDB
//  Name: 
//Status: 
//=====================================
//121005  8:46:16 INNODB MONITOR OUTPUT
//=====================================
//Per second averages calculated from the last 4 seconds
//----------
//SEMAPHORES
//----------
//OS WAIT ARRAY INFO: reservation count 269711023, signal count 219868573
//--Thread 1179105600 has waited at ../../storage/innobase/include/btr0btr.ic line 28 for 3.00 seconds the semaphore:
//S-lock on RW-latch at 0x2aac38299a90 created in file buf/buf0buf.c line 547
//a writer (thread id 1191147840) has reserved it in mode  exclusive
//number of readers 0, waiters flag 1
//Last time read locked in file btr/btr0pcur.c line 249
//Last time write locked in file buf/buf0buf.c line 1797
//Mutex spin waits 0, rounds 4780628438, OS waits 28413956
//RW-shared spins 455883650, OS waits 221060531; RW-excl spins 21363149, OS waits 12813645
//------------
//TRANSACTIONS
//------------
//Trx id counter 2 2573186583
//Purge done for trx's n:o < 2 2573138805 undo n:o < 0 0
//History list length 21860
//LIST OF TRANSACTIONS FOR EACH SESSION:
//---TRANSACTION 0 0, not started, process no 745, OS thread id 1187133760
//MySQL thread id 2664411, query id 7121412548 localhost root
//show engine innodb status
//---TRANSACTION 2 2573186577, not started, process no 745, OS thread id 1188567360
//MySQL thread id 2664415, query id 7121412324 drake 10.12.50.67 jobisjob_solr
//---TRANSACTION 2 2573186578, not started, process no 745, OS thread id 1181399360
//MySQL thread id 2663543, query id 7121412332 drake 10.12.50.67 jobisjob_solr
//---TRANSACTION 2 2573186569, not started, process no 745, OS thread id 1172797760
//MySQL thread id 2257814, query id 7121412249 Table lock
//insert into sic_site_impressions_clicks (sit_id, sic_date, sic_pay, sic_impressions) values( 928, now(), 0, 3) on duplicate key update sic_impressions=sic_impressions+3
//---TRANSACTION 2 2573186582, ACTIVE 15 sec, process no 745, OS thread id 1179965760
//mysql tables in use 1, locked 0
//MySQL thread id 2664418, query id 7121412476 drake 10.12.50.67 jobisjob_solr Table lock
//select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit left join sic_site_impressions_clicks sic using (sit_id)  where sic_date > (now() - interval 14 day)   and cou_id = 1 group by sit_id
//---TRANSACTION 2 2573186573, ACTIVE 35 sec, process no 745, OS thread id 1185413440
//mysql tables in use 1, locked 0
//MySQL thread id 2664414, query id 7121412283 drake 10.12.50.67 jobisjob_solr Table lock
//select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit left join sic_site_impressions_clicks sic using (sit_id)  where sic_date > (now() - interval 14 day)   and cou_id = 13 group by sit_id
//---TRANSACTION 2 2573186507, ACTIVE 55 sec, process no 745, OS thread id 1178245440, thread declared inside InnoDB 499
//mysql tables in use 1, locked 0
//MySQL thread id 2664416, query id 7121412185 drake 10.12.50.67 jobisjob_solr Sending data
//select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit left join sic_site_impressions_clicks sic using (sit_id)  where sic_date > (now() - interval 14 day)   and cou_id = 5 group by sit_id
//Trx read view will not see trx with id >= 2 2573186508, sees < 2 2573138803
//---TRANSACTION 2 2573141666, ACTIVE 4035 sec, process no 745, OS thread id 1179105600 fetching rows, thread declared inside InnoDB 421
//mysql tables in use 2, locked 0
//MySQL thread id 2663678, query id 7121330592 drake 10.12.50.67 jobisjob_solr Sending data
//select off.sit_id, off.off_id, off.sit_id, off.off_url, off.off_descr, off.off_title, off.off_salary, off.off_company,                  off.off_job_type, off.off_source_location, emp_id, off_custom2,              UNIX_TIMESTAMP(off_date) as timestamp, date_format(off_date_insert, '%Y-%m-%d') as off_date_insert,                 date_format(off_date, '%Y-%m-%dT%H:%i:%sZ') as date_posted, CHAR_LENGTH(off_descr) as off_descr_length              from off_offer off join sit_site sit using (sit_id)              where                 sit.cou_id = 7 and                 off.off_state = 0
//Trx read view will not see trx with id >= 2 2573141667, sees < 2 2573121368
//---TRANSACTION 2 2573138803, ACTIVE 4935 sec, process no 745, OS thread id 1191147840 fetching rows, thread declared inside InnoDB 457
//mysql tables in use 2, locked 0
//MySQL thread id 2663542, query id 7121317118 drake 10.12.50.67 jobisjob_solr Sending data
//select off.sit_id, off.off_id, off.sit_id, off.off_url, off.off_descr, off.off_title, off.off_salary, off.off_company,                  off.off_job_type, off.off_source_location, emp_id, off_custom2,              UNIX_TIMESTAMP(off_date) as timestamp, date_format(off_date_insert, '%Y-%m-%d') as off_date_insert,                 date_format(off_date, '%Y-%m-%dT%H:%i:%sZ') as date_posted, CHAR_LENGTH(off_descr) as off_descr_length              from off_offer off join sit_site sit using (sit_id)              where                 sit.cou_id = 3 and                 off.off_state = 0
//Trx read view will not see trx with id >= 2 2573138804, sees < 2 2573098434
//--------
//FILE I/O
//--------
//I/O thread 0 state: waiting for i/o request (insert buffer thread)
//I/O thread 1 state: waiting for i/o request (log thread)
//I/O thread 2 state: waiting for i/o request (read thread)
//I/O thread 3 state: doing file i/o (write thread) ev set
//Pending normal aio reads: 0, aio writes: 78,
// ibuf aio reads: 0, log i/o's: 0, sync i/o's: 0
//Pending flushes (fsync) log: 0; buffer pool: 0
//570296101 OS file reads, 1466008745 OS file writes, 1087014359 OS fsyncs
//1 pending preads, 1 pending pwrites
//4.00 reads/s, 65536 avg bytes/read, 4.75 writes/s, 0.00 fsyncs/s
//-------------------------------------
//INSERT BUFFER AND ADAPTIVE HASH INDEX
//-------------------------------------
//Ibuf: size 1, free list len 11238, seg size 11240,
//16675769 inserts, 16675769 merged recs, 6457966 merges
//Hash table size 10624987, node heap has 16 buffer(s)
//10.50 hash searches/s, 0.00 non-hash searches/s
//---
//LOG
//---
//Log sequence number 908 60222556
//Log flushed up to   908 60222556
//Last checkpoint at  908 54523834
//0 pending log writes, 0 pending chkp writes
//1068971138 log i/o's done, 0.00 log i/o's/second
//----------------------
//BUFFER POOL AND MEMORY
//----------------------
//Total memory allocated 5907399978; in additional pool allocated 1048576
//Dictionary memory allocated 367104
//Buffer pool size   327680
//Free buffers       0
//Database pages     327664
//Modified db pages  1353
//Pending reads 1
//Pending writes: LRU 0, flush list 78, single page 0
//Pages read 1229781967, created 12066478, written 516577822
//16.00 reads/s, 0.00 creates/s, 5.00 writes/s
//Buffer pool hit rate 900 / 1000
//--------------
//ROW OPERATIONS
//--------------
//3 queries inside InnoDB, 0 queries in queue
//4 read views open inside InnoDB
//Main thread process no. 745, id 1171650880, state: flushing buffer pool pages
//Number of rows inserted 80753689, updated 957878121, deleted 51305581, read 673982612246
//0.00 inserts/s, 0.00 updates/s, 0.00 deletes/s, 106.47 reads/s
//----------------------------
//END OF INNODB MONITOR OUTPUT
//============================
//
//1 row in set (0.00 sec)
//
// SLAVE ( jobisjob ) > 

//SLAVE ( jobisjob ) > show processlist;
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+------------------------------------------------------------------------------------------------------+
//| Id      | User          | Host        | db       | Command | Time     | State                            | Info                                                                                                 |
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+------------------------------------------------------------------------------------------------------+
//|      13 | system user   |             | NULL     | Connect | 17602883 | Waiting for master to send event | NULL                                                                                                 |
//| 2257814 | system user   |             | NULL     | Connect |    13217 | Reading event from the relay log | NULL                                                                                                 |
//| 2663542 | jobisjob_solr | drake:59859 | jobisjob | Query   |     5048 | Sending data                     | select off.sit_id, off.off_id, off.sit_id, off.off_url, off.off_descr, off.off_title, off.off_salary |
//| 2663543 | jobisjob_solr | drake:59860 | jobisjob | Sleep   |       31 |                                  | NULL                                                                                                 |
//| 2663678 | jobisjob_solr | drake:56426 | jobisjob | Query   |     4148 | Sending data                     | select off.sit_id, off.off_id, off.sit_id, off.off_url, off.off_descr, off.off_title, off.off_salary |
//| 2664411 | root          | localhost   | jobisjob | Query   |        0 | NULL                             | show processlist                                                                                     |
//| 2664414 | jobisjob_solr | drake:52687 | jobisjob | Sleep   |       31 |                                  | NULL                                                                                                 |
//| 2664415 | jobisjob_solr | drake:52688 | jobisjob | Sleep   |       31 |                                  | NULL                                                                                                 |
//| 2664416 | jobisjob_solr | drake:52689 | jobisjob | Sleep   |        8 |                                  | NULL                                                                                                 |
//| 2664436 | jobisjob_solr | drake:33442 | jobisjob | Sleep   |       31 |                                  | NULL                                                                                                 |
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+------------------------------------------------------------------------------------------------------+
//10 rows in set (0.00 sec)
//
// SLAVE ( jobisjob ) > 

//SLAVE ( jobisjob ) > show processlist;
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+------------------------------------------------------------------------------------------------------+
//| Id      | User          | Host        | db       | Command | Time     | State                            | Info                                                                                                 |
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+------------------------------------------------------------------------------------------------------+
//|      13 | system user   |             | NULL     | Connect | 17603827 | Waiting for master to send event | NULL                                                                                                 |
//| 2257814 | system user   |             | jobisjob | Connect |    13790 | Waiting for release of readlock  | UPDATE sit_site SET sit_last_index_time='2012-10-05 05:13:33' WHERE sit_id=1222                      |
//| 2663542 | jobisjob_solr | drake:59859 | jobisjob | Query   |     5992 | Sending data                     | select off.sit_id, off.off_id, off.sit_id, off.off_url, off.off_descr, off.off_title, off.off_salary |
//| 2663678 | jobisjob_solr | drake:56426 | jobisjob | Query   |     5092 | Sending data                     | select off.sit_id, off.off_id, off.sit_id, off.off_url, off.off_descr, off.off_title, off.off_salary |
//| 2664411 | root          | localhost   | jobisjob | Query   |        0 | NULL                             | show processlist                                                                                     |
//| 2664414 | jobisjob_solr | drake:52687 | jobisjob | Query   |      212 | Waiting for table                | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 5     |
//| 2664416 | jobisjob_solr | drake:52689 | jobisjob | Query   |      193 | Waiting for table                | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 13    |
//| 2664528 | root          | localhost   | mysql    | Query   |      323 | Flushing tables                  | FLUSH TABLES WITH READ LOCK                                                                          |
//| 2664554 | jobisjob_solr | drake:58355 | jobisjob | Query   |      173 | Waiting for table                | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 1     |
//| 2664562 | jobisjob_solr | drake:58413 | jobisjob | Query   |      153 | Waiting for table                | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 4     |
//| 2664565 | jobisjob_solr | drake:58455 | jobisjob | Query   |      133 | Waiting for table                | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 9     |
//| 2664568 | jobisjob_solr | drake:58496 | jobisjob | Query   |      113 | Waiting for table                | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 2     |
//| 2664573 | jobisjob_solr | drake:58552 | jobisjob | Query   |       93 | Waiting for table                | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 11    |
//| 2664582 | jobisjob_solr | drake:58591 | jobisjob | Query   |       73 | Waiting for table                | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 10    |
//| 2664585 | jobisjob_solr | drake:58685 | jobisjob | Query   |       33 | Waiting for table                | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 12    |
//| 2664586 | jobisjob_solr | drake:58724 | jobisjob | Query   |       13 | Waiting for table                | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 8     |
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+------------------------------------------------------------------------------------------------------+
//16 rows in set (0.00 sec)
//
// SLAVE ( jobisjob ) > 



//SLAVE ( jobisjob ) > show processlist;
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+------------------------------------------------------------------------------------------------------+
//| Id      | User          | Host        | db       | Command | Time     | State                            | Info                                                                                                 |
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+------------------------------------------------------------------------------------------------------+
//|      13 | system user   |             | NULL     | Connect | 17603899 | Waiting for master to send event | NULL                                                                                                 |
//| 2257814 | system user   |             | jobisjob | Connect |    13861 | Locked                           | insert into sic_site_impressions_clicks (sit_id, sic_date, sic_pay, sic_impressions) values( 212, no |
//| 2663542 | jobisjob_solr | drake:59859 | jobisjob | Sleep   |        4 |                                  | NULL                                                                                                 |
//| 2663678 | jobisjob_solr | drake:56426 | jobisjob | Sleep   |        4 |                                  | NULL                                                                                                 |
//| 2664411 | root          | localhost   | jobisjob | Query   |        0 | NULL                             | show processlist                                                                                     |
//| 2664414 | jobisjob_solr | drake:52687 | jobisjob | Query   |       19 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664416 | jobisjob_solr | drake:52689 | jobisjob | Query   |       19 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664554 | jobisjob_solr | drake:58355 | jobisjob | Query   |       20 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664562 | jobisjob_solr | drake:58413 | jobisjob | Query   |      225 | Sending data                     | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 4     |
//| 2664565 | jobisjob_solr | drake:58455 | jobisjob | Query   |       20 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664568 | jobisjob_solr | drake:58496 | jobisjob | Query   |       20 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664573 | jobisjob_solr | drake:58552 | jobisjob | Query   |       20 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664582 | jobisjob_solr | drake:58591 | jobisjob | Query   |       19 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664585 | jobisjob_solr | drake:58685 | jobisjob | Query   |      105 | Sending data                     | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 12    |
//| 2664586 | jobisjob_solr | drake:58724 | jobisjob | Query   |       19 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664587 | jobisjob_solr | drake:58762 | jobisjob | Query   |       19 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664590 | jobisjob_solr | drake:58823 | jobisjob | Query   |       19 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664591 | jobisjob_solr | drake:58862 | jobisjob | Query   |       19 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit lef |
//| 2664592 | jobisjob_solr | drake:58879 | jobisjob | Sleep   |        4 |                                  | NULL                                                                                                 |
//| 2664593 | jobisjob_solr | drake:58881 | jobisjob | Query   |        5 | Sending data                     | select sit_id, sit_name, sit_boost_custom, sit_posted_offers from sit_site sit  where cou_id = 17    |
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+------------------------------------------------------------------------------------------------------+
//20 rows in set (0.00 sec)
//
// SLAVE ( jobisjob ) > 




//SLAVE ( jobisjob ) > show full processlist;
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
//| Id      | User          | Host        | db       | Command | Time     | State                            | Info                                                                                                                                                                                                                                   |
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
//|      13 | system user   |             | NULL     | Connect | 17603922 | Waiting for master to send event | NULL                                                                                                                                                                                                                                   |
//| 2257814 | system user   |             | jobisjob | Connect |    13883 | Locked                           | insert into sic_site_impressions_clicks (sit_id, sic_date, sic_pay, sic_impressions) values( 1187, now(), 0, 12) on duplicate key update sic_impressions=sic_impressions+12                                                            |
//| 2663542 | jobisjob_solr | drake:59859 | jobisjob | Sleep   |        6 |                                  | NULL                                                                                                                                                                                                                                   |
//| 2663678 | jobisjob_solr | drake:56426 | jobisjob | Sleep   |        0 |                                  | NULL                                                                                                                                                                                                                                   |
//| 2664411 | root          | localhost   | jobisjob | Query   |        0 | NULL                             | show full processlist                                                                                                                                                                                                                  |
//| 2664414 | jobisjob_solr | drake:52687 | jobisjob | Sleep   |        0 |                                  | NULL                                                                                                                                                                                                                                   |
//| 2664562 | jobisjob_solr | drake:58413 | jobisjob | Query   |        7 | Sending data                     | select sit_id, sum(sic_impressions) as impressions, sum(sic_clicks) as clicks  from sit_site sit left join sic_site_impressions_clicks sic using (sit_id)  where sic_date > (now() - interval 14 day)   and cou_id = 4 group by sit_id |
//| 2664582 | jobisjob_solr | drake:58591 | jobisjob | Sleep   |        1 |                                  | NULL                                                                                                                                                                                                                                   |
//| 2664586 | jobisjob_solr | drake:58724 | jobisjob | Sleep   |        0 |                                  | NULL                                                                                                                                                                                                                                   |
//| 2664587 | jobisjob_solr | drake:58762 | jobisjob | Sleep   |        0 |                                  | NULL                                                                                                                                                                                                                                   |
//| 2664590 | jobisjob_solr | drake:58823 | jobisjob | Sleep   |        1 |                                  | NULL                                                                                                                                                                                                                                   |
//| 2664592 | jobisjob_solr | drake:58879 | jobisjob | Query   |        0 | statistics                       | select soo_sponsored from soo_sponsored_offer_order where off_id = 193186249                                                                                                                                                           |
//| 2664593 | jobisjob_solr | drake:58881 | jobisjob | Sleep   |        0 |                                  | NULL                                                                                                                                                                                                                                   |
//+---------+---------------+-------------+----------+---------+----------+----------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
//13 rows in set (0.00 sec)
//
// SLAVE ( jobisjob ) > 



//  =====================================
//	121010 10:37:14 INNODB MONITOR OUTPUT
//	=====================================
//	Per second averages calculated from the last 48 seconds
//	----------
//	SEMAPHORES
//	----------
//	OS WAIT ARRAY INFO: reservation count 93159071, signal count 59435388
//	Mutex spin waits 0, rounds 6610760215, OS waits 14763220
//	RW-shared spins 166339241, OS waits 70876277; RW-excl spins 181227055, OS waits 3516120
//	------------------------
//	LATEST DETECTED DEADLOCK
//	------------------------
//	121008 13:08:25
//	*** (1) TRANSACTION:
//	TRANSACTION 6 1096615036, ACTIVE 0 sec, process no 31146, OS thread id 1103485248 inserting
//	mysql tables in use 1, locked 1
//	LOCK WAIT 3 lock struct(s), heap size 1216, 2 row lock(s)
//	MySQL thread id 212113998, query id 5989914300 10.12.64.5 ivoox update
//	INSERT INTO playlist VALUES ('3912579','','1472804','314643',1)
//	*** (1) WAITING FOR THIS LOCK TO BE GRANTED:
//	RECORD LOCKS space id 5509 page no 4718 n bits 528 index `PRIMARY` of table `ivoox`.`playlist` trx id 6 1096615036 lock_mode X locks rec but not gap waiting
//	Record lock, heap no 329 PHYSICAL RECORD: n_fields 7; compact format; info bits 32
//	 0: len 4; hex 803bb383; asc  ;  ;; 1: len 6; hex 0006415cfefa; asc   A\  ;; 2: len 7; hex 00000000db0110; asc        ;; 3: len 0; hex ; asc ;; 4: len 4; hex 80167924; asc   y$;; 5: len 4; hex 8004cd13; asc     ;; 6: len 1; hex 81; asc  ;;
//
//	*** (2) TRANSACTION:
//	TRANSACTION 6 1096615037, ACTIVE 0 sec, process no 31146, OS thread id 1104017728 inserting, thread declared inside InnoDB 1
//	mysql tables in use 1, locked 1
//	3 lock struct(s), heap size 1216, 2 row lock(s)
//	MySQL thread id 212114013, query id 5989914301 10.12.64.5 ivoox update
//	INSERT INTO playlist VALUES ('3912579','','1472804','314643',1)
//	*** (2) HOLDS THE LOCK(S):
//	RECORD LOCKS space id 5509 page no 4718 n bits 528 index `PRIMARY` of table `ivoox`.`playlist` trx id 6 1096615037 lock mode S locks rec but not gap
//	Record lock, heap no 329 PHYSICAL RECORD: n_fields 7; compact format; info bits 32
//	 0: len 4; hex 803bb383; asc  ;  ;; 1: len 6; hex 0006415cfefa; asc   A\  ;; 2: len 7; hex 00000000db0110; asc        ;; 3: len 0; hex ; asc ;; 4: len 4; hex 80167924; asc   y$;; 5: len 4; hex 8004cd13; asc     ;; 6: len 1; hex 81; asc  ;;
//
//	*** (2) WAITING FOR THIS LOCK TO BE GRANTED:
//	RECORD LOCKS space id 5509 page no 4718 n bits 528 index `PRIMARY` of table `ivoox`.`playlist` trx id 6 1096615037 lock_mode X locks rec but not gap waiting
//	Record lock, heap no 329 PHYSICAL RECORD: n_fields 7; compact format; info bits 32
//	 0: len 4; hex 803bb383; asc  ;  ;; 1: len 6; hex 0006415cfefa; asc   A\  ;; 2: len 7; hex 00000000db0110; asc        ;; 3: len 0; hex ; asc ;; 4: len 4; hex 80167924; asc   y$;; 5: len 4; hex 8004cd13; asc     ;; 6: len 1; hex 81; asc  ;;
//
//	*** WE ROLL BACK TRANSACTION (2)
//	------------
//	TRANSACTIONS
//	------------
//	Trx id counter 6 1112545234
//	Purge done for trx's n:o < 6 1112545190 undo n:o < 0 0
//	History list length 15
//	LIST OF TRANSACTIONS FOR EACH SESSION:
//	---TRANSACTION 0 0, not started, process no 31146, OS thread id 1211377984
//	MySQL thread id 214206613, query id 6040703665 localhost root
//	show engine innodb status
//	---TRANSACTION 6 1112544068, not started, process no 31146, OS thread id 1088477504
//	MySQL thread id 214205914, query id 6040698147 10.12.10.102 ivoox_cron
//	---TRANSACTION 6 1112545230, not started, process no 31146, OS thread id 1187948864
//	MySQL thread id 214205264, query id 6040703650 10.12.10.102 ivoox_cron
//	--------
//	FILE I/O
//	--------
//	I/O thread 0 state: waiting for i/o request (insert buffer thread)
//	I/O thread 1 state: waiting for i/o request (log thread)
//	I/O thread 2 state: waiting for i/o request (read thread)
//	I/O thread 3 state: waiting for i/o request (write thread)
//	Pending normal aio reads: 0, aio writes: 0,
//	 ibuf aio reads: 0, log i/o's: 0, sync i/o's: 0
//	Pending flushes (fsync) log: 0; buffer pool: 0
//	20935731 OS file reads, 329683653 OS file writes, 259889386 OS fsyncs
//	0.02 reads/s, 16384 avg bytes/read, 24.62 writes/s, 19.54 fsyncs/s
//	-------------------------------------
//	INSERT BUFFER AND ADAPTIVE HASH INDEX
//	-------------------------------------
//	Ibuf: size 1, free list len 828, seg size 830,
//	2120442 inserts, 2120442 merged recs, 505224 merges
//	Hash table size 12451141, node heap has 28394 buffer(s)
//	92847.98 hash searches/s, 1218.50 non-hash searches/s
//	---
//	LOG
//	---
//	Log sequence number 553 4128497310
//	Log flushed up to   553 4128497310
//	Last checkpoint at  553 4127936141
//	0 pending log writes, 0 pending chkp writes
//	251241480 log i/o's done, 18.35 log i/o's/second
//	----------------------
//	BUFFER POOL AND MEMORY
//	----------------------
//	Total memory allocated 6916535098; in additional pool allocated 1048576
//	Dictionary memory allocated 429024
//	Buffer pool size   384000
//	Free buffers       246
//	Database pages     355360
//	Modified db pages  373
//	Pending reads 0
//	Pending writes: LRU 0, flush list 0, single page 0
//	Pages read 80261512, created 16793428, written 152555395
//	0.02 reads/s, 0.02 creates/s, 7.58 writes/s
//	Buffer pool hit rate 1000 / 1000
//	--------------
//	ROW OPERATIONS
//	--------------
//	0 queries inside InnoDB, 0 queries in queue
//	1 read views open inside InnoDB
//	Main thread process no. 31146, id 1180494144, state: sleeping
//	Number of rows inserted 2741688548, updated 195399631, deleted 56537106, read 767651653341
//	5.25 inserts/s, 3.54 updates/s, 1.06 deletes/s, 286042.56 reads/s
//	----------------------------
//	END OF INNODB MONITOR OUTPUT
//	============================
//
//	1 row in set (0.00 sec)
//
//	mysql> select version();
//	+------------+
//	| version()  |
//	+------------+
//	| 5.1.43-log |
//	+------------+
//	1 row in set (0.00 sec)
//
//	mysql> 

