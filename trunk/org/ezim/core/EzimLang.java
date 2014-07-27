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

import org.ezim.core.Ezim;
import org.ezim.core.EzimLogger;

import java.util.ResourceBundle;

public class EzimLang
{
	// username input dialog
	public static String PleaseInputYourName;

	// about dialog
	public static String Notice;
	public static String About;

	// main window
	public static String ClickToChangeState;
	public static String ClickToChangeStatus;
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
	public static String ReceivedAt;
	public static String ClickHereToOpenMessage;
	public static String Reply;
	public static String OriginalMessageFrom;

	// outgoing message window
	public static String OutgoingMessage;
	public static String To;
	public static String Send;
	public static String DeleteLastRecipient;
	public static String RecipientNotExists;

	// send message error dialog
	public static String SendMessageError;
	public static String RecipientsError;

	// incoming file window
	public static String IncomingFile;
	public static String ReceiveFile;
	public static String Yes;
	public static String No;
	public static String Receiving;
	public static String OverwriteExistingFile;

	// outgoing file window
	public static String OutgoingFile;
	public static String Filename;
	public static String Size;
	public static String Cancel;
	public static String WaitingForResponse;
	public static String Sending;
	public static String RefusedByRemote;
	public static String FileNotFoundTransmissionAborted;
	public static String TransmissionAbortedByRemote;
	public static String Done;
	public static String Close;

	// plaza of speech
	public static String Speak;
	public static String HasJoinedPlazaOfSpeech;
	public static String HasLeftPlazaOfSpeech;

	// control panel
	public static String Prefs;
	public static String Network;
	public static String McGroup;
	public static String RestoreToDefault;
	public static String McPort;
	public static String DtxPort;
	public static String LocalAddress;
	public static String Design;
	public static String ColorSelf;
	public static String ColorSelfR;
	public static String ColorSelfG;
	public static String ColorSelfB;
	public static String UI;
	public static String AlwaysOnTop;
	public static String AutoOpenMsgIn;
	public static String Locale;
	public static String StateiconSize;
	public static String Sound;
	public static String EnableSound;
	public static String StateChg;
	public static String StatusChg;
	public static String MsgIn;
	public static String FileIn;
	public static String Save;
	public static String Restore;
	public static String RestartNeeded;
	public static String McGroupError;

	// tray icon pop-up menu
	public static String ShowHide;
	public static String Exit;

	// text editing pop-up menu
	public static String Cut;
	public static String Copy;
	public static String Paste;
	public static String SelectAll;

