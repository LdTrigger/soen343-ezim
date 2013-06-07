/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2013  Chun-Kwong Wong
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
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import org.ezim.core.Ezim;
import org.ezim.core.EzimAckSemantics;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimLogger;
import org.ezim.core.EzimThreadPool;
import org.ezim.ui.EzimMain;
import org.ezim.ui.EzimPreferences;

public class EzimAckTaker implements Runnable
{
	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the ACK taker class
	 */
	public EzimAckTaker()
	{
	}

	// P R I V A T E -------------------------------------------------------
	/**
	 * ACK data receiving loop
	 * @param msIn multicast socket where ACK data comes from
	 */
	private void loop(MulticastSocket msIn)
	{
		byte[] arrBytes = new byte[Ezim.inBuf];
		DatagramPacket dp = new DatagramPacket(arrBytes, arrBytes.length);

		while(Ezim.isRunning())
		{
			try
			{
				msIn.receive(dp);

				final InetAddress iaAck = dp.getAddress();

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
								iaAck
								, strAck
							);
						}
					}
				);
			}
			catch(Exception e)
			{
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}
		}
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * the method to be invoked
	 */
	public void run()
	{
		int iPort = 0;
		InetAddress iaMc = null;
		InetSocketAddress isaMc = null;
		MulticastSocket ms = null;

		EzimConf ecTmp = EzimConf.getInstance();

		try
		{
			iPort = Integer.parseInt
			(
				ecTmp.settings.getProperty(EzimConf.ezimMcPort)
			);
			iaMc = InetAddress.getByName
			(
				ecTmp.settings.getProperty(EzimConf.ezimMcGroup)
			);

			isaMc = new InetSocketAddress(iaMc, iPort);

			try
			{
				// we have to do this on Linux, or it will cross talk
				ms = new MulticastSocket(isaMc);
			}
			catch(java.net.BindException be)
			{
				// Windows and Mac fallback
				ms = new MulticastSocket();
			}

			ms.setReuseAddress(true);

			// Review:2012-11-10:this is NO effect on Linux
			ms.joinGroup(isaMc, Ezim.localNI);

			this.loop(ms);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			EzimMain.showError(EzimLang.McPort + "\n" + e.getMessage());

			new EzimPreferences();
			EzimMain.getInstance().panic();
		}
		finally
		{
			try
			{
				if (isaMc != null && ms != null && ! ms.isClosed())
					ms.leaveGroup(isaMc, Ezim.localNI);
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
	}
}
