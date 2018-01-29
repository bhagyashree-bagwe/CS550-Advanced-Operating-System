package peer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class implements server side functionality of Peer
 * 
 * @author Bhagyashree
 * @since  11-05-2017
 */
public class PeerServer implements Runnable{

	
	public PeerServer(int port)
	{
		this.port=port;
	}
	
	int port;
	ServerSocket server;
	Socket connection;
	ObjectInputStream objInputStream;
	ObjectOutputStream objOutputStream;

	@Override
	public void run()
	{
		try
		{
				server = new ServerSocket(port);
				while(true)
				{
					System.out.println("**************** Peer is waiting for download requests ***************");
					connection = server.accept();
					objInputStream = new ObjectInputStream(connection.getInputStream());
					String searchFileName;
					String searchFilePath;
					String peerData = objInputStream.readObject().toString();
					String[] strArray = peerData.split(" ");
					searchFilePath=strArray[0];
					searchFileName=strArray[1];
					try
					{
						File file=new File(searchFilePath+File.separator+searchFileName);
						byte[] filebytesArray=new byte[(int)file.length()];
						BufferedInputStream buf=new BufferedInputStream(new FileInputStream(file));
						buf.read(filebytesArray,0,filebytesArray.length);
						objOutputStream = new ObjectOutputStream(connection.getOutputStream());
						objOutputStream.write(filebytesArray, 0, filebytesArray.length);
						objOutputStream.flush();
						buf.close();
						objOutputStream.close();
					} 
					catch(Exception e)
					{
						System.out.println("Cannot Open File");
					}
					objInputStream.close();
					connection.close();
				}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		 catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}



}
