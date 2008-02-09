/*
    EZ Intranet Messenger
    Copyright (C) 2007  Chun-Kwong Wong <chunkwong.wong@gmail.com>

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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.ezim.core.Ezim;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimImage;

public class EzimContactListRenderer
	extends JLabel
	implements ListCellRenderer
{
	private EzimMain emHwnd;

	public EzimContactListRenderer(EzimMain emIn)
	{
		this.emHwnd = emIn;
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
		EzimContact ecTmp = (EzimContact) objIn;

		// state icon
		switch(ecTmp.getState())
		{
			case(EzimContact.PLAZA_STATE):
				this.setIcon(EzimImage.icoMan01);
				break;
			case(EzimContact.DEFAULT_STATE):
			default:
				this.setIcon(EzimImage.icoMan00);
				break;
		}

		// username + status
		this.setText(ecTmp.getName() + " (" + ecTmp.getStatus() + ")");

		if (blnSelected)
		{
			this.setBackground(jlstIn.getSelectionBackground());
			this.setForeground(jlstIn.getSelectionForeground());
		}
		else
		{
			if (ecTmp.getIp().equals(emHwnd.localAddress))
				this.setBackground(Ezim.colorSelf);
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
