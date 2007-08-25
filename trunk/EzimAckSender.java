/*
    Java Intranet Messenger
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

import java.lang.Thread;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

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
		DatagramPacket dp = null;
		byte[] arrBytes = null;

		try
		{
			ia = InetAddress.getByName(Ezim.mcGroup);

			ms = new MulticastSocket(Ezim.ackPort);
			ms.setReuseAddress(true);
			ms.setTimeToLive(Ezim.ttl);
			if (ms.getLoopbackMode()) ms.setLoopbackMode(false);

			arrBytes = this.msg.getBytes(Ezim.rtxEnc);
			dp = new DatagramPacket
			(
				arrBytes
				, arrBytes.length
				, ia
				, Ezim.ackPort
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
