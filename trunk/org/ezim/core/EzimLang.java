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
	public static String SendMessageToContact;
	public static String SendFileToContact;
	public static String RefreshContactList;
	public static String PlazaOfSpeech;

	// error dialog
	public static String Error;

	// incoming message window
	public static String IncomingMessage;
	public static String From;
	public static String Subject;
	public static String ClickHereToOpenMessage;
	public static String Reply;
	public static String OriginalMessageFrom;

	// outgoing message window
	public static String OutgoingMessage;
	public static String To;
	public static String Send;

	// send message error dialog
	public static String SendMessageError;

	// incoming file window
	public static String IncomingFile;
	public static String ReceiveFile;
	public static String Yes;
	public static String No;
	public static String Receiving;

	// outgoing file window
	public static String OutgoingFile;
	public static String Filename;
	public static String Cancel;
	public static String WaitingForResponse;
	public static String Sending;
	public static String RefusedByRemote;
	public static String TransmissionAbortedByRemote;
	public static String Done;
	public static String Close;

	// plaza of speech
	public static String Speak;
	public static String HasJoinedPlazaOfSpeech;
	public static String HasLeftPlazaOfSpeech;

	// tray icon pop-up menu
	public static String Show;
	public static String Exit;

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
			SendMessageToContact = rbTmp.getString("SEND_MESSAGE_TO_CONTACT");
			SendFileToContact = rbTmp.getString("SEND_FILE_TO_CONTACT");
			RefreshContactList = rbTmp.getString("REFRESH_CONTACT_LIST");
			PlazaOfSpeech = rbTmp.getString("PLAZA_OF_SPEECH");

			// error dialog
			Error = rbTmp.getString("ERROR");

			// incoming message window
			IncomingMessage = rbTmp.getString("INCOMING_MESSAGE");
			From = rbTmp.getString("FROM");
			Subject = rbTmp.getString("SUBJECT");
			ClickHereToOpenMessage = rbTmp.getString("CLICK_HERE_TO_OPEN_MESSAGE");
			Reply = rbTmp.getString("REPLY");
			OriginalMessageFrom = rbTmp.getString("ORIGINAL_MESSAGE_FROM");

			// outgoing message window
			OutgoingMessage = rbTmp.getString("OUTGOING_MESSAGE");
			To = rbTmp.getString("TO");
			Send = rbTmp.getString("SEND");

			// send message error dialog
			SendMessageError = rbTmp.getString("SEND_MESSAGE_ERROR");

			// incoming file window
			IncomingFile = rbTmp.getString("INCOMING_FILE");
			ReceiveFile = rbTmp.getString("RECEIVE_FILE");
			Yes = rbTmp.getString("YES");
			No = rbTmp.getString("NO");
			Receiving = rbTmp.getString("RECEIVING");

			// outgoing file window
			OutgoingFile = rbTmp.getString("OUTGOING_FILE");
			Filename = rbTmp.getString("FILENAME");
			Cancel = rbTmp.getString("CANCEL");
			WaitingForResponse = rbTmp.getString("WAITING_FOR_RESPONSE");
			Sending = rbTmp.getString("SENDING");
			RefusedByRemote = rbTmp.getString("REFUSED_BY_REMOTE");
			TransmissionAbortedByRemote = rbTmp.getString("TRANSMISSION_ABORTED_BY_REMOTE");
			Done = rbTmp.getString("DONE");
			Close = rbTmp.getString("CLOSE");

			// plaza of speech
			Speak = rbTmp.getString("SPEAK");
			HasJoinedPlazaOfSpeech = rbTmp.getString("HAS_JOINED_PLAZA_OF_SPEECH");
			HasLeftPlazaOfSpeech = rbTmp.getString("HAS_LEFT_PLAZA_OF_SPEECH");

			// tray icon pop-up menu
			Show = rbTmp.getString("SHOW");
			Exit = rbTmp.getString("EXIT");
		}
		catch(Exception e)
		{
			// properties file is obligatory
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}
