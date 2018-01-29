package peer;

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
import java.util.Scanner;

import pojo.PeerInformation;
import pojo.ServerInformation;
import server.IndexingServerUtility;


/**
 * This class behaves as client side of Peer and allows user 
 * to perform register/search/download functionality
 * 
 * @author Bhagyashree
 * @since  11-05-2017
 */
public class PeerClient {

	String IS_HOSTNAME;
	String IS_PORT;
	String PEER_HOSTNAME;
	String PEER_REG_DATA;
	String PEER_DOWNLOAD_PORT;
	String choice;
	int i=0;
	ObjectOutputStream out;
	ObjectInputStream in;
	Socket connection;
	static Map<String, ServerInformation> serverList = IndexingServerUtility.getIndexingServerList();
	ArrayList<PeerInformation> searchResultList= new ArrayList<PeerInformation>();
	List keysAsArray = null;
	Iterator it=null;
	PeerInformation peer;
	Scanner scan = new Scanner(System.in);

	public PeerClient()
	{
		peerServerThread();
		
		while (true)
		{
			System.out.println("\nEnter The Option :\n1. Registering a File \n \n2. Searching a file \n \n3. Download a file \n \n4. Exit\n");	
			choice = scan.nextLine();
			if (choice.equals("1")){
				RegisterWithIS();	//Register Method call
			}		
			if (choice.equals("2")){
				SearchWithIS();		//Search Method call
			}
			if (choice.equals("3")){
				DownloadFromPeer();	//Download Method call 
			}
			if (choice.equals("4")){
				System.out.println("Exiting.");
				System.exit(0);   		
			}
		}
	}
	
	public void RegisterWithIS()
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
			
			System.out.println("\nPlease enter 4 digit Peer ID, File path and file name, separated by space:\n");
			PEER_REG_DATA = scan.nextLine();
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
	
	public void SearchWithIS()
	{
		    searchResultList.clear();
			System.out.println("\nWhich file you want to search? \n");
			String searchFileName = scan.nextLine();
			keysAsArray = new ArrayList<>(serverList.keySet());
			i=0;
			search(searchFileName);
			it = searchResultList.iterator();
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
		
	public void search(String fileName)
	{
		try
		{
			while(i<keysAsArray.size())
			{
				ServerInformation server = serverList.get(keysAsArray.get(i));
				//System.out.println("\nTrying to connect to server  "+server.getName()+"...");
				
				IS_HOSTNAME=server.getIpAddress();
				IS_PORT=server.getSearchPort();
				
				connection = new Socket(IS_HOSTNAME, Integer.parseInt(IS_PORT));
				//System.out.println("\n-----> Connected to search with Indexing server "+server.getName()+" on port "+server.getSearchPort()+"<-----\n");
				
				out = new ObjectOutputStream(connection.getOutputStream());
				out.flush();			
				out.writeObject(fileName);
				out.flush();
				in = new ObjectInputStream(connection.getInputStream());
				ArrayList<PeerInformation> list= (ArrayList<PeerInformation>) in.readObject();
				System.out.println("----"+list.size());
				if(list!=null && !list.isEmpty())
					searchResultList.addAll(list);
				i++;
				search(fileName);
			}
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
		System.out.println("Enter Peer hostname: \n");
		PEER_HOSTNAME = scan.nextLine();
		byte[] byteArray=new byte[1024*64];
		try 
		{
			connection = new Socket(PEER_HOSTNAME, 2000);
			System.out.println("\nConnected to peer "+PEER_HOSTNAME+" on port 2000\n");
			System.out.println("Enter filepath and filename for download : \n");
			String filePathAndName = scan.nextLine();
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
	
	public static void main(String[] args) {
		
		PeerClient mainFrame2 = new PeerClient();
	}
	
	public void peerServerThread()
	{
		//Peer download Request Thread
		System.out.println("Enter your download port : ");
		PEER_DOWNLOAD_PORT=scan.nextLine();
		Thread sthread = new Thread (new PeerServer(Integer.parseInt(PEER_DOWNLOAD_PORT)));                    
		sthread.setName("Listen For Peer Download");
		sthread.start();
	}
}
