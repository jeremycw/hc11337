package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Run extends Action {
	private HC11337Controller controller;
	
	public Run(HC11337Controller controller)
	{
		super("&Run@F8", AS_PUSH_BUTTON);
		setToolTipText("Run");
		Image descriptor = new Image(null, "icons/run.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
		this.controller = controller;
	}
	
	public void run()
	{
		controller.highlightAtCaret();
	}
	

}