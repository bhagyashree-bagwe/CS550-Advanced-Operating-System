package evaluation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import POJO.PeerInformation;
/*
Author: Bhagyashree Bagwe
Date: 	09/24/2017
*/
public class PerformaceEvaluationImpl extends Thread {

		private String CIS_HOSTNAME;
		private String fileName;
		private int port;
		private int SEARCH_TEST_COUNT;
		private int DOWNLOAD_TEST_COUNT;
		ObjectInputStream objectInputSTream;
		ObjectOutputStream objectOutputStream;
		Socket connection;
		ArrayList<PeerInformation> searchResultList = new ArrayList<PeerInformation>();
		
		public PerformaceEvaluationImpl(String host, String file, int port, int SEARCH_TEST_COUNT) {
			this.CIS_HOSTNAME = host;
			this.fileName = file;
			this.port = port;
			this.SEARCH_TEST_COUNT = SEARCH_TEST_COUNT;
		}
		
		public void run() 
		{
			System.out.println("New thread started!");
			long lookupStartTime, lookEndTime, lookupTotalTime = 0;
			long downloadStartTime, downloadEndTime, downloadTotalTime = 0;
			double avgLookTime, avgDownloadTime;
			if(port == 2001)
			{
				try 
				{
					for(int i=0;i<SEARCH_TEST_COUNT;i++)
					{
						lookupStartTime = System.currentTimeMillis();
						search();
						lookEndTime = System.currentTimeMillis();
						lookupTotalTime += (lookEndTime - lookupStartTime);
					}
					avgLookTime = (double) Math.round(lookupTotalTime / (double) SEARCH_TEST_COUNT) / 1000;
					System.out.println("AVG LookUp Time : "+avgLookTime+" Sec\nNo of Request/Thread : "+SEARCH_TEST_COUNT+"\nTotal Lookup time : "+lookupTotalTime);
				}
				catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			if(port == 2000)
			{
				try 
				{
					for(int i=0;i<DOWNLOAD_TEST_COUNT;i++)
					{
						downloadStartTime = System.currentTimeMillis();
						DownloadFromPeer();
						downloadEndTime = System.currentTimeMillis();
						downloadTotalTime += (downloadEndTime - downloadStartTime);
					}
					avgDownloadTime = (double) Math.round(downloadTotalTime / (double) DOWNLOAD_TEST_COUNT) / 1000;
					System.out.println("AVG Download Time : "+avgDownloadTime+" Sec\nNo of Request/Thread : "+DOWNLOAD_TEST_COUNT+"\nTotal Download time : "+downloadTotalTime);
				}
				catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		
public void search() throws UnknownHostException, IOException, ClassNotFoundException
{
	connection = new Socket(CIS_HOSTNAME, 2001);
	System.out.println("\nConnected to Search with CIS on port 2003\n");
	//System.out.println("\nWhich file you want to search? \n");
	String searchFileName = fileName;
	objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
	objectOutputStream.flush();			
	objectOutputStream.writeObject(searchFileName);
	objectOutputStream.flush();
	objectInputSTream = new ObjectInputStream(connection.getInputStream());
	ArrayList<PeerInformation> searchResultList = (ArrayList<PeerInformation>) objectInputSTream.readObject();
	Iterator it = searchResultList.iterator();
	
	if(searchResultList.isEmpty())
	{
		System.out.println("\n This file is not registered with any peer! \n");
		System.out.println("\n Please check the spelling of filename or try again with different name!\n");
	}
	else
	{
		System.out.println("Above file is found on following peers:");
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("PeerId\t|Hostname\t\t|Location\t\t\t|FileName");
		System.out.println("-----------------------------------------------------------------------------------");
		while(it.hasNext())
		{
			PeerInformation peer = (PeerInformation)it.next();
			System.out.println(peer.getPeerID()+"\t|"+peer.getPeerHostname()+"\t|"+peer.getFilePath()+"\t\t|"+searchFileName);
	
	
		} 
	}
}


public void DownloadFromPeer()
{
	byte[] byteArray=new byte[1024*64];
	try 
	{
		connection = new Socket(CIS_HOSTNAME, 2000);
		String filePathAndName = fileName;
		String[] str = filePathAndName.split(" ");
		String fileName = str[1];
		objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
		objectOutputStream.flush();			
		objectOutputStream.writeObject(filePathAndName);
		objectOutputStream.flush();
		objectInputSTream = new ObjectInputStream(connection.getInputStream());
		objectInputSTream.read(byteArray,0,64000);//reading a contents of size upto 64KB
		String home = System.getProperty("user.home");
		FileOutputStream fos = new FileOutputStream(home+"/Downloads/"+fileName+".txt");
		fos.write(byteArray);
		fos.close();
	} 
	catch (UnknownHostException e) 
	{
		e.printStackTrace();
	} 
	catch (IOException e) 
	{
		e.printStackTrace();
	}
}


}
	