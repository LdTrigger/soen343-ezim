/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2009  Chun-Kwong Wong
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

import org.ezim.core.EzimContact;
import org.ezim.core.EzimImage;

public class EzimContactListRenderer
	extends JLabel
	implements ListCellRenderer
{
	public EzimContactListRenderer()
	{
	}

	public Component getListCellRendererComponent
	(
		JList jlstIn
		, Object objIn
		, int iIdx
		, boolean blnSelected
		, boolean blnCellHasFocus
	)
	{
		EzimMain emHwnd = EzimMain.getInstance();
		EzimContact ecTmp = (EzimContact) objIn;
		Color clrSelf = new Color(emHwnd.colorSelf);

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
			if (ecTmp.getAddress().equals(emHwnd.localAddress))
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
