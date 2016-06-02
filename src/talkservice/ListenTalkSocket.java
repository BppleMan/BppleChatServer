/**
 * 
 */
package talkservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.UserInfo;
import listen.ListenSocket;

/**
 * @author BppleMan
 *
 */
public class ListenTalkSocket extends ListenSocket
{

	/**
	 * @param socket
	 * @param userInfo
	 * @param ois
	 * @param oos
	 */
	public ListenTalkSocket(Socket socket, UserInfo userInfo, ObjectInputStream ois, ObjectOutputStream oos)
	{
		super(socket, userInfo, ois, oos);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see listen.ListenSocket#run()
	 */
	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				socket.sendUrgentData(0xff);
				Thread.sleep(2000);
			}
		}
		catch (Exception e)
		{
			try
			{
				TalkServerUserManege.getInstance().removeUser(user_Socket.userName);
			}
			catch (IOException e1)
			{
				e1.printStackTrace(System.out);
			}
		}
	}

}
