/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2009  Chun-Kwong Wong
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

import java.awt.event.WindowEvent;
import java.lang.Runnable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

import org.ezim.core.Ezim;
import org.ezim.core.EzimDtxSemantics;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimLogger;
import org.ezim.ui.EzimMsgOut;

public class EzimMsgSender implements Runnable
{
	private EzimMsgOut emo = null;
	private InetAddress addr = null;
	private int port = -1;
	private String sbj = null;
	private String msg = null;

	public EzimMsgSender
	(
		EzimMsgOut emoIn
		, InetAddress iaIn
		, int iPort
		, String strSbj
		, String strMsg
	)
	{
		this.emo = emoIn;
		this.addr = iaIn;
		this.port = iPort;
		this.sbj = strSbj;
		this.msg = strMsg;
	}

	public void run()
	{
		Socket sckOut = null;
		InetSocketAddress isaTmp = null;

		try
		{
			// disable the send message window before proceeding
			this.emo.setEnabled(false);

			sckOut = new Socket();
			sckOut.bind
			(
				new InetSocketAddress
				(
					Ezim.localAddress
					, 0
				)
			);
			isaTmp = new InetSocketAddress
			(
				this.addr
				, this.port
			);
			sckOut.connect(isaTmp, Ezim.dtxTimeout);

			EzimDtxSemantics.sendMsg(sckOut, this.sbj, this.msg);

			// close the send message window upon success
			WindowEvent weTmp = new WindowEvent
			(
				this.emo
				, WindowEvent.WINDOW_CLOSING
			);

			this.emo.dispatchEvent(weTmp);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().warning(e.getMessage(), e);

			JOptionPane.showMessageDialog
			(
				null
				, e.getMessage()
				, EzimLang.SendMessageError
				, JOptionPane.ERROR_MESSAGE
			);

			// re-enable the send message window upon failure
			this.emo.setEnabled(true);
		}
		finally
		{
			try
			{
				if (sckOut != null && ! sckOut.isClosed()) sckOut.close();
			}
			catch(Exception e)
			{
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}
		}
	}
}
