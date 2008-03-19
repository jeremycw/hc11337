package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;

import ca.hc11337.gui.*;

public class About extends Action {
	public About(HC11337Controller controller)
	{
		super("&About...", AS_PUSH_BUTTON);
		setToolTipText("About");
		//Image descriptor = new Image(null, "icons/open.png");
		//setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("About...");
	}
	

}
