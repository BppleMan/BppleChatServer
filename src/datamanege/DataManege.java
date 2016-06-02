/**
 * 
 */
package datamanege;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import data.CommandSource;
import data.UserInfo;
import tools.PathSource;

/**
 * @author BppleMan
 *
 */
public class DataManege
{
	private UserInfo userInfo = null;
	private SAXReader reader = null;

	/**
	 * @param userInfo
	 */
	public DataManege(UserInfo userInfo)
	{
		this.userInfo = userInfo;
		this.reader = new SAXReader();
	}

	public DataManege(String userName)
	{
		this.userInfo = new UserInfo(userName, null);
		this.reader = new SAXReader();
	}

	public String getUserState() throws DocumentException
	{
		File userInfoFile = new File(PathSource.sourceFolder + userInfo.getUserName(), PathSource.userInfoFile);
		Document userInfoDoc = reader.read(userInfoFile);
		Element userInfoEle = userInfoDoc.getRootElement().element("userinfo");
		return userInfoEle.elementText("userstate");
	}

	public String[] getUserFriends() throws DocumentException
	{
		File userInfoFile = new File(PathSource.sourceFolder + userInfo.getUserName(), PathSource.userInfoFile);
		Document userInfoDoc = reader.read(userInfoFile);
		Element friendListEle = userInfoDoc.getRootElement().element("friendlist");
		List<Element> list = friendListEle.elements();
		String[] friendList = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
		{
			Element element = list.get(i);
			friendList[i] = element.getText();
		}
		return friendList;
	}

	public String validUserInfo() throws DocumentException
	{
		File userIndexFile = new File(PathSource.sourceFolder, PathSource.userIndexFile);
		Document userIndexDoc = reader.read(userIndexFile);
		Element userListEle = userIndexDoc.getRootElement().element("userlist");
		List<Element> list = userListEle.elements();
		boolean flag = false;
		for (Element element : list)
		{
			if (element.getText().equals(userInfo.getUserName()))
				flag = true;
		}
		if (flag)
		{
			File userInfoFile = new File(PathSource.sourceFolder + userInfo.getUserName(), PathSource.userInfoFile);
			Document userInfoDoc = reader.read(userInfoFile);
			Element userInfoEle = userInfoDoc.getRootElement().element("userinfo");
			Element passwordEle = userInfoEle.element("password");
			if (passwordEle.getText().equals(userInfo.getPassWord()))
				return CommandSource.canLoginCommand;
			else
				return CommandSource.passWordError;
		}
		else
			return CommandSource.notFoundUser;
	}

	public String searchUserInfo() throws DocumentException
	{
		File userIndexFile = new File(PathSource.sourceFolder, PathSource.userIndexFile);
		Document userIndexDoc = reader.read(userIndexFile);
		Element userListEle = userIndexDoc.getRootElement().element("userlist");
		List<Element> list = userListEle.elements();
		for (Element element : list)
		{
			if (element.getText().equals(userInfo.getUserName()))
				return CommandSource.notCanRegistCommand;
		}
		return CommandSource.canRegistCommand;
	}

	public void registUser() throws DocumentException, IOException
	{
		// 取用户索引列表xml
		File userIndexFile = new File(PathSource.sourceFolder, PathSource.userIndexFile);
		Document userIndexDoc = reader.read(userIndexFile);
		// 获取根节点
		Element userIndexEle = userIndexDoc.getRootElement();
		Element userListEle = userIndexEle.element("userlist");
		Element userNewEle = userListEle.addElement("username");
		userNewEle.setText(userInfo.getUserName());
		// 写入索引
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setIndent("    ");
		XMLWriter writer = new XMLWriter(new FileWriter(userIndexFile), format);
		writer.write(userIndexDoc);
		writer.flush();

		// 创建 用户文件夹
		String userFoldePath = PathSource.sourceFolder + userInfo.getUserName() + "/";
		File userFolder = new File(userFoldePath);
		if (!userFolder.exists())
			userFolder.mkdirs();
		// 取／创建 用户信息文件
		File userInfoFile = new File(userFoldePath, PathSource.userInfoFile);
		// 工厂模式创建一个Documents
		reader = new SAXReader();
		Document userInfoDoc = reader.getDocumentFactory().createDocument("utf-8");
		// doc中增加一个根节点userinfo
		Element rootEle = userInfoDoc.addElement("user");
		Element userInfoEle = rootEle.addElement("userinfo");
		Element friendListEle = rootEle.addElement("friendlist");
		// 写入用户信息
		Element userNameEle = userInfoEle.addElement("username");
		userNameEle.setText(userInfo.getUserName());
		Element passWordEle = userInfoEle.addElement("password");
		passWordEle.setText(userInfo.getPassWord());
		Element userStateEle = userInfoEle.addElement("userstate");
		userStateEle.setText("offline");

		// 写入文件
		writer = new XMLWriter(new FileWriter(userInfoFile), format);
		writer.write(userInfoDoc);
		writer.flush();
		writer.close();
	}

	public void removeUser() throws IOException, DocumentException
	{
		// 取用户索引列表
		File usersIndexFile = new File(PathSource.sourceFolder, PathSource.userIndexFile);
		Document userIndexDoc = reader.read(usersIndexFile);
		// 取出所有用户名
		Element userListEle = userIndexDoc.getRootElement().element("userlist");
		List<Element> users = userListEle.elements();
		// 遍历列表
		for (Element element : users)
		{
			// 删除标签
			if (element.getText().equals(userInfo.getUserName()))
				userListEle.remove(element);
		}
		// 刷新索引表
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setIndent("    ");
		XMLWriter writer = new XMLWriter(new FileWriter(usersIndexFile), format);
		writer.write(userIndexDoc);
		writer.flush();
		writer.close();
		// 取用户文件夹
		File userFolder = new File(PathSource.sourceFolder + userInfo.getUserName());
		deleteDir(userFolder);
	}

	/*
	 * 递归删除
	 */
	private boolean deleteDir(File dir)
	{
		if (dir.isDirectory())
		{
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++)
			{
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success)
				{
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}
}
