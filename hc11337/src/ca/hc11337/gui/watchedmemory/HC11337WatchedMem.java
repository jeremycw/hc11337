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

package ca.hc11337.gui.watchedmemory;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class HC11337WatchedMem {
	
	public HC11337WatchedMem(Composite parent, int style)
	{
		Group memory = new Group(parent, 0);
		memory.setText("Watched Memory");
		memory.setLayout(new FillLayout());
		final TableViewer memViewer = new TableViewer(memory, 0);
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(40, false));
		layout.addColumnData(new ColumnWeightData(60, true));
		memViewer.getTable().setLayout(layout);
		memViewer.getTable().setLinesVisible(true);
		memViewer.getTable().setHeaderVisible(true);
		memViewer.getTable().setToolTipText("Watched Memory");
		
		TableColumn column1 = new TableColumn(memViewer.getTable(), SWT.CENTER);
		column1.setText("Name");
		TableColumn column2 = new TableColumn(memViewer.getTable(), SWT.LEFT);
		column2.setText("Value");
	}
}