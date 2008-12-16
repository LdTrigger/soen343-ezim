/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2008  Chun-Kwong Wong
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
package org.ezim.core;

import java.lang.Runnable;
import java.net.Socket;
import java.net.InetSocketAddress;

import org.ezim.core.Ezim;
import org.ezim.core.EzimDtxSemantics;
import org.ezim.core.EzimLogger;
import org.ezim.ui.EzimFileOut;

public class EzimFileSender implements Runnable
{
	private EzimFileOut efo;
	private String ip;
	private int port;

	public EzimFileSender
	(
		EzimFileOut efoIn
		, String strIp
		, int iPort
	)
	{
		this.efo = efoIn;
		this.ip = strIp;
		this.port = iPort;
	}

	public void run()
	{
		Socket sckOut = null;
		InetSocketAddress isaTmp = null;

		try
		{
			sckOut = new Socket();
			isaTmp = new InetSocketAddress
			(
				this.ip
				, this.port
			);
			sckOut.connect(isaTmp, Ezim.dtxTimeout);

			this.efo.setSocket(sckOut);

			EzimDtxSemantics.sendFile
			(
				sckOut
				, this.efo.getId()
				, this.efo
			);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);

			// re-enable the send message window upon failure
			this.efo.setEnabled(true);
		}
		finally
		{
			try
			{
				if (sckOut != null && ! sckOut.isClosed()) sckOut.close();
			}
			catch(Exception e)
			{
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}
		}
	}
}