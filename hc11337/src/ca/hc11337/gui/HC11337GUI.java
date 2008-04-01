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

package ca.hc11337.gui;

import java.util.*;
import java.awt.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import ca.hc11337.gui.console.HC11337Console;
import ca.hc11337.gui.cpuview.HC11337CPUView;
import ca.hc11337.gui.editor.HC11337Editor;
import ca.hc11337.gui.fileselector.HC11337FileSelector;
import ca.hc11337.gui.memoryview.HC11337Memory;
import ca.hc11337.gui.stackview.HC11337StackView;
import ca.hc11337.gui.watchedmemory.HC11337WatchedMem;
import ca.hc11337.app.core.*;

public class HC11337GUI implements Observer {

	private HC11337FileSelector fileViewer;
	private Vector<HC11337Editor> editors = new Vector<HC11337Editor>();
	private HC11337Console console;
	private HC11337CPUView cpuView;
	private HC11337StackView stackView;
	private HC11337WatchedMem watchedMemView;
	private HC11337Memory mem;
	private TabFolder tabFolder;
	private TabFolder editFolder;
	private TabItem consoleTab;
	private TabItem memTab;
	private Composite parent;
	private SashForm sashMain;
	private SashForm sashEdit;
	private HC11337Controller controller;
	
	
	public void setUp(Composite parent, HC11337Controller controller)
	{
		this.parent = parent;
		this.controller = controller;
		createContents();
	}
	
	private void createContents()
	{
		sashMain = new SashForm(parent, SWT.HORIZONTAL | SWT.NULL);
		fileViewer = new HC11337FileSelector(sashMain, controller, SWT.BORDER | SWT.FULL_SELECTION);
		
		sashEdit = new SashForm(sashMain, SWT.VERTICAL | SWT.NULL);
		SashForm sashCPU = new SashForm(sashMain, SWT.VERTICAL | SWT.NULL);
		
		editFolder = new TabFolder (sashEdit, SWT.FLAT);
		
		//bottom tabs
		tabFolder = new TabFolder (sashEdit, SWT.FLAT);
		consoleTab = new TabItem (tabFolder, SWT.NONE);
		consoleTab.setText ("Console");
		memTab = new TabItem (tabFolder, SWT.NONE);
		memTab.setText ("Memory");
		
		console = new HC11337Console(tabFolder, SWT.BORDER | SWT.H_SCROLL| SWT.V_SCROLL);
		consoleTab.setControl(console.getConsole());
		
		mem = new HC11337Memory(tabFolder, SWT.SINGLE);
		memTab.setControl(mem.getMem());
		
		//tabFolder.pack ();
		
		cpuView = new HC11337CPUView(sashCPU, SWT.FULL_SELECTION);
		stackView = new HC11337StackView(sashCPU, SWT.FULL_SELECTION);
		watchedMemView = new HC11337WatchedMem(sashCPU, SWT.FULL_SELECTION);
		
		sashMain.setWeights(new int[] {1,3,1});
		sashEdit.setWeights(new int[] {5,3});
	}
	
	public void newEditorTab(String name)
	{
		editors.add(new HC11337Editor(editFolder, name, controller, SWT.MULTI));
		sashEdit.setWeights(new int[] {5,3});
	}
	
	public void removeEditorTab(int index)
	{
		editors.get(index).dispose();
		editors.remove(index);
	}
	
	public void setEditorText(String text)
	{
		editors.get(indexOfCurrentEditor()).setText(text);
	}
	
	public HC11337Editor getCurrentEditor()
	{
		return editors.get(indexOfCurrentEditor());
	}
	
	public int indexOfCurrentEditor()
	{
		return editFolder.getSelectionIndex();
	}
	
	public int getNumberOfTabs()
	{
		return editFolder.getItemCount();
	}
	
	public void switchEditor(int index)
	{
		editFolder.setSelection(index);
	}
	
	public void switchCurrentConsole(int index)
	{
		tabFolder.setSelection(index);
	}
	
	public String getConsoleText()
	{
		return console.getText();
	}
	
	public void setConsoleText(String text)
	{
		console.setText(text);
	}
	
	public void setCPUData(String[][] reg)
	{
		cpuView.setData(reg);
	}
	
	public void setMemData(int[] memory)
	{
		int[][] memDump = new int[4096][16];
		for(int i = 0; i < 4096; i++)
			for(int j = 0; j < 16; j++)
				memDump[i][j] = memory[i*0x10+j];
		mem.setData(memDump);
	}
	
	public void setCPURegister(int index, int value)
	{
		cpuView.setRegister(index, value);
	}
	
	public void displayStatusMessage()
	{
		
	}
	
	public void updateEditor(int index)
	{
		
	}
	
	public void updateConsole()
	{
		
	}
	
	public void updateMem(Vector<Point> updates)
	{
		
	}
	
	public void updateStack()
	{
		
	}
	
	public void updateCPU(int index)
	{
		
	}
	
	public int checkIfOpen(String name)
	{
		for(int i = 0; i < getNumberOfTabs(); i++)
		{
			if(editors.get(i).getName().equals(name))
				return i;
		}
		return -1;
	}
	
	public void update(Observable o, Object arg) {
		HC11337Editor editor = editors.get(indexOfCurrentEditor());
		
		switch((Integer)arg)
		{
		case 0:
			editor.setText(((HC11337Core)o).getText(indexOfCurrentEditor()));
			getCurrentEditor().highlightSyntax(((HC11337Core)o).getHighlightRanges(indexOfCurrentEditor()));
			break;
		case 1:
			getCurrentEditor().highlightSyntax(((HC11337Core)o).getHighlightRanges(indexOfCurrentEditor()));
			break;
		case 2:
			setMemData(((HC11337Core)o).getMemDump());
			break;
		}
	}

}
