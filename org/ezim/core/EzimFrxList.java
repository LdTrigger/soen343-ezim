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

import org.ezim.ui.EzimFileIn;

public class EzimFrxList
{
	private static EzimFrxList frxs = null;
	private Hashtable<String, EzimFileIn> list = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the (incoming) file reception list
	 */
	private EzimFrxList()
	{
		this.list = new Hashtable<String, EzimFileIn>();
	}

	// P U B L I C   M E T H O D -------------------------------------------
	/**
	 * return the only instance of this class
	 * @return instance of this class
	 */
	public static EzimFrxList getInstance()
	{
		return EzimFrxList.getInstance(false);
	}

	/**
	 * reset and return the only instance of this class
	 * @param blnReset force reseting the instance if true
	 * @return instance of this class
	 */
	public static EzimFrxList getInstance(boolean blnReset)
	{
		if (EzimFrxList.frxs == null || blnReset)
			EzimFrxList.frxs = new EzimFrxList();

		return EzimFrxList.frxs;
	}

	/**
	 * get an incoming file window from the list if exists
	 * @param strId ID indicating the specified incoming file window
	 * @return incoming file window
	 */
	public EzimFileIn get(String strId)
	{
		return this.list.get(strId);
	}

	/**
	 * add a new incoming file window to the list if not yet exists
	 * @param strId ID indicating the specified incoming file window
	 * @param efiIn incoming file window
	 */
	public void put(String strId, EzimFileIn efiIn)
	{
		this.list.put(strId, efiIn);
	}

	/**
	 * remove an incoming file window from the list if exists
	 * @param strId ID indicating the specified incoming file window
	 */
	public void remove(String strId)
	{
		this.list.remove(strId);
	}
}
