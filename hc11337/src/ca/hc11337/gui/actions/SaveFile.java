package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class SaveFile extends Action {
	private HC11337Controller controller;
	
	public SaveFile(HC11337Controller controller)
	{
		super("&Save@Ctrl+S", AS_PUSH_BUTTON);
		setToolTipText("Save");
		Image descriptor = new Image(null, "icons/save.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
		this.controller = controller;
	}
	
	public void run()
	{
		controller.saveFile();
	}
	

}