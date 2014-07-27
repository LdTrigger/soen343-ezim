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

public class EzimAckTaker implements Runnable
{
	// P R O P E R T I E S -------------------------------------------------
	private InetSocketAddress isaMc = null;

	// multicast socket where ACK data comes from
	private MulticastSocket ms = null;

	// singleton object
	private static EzimAckTaker ackTaker = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the ACK taker class
	 */
	private EzimAckTaker()
	{
	}

	// P R I V A T E -------------------------------------------------------
	/**
	 * ACK data receiving loop
	 */
	private void loop()
	{
		Thread thTmp = Thread.currentThread();
		byte[] arrBytes = new byte[Ezim.inBuf];
		DatagramPacket dp = new DatagramPacket(arrBytes, arrBytes.length);

		while(! thTmp.isInterrupted())
		{
			try
			{
				this.ms.receive(dp);

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
				if (! thTmp.isInterrupted())
					EzimLogger.getInstance().severe(e.getMessage(), e);
			}
		}
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * return an EzimAckTaker object
	 */
	public static EzimAckTaker getInstance()
	{
		if (EzimAckTaker.ackTaker == null)
			EzimAckTaker.ackTaker = new EzimAckTaker();

		return EzimAckTaker.ackTaker;
	}

	/**
	 * the method to be invoked
	 */
	public void run()
	{
		int iPort = 0;
		InetAddress iaMc = null;

		try
		{
			iPort = EzimConf.NET_MC_PORT;
			iaMc = InetAddress.getByName(EzimConf.NET_MC_GROUP);

			this.isaMc = new InetSocketAddress(iaMc, iPort);

			this.ms = new MulticastSocket(iPort);
			this.ms.setReuseAddress(true);
			this.ms.setInterface(Ezim.localAddress);

			// Review:2012-11-10:this is NO effect on Linux
			this.ms.joinGroup(this.isaMc, Ezim.localNI);

			this.loop();
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			EzimMain.showError(EzimLang.McPort + "\n" + e.getMessage());
			EzimMain.getInstance().panic(1);
		}
		finally
		{
			try
			{
				if
				(
					this.isaMc != null
					&& this.ms != null
					&& ! this.ms.isClosed()
				)
					this.ms.leaveGroup(isaMc, Ezim.localNI);
			}
			catch(Exception e)
			{
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}

			try
			{
				if (this.ms != null && ! this.ms.isClosed())
					this.ms.close();
			}
			catch(Exception e)
			{
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}
		}
	}

	/**
	 * close the underlying multicast socket
	 */
	public void closeSocket()
	{
		try
		{
			if
			(
				this.isaMc != null
				&& this.ms != null
				&& ! this.ms.isClosed()
			)
				this.ms.leaveGroup(isaMc, Ezim.localNI);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}

		try
		{
			if (this.ms != null && ! this.ms.isClosed())
				this.ms.close();
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}
	}
}
