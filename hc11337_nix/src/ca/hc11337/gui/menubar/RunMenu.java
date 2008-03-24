package ca.hc11337.gui.menubar;


import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

import ca.hc11337.gui.HC11337Controller;
import ca.hc11337.gui.Managed;
import ca.hc11337.gui.actions.*;


public class RunMenu implements Managed{
	private MenuManager menu = new MenuManager("&Run");
	public RunMenu(HC11337Controller controller)
	{
		IAction resetAction = new Reset(controller);
		IAction deepResetAction = new ResetDeep(controller);
		IAction runAction = new Run(controller);
		IAction stopAction = new Stop(controller);
		IAction stepAction = new Step(controller);
		IAction buildAction = new Build(controller);
		
		menu.add(resetAction);
		menu.add(deepResetAction);
		menu.add(new Separator());
		menu.add(runAction);
		menu.add(stopAction);
		menu.add(stepAction);
		menu.add(new Separator());
		menu.add(buildAction);
	}
	
	public ContributionManager getManager()
	{
		return menu;
	}
}
