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

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import java.io.*;
import java.util.*;

public class FileSelectorLabelProvider implements ITableLabelProvider {
	private Image text = new Image(null, "icons/text.png");
	private Image binary = new Image(null, "icons/binary.png");
	
	List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>();
	
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
	
	public Image getColumnImage(Object arg0, int arg1) {
		if(getExtension((File)arg0).equals("asm"))
			return text;
		if(getExtension((File)arg0).equals("s19"))
			return binary;
		else
			return text;
	}

	public String getColumnText(Object arg0, int arg1) {
		String name = ((File)arg0).getName();
		String ext = getExtension((File)arg0);
		return name.substring(0, name.length()-ext.length()-1);
	}

	public void addListener(ILabelProviderListener arg0) {
		listeners.add(arg0);

	}

	public void dispose() {
		text.dispose();
		binary.dispose();
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
		listeners.remove(arg0);

	}
}
