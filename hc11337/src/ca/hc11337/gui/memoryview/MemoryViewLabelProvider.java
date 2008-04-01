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

package ca.hc11337.gui.memoryview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class MemoryViewLabelProvider implements ITableLabelProvider {
	private int itemCount = 0;
	
	List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>();
	
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	public String getColumnText(Object arg0, int arg1) {
		if(itemCount > 65520)
			itemCount = 0;
		switch(arg1)
		{
		case 0:
			String str = Integer.toHexString(itemCount);
			while(str.length() < 4)
				str = "0"+str;
			itemCount+=0x10;
			return str;
		case 1:
			return Integer.toHexString(((int[])arg0)[0]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[0]) :
						Integer.toHexString(((int[])arg0)[0]);
		case 2:
			return Integer.toHexString(((int[])arg0)[1]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[1]) :
						Integer.toHexString(((int[])arg0)[1]);
		case 3:
			return Integer.toHexString(((int[])arg0)[2]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[2]) :
						Integer.toHexString(((int[])arg0)[2]);
		case 4:
			return Integer.toHexString(((int[])arg0)[3]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[3]) :
						Integer.toHexString(((int[])arg0)[3]);
		case 5:
			return Integer.toHexString(((int[])arg0)[4]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[4]) :
						Integer.toHexString(((int[])arg0)[4]);
		case 6:
			return Integer.toHexString(((int[])arg0)[5]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[5]) :
						Integer.toHexString(((int[])arg0)[5]);
		case 7:
			return Integer.toHexString(((int[])arg0)[6]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[6]) :
						Integer.toHexString(((int[])arg0)[6]);
		case 8:
			return Integer.toHexString(((int[])arg0)[7]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[7]) :
						Integer.toHexString(((int[])arg0)[7]);
		case 9:
			return Integer.toHexString(((int[])arg0)[8]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[8]) :
						Integer.toHexString(((int[])arg0)[8]);
		case 10:
			return Integer.toHexString(((int[])arg0)[9]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[9]) :
						Integer.toHexString(((int[])arg0)[9]);
		case 11:
			return Integer.toHexString(((int[])arg0)[10]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[10]) :
						Integer.toHexString(((int[])arg0)[10]);
		case 12:
			return Integer.toHexString(((int[])arg0)[11]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[11]) :
						Integer.toHexString(((int[])arg0)[11]);
		case 13:
			return Integer.toHexString(((int[])arg0)[12]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[12]) :
						Integer.toHexString(((int[])arg0)[12]);
		case 14:
			return Integer.toHexString(((int[])arg0)[13]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[13]) :
						Integer.toHexString(((int[])arg0)[13]);
		case 15:
			return Integer.toHexString(((int[])arg0)[14]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[14]) :
						Integer.toHexString(((int[])arg0)[14]);
		case 16:
			return Integer.toHexString(((int[])arg0)[15]).length() == 1?
					"0"+Integer.toHexString(((int[])arg0)[15]) :
						Integer.toHexString(((int[])arg0)[15]);
		}
		return "Fuck!";
	}

	public void addListener(ILabelProviderListener arg0) {
		listeners.add(arg0);

	}

	public void dispose() {

	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
		listeners.remove(arg0);

	}

}
