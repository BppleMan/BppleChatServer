/**
 * 
 */
package talkservice;

import tools.UserManege;

/**
 * @author BppleMan
 *
 */
public class TalkServerUserManege extends UserManege
{
	private static TalkServerUserManege talkServerUserManege = new TalkServerUserManege();

	/**
	 * 
	 */
	private TalkServerUserManege()
	{
		super();
	}

	public static TalkServerUserManege getInstance()
	{
		return talkServerUserManege;
	}
}
