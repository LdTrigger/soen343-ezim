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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.ezim.core.Ezim;

public class EzimConf
{
	// configuration item name
	public final static String ezimmainLocalname = "ezimmain.localname";
	public final static String ezimmainLocationX = "ezimmain.location.x";
	public final static String ezimmainLocationY = "ezimmain.location.y";
	public final static String ezimmainSizeH = "ezimmain.size.h";
	public final static String ezimmainSizeW = "ezimmain.size.w";
	public final static String ezimplazaLocationX = "ezimplaza.location.x";
	public final static String ezimplazaLocationY = "ezimplaza.location.y";
	public final static String ezimplazaSizeH = "ezimplaza.size.h";
	public final static String ezimplazaSizeW = "ezimplaza.size.w";
	public final static String ezimMsgOutLocationX = "ezimMsgOut.location.x";
	public final static String ezimMsgOutLocationY = "ezimMsgOut.location.y";
	public final static String ezimMsgOutSizeH = "ezimMsgOut.size.h";
	public final static String ezimMsgOutSizeW = "ezimMsgOut.size.w";
	public final static String ezimMsgInLocationX = "ezimMsgIn.location.x";
	public final static String ezimMsgInLocationY = "ezimMsgIn.location.y";
	public final static String ezimMsgInSizeH = "ezimMsgIn.size.h";
	public final static String ezimMsgInSizeW = "ezimMsgIn.size.w";
	public final static String ezimOpenMsgsImmediatly = "ezimOpenMsgsImmediatly";

	// configuration item
	public Properties settings;

	// C O N S T R U C T O R -----------------------------------------------
	private EzimConf()
	{
		// set default values
		this.init();

		// set saved configuration settings
		this.read();
	}
	private static EzimConf ezCf = null;
	
	synchronized public static EzimConf getInstance(){
		if(ezCf == null){
			ezCf = new EzimConf();
		}
		return ezCf;
	}
	
	/**
	 * initialize all configuration items
	 */
	private void init()
	{
		this.settings = new Properties();

		// default values
		this.settings.setProperty(EzimConf.ezimmainLocationX, "0");
		this.settings.setProperty(EzimConf.ezimmainLocationY, "0");
		this.settings.setProperty(EzimConf.ezimmainSizeH, "0");
		this.settings.setProperty(EzimConf.ezimmainSizeW, "0");
		this.settings.setProperty(EzimConf.ezimplazaLocationX, "0");
		this.settings.setProperty(EzimConf.ezimplazaLocationY, "0");
		this.settings.setProperty(EzimConf.ezimplazaSizeH, "0");
		this.settings.setProperty(EzimConf.ezimplazaSizeW, "0");

		this.settings.setProperty(EzimConf.ezimMsgOutLocationX, "0");
		this.settings.setProperty(EzimConf.ezimMsgOutLocationY, "0");
		this.settings.setProperty(EzimConf.ezimMsgOutSizeH, "0");
		this.settings.setProperty(EzimConf.ezimMsgOutSizeW, "0");
		this.settings.setProperty(EzimConf.ezimMsgInLocationX, "0");
		this.settings.setProperty(EzimConf.ezimMsgInLocationY, "0");
		this.settings.setProperty(EzimConf.ezimMsgInSizeH, "0");
		this.settings.setProperty(EzimConf.ezimMsgInSizeW, "0");
		this.settings.setProperty(EzimConf.ezimOpenMsgsImmediatly, "false");
		
		return;
	}

	// P U B L I C   M E T H O D S -----------------------------------------
	/**
	 * determine the appropriate configuration directory name
	 */
	public static String getConfDir()
	{
		String strSep = System.getProperty("file.separator");
		String strHome = System.getProperty("user.home");
		StringBuffer sbFName = new StringBuffer();

		sbFName.append(strHome);
		sbFName.append(strSep);

		sbFName.append(".");
		sbFName.append(Ezim.appAbbrev);

		return sbFName.toString();
	}

	/**
	 * determine the appropriate configuration filename
	 */
	public static String getConfFilename()
	{
		String strSep = System.getProperty("file.separator");
		StringBuffer sbFName = new StringBuffer();

		sbFName.append(EzimConf.getConfDir());
		sbFName.append(strSep);
		sbFName.append(Ezim.appAbbrev);
		sbFName.append(".conf");

		return sbFName.toString();
	}

	/**
	 * read configuration items from file indicated by the given filename
	 */
	public void read()
	{
		FileInputStream fisTmp = null;

		try
		{
			fisTmp = new FileInputStream(EzimConf.getConfFilename());
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
	 */
	public void write()
	{
		File fTmp = null;
		FileOutputStream fosTmp = null;

		try
		{
			// prepare directories if necessary
			fTmp = new File(EzimConf.getConfDir());
			if (! fTmp.exists() || ! fTmp.isDirectory()) fTmp.mkdirs();

			// output configuration to file
			fosTmp = new FileOutputStream(EzimConf.getConfFilename());
			this.settings.store(fosTmp, "--- ezim Configuration File ---");
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
