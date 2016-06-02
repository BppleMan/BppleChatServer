/**
 * 
 */
package tools;

import java.io.IOException;
import java.io.OutputStream;

import view.DataManegeView;

/**
 * @author BppleMan
 *
 */
public class MyOutputStream extends OutputStream
{

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException
	{
		DataManegeView.logText.append(String.valueOf(b));
	}

	public void write(String string) throws IOException
	{
		DataManegeView.logText.append(new String(string));
	}

	public void write(byte data[], int off, int len) throws IOException
	{
		// 追加一行字符串中指定的部分，这个最重要
		DataManegeView.logText.append(new String(data, off, len));
		// 移动TextArea的光标到最后，实现自动滚动
		DataManegeView.logText.setCaretPosition(DataManegeView.logText.getText().length());
	}
}
