/**
 * 
 */
package mytablemodel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableModel;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import data.UserInfo;
import datamanege.DataManege;
import tools.PathSource;

/**
 * @author BppleMan
 *
 */
public class MyTableModel extends DefaultTableModel
{
	private Object[][] data = null;
	private Object[] columnNames = null;
	protected EventListenerList userStateListenerList = new EventListenerList();
	private static MyTableModel myTableModel = null;

	/**
	 * @param data
	 * @param columnNames
	 *            构造函数
	 */
	private MyTableModel(Object[][] data, Object[] columnNames)
	{
		super(data, columnNames);
		this.data = data;
		this.columnNames = columnNames;
	}

	public static MyTableModel creatNewTableModel(Object[][] data, Object[] columnNames)
	{
		if (myTableModel == null)
		{
			myTableModel = new MyTableModel(data, columnNames);
		}
		return myTableModel;
	}

	public static MyTableModel getMyDefaultTableModel()
	{
		return myTableModel;
	}

	public void addTableModelListener(MyTableModelListener listener)
	{
		listenerList.add(MyTableModelListener.class, listener);
	}

	/*
	 * （非 Javadoc） 添加行
	 * 
	 * @see javax.swing.table.DefaultTableModel#addRow(java.lang.Object[])
	 */
	public void addRow(Object[] rowData, UserInfo userInfo, String userState)
	{
		super.addRow(rowData);
		try
		{
			DataManege dm = new DataManege(userInfo);
			dm.registUser();
			System.out.println("成功添加成员:" + userInfo.getUserName());
		}
		catch (IOException e)
		{
			e.printStackTrace(System.out);
		}
		catch (DocumentException e)
		{
			e.printStackTrace(System.out);
		}
	}

	/*
	 * （非 Javadoc） 删除行
	 * 
	 * @see javax.swing.table.DefaultTableModel#removeRow(int)
	 */
	@Override
	public void removeRow(int row)
	{
		String theRowValue = (String) getValueAt(row, 0);
		super.removeRow(row);
		try
		{
			DataManege dm = new DataManege(theRowValue);
			dm.removeUser();
			System.out.println("成功删除成员:" + theRowValue);
		}
		catch (IOException e)
		{
			e.printStackTrace(System.out);
		}
		catch (DocumentException e)
		{
			e.printStackTrace(System.out);
		}
	}

	/*
	 * 修改用户状态
	 */
	public void updataUserStateValue(String userName, String userState)
	{
		Vector<Object> vector = getDataVector();
		for (Object object : vector)
		{
			Vector<Object> v = (Vector<Object>) object;
			if (v.elementAt(0).equals(userName))
			{
				int row = vector.indexOf(object);
				setValueAt(userState, row, findColumn("状态"));
			}
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		if (column == findColumn("状态"))
			return true;
		else
			return false;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object,
	 * int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int row, int column)
	{
		userStateValueChanged((String) getValueAt(row, column), (String) aValue,
				(String) getValueAt(row, findColumn("用户名")));
		super.setValueAt(aValue, row, column);
	}

	public void userStateValueChanged(String oldState, String newState, String userName)
	{
		if (!oldState.equals(newState))
		{
			try
			{
				SAXReader reader = new SAXReader();
				File userInfoFile = new File(PathSource.sourceFolder + userName, PathSource.userInfoFile);
				Document userInfoDoc = reader.read(userInfoFile);
				Element userInfoEle = userInfoDoc.getRootElement().element("userinfo");
				Element userStateEle = userInfoEle.element("userstate");
				userStateEle.setText(newState);
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setIndent("    ");
				XMLWriter writer = new XMLWriter(new FileWriter(userInfoFile), format);
				writer.write(userInfoDoc);
				writer.flush();
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace(System.out);
			}
			catch (DocumentException e)
			{
				e.printStackTrace(System.out);
			}
		}
	}

	// // 最重要要是这一步这一步将会影响到最终会调用哪个xxxxChanged()方法
	// @Override
	// public void fireTableCellUpdated(int row, int column)
	// {
	// // 这是自己写的方法(复制粘贴)
	// myValueChanged(new TableModelEvent(this, row, row, column));
	// }
	//
	// /* 这是自己写的方法(复制粘贴super.fireTableChanged方法得到) */
	// public void myValueChanged(TableModelEvent e)
	// {
	// Object[] listeners = listenerList.getListenerList();
	// for (int i = listeners.length - 2; i >= 0; i -= 2)
	// {
	// if (listeners[i] == MyTableModelListener.class)
	// {
	// ((MyTableModelListener) listeners[i + 1]).tableValueChanged(e);
	// }
	// }
	// }
	//
	// /*
	// * （非 Javadoc）
	// *
	// * @see
	// javax.swing.event.TableModelListener#tableChanged(javax.swing.event.
	// * TableModelEvent)
	// */
	// @Override
	// public void tableChanged(TableModelEvent e)
	// {
	// // 这里不实现
	// }
	//
	// /*
	// * （非 Javadoc）
	// *
	// * @see
	// * mytablemodel.MyTableModelListener#tableValueChanged(javax.swing.event.
	// * TableModelEvent)
	// */
	// @Override
	// public void tableValueChanged(TableModelEvent e)
	// {
	// //暂时不实现
	// }
}
