/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2010  Chun-Kwong Wong
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

import org.ezim.core.EzimContactException;
import org.ezim.core.EzimImage;

public class EzimContact implements Comparable
{
	// system state
	public final static int SYSSTATE_DEFAULT = 0;
	public final static int SYSSTATE_PLAZA = 1;

	// user state
	public final static int STATE_DEFAULT = 0;

	// status
	public final static String STATUS_DEFAULT = "Online";

	private InetAddress addr;
	private int port;
	private String name;
	private int sysState;
	private int state;
	private String status;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the contact class
	 * @param iaIn address of the contact
	 * @param iPort DTX port of the contact
	 * @param strName name of the contact
	 * @param iSysState system state of the contact
	 * @param iState user state of the contact
	 * @param strStatus user status of the contact
	 */
	public EzimContact
	(
		InetAddress iaIn
		, int iPort
		, String strName
		, int iSysState
		, int iState
		, String strStatus
	)
		throws EzimContactException
	{
		this.setAddress(iaIn);
		this.setPort(iPort);
		this.setName(strName);
		this.setSysState(iSysState);
		this.setState(iState);
		this.setStatus(strStatus);
	}

	// P U B L I C ---------------------------------------------------------
	/**
	 * compare this class instance with another
	 * @param ecIn the other class instance to be compared
	 * @return comparison result
	 */
	public int compareTo(Object ecIn)
		throws ClassCastException
	{
		if (! (ecIn instanceof EzimContact))
			throw new ClassCastException("An EzimContact object expected.");

		return this.getAddress().getHostAddress().compareTo
		(
			((EzimContact) ecIn).getAddress().getHostAddress()
		);
	}

	/**
	 * get address of the contact
	 * @return address of the contact
	 */
	public InetAddress getAddress()
	{
		return this.addr;
	}

	/**
	 * get DTX port of the contact
	 * @return DTX port of the contact
	 */
	public int getPort()
	{
		return this.port;
	}

	/**
	 * get name of the contact
	 * @return name of the contact
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * get system state of the contact
	 * @return system state of the contact
	 */
	public int getSysState()
	{
		return this.sysState;
	}

	/**
	 * get user state of the contact
	 * @return user state of the contact
	 */
	public int getState()
	{
		return this.state;
	}

	/**
	 * get user status of the contact
	 * @return user status of the contact
	 */
	public String getStatus()
	{
		return this.status;
	}

	/**
	 * set address of the contact
	 * @param iaIn address to be applied
	 */
	public void setAddress(InetAddress iaIn)
		throws EzimContactException
	{
		if (iaIn == null)
			throw new EzimContactException("Address is null.");

		this.addr = iaIn;

		return;
	}

	/**
	 * set DTX port of the contact
	 * @param iIn DTX port to be applied
	 */
	public void setPort(int iIn)
		throws EzimContactException
	{
		if (iIn < 0 || iIn > 65535)
			throw new EzimContactException("Invalid port number.");

		this.port = iIn;

		return;
	}

	/**
	 * set name of the contact
	 * @param strIn name to be set
	 */
	public void setName(String strIn)
	{
		if (strIn != null && strIn.length() > 0)
			this.name = strIn;
		else
			this.name = this.addr.getHostAddress();

		return;
	}

	/**
	 * set system state of the contact
	 * @param iIn system state to be set
	 */
	public void setSysState(int iIn)
	{
		if (iIn >= 0 && iIn < EzimImage.icoSysStates.length)
		{
			this.sysState = iIn;
		}
		else
		{
			this.sysState = EzimContact.SYSSTATE_DEFAULT;
		}

		return;
	}

	/**
	 * set user state of the contact
	 * @param iIn user state of the contact
	 */
	public void setState(int iIn)
	{
		if (iIn >= 0 && iIn < EzimImage.icoStates.length)
		{
			this.state = iIn;
		}
		else
		{
			this.state = EzimContact.STATE_DEFAULT;
		}

		return;
	}

	/**
	 * set user status of the contact
	 * @param strIn user status to be set
	 */
	public void setStatus(String strIn)
	{
		if (strIn != null && strIn.length() > 0)
			this.status = strIn;
		else
			this.status = EzimContact.STATUS_DEFAULT;

		return;
	}
}
