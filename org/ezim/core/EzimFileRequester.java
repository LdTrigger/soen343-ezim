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
import javax.swing.JOptionPane;

import org.ezim.core.Ezim;
import org.ezim.core.EzimDtxSemantics;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimLogger;
import org.ezim.ui.EzimFileOut;

public class EzimFileRequester implements Runnable
{
	private String ip = null;
	private int port = -1;
	private String id = null;
	private EzimFileOut efo = null;

	public EzimFileRequester
	(
		String strIp
		, int iPort
		, String strId
		, EzimFileOut efoIn
	)
	{
		this.ip = strIp;
		this.port = iPort;
		this.id = strId;
		this.efo = efoIn;
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

			EzimDtxSemantics.sendFileReq(sckOut, this.id, this.efo);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);

			JOptionPane.showMessageDialog
			(
				null
				, e.getMessage()
				, EzimLang.SendMessageError
				, JOptionPane.ERROR_MESSAGE
			);
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