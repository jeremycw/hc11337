package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Build extends Action {
	private HC11337Controller controller;
	
	public Build(HC11337Controller controller)
	{
		super("&Build@F11", AS_PUSH_BUTTON);
		setToolTipText("Build");
		Image descriptor = new Image(null, "icons/build.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
		this.controller = controller;
	}
	
	public void run()
	{
		controller.build();
	}
	

}