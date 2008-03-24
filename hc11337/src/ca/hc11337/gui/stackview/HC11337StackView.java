package ca.hc11337.gui.stackview;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class HC11337StackView {
	
	public HC11337StackView(Composite parent, int style)
	{
		Group stack = new Group(parent, 0);
		stack.setText("Stack");
		stack.setLayout(new FillLayout());
		final TableViewer stackViewer = new TableViewer(stack, 0);
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(40, false));
		layout.addColumnData(new ColumnWeightData(60, true));
		stackViewer.getTable().setLayout(layout);
		stackViewer.getTable().setLinesVisible(true);
		stackViewer.getTable().setHeaderVisible(true);
		stackViewer.getTable().setToolTipText("Stack");
		
		TableColumn column1 = new TableColumn(stackViewer.getTable(), SWT.CENTER);
		column1.setText("Address");
		TableColumn column2 = new TableColumn(stackViewer.getTable(), SWT.LEFT);
		column2.setText("Value");
	}
}