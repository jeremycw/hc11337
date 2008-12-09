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
import java.io.*;
import java.awt.Point;

import ca.hc11337.app.core.*;

public class HC11337Controller implements Observer {

	private final HC11337GUI view;
	private HC11337Core model;
	private Vector<HC11337Core> models = new Vector<HC11337Core>();
	private int newFileCount = 0;
	
	public HC11337Controller(HC11337GUI gui, HC11337Core core)
	{
		view = gui;
		//model = core;
		//model.addObserver(view);
		//model.addObserver(this);
		//models.add(core);
	}
	
	/**
	 * Controls the "New File" user action
	 */
	public void newFile()
	{
		try{
			view.newEditorTab("Untitled-"+newFileCount+".asm");
			model = new HC11337Core("Untitled-"+newFileCount+".asm");
			model.addObserver(view);
			model.addObserver(this);
			view.switchEditor(view.getNumberOfTabs()-1);
			newFileCount++;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Controls the "Close File" user action
	 */
	public void closeFile()
	{
		int index = view.indexOfCurrentEditor();
		if(index >= 0)
		{
			view.removeEditorTab(index); 
			models.remove(index);
		}
	}
	
	/**
	 * Controls the "Close All" user action
	 */
	public void closeAll()
	{
		int numberOfTabs = view.getNumberOfTabs();
		for(int i = 0; i < numberOfTabs; i++)
		{
			view.removeEditorTab(0);
			models.remove(0);
		}
	}
	
	/**
	 * Loads a binary file into memory
	 * 
	 * @param file Binary file
	 */
	public void loadBinary(File file)
	{
		try{
			model.loadS19(file);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Controls the "Open File" user action
	 * 
	 * @param file Source file
	 */
	public void openFile(File file)
	{
		try{
			int tabNum = view.checkIfOpen(file.getName());
			if(tabNum >= 0)
				view.switchEditor(tabNum);
			else{
				view.newEditorTab(file.getName());
				view.switchEditor(view.getNumberOfTabs()-1);
				model = new HC11337Core(file);
				model.addObserver(view);
				view.clearMemView();
				model.refresh();
				//model.addObserver(this);
				//initCPUView();
				//initMemTab(); //slow needs speedup
				models.add(model);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Controls the "Save File" user action
	 */
	public void saveFile()
	{
		try{
			if(view.getNumberOfTabs() > 0){
				File file = model.getEditorFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(view.getCurrentEditor().getText());
				writer.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Controls the "Run" user action
	 */
	public void run()
	{
		view.setCPURegister(0, 12);
	}
	
	/**
	 * Controls the "Step" user action
	 */
	public void step()
	{
		model.execute();
	}
	
	/**
	 * Controls the "Stop" user action
	 */
	public void stop()
	{
		
	}
	
	/**
	 * Controls the "Delete" user action
	 */
	public void delete()
	{
		
	}
	
	/**
	 * Controls the "Cut" user action
	 */
	public void cut()
	{
		if(view.getNumberOfTabs() > 0)
			if(view.getCurrentEditor().hasSelection())
			{
				view.getCurrentEditor().cut();
				model.setText(view.getCurrentEditor().getText());
				highlightAtCaret();
			}
	}
	
	/**
	 * Controls the "Copy" user action
	 */
	public void copy()
	{
		if(view.getNumberOfTabs() > 0)
			view.getCurrentEditor().copy();
	}
	
	/**
	 * Controls the "Paste" user action
	 */
	public void paste()
	{
		if(view.getNumberOfTabs() > 0){
			view.getCurrentEditor().paste();
			model.setText(view.getCurrentEditor().getText());
			highlightSyntax();
		}
	}
	
	/**
	 * Controls the "Reset" user action
	 */
	public void reset()
	{
		model.setRegisterValue(6, 0);
		model.setRegisterValue(5, 0xC000);
	}
	
	/**
	 * Controls the "Deep Reset" user action
	 */
	public void deepReset()
	{
		
	}
	
	/**
	 * Controls the "Undo" user action
	 */
	public void undo()
	{
		// TODO undo function
	}
	
	/**
	 * Controls the "Redo" user action
	 */
	public void redo()
	{
		// TODO redo function
	}
	
	/**
	 * Controls the "Build" user action
	 */
	public void build()
	{
		saveFile();
		view.switchCurrentConsole(0);
		try{
			if(view.getNumberOfTabs() > 0)
			{
				String file = model.getEditorFile().getAbsolutePath();
				String cmdarray[] = new String[2];
				cmdarray[0] = "./as11";
				cmdarray[1] = file;
				Process asm = Runtime.getRuntime().exec(cmdarray);
				Scanner console = new Scanner(asm.getInputStream());
				view.setConsoleText("");
				while(console.hasNext())
					view.setConsoleText(view.getConsoleText()+console.nextLine()+'\n');
			}
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	/**
	 * Highlights syntax around the caret (as you type highlighting)
	 */
	public void highlightAtCaret()
	{
		model.setText(view.getCurrentEditor().getText());
		if(model.getText().length() != 0){
			int[] array1 = model.getHighlightAt(view.getCurrentEditor().getCaretPosition());
			int[] array2;
			Vector<int[]> v = new Vector<int[]>();
			if(view.getCurrentEditor().getCaretPosition()-2 >= 0){
				array2 = model.getHighlightAt(view.getCurrentEditor().getCaretPosition()-2);
				v.add(array2);
			}
			v.add(array1);
			view.getCurrentEditor().highlightSyntax(v);
		}
	}
	
	/**
	 * Highlights syntax in the entire file
	 */
	public void highlightSyntax()
	{
		//view.getCurrentEditor().setText(model.getText(view.indexOfCurrentEditor()));
		view.getCurrentEditor().highlightSyntax(model.getHighlightRanges());
	}
	
	/**
	 * Controls the "Select All" user action
	 */
	public void selectAll()
	{
		view.getCurrentEditor().selectAll();
	}
	
	/**
	 * Sets up the CPU GUI
	 */
	public void initCPUView()
	{
		String regNames[] = model.getRegisterNames();
		int regValues[] = model.getRegisterValues();
		String[][] regs = new String[2][regNames.length];
		regs[0] = regNames;
		for(int i = 0; i < regValues.length; i++)
			regs[1][i] = Integer.toHexString(regValues[i]);
		view.setCPUData(regs);
	}
	
	/**
	 * Sets up the Memory Tab GUI
	 */
	public void initMemTab()
	{
		view.setMemData(model.getMemDump());
	}
	
	public void switchModel(int index)
	{
		model = models.get(index);
		view.clearMemView();
		model.refresh();
	}
	
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
