/*This file is part of HC11337.

    HC11337 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HC11337 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with HC11337.  If not, see <http://www.gnu.org/licenses/>.
*/

package ca.hc11337.gui.memoryview;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

public class HC11337Memory {
	private final TableViewer memViewer;
	public HC11337Memory(Composite parent, int style)
	{
		memViewer = new TableViewer(parent, style);
		TableLayout layout = new TableLayout();
		for(int i = 0; i < 0xF; i++)
			layout.addColumnData(new ColumnWeightData(6, false));
		memViewer.getTable().setLayout(layout);
		memViewer.getTable().setLinesVisible(true);
		memViewer.getTable().setHeaderVisible(true);
		memViewer.getTable().setToolTipText("Memory");
		
		for(int i = 0; i < 0xF; i++)
			new TableColumn(memViewer.getTable(), SWT.CENTER);
		//column1.setText("Address");
		/*TableColumn column2 = new TableColumn(memViewer.getTable(), SWT.LEFT);
		column2.setText("Value");*/
	}
	
	public Composite getMem()
	{
		return memViewer.getTable();
	}
}
