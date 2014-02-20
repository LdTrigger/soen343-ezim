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

import java.net.InetAddress;

import org.ezim.core.Ezim;
import org.ezim.core.EzimAckSender;
import org.ezim.core.EzimContactList;
import org.ezim.core.EzimThreadPool;
import org.ezim.ui.EzimMain;

public class EzimAckSemantics
{
	private final static String NEWLINE		= "\n";
	private final static String PREFIX		= Ezim.appAbbrev.toUpperCase();
	private final static String PORT		= "PORT:";
	private final static String NAME		= "NAME:";
	private final static String SYSSTATE	= "SYSSTATE:";
	private final static String STATE		= "STATE:";
	private final static String STATUS		= "STATUS:";
	private final static String SPEECH		= "SPEECH:";
	private final static String POLL		= "POLL:";
	private final static String OFF			= "OFF:";

	/**
	 * used to poll from a user specified by IP address
	 * @param iaIn address of the user to poll from
	 */
	public static String poll(InetAddress iaIn)
	{
		StringBuffer sbOut = new StringBuffer();

		sbOut.append(EzimAckSemantics.PREFIX);
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.POLL);

		if (iaIn != null) sbOut.append(iaIn.getHostAddress());

		sbOut.append(EzimAckSemantics.NEWLINE);

