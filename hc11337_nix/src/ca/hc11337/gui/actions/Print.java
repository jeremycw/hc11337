package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Print extends Action {
	public Print(HC11337Controller controller)
	{
		super("&Print...@Ctrl+P", AS_PUSH_BUTTON);
		setToolTipText("Print");
		Image descriptor = new Image(null, "icons/print.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Print...");
	}
	

}