package ca.hc11337.gui.actions;

import org.eclipse.jface.action.Action;

import ca.hc11337.gui.HC11337Controller;

public class Exit extends Action {
	public Exit(HC11337Controller controller)
	{
		super("E&xit", AS_PUSH_BUTTON);
		setToolTipText("Exit");
		//Image descriptor = new Image(null, "icons/open.png");
		//setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Exit");
	}
	

}