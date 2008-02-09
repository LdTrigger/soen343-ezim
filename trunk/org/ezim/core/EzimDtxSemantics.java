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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.lang.Thread;
import java.util.Enumeration;
import java.util.Hashtable;

import org.ezim.core.Ezim;
import org.ezim.core.EzimContact;
import org.ezim.core.EzimFileSender;
import org.ezim.core.EzimFtxList;
import org.ezim.core.EzimLang;
import org.ezim.ui.EzimFileIn;
import org.ezim.ui.EzimFileOut;
import org.ezim.ui.EzimMsgIn;

public class EzimDtxSemantics
{
	// C L A S S   C O N S T A N T -----------------------------------------
	// header field name and value separator
	private final static String HDRSPR			= ": ";
	private final static String CRLF			= "\r\n";

	// header terminator
	private final static String HDRTRM			= "\r\n";

	// header field "Content-Type"
	private final static String HDR_CTYPE		= "Content-Type";
	private final static String CTYPE_MSG		= "Message";
//	private final static String CTYPE_MSGRT		= "Message-Return-Ticket";
	private final static String CTYPE_FILE		= "File";
	private final static String CTYPE_FILEREQ	= "File-Request";
	private final static String CTYPE_FILERES	= "File-Response";

	// header field "Content-Length"
	private final static String HDR_CLEN		= "Content-Length";

	// header field "Subject"
	// valid content type: "Message"
//	private final static String HDR_SBJ			= "Subject";

	// header field "File-Request-ID"
	// valid content type: "File", "File-Request", "File-Response"
	private final static String HDR_FILEREQID	= "File-Request-ID";

	// header field "Filename"
	// valid content type: "File-Request"
	private final static String HDR_FILENAME	= "Filename";

	// header field "File-Response"
	// valid content type: "File Request"
	private final static String HDR_FILERES		= "File-Response";
	private final static String RES_OK			= "OK";
	private final static String RES_NG			= "NG";

	// C L A S S   V A R I A B L E -----------------------------------------
	private static byte[] bHDRSPR	= null;
	private static byte[] bCRLF		= null;
	private static byte[] bHDRTRM	= null;

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

		return;
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

