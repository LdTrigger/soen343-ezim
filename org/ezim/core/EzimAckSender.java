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

import java.lang.Thread;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.ui.EzimMain;

public class EzimAckSender extends Thread
{
	private EzimMain emHwnd;
	private String msg;

	public EzimAckSender(EzimMain emIn, String strIn)
	{
		this.emHwnd = emIn;
		this.msg = strIn;
	}

	public void run()
	{
		MulticastSocket ms = null;
		InetAddress ia = null;
		int iMcPort = 0;
		DatagramPacket dp = null;
		byte[] arrBytes = null;

		EzimConf ecTmp = EzimConf.getInstance();

		try
		{
			ia = InetAddress.getByName
			(
				ecTmp.settings.getProperty(EzimConf.ezimMcGroup)
			);

			iMcPort = Integer.parseInt
			(
				ecTmp.settings.getProperty(EzimConf.ezimMcPort)
			);

			ms = new MulticastSocket(iMcPort);
			ms.setReuseAddress(true);
			ms.setTimeToLive(Ezim.ttl);
			if (ms.getLoopbackMode()) ms.setLoopbackMode(false);

			arrBytes = this.msg.getBytes(Ezim.dtxMsgEnc);
			if (arrBytes.length > Ezim.inBuf)
				throw new Exception("Ack message too long.");
			dp = new DatagramPacket
			(
				arrBytes
				, arrBytes.length
				, ia
				, iMcPort
			);

			ms.send(dp);
		}
		catch(Exception e)
		{
			emHwnd.errAlert(e.getMessage());
		}
		finally
		{
			try
			{
				if (ms != null && ! ms.isClosed()) ms.close();
			}
			catch(Exception e)
			{
				// ignore
			}
		}

		return;
	}
}
