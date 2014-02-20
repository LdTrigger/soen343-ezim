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
import org.ezim.core.EzimFrxList;
import org.ezim.core.EzimLogger;

import org.ezim.ui.EzimFileIn;
import org.ezim.ui.EzimMain;

public class EzimFileResponder implements Runnable
{
	private InetAddress addr = null;
	private int port = -1;
	private String id = null;
	private boolean blnRes = false;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the file confirmer
	 * @param iaIn address of the sender
	 * @param iPort DTX port of the sender
	 * @param strId File-Request-ID to respond to
	 * @param blnIn indicates whether the transmission is accepted
	 */
	public EzimFileResponder
	(
		InetAddress iaIn
		, int iPort
		, String strId
		, boolean blnIn
	)
	{
		this.addr = iaIn;
		this.port = iPort;
		this.id = strId;
		this.blnRes = blnIn;
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

			EzimDtxSemantics.sendFileRes(sckOut, this.id, this.blnRes);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);

			EzimFileIn efiTmp = EzimFrxList.getInstance().get(this.id);
			EzimMain.showError(efiTmp, e.getMessage());

			efiTmp.unregDispose();
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