		return;
	}

	/**
	 * format and output message
	 * @param sckIn outgoing socket
	 * @param strMsg message to be formatted and sent
	 * @return
	 */
	public static void sendMsg(Socket sckIn, String strMsg)
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

		// output header in bytes
		sendHeaderBytes(sckIn, htTmp);

		// output message body
		osTmp.write(bMsg);

		// make sure everything is sent
		osTmp.flush();

		return;
	}

	/**
	 * format and output file request
	 * @param sckIn outgoing socket
	 * @param strId file request ID
	 * @param fIn file to be requested
	 * @return
	 */
	public static void sendFileReq
	(
		Socket sckIn
		, String strId
		, File fIn
	)
		throws Exception
	{
		if
		(
			fIn != null
			&& strId != null && strId.length() > 0
		)
		{
			EzimDtxSemantics.initByteArrays();

			OutputStream osTmp = sckIn.getOutputStream();

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
				, fIn.getName()
			);

			// output header in bytes
			sendHeaderBytes(sckIn, htTmp);

			// make sure everything is sent
			osTmp.flush();
		}

		return;
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
				? EzimDtxSemantics.RES_OK
				: EzimDtxSemantics.RES_NG;

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

		return;
	}

	/**
	 * format and output file
	 * @param sckIn outgoing socket
	 * @param strId file request ID
	 * @param efoIn the associated outgoing file window
	 * @return
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
			EzimDtxSemantics.initByteArrays();

			byte[] bTmp = new byte[Ezim.dtxBufLen];
			int iTmp = 0;
			int iCnt = 0;

			FileInputStream fisTmp = null;

			try
			{
				OutputStream osTmp = sckIn.getOutputStream();
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
					, Integer.toString(fisTmp.available())
				);
				htTmp.put
				(
					EzimDtxSemantics.HDR_FILEREQID
					, strId
				);

				// output header in bytes
				sendHeaderBytes(sckIn, htTmp);

				efoIn.setSize(fisTmp.available());
				// convert and output file contents in bytes
				while(! ((iTmp = fisTmp.read(bTmp)) < 0))
				{
					osTmp.write(bTmp, 0, iTmp);
					iCnt += iTmp;
					efoIn.setProgressed(iCnt);
				}

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
					// ignore
				}
			}
		}

		return;
	}

	// M E T H O D S   F O R   I N C O M I N G   T R A N S M I S S I O N ---
	/**
	 * retrieve header from the incoming socket
	 * @param fIn file which contains the header
	 * @return hashtabled header
	 */
	private static Hashtable<String, String> getHeader
	(
		File fIn
	)
		throws Exception
	{
		Hashtable<String, String> htOut = null;
		FileInputStream fisHdr = new FileInputStream(fIn);
		byte[] bBuf = null;
		String strHdr = null;
		String[] arrLines = null;
		String[] arrHdrFldParts = null;

		htOut = new Hashtable<String, String>();

		try
		{
			fisHdr = new FileInputStream(fIn);
			bBuf = new byte[fisHdr.available()];
			fisHdr.read(bBuf);
		}
		finally
		{
			fisHdr.close();
		}

		strHdr = new String(bBuf, Ezim.dtxMsgEnc);
		arrLines = strHdr.split(EzimDtxSemantics.CRLF);

		for(int iX = 0; iX < arrLines.length; iX ++)
		{
			arrHdrFldParts = arrLines[iX].split(EzimDtxSemantics.HDRSPR);

			if (arrHdrFldParts.length > 1)
				htOut.put(arrHdrFldParts[0], arrHdrFldParts[1]);
		}

		return htOut;
	}

	/**
	 * retrieve message from incoming socket
	 * @param isIn input stream which streams raw incoming data
	 * @param iCLen length of the message in bytes
	 * @param ecIn peer user who made the direct transmission
	 * @return
	 */
	private static void getMsg
	(
		InputStream isIn
		, int iCLen
		, EzimContact ecIn
	)
		throws Exception
	{
		byte[] bBuf = new byte[iCLen];
		int iTmp = 0;
		int iCnt = 0;
		String strTmp = null;

		while (! (iTmp < 0) && iCnt < iCLen)
		{
			iTmp = isIn.read();

			if (! (iTmp < 0))
			{
				bBuf[iCnt] = (byte) iTmp;
				iCnt ++;
			}
		}

		strTmp = new String(bBuf, 0, iCnt, Ezim.dtxMsgEnc);

		new EzimMsgIn(ecIn, strTmp);

		return;
	}

	/**
	 * retrieve file from incoming socket
	 * @param isIn input stream which streams raw incoming data
	 * @param iCLen length of the file in bytes
	 * @param efiIn the associated incoming file window
	 * @return
	 */
	private static void getFile
	(
		InputStream isIn
		, int iCLen
		, EzimFileIn efiIn
	)
		throws Exception
	{
		FileOutputStream fosTmp = null;
		byte[] bBuf = new byte[Ezim.dtxBufLen];
		int iTmp = 0;
		int iCnt = 0;

		try
		{
			fosTmp = new FileOutputStream(efiIn.getFile());
			efiIn.setSize(iCLen);

			while(! ((iTmp = isIn.read(bBuf)) < 0) && iCnt < iCLen)
			{
				fosTmp.write(bBuf, 0, iTmp);
				iCnt += iTmp;
				efiIn.setProgressed(iCnt);
			}

			fosTmp.flush();

			efiIn.finishProgress();
		}
		finally
		{
			if (fosTmp != null) fosTmp.close();
		}

		return;
	}

	/**
	 * parse all incoming direct transmissions and react accordingly
	 * @param fIn file containing the header
	 * @param sckIn incoming socket
	 * @param ecIn peer user who made the direct transmission
	 */
	public static void parser(File fIn, Socket sckIn, EzimContact ecIn)
	{
		Hashtable<String, String> htHdr = null;
		InputStream isData = null;
		String strCType = null;
		String strCLen = null;
		int iCLen = 0;

		try
		{
			isData = sckIn.getInputStream();
			htHdr = EzimDtxSemantics.getHeader(fIn);
			strCType = htHdr.get(EzimDtxSemantics.HDR_CTYPE);
			strCLen = htHdr.get(EzimDtxSemantics.HDR_CLEN);

			if (strCType == null)
			{
				throw new Exception
				(
					"Header field \"Content-Type\" is missing."
				);
			}
			else if (strCLen == null)
			{
				throw new Exception
				(
					"Header field \"Content-Length\" is missing."
				);
			}
			else
			{
				iCLen = Integer.parseInt(strCLen);

				// receive incoming message
				if (strCType.equals(EzimDtxSemantics.CTYPE_MSG))
				{
					EzimDtxSemantics.getMsg(isData, iCLen, ecIn);
				}
				// receive incoming file
				else if (strCType.equals(EzimDtxSemantics.CTYPE_FILE))
				{
					String strFileReqId = htHdr.get
					(
						EzimDtxSemantics.HDR_FILEREQID
					);

					EzimFileIn efiTmp = EzimFrxList.getInstance()
						.get(strFileReqId);

					efiTmp.setSysMsg(EzimLang.Receiving);
					EzimDtxSemantics.getFile
					(
						isData
						, iCLen
						, efiTmp
					);
				}
				// receive incoming file request
				else if (strCType.equals(EzimDtxSemantics.CTYPE_FILEREQ))
				{
					String strFilename = htHdr.get
					(
						EzimDtxSemantics.HDR_FILENAME
					);
					String strFileReqId = htHdr.get
					(
						EzimDtxSemantics.HDR_FILEREQID
					);

					EzimFileIn efiTmp = new EzimFileIn
					(
						ecIn
						, strFileReqId
						, strFilename
					);
					efiTmp.getUserInput();
				}
				// receive incoming file response
				else if (strCType.equals(EzimDtxSemantics.CTYPE_FILERES))
				{
					String strFileReqId = htHdr.get
					(
						EzimDtxSemantics.HDR_FILEREQID
					);
					String strFileRes = htHdr.get
					(
						EzimDtxSemantics.HDR_FILERES
					);

					EzimFileOut efoTmp = EzimFtxList.getInstance()
						.get(strFileReqId);

					if (strFileRes.equals(EzimDtxSemantics.RES_OK))
					{
						efoTmp.setSysMsg(EzimLang.Sending);
						new EzimFileSender(efoTmp, ecIn.getIp()).run();
					}
					else
					{
						efoTmp.setSysMsg(EzimLang.RefusedByRemote);
						efoTmp.setCloseButtonEnabled(true);
					}
				}
			}
		}
		catch(Exception e)
		{
			// ignore
		}
		finally
		{
			try
			{
				if (isData != null) isData.close();
			}
			catch(Exception e)
			{
				// ignore
			}
		}

		return;
	}
}