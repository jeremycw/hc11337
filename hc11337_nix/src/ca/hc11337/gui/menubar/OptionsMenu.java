package ca.hc11337.gui.menubar;


import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;

import ca.hc11337.gui.HC11337Controller;
import ca.hc11337.gui.Managed;
import ca.hc11337.gui.actions.*;


public class OptionsMenu implements Managed{
	private MenuManager menu = new MenuManager("&Options");
	public OptionsMenu(HC11337Controller controller)
	{
		IAction optionsAction = new Options(controller);
		menu.add(optionsAction);
		//MenuManager aboutMenuManager = new MenuManager("About");
		//aboutMenuManager.fill(bar, index);
	}
	
	public ContributionManager getManager()
	{
		return menu;
	}
}
