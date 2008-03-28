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

package ca.hc11337.gui.fileselector;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import java.io.*;
import java.util.*;

public class FileSelectorContentProvider implements IStructuredContentProvider {
	private Vector<File> fileVector = new Vector<File>();
	private Vector<String> nameVector = new Vector<String>();
	
	public Object[] getElements(Object arg0) {
		File[] files = ((File)arg0).listFiles();
		for(int i = 0; i < files.length; i++)
		{
			String ext = getExtension(files[i]);
			if(ext.equals("asm"))
			{
				fileVector.add(files[i]);
				nameVector.add(getNameMinusExtension(files[i]));
			}
		}
		Collections.sort(nameVector);
		
		for(int i = 0; i < files.length; i++)
		{
			String name = getNameMinusExtension(files[i]);
			String ext = getExtension(files[i]);
			if(ext.equals("s19") && !(Collections.binarySearch(nameVector, name) >= 0))
				fileVector.add(files[i]);
		}
		
		return fileVector.toArray();
	}
	
	private String getNameMinusExtension(File file)
	{
		String nameExt = file.getName();
		String ext = getExtension(file);
		return nameExt.substring(0, nameExt.length()-ext.length()-1);
	}
	
	private String getExtension(File file)
	{
		String name = file.getName();
		String extension = "";
		int i = name.length()-1;
		do
		{
			extension = name.charAt(i) + extension;
			i--;
		}while(name.charAt(i) != '.');
		
		return extension.toLowerCase();
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

}
