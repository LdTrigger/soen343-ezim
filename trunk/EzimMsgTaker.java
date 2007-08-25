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
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EzimMsgTaker extends Thread
{
	private EzimMain emHwnd;

	public EzimMsgTaker(EzimMain emIn)
	{
		this.emHwnd = emIn;
	}

	public void run()
	{
		ServerSocket ssck = null;
		Socket sckIn = null;

		try
		{
			ssck = new ServerSocket(Ezim.msgPort);

			while(true)
			{
				sckIn = ssck.accept();

				EzimContact ecTmp = this.emHwnd.getContact
				(
					((InetSocketAddress) sckIn.getRemoteSocketAddress())
						.getAddress().getHostAddress()
				);

				EzimMsgTakerThread emttTmp = new EzimMsgTakerThread
				(
					ecTmp
					, sckIn
				);
				emttTmp.run();
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
