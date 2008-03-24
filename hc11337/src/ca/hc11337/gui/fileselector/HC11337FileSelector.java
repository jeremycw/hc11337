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
				controller.openFile(file);
			}
		}
		
		fileViewer.addDoubleClickListener(new DoubleClickListener(controller));
		
		TableColumn column1 = new TableColumn(fileViewer.getTable(), SWT.LEFT);
		column1.setText("Source Files");
	}
}
