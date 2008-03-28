package ca.hc11337.gui.cpuview;

import java.util.Vector;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;

public class HC11337CPUView {
	private final TableViewer cpuViewer;
	private String[][] data;
	
	public HC11337CPUView(Composite parent, int style)
	{
		Group cpu = new Group(parent, 0);
		cpu.setText("CPU");
		cpu.setLayout(new FillLayout());
		cpuViewer = new TableViewer(cpu, style);
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(40, false));
		layout.addColumnData(new ColumnWeightData(60, true));
		cpuViewer.getTable().setLayout(layout);
		cpuViewer.getTable().setLinesVisible(true);
		cpuViewer.getTable().setHeaderVisible(true);
		cpuViewer.getTable().setToolTipText("CPU");
		cpuViewer.setContentProvider(new CPUViewContentProvider());
		cpuViewer.setLabelProvider(new CPUViewLabelProvider());
		
		TableColumn column1 = new TableColumn(cpuViewer.getTable(), SWT.CENTER);
		column1.setText("Register");
		TableColumn column2 = new TableColumn(cpuViewer.getTable(), SWT.LEFT);
		column2.setText("Value");
	}
	
	public void setData(String[][] registers)
	{
		data = registers;
		cpuViewer.setInput(data);
	}
	
	public void setRegister(int index, int value)
	{
		data[1][index] = Integer.toHexString(value);
		cpuViewer.refresh();
	}
}