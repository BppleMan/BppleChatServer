package talkservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import data.TalkInfo;

public class InputThread extends Thread
{
	private TalkInfo talkInfo = null;
	private TalkInfoBuff talkInfoBuff = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;

	public InputThread(TalkInfoBuff talkInfoBuff, ObjectInputStream ois, ObjectOutputStream oos)
	{
		this.talkInfoBuff = talkInfoBuff;
		this.ois = ois;
		this.oos = oos;
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
