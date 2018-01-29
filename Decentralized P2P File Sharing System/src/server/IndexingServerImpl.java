package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pojo.PeerInformation;

/**
 * @author Bhagyashree
 * @since  11-05-2017
 */
public class IndexingServerImpl implements Runnable{
	
	long index = 0;
	int mapSize =0;
	int port;
	String serverName;
	//Create a HashMap to store information of registered Peers
	static final HashMap<Long, PeerInformation> peers = new HashMap<Long, PeerInformation>();
	ArrayList<PeerInformation> searchResultList = new ArrayList<PeerInformation>();
	ServerSocket server;
	Socket connection;
	ObjectInputStream objInputStream;
	ObjectOutputStream objOutputStream;

	public IndexingServerImpl(String port, String serverName)
	{
		this.port=Integer.parseInt(port);
		this.serverName=serverName;
	}
	
	public void run()
	{

		if(port==2001 || port==2003 || port==2005 || port==2007 || port == 2009 || port == 2011 || port == 2013) //Peer registration
		{
			try 
			{
				server = new ServerSocket(port);
				while(true)
				{
					System.out.println("**************** Indexing server "+serverName+" is waiting for registration clients *****************");
					connection = server.accept(); 
					objInputStream = new ObjectInputStream(connection.getInputStream());
					String peerData = objInputStream.readObject().toString();
					String[] peerDataArray = peerData.split(" ");
					PeerInformation peer = new PeerInformation();
					ArrayList<String> fileNames = new ArrayList<String>(); 
					for(int i=2;i<peerDataArray.length;i++)
					{
						fileNames.add(peerDataArray[i]); 
					}
					peer.setPeerID(Long.parseLong(peerDataArray[0])); 
					peer.setPeerHostname(connection.getInetAddress().getHostName()); 
					peer.setFilePath(peerDataArray[1]);
					peer.setFileNames(fileNames);
					peers.put(index, peer); 
					
					index++;
					List keysAsArray  = new ArrayList<>(peers.keySet());
					Iterator it = keysAsArray.iterator();
					System.out.println("New file registered!");
					System.out.println("-----------------------------------------------------------------------------------");
					System.out.println("PeerId\t|Hostname\t|Location\t|FileName");
					System.out.println("-----------------------------------------------------------------------------------");
					while(it.hasNext())
					{
						peer = peers.get(it.next());
						System.out.println(peer.getPeerID()+"\t|"+peer.getPeerHostname()+"\t|"+peer.getFilePath()+"\t\t|"+peer.getFileNames());
					}
					objInputStream.close();
					connection.close();
				}
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
		if(port==2002 || port==2004 || port==2006 || port==2008 || port == 2010 || port == 2012 || port == 2014) //Peer search 
			{
				try
				{
					server = new ServerSocket(port);
					
					while(true)
					{
						searchResultList.clear();
						System.out.println("**************** Indexing Server "+serverName+" is waiting for searching clients *****************");
						connection = server.accept();
						objInputStream = new ObjectInputStream(connection.getInputStream());
						String searchFileName = objInputStream.readObject().toString();
						List keysAsArray = new ArrayList<>(peers.keySet());
						Iterator it = keysAsArray.iterator();
						int i=0;
						while(i<keysAsArray.size())
						{
							ArrayList<String> fileNames = peers.get(keysAsArray.get(i)).getFileNames();
							for(int j=0; j<fileNames.size();j++)
							{
								if(searchFileName.equalsIgnoreCase(fileNames.get(j)))
								{
									searchResultList.add(peers.get(keysAsArray.get(i)));
								}
							}
							i++;
						}
						objOutputStream = new ObjectOutputStream(connection.getOutputStream());
						objOutputStream.flush();
						objOutputStream.writeObject(searchResultList);
						objOutputStream.flush();
					}
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
	}
}
