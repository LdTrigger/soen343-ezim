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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import org.ezim.core.EzimImage;
import org.ezim.core.EzimLang;

public class EzimTextEditingPopupMenu
	extends JPopupMenu
{
	private JMenuItem miCut;
	private JMenuItem miCopy;
	private JMenuItem miPaste;
	private JMenuItem miSelectAll;

	private final JTextComponent jtcParent;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct a text editing pop-up menu
	 */
	public EzimTextEditingPopupMenu(JTextComponent jtcIn)
	{
		super();
		this.jtcParent = jtcIn;
		this.initGUI();
	}

	// P R I V A T E -------------------------------------------------------
	/**
	 * initialize components of the menu
	 */
	private void initGUI()
	{
		this.miCut = new JMenuItem
		(
			EzimLang.Cut
			, EzimImage.icoTxtPops[0]
		);
		ActionListener alCut = new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				EzimTextEditingPopupMenu.this.jtcParent.cut();
			}
		};
		this.miCut.addActionListener(alCut);
		this.add(this.miCut);

		this.miCopy = new JMenuItem
		(
			EzimLang.Copy
			, EzimImage.icoTxtPops[1]
		);
		ActionListener alCopy = new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				EzimTextEditingPopupMenu.this.jtcParent.copy();
			}
		};
		this.miCopy.addActionListener(alCopy);
		this.add(this.miCopy);

		this.miPaste = new JMenuItem
		(
			EzimLang.Paste
			, EzimImage.icoTxtPops[2]
		);
		ActionListener alPaste = new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				EzimTextEditingPopupMenu.this.jtcParent.paste();
			}
		};
		this.miPaste.addActionListener(alPaste);
		this.add(miPaste);

		this.miSelectAll = new JMenuItem
		(
			EzimLang.SelectAll
		);
		ActionListener alSelectAll = new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				EzimTextEditingPopupMenu.this.jtcParent.selectAll();
			}
		};
		this.miSelectAll.addActionListener(alSelectAll);
		this.add(this.miSelectAll);

		MouseListener mlTmp = new PopupListener(this);
		this.jtcParent.addMouseListener(mlTmp);
	}

	/**
	 * event handling class for bringing up the pop-up menu
	 */
	private class PopupListener
		extends MouseAdapter
	{
		EzimTextEditingPopupMenu etepm;

		PopupListener(EzimTextEditingPopupMenu etepmIn)
		{
			this.etepm = etepmIn;
		}

		private void showPopup(MouseEvent me)
		{
			if (me.isPopupTrigger())
			{
				this.etepm.reveal
				(
					me.getComponent()
					, me.getX()
					, me.getY()
				);
			}
		}

		public void mousePressed(MouseEvent me)
		{
			this.showPopup(me);
		}

		public void mouseReleased(MouseEvent me)
		{
			this.showPopup(me);
		}
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * displays the pop-up menu at the position x,y in the coordinate space
	 * of the component invoker
	 * @param cmpIn invoker component
	 * @param iX x-coordinate of the position
	 * @param iY y-coordinate of the position
	 */
	public void reveal(Component cmpIn, int iX, int iY)
	{
		boolean bEnabled = this.jtcParent.isEnabled();
		boolean bEditable = this.jtcParent.isEditable();
		boolean bSelected = null != this.jtcParent.getSelectedText();

		this.miCut.setEnabled(bEnabled && bEditable && bSelected);
		this.miCopy.setEnabled(bEnabled && bSelected);
		this.miPaste.setEnabled(bEnabled && bEditable);
		this.miSelectAll.setEnabled(bEnabled);

		this.show(cmpIn, iX, iY);
	}
}
