package ca.hc11337.gui.actions;

import org.eclipse.jface.action.Action;

import ca.hc11337.gui.HC11337Controller;

public class SelectAll extends Action {
	public SelectAll(HC11337Controller controller)
	{
		super("&Select All@Ctrl+A", AS_PUSH_BUTTON);
		setToolTipText("Select All");
		//Image descriptor = new Image(null, "icons/open.png");
		//setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Select All");
	}
	

}