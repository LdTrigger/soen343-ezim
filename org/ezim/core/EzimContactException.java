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

public class EzimContactException extends Exception
{
	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the contact exception class
	 */
	public EzimContactException()
	{
		super();
	}

	/**
	 * construct an instance of the contact exception class
	 * @param strMsg the detailed message
	 */
	public EzimContactException(String strMsg)
	{
		super(strMsg);
	}

	/**
	 * construct an instance of the contact exception class
	 * @param strMsg the detailed message
	 * @param cause the cause
	 */
	public EzimContactException(String strMsg, Throwable cause)
	{
		super(strMsg, cause);
	}

	/**
	 * construct an instance of the contact exception class
	 * @param cause the cause
	 */
	public EzimContactException(Throwable cause)
	{
		super(cause);
	}
}
