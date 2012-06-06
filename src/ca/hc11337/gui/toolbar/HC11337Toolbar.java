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

package ca.hc11337.gui.toolbar;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;

import ca.hc11337.gui.*;
import ca.hc11337.gui.actions.*;


public class HC11337Toolbar implements Managable{
	private ToolBarManager tool_bar_manager;
	public HC11337Toolbar(HC11337Controller controller, int style)
	{
		tool_bar_manager  = new ToolBarManager(style);
		Action openAction = new OpenFile(controller);
		Action newAction = new NewFile(controller);
		Action saveAction = new SaveFile(controller);
		Action printAction = new Print(controller);
		
		Action undoAction = new Undo(controller);
		Action cutAction = new Cut(controller);
		Action copyAction = new Copy(controller);
		Action pasteAction = new Paste(controller);

		tool_bar_manager.add(newAction);
		tool_bar_manager.add(openAction);
		tool_bar_manager.add(saveAction);
		tool_bar_manager.add(printAction);
		tool_bar_manager.add(new Separator());
		
		tool_bar_manager.add(undoAction);
		tool_bar_manager.add(cutAction);
		tool_bar_manager.add(copyAction);
		tool_bar_manager.add(pasteAction);
		tool_bar_manager.add(new Separator());
		
		Action resetAction = new Reset(controller);
		Action runAction = new Run(controller);
		Action stopAction = new Stop(controller);
		Action stepAction = new Step(controller);
		Action buildAction = new Build(controller);
		
		tool_bar_manager.add(resetAction);
		tool_bar_manager.add(runAction);
		tool_bar_manager.add(stopAction);
		tool_bar_manager.add(stepAction);
		tool_bar_manager.add(buildAction);
		tool_bar_manager.add(new Separator());
		
		Action optionsAction = new Options(controller);
		tool_bar_manager.add(optionsAction);
	}

	public ContributionManager getManager()
	{
		return tool_bar_manager;
	}
}
