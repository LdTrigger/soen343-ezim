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
import org.ezim.ui.EzimPreferences;

public class EzimDtxTaker implements Runnable
{
	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the DTX taker class
	 */
	public EzimDtxTaker()
	{
	}

	// P R I V A T E -------------------------------------------------------
	/**
	 * DTX data receiving loop
	 * @param ssckIn server socket where DTX data comes from
	 */
	private void loop(ServerSocket ssckIn)
	{
		Socket sckTmp = null;

		while(Ezim.isRunning())
		{
			try
			{
				sckTmp = ssckIn.accept();
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
		ServerSocket ssck = null;

		try
		{
			ssck = new ServerSocket();
			ssck.bind
			(
				new InetSocketAddress
				(
					Ezim.localAddress
					, Ezim.localDtxPort
				)
			);

			this.loop(ssck);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			EzimMain.showError(EzimLang.DtxPort + "\n" + e.getMessage());

			new EzimPreferences();
			EzimMain.getInstance().panic();
		}
		finally
		{
			try
			{
				if (ssck != null && ! ssck.isClosed()) ssck.close();
			}
			catch(Exception exp)
			{
				EzimLogger.getInstance().severe(exp.getMessage(), exp);
			}
		}
	}
}
