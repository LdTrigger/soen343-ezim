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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
	public final static String appVer = "0.0.2";

	// transmission character encoding
	public final static String rtxEnc = "UTF-8";

	// multicast group, port, TTL, and incoming buffer size
	public final static String mcGroup = "229.0.0.1";
	public final static int ackPort = 5555;
	public final static int ttl = 1;
	public final static int inBuf = 1024;

	// message transmission port
	public final static int msgPort = 6666;

	public static String getConf()
	{
		String strOut = null;
		String strSep = System.getProperty("file.separator");
		StringBuffer sbFName = new StringBuffer();
		BufferedReader brTmp = null;
		BufferedWriter bwTmp = null;

		try
		{
			sbFName.append(System.getProperty("user.home"));
			sbFName.append(strSep);

			if (strSep.equals("/"))	sbFName.append(".");
			else					sbFName.append("_");

			sbFName.append("Ezim.conf");

			brTmp = new BufferedReader
			(
				new FileReader(sbFName.toString())
			);

			strOut = brTmp.readLine();
		}
		catch(Exception e)
		{
			// obtain user name
			while(strOut == null || strOut.length() == 0)
			{
				strOut = JOptionPane.showInputDialog
				(
					EzimLang.PleaseInputYourName
				);
			}

			try
			{
				bwTmp = new BufferedWriter
				(
					new FileWriter(sbFName.toString())
				);

				bwTmp.write(strOut);
			}
			catch(Exception exp)
			{
				// ignore?!
			}
		}
		finally
		{
			try
			{
				if (brTmp != null)
					brTmp.close();
			}
			catch(Exception e)
			{
			}

			try
			{
				if (bwTmp != null)
				{
					bwTmp.flush();
					bwTmp.close();
				}
			}
			catch(Exception e)
			{
			}
		}

		return strOut;
	}

	public static void main(String[] arrArgs)
	{
		EzimLang.init();
		String strName = getConf();

		EzimMain emTmp = new EzimMain();
		emTmp.localName = strName;

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
