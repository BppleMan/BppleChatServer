/**
 * 
 */
package view;

import java.awt.TextArea;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import tools.LogString;
import tools.PathSource;

/**
 * @author BppleMan
 *
 */
public class BTextArea extends TextArea
{
	/**
	 * @param string
	 */
	public BTextArea(String string)
	{
		super(string);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.TextArea#append(java.lang.String)
	 */
	@Override
	public void append(String str)
	{
		if (!str.equals("\n"))
		{
			LogString ls = new LogString(str);
			super.append(ls.toString());
			updataLogFile(ls.getDate(), ls.getTime(), ls.getLog());
		}
		else
			super.append(str);
	}

	private void updataLogFile(String date, String time, String log)
	{
		try
		{
			SAXReader reader = new SAXReader();
			File logFile = new File(PathSource.logFolder, PathSource.logFile);
			Document logDoc = reader.read(logFile);
			Element rootEle = logDoc.getRootElement();
			Element logStringEle = rootEle.addElement("logstring");
			logStringEle.addAttribute("date", date);
			logStringEle.addAttribute("time", time);
			logStringEle.setText(log);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setIndent("    ");
			XMLWriter writer = new XMLWriter(new FileWriter(logFile), format);
			writer.write(logDoc);
			writer.flush();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