	/**
	 * initialize public static class members
	 */
	public static void init()
	{
		try
		{
			ResourceBundle rbTmp = ResourceBundle.getBundle
			(
				"org.ezim.properties.EzimUI"
			);

			// username input dialog
			PleaseInputYourName = rbTmp.getString("PleaseInputYourName");

			// about dialog
			Notice = Ezim.appName + "\n"
			 + "version " + Ezim.appVer + "\n\n"
			 + rbTmp.getString("Notice");
			About = rbTmp.getString("About");

			// main window
			ClickToChangeState = rbTmp.getString("ClickToChangeState");
			ClickToChangeStatus = rbTmp.getString("ClickToChangeStatus");
			SendMessageToContact = rbTmp.getString("SendMessageToContact");
			SendFileToContact = rbTmp.getString("SendFileToContact");
			RefreshContactList = rbTmp.getString("RefreshContactList");
			PlazaOfSpeech = rbTmp.getString("PlazaOfSpeech");

			// error dialog
			Error = rbTmp.getString("Error");

			// incoming message window
			IncomingMessage = rbTmp.getString("IncomingMessage");
			From = rbTmp.getString("From");
			Subject = rbTmp.getString("Subject");
			ReceivedAt = rbTmp.getString("ReceivedAt");
			ClickHereToOpenMessage = rbTmp.getString("ClickHereToOpenMessage");
			Reply = rbTmp.getString("Reply");
			OriginalMessageFrom = rbTmp.getString("OriginalMessageFrom");

			// outgoing message window
			OutgoingMessage = rbTmp.getString("OutgoingMessage");
			To = rbTmp.getString("To");
			Send = rbTmp.getString("Send");
			DeleteLastRecipient = rbTmp.getString("DeleteLastRecipient");
			RecipientNotExists = rbTmp.getString("RecipientNotExists");

			// send message error dialog
			SendMessageError = rbTmp.getString("SendMessageError");
			RecipientsError = rbTmp.getString("RecipientsError");

			// incoming file window
			IncomingFile = rbTmp.getString("IncomingFile");
			ReceiveFile = rbTmp.getString("ReceiveFile");
			Yes = rbTmp.getString("Yes");
			No = rbTmp.getString("No");
			Receiving = rbTmp.getString("Receiving");
			OverwriteExistingFile = rbTmp.getString("OverwriteExistingFile");

			// outgoing file window
			OutgoingFile = rbTmp.getString("OutgoingFile");
			Filename = rbTmp.getString("Filename");
			Size = rbTmp.getString("Size");
			Cancel = rbTmp.getString("Cancel");
			WaitingForResponse = rbTmp.getString("WaitingForResponse");
			Sending = rbTmp.getString("Sending");
			RefusedByRemote = rbTmp.getString("RefusedByRemote");
			FileNotFoundTransmissionAborted = rbTmp.getString("FileNotFoundTransmissionAborted");
			TransmissionAbortedByRemote = rbTmp.getString("TransmissionAbortedByRemote");
			Done = rbTmp.getString("Done");
			Close = rbTmp.getString("Close");

			// plaza of speech
			Speak = rbTmp.getString("Speak");
			HasJoinedPlazaOfSpeech = rbTmp.getString("HasJoinedPlazaOfSpeech");
			HasLeftPlazaOfSpeech = rbTmp.getString("HasLeftPlazaOfSpeech");

			// control panel
			Prefs = rbTmp.getString("Prefs");
			Network = rbTmp.getString("Network");
			McGroup = rbTmp.getString("McGroup");
			RestoreToDefault = rbTmp.getString("RestoreToDefault");
			McPort = rbTmp.getString("McPort");
			DtxPort = rbTmp.getString("DtxPort");
			LocalAddress = rbTmp.getString("LocalAddress");
			Design = rbTmp.getString("Design");
			ColorSelf = rbTmp.getString("ColorSelf");
			ColorSelfR = rbTmp.getString("ColorSelfR");
			ColorSelfG = rbTmp.getString("ColorSelfG");
			ColorSelfB = rbTmp.getString("ColorSelfB");
			UI = rbTmp.getString("UI");
			AlwaysOnTop = rbTmp.getString("AlwaysOnTop");
			AutoOpenMsgIn = rbTmp.getString("AutoOpenMsgIn");
			Locale = rbTmp.getString("Locale");
			StateiconSize = rbTmp.getString("StateiconSize");
			Sound = rbTmp.getString("Sound");
			EnableSound = rbTmp.getString("EnableSound");
			StateChg = rbTmp.getString("StateChg");
			StatusChg = rbTmp.getString("StatusChg");
			MsgIn = rbTmp.getString("MsgIn");
			FileIn = rbTmp.getString("FileIn");
			Save = rbTmp.getString("Save");
			Restore = rbTmp.getString("Restore");
			RestartNeeded = rbTmp.getString("RestartNeeded");
			McGroupError = rbTmp.getString("McGroupError");

			// tray icon pop-up menu
			ShowHide = rbTmp.getString("ShowHide");
			Exit = rbTmp.getString("Exit");

			// text editing pop-up menu
			Cut = rbTmp.getString("Cut");
			Copy = rbTmp.getString("Copy");
			Paste = rbTmp.getString("Paste");
			SelectAll = rbTmp.getString("SelectAll");
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);

			// properties file is obligatory
			System.out.println(e.getMessage());
			Ezim.exit(1);
		}
	}
}
