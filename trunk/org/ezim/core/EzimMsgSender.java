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

import java.awt.event.WindowEvent;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.ezim.core.Ezim;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimDtxSemantics;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimLogger;

import org.ezim.ui.EzimMain;
import org.ezim.ui.EzimMsgOut;

public class EzimMsgSender implements Runnable
{
	private EzimMsgOut emo = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the message sender
	 * @param emoIn the associated outgoing message window
	 */
	public EzimMsgSender(EzimMsgOut emoIn)
	{
		this.emo = emoIn;
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * the method to be invoked
	 */
	public void run()
	{
		List<EzimContact> alEc = this.emo.getContacts();
		final List<EzimContact> lNg
			= new ArrayList<EzimContact>(alEc);
		final String sbj = this.emo.getSubject();
		final String msg = this.emo.getBody();

		// close the send message window
		WindowEvent weTmp = new WindowEvent
		(
			this.emo
			, WindowEvent.WINDOW_CLOSING
		);
		this.emo.dispatchEvent(weTmp);

		ThreadPoolExecutor extr = new ThreadPoolExecutor
		(
			alEc.size()
			, alEc.size()
			, 1
			, TimeUnit.MINUTES
			, new LinkedBlockingQueue<Runnable>()
		);

		for(final EzimContact ec: alEc)
		{
			final Socket sckOut = new Socket();

			final InetSocketAddress isaTmp = new InetSocketAddress
			(
				ec.getAddress()
				, ec.getPort()
			);

			extr.execute
			(
				new Runnable()
				{
					public void run()
					{
						try
						{
							sckOut.bind
							(
								new InetSocketAddress
								(
									Ezim.localAddress
									, 0
								)
							);
							sckOut.connect(isaTmp, Ezim.dtxTimeout);

							EzimDtxSemantics.sendMsg(sckOut, sbj, msg);

							synchronized(lNg)
							{
								lNg.remove(ec);
								((ArrayList) lNg).trimToSize();
							}
						}
						catch(Exception e)
						{
							EzimLogger.getInstance().warning
							(
								e.getMessage()
								, e
							);
						}
						finally
						{
							try
							{
								if (sckOut != null && ! sckOut.isClosed())
									sckOut.close();
							}
							catch(Exception e)
							{
								EzimLogger.getInstance().severe
								(
									e.getMessage()
									, e
								);
							}
						}
					}
				}
			);
		}

		extr.shutdown();

		try
		{
			extr.awaitTermination(1, TimeUnit.MINUTES);
		}
		catch(InterruptedException ie)
		{
			EzimLogger.getInstance().warning
			(
				ie.getMessage()
				, ie
			);
		}

		// open outgoing message window for contacts could not sent
		if (lNg.size() > 0)
		{
			EzimMsgOut emoTmp = new EzimMsgOut(lNg, sbj, msg);

			EzimMain.showError
			(
				emoTmp
				, EzimLang.RecipientsError
				, EzimLang.SendMessageError
			);
		}
	}
}
