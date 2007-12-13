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
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.ezim.core.EzimAckSemantics;
import org.ezim.ui.EzimMain;

public class EzimAckTaker extends Thread
{
	private EzimMain emHwnd;

	public EzimAckTaker(EzimMain emIn)
	{
		this.emHwnd = emIn;
	}

	public void run()
	{
		MulticastSocket ms = null;
		InetAddress ia = null;
		DatagramPacket dp = null;
		byte[] arrBytes = new byte[Ezim.inBuf];
		String strTmp = null;

		try
		{
			ia = InetAddress.getByName(Ezim.mcGroup);

			ms = new MulticastSocket(Ezim.ackPort);
			ms.setReuseAddress(true);
			ms.joinGroup(ia);

			dp = new DatagramPacket(arrBytes, arrBytes.length);

			while(true)
			{
				ms.receive(dp);

				strTmp = new String
				(
					dp.getData()
					, 0
					, dp.getLength()
					, Ezim.rtxEnc
				);

				EzimAckSemantics.parser
				(
					this.emHwnd
					, dp.getAddress().getHostAddress()
					, strTmp
				);
			}
		}
		catch(Exception e)
		{
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
				// ignore
			}

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
