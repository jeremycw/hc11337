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
import ca.hc11337.gui.Managable;
import ca.hc11337.gui.actions.*;


public class EditMenu implements Managable {
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