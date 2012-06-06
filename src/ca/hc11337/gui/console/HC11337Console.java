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

package ca.hc11337.gui.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.*;

public class HC11337Console {
	private StyledText consoleText;
	public HC11337Console(Composite parent, int style)
	{
		consoleText = new StyledText (parent, style);
		consoleText.setEditable(false);
	}
	
	public Composite getConsole()
	{
		return consoleText;
	}
	
	public String getText()
	{
		return consoleText.getText();
	}
	
	public void setText(String text)
	{
		consoleText.setText(text);
	}
}
