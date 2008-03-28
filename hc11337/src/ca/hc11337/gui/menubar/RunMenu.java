/*This file is part of HC11337.

    HC11337 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HC11337 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with HC11337.  If not, see <http://www.gnu.org/licenses/>.
*/

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
