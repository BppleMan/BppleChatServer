package dataservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.dom4j.DocumentException;

import data.CommandSource;
import data.UserInfo;
import datamanege.DataManege;
import mytablemodel.MyTableModel;

public class RegistProcessing
{
	private UserInfo userInfo = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	public RegistProcessing(ObjectInputStream ois, ObjectOutputStream oos)
	{
		this.ois = ois;
		this.oos = oos;
	}

	public boolean canRegist()
	{
		boolean flag = false;
		try
		{
			userInfo = (UserInfo) ois.readObject();
			DataManege dm = new DataManege(userInfo);
			String command = dm.searchUserInfo();
			if (command.equals(CommandSource.notCanRegistCommand))
			{
				// 通知客户端无法注册
				oos.writeObject(command);
				flag = false;
			}
			else if (command.equals(CommandSource.canRegistCommand))
			{
				// 通知客户端可以注册
				oos.writeObject(command);
				registUser();
				flag = true;
			}
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("找不到UserInfo类");
		}
		catch (IOException e)
		{
			e.printStackTrace(System.out);
		}
		catch (DocumentException e)
		{
			e.printStackTrace(System.out);
		}
		return flag;
	}

	public UserInfo getUserInfo()
	{
		return userInfo;
	}

	public void registUser()
	{
		MyTableModel mtm = MyTableModel.getMyDefaultTableModel();
		Object[] rowData = { userInfo.getUserName(), userInfo.getPassWord(), "offline" };
		mtm.addRow(rowData, userInfo, "offline");
	}
}
