package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;

import ca.hc11337.gui.HC11337Controller;

public class CloseFile extends Action {
	private HC11337Controller controller;
	
	public CloseFile(HC11337Controller controller)
	{
		super("&Close@Ctrl+W", AS_PUSH_BUTTON);
		setToolTipText("Close");
		this.controller = controller;
		//Image descriptor = new Image(null, "icons/open.png");
		//setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		controller.closeFile();
	}
	

}


