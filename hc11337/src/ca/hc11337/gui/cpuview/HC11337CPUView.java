package ca.hc11337.gui.cpuview;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class HC11337CPUView {
	
	public HC11337CPUView(Composite parent, int style)
	{
		Group cpu = new Group(parent, 0);
		cpu.setText("CPU");
		cpu.setLayout(new FillLayout());
		final TableViewer cpuViewer = new TableViewer(cpu, 0);
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(40, false));
		layout.addColumnData(new ColumnWeightData(60, true));
		cpuViewer.getTable().setLayout(layout);
		cpuViewer.getTable().setLinesVisible(true);
		cpuViewer.getTable().setHeaderVisible(true);
		cpuViewer.getTable().setToolTipText("CPU");
		
		TableColumn column1 = new TableColumn(cpuViewer.getTable(), SWT.CENTER);
		column1.setText("Register");
		TableColumn column2 = new TableColumn(cpuViewer.getTable(), SWT.LEFT);
		column2.setText("Value");
	}
}