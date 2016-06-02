package talkservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import data.SocketPort;

public class TalkServer extends Thread
{
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;

	public TalkServer()
	{
		try
		{
			serverSocket = new ServerSocket(SocketPort.talkServerPort);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Socket socket = serverSocket.accept();
				SocketThread st = new SocketThread(socket);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
