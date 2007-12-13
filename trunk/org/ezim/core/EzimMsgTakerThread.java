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
import java.io.InputStreamReader;
import java.lang.Thread;
import java.net.Socket;

import org.ezim.core.EzimContact;
import org.ezim.ui.EzimMsgIn;

public class EzimMsgTakerThread extends Thread
{
	private EzimContact ec;
	private Socket sck;

	public EzimMsgTakerThread(EzimContact ecIn, Socket sckIn)
	{
		this.ec = ecIn;
		this.sck = sckIn;
	}

	public void run()
	{
		StringBuffer sbTmp = new StringBuffer();
		BufferedReader brTmp = null;
		String strTmp = null;

		try
		{
			brTmp = new BufferedReader
			(
				new InputStreamReader
				(
					this.sck.getInputStream()
					, Ezim.rtxEnc
				)
			);

			// we need this block due to BufferedReader.ready()'s nature
			strTmp = brTmp.readLine();
			if (strTmp != null)
			{
				sbTmp.append(strTmp);
				sbTmp.append("\n");
			}

			while(brTmp.ready())
			{
				sbTmp.append(brTmp.readLine());
				sbTmp.append("\n");
			}

			new EzimMsgIn(this.ec, sbTmp.toString());
		}
		catch(Exception e)
		{
			// ignore
		}
		finally
		{
			try
			{
				brTmp.close();
			}
			catch(Exception e)
			{
				// ignore
			}

			try
			{
				if (sck != null && ! sck.isClosed()) sck.close();
			}
			catch(Exception e)
			{
				// ignore
			}
		}

		return;
	}
}
