package ca.hc11337.app.core;

import java.util.*;
import java.io.*;
import java.awt.Point;

public class Editor {
	
	private String text = "";
	private Vector<String> changes = new Vector<String>();
	private SyntaxDictionary dict = new SyntaxDictionary("hc11.syn");
	private File file;
	
	public Editor(String name) throws FileNotFoundException
	{
		file = new File("workspace/"+name);
	}
	
	public Editor(File file) throws FileNotFoundException
	{
		this.file = file;
		Scanner fileScanner = new Scanner(file);
		while(fileScanner.hasNext())
		{
			text += fileScanner.nextLine()+'\n';
		}
	}
	
	public void setText(String text)
	{
		if(text.length() > 0){
			if(text.charAt(text.length()-1) != '\n')
				this.text = text + '\n';
			else
				this.text = text;
		}
	}
	
	public String getText()
	{
		return text;
	}
	
	public Vector<int[]> highlightSyntax()
	{
		Vector<int[]> points = new Vector<int[]>();
		StringTokenizer textScanner = new StringTokenizer(text);
		String temp = "";
		while(textScanner.hasNext())
		{
			String current = textScanner.next();
			if(dict.checkFor(current.toUpperCase()))
			{
				temp += current;
				int[] p = new int[3];
				p[0] = 1;
				p[1] = textScanner.getIndex()-current.length();
				p[2] = current.length();
				points.add(p);
			}
		}
		return points;
	}
	
	public int[] checkTokenAt(int index)
	{
		if(index == text.length())
			index--;
		StringTokenizer textScanner = new StringTokenizer(text);
		String token = textScanner.getTokenAt(index);
		int[] results = new int[3];
		results[0] = dict.checkFor(token.toUpperCase())? 1 : 0;
		results[1] = textScanner.getIndex()-token.length();
		results[2] = token.length();
		return results;
	}
	
	public void delete(int index, int length)
	{
		text = text.substring(0, index) + text.substring(length, text.length());
	}
	
	public File getFile()
	{
		return file;
	}
	
	public void undo()
	{
		
	}
	
	public void redo()
	{
		
	}
}
