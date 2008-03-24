package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Undo extends Action {
	public Undo(HC11337Controller controller)
	{
		super("&Undo@Ctrl+Z", AS_PUSH_BUTTON);
		setToolTipText("Undo");
		Image descriptor = new Image(null, "icons/undo.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		System.out.println("Undo");
	}
	

}