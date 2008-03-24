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