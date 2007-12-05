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

import java.util.ResourceBundle;

public class EzimLang
{
	// username input dialog
	public static String PleaseInputYourName;

	// about dialog
	public static String Notice;
	public static String About;

	// main window
	public static String Status;
	public static String Message;
	public static String SendMessageToContact;
	public static String Refresh;
	public static String RefreshContactList;
	public static String Plaza;

	// error dialog
	public static String Error;

	// incoming message window
	public static String IncomingMessage;
	public static String From;
	public static String ClickHereToOpenMessage;
	public static String Reply;
	public static String OriginalMessageFrom;

	// outgoing message window
	public static String OutgoingMessage;
	public static String To;
	public static String Send;

	// send message error dialog
	public static String SendMessageError;

	// plaza of speech
	public static String PlazaOfSpeech;
	public static String Speak;
	public static String HasJoinedPlazaOfSpeech;
	public static String HasLeftPlazaOfSpeech;

	public static void init()
	{
		try
		{
			ResourceBundle rbTmp = ResourceBundle.getBundle
			(
				"org/ezim/properties/EzimUI"
			);

			// username input dialog
			PleaseInputYourName = rbTmp.getString("PLEASE_INPUT_YOUR_NAME");

			// about dialog
			Notice = Ezim.appName + "\n"
			 + "version " + Ezim.appVer + "\n\n"
			 + rbTmp.getString("NOTICE");
			About = rbTmp.getString("ABOUT");

			// main window
			Status = rbTmp.getString("STATUS");
			Message = rbTmp.getString("MESSAGE");
			SendMessageToContact = rbTmp.getString("SEND_MESSAGE_TO_CONTACT");
			Refresh = rbTmp.getString("REFRESH");
			RefreshContactList = rbTmp.getString("REFRESH_CONTACT_LIST");
			Plaza = rbTmp.getString("PLAZA");

			// error dialog
			Error = rbTmp.getString("ERROR");

			// incoming message window
			IncomingMessage = rbTmp.getString("INCOMING_MESSAGE");
			From = rbTmp.getString("FROM");
			ClickHereToOpenMessage = rbTmp.getString("CLICK_HERE_TO_OPEN_MESSAGE");
			Reply = rbTmp.getString("REPLY");
			OriginalMessageFrom = rbTmp.getString("ORIGINAL_MESSAGE_FROM");

			// outgoing message window
			OutgoingMessage = rbTmp.getString("OUTGOING_MESSAGE");
			To = rbTmp.getString("TO");
			Send = rbTmp.getString("SEND");

			// send message error dialog
			SendMessageError = rbTmp.getString("SEND_MESSAGE_ERROR");

			// plaza of speech
			PlazaOfSpeech = rbTmp.getString("PLAZA_OF_SPEECH");
			Speak = rbTmp.getString("SPEAK");
			HasJoinedPlazaOfSpeech = rbTmp.getString("HAS_JOINED_PLAZA_OF_SPEECH");
			HasLeftPlazaOfSpeech = rbTmp.getString("HAS_LEFT_PLAZA_OF_SPEECH");
		}
		catch(Exception e)
		{
			// properties file is obligatory
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}
