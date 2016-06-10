package talkservice;

import java.io.IOException;
import java.io.ObjectInputStream;

import data.TalkInfo;

public class ReceiveTalkInfo extends Thread
{
	private TalkInfo talkInfo = null;
	private TalkInfoBuff talkInfoBuff = null;
	private ObjectInputStream ois = null;

	public ReceiveTalkInfo(TalkInfoBuff talkInfoBuff, ObjectInputStream ois)
	{
		this.talkInfoBuff = talkInfoBuff;
		this.ois = ois;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				Object object = ois.readObject();
				if (object.getClass() == TalkInfo.class)
				{
					talkInfo = (TalkInfo) object;
					talkInfoBuff.apend(talkInfo);
				}
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("下线");
		}
	}
}
