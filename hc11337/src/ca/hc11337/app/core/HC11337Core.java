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
	
	public void newEditor() throws FileNotFoundException
	{
		editors.add(new Editor());
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
}
