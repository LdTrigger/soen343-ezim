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
import org.ezim.core.EzimFileConfirmer;
import org.ezim.core.EzimFileSender;
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
	public final static String HDRSPR			= ": ";
	public final static String CRLF			= "\r\n";

	// header terminator
	public final static String HDRTRM			= "\r\n";

	// header field "Content-Type"
	public final static String HDR_CTYPE		= "Content-Type";
	public final static String CTYPE_MSG		= "Message";
//	public final static String CTYPE_MSGRT		= "Message-Return-Ticket";
	public final static String CTYPE_FILE		= "File";
	public final static String CTYPE_FILEREQ	= "File-Request";
	public final static String CTYPE_FILERES	= "File-Response";
	public final static String CTYPE_FILECFM	= "File-Confirm";

	// header field "Content-Length"
	public final static String HDR_CLEN		= "Content-Length";

	// header field "Subject"
	// valid content type: "Message"
	public final static String HDR_SBJ			= "Subject";

	// header field "File-Request-ID"
	// valid content type: "File", "File-Request", "File-Response"
	public final static String HDR_FILEREQID	= "File-Request-ID";

	// header field "Filename"
	// valid content type: "File-Request"
	public final static String HDR_FILENAME	= "Filename";

	// header field "Filesize"
	// valid content type: "File-Request"
	public final static String HDR_FILESIZE	= "Filesize";

	// header field "File-Response"
	// valid content type: "File-Response"
	public final static String HDR_FILERES		= "File-Response";
	public final static String OK				= "OK";
	public final static String NG				= "NG";

	// header field "File-Confirm"
	// valid content type: "File-Confirm"
	public final static String HDR_FILECFM		= "File-Confirm";

	// C L A S S   V A R I A B L E -----------------------------------------
	public static byte[] bHDRSPR	= null;
	public static byte[] bCRLF		= null;
	public static byte[] bHDRTRM	= null;

	// M E T H O D S   F O R   O U T G O I N G   T R A N S M I S S I O N ---
	/**
	 * initialize byte array class variables
	 */
	private static void initByteArrays()
		throws Exception
	{
		EzimDtxSemantics.bHDRSPR
			= EzimDtxSemantics.HDRSPR.getBytes(Ezim.dtxMsgEnc);

		EzimDtxSemantics.bCRLF
			= EzimDtxSemantics.CRLF.getBytes(Ezim.dtxMsgEnc);

		EzimDtxSemantics.bHDRTRM
			= EzimDtxSemantics.HDRTRM.getBytes(Ezim.dtxMsgEnc);
	}

	/**
	 * transform and output header in bytes to the output stream given
	 * @param sckIn outgoing socket
	 * @param htIn hashtable containing the header fields
	 * @return
	 */
	private static void sendHeaderBytes
	(
		Socket sckIn
		, Hashtable<String, String> htIn
	)
		throws Exception
	{
		OutputStream osIn = sckIn.getOutputStream();
		Enumeration enumKeys = null;
		String strKey = null;
		String strVal = null;

		if (htIn != null)
		{
			enumKeys = htIn.keys();

			// header fields
			while(enumKeys.hasMoreElements())
			{
				strKey = (String) enumKeys.nextElement();
				strVal = htIn.get(strKey);

				osIn.write(strKey.getBytes(Ezim.dtxMsgEnc));
				osIn.write(EzimDtxSemantics.bHDRSPR);
				osIn.write(strVal.getBytes(Ezim.dtxMsgEnc));
				osIn.write(EzimDtxSemantics.bCRLF);
			}

			// header terminator
			osIn.write(EzimDtxSemantics.bHDRTRM);
		}
	}

	/**
	 * format and output message
	 * @param sckIn outgoing socket
	 * @param strSbj subject line of the outgoing message
	 * @param strMsg message to be formatted and sent
	 */
	public static void sendMsg(Socket sckIn, String strSbj, String strMsg)
		throws Exception
	{
		EzimDtxSemantics.initByteArrays();

		byte[] bMsg = null;

		OutputStream osTmp = sckIn.getOutputStream();

		// convert message body in bytes
		bMsg = strMsg.getBytes(Ezim.dtxMsgEnc);

		// create necessary header fields
		Hashtable<String, String> htTmp
			= new Hashtable<String, String>();

		htTmp.put
		(
			EzimDtxSemantics.HDR_CTYPE
			, EzimDtxSemantics.CTYPE_MSG
		);
		htTmp.put
		(
			EzimDtxSemantics.HDR_CLEN
			, Integer.toString(bMsg.length)
		);
		htTmp.put
		(
			EzimDtxSemantics.HDR_SBJ
			, strSbj
		);

		// output header in bytes
		sendHeaderBytes(sckIn, htTmp);

		// output message body
		osTmp.write(bMsg);

		// make sure everything is sent
		osTmp.flush();
	}

	/**
	 * format and output file request
	 * @param sckIn outgoing socket
	 * @param strId file request ID
	 * @param efoIn the associated outgoing file GUI
	 */
	public static void sendFileReq
	(
		Socket sckIn
		, String strId
		, EzimFileOut efoIn
	)
		throws Exception
	{
		if
		(
			efoIn != null
			&& strId != null && strId.length() > 0
		)
		{
			EzimDtxSemantics.initByteArrays();

			long lSize = 0;

			FileInputStream fisTmp = null;

			try
			{
				OutputStream osTmp = sckIn.getOutputStream();
				lSize = efoIn.getFile().length();
				fisTmp = new FileInputStream(efoIn.getFile());

				efoIn.setSize(lSize);

				// create necessary header fields
				Hashtable<String, String> htTmp
					= new Hashtable<String, String>();

				htTmp.put
				(
					EzimDtxSemantics.HDR_CTYPE
					, EzimDtxSemantics.CTYPE_FILEREQ
				);
				htTmp.put
				(
					EzimDtxSemantics.HDR_CLEN
					, "0"
				);
				htTmp.put
				(
					EzimDtxSemantics.HDR_FILEREQID
					, strId
				);
				htTmp.put
				(
					EzimDtxSemantics.HDR_FILENAME
					, efoIn.getFile().getName()
				);
				htTmp.put
				(
					EzimDtxSemantics.HDR_FILESIZE
					, Long.toString(lSize)
				);

				// output header in bytes
				sendHeaderBytes(sckIn, htTmp);

				// make sure everything is sent
				osTmp.flush();
			}
			finally
			{
				try
				{
					if (fisTmp != null) fisTmp.close();
				}
				catch(Exception e)
				{
					EzimLogger.getInstance().severe(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * format and output file response
	 * @param sckIn outgoing socket
	 * @param strId File-Request-ID to respond to
	 * @param blnRes response (true: OK; false: NG)
	 */
	public static void sendFileRes
	(
		Socket sckIn
		, String strId
		, boolean blnRes
	)
		throws Exception
	{
		if
		(
			strId != null && strId.length() > 0
		)
		{
			EzimDtxSemantics.initByteArrays();

			String strRes = blnRes
				? EzimDtxSemantics.OK
				: EzimDtxSemantics.NG;

			OutputStream osTmp = sckIn.getOutputStream();

			// create necessary header fields
			Hashtable<String, String> htTmp
				= new Hashtable<String, String>();

			htTmp.put
			(
				EzimDtxSemantics.HDR_CTYPE
				, EzimDtxSemantics.CTYPE_FILERES
			);
			htTmp.put
			(
				EzimDtxSemantics.HDR_CLEN
				, "0"
			);
			htTmp.put
			(
				EzimDtxSemantics.HDR_FILEREQID
				, strId
			);
			htTmp.put
			(
				EzimDtxSemantics.HDR_FILERES
				, strRes
			);

			// output header in bytes
			sendHeaderBytes(sckIn, htTmp);

			// make sure everything is sent
			osTmp.flush();
		}
	}

	/**
	 * format and output file confirmation
	 * @param sckIn outgoing socket
	 * @param strId File-Request-ID to respond to
	 * @param blnConfirm confirmation (true: OK; false: NG)
	 */
	public static void sendFileConfirm
	(
		Socket sckIn
		, String strId
		, boolean blnConfirm
	)
		throws Exception
	{
		if
		(
			strId != null && strId.length() > 0
		)
		{
			EzimDtxSemantics.initByteArrays();

			String strConfirm = blnConfirm
				? EzimDtxSemantics.OK
				: EzimDtxSemantics.NG;

			OutputStream osTmp = sckIn.getOutputStream();

			// create necessary header fields
			Hashtable<String, String> htTmp
				= new Hashtable<String, String>();

			htTmp.put
			(
				EzimDtxSemantics.HDR_CTYPE
				, EzimDtxSemantics.CTYPE_FILECFM
			);
			htTmp.put
			(
				EzimDtxSemantics.HDR_CLEN
				, "0"
			);
			htTmp.put
			(
				EzimDtxSemantics.HDR_FILEREQID
				, strId
			);
			htTmp.put
			(
				EzimDtxSemantics.HDR_FILECFM
				, strConfirm
			);

			// output header in bytes
			sendHeaderBytes(sckIn, htTmp);

			// make sure everything is sent
			osTmp.flush();
		}
	}

	/**
	 * format and output file
	 * @param sckIn outgoing socket
	 * @param strId file request ID
	 * @param efoIn the associated outgoing file window
	 */
	public static void sendFile
	(
		Socket sckIn
		, String strId
		, EzimFileOut efoIn
	)
		throws Exception
	{
		if (efoIn != null)
		{
			efoIn.setSocket(sckIn);

			EzimDtxSemantics.initByteArrays();

			byte[] bTmp = new byte[Ezim.dtxBufLen];
			int iTmp = 0;
			long lCnt = 0;
			long lCLen = 0;

			FileInputStream fisTmp = null;

			try
			{
				OutputStream osTmp = sckIn.getOutputStream();
				lCLen = efoIn.getFile().length();
				fisTmp = new FileInputStream(efoIn.getFile());

				// create necessary header fields
				Hashtable<String, String> htTmp
					= new Hashtable<String, String>();

				htTmp.put
				(
					EzimDtxSemantics.HDR_CTYPE
					, EzimDtxSemantics.CTYPE_FILE
				);
				htTmp.put
				(
					EzimDtxSemantics.HDR_CLEN
					, Long.toString(lCLen)
				);
				htTmp.put
				(
					EzimDtxSemantics.HDR_FILEREQID
					, strId
				);

				// output header in bytes
				sendHeaderBytes(sckIn, htTmp);

				efoIn.setSize(lCLen);
				efoIn.setProgressed(lCnt);
				// convert and output file contents in bytes
				while(! ((iTmp = fisTmp.read(bTmp)) < 0))
				{
					osTmp.write(bTmp, 0, iTmp);
					lCnt += iTmp;
					efoIn.setProgressed(lCnt);
				}

				// make sure everything is sent
				osTmp.flush();
			}
			catch(SocketException se)
			{
				// connection closed by remote
			}
			finally
			{
				try
				{
					if (fisTmp != null) fisTmp.close();
				}
				catch(Exception e)
				{
					EzimLogger.getInstance().severe(e.getMessage(), e);
				}

				String strSysMsg = null;

				if (lCnt < lCLen)
					strSysMsg = EzimLang.TransmissionAbortedByRemote;
				else if (lCnt == lCLen)
					strSysMsg = EzimLang.Done;

				efoIn.endProgress(strSysMsg);
			}
		}
	}

	
}
