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

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.ezim.core.Ezim;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactList;
import org.ezim.core.EzimDtxTakerThread;
import org.ezim.core.EzimLogger;
import org.ezim.core.EzimThreadPool;
import org.ezim.ui.EzimMain;

public class EzimDtxTaker implements Runnable
{
	// P R O P E R T I E S -------------------------------------------------
	// server socket where DTX data comes from
	private ServerSocket ssck = null;

	// singleton object
	private static EzimDtxTaker dtxTaker = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the DTX taker class
	 */
	private EzimDtxTaker()
	{
	}

	// P R I V A T E -------------------------------------------------------
	/**
	 * DTX data receiving loop
	 */
	private void loop()
	{
		Thread thTmp = Thread.currentThread();
		Socket sckTmp = null;

		while(! thTmp.isInterrupted())
		{
			try
			{
				sckTmp = this.ssck.accept();
				sckTmp.setSoTimeout(Ezim.dtxTimeout);

				EzimContact ecTmp = EzimContactList.getInstance().getContact
				(
					((InetSocketAddress) sckTmp.getRemoteSocketAddress())
						.getAddress()
				);

				// only take messages from known contacts
				if (ecTmp != null)
				{
					EzimDtxTakerThread emttTmp = new EzimDtxTakerThread
					(
						ecTmp
						, sckTmp
					);

					EzimThreadPool etpTmp = EzimThreadPool.getInstance();

					etpTmp.execute(emttTmp);
				}
				else if (sckTmp != null && ! sckTmp.isClosed())
				{
					sckTmp.close();
					sckTmp = null;
				}
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
	 * return an EzimDtxTaker object
	 */
	public static EzimDtxTaker getInstance()
	{
		if (EzimDtxTaker.dtxTaker == null)
			EzimDtxTaker.dtxTaker = new EzimDtxTaker();

		return EzimDtxTaker.dtxTaker;
	}

	/**
	 * the method to be invoked
	 */
	public void run()
	{
		try
		{
			this.ssck = new ServerSocket();
			this.ssck.bind
			(
				new InetSocketAddress
				(
					Ezim.localAddress
					, Ezim.localDtxPort
				)
			);

			this.loop();
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			EzimMain.showError(EzimLang.DtxPort + "\n" + e.getMessage());
			EzimMain.getInstance().panic(1);
		}
		finally
		{
			try
			{
				if (this.ssck != null && ! this.ssck.isClosed())
					this.ssck.close();
			}
			catch(Exception exp)
			{
				EzimLogger.getInstance().severe(exp.getMessage(), exp);
			}
		}
	}

	/**
	 * close the underlying server socket
	 */
	public void closeSocket()
	{
		try
		{
			if (this.ssck != null && ! this.ssck.isClosed())
				this.ssck.close();
		}
		catch(Exception exp)
		{
			EzimLogger.getInstance().severe(exp.getMessage(), exp);
		}
	}
}
