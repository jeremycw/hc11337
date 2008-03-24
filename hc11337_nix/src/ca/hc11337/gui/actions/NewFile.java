package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;
import java.io.*;

import ca.hc11337.gui.HC11337Controller;

public class NewFile extends Action {
	private HC11337Controller controller;
	
	public NewFile(HC11337Controller controller)
	{
		super("&New@Ctrl+N", AS_PUSH_BUTTON);
		setToolTipText("New");
		Image descriptor = new Image(null, "icons/new.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
		this.controller = controller;
	}
	
	public void run()
	{
		try
		{
		controller.newFile();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

}