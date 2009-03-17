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

import java.awt.Component;
import java.net.InetAddress;
import java.net.NetworkInterface;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class EzimLocalAddressListRenderer
	extends JLabel
	implements ListCellRenderer
{
	public EzimLocalAddressListRenderer()
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
		InetAddress iaTmp = (InetAddress) objIn;
		StringBuffer sbTmp = new StringBuffer
		(
			iaTmp.getHostAddress()
		);
		NetworkInterface niTmp = null;

		try
		{
			niTmp = NetworkInterface.getByInetAddress(iaTmp);

			if
			(
				niTmp != null
				&& niTmp.getName() != null
				&& niTmp.getName().length() > 0
			)
			{
				sbTmp.insert(0, ") ");
				sbTmp.insert(0, niTmp.getName());
				sbTmp.insert(0, "(");
			}
		}
		catch(Exception e)
		{
			niTmp = null;
		}

		// display IP of the InetAddress
		this.setText(sbTmp.toString());

		this.setEnabled(jlstIn.isEnabled());
		this.setFont(jlstIn.getFont());
		this.setOpaque(true);

		return this;
	}
}
