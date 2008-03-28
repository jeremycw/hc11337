package ca.hc11337.gui.cpuview;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class CPUViewContentProvider implements IStructuredContentProvider {
	
	public Object[] getElements(Object arg0) {
		Object[] elements = new Object[((String[][])arg0)[0].length];
		for(int i = 0; i < ((String[][])arg0)[0].length; i++)
			elements[i] = new String[]{((String[][])arg0)[0][i], ((String[][])arg0)[1][i]};
		return elements;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

}
