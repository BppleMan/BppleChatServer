/**
 * 
 */
package listen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.UserInfo;
import friendstate.SendFriendInfo;
import mytablemodel.MyTableModel;

/**
 * @author BppleMan
 *
 */
public class ListenSocket extends Thread
{
	protected Socket socket = null;
	protected UserInfo userInfo = null;
	protected User_Socket user_Socket = null;
	protected MyTableModel myTableModel = null;

	/**
	 * 
	 */
	public ListenSocket(Socket socket, UserInfo userInfo, ObjectInputStream ois, ObjectOutputStream oos)
	{
		this.socket = socket;
		this.userInfo = userInfo;
		user_Socket = new User_Socket(userInfo.getUserName(), socket, ois, oos);
		myTableModel = MyTableModel.getMyDefaultTableModel();
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
				user_Socket.socket.sendUrgentData(0xff);
				Thread.sleep(2000);
			}
		}
		catch (IOException e)
		{
			myTableModel.updataUserStateValue(user_Socket.userName, "offline");
			System.out.println(user_Socket.userName + "已下线");
			SendFriendInfo sf = new SendFriendInfo(userInfo, "offline");
			sf.start();
			try
			{
				DataServerUserManege.getInstance().removeUser(user_Socket.userName);
			}
			catch (IOException e1)
			{
				e1.printStackTrace(System.out);
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
