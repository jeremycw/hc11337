package ca.hc11337.gui.actions;

import org.eclipse.jface.action.Action;

import ca.hc11337.gui.HC11337Controller;

public class SelectAll extends Action {
	private HC11337Controller controller;
	
	public SelectAll(HC11337Controller controller)
	{
		super("&Select All@Ctrl+A", AS_PUSH_BUTTON);
		setToolTipText("Select All");
		//Image descriptor = new Image(null, "icons/open.png");
		//setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
		this.controller = controller;
	}
	
	public void run()
	{
		controller.selectAll();
	}
	

}