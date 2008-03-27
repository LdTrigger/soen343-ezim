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

import javax.swing.UIManager;

import org.ezim.core.EzimAckTaker;
import org.ezim.core.EzimDtxTaker;
import org.ezim.core.EzimLang;
import org.ezim.ui.EzimMain;

public class Ezim
{
	// application name and version
	public final static String appName = "EZ Intranet Messenger";
	public final static String appAbbrev = "ezim";
	public final static String appVer = "0.1.0";

	// multicast group, port, TTL, and incoming buffer size
	// where group should be from 224.0.0.0 to 239.255.255.255
	public final static String mcGroup = "229.0.0.1";
	public final static int mcPort = 5555;
	public final static int ttl = 1;
	public final static int inBuf = 1024;

	// maximum textfield lengths (for Ack messages)
	public final static int maxAckLength = inBuf / 4;

	// direct transmission port and timeout limit (in ms)
	public final static int dtxPort = 6666;
	public final static int dtxTimeout = 30000;

	// direct transmission message encoding
	public final static String dtxMsgEnc = "UTF-8";

	// direct transmission buffer length (in bytes)
	public final static int dtxBufLen = 1024;

	// self-entry background color on the contact list
	public final static int colorSelf = (int) 0xDEEFFF;

	public static void main(String[] arrArgs)
	{
		UIManager.put("Button.defaultButtonFollowsFocus", true);

		EzimLang.init();
		EzimImage.init();

		EzimMain emTmp = EzimMain.getInstance();

		EzimDtxTaker edtTmp = new EzimDtxTaker();
		edtTmp.start();

		EzimAckTaker eatTmp = new EzimAckTaker();
		eatTmp.start();

		try
		{
			Thread.sleep(500);
		}
		catch(Exception e)
		{
			// ignore whatever
		}

		emTmp.freshPoll();

		return;
	}
}
