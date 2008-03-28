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
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;

import ca.hc11337.gui.HC11337Controller;

public class Cut extends Action {
	private HC11337Controller controller;
	
	public Cut(HC11337Controller controller)
	{
		super("C&ut@Ctrl+X", AS_PUSH_BUTTON);
		setToolTipText("Cut");
		Image descriptor = new Image(null, "icons/cut.png");
		setImageDescriptor(ImageDescriptor.createFromImage(descriptor));
		this.controller = controller;
	}
	
	public void run()
	{
		controller.cut();
	}
	

}