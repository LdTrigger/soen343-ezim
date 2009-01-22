/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2009  Chun-Kwong Wong
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
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimLogger;
import org.ezim.ui.EzimMain;

public class EzimAckSender implements Runnable
{
	private String msg;

	public EzimAckSender(String strIn)
	{
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
			EzimMain.getInstance().errAlert(e.getMessage());
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (ms != null && ! ms.isClosed()) ms.close();
			}
			catch(Exception e)
			{
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}
		}

		return;
	}
}
