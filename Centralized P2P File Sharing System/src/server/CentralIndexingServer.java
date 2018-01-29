package server;

import java.util.HashMap;

import POJO.PeerInformation;

/*
Author: Bhagyashree Bagwe
Date: 	09/24/2017
*/

public class CentralIndexingServer {
	
	public CentralIndexingServer() {
		RegisterRequestThread();                           
		SearchRequestThread();
	}

	public static void main(String[] args) {

		CentralIndexingServer mainFrame = new CentralIndexingServer();

	}
	public void RegisterRequestThread()
	{
		//Register Request Thread
		Thread rthread = new Thread (new CentralIndexingServerImpl(2002));                     
		rthread.setName("Listen For Register");
		rthread.start();
	}
	public void SearchRequestThread()
	{
		//Search Request Thread
		Thread sthread = new Thread (new CentralIndexingServerImpl(2001));                    
		sthread.setName("Listen For Search");
		sthread.start();

	}
}