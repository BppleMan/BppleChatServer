package dataservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.dom4j.DocumentException;

import data.FriendInfo;
import data.UserInfo;
import datamanege.DataManege;

public class GetFriendListProcessing
{
	private UserInfo userInfo = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private ArrayList<FriendInfo> friendList = new ArrayList<>();

	public GetFriendListProcessing(ObjectInputStream ois, ObjectOutputStream oos, UserInfo userInfo)
	{
		this.ois = ois;
		this.oos = oos;
		this.userInfo = userInfo;
	}

	public void sendFriendList()
	{
		try
		{
			// 读取好友状态
			DataManege dm = new DataManege(userInfo);
			String[] friendnames = dm.getUserFriends();
			String[] friendstate = new String[friendnames.length];
			for (int i = 0; i < friendnames.length; i++)
			{
				// 获取用户状态UserInfo -> userstate
				DataManege dm1 = new DataManege(friendnames[i]);
				friendstate[i] = dm1.getUserState();
			}
			// 拼装好友列表
			for (int i = 0; i < friendnames.length; i++)
			{
				FriendInfo friendInfo = new FriendInfo(friendnames[i], friendstate[i]);
				friendList.add(friendInfo);
			}
			oos.writeObject(friendList);
			System.out.println("成功向" + userInfo.getUserName() + "发送好友列表");
		}
		catch (IOException e)
		{
			e.printStackTrace(System.out);
		}
		catch (DocumentException e)
		{
			e.printStackTrace(System.out);
		}
	}
}
