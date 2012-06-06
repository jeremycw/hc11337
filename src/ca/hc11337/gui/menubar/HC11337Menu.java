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

import ca.hc11337.gui.*;


public class HC11337Menu implements Managable
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