		return sbOut.toString();
	}

	/**
	 * used to poll from a user specified by IP address
	 */
	public static String poll()
	{
		return EzimAckSemantics.poll(null);
	}

	/**
	 * used to reply someone else's polling
	 * @param iPort DTX port
	 * @param strName local user name
	 * @param iSysState system state
	 * @param iState user state
	 * @param strStatus status
	 */
	public static String allInfo
	(
		int iPort
		, String strName
		, int iSysState
		, int iState
		, String strStatus
	)
	{
		StringBuffer sbOut = new StringBuffer();

		sbOut.append(EzimAckSemantics.PREFIX);
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.PORT);
		sbOut.append(Integer.toString(iPort));
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.NAME);
		sbOut.append(strName);
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.SYSSTATE);
		sbOut.append(Integer.toString(iSysState));
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.STATE);
		sbOut.append(Integer.toString(iState));
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.STATUS);
		sbOut.append(strStatus);
		sbOut.append(EzimAckSemantics.NEWLINE);

		return sbOut.toString();
	}

	/**
	 * used to acknowledge all other users we're going to disconnect
	 */
	public static String offline()
	{
		StringBuffer sbOut = new StringBuffer();

		sbOut.append(EzimAckSemantics.PREFIX);
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.OFF);
		sbOut.append(EzimAckSemantics.NEWLINE);

		return sbOut.toString();
	}

	/**
	 * used to acknowledge all other users to change system state
	 * @param iState system state
	 */
	public static String sysState(int iState)
	{
		StringBuffer sbOut = new StringBuffer();

		sbOut.append(EzimAckSemantics.PREFIX);
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.SYSSTATE);
		sbOut.append(Integer.toString(iState));
		sbOut.append(EzimAckSemantics.NEWLINE);

		return sbOut.toString();
	}

	/**
	 * used to acknowledge all other users to change state
	 * @param iState user state
	 */
	public static String state(int iState)
	{
		StringBuffer sbOut = new StringBuffer();

		sbOut.append(EzimAckSemantics.PREFIX);
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.STATE);
		sbOut.append(Integer.toString(iState));
		sbOut.append(EzimAckSemantics.NEWLINE);

		return sbOut.toString();
	}

	/**
	 * used to acknowledge all users to change the status
	 * @param strStatus status
	 */
	public static String status(String strStatus)
	{
		StringBuffer sbOut = new StringBuffer();

		sbOut.append(EzimAckSemantics.PREFIX);
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.STATUS);
		sbOut.append(strStatus);
		sbOut.append(EzimAckSemantics.NEWLINE);

		return sbOut.toString();
	}

	/**
	 * used to speak publicly in the plaza
	 * @param strSpeech speech
	 */
	public static String speech(String strSpeech)
	{
		StringBuffer sbOut = new StringBuffer();

		sbOut.append(EzimAckSemantics.PREFIX);
		sbOut.append(EzimAckSemantics.NEWLINE);
		sbOut.append(EzimAckSemantics.SPEECH);
		sbOut.append(strSpeech);
		sbOut.append(EzimAckSemantics.NEWLINE);

		return sbOut.toString();
	}

	/**
	 * parse all incoming acknowledge broadcast messages and react
	 * accordingly
	 * @param iaIn sender's address
	 * @param strAck acknowledge broadcast message to be parsed
	 */
	public static void parser(InetAddress iaIn, String strAck)
	{
		EzimMain emTmp = EzimMain.getInstance();
		EzimContactList eclTmp = EzimContactList.getInstance();

		String[] arrAckLines = strAck.split(EzimAckSemantics.NEWLINE);

		int iCnt = 0;
		int iLen = arrAckLines.length;
		String strLine = null;

		boolean blnAllInfo = false;
		boolean blnOff = false;

		int iPort = -1;
		String strName = null;
		int iSysState = -1;
		int iState = -1;
		String strStatus = null;
		String strSpeech = null;

		if
		(
			arrAckLines == null
			|| arrAckLines.length == 0
			|| ! arrAckLines[0].equals(EzimAckSemantics.PREFIX)
		)
		{
			return;
		}

		for(iCnt = 1; iCnt < iLen; iCnt ++)
		{
			strLine = arrAckLines[iCnt];

			if (strLine.length() == 0)
			{
				break;
			}
			else if
			(
				strLine.startsWith(EzimAckSemantics.POLL)
				&& ! Ezim.localAddress.equals(iaIn)
			)
			{
				String strPollIp = strLine.substring
				(
					EzimAckSemantics.POLL.length()
				);

				try
				{
					if
					(
						strPollIp.length() == 0
						|| Ezim.localAddress.equals
							(
								InetAddress.getByName(strPollIp)
							)
					)
					{
						blnAllInfo = true;
					}
				}
				catch(Exception e)
				{
					EzimLogger.getInstance().warning(e.getMessage(), e);
				}
			}
			else if (strLine.startsWith(EzimAckSemantics.OFF))
			{
				blnOff = true;
			}
			else if (strLine.startsWith(EzimAckSemantics.PORT))
			{
				iPort = Integer.valueOf
				(
					strLine.substring
					(
						EzimAckSemantics.PORT.length()
					)
				);
			}
			else if (strLine.startsWith(EzimAckSemantics.NAME))
			{
				strName = strLine.substring(EzimAckSemantics.NAME.length());
			}
			else if (strLine.startsWith(EzimAckSemantics.SYSSTATE))
			{
				iSysState = Integer.valueOf
				(
					strLine.substring
					(
						EzimAckSemantics.SYSSTATE.length()
					)
				);
			}
			else if (strLine.startsWith(EzimAckSemantics.STATE))
			{
				iState = Integer.valueOf
				(
					strLine.substring
					(
						EzimAckSemantics.STATE.length()
					)
				);
			}
			else if (strLine.startsWith(EzimAckSemantics.STATUS))
			{
				strStatus = strLine.substring
				(
					EzimAckSemantics.STATUS.length()
				);
			}
			else if (strLine.startsWith(EzimAckSemantics.SPEECH))
			{
				strSpeech = strLine.substring
				(
					EzimAckSemantics.SPEECH.length()
				);
			}
		}

		// update ACK info of the sender
		if (blnOff)
		{
			eclTmp.rmContact(iaIn);
		}
		else if (eclTmp.getContact(iaIn) == null)
		{
			if (iPort > 0 && iPort < 65536 && strName != null)
			{
				eclTmp.addContact
				(
					iaIn
					, iPort
					, strName
					, iSysState
					, iState
					, strStatus
				);
			}
			else
			{
				EzimThreadPool etpTmp = EzimThreadPool.getInstance();
				EzimAckSender easTmp = new EzimAckSender
				(
					EzimAckSemantics.poll
					(
						iaIn
					)
				);

				etpTmp.execute(easTmp);
			}

			if (! Ezim.localAddress.equals(iaIn))
			{
				blnAllInfo = true;
			}
		}
		else
		{
			if (iPort > 0 && iPort < 65536)
			{
				eclTmp.updContactPort
				(
					iaIn
					, iPort
				);
			}

			if (strName != null && strName.length() > 0)
			{
				eclTmp.updContactName
				(
					iaIn
					, strName
				);
			}

			if (iSysState > -1)
			{
				eclTmp.updContactSysState
				(
					iaIn
					, iSysState
				);
			}

			if (iState > -1)
			{
				eclTmp.updContactState
				(
					iaIn
					, iState
				);
			}

			if (strStatus != null && strStatus.length() > 0)
			{
				eclTmp.updContactStatus
				(
					iaIn
					, strStatus
				);
			}
		}

		// update plaza speech
		if (! blnOff && strSpeech != null && strSpeech.length() > 0)
		{
			emTmp.epMain.addSpeech
			(
				iaIn
				, strSpeech
			);
		}

		// send back info if necessary
		if (blnAllInfo)
		{
			EzimAckSemantics.sendAllInfo();
		}
	}

	/**
	 * send out all local ACK info
	 */
	public static void sendAllInfo()
	{
		EzimMain emTmp = EzimMain.getInstance();
		EzimThreadPool etpTmp = EzimThreadPool.getInstance();
		EzimAckSender easTmp = new EzimAckSender
		(
			EzimAckSemantics.allInfo
			(
				Ezim.localDtxPort
				, Ezim.localName
				, emTmp.localSysState
				, emTmp.localState
				, emTmp.localStatus
			)
		);

		etpTmp.execute(easTmp);
	}
}
