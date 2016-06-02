package dataservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.CommandSource;
import data.UserInfo;
import friendstate.SendFriendInfo;
import listen.DataServerUserManege;
import listen.ListenSocket;
import listen.User_Socket;
import mytablemodel.MyTableModel;

public class DateServer extends Thread
{
	private Socket socket = null;
	private UserInfo userInfo = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private boolean canLogin = false;

	public DateServer(Socket socket, ObjectInputStream ois, ObjectOutputStream oos)
	{
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
	}

	@Override
	public void run()
	{
		try
		{
			String command = null;
			while (!socket.isClosed())
			{
				Object object = ois.readObject();
				if (object instanceof String)
				{
					command = (String) object;

					if (command.equals(CommandSource.loginCommand))
					{
						creatLoginProcessing(ois, oos);
					}
					else if (command.equals(CommandSource.getFriendCommand))
					{
						creatGetFriendListProcessing(ois, oos);
					}
					else if (command.equals(CommandSource.registCommand))
					{
						creatRegistProcessing(ois, oos);
						ois.close();
						oos.close();
						socket.close();
						ois = null;
						oos = null;
						socket = null;
						break;
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace(System.out);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace(System.out);
		}
	}

	private void creatLoginProcessing(ObjectInputStream ois, ObjectOutputStream oos) throws IOException
	{
		LoginProcessing loginProcessing = new LoginProcessing(ois, oos);
		String loginCommand = loginProcessing.CanLogin();
		oos.writeObject(loginCommand);
		if (loginCommand.equals(CommandSource.canLoginCommand))
		{
			userInfo = loginProcessing.getUserInfo();
			System.out.println(userInfo.getUserName() + "可以登录");
			DataServerUserManege.getInstance().addUser(new User_Socket(userInfo.getUserName(), socket, ois, oos));
			ListenSocket ls = new ListenSocket(socket, userInfo, ois, oos);
			ls.start();
			// 通知好友更新状态
			SendFriendInfo sf = new SendFriendInfo(userInfo, "online");
			sf.start();
			// 通知Table更新数据
			MyTableModel.getMyDefaultTableModel().updataUserStateValue(userInfo.getUserName(), "online");
		}
		else if (loginCommand.equals(CommandSource.passWordError))
		{
			System.out.println(loginProcessing.getUserInfo().getUserName() + "密码错误");
		}
		else if (loginCommand.equals(CommandSource.notFoundUser))
		{
			System.out.println(loginProcessing.getUserInfo().getUserName() + "该用户未注册");
		}
	}

	private void creatGetFriendListProcessing(ObjectInputStream ois, ObjectOutputStream oos)
	{
		GetFriendListProcessing friendList = new GetFriendListProcessing(ois, oos, userInfo);
		friendList.sendFriendList();
	}

	private void creatRegistProcessing(ObjectInputStream ois, ObjectOutputStream oos)
	{
		RegistProcessing registProcessing = new RegistProcessing(ois, oos);
		if (registProcessing.canRegist())
		{
			userInfo = registProcessing.getUserInfo();
			System.out.println(userInfo.getUserName() + "注册成功");
		}
	}
}
