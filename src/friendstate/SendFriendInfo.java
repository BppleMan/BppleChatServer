/**
 * 
 */
package friendstate;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.dom4j.DocumentException;

import data.FriendInfo;
import data.UserInfo;
import datamanege.DataManege;
import listen.DataServerUserManege;
import listen.User_Socket;

/**
 * @author BppleMan
 *
 */
public class SendFriendInfo extends Thread
{
	private UserInfo userInfo = null;
	private String userState = null;
	private String[] friends = null;

	/**
	 * 
	 */
	public SendFriendInfo(UserInfo userInfo, String userState)
	{
		this.userInfo = userInfo;
		this.userState = userState;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		DataManege dm = new DataManege(userInfo.getUserName());
		try
		{
			friends = dm.getUserFriends();
			for (int i = 0; i < friends.length; i++)
			{
				String string = friends[i];
				for (User_Socket us : DataServerUserManege.getInstance().users)
				{
					if (string.equals(us.userName))
					{
						FriendInfo friendInfo = new FriendInfo(userInfo.getUserName(), userState);
						sendFriendInfoToSocket(friendInfo, us.oos);
						break;
					}
				}
			}
		}
		catch (DocumentException e)
		{
			e.printStackTrace(System.out);
		}
	}

	public void sendFriendInfoToSocket(FriendInfo friendInfo, ObjectOutputStream oos)
	{
		try
		{
			oos.writeObject(friendInfo);
		}
		catch (IOException e)
		{
			e.printStackTrace(System.out);
		}
	}
}
