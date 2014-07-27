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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimImage;

public class EzimContactListRenderer
	extends JLabel
	implements ListCellRenderer
{
	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct a list cell renderer for contact display
	 */
	public EzimContactListRenderer()
	{
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * return a component that has been configured to display the specified
	 * value
	 * @param jlstIn the JList we're painting
	 * @param objIn value returned by list.getModel().getElementAt(index)
	 * @param iIdx the cells index
	 * @param blnSelected true if the specified cell was selected
	 * @param blnCellHasFocus true if the specified cell has the focus
	 */
	public Component getListCellRendererComponent
	(
		JList jlstIn
		, Object objIn
		, int iIdx
		, boolean blnSelected
		, boolean blnCellHasFocus
	)
	{
		EzimContact ecTmp = (EzimContact) objIn;
		Color clrSelf = new Color(EzimConf.UI_COLOR_SELF);

		// state icon
		if (ecTmp.getSysState() != EzimContact.SYSSTATE_DEFAULT)
			this.setIcon(EzimImage.icoSysStates[ecTmp.getSysState()]);
		else
			this.setIcon(EzimImage.icoStates[ecTmp.getState()]);

		// username + status
		this.setText(ecTmp.getName() + " (" + ecTmp.getStatus() + ")");

		if (blnSelected)
		{
			this.setBackground(jlstIn.getSelectionBackground());
			this.setForeground(jlstIn.getSelectionForeground());
		}
		else
		{
			if (Ezim.isLocalAddress(ecTmp.getAddress()))
				this.setBackground(clrSelf);
			else
				this.setBackground(jlstIn.getBackground());

			this.setForeground(jlstIn.getForeground());
		}

		this.setEnabled(jlstIn.isEnabled());
		this.setFont(jlstIn.getFont());
		this.setOpaque(true);

		return this;
	}
}
