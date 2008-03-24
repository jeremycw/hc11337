package ca.hc11337.gui.menubar;


import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

import ca.hc11337.gui.HC11337Controller;
import ca.hc11337.gui.Managed;
import ca.hc11337.gui.actions.*;


public class EditMenu implements Managed {
	private MenuManager menu = new MenuManager("&Edit");
	public EditMenu(HC11337Controller controller)
	{
		IAction undoAction = new Undo(controller);
		IAction redoAction = new Redo(controller);
		IAction cutAction = new Cut(controller);
		IAction copyAction = new Copy(controller);
		IAction pasteAction = new Paste(controller);
		IAction deleteAction = new Delete(controller);
		IAction selectAllAction = new SelectAll(controller);
		
		menu.add(undoAction);
		menu.add(redoAction);
		menu.add(new Separator());
		menu.add(cutAction);
		menu.add(copyAction);
		menu.add(pasteAction);
		menu.add(new Separator());
		menu.add(deleteAction);
		menu.add(new Separator());
		menu.add(selectAllAction);

	}
	
	public ContributionManager getManager()
	{
		return menu;
	}
}