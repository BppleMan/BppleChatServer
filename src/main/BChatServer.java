/**
 * 
 */
package main;

import java.io.PrintStream;

import javax.swing.JFrame;

import listen.ListenClientSocket;
import talkservice.TalkServer;
import tools.MyOutputStream;
import view.DataManegeView;
import view.ManageMentPanel;

/**
 * @author BppleMan
 *
 */
public class BChatServer
{
	DataManegeView dataManegeView = null;
	ManageMentPanel manageMentPanel = null;
	ListenClientSocket listenClientSocket = null;

	public BChatServer()
	{
		DataManegeView.setTextArea();
		dataManegeView = new DataManegeView();
		dataManegeView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dataManegeView.setVisible(true);

		listenClientSocket = new ListenClientSocket();
		listenClientSocket.start();

		TalkServer ts = new TalkServer();
		ts.start();
	}

	public static void main(String[] args)
	{
		System.setOut(new PrintStream(new MyOutputStream()));
		BChatServer bChatServer = new BChatServer();
		System.gc();
	}
}
