/*
    EZ Intranet Messenger
    Copyright (C) 2007 - 2008  Chun-Kwong Wong <chunkwong.wong@gmail.com>

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
package org.ezim.core;

import java.net.URL;
import javax.swing.ImageIcon;

public class EzimImage
{
	// frame window icons
	public static ImageIcon icoMain;
	public static ImageIcon icoMsg;
	public static ImageIcon icoPlaza;

	// state icons
	public static ImageIcon icoMan00;
	public static ImageIcon icoMan01;

	// EzimMain buttons
	public static ImageIcon icoBtnMsg;
	public static ImageIcon icoBtnFtx;
	public static ImageIcon icoBtnFrx;
	public static ImageIcon icoBtnRefresh;
	public static ImageIcon icoBtnPlaza;

	// EzimMain labels
	public static ImageIcon icoLblAbout;

	public static void init()
	{
		URL iconUrlTmp = null;

		// main window frame icon
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/ezim.png"
		);
		icoMain = new ImageIcon(iconUrlTmp);

		// (incoming + outgoing) message window frame icon
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/msg.png"
		);
		icoMsg = new ImageIcon(iconUrlTmp);

		// speech plaza window frame icon
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/plaza.png"
		);
		icoPlaza = new ImageIcon(iconUrlTmp);

		// status man icon 00
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/man00.png"
		);
		icoMan00 = new ImageIcon(iconUrlTmp);

		// status man icon 01
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/man01.png"
		);
		icoMan01 = new ImageIcon(iconUrlTmp);

		// main window message button icon
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/btnmsg.png"
		);
		icoBtnMsg = new ImageIcon(iconUrlTmp);

		// main window file transmission button icon
		// outgoing file window frame icon
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/btnftx.png"
		);
		icoBtnFtx = new ImageIcon(iconUrlTmp);

		// incoming file window frame icon
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/btnfrx.png"
		);
		icoBtnFrx = new ImageIcon(iconUrlTmp);

		// main window refresh button icon
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/btnrefresh.png"
		);
		icoBtnRefresh = new ImageIcon(iconUrlTmp);

		// main window plaza button icon
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/btnplaza.png"
		);
		icoBtnPlaza = new ImageIcon(iconUrlTmp);

		// main window about label icon
		iconUrlTmp = ClassLoader.getSystemResource
		(
			"org/ezim/image/icon/lblabout.png"
		);
		icoLblAbout = new ImageIcon(iconUrlTmp);

		return;
	}
}
