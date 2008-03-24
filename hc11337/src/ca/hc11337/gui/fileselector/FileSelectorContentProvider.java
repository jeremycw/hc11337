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
