package ca.hc11337.gui.actions;

import org.eclipse.jface.action.Action;

import ca.hc11337.gui.HC11337Controller;

public class Redo extends Action {
	public Redo(HC11337Controller controller)
	{
		super("&Redo@Ctrl+Y", AS_PUSH_BUTTON);
		setToolTipText("Redo");
		//Image descriptor = new Image(null, "icons/open.png");
		//setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Redo");
	}
	

}