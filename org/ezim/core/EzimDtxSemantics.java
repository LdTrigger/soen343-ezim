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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.ezim.core.Ezim;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimSocketBinder;
import org.ezim.core.EzimFtxList;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimLogger;
import org.ezim.core.EzimThreadPool;
import org.ezim.ui.EzimFileIn;
import org.ezim.ui.EzimFileOut;
import org.ezim.ui.EzimMsgIn;

public class EzimDtxSemantics
{
	// C L A S S   C O N S T A N T -----------------------------------------
	// header field name and value separator
	protected final static String HDRSPR			= ": ";
	protected final static String CRLF			= "\r\n";

	// header terminator
	protected final static String HDRTRM			= "\r\n";

	// header field "Content-Type"
	protected final static String HDR_CTYPE		= "Content-Type";
	protected final static String CTYPE_MSG		= "Message";
//	private final static String CTYPE_MSGRT		= "Message-Return-Ticket";
	protected final static String CTYPE_FILE		= "File";
	protected final static String CTYPE_FILEREQ	= "File-Request";
	protected final static String CTYPE_FILERES	= "File-Response";
	protected final static String CTYPE_FILECFM	= "File-Confirm";

	// header field "Content-Length"
	protected final static String HDR_CLEN		= "Content-Length";

	// header field "Subject"
	// valid content type: "Message"
	protected final static String HDR_SBJ			= "Subject";

	// header field "File-Request-ID"
	// valid content type: "File", "File-Request", "File-Response"
	protected final static String HDR_FILEREQID	= "File-Request-ID";

	// header field "Filename"
	// valid content type: "File-Request"
	protected final static String HDR_FILENAME	= "Filename";

	// header field "Filesize"
	// valid content type: "File-Request"
	protected final static String HDR_FILESIZE	= "Filesize";

	// header field "File-Response"
	// valid content type: "File-Response"
	protected final static String HDR_FILERES		= "File-Response";
	protected final static String OK				= "OK";
	protected final static String NG				= "NG";

	// header field "File-Confirm"
	// valid content type: "File-Confirm"
	protected final static String HDR_FILECFM		= "File-Confirm";

	// C L A S S   V A R I A B L E -----------------------------------------
	protected static byte[] bHDRSPR	= null;
	protected static byte[] bCRLF		= null;
	protected static byte[] bHDRTRM	= null;

}
