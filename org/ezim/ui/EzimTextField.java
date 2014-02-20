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

import javax.swing.JTextField;
import javax.swing.text.Document;

import org.ezim.ui.EzimTextEditingPopupMenu;

public class EzimTextField
	extends JTextField
{
	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct a new EzimTextField
	 * @param bPopup whether to add a pop-up menu to this component
	 */
	public EzimTextField(boolean bPopup)
	{
		super();
		this.init(bPopup);
	}

	/**
	 * construct a new EzimTextField
	 * @param docIn the text storage to use
	 * @param strIn the initial string to display
	 * @param iCols the number of columns for calculating preferred width
	 * @param bPopup whether to add a pop-up menu to this component
	 */
	public EzimTextField
	(
		Document docIn
		, String strIn
		, int iCols
		, boolean bPopup
	)
	{
		super(docIn, strIn, iCols);
		this.init(bPopup);
	}

	/**
	 * construct a new EzimTextField
	 * @param iCols the number of columns for calculating preferred width
	 * @param bPopup whether to add a pop-up menu to this component
	 */
	public EzimTextField(int iCols, boolean bPopup)
	{
		super(iCols);
		this.init(bPopup);
	}

	/**
	 * construct a new EzimTextField
	 * @param strIn the initial string to display
	 * @param bPopup whether to add a pop-up menu to this component
	 */
	public EzimTextField(String strIn, boolean bPopup)
	{
		super(strIn);
		this.init(bPopup);
	}

	/**
	 * construct a new EzimTextField
	 * @param strIn the initial string to display
	 * @param iCols the number of columns for calculating preferred width
	 * @param bPopup whether to add a pop-up menu to this component
	 */
	public EzimTextField(String strIn, int iCols, boolean bPopup)
	{
		super(strIn, iCols);
		this.init(bPopup);
	}

	/**
	 * construct a new EzimTextField and add a pop-up menu to it
	 */
	public EzimTextField()
	{
		super();
		this.init();
	}

	/**
	 * construct a new EzimTextField and add a pop-up menu to it
	 * @param docIn the text storage to use
	 * @param strIn the initial string to display
	 * @param iCols the number of columns for calculating preferred width
	 */
	public EzimTextField(Document docIn, String strIn, int iCols)
	{
		super(docIn, strIn, iCols);
		this.init();
	}

	/**
	 * construct a new EzimTextField and add a pop-up menu to it
	 * @param iCols the number of columns for calculating preferred width
	 */
	public EzimTextField(int iCols)
	{
		super(iCols);
		this.init();
	}

	/**
	 * construct a new EzimTextField and add a pop-up menu to it
	 * @param strIn the initial string to display
	 */
	public EzimTextField(String strIn)
	{
		super(strIn);
		this.init();
	}

	/**
	 * construct a new EzimTextField and add a pop-up menu to it
	 * @param strIn the initial string to display
	 * @param iCols the number of columns for calculating preferred width
	 */
	public EzimTextField(String strIn, int iCols)
	{
		super(strIn, iCols);
		this.init();
	}

	// P R I V A T E -------------------------------------------------------
	/**
	 * initialize features specific to EzimTextField
	 * @param bPopup whether to add a pop-up menu to this component
	 */
	private void init(boolean bPopup)
	{
		if (bPopup) new EzimTextEditingPopupMenu(this);
	}

	/**
	 * initialize features specific to EzimTextField and add a pop-up menu
	 * to it
	 */
	private void init()
	{
		this.init(true);
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * get the text contents contained
	 * @return text contents
	 */
	public String getText()
	{
		return super.getText().trim();
	}
}
