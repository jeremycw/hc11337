package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;

import ca.hc11337.gui.HC11337Controller;

public class ResetDeep extends Action {
	public ResetDeep(HC11337Controller controller)
	{
		super("&Deep Reset@Ctrl+Shift+F7", AS_PUSH_BUTTON);
		setToolTipText("Deep Reset");
	}
	
	public void run()
	{
		System.out.println("Run");
	}
	

}
