package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import pojo.ServerInformation;

/**
 * @author Bhagyashree
 * @since  11-05-2017
 */
public class IndexingServer {
	    
	public IndexingServer(String registerPort, String searchPort, String serverName)
	{
		RegisterRequestThread(registerPort, serverName);
		SearchRequestThread(searchPort, serverName);
	}
	
	public static void main(String[] args) {

	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	        System.out.println("Which Indexing server do you want to start? ");
	        try {
				String userInput=bufferedReader.readLine();
				bufferedReader.close(); 
				HashMap<String, ServerInformation> serverList = IndexingServerUtility.getIndexingServerList();
				IndexingServer i= new IndexingServer(serverList.get(userInput).getRegisterPort(), serverList.get(userInput).getSearchPort(), serverList.get(userInput).getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	/**
	 * @param registerPort
	 * @param serverName
	 */
	public void RegisterRequestThread(String registerPort, String serverName)
	{
		//Register Request Thread
		Thread rthread = new Thread (new IndexingServerImpl(registerPort, serverName));                     
		rthread.setName("Listen For Register");
		rthread.start();
	}
	public void SearchRequestThread(String searchPort, String serverName)
	{
		//Search Request Thread
		Thread sthread = new Thread (new IndexingServerImpl(searchPort, serverName));                    
		sthread.setName("Listen For Search");
		sthread.start();
	}
}
