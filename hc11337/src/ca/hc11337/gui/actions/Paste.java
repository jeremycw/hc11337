package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Paste extends Action {
	private HC11337Controller controller;
	
	public Paste(HC11337Controller controller)
	{
		super("&Paste@Ctrl+V", AS_PUSH_BUTTON);
		setToolTipText("Paste");
		Image descriptor = new Image(null, "icons/paste.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
		this.controller = controller;
	}
	
	public void run()
	{
		controller.paste();
	}
	

}