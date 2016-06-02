/**
 * 
 */
package tools;

import java.io.IOException;
import java.util.Vector;

import listen.User_Socket;

/**
 * @author BppleMan
 *
 */
public class UserManege
{
	public volatile Vector<User_Socket> users = null;

	public UserManege()
	{
		users = new Vector<User_Socket>();
	}

	public void addUser(User_Socket us)
	{
		users.addElement(us);
	}

	public User_Socket getUser(int index)
	{
		return users.elementAt(index);
	}

	public User_Socket getUser(String userName)
	{
		for (User_Socket user_Socket : users)
		{
			if (user_Socket.equals(userName))
				return user_Socket;
		}
		return null;
	}

	public void removeUser(String userName) throws IOException
	{
		for (User_Socket user_Socket : users)
		{
			if (user_Socket.equals(userName))
			{
				users.remove(user_Socket);
				break;
			}
		}
	}

}
