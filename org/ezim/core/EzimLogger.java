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

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.ezim.core.Ezim;

public class EzimLogger
{
	// logger
	private Logger logger = null;

	// singleton object
	private static EzimLogger ezlogger = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the logger
	 */
	private EzimLogger()
	{
		FileHandler fhTmp = null;

		this.logger = Logger.getLogger(Ezim.appAbbrev);

		try
		{
			fhTmp = new FileHandler
			(
				"%h/." + Ezim.appAbbrev + "/" + Ezim.appAbbrev + ".log"
				, true
			);
			fhTmp.setFormatter(new SimpleFormatter());
			fhTmp.setEncoding("UTF-8");

			this.logger.addHandler(fhTmp);
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
	}

	// P U B L I C   M E T H O D S -----------------------------------------
	/**
	 * return an EzimLogger object
	 */
	public static EzimLogger getInstance()
	{
		if (EzimLogger.ezlogger == null)
			EzimLogger.ezlogger = new EzimLogger();

		return EzimLogger.ezlogger;
	}

	/**
	 * log a SEVERE message
	 * @param strMsg the string message
	 */
	public void severe(String strMsg)
	{
		this.logger.log(Level.SEVERE, strMsg);
	}

	/**
	 * log a SEVERE message
	 * @param strMsg the string message
	 * @param thwInfo Throwable associated with the message
	 */
	public void severe(String strMsg, Throwable thwInfo)
	{
		this.logger.log(Level.SEVERE, strMsg, thwInfo);
	}

	/**
	 * log a WARNING message
	 * @param strMsg the string message
	 */
	public void warning(String strMsg)
	{
		this.logger.log(Level.WARNING, strMsg);
	}

	/**
	 * log a WARNING message
	 * @param strMsg the string message
	 * @param thwInfo Throwable associated with the message
	 */
	public void warning(String strMsg, Throwable thwInfo)
	{
		this.logger.log(Level.WARNING, strMsg, thwInfo);
	}
}
