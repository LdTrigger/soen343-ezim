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

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.lang.Thread;
import java.net.Socket;
import javax.swing.JOptionPane;

public class EzimMsgSender extends Thread
{
	private String ip;
	private String msg;

	public EzimMsgSender(String strIp, String strMsg)
	{
		this.ip = strIp;
		this.msg = strMsg;
	}

	public void run()
	{
		Socket sckOut = null;
		BufferedWriter bwTmp = null;

		try
		{
			sckOut = new Socket(this.ip, Ezim.msgPort);
			bwTmp = new BufferedWriter
			(
				new OutputStreamWriter
				(
					sckOut.getOutputStream()
					, Ezim.rtxEnc
				)
			);

			bwTmp.write(this.msg);
			bwTmp.flush();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog
			(
				null
				, e.getMessage()
				, "Send Message Error"
				, JOptionPane.ERROR_MESSAGE
			);
		}
		finally
		{
			try
			{
				bwTmp.close();
			}
			catch(Exception e)
			{
				// ignore
			}

			try
			{
				if (sckOut != null && ! sckOut.isClosed())
					sckOut.close();
			}
			catch(Exception e)
			{
				// ignore
			}
		}
	}
}
