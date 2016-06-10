package talkservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.UserInfo;
import listen.User_Socket;

public class SocketThread implements Runnable
{
	private Socket socket = null;
	private TalkInfoBuff talkInfoBuff = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private ReceiveTalkInfo receiveTalkInfo = null;
	private SendTalkInfo sendTalkInfo = null;

	// 不需要提供IO流对象，因为发送消息并不是发送给原用户
	// 而是要将消息分发给不同的好友，要利用好友对应的输出流进行发送消息。
	public SocketThread(Socket socket)
	{
		this.socket = socket;
		run();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Runnable#run()
	 */
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
				receiveTalkInfo = new ReceiveTalkInfo(talkInfoBuff, ois);
				receiveTalkInfo.start();
				sendTalkInfo = new SendTalkInfo(talkInfoBuff);
				sendTalkInfo.start();
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

}
