package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;

import ca.hc11337.gui.HC11337Controller;

public class SaveAs extends Action {
	public SaveAs(HC11337Controller controller)
	{
		super("Sa&ve As...", AS_PUSH_BUTTON);
		setToolTipText("Save As");
		//Image descriptor = new Image(null, "icons/open.png");
		//setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Save As...");
	}
	

}