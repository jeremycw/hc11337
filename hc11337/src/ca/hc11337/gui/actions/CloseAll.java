package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;

import ca.hc11337.gui.HC11337Controller;

public class CloseAll extends Action {
	private HC11337Controller controller;
	
	public CloseAll(HC11337Controller controller)
	{
		super("Close &All@Ctrl+Shift+W", AS_PUSH_BUTTON);
		setToolTipText("Close All");
		this.controller = controller;
		//Image descriptor = new Image(null, "icons/open.png");
		//setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		controller.closeAll();
	}
	

}