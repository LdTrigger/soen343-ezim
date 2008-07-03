/*
    EZ Intranet Messenger
    Copyright (C) 2007 - 2008  Chun-Kwong Wong <chunkwong.wong@gmail.com>

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
import java.net.Socket;
import java.net.InetSocketAddress;
import javax.swing.JOptionPane;

import org.ezim.core.Ezim;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimDtxSemantics;
import org.ezim.core.EzimLang;
import org.ezim.ui.EzimMsgOut;

public class EzimMsgSender implements Runnable
{
	private EzimMsgOut emo;
	private String ip;
	private String sbj;
	private String msg;

	public EzimMsgSender
	(
		EzimMsgOut emoIn
		, String strIp
		, String strSbj
		, String strMsg
	)
	{
		this.emo = emoIn;
		this.ip = strIp;
		this.sbj = strSbj;
		this.msg = strMsg;
	}

	public void run()
	{
		Socket sckOut = null;
		InetSocketAddress isaTmp = null;

		EzimConf ecTmp = EzimConf.getInstance();

		try
		{
			// disable the send message window before proceeding
			this.emo.setEnabled(false);

			sckOut = new Socket();
			isaTmp = new InetSocketAddress
			(
				this.ip
				, Integer.parseInt
				(
					ecTmp.settings.getProperty(EzimConf.ezimDtxPort)
				)
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
				// ignore
			}
		}
	}
}
