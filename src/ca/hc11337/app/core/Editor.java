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
