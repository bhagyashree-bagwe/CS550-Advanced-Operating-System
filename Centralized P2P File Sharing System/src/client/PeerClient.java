package client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import POJO.PeerInformation;

/*
Author: Bhagyashree Bagwe
Date: 	09/25/2017
Allows user to 1.Register with CIS 2.Look up with CIS 3.Download from peer
*/

public class PeerClient {
	
	String CIS_HOSTNAME;
	String PEER_HOSTNAME;
	String PEER_REG_DATA;
	Socket connection;
	ObjectOutputStream out;
	ObjectInputStream in;
	String choice;
	
	Scanner scan = new Scanner(System.in);

	public PeerClient()
	{
		peerServerThread();
		
		while (true)
		{
			System.out.println("\nEnter The Option :\n 1. Registering the File \n \n2. Searching On CentralIndxServer \n \n3. Downloading From Peer Server \n \n4. Exit\n");	
			choice = scan.nextLine();
			if (choice.equals("1")){
				RegisterWithCIS();                          //Register Method call
			}		
			if (choice.equals("2")){
				SearchWithCIS();                            //Search Method call
			}
			if (choice.equals("3")){
				DownloadFromPeer();                         //Download Method call 
			}
			if (choice.equals("4")){
				System.out.println("Exiting.");
				System.exit(0);   		
			}
		}
	}
	
	public void RegisterWithCIS()
	{
		try 
		{
			System.out.println("\nPlease enter CIS hostname: \n");
			CIS_HOSTNAME = scan.nextLine();
			connection = new Socket(CIS_HOSTNAME, 2002);
			System.out.println("\n-----> Connected to Register with CIS on port 2002 <-----\n");
			System.out.println("\nPlease enter 4 digit Peer ID, File path and file name, separated by space:\n");
			PEER_REG_DATA = scan.nextLine();
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();			
			out.writeObject(PEER_REG_DATA);
			out.flush();
			System.out.println("Registered Successfully!!\n");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void SearchWithCIS()
	{
		try 
		{
			System.out.println("\nPlease enter CIS hostname: \n");
			CIS_HOSTNAME = scan.nextLine();
			connection = new Socket(CIS_HOSTNAME, 2001);
			System.out.println("\nConnected to Search with CIS on port 2003\n");
			System.out.println("\nWhich file you want to search? \n");
			String searchFileName = scan.nextLine();
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();			
			out.writeObject(searchFileName);
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			ArrayList<PeerInformation> searchResultList = (ArrayList<PeerInformation>) in.readObject();
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
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
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
		Thread sthread = new Thread (new PeerServer(2000));                    
		sthread.setName("Listen For Peer Download");
		sthread.start();

	}
}
