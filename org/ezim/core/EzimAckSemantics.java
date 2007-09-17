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

import org.ezim.core.EzimAckSender;
import org.ezim.core.EzimContact;
import org.ezim.ui.EzimMain;

public class EzimAckSemantics
{
	private final static String POLL	= Ezim.appAbbrev + "POLL:";
	private final static String ON		= Ezim.appAbbrev + "ON:";
	private final static String OFF		= Ezim.appAbbrev + "OFF:";
	private final static String STATE	= Ezim.appAbbrev + "STATE:";
	private final static String STATUS	= Ezim.appAbbrev + "STATUS:";
	private final static String SPEECH	= Ezim.appAbbrev + "SPEECH:";

	/**
	 * used when the application starts to poll from all existing users
	 * @param strName local user name
	 */
	public static String poll(String strName)
	{
		return EzimAckSemantics.POLL + strName;
	}

	/**
	 * used to reply someone else's polling
	 * @param strName local user name
	 */
	public static String online(String strName)
	{
		return EzimAckSemantics.ON + strName;
	}

	/**
	 * used to acknowledge all other users we're going to disconnect
	 */
	public static String offline()
	{
		return EzimAckSemantics.OFF;
	}

	/**
	 * used to acknowledge all other users to change state
	 */
	public static String state(int iState)
	{
		return EzimAckSemantics.STATE + Integer.toString(iState);
	}

	/**
	 * used to acknowledge all users to change the status
	 */
	public static String status(String strStatus)
	{
		return EzimAckSemantics.STATUS + strStatus;
	}

	/**
	 * used to speak publicly in the plaza
	 */
	public static String speech(String strSpeech)
	{
		return EzimAckSemantics.SPEECH + strSpeech;
	}

	/**
	 * parse all incoming acknowledge broadcast messages and react
	 * accordingly
	 */
	public static void parser(EzimMain emIn, String strIp, String strAck)
	{
		if (strAck.startsWith(EzimAckSemantics.POLL))
		{
			if (! strIp.equals(emIn.localAddress))
			{
				emIn.addContact
				(
					strIp
					, strAck.substring(EzimAckSemantics.POLL.length())
					, EzimContact.DEFAULT_STATUS
				);
			}

			EzimAckSender easTmp1 = new EzimAckSender
			(
				emIn
				, EzimAckSemantics.online(emIn.localName)
			);
			easTmp1.start();

			try
			{
				Thread.sleep(500);
			}
			catch(Exception e)
			{
				// ignore whatever
			}

			EzimAckSender easTmp2 = new EzimAckSender
			(
				emIn
				, EzimAckSemantics.state(emIn.localState)
			);
			easTmp2.start();

			try
			{
				Thread.sleep(500);
			}
			catch(Exception e)
			{
				// ignore whatever
			}

			EzimAckSender easTmp3 = new EzimAckSender
			(
				emIn
				, EzimAckSemantics.status(emIn.localStatus)
			);
			easTmp3.start();
		}
		else if (strAck.startsWith(EzimAckSemantics.ON))
		{
			emIn.addContact
			(
				strIp
				, strAck.substring(EzimAckSemantics.ON.length())
				, EzimContact.DEFAULT_STATUS
			);
		}
		else if (strAck.startsWith(EzimAckSemantics.OFF))
		{
			emIn.rmContact(strIp);
		}
		else if (strAck.startsWith(EzimAckSemantics.STATE))
		{
			emIn.updContactState
			(
				strIp
				, Integer.valueOf
					(
						strAck.substring
						(
							EzimAckSemantics.STATE.length()
						)
					)
			);
		}
		else if (strAck.startsWith(EzimAckSemantics.STATUS))
		{
			emIn.updContactStatus
			(
				strIp
				, strAck.substring(EzimAckSemantics.STATUS.length())
			);
		}
		else if (strAck.startsWith(EzimAckSemantics.SPEECH))
		{
			emIn.epMain.addSpeech
			(
				strIp
				, strAck.substring(EzimAckSemantics.SPEECH.length())
			);
		}

		return;
	}
}
