package org.ezim.core;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.ezim.ui.EzimFileOut;

public class EzimSender {

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
			EzimSender.initByteArrays();

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
				EzimSender.initByteArrays();

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
				EzimSender.initByteArrays();

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
				EzimSender.initByteArrays();

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

				EzimSender.initByteArrays();

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
