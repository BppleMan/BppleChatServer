/**
 * 
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import data.UserInfo;
import mytablemodel.MyTableModel;
import mytablemodel.PasswordCellRenderer;
import tools.PathSource;

/**
 * @author BppleMan
 *
 */
public class ManageMentPanel extends JPanel
{
	JTable dataTable = null;
	MyTableModel myTableModel = null;
	ListSelectionModel selectModel = null;
	ListSelectionListener selectionListener = null;
	JScrollPane jsp = null;
	UserInfo userInfo = null;
	String userState = null;

	JPanel northPanel = null;
	JLabel userNameLabel = null;
	JLabel passWordLabel = null;
	JTextField userNameText = null;
	JTextField passWordText = null;
	JButton addButton = null;
	JButton removeButton = null;
	ActionListener addAction = null;
	ActionListener removeAction = null;

	private Object[][] data = null;
	private Object[] columnNames = null;

	public ManageMentPanel()
	{
		initAction();
		initAllComponent();

		setLayout(new BorderLayout());

		add(northPanel, BorderLayout.NORTH);
		add(jsp, BorderLayout.CENTER);
	}

	// 初始化所有组件
	public void initAllComponent()
	{
		userNameLabel = new JLabel("用户名");
		passWordLabel = new JLabel("密码");
		userNameText = new JTextField();
		userNameText.setPreferredSize(new Dimension(100, 25));
		passWordText = new JTextField();
		passWordText.setPreferredSize(new Dimension(100, 25));
		addButton = new JButton("增加用户");
		removeButton = new JButton("删除用户");
		addButton.addActionListener(addAction);
		removeButton.addActionListener(removeAction);

		northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northPanel.add(userNameLabel);
		northPanel.add(userNameText);
		northPanel.add(passWordLabel);
		northPanel.add(passWordText);
		northPanel.add(addButton);
		northPanel.add(removeButton);

		initModel();
		JComboBox<String> jcb = new JComboBox<>();
		jcb.addItem("online");
		jcb.addItem("offline");
		JPasswordField jpf = new JPasswordField();
		myTableModel = MyTableModel.creatNewTableModel(data, columnNames);
		dataTable = new JTable(myTableModel);
		dataTable.getColumn("状态").setCellEditor(new DefaultCellEditor(jcb));
		dataTable.getColumn("密码").setCellEditor(new DefaultCellEditor(jpf));
		dataTable.getColumn("密码").setCellRenderer(new PasswordCellRenderer());
		selectModel = dataTable.getSelectionModel();
		selectModel.addListSelectionListener(selectionListener);
		jsp = new JScrollPane(dataTable);
	}

	// 初始化模型初始数据
	public void initModel()
	{
		ArrayList<UserInfo> usersInfoList = new ArrayList<>();
		ArrayList<String> usersState = new ArrayList<>();
		try
		{
			// 取用户索引列表xml
			File userIndexFile = new File(PathSource.sourceFolder, PathSource.userIndexFile);
			SAXReader reader = new SAXReader();
			Document document = reader.read(userIndexFile);

			// 读取根节点
			Element rootEle = document.getRootElement();

			// 初始化模型名称columnNames数组
			Element userModel = rootEle.element("usermodel");
			List<Element> list = userModel.elements();
			columnNames = new Object[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				Element element = list.get(i);
				columnNames[i] = element.getText();
			}

			// 建立模型数据data数组
			File file = new File(PathSource.sourceFolder);
			File[] files = file.listFiles();
			int templength = 0;
			for (File file2 : files)
			{
				if (file2.isDirectory())
					templength++;
			}
			data = new String[templength][columnNames.length];
			for (int i = 0, k = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					File userInfo = new File(PathSource.sourceFolder + files[i].getName(), PathSource.userInfoFile);
					Document userInfoDoc = reader.read(userInfo);
					Element userEle = userInfoDoc.getRootElement();
					Element userInfoEle = userEle.element("userinfo");
					List<Element> userInfoList = userInfoEle.elements();
					// 将数据写入模型
					for (int j = 0; j < columnNames.length; j++)
					{
						Element temp = userInfoList.get(j);
						data[k][j] = temp.getText();
					}
					k++;
				}
			}
		}
		catch (DocumentException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	// 初始化所有事件
	public void initAction()
	{
		addAction = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (userNameText.getText().length() > 0 && passWordText.getText().length() > 0)
				{
					addHandle();
				}
			}
		};
		removeAction = new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				removeHandle();
			}
		};
	}

	// 添加操作
	public void addHandle()
	{
		userState = "offline";
		String[] rowData = { userNameText.getText(), passWordText.getText(), userState };
		userInfo = new UserInfo(rowData[0], rowData[1]);
		myTableModel.addRow(rowData, userInfo, userState);
		userNameText.setText("");
		passWordText.setText("");
	}

	// 删除操作
	public void removeHandle()
	{
		myTableModel.removeRow(dataTable.getSelectedRow());
	}
}
