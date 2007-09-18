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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class EzimConf
{
	// configuration item name
	public final static String username = "USERNAME";
	public final static String mainFrameX = "MAIN_FRAME_X";
	public final static String mainFrameY = "MAIN_FRAME_Y";
	public final static String mainFrameH = "MAIN_FRAME_H";
	public final static String mainFrameW = "MAIN_FRAME_W";

	// configuration item
	public Properties settings;

	// C O N S T R U C T O R -----------------------------------------------
	public EzimConf(String strFName)
	{
		// set default values
		this.init();

		// set saved configuration settings
		if (strFName != null && strFName.length() > 0)
			this.read(strFName);
	}

	/**
	 * initialize all configuration items
	 */
	private void init()
	{
		this.settings = new Properties();

		// default values
		this.settings.setProperty(EzimConf.mainFrameX, "0");
		this.settings.setProperty(EzimConf.mainFrameY, "0");
		this.settings.setProperty(EzimConf.mainFrameH, "0");
		this.settings.setProperty(EzimConf.mainFrameW, "0");

		return;
	}

	// P U B L I C   M E T H O D S -----------------------------------------
	/**
	 * read configuration items from file indicated by the given filename
	 * @param strFName name of the configuration file
	 */
	public void read(String strFName)
	{
		FileInputStream fisTmp = null;

		try
		{
			fisTmp = new FileInputStream(strFName);
			this.settings.load(fisTmp);
		}
		catch(Exception e)
		{
			// ignore
		}
		finally
		{
			try
			{
				if (fisTmp != null) fisTmp.close();
			}
			catch(Exception e)
			{
				// ignore
			}
		}

		return;
	}

	/**
	 * write configuration items to the file indicated by the given filename
	 * @param strFName name of the configuration file
	 */
	public void write(String strFName)
	{
		FileOutputStream fosTmp = null;

		try
		{
			fosTmp = new FileOutputStream(strFName);
			this.settings.store(fosTmp, "--- Ezim Configuration File ---");
		}
		catch(Exception e)
		{
			// OMG!
		}
		finally
		{
			try
			{
				if (fosTmp != null)
				{
					fosTmp.flush();
					fosTmp.close();
				}
			}
			catch(Exception e)
			{
				// ignore
			}
		}

		return;
	}
}
