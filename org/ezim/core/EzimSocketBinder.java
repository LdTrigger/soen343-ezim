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
import java.net.InetSocketAddress;
import java.net.Socket;

import org.ezim.core.Ezim;
import org.ezim.core.EzimDtxSemantics;
import org.ezim.core.EzimLogger;
import org.ezim.ui.EzimMain;
import org.ezim.ui.EzimFileIn;
import org.ezim.ui.EzimFileOut;
import org.ezim.core.EzimFrxList;
import org.ezim.core.EzimFtxList;

public class EzimSocketBinder implements Runnable
{
	protected InetAddress addr = null;
	protected int port = -1;
	protected String id = null;
	protected EzimFileOut efo = null;
	
	protected boolean blnRes = false;
	protected boolean blnConfirm = false;
	
	private boolean confirm = false;
	private boolean respond = false;
	private boolean send = false;
	private boolean request = false;
	
	// Will determine the fileType and run the appropriate code (to be implemented in run)
	
	public void EzimFileType(String str)
	{
		if(str.equals("respond"))
		{
			this.respond = true;
		}
		else if(str.equals("request"))
		{
			this.request = true;
		}
		else if(str.equals("confirm"))
		{
			this.confirm = true;
		}
		else if(str.equals("send"))
		{
			this.send = true;
		}
	}
	
	// This function will be called by one of the classes(Requester , Responder etc...)
	
	public void doRun(String fileType)
	{
		EzimFileType(fileType);
		run();
	}
	
	public void run()
	{
		// Function to be implemented in coming patchsets
	}
}