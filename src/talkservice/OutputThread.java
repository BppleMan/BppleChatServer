package talkservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import data.TalkInfo;
import listen.User_Socket;

public class OutputThread extends Thread
{
	private TalkInfo talkInfo = null;
	private TalkInfoBuff talkInfoBuff = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	public OutputThread(TalkInfoBuff talkInfoBuff, ObjectInputStream ois)
	{
		this.talkInfoBuff = talkInfoBuff;
		this.ois = ois;
	}

	@Override
	public void run()
	{
		while (true)
		{
			if (!talkInfoBuff.isEmpty())
			{
				try
				{
					talkInfo = talkInfoBuff.getTalkInfo(0);
					for (User_Socket us : TalkServerUserManege.getInstance().users)
					{
						if (us.userName.equals(talkInfo.getReceiveUser()))
						{
							oos = us.oos;
							oos.writeObject(talkInfo);
							talkInfoBuff.remove(talkInfo);
						}
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
