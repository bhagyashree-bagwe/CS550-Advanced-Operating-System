package evaluation;

import peer.PeerServer;
import server.IndexingServerImpl;

public class EvaluationUtility {
public static void startServers()
{
	/*Starting 8 instances of indexing servers on registration port*/
	Thread t1 = new Thread (new IndexingServerImpl("2001", "s1"));                     
	t1.start();
	Thread t2 = new Thread (new IndexingServerImpl("2003", "s2"));                     
	t2.start();
	Thread t3 = new Thread (new IndexingServerImpl("2005", "s3"));                     
	t3.start();
	Thread t4 = new Thread (new IndexingServerImpl("2007", "s4"));                     
	t4.start();
	Thread t5 = new Thread (new IndexingServerImpl("2009", "s5"));                     
	t5.start();
	Thread t6 = new Thread (new IndexingServerImpl("2011", "s6"));                     
	t6.start();
	Thread t7 = new Thread (new IndexingServerImpl("2013", "s7"));                     
	t7.start();
	Thread t8 = new Thread (new IndexingServerImpl("2015", "s8"));                     
	t8.start();
	
	/*Starting 8 instances of indexing servers on search port*/
	Thread t9 = new Thread (new IndexingServerImpl("2002", "s1"));                     
	t9.start();
	Thread t10 = new Thread (new IndexingServerImpl("2004", "s2"));                     
	t10.start();
	Thread t11 = new Thread (new IndexingServerImpl("2006", "s3"));                     
	t11.start();
	Thread t12 = new Thread (new IndexingServerImpl("2008", "s4"));                     
	t12.start();
	Thread t13 = new Thread (new IndexingServerImpl("2010", "s5"));                     
	t13.start();
	Thread t14 = new Thread (new IndexingServerImpl("2012", "s6"));                     
	t14.start();
	Thread t15 = new Thread (new IndexingServerImpl("2014", "s7"));                     
	t15.start();
	Thread t16 = new Thread (new IndexingServerImpl("2016", "s8"));                     
	t16.start();
	
	/*Starting peer instance on Download port*/
	Thread t17 = new Thread (new PeerServer(2000));                    
	t17.start();
		
}
}
