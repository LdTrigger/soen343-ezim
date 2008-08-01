/*
    EZ Intranet Messenger

    Copyright (C) 2007 - 2008  Chun-Kwong Wong
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

import java.util.ArrayList;

import org.ezim.core.EzimContact;
import org.ezim.core.EzimContactException;

public class EzimContactList
{
	private static EzimContactList contacts = null;

	private ArrayList<EzimContact> list = null;

	// C O N S T R U C T O R -----------------------------------------------
	private EzimContactList()
	{
		this.list = new ArrayList<EzimContact>();
	}

	// P U B L I C   M E T H O D S -----------------------------------------
	/**
	 * return the only instance of this class
	 * @return instance of this class
	 */
	public static EzimContactList getInstance()
	{
		return EzimContactList.getInstance(false);
	}

	/**
	 * reset and return the only instance of this class
	 * @param blnReset force reseting the instance if true
	 * @return instance of this class
	 */
	public static EzimContactList getInstance(boolean blnReset)
	{
		if (EzimContactList.contacts == null || blnReset)
			EzimContactList.contacts = new EzimContactList();

		return EzimContactList.contacts;
	}

	/**
	 * return all list contents in array
	 */
	public EzimContact[] toArray()
	{
		EzimContact[] ecTmp = (EzimContact[]) this.list.toArray
		(
			new EzimContact[0]
		);

		return ecTmp;
	}

	/**
	 * find and return index of the contact specified by the IP address
	 * @param strIp IP address to look for
	 * @return index of the contact, or -1 if not found
	 */
	private int idxContact(String strIp)
	{
		int iOut = -1;
		int iCnt = 0;
		int iLen = list.size();
		EzimContact ecTmp = null;

		for(iCnt = 0; iCnt < iLen; iCnt ++)
		{
			ecTmp = (EzimContact) this.list.get(iCnt);

			if (strIp.equals(ecTmp.getIp()))
			{
				iOut = iCnt;
				break;
			}
		}

		return iOut;
	}

	/**
	 * find and return the contact specified by the IP address
	 * @param strIp IP address to look for
	 * @return instance of the contact, or null if not found
	 */
	public EzimContact get(String strIp)
	{
		EzimContact ecOut = null;
		int iIdx = idxContact(strIp);

		if (iIdx != -1)
			ecOut = this.list.get(iIdx);

		return ecOut;
	}

	/**
	 * add a new contact to the list if not yet exists
	 * @param strIp IP address of the new contact
	 * @param strname name of the new contact
	 * @param strStatus status of the new contact
	 */
	public void add(String strIp, String strName, String strStatus)
		throws EzimContactException
	{
		if (this.idxContact(strIp) == -1)
		{
			this.list.add
			(
				new EzimContact(strIp, strName, strStatus)
			);
		}

		return;
	}

	/**
	 * remove contact from the list if exists
	 * @param strIp IP address of the contact to be removed
	 */
	public void remove(String strIp)
		throws Exception
	{
		int iIdx = idxContact(strIp);

		if (iIdx != -1)
		{
			this.list.remove(iIdx);
		}

		return;
	}
}
