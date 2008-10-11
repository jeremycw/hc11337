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
	
	/**
	 * Draws the GUI
	 */
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
	
	/**
	 * Creates a new editor tab
	 * 
	 * @param name Label for the tab
	 */
	public void newEditorTab(String name)
	{
		editors.add(new HC11337Editor(editFolder, name, controller, SWT.MULTI));
		sashEdit.setWeights(new int[] {5,3});
	}
	
	/**
	 * Removes an editor tab
	 * 
	 * @param index Index of the tab
	 */
	public void removeEditorTab(int index)
	{
		editors.get(index).dispose();
		editors.remove(index);
	}
	
	/**
	 * Sets the display text in the editor text box
	 * 
	 * @param text
	 */
	public void setEditorText(String text)
	{
		editors.get(indexOfCurrentEditor()).setText(text);
	}
	
	/**
	 * Gets the editor that is currently selected
	 * 
	 * @return View of the editor
	 */
	public HC11337Editor getCurrentEditor()
	{
		return editors.get(indexOfCurrentEditor());
	}
	
	/**
	 * Gets the index of the current editor
	 * 
	 * @return Index
	 */
	public int indexOfCurrentEditor()
	{
		return editFolder.getSelectionIndex();
	}
	
	/**
	 * Gets the total number of open editor tabs
	 * 
	 * @return Number of tabs
	 */
	public int getNumberOfTabs()
	{
		return editFolder.getItemCount();
	}
	
	/**
	 * Switches the current editor
	 * 
	 * @param index Index of the editor to switch to
	 */
	public void switchEditor(int index)
	{
		editFolder.setSelection(index);
	}
	
	/**
	 * Switch between the console tab and the memory tab
	 * 
	 * @param index
	 */
	public void switchCurrentConsole(int index)
	{
		tabFolder.setSelection(index);
	}
	
	/**
	 * Get the text from the console
	 * 
	 * @return Console text
	 */
	public String getConsoleText()
	{
		return console.getText();
	}
	
	/**
	 * Sets the console text
	 * 
	 * @param text
	 */
	public void setConsoleText(String text)
	{
		console.setText(text);
	}
	
	/**
	 * Sets the values displayed in the CPU view
	 * 
	 * @param reg 2D string array with register names in the first column and the values in the second
	 */
	public void setCPUData(String[][] reg)
	{
		cpuView.setData(reg);
	}
	
	/**
	 * Sets the values displayed in the memory tab
	 * 
	 * @param memory All memory values
	 */
	public void setMemData(int[] memory)
	{
		int[][] memDump = new int[4096][16];
		for(int i = 0; i < 4096; i++)
			for(int j = 0; j < 16; j++)
				memDump[i][j] = memory[i*0x10+j];
		mem.setData(memDump);
	}
	
	/**
	 * Sets the displayed value of a specific cpu register
	 * 
	 * @param index
	 * @param value
	 */
	public void setCPURegister(int index, int value)
	{
		cpuView.setRegister(index, value);
	}
	
	public void displayStatusMessage()
	{
		
	}
	
	/**
	 * Checks if an editor already exists
	 * 
	 * @param name Label of the tab to look for
	 * @return Index of the tab if found -1 if not
	 */
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
		case 3:
			String regNames[] = ((HC11337Core)o).getRegisterNames();
			int regValues[] = ((HC11337Core)o).getRegisterValues();
			String[][] regs = new String[2][regNames.length];
			regs[0] = regNames;
			for(int i = 0; i < regValues.length; i++)
				regs[1][i] = Integer.toHexString(regValues[i]);
			setCPUData(regs);
			int[] addr = ((HC11337Core)o).getMemChanges();
			for(int i = 0; i < addr.length; i++)
				mem.setMemoryCell(addr[i]/16, addr[i]%16, ((HC11337Core)o).getMemoryAt(addr[i]));
			//setMemData(((HC11337Core)o).getMemDump());
			break;
		}
	}

}
