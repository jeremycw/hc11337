package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Step extends Action {
	public Step(HC11337Controller controller)
	{
		super("&Step@F10", AS_PUSH_BUTTON);
		setToolTipText("Step");
		Image descriptor = new Image(null, "icons/step.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Step");
	}
	

}