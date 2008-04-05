/*
    EZ Intranet Messenger
    Copyright (C) 2007 - 2008  Chun-Kwong Wong <chunkwong.wong@gmail.com>

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

import org.ezim.core.EzimContactException;

public class EzimContact implements Comparable
{
	// system state
	public final static int SYSSTATE_DEFAULT = 0;
	public final static int SYSSTATE_PLAZA = 1;

	// user state
	public final static int STATE_DEFAULT = 0;

	// status
	public final static String STATUS_DEFAULT = "Online";

	private String ip;
	private String name;
	private int sysState;
	private int state;
	private String status;

	public EzimContact(String strIp, String strName, String strStatus)
		throws EzimContactException
	{
		this.setIp(strIp);
		this.setName(strName);
		this.setSysState(EzimContact.SYSSTATE_DEFAULT);
		this.setState(EzimContact.STATE_DEFAULT);
		this.setStatus(strStatus);
	}

	public int compareTo(Object ecIn)
		throws ClassCastException
	{
		if (! (ecIn instanceof EzimContact))
			throw new ClassCastException("An EzimContact object expected.");

		return this.getIp().compareTo(((EzimContact) ecIn).getIp());
	}

	public String getIp()
	{
		return this.ip;
	}

	public String getName()
	{
		return this.name;
	}

	public int getSysState()
	{
		return this.sysState;
	}

	public int getState()
	{
		return this.state;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setIp(String strIn)
		throws EzimContactException
	{
		if (strIn == null) throw new EzimContactException("IP is null.");
		this.ip = strIn;

		return;
	}

	public void setName(String strIn)
	{
		if (strIn != null && strIn.length() > 0)
			this.name = strIn;
		else
			this.name = this.ip;

		return;
	}

	public void setSysState(int iIn)
	{
		if (iIn >= 0) this.sysState = iIn;
		else this.sysState = EzimContact.SYSSTATE_DEFAULT;

		return;
	}

	public void setState(int iIn)
	{
		if (iIn >= 0) this.state = iIn;
		else this.state = EzimContact.STATE_DEFAULT;

		return;
	}

	public void setStatus(String strIn)
	{
		if (strIn != null && strIn.length() > 0)
			this.status = strIn;
		else
			this.status = "Online";

		return;
	}
}
