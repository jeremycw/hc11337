package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class OpenFile extends Action {
	public OpenFile(HC11337Controller controller)
	{
		super("&Open File...@Ctrl+O", AS_PUSH_BUTTON);
		setToolTipText("Open");
		Image descriptor = new Image(null, "icons/open.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Open File...");
	}
	

}
