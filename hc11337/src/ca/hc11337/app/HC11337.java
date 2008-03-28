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

package ca.hc11337.app;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.jface.action.*;
import org.eclipse.jface.window.*;
import org.eclipse.swt.*;

import ca.hc11337.gui.console.*;
import ca.hc11337.gui.cpuview.*;
import ca.hc11337.gui.editor.*;
import ca.hc11337.gui.fileselector.*;
import ca.hc11337.gui.memoryview.*;
import ca.hc11337.gui.menubar.*;
import ca.hc11337.gui.stackview.*;
import ca.hc11337.gui.statusline.*;
import ca.hc11337.gui.toolbar.*;
import ca.hc11337.gui.watchedmemory.*;
import ca.hc11337.gui.*;
import ca.hc11337.app.core.*;


public class HC11337 extends ApplicationWindow{

	/**
	 * @param args
	 */
	private final HC11337GUI gui = new HC11337GUI();
	private final HC11337Core core = new HC11337Core();
	private final HC11337Controller controller = new HC11337Controller(gui, core);
	
	public HC11337()
	{
		super(null);
		addMenuBar();
		addToolBar(SWT.FLAT);
		addStatusLine();
	}
	
	public static void main(String[] args) {
		HC11337 swin = new HC11337();
		swin.setBlockOnOpen(true);
		swin.open();
		Display.getCurrent().dispose();
	}
	
	protected ToolBarManager createToolBarManager(int style)
	{
		HC11337Toolbar tb = new HC11337Toolbar(controller, style);
		return (ToolBarManager)(tb.getManager());
	}
	
	protected MenuManager createMenuManager()
	{
		HC11337Menu menu = new HC11337Menu(controller);
		return (MenuManager)(menu.getManager());
	}
	
	protected StatusLineManager createStatusLineManager()
	{
		HC11337StatusLine sl = new HC11337StatusLine();
		return (StatusLineManager)(sl.getManager());
	}
	
	protected Control createContents(Composite parent)
	{
		Image icon = new Image(Display.getCurrent(), "icons/chip.png");
		getShell().setSize(720, 570);
		getShell().setText("HC11337 - pronounced 'aitch 'cee 'ee-'leet");
		getShell().setImage(icon);
		gui.setUp(parent, controller);
		controller.initCPUView();
		return parent;
	}
}
