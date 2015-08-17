package willy.server.sql.mysql.engines;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import willy.server.regex.RegexParser;

public class InnoDBTransaction {
	
	protected long mysql_thread_id=0;
	public long getMySQLThreadID() { return this.mysql_thread_id; }
	
	protected boolean locked=false;
	public boolean isLocked() { return this.locked; }
	
	protected String strorig=null;
	public String toString() { return this.strorig; }
	
	public InnoDBTransaction(String str)
	{
		RegexParser regexp=null;
		this.strorig=str;
		
//		0 0, not started, process no 8712, OS thread id 1199040832
//		*MySQL thread id 155073150*, query id 4702572362 172.20.0.100 testro
//		show engine innodb status
		
		//insert sense commit
//		---TRANSACTION 0 1221801, ACTIVE 12 sec, process no 8712, OS thread id 1204898112
//		1 lock struct(s), heap size 368, 0 row lock(s), undo log entries 1
//		MySQL thread id 155076224, query id 4702601887 localhost root
//		show innodb status

		//bloquejada
//		---TRANSACTION 0 1221803, ACTIVE 15 sec, process no 8712, OS thread id 1211287872 starting index read
//		mysql tables in use 1, locked 1
//		LOCK WAIT 2 lock struct(s), heap size 368, 1 row lock(s)
//		MySQL thread id 155076965, query id 4702606117 localhost root Updating
//		update innodb set y=4 where x=2
//		------- TRX HAS BEEN WAITING 15 SEC FOR THIS LOCK TO BE GRANTED:
//		RECORD LOCKS space id 2052 page no 3 n bits 72 index `GEN_CLUST_INDEX` of table `systemadmin`.`innodb` trx id 0 1221803 lock_mode X waiting
//		Record lock, heap no 2 PHYSICAL RECORD: n_fields 5; compact format; info bits 0
//		 0: len 6; hex 000000000200; asc       ;; 1: len 6; hex 00000012957c; asc      |;; 2: len 7; hex 800000002d0110; asc     -  ;; 3: len 4; hex 80000001; asc     ;; 4: len 4; hex 80000002; asc     ;;
//
		//bloquejant
//		------------------
//		---TRANSACTION 0 1221802, ACTIVE 56 sec, process no 8712, OS thread id 1204898112
//		2 lock struct(s), heap size 368, 3 row lock(s), undo log entries 1
//		MySQL thread id 155076224, query id 4702606228 localhost root
//		Trx read view will not see trx with id >= 0 1221803, sees < 0 1221803	
		
		String pattern = "MySQL thread id (\\d+),";
		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(str);
		if (m.find()) 
		{
			//System.out.println("Found value: " + m.group(1) );
			this.mysql_thread_id=Long.parseLong(m.group(1));
			System.out.println("@THREADID: "+this.mysql_thread_id);
		}
		
		//TODO: TRX HAS BEEN WAITING \\d+ SEC FOR THIS LOCK TO BE GRANTED
		regexp=new RegexParser(str,"TRX HAS BEEN WAITING (\\d+) SEC FOR THIS LOCK TO BE GRANTED");
		System.out.println("@TX WAITING: "+regexp.getMatch(1));
		
		//TODO: ACTIVE (\\d+) sec
		regexp=new RegexParser(str,"ACTIVE (\\d+) sec");
		System.out.println("@ACTIVE: "+regexp.getMatch(1));
		
		//TODO: (\\d+) lock struct(s), heap size 368, (\\d+) row lock(s), undo log entries (\\d+)
		//si tinc això estic bloquejant
		regexp=new RegexParser(str,"(\\d+) lock struct\\(s\\), heap size 368, (\\d+) row lock\\(s\\), undo log entries (\\d+)");
		System.out.println("@LOCK STRUCT: "+regexp.getMatch(1));
		System.out.println("@ROW LOCKS: "+regexp.getMatch(2));
		System.out.println("@UNDO ENTRIES: "+regexp.getMatch(3));
		
		//TODO: of table `systemadmin`.`innodb` trx id
		//      of table (`[^`]+`.`[^`]+`) trx id
		//si tinc això estic bloquejat
		regexp=new RegexParser(str,"of table (`[^`]+`.`[^`]+`) trx id");
		System.out.println("@DB I TAULA: "+regexp.getMatch(1));
		
	}
	
	//TODO: parsejar els threads de show engine innodb status;
	//thread que no esta fent el commit
//	---TRANSACTION 0 1217916, ACTIVE 13 sec, process no 8712, OS thread id 1196910912
//	1 lock struct(s), heap size 368, 0 row lock(s), undo log entries 1
//	MySQL thread id 154252715, query id 4670768942 localhost root
	//consta com a sleep:
//| 154252715 | root     | localhost | systemadmin   | Sleep   |   29 |        | NULL        

	
//	mysql> show processlist;
//	+-----------+------+-----------+-------------+---------+------+----------+---------------------------------+
//	| Id        | User | Host      | db          | Command | Time | State    | Info                            |
//	+-----------+------+-----------+-------------+---------+------+----------+---------------------------------+
//	| 154252715 | root | localhost | systemadmin | Sleep   |    6 |          | NULL                            |
//	| 154253328 | root | localhost | NULL        | Query   |    0 | NULL     | show processlist                |
//	| 154253895 | root | localhost | systemadmin | Query   |    4 | Updating | update innodb set y=4 where y=1 |
//	+-----------+------+-----------+-------------+---------+------+----------+---------------------------------+
	//154252715 <-- amb commit pendent
	//154253895 <-- locked a espera de l'altre
	
//	------------
//	TRANSACTIONS
//	------------
//	Trx id counter 0 1217921
//	Purge done for trx's n:o < 0 1213521 undo n:o < 0 0
//	History list length 17
//	LIST OF TRANSACTIONS FOR EACH SESSION:
//	---TRANSACTION 0 0, not started, process no 8712, OS thread id 1202768192
//	MySQL thread id 154253328, query id 4671011839 localhost root
//	show innodb status
//	---TRANSACTION 0 1217920, ACTIVE 2 sec, process no 8712, OS thread id 1196378432 starting index read
//	mysql tables in use 1, locked 1
//	LOCK WAIT 2 lock struct(s), heap size 368, 1 row lock(s)
//	MySQL thread id 154253895, query id 4671011709 localhost root Updating
//	update innodb set y=4 where y=1
//	------- TRX HAS BEEN WAITING 2 SEC FOR THIS LOCK TO BE GRANTED:
//	RECORD LOCKS space id 2052 page no 3 n bits 72 index `GEN_CLUST_INDEX` of table `systemadmin`.`innodb` trx id 0 1217920 lock_mode X waiting
//	Record lock, heap no 2 PHYSICAL RECORD: n_fields 5; compact format; info bits 0
//	 0: len 6; hex 000000000200; asc       ;; 1: len 6; hex 00000012957f; asc       ;; 2: len 7; hex 000000003229ee; asc     2) ;; 3: len 4; hex 80000001; asc     ;; 4: len 4; hex 80000003; asc     ;;
//
//	------------------
//	---TRANSACTION 0 1217919, ACTIVE 4 sec, process no 8712, OS thread id 1196910912
//	2 lock struct(s), heap size 368, 2 row lock(s), undo log entries 1
//	MySQL thread id 154252715, query id 4671011676 localhost root

	//Quan caduca:
	
//	------------
//	TRANSACTIONS
//	------------
//	Trx id counter 0 1217931
//	Purge done for trx's n:o < 0 1213521 undo n:o < 0 0
//	History list length 17
//	LIST OF TRANSACTIONS FOR EACH SESSION:
//	---TRANSACTION 0 1217920, not started, process no 8712, OS thread id 1196378432
//	MySQL thread id 154253895, query id 4671011709 localhost root
//	---TRANSACTION 0 0, not started, process no 8712, OS thread id 1202768192
//	MySQL thread id 154253328, query id 4671485367 localhost root
//	show innodb status
//	---TRANSACTION 0 1217919, ACTIVE 312 sec, process no 8712, OS thread id 1196910912
//	2 lock struct(s), heap size 368, 2 row lock(s), undo log entries 1
//	MySQL thread id 154252715, query id 4671011676 localhost root

	
	
	
}
