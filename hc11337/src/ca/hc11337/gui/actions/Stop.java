package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Stop extends Action {
	public Stop(HC11337Controller controller)
	{
		super("Sto&p@F9", AS_PUSH_BUTTON);
		setToolTipText("Stop");
		Image descriptor = new Image(null, "icons/stop.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Stop");
	}
	

}