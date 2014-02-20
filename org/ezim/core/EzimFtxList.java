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

import java.util.Hashtable;

import org.ezim.ui.EzimFileOut;

public class EzimFtxList
{
	private static EzimFtxList ftxs = null;
	private Hashtable<String, EzimFileOut> list = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the (outgoing) file transmission list
	 */
	private EzimFtxList()
	{
		this.list = new Hashtable<String, EzimFileOut>();
	}

	// P U B L I C   M E T H O D -------------------------------------------
	/**
	 * return the only instance of this class
	 * @return instance of this class
	 */
	public static EzimFtxList getInstance()
	{
		return EzimFtxList.getInstance(false);
	}

	/**
	 * reset and return the only instance of this class
	 * @param blnReset force reseting the instance if true
	 * @return instance of this class
	 */
	public static EzimFtxList getInstance(boolean blnReset)
	{
		if (EzimFtxList.ftxs == null || blnReset)
			EzimFtxList.ftxs = new EzimFtxList();

		return EzimFtxList.ftxs;
	}

	/**
	 * get an outgoing file window from the list if exists
	 * @param strId ID indicating the specified outgoing file window
	 * @return outgoing file window
	 */
	public EzimFileOut get(String strId)
	{
		return this.list.get(strId);
	}

	/**
	 * add a new outgoing file window to the list if not yet exists
	 * @param strId ID indicating the specified outgoing file window
	 * @param efiIn outgoing file window
	 */
	public void put(String strId, EzimFileOut efiIn)
	{
		this.list.put(strId, efiIn);
	}

	/**
	 * remove an outgoing file window from the list if exists
	 * @param strId ID indicating the specified outgoing file window
	 */
	public void remove(String strId)
	{
		this.list.remove(strId);
	}
}
