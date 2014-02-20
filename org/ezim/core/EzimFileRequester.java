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
import org.ezim.ui.EzimMain;

public class EzimFileRequester implements Runnable
{
	private InetAddress addr = null;
	private int port = -1;
	private String id = null;
	private EzimFileOut efo = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the file requester
	 * @param iaIn address of the recipient
	 * @param iPort DTX port of the recipient
	 * @param strId file ID in the outgoing file queue (File-Request-ID)
	 * @param efoIn the associated outgoing file window
	 */
	public EzimFileRequester
	(
		InetAddress iaIn
		, int iPort
		, String strId
		, EzimFileOut efoIn
	)
	{
		this.addr = iaIn;
		this.port = iPort;
		this.id = strId;
		this.efo = efoIn;
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

			EzimDtxSemantics.sendFileReq(sckOut, this.id, this.efo);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);
			EzimMain.showError(this.efo, e.getMessage());

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
