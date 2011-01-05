/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2011  Chun-Kwong Wong
    chunkwong.wong@gmail.com
    http://ezim.sourceforge.net/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ezim.ui;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class EzimTextArea
	extends JTextArea
{
	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct a textarea for message windows
	 */
	public EzimTextArea()
	{
		super();
		this.initCtrls();
	}

	/**
	 * initialize controls with modified key mappings
	 */
	private void initCtrls()
	{
		// K E Y S T R O K E -----------------------------------------------
		AWTKeyStroke aksTab = AWTKeyStroke.getAWTKeyStroke
		(
			KeyEvent.VK_TAB
			, 0
		);
		AWTKeyStroke aksShftTab = AWTKeyStroke.getAWTKeyStroke
		(
			KeyEvent.VK_TAB
			, InputEvent.SHIFT_DOWN_MASK
		);
		AWTKeyStroke aksCtrlTab = AWTKeyStroke.getAWTKeyStroke
		(
			KeyEvent.VK_TAB
			, InputEvent.CTRL_DOWN_MASK
		);
		AWTKeyStroke aksCtrlShftTab = AWTKeyStroke.getAWTKeyStroke
		(
			KeyEvent.VK_TAB
			, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK
		);

		KeyStroke ksCtrlTab = KeyStroke.getKeyStroke
		(
			KeyEvent.VK_TAB
			, InputEvent.CTRL_DOWN_MASK
		);

		// F O R W A R D   T R A V E R S A L   K E Y S ---------------------
		Set<AWTKeyStroke> ksSetFwd = new HashSet<AWTKeyStroke>
		(
			this.getFocusTraversalKeys
			(
				KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS
			)
		);
		ksSetFwd.remove(aksCtrlTab);
		ksSetFwd.add(aksTab);
		this.setFocusTraversalKeys
		(
			KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS
			, ksSetFwd
		);

		// B A C K W A R D   T R A V E R S A L   K E Y S -------------------
		Set<AWTKeyStroke> ksSetBwd = new HashSet<AWTKeyStroke>
		(
			this.getFocusTraversalKeys
			(
				KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS
			)
		);
		ksSetBwd.remove(aksCtrlShftTab);
		ksSetBwd.add(aksShftTab);
		this.setFocusTraversalKeys
		(
			KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS
			, ksSetBwd
		);

		// R E M A P   I N S E R T   T A B   K E Y -------------------------
		this.getInputMap().put(ksCtrlTab, "insert-tab");

		return;
	}
}
