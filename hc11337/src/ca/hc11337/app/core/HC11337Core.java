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

package ca.hc11337.app.core;

import java.util.*;
import java.io.*;
import java.awt.Point;

public class HC11337Core extends Observable {
	private Editor editor;
	private ConsoleManager consoleManager = new ConsoleManager();
	private HardwareAPI api = new HardwareAPI();
	
	public HC11337Core(File file) throws FileNotFoundException
	{
		editor = new Editor(file);
	}
	
	public HC11337Core(String name) throws FileNotFoundException
	{
		editor = new Editor(name);
	}
	
	//Editor stuff
	
	/**
	 * Creates a new empty editor
	 * 
	 */
	/*public void newEditor(String name) throws FileNotFoundException
	{
		editor = new Editor(name);
	}*/
	
	/**
	 * Close an editor
	 * 
	 * @param index Index of the editor tab
	 */
	/*public void removeEditor(int index)
	{
		editors.remove(index);
	}*/
	
	/**
	 * Create a new editor and load it with file
	 * 
	 * @param file File to be loaded into the editor
	 * @throws FileNotFoundException
	 */
	/*public void newEditor(File file) throws FileNotFoundException
	{
		editors.add(new Editor(file));
		setChanged();
		notifyObservers(0);
	}*/
	
	/**
	 * Get the text of the editor
	 * 
	 * @param index Index of the editor tab
	 * @return Text from the editor
	 */
	public String getText()
	{
		return editor.getText();
	}
	
	/**
	 * Set the text of the editor
	 * 
	 * @param text
	 * @param index
	 */
	public void setText(String text)
	{
		int sizeDif = getText().length() - text.length();
		editor.setText(text);
		setChanged();
		if(Math.abs(sizeDif) > 1)
			notifyObservers(1);
	}
	
	public Vector<int[]> getHighlightRanges()
	{
		return editor.highlightSyntax();
	}
	
	public int[] getHighlightAt(int offset)
	{
		return editor.checkTokenAt(offset);
	}
	
	/**
	 * Get the file that is open in an editor
	 * 
	 * @param index The index of the tab that contains the editor
	 * @return The file that is open in that editor
	 */
	public File getEditorFile()
	{
		return editor.getFile();
	}
	
	//Sim stuff
	public String[] getRegisterNames()
	{
		return api.getRegisterNames();
	}
	
	/**
	 * Get all register values
	 * 
	 * @return Dump of all register values
	 */
	public int[] getRegisterValues()
	{
		return api.getRegisterValues();
	}
	
	/**
	 * Set the value of a CPU register
	 * 
	 * @param index use one of the constants defined in Reg to specify which register to set
	 * @param value The value the register will change to
	 */
	public void setRegisterValue(int index, int value)
	{
		api.setRegister(index, value);
		setChanged();
		notifyObservers(3);
	}
	
	/**
	 * Load an S19 file into memory
	 * 
	 * @param file .s19 file
	 * @throws FileNotFoundException
	 */
	public void loadS19(File file) throws FileNotFoundException
	{
		S19Processor processor = new S19Processor(file);
		do
		{
			int addr = processor.nextAddress();
			int bc = processor.nextByteCount();
			for(int i = 0; i < bc; i++)
			{
				int newByte = processor.nextByte();
				api.setMemoryAt(addr, newByte);
				addr++;
			}
		}while(processor.hasNextLine());
		setChanged();
		notifyObservers(2);
	}
	
	public int[] getMemChanges()
	{
		return api.getChangedMemoryValues();
	}
	
	/**
	 * Get all values from memory
	 * 
	 * @return Dump of memory
	 */
	public Hashtable<Integer, Integer> getMemDump()
	{
		return api.getUsedMemory();
	}
	
	/**
	 * Executes the next instruction
	 */
	public void execute()
	{
		api.execute();
		setChanged();
		notifyObservers(3);
	}
	
	/**
	 * Get a value from memory
	 * 
	 * @param index
	 * @return Value in memory at index
	 */
	public int getMemoryAt(int index)
	{
		return api.getMemoryAt(index);
	}
	
	public void addObserver(Observer o)
	{
		super.addObserver(o);
		//setChanged();
		//notifyObservers(4);
	}
	
	public void refresh()
	{
		setChanged();
		notifyObservers(4);
	}
	
	
}
