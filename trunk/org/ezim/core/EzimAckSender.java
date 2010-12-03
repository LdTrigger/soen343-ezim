/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2010  Chun-Kwong Wong
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

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimLogger;
import org.ezim.ui.EzimMain;
import org.ezim.ui.EzimPreferences;

public class EzimAckSender implements Runnable
{
	private static MulticastSocket ms;
	private static InetAddress ia;
	private static int iMcPort;

	private String msg;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the ACK sender class
	 * @param strIn ACK message to be sent
	 */
	public EzimAckSender(String strIn)
	{
		this.msg = strIn;
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * the method to be invoked
	 */
	public void run()
	{
		DatagramPacket dp = null;
		byte[] arrBytes = null;

		try
		{
			arrBytes = this.msg.getBytes(Ezim.dtxMsgEnc);
			if (arrBytes.length > Ezim.inBuf)
				throw new Exception("Ack message too long.");
			dp = new DatagramPacket
			(
				arrBytes
				, arrBytes.length
				, EzimAckSender.ia
				, EzimAckSender.iMcPort
			);

			synchronized(EzimAckSender.ms)
			{
				EzimAckSender.ms.send(dp);
			}
		}
		catch(Exception e)
		{
			EzimMain.getInstance().errAlert(e.getMessage());
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}

		return;
	}

	/**
	 * prepare a single multicast purposes which will be used throughout
	 * the program's lifetime
	 */
	public static void prepareSocket()
	{
		if (EzimAckSender.ms == null)
		{
			try
			{
				EzimConf ecTmp = EzimConf.getInstance();

				EzimAckSender.ia = InetAddress.getByName
				(
					ecTmp.settings.getProperty(EzimConf.ezimMcGroup)
				);

				EzimAckSender.iMcPort = Integer.parseInt
				(
					ecTmp.settings.getProperty(EzimConf.ezimMcPort)
				);

				EzimAckSender.ms = new MulticastSocket(iMcPort);
				EzimAckSender.ms.setNetworkInterface(Ezim.operatingNI);
				EzimAckSender.ms.setReuseAddress(true);
				EzimAckSender.ms.setTimeToLive(Ezim.ttl);

				if (EzimAckSender.ms.getLoopbackMode())
					EzimAckSender.ms.setLoopbackMode(false);
			}
			catch(Exception e)
			{
				EzimMain emHwnd = EzimMain.getInstance();

				EzimLogger.getInstance().severe(e.getMessage(), e);
				emHwnd.errAlert(e.getMessage());

				new EzimPreferences();
				emHwnd.panic();

				System.exit(1);
			}
		}
	}
}
