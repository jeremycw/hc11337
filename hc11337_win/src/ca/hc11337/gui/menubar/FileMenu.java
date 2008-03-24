package ca.hc11337.gui.menubar;


import org.eclipse.jface.action.*;

import ca.hc11337.gui.HC11337Controller;
import ca.hc11337.gui.Managed;
import ca.hc11337.gui.actions.*;


public class FileMenu implements Managed{
	private MenuManager menu = new MenuManager("&File");
	public FileMenu(HC11337Controller controller)
	{
		//MenuManager fileMenuManager = new MenuManager("File");
		
		IAction openAction = new OpenFile(controller);
		IAction newAction = new NewFile(controller);
		IAction saveAction = new SaveFile(controller);
		IAction saveAsAction = new SaveAs(controller);
		IAction closeAction = new CloseFile(controller);
		IAction closeAllAction = new CloseAll(controller);
		IAction printAction = new Print(controller);
		IAction exitAction = new Exit(controller);
		
		menu.add(newAction);
		menu.add(openAction);
		menu.add(saveAction);
		menu.add(saveAsAction);
		menu.add(new Separator());
		menu.add(closeAction);
		menu.add(closeAllAction);
		menu.add(new Separator());
		menu.add(printAction);
		menu.add(new Separator());
		menu.add(exitAction);
		
		//fileMenuManager.fill(bar, index);
	}
	
	public ContributionManager getManager()
	{
		return menu;
	}
}
