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

import ca.hc11337.gui.cpuview.CPUViewContentProvider;
import ca.hc11337.gui.cpuview.CPUViewLabelProvider;

public class HC11337Memory {
	private final TableViewer memViewer;
	private int[][] data;
	private TableColumn[] columns = new TableColumn[17];
	
	public HC11337Memory(Composite parent, int style)
	{
		memViewer = new TableViewer(parent, style);
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(3, false));
		for(int i = 1; i <= 0x10; i++)
			layout.addColumnData(new ColumnWeightData(2, false));
		memViewer.getTable().setLayout(layout);
		memViewer.getTable().setLinesVisible(true);
		memViewer.getTable().setHeaderVisible(true);
		memViewer.getTable().setToolTipText("Memory");
		memViewer.setContentProvider(new MemoryViewContentProvider());
		memViewer.setLabelProvider(new MemoryViewLabelProvider());
		
		for(int i = 0; i <= 0x10; i++){
			columns[i] = new TableColumn(memViewer.getTable(), SWT.CENTER);
		}

		for(int i = 1; i <= 0x10; i++){
			columns[i].setText(Integer.toHexString(i-1));
		}
		
		/*for(int i = 1; i <= 0x10; i++)
			new TableColumn(memViewer.getTable(), SWT.CENTER);*/
		//column1.setText("Address");
		/*TableColumn column2 = new TableColumn(memViewer.getTable(), SWT.LEFT);
		column2.setText("Value");*/
	}
	
	public Composite getMem()
	{
		return memViewer.getTable();
	}
	
	public void setData(int[][] mem)
	{
		data = mem;
		memViewer.setInput(data);
		memViewer.refresh();
	}
	
	public void setMemoryCell(int row, int col, int value)
	{
		data[row][col] = value;
		String num = Integer.toHexString(value);
		if(num.length() == 1)
			num = "0"+num;
		memViewer.getTable().getItem(row).setText(col+1, num);
	}
}
