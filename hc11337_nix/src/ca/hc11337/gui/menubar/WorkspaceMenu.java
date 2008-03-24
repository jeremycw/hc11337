package ca.hc11337.gui.menubar;


import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.MenuManager;

import ca.hc11337.gui.HC11337Controller;
import ca.hc11337.gui.Managed;

public class WorkspaceMenu implements Managed{
	private MenuManager menu = new MenuManager("Workspace");
	public WorkspaceMenu(HC11337Controller controller)
	{
		//MenuManager workspaceMenuManager = new MenuManager("Workspace");
		//workspaceMenuManager.fill(bar, index);
	}
	
	public ContributionManager getManager()
	{
		return menu;
	}
}
