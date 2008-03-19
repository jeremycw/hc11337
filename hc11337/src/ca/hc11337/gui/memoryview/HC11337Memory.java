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
