package talkservice;

import java.io.IOException;

import data.TalkInfo;
import listen.User_Socket;

public class SendTalkInfo extends Thread
{
	private TalkInfo talkInfo = null;
	private TalkInfoBuff talkInfoBuff = null;

	public SendTalkInfo(TalkInfoBuff talkInfoBuff)
	{
		this.talkInfoBuff = talkInfoBuff;
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
							us.oos.writeObject(talkInfo);
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
