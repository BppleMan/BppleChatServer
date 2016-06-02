/**
 * 
 */
package tools;

import java.io.IOException;
import java.net.Socket;

/**
 * @author BppleMan
 *
 */
public class SendUrgentData extends Thread
{
	Socket socket = null;

	public SendUrgentData(Socket socket)
	{
		this.socket = socket;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		super.run();
		while (true)
		{
			try
			{
				socket.sendUrgentData(0xff);
			}
			catch (IOException e)
			{
				try
				{
					socket.close();
					// System.out.println("发送数据包对方未响应 @断开连接");
				}
				catch (IOException e1)
				{
					e1.printStackTrace(System.out);
				}
				break;
			}
		}
	}
}
