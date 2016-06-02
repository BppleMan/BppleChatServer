/**
 * 
 */
package mytablemodel;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author BppleMan
 *
 */
public class PasswordCellRenderer extends DefaultTableCellRenderer
{
	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
	 */
	@Override
	protected void setValue(Object value)
	{
		String pwd = new String();
		if (value instanceof String)
		{
			for (int i = 0; i < ((String) value).length(); i++)
			{
				pwd += "*";
			}
		}
		super.setValue(pwd);
	}
}
