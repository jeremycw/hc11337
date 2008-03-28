package ca.hc11337.gui.cpuview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class CPUViewLabelProvider implements ITableLabelProvider {
	private Image text = new Image(null, "icons/text.png");
	private Image binary = new Image(null, "icons/binary.png");
	
	List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>();
	
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	public String getColumnText(Object arg0, int arg1) {
		switch(arg1)
		{
		case 0:
			return ((String[])arg0)[0];
		case 1:
			return ((String[])arg0)[1];
		}
		return "Boo";
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
