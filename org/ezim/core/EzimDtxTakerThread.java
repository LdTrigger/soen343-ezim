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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.Thread;
import java.net.Socket;

import org.ezim.core.Ezim;
import org.ezim.core.EzimContact;

public class EzimDtxTakerThread extends Thread
{
	private EzimContact ec;
	private Socket sck;

	public EzimDtxTakerThread(EzimContact ecIn, Socket sckIn)
	{
		this.ec = ecIn;
		this.sck = sckIn;
	}

	public void run()
	{
		File fHdr = null;
		FileOutputStream fosData = null;
		InputStream isSck = null;
		int iFlg = 4;
		int iTmp = 0;
		byte bTmp = 0;

		try
		{
			fHdr = File.createTempFile(Ezim.appAbbrev, null);
			fosData = new FileOutputStream(fHdr);
			isSck = this.sck.getInputStream();

			while
			(
				iFlg > 0
				&& ! ((iTmp = isSck.read()) < 0)
			)
			{
				bTmp = (byte) iTmp;
				fosData.write(bTmp);

				if
				(
					(iTmp == 0x0d && iFlg % 2 == 0)
					|| (iTmp == 0x0a && iFlg % 2 == 1)
				)
				{
					iFlg --;
				}
				else
				{
					iFlg = 4;
				}
			}

			fosData.close();

			EzimDtxSemantics.parser(fHdr, this.sck, this.ec);
		}
		catch(Exception e)
		{
			// ignore (safe?)
		}
		finally
		{
			try
			{
				if (this.sck != null) this.sck.close();
			}
			catch(Exception e)
			{
				// ignore
			}

			try
			{
				if (fosData != null) fosData.close();
			}
			catch(Exception e)
			{
				// ignore
			}

			fHdr.delete();
		}

		return;
	}
}
