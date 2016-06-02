/**
 * 
 */
package listen;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author BppleMan
 *
 */
public class User_Socket
{
	public String userName = null;
	public Socket socket = null;
	public ObjectInputStream ois = null;
	public ObjectOutputStream oos = null;

	/**
	 * 
	 */
	public User_Socket(String userName, Socket socket, ObjectInputStream ois, ObjectOutputStream oos)
	{
		this.userName = userName;
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return userName.toString() + "\n" + socket.toString();
	}

	public boolean equals(String userName)
	{
		if (this.userName.equals(userName))
			return true;
		else
			return false;
	}

	public boolean equals(User_Socket us)
	{
		if (this == us)
			return true;
		else
			return false;
	}
}
