package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Reset extends Action {
	public Reset(HC11337Controller controller)
	{
		super("R&eset       @F7", AS_PUSH_BUTTON);
		setToolTipText("Reset");
		Image descriptor = new Image(null, "icons/refresh.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Run");
	}
	

}
