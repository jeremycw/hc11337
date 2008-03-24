package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Cut extends Action {
	private HC11337Controller controller;
	
	public Cut(HC11337Controller controller)
	{
		super("C&ut@Ctrl+X", AS_PUSH_BUTTON);
		setToolTipText("Cut");
		Image descriptor = new Image(null, "icons/cut.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
		this.controller = controller;
	}
	
	public void run()
	{
		controller.cut();
	}
	

}