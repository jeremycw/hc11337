package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Options extends Action {
	public Options(HC11337Controller controller)
	{
		super("&Options...", AS_PUSH_BUTTON);
		setToolTipText("Options");
		Image descriptor = new Image(null, "icons/options.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Options...");
	}
	

}
