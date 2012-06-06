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


import org.eclipse.jface.action.*;

import ca.hc11337.gui.HC11337Controller;
import ca.hc11337.gui.Managable;
import ca.hc11337.gui.actions.*;


public class FileMenu implements Managable{
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
