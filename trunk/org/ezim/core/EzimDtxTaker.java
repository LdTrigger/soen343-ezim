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
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimDtxTakerThread;
import org.ezim.ui.EzimMain;

public class EzimDtxTaker extends Thread
{
	private EzimMain emHwnd;

	public EzimDtxTaker(EzimMain emIn)
	{
		this.emHwnd = emIn;
	}

	public void run()
	{
		ServerSocket ssck = null;
		Socket sckIn = null;

		EzimConf ecnfTmp = EzimConf.getInstance();

		try
		{
			ssck = new ServerSocket
			(
				Integer.parseInt
				(
					ecnfTmp.settings.getProperty(EzimConf.ezimDtxPort)
				)
			);
		}
		catch(Exception e)
		{
			emHwnd.errAlert(e.getMessage());

			try
			{
				if (ssck != null && ! ssck.isClosed()) ssck.close();
			}
			catch(Exception exp)
			{
				// ignore
			}

			// only one instance of the application is allowed at one time
			System.exit(1);
		}

		try
		{
			while(true)
			{
				sckIn = ssck.accept();
				sckIn.setSoTimeout(Ezim.dtxTimeout);

				EzimContact ecTmp = EzimContactList.getInstance().get
				(
					((InetSocketAddress) sckIn.getRemoteSocketAddress())
						.getAddress().getHostAddress()
				);

				// only take messages from known contacts
				if (ecTmp != null)
				{
					EzimDtxTakerThread emttTmp = new EzimDtxTakerThread
					(
						ecTmp
						, sckIn
					);
					emttTmp.start();
				}
				else if (sckIn != null && ! sckIn.isClosed())
				{
					sckIn.close();
					sckIn = null;
				}
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
				if (ssck != null && ! ssck.isClosed()) ssck.close();
			}
			catch(Exception e)
			{
				// ignore
			}
		}
	}
}
