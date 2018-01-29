package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import POJO.PeerInformation;

/*
Author: Bhagyashree Bagwe
Date: 	09/24/2017
Indexes all the registered servers and looks up given file with registered servers
*/

public class CentralIndexingServerImpl implements Runnable{
	
	CentralIndexingServerImpl(int port)
	{
		this.port=port;
			
	}
	
	//Create a HashMap to store information of registered Peers
	static final HashMap<Long, PeerInformation> peers = new HashMap<Long, PeerInformation>();
	ArrayList<PeerInformation> searchResultList = new ArrayList<PeerInformation>();

	long index = 0;
	int mapSize =0;
	int port;
	ServerSocket server;
	Socket connection;
	ObjectInputStream objInputStream;
	ObjectOutputStream objOutputStream;

	public void run()
	{
		if(port==2002) //Peer registration done on port no 2002
		{
			try 
			{
				server = new ServerSocket(2002);
				while(true)
				{
					System.out.println("**************** CIS is waiting for registration clients *****************");
					connection = server.accept(); 
					objInputStream = new ObjectInputStream(connection.getInputStream());
					String peerData = objInputStream.readObject().toString();
					String[] peerDataArray = peerData.split(" ");
					PeerInformation peer = new PeerInformation();
					ArrayList<String> fileNames = new ArrayList<String>(); 
					for(int i=1;i<peerDataArray.length;i++)
					{
						fileNames.add(peerDataArray[i]); 
					}
					//Populate PeerInformation object
					peer.setPeerID(Long.parseLong(peerDataArray[0])); 
					peer.setPeerHostname(connection.getInetAddress().getHostName()); 
					peer.setFilePath(peerDataArray[1]);
					peer.setFileNames(fileNames);
					
					peers.put(index, peer); 
					index++;
					
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
		if(port==2001) //Peer search done on port no 2001
			{
				try
				{
					server = new ServerSocket(2001);
					while(true)
					{
						System.out.println("**************** CIS is waiting for searching clients *****************");
						connection = server.accept();
						objInputStream = new ObjectInputStream(connection.getInputStream());
						String searchFileName = objInputStream.readObject().toString();
						System.out.println("received data from client");
						Iterator it = peers.entrySet().iterator();
						while(it.hasNext())
						{
							Map.Entry pair = (Map.Entry)it.next();
							PeerInformation p1 = (PeerInformation)pair.getValue();
							for(int i=0; i<p1.getFileNames().size();i++)
							{
								if(searchFileName.equalsIgnoreCase(p1.getFileNames().get(i)))
								{
									searchResultList.add(p1);
								}
							}
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