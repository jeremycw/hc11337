package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Copy extends Action {
	private HC11337Controller controller;
	
	public Copy(HC11337Controller controller)
	{
		super("&Copy@Ctrl+C", AS_PUSH_BUTTON);
		setToolTipText("Copy");
		Image descriptor = new Image(null, "icons/copy.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
		this.controller = controller;
	}
	
	public void run()
	{
		controller.copy();
	}
	

}