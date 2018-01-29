package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import pojo.ServerInformation;

/**
 * @author Bhagyashree
 * @since  11-05-2017
 */
public class IndexingServerUtility {
	public static HashMap<String, ServerInformation> serverList = new HashMap<String, ServerInformation>();
	
	public static HashMap<String, ServerInformation> getIndexingServerList(){
	try {
		String line = null;
		String[] serverInfo;
		ServerInformation server=null;	 

		String basePath = new File("").getAbsolutePath();
	    String path = new File("src/Network.config").getAbsolutePath();
	    FileReader fileReader = new FileReader(path);
	    BufferedReader bufferedReader =  new BufferedReader(fileReader);
	
	    while((line = bufferedReader.readLine()) != null) {
	    	serverInfo=line.split(" ");
	    	
	    	server= new ServerInformation();
	    	server.setName(serverInfo[0]);
	    	server.setIpAddress(serverInfo[1]);
	    	server.setRegisterPort(serverInfo[2]);
	    	server.setSearchPort(serverInfo[3]);
	    	
	    	serverList.put(server.getName(), server);
	    }   
	    bufferedReader.close(); 
		}
		catch(FileNotFoundException ex) {
	        System.out.println("Unable to open Netwrok.config file");                
	    }
	    catch(IOException ex) {
	        System.out.println("Error reading Netwrok.config file");                  
	    }
	 	
		return serverList;
	}
}
