/**
 * 
 */
package listen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import data.SocketPort;
import dataservice.DateServer;

/**
 * @author BppleMan
 *
 */
public class ListenClientSocket extends Thread
{
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;

	/**
	 * 
	 */
	public ListenClientSocket()
	{
		try
		{
			serverSocket = new ServerSocket(SocketPort.dataServerPort);
		}
		catch (IOException e)
		{
			e.printStackTrace(System.out);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				socket = serverSocket.accept();
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				DateServer dServer = new DateServer(socket, ois, oos);
				dServer.start();
			}
		}
		catch (IOException e)
		{

		}
	}
}
