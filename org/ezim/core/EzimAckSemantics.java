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

import org.ezim.core.EzimAckSender;
import org.ezim.core.EzimContact;
import org.ezim.ui.EzimMain;

public class EzimAckSemantics
{
	private final static String PREFIX
		= Ezim.appAbbrev.toUpperCase() + " ";

	private final static String POLL
		= EzimAckSemantics.PREFIX + "POLL:";
	private final static String ON
		= EzimAckSemantics.PREFIX + "ON:";
	private final static String OFF
		= EzimAckSemantics.PREFIX + "OFF:";
	private final static String SYSSTATE
		= EzimAckSemantics.PREFIX + "SYSSTATE:";
	private final static String STATE
		= EzimAckSemantics.PREFIX + "STATE:";
	private final static String STATUS
		= EzimAckSemantics.PREFIX + "STATUS:";
	private final static String SPEECH
		= EzimAckSemantics.PREFIX + "SPEECH:";

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
	 * used to acknowledge all other users to change system state
	 */
	public static String sysState(int iState)
	{
		return EzimAckSemantics.SYSSTATE + Integer.toString(iState);
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
	public static void parser(String strIp, String strAck)
	{
		EzimMain emTmp = EzimMain.getInstance();

		if (strAck.startsWith(EzimAckSemantics.POLL))
		{
			if (! strIp.equals(emTmp.localAddress))
			{
				emTmp.addContact
				(
					strIp
					, strAck.substring(EzimAckSemantics.POLL.length())
					, EzimContact.STATUS_DEFAULT
				);
			}

			EzimAckSender easTmp1 = new EzimAckSender
			(
				EzimAckSemantics.online(emTmp.localName)
			);
			easTmp1.start();

			EzimAckSender easTmp2 = new EzimAckSender
			(
				EzimAckSemantics.sysState(emTmp.localSysState)
			);
			easTmp2.start();

			EzimAckSender easTmp3 = new EzimAckSender
			(
				EzimAckSemantics.state(emTmp.localState)
			);
			easTmp3.start();

			EzimAckSender easTmp4 = new EzimAckSender
			(
				EzimAckSemantics.status(emTmp.localStatus)
			);
			easTmp4.start();
		}
		else if (strAck.startsWith(EzimAckSemantics.ON))
		{
			emTmp.addContact
			(
				strIp
				, strAck.substring(EzimAckSemantics.ON.length())
				, EzimContact.STATUS_DEFAULT
			);
		}
		else if (strAck.startsWith(EzimAckSemantics.OFF))
		{
			emTmp.rmContact(strIp);
		}
		else if (strAck.startsWith(EzimAckSemantics.SYSSTATE))
		{
			emTmp.updContactSysState
			(
				strIp
				, Integer.valueOf
					(
						strAck.substring
						(
							EzimAckSemantics.SYSSTATE.length()
						)
					)
			);
		}
		else if (strAck.startsWith(EzimAckSemantics.STATE))
		{
			emTmp.updContactState
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
			emTmp.updContactStatus
			(
				strIp
				, strAck.substring(EzimAckSemantics.STATUS.length())
			);
		}
		else if (strAck.startsWith(EzimAckSemantics.SPEECH))
		{
			emTmp.epMain.addSpeech
			(
				strIp
				, strAck.substring(EzimAckSemantics.SPEECH.length())
			);
		}

		return;
	}
}
