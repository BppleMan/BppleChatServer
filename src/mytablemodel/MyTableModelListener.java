/**
 * 
 */
package mytablemodel;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * @author BppleMan
 *
 */
public interface MyTableModelListener extends TableModelListener
{
	public void tableValueChanged(TableModelEvent e);
}
