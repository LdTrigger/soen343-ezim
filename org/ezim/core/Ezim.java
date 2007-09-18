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
package org.ezim.core;

import java.awt.Color;
import javax.swing.JOptionPane;

import org.ezim.core.EzimAckTaker;
import org.ezim.core.EzimMsgTaker;
import org.ezim.core.EzimLang;
import org.ezim.ui.EzimMain;

public class Ezim
{
	// application name and version
	public final static String appName = "EZ Intranet Messenger";
	public final static String appAbbrev = "Ezim";
	public final static String appVer = "0.0.4";

	// transmission character encoding
	public final static String rtxEnc = "UTF-8";

	// multicast group, port, TTL, and incoming buffer size
	public final static String mcGroup = "229.0.0.1";
	public final static int ackPort = 5555;
	public final static int ttl = 1;
	public final static int inBuf = 1024;

	// message transmission port and timeout limit (in ms)
	public final static int msgPort = 6666;
	public final static int msgTimeout = 30000;

	// maximum textfield lengths (for Ack messages)
	public final static int maxAckLength = inBuf / 4;

	// self-entry background color on the contact list
	public final static Color colorSelf = new Color((int) 0xDEEFFF);

	/**
	 * determine the appropriate configuration filename
	 */
	public static String getConfFilename()
	{
		String strSep = System.getProperty("file.separator");
		String strHome = System.getProperty("user.home");
		StringBuffer sbFName = new StringBuffer();

		sbFName.append(strHome);
		sbFName.append(strSep);

		if (strSep.equals("/"))	sbFName.append(".");
		else					sbFName.append("_");

		sbFName.append("ezim.conf");

		return sbFName.toString();
	}

	/**
	 * retrieve configurations from file
	 */
	public static EzimConf getConf()
	{
		return new EzimConf(getConfFilename());
	}

	public static void main(String[] arrArgs)
	{
		EzimLang.init();
		EzimImage.init();

		EzimConf ecTmp = getConf();

		EzimMain emTmp = new EzimMain();
		emTmp.setLocation
		(
			Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.mainFrameX
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.mainFrameY
				)
			)
		);
		emTmp.setSize
		(
			Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.mainFrameW
				)
			)
			, Integer.parseInt
			(
				ecTmp.settings.getProperty
				(
					EzimConf.mainFrameH
				)
			)
		);
		emTmp.localName = ecTmp.settings.getProperty
		(
			EzimConf.username
		);

		// query username if isn't set yet
		if (emTmp.localName == null || emTmp.localName.length() == 0)
		{
			String strTmp = null;

			// obtain user name
			while(strTmp == null || strTmp.length() == 0)
			{
				strTmp = JOptionPane.showInputDialog
				(
					EzimLang.PleaseInputYourName
				);
			}

			emTmp.localName = strTmp;
		}

		EzimMsgTaker emtTmp = new EzimMsgTaker(emTmp);
		emtTmp.start();

		EzimAckTaker eatTmp = new EzimAckTaker(emTmp);
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
