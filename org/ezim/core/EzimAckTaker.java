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
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.ezim.core.Ezim;
import org.ezim.core.EzimAckSemantics;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimLogger;
import org.ezim.core.EzimThreadPool;
import org.ezim.ui.EzimMain;

public class EzimAckTaker implements Runnable
{
	public EzimAckTaker()
	{
	}

	public void run()
	{
		MulticastSocket ms = null;
		InetAddress ia = null;
		DatagramPacket dp = null;
		byte[] arrBytes = new byte[Ezim.inBuf];

		EzimConf ecTmp = EzimConf.getInstance();
		EzimMain emHwnd = EzimMain.getInstance();

		try
		{
			ia = InetAddress.getByName
			(
				ecTmp.settings.getProperty(EzimConf.ezimMcGroup)
			);

			ms = new MulticastSocket
			(
				Integer.parseInt
				(
					ecTmp.settings.getProperty(EzimConf.ezimMcPort)
				)
			);
			ms.setReuseAddress(true);
			ms.joinGroup(ia);

			dp = new DatagramPacket(arrBytes, arrBytes.length);

			while(true)
			{
				ms.receive(dp);

				final String strHAdr = dp.getAddress().getHostAddress();

				final String strAck = new String
				(
					dp.getData()
					, 0
					, dp.getLength()
					, Ezim.dtxMsgEnc
				);

				EzimThreadPool etpTmp = EzimThreadPool.getInstance();
				etpTmp.execute
				(
					new Runnable()
					{
						public void run()
						{
							EzimAckSemantics.parser
							(
								strHAdr
								, strAck
							);
						}
					}
				);
			}
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			emHwnd.errAlert(e.getMessage());
		}
		finally
		{
			try
			{
				if (ia != null && ms != null && ! ms.isClosed())
					ms.leaveGroup(ia);
			}
			catch(Exception e)
			{
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}

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
