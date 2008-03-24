package ca.hc11337.gui.menubar;

import org.eclipse.jface.action.*;

import ca.hc11337.gui.*;


public class HC11337Menu implements Managed
{
	private MenuManager menu = new MenuManager(null);
	
	public HC11337Menu(HC11337Controller controller)
	{
		FileMenu fileMenu = new FileMenu(controller);
		EditMenu editMenu = new EditMenu(controller);
		WorkspaceMenu workspaceMenu = new WorkspaceMenu(controller);
		RunMenu runMenu = new RunMenu(controller);
		OptionsMenu optionsMenu = new OptionsMenu(controller);
		AboutMenu aboutMenu = new AboutMenu(controller);
		
		menu.add((MenuManager)fileMenu.getManager());
		menu.add((MenuManager)editMenu.getManager());
		menu.add((MenuManager)workspaceMenu.getManager());
		menu.add((MenuManager)runMenu.getManager());
		menu.add((MenuManager)optionsMenu.getManager());
		menu.add((MenuManager)aboutMenu.getManager());
	}
	
	public ContributionManager getManager()
	{
		return menu;
	}
	
}
