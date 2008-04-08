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

import java.lang.Thread;
import java.net.Socket;
import java.net.InetSocketAddress;
import javax.swing.JOptionPane;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimDtxSemantics;
import org.ezim.core.EzimLang;

public class EzimFileConfirmer extends Thread
{
	private String ip = null;
	private String id = null;
	private boolean blnConfirm = false;

	public EzimFileConfirmer
	(
		String strIp
		, String strId
		, boolean blnIn
	)
	{
		this.ip = strIp;
		this.id = strId;
		this.blnConfirm = blnIn;
	}

	public void run()
	{
		Socket sckOut = null;
		InetSocketAddress isaTmp = null;

		EzimConf ecTmp = EzimConf.getInstance();

		try
		{
			sckOut = new Socket();
			isaTmp = new InetSocketAddress
			(
				this.ip
				, Integer.parseInt
				(
					ecTmp.settings.getProperty(EzimConf.ezimDtxPort)
				)
			);
			sckOut.connect(isaTmp, Ezim.dtxTimeout);

			EzimDtxSemantics.sendFileConfirm
			(
				sckOut
				, this.id
				, this.blnConfirm
			);
		}
		catch(Exception e)
		{
			// ignore
		}
		finally
		{
			try
			{
				if (sckOut != null && ! sckOut.isClosed()) sckOut.close();
			}
			catch(Exception e)
			{
				// ignore
			}
		}
	}
}