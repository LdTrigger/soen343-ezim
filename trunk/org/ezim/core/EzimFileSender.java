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
package org.ezim.core;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.ezim.core.Ezim;
import org.ezim.core.EzimDtxSemantics;
import org.ezim.core.EzimLogger;
import org.ezim.ui.EzimFileOut;

public class EzimFileSender implements Runnable
{
	private EzimFileOut efo;
	private InetAddress addr;
	private int port;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the file sender
	 * @param efoIn the associated outgoing file window
	 * @param iaIn address of the recipient
	 * @param iPort DTX port of the recipient
	 */
	public EzimFileSender
	(
		EzimFileOut efoIn
		, InetAddress iaIn
		, int iPort
	)
	{
		this.efo = efoIn;
		this.addr = iaIn;
		this.port = iPort;
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * the method to be invoked
	 */
	public void run()
	{
		Socket sckOut = null;
		InetSocketAddress isaTmp = null;

		try
		{
			sckOut = new Socket();
			sckOut.bind
			(
				new InetSocketAddress
				(
					Ezim.localAddress
					, 0
				)
			);
			isaTmp = new InetSocketAddress
			(
				this.addr
				, this.port
			);
			sckOut.connect(isaTmp, Ezim.dtxTimeout);

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

			this.efo.unregDispose();
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
