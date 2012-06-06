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

package ca.hc11337.gui.fileselector;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import java.io.*;

import ca.hc11337.gui.*;

public class HC11337FileSelector {
	private final TableViewer fileViewer;
	
	public HC11337FileSelector(Composite parent, HC11337Controller controller, int style)
	{
		fileViewer = new TableViewer(parent, style);
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(100, true));
		fileViewer.getTable().setLayout(layout);
		fileViewer.getTable().setHeaderVisible(true);
		fileViewer.setContentProvider(new FileSelectorContentProvider());
		fileViewer.setLabelProvider(new FileSelectorLabelProvider());
		fileViewer.setInput(new File("workspace"));
		
		class DoubleClickListener implements IDoubleClickListener {
			private HC11337Controller controller;
			
			public DoubleClickListener(HC11337Controller controller)
			{
				this.controller = controller;
			}
			
			public void doubleClick(DoubleClickEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				File file = (File)selection.getFirstElement();
				if(getExtension(file).equals("asm")){
					controller.openFile(file);
					File binary = new File("workspace/"+getNameMinusExtension(file)+".s19");
					controller.loadBinary(binary);
				}else
					controller.loadBinary(file);
			}
			
			private String getNameMinusExtension(File file)
			{
				String nameExt = file.getName();
				String ext = getExtension(file);
				return nameExt.substring(0, nameExt.length()-ext.length()-1);
			}
			
			private String getExtension(File file)
			{
				String name = file.getName();
				String extension = "";
				int i = name.length()-1;
				do
				{
					extension = name.charAt(i) + extension;
					i--;
				}while(name.charAt(i) != '.');
				
				return extension.toLowerCase();
			}
		}
		
		fileViewer.addDoubleClickListener(new DoubleClickListener(controller));
		
		TableColumn column1 = new TableColumn(fileViewer.getTable(), SWT.LEFT);
		column1.setText("Source Files");
	}
}
