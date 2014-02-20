/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2014  Chun-Kwong Wong
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
import javax.swing.text.Document;

import org.ezim.ui.EzimTextEditingPopupMenu;

public class EzimTextArea
	extends JTextArea
{
	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct a new EzimTextArea
	 */
	public EzimTextArea()
	{
		super();
		this.init();
	}

	/**
	 * construct a new EzimTextArea
	 * @param docIn the model to use
	 */
	public EzimTextArea(Document docIn)
	{
		super(docIn);
		this.init();
	}

	/**
	 * construct a new EzimTextArea
	 * @param docIn the model to use
	 * @param strIn the text to display
	 * @param iRows the number of rows
	 * @param iCols the number of columns
	 */
	public EzimTextArea(Document docIn, String strIn, int iRows, int iCols)
	{
		super(docIn, strIn, iRows, iCols);
		this.init();
	}

	/**
	 * construct a new EzimTextArea
	 * @param iRows the number of rows
	 * @param iCols the number of columns
	 */
	public EzimTextArea(int iRows, int iCols)
	{
		super(iRows, iCols);
		this.init();
	}

	/**
	 * construct a new EzimTextArea
	 * @param strIn the text to display
	 */
	public EzimTextArea(String strIn)
	{
		super(strIn);
		this.init();
	}

	/**
	 * construct a new EzimTextArea
	 * @param strIn the text to display
	 * @param iRows the number of rows
	 * @param iCols the number of columns
	 */
	public EzimTextArea(String strIn, int iRows, int iCols)
	{
		super(strIn, iRows, iCols);
		this.init();
	}

	// P R I V A T E -------------------------------------------------------
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
	}

	/**
	 * initialize features specific to EzimTextArea
	 */
	private void init()
	{
		this.initCtrls();
		new EzimTextEditingPopupMenu(this);
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * get the text contents contained
	 * @return text contents
	 */
	public String getText()
	{
		return super.getText().trim() + "\n";
	}
}
