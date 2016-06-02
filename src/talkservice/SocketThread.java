package talkservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.UserInfo;
import listen.User_Socket;

public class SocketThread
{
	private Socket socket = null;
	private TalkInfoBuff talkInfoBuff = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private InputThread inputThread = null;
	private OutputThread outputThread = null;
	Runnable runnable = new Runnable()
	{

		@Override
		public void run()
		{
			try
			{
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				Object object = ois.readObject();
				if (object.getClass() == UserInfo.class)
				{
					UserInfo userInfo = (UserInfo) object;
					User_Socket us = new User_Socket(userInfo.getUserName(), socket, ois, oos);
					TalkServerUserManege.getInstance().addUser(us);
					talkInfoBuff = new TalkInfoBuff();
					inputThread = new InputThread(talkInfoBuff, ois, oos);
					inputThread.start();
					outputThread = new OutputThread(talkInfoBuff, ois);
					outputThread.start();
					ListenTalkSocket lts = new ListenTalkSocket(socket, userInfo, ois, oos);
					lts.start();
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
	};

	public SocketThread(Socket socket)
	{
		this.socket = socket;
		runnable.run();
	}

}
