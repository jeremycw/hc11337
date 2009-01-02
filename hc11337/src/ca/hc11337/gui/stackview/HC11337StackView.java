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

package ca.hc11337.gui.stackview;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.*;

import ca.hc11337.gui.cpuview.CPUViewContentProvider;
import ca.hc11337.gui.cpuview.CPUViewLabelProvider;

public class HC11337StackView {
	private final TableViewer stackViewer;
	int[] data;
	
	public HC11337StackView(Composite parent, int style)
	{
		Group stack = new Group(parent, 0);
		stack.setText("Stack");
		stack.setLayout(new FillLayout());
		stackViewer = new TableViewer(stack, 0);
		TableLayout layout = new TableLayout();
		//layout.addColumnData(new ColumnWeightData(40, false));
		layout.addColumnData(new ColumnWeightData(60, true));
		stackViewer.getTable().setLayout(layout);
		stackViewer.getTable().setLinesVisible(true);
		stackViewer.getTable().setHeaderVisible(true);
		stackViewer.getTable().setToolTipText("Stack");
		stackViewer.setContentProvider(new StackViewContentProvider());
		stackViewer.setLabelProvider(new StackViewLabelProvider());
		stackViewer.getTable().setFont(new Font(Display.getCurrent(), "Monospace", 10, SWT.NORMAL));
		
		//TableColumn column1 = new TableColumn(stackViewer.getTable(), SWT.CENTER);
		//column1.setText("Address");
		TableColumn column2 = new TableColumn(stackViewer.getTable(), SWT.LEFT);
		column2.setText("Value");
	}
	
	public void setData(int[] stack)
	{
		data = stack;
		stackViewer.setInput(data);
	}
}