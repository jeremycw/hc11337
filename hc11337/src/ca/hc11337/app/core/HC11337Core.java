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
	private Vector<Editor> editors = new Vector<Editor>();
	private StackManager stackManager = new StackManager();
	private MemoryManager memManager = new MemoryManager();
	private CPUManager cpuManager = new CPUManager();
	private WatchedMemManager watchedMemManager = new WatchedMemManager();
	private ConsoleManager consoleManager = new ConsoleManager();
	private HardwareAPI api = new HardwareAPI();
	
	public HC11337Core()
	{
		cpuManager.setRegisterNames(api.getRegisterNames());
	}
	
	//Editor stuff
	public void newEditor(String name) throws FileNotFoundException
	{
		editors.add(new Editor(name));
	}
	
	public void removeEditor(int index)
	{
		editors.remove(index);
	}
	
	public void newEditor(File file) throws FileNotFoundException
	{
		editors.add(new Editor(file));
		setChanged();
		notifyObservers(0);
	}
	
	public String getText(int index)
	{
		return editors.get(index).getText();
	}
	
	public void setText(String text, int index)
	{
		int sizeDif = getText(index).length() - text.length();
		editors.get(index).setText(text);
		setChanged();
		if(Math.abs(sizeDif) > 1)
			notifyObservers(1);
	}
	
	public Vector<int[]> getHighlightRanges(int index)
	{
		return editors.get(index).highlightSyntax();
	}
	
	public int[] getHighlightAt(int index, int offset)
	{
		return editors.get(index).checkTokenAt(offset);
	}
	
	public File getEditorFile(int index)
	{
		return editors.get(index).getFile();
	}
	
	//Sim stuff
	public String[] getRegisterNames()
	{
		return cpuManager.getRegisterNames();
	}
	
	public int[] getRegisterValues()
	{
		return cpuManager.getRegisterValues();
	}
	
	public void setRegisterValue(int index, int value)
	{
		cpuManager.setRegisterValue(index, value);
	}
	
	public void loadS19(File file) throws FileNotFoundException
	{
		S19Processor processor = new S19Processor(file);
		while(processor.hasNextLine())
		{
			int addr = processor.nextAddress();
			int bc = processor.nextByteCount();
			for(int i = 0; i < bc; i++)
			{
				int newByte = processor.nextByte();
				api.setMemoryAt(addr, newByte);
				addr++;
			}
		}
		setChanged();
		notifyObservers(2);
	}
	
	public int[] getMemDump()
	{
		int[] memDump = new int[65536];
		for(int i = 0; i < 65536; i++)
				memDump[i] = api.getMemoryAt(i);
		return memDump;
	}
	
	
}
