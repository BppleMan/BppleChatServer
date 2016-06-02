/**
 * 
 */
package tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author BppleMan
 *
 */
public class LogString
{
	String log = null;
	String string = null;
	Date date = null;

	/**
	 * 
	 */
	public LogString(String string)
	{
		date = new Date();
		this.string = string;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.log = df.format(date) + ":" + string;
	}

	public String getDate()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	public String getTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(date);
	}

	public String getLog()
	{
		return string;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return log;
	}
}
