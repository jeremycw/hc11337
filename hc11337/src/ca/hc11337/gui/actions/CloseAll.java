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

package ca.hc11337.gui.actions;

import org.eclipse.jface.action.*;

import ca.hc11337.gui.HC11337Controller;

public class CloseAll extends Action {
	private HC11337Controller controller;
	
	public CloseAll(HC11337Controller controller)
	{
		super("Close &All@Ctrl+Shift+W", AS_PUSH_BUTTON);
		setToolTipText("Close All");
		this.controller = controller;
		//Image descriptor = new Image(null, "icons/open.png");
		//setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
	}
	
	public void run()
	{
		controller.closeAll();
	}
	

}