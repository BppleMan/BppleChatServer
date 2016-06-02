/**
 * 
 */
package listen;

import tools.UserManege;

/**
 * @author BppleMan
 *
 */
public class DataServerUserManege extends UserManege
{
	private static DataServerUserManege dataServerUserManege = new DataServerUserManege();

	/**
	 * 
	 */
	private DataServerUserManege()
	{
		super();
	}

	public static DataServerUserManege getInstance()
	{
		return dataServerUserManege;
	}
}
