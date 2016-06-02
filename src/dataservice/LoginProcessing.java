package dataservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.dom4j.DocumentException;

import data.UserInfo;
import datamanege.DataManege;

public class LoginProcessing
{
	boolean canLogin = false;
	private String returnCommand = null;
	private String userName = null;
	private String passWord = null;
	private UserInfo userInfo = null;
	private Socket thisSocket = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	public LoginProcessing(ObjectInputStream ois, ObjectOutputStream oos)
	{
		this.ois = ois;
		this.oos = oos;
	}

	public String CanLogin()
	{
		String command = null;
		try
		{
			userInfo = (UserInfo) ois.readObject();
			if (userInfo != null)
			{
				DataManege dm = new DataManege(userInfo);
				command = dm.validUserInfo();
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
		return command;
	}

	public UserInfo getUserInfo()
	{
		return userInfo;
	}

}
