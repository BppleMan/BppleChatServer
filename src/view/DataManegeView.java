/**
 * 
 */
package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * @author BppleMan
 *
 */
public class DataManegeView extends JFrame
{
	private JTabbedPane selectPanel = null;
	public static BTextArea logText = null;
	private ManageMentPanel managementPanel = null;

	public DataManegeView()
	{
		setBounds();
		managementPanel = new ManageMentPanel();
		// setTextArea();

		selectPanel = new JTabbedPane();
		selectPanel.setAutoscrolls(true);
		selectPanel.add("数据中心", managementPanel);
		selectPanel.add("控制台日志", logText);
		add(selectPanel);
	}

	public void setBounds()
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int sWidth = (int) d.getWidth();
		int sHeight = (int) d.getHeight();
		setSize(new Dimension(sWidth * 2 / 3, sHeight * 2 / 3));
		setLocation((sWidth - getWidth()) / 2, (sHeight - getHeight()) / 2);
	}

	public static void setTextArea()
	{
		logText = new BTextArea("hello\n");
		Font font = new Font("Menlo-Regular", Font.PLAIN, 18);
		logText.setFont(font);
		logText.setEditable(false);
	}

}