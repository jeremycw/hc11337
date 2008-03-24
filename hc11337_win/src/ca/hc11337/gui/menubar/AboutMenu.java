package ca.hc11337.gui.menubar;


import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;

import ca.hc11337.gui.*;
import ca.hc11337.gui.actions.*;


public class AboutMenu implements Managed {
	private MenuManager menu = new MenuManager("&About");
	public AboutMenu(HC11337Controller controller)
	{
		IAction aboutAction = new About(controller);
		menu.add(aboutAction);
		//MenuManager aboutMenuManager = new MenuManager("About");
		//aboutMenuManager.fill(bar, index);
	}
	
	public ContributionManager getManager()
	{
		return menu;
	}
}
