package evaluation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pojo.PeerInformation;
import pojo.ServerInformation;
import server.IndexingServerUtility;

public class EvaluationPeerClient implements Runnable{
	static String IS_HOSTNAME;
	static String IS_PORT;
	static String PEER_HOSTNAME;
	static String PEER_REG_DATA;
	static String choice;
	static int i=0;
	static ObjectOutputStream out;
	static ObjectInputStream in;
	static Socket connection;
	static Map<String, ServerInformation> serverList = IndexingServerUtility.getIndexingServerList();
	static ArrayList<PeerInformation> searchResultList=null;
	static List keysAsArray = null;
	static Iterator it=null;
	static PeerInformation peer;
	static long registerStartTime, registerEndTime, registerTotalTime= 0;
	static long searchStartTime, searchEndTime, searchTotalTime= 0;
	static long downloadStartTime, downloadEndTime, downloadTotalTime= 0;
	static double avgRegisterTime =0.0;
	static double avgsearchTime =0.0;
	static double avgDownloadTime =0.0;
	public void run()
	{
		//Evaluating 1k operations per client
		
				/* Evaluate Register functionality-Start */
				for(int j=0;j<1000;j++)
				{
					registerStartTime = System.currentTimeMillis();
					RegisterWithIS();
					registerEndTime = System.currentTimeMillis();
					registerTotalTime += (registerEndTime - registerStartTime);
				}
				avgRegisterTime = (double) Math.round(registerTotalTime / 1000) / 1000;
				System.out.println("AVG Register Time : "+avgRegisterTime+" Sec\nNo of Request : "+1000+"\nTotal Register time : "+registerTotalTime+" ms");
				/* Evaluate Register functionality-End */
				
				/* Evaluate Search functionality-Start */
				for(int k=0;k<1000;k++)
				{
					searchStartTime = System.currentTimeMillis();
					SearchWithIS();
					searchEndTime = System.currentTimeMillis();
					searchTotalTime += (searchEndTime - searchStartTime);
				}
				avgsearchTime = (double) Math.round(searchTotalTime / 1000) / 1000;
				System.out.println("AVG Search Time : "+avgsearchTime+" Sec\nNo of Request : "+1000+"\nTotal Search time : "+searchTotalTime+" ms");
				/* Evaluate Search functionality-End */
				
				/* Evaluate Download functionality-Start */
				for(int l=0;l<1000;l++)
				{
					downloadStartTime = System.currentTimeMillis();
					SearchWithIS();
					downloadEndTime = System.currentTimeMillis();
					downloadTotalTime += (downloadEndTime - downloadStartTime);
				}
				avgDownloadTime = (double) Math.round(downloadTotalTime / 1000) / 1000;
				System.out.println("AVG Download Time : "+avgDownloadTime+" Sec\nNo of Request : "+1000+"\nTotal Download time : "+downloadTotalTime+" ms");
				/* Evaluate Download functionality-End */	
	}
	

	public static void RegisterWithIS()
	{
		try 
		{
			keysAsArray = new ArrayList<>(serverList.keySet());
			Random generator = new Random();
			
			do
			{
				ServerInformation server = serverList.get(keysAsArray.get(generator.nextInt(keysAsArray.size())));
				System.out.println("\nTrying to connect to server  "+server.getName()+"...");
				
				IS_HOSTNAME=server.getIpAddress();
				IS_PORT=server.getRegisterPort();
				
				connection = new Socket(IS_HOSTNAME, Integer.parseInt(IS_PORT));
				System.out.println("\n-----> Connected to Register with Indexing server"+server.getName()+" on port "+server.getRegisterPort()+"<-----\n");
			}while(!connection.isConnected());
			
			PEER_REG_DATA = "1234 home abc";
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();			
			out.writeObject(PEER_REG_DATA);
			out.flush();
			System.out.println("Registered Successfully!!\n");
		} 
		catch (ConnectException e) {
			System.out.println("\nConnection refused, trying again..");
			RegisterWithIS();
	    } 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void SearchWithIS()
	{
			String searchFileName = "abc";
			keysAsArray = new ArrayList<>(serverList.keySet());
			
			i=0;
			search(searchFileName);
			if(searchResultList.isEmpty())
			{
				System.out.println("\n This file is not registered with any peer! \n");
				System.out.println("\n Please check the spelling of filename or try again with different name!\n");
			}
			else
			{
				it = searchResultList.iterator();
				System.out.println("Above file is found on following peers:");
				System.out.println("-----------------------------------------------------------------------------------");
				System.out.println("PeerId\t|Hostname\t|Location\t|FileName");
				System.out.println("-----------------------------------------------------------------------------------");
				while(it.hasNext())
				{
					peer = (PeerInformation)it.next();
					System.out.println(peer.getPeerID()+"\t|"+peer.getPeerHostname()+"\t|"+peer.getFilePath()+"\t\t|"+searchFileName);
				}
			}
		} 
		
	
	public static void search(String fileName)
	{
		try
		{
			do
			{
				ServerInformation server = serverList.get(keysAsArray.get(i));
				System.out.println("\nTrying to connect to server  "+server.getName()+"...");
				
				IS_HOSTNAME=server.getIpAddress();
				IS_PORT=server.getSearchPort();
				
				connection = new Socket(IS_HOSTNAME, Integer.parseInt(IS_PORT));
				System.out.println("\n-----> Connected to search with Indexing server "+server.getName()+" on port "+server.getSearchPort()+"<-----\n");
				
				out = new ObjectOutputStream(connection.getOutputStream());
				out.flush();			
				out.writeObject(fileName);
				out.flush();
				in = new ObjectInputStream(connection.getInputStream());
				searchResultList = (ArrayList<PeerInformation>) in.readObject();
				it = searchResultList.iterator();
				
				if(!searchResultList.isEmpty())
				{
					break;
				}
				
			}while(i<keysAsArray.size());
		}
		catch (ConnectException e) 
		{
			i++;
			search(fileName);
	    }
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void DownloadFromPeer()
	{
		PEER_HOSTNAME = "127.0.0.1";
		byte[] byteArray=new byte[1024*64];
		try 
		{
			connection = new Socket(PEER_HOSTNAME, 2000);
			System.out.println("\nConnected to peer "+PEER_HOSTNAME+" on port 2000\n");
			String filePathAndName = "abc";
			String[] str = filePathAndName.split(" ");
			String fileName = str[1];
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();			
			out.writeObject(filePathAndName);
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			in.read(byteArray,0,64000);//reading a contents of size upto 64KB
			String home = System.getProperty("user.home");
			FileOutputStream fos = new FileOutputStream(home+"/Downloads/"+fileName+".txt");
			fos.write(byteArray);
			fos.close();
			System.out.println("File Downloaded successfully in /Downloads folder!");
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
