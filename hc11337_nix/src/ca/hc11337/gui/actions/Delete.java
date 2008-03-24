package ca.hc11337.gui.actions;

import org.eclipse.jface.action.Action;

import ca.hc11337.gui.HC11337Controller;

public class Delete extends Action {
	public Delete(HC11337Controller controller)
	{
		super("&Delete", AS_PUSH_BUTTON);
		setToolTipText("Delete");
		//Image descriptor = new Image(null, "icons/open.png");
		//setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Delete");
	}
	

}