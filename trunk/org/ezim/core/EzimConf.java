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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Properties;

import org.ezim.core.Ezim;
import org.ezim.core.EzimLogger;

public class EzimConf
{
	// configuration item name
	public final static String ezimThPoolSizeCore = "ezim.thpoolsize.core";
	public final static String ezimThPoolSizeMax = "ezim.thpoolsize.max";
	public final static String ezimThPoolKeepAlive = "ezim.thpool.KeepAlive";

	public final static String ezimMcGroup = "ezim.multicast.group";
	public final static String ezimMcPort = "ezim.multicast.port";
	public final static String ezimDtxPort = "ezim.dtx.port";

	public final static String ezimLocaladdress = "ezim.localaddress";
	public final static String ezimLocalname = "ezim.localname";

	public final static String ezimColorSelf = "ezim.color.self";

	public final static String ezimUserLocale = "ezim.user.locale";

	public final static String ezimStateiconSize = "ezim.stateicon.size";

	public final static String ezimmainAlwaysontop = "ezimmain.alwaysontop";
	public final static String ezimmainVisible = "ezimmain.visible";
	public final static String ezimmainLocationX = "ezimmain.location.x";
	public final static String ezimmainLocationY = "ezimmain.location.y";
	public final static String ezimmainSizeH = "ezimmain.size.h";
	public final static String ezimmainSizeW = "ezimmain.size.w";

	public final static String ezimplazaLocationX = "ezimplaza.location.x";
	public final static String ezimplazaLocationY = "ezimplaza.location.y";
	public final static String ezimplazaSizeH = "ezimplaza.size.h";
	public final static String ezimplazaSizeW = "ezimplaza.size.w";

	public final static String ezimmsgoutLocationX = "ezimmsgout.location.x";
	public final static String ezimmsgoutLocationY = "ezimmsgout.location.y";
	public final static String ezimmsgoutSizeH = "ezimmsgout.size.h";
	public final static String ezimmsgoutSizeW = "ezimmsgout.size.w";

	public final static String ezimmsginAutoopen = "ezimmsgin.autoopen";
	public final static String ezimmsginLocationX = "ezimmsgin.location.x";
	public final static String ezimmsginLocationY = "ezimmsgin.location.y";
	public final static String ezimmsginSizeH = "ezimmsgin.size.h";
	public final static String ezimmsginSizeW = "ezimmsgin.size.w";

	public final static String ezimfileoutLocationX = "ezimfileout.location.x";
	public final static String ezimfileoutLocationY = "ezimfileout.location.y";
	public final static String ezimfileoutSizeH = "ezimfileout.size.h";
	public final static String ezimfileoutSizeW = "ezimfileout.size.w";
	public final static String ezimfileoutDirectory = "ezimfileout.directory";

	public final static String ezimfileinLocationX = "ezimfilein.location.x";
	public final static String ezimfileinLocationY = "ezimfilein.location.y";
	public final static String ezimfileinSizeH = "ezimfilein.size.h";
	public final static String ezimfileinSizeW = "ezimfilein.size.w";
	public final static String ezimfileinDirectory = "ezimfilein.directory";

	public final static String ezimsoundEnabled = "ezimsound.enabled";

	// configuration item
	public Properties settings;

	// singleton object
	private static EzimConf ezconf = null;

	// C O N S T R U C T O R -----------------------------------------------
	/**
	 * construct an instance of the configuration class
	 */
	private EzimConf()
	{
		this.settings = new Properties();

		this.read();

		this.validate();
	}

	/**
	 * validate settings and set default values where necessary
	 */
	private void validate()
	{
		// thread pool settings
		if (this.settings.getProperty(EzimConf.ezimThPoolSizeCore) == null)
		{
			this.settings.setProperty
			(
				EzimConf.ezimThPoolSizeCore
				, Integer.toString(Ezim.thPoolSizeCore)
			);
		}
		if (this.settings.getProperty(EzimConf.ezimThPoolSizeMax) == null)
		{
			this.settings.setProperty
			(
				EzimConf.ezimThPoolSizeMax
				, Integer.toString(Ezim.thPoolSizeMax)
			);
		}
		if (this.settings.getProperty(EzimConf.ezimThPoolKeepAlive) == null)
		{
			this.settings.setProperty
			(
				EzimConf.ezimThPoolKeepAlive
				, Integer.toString(Ezim.thPoolKeepAlive)
			);
		}

		// ACK port
		int iMcPort = -1;
		if (this.settings.getProperty(EzimConf.ezimMcPort) != null)
		{
			iMcPort = Integer.parseInt
			(
				this.settings.getProperty(EzimConf.ezimMcPort)
			);
		}
		if (iMcPort < 0 || iMcPort > 65535)
		{
			this.settings.setProperty
			(
				EzimConf.ezimMcPort
				, Integer.toString(Ezim.mcPort)
			);
		}

		// DTX port
		int iDtxPort = -1;
		if (this.settings.getProperty(EzimConf.ezimDtxPort) != null)
		{
			iDtxPort = Integer.parseInt
			(
				this.settings.getProperty(EzimConf.ezimDtxPort)
			);
		}
		if (iDtxPort < 0 || iDtxPort > 65535)
		{
			this.settings.setProperty
			(
				EzimConf.ezimDtxPort
				, Integer.toString(Ezim.dtxPort)
			);
		}

		// self-color
		String strRgb = this.settings.getProperty(EzimConf.ezimColorSelf);
		if (strRgb == null || ! strRgb.matches(Ezim.regexpRgb))
		{
			this.settings.setProperty
			(
				EzimConf.ezimColorSelf
				, Integer.toString(Ezim.colorSelf, 16)
			);
		}

		// locale
		String strLocale = this.settings.getProperty
		(
			EzimConf.ezimUserLocale
		);
		if (strLocale == null) strLocale = Locale.getDefault().toString();
		boolean blnInvalidLocale = true;
		for(int iCnt = 0; iCnt < Ezim.locales.length; iCnt ++)
		{
			if (strLocale.equals(Ezim.locales[iCnt].toString()))
			{
				blnInvalidLocale = false;
				break;
			}
		}
		if (blnInvalidLocale) strLocale = Ezim.locales[0].toString();
		this.settings.setProperty
		(
			EzimConf.ezimUserLocale
			, strLocale
		);

		// state icon size
		int iStateiconSize = -1;
		boolean blnInvalidStateiconSize = true;
		if (this.settings.getProperty(EzimConf.ezimStateiconSize) != null)
		{
			iStateiconSize = Integer.parseInt
			(
				this.settings.getProperty(EzimConf.ezimStateiconSize)
			);
		}
		for(int iCnt = 0; iCnt < Ezim.stateiconSizes.length; iCnt ++)
		{
			if (Ezim.stateiconSizes[iCnt].intValue() == iStateiconSize)
			{
				blnInvalidStateiconSize = false;
				break;
			}
		}
		if (blnInvalidStateiconSize)
		{
			this.settings.setProperty
			(
				EzimConf.ezimStateiconSize
				, Ezim.stateiconSizes[0].toString()
			);
		}

		// main window geometry
		if (this.settings.getProperty(EzimConf.ezimmainAlwaysontop) == null)
			this.settings.setProperty(EzimConf.ezimmainAlwaysontop, "false");
		if (this.settings.getProperty(EzimConf.ezimmainVisible) == null)
			this.settings.setProperty(EzimConf.ezimmainVisible, "true");
		if (this.settings.getProperty(EzimConf.ezimmainLocationX) == null)
			this.settings.setProperty(EzimConf.ezimmainLocationX, "0");
		if (this.settings.getProperty(EzimConf.ezimmainLocationY) == null)
			this.settings.setProperty(EzimConf.ezimmainLocationY, "0");
		if (this.settings.getProperty(EzimConf.ezimmainSizeH) == null)
			this.settings.setProperty(EzimConf.ezimmainSizeH, "0");
		if (this.settings.getProperty(EzimConf.ezimmainSizeW) == null)
			this.settings.setProperty(EzimConf.ezimmainSizeW, "0");

		// plaza window geometry
		if (this.settings.getProperty(EzimConf.ezimplazaLocationX) == null)
			this.settings.setProperty(EzimConf.ezimplazaLocationX, "0");
		if (this.settings.getProperty(EzimConf.ezimplazaLocationY) == null)
			this.settings.setProperty(EzimConf.ezimplazaLocationY, "0");
		if (this.settings.getProperty(EzimConf.ezimplazaSizeH) == null)
			this.settings.setProperty(EzimConf.ezimplazaSizeH, "0");
		if (this.settings.getProperty(EzimConf.ezimplazaSizeW) == null)
			this.settings.setProperty(EzimConf.ezimplazaSizeW, "0");

		// outgoing message window geometry
		if (this.settings.getProperty(EzimConf.ezimmsgoutLocationX) == null)
			this.settings.setProperty(EzimConf.ezimmsgoutLocationX, "0");
		if (this.settings.getProperty(EzimConf.ezimmsgoutLocationY) == null)
			this.settings.setProperty(EzimConf.ezimmsgoutLocationY, "0");
		if (this.settings.getProperty(EzimConf.ezimmsgoutSizeH) == null)
			this.settings.setProperty(EzimConf.ezimmsgoutSizeH, "0");
		if (this.settings.getProperty(EzimConf.ezimmsgoutSizeW) == null)
			this.settings.setProperty(EzimConf.ezimmsgoutSizeW, "0");

		// incoming message window geometry
		if (this.settings.getProperty(EzimConf.ezimmsginAutoopen) == null)
			this.settings.setProperty(EzimConf.ezimmsginAutoopen, "false");
		if (this.settings.getProperty(EzimConf.ezimmsginLocationX) == null)
			this.settings.setProperty(EzimConf.ezimmsginLocationX, "0");
		if (this.settings.getProperty(EzimConf.ezimmsginLocationY) == null)
			this.settings.setProperty(EzimConf.ezimmsginLocationY, "0");
		if (this.settings.getProperty(EzimConf.ezimmsginSizeH) == null)
			this.settings.setProperty(EzimConf.ezimmsginSizeH, "0");
		if (this.settings.getProperty(EzimConf.ezimmsginSizeW) == null)
			this.settings.setProperty(EzimConf.ezimmsginSizeW, "0");

		// outgoing file window geometry
		if (this.settings.getProperty(EzimConf.ezimfileoutLocationX) == null)
			this.settings.setProperty(EzimConf.ezimfileoutLocationX, "0");
		if (this.settings.getProperty(EzimConf.ezimfileoutLocationY) == null)
			this.settings.setProperty(EzimConf.ezimfileoutLocationY, "0");
		if (this.settings.getProperty(EzimConf.ezimfileoutSizeH) == null)
			this.settings.setProperty(EzimConf.ezimfileoutSizeH, "0");
		if (this.settings.getProperty(EzimConf.ezimfileoutSizeW) == null)
			this.settings.setProperty(EzimConf.ezimfileoutSizeW, "0");

		// incoming file window geometry
		if (this.settings.getProperty(EzimConf.ezimfileinLocationX) == null)
			this.settings.setProperty(EzimConf.ezimfileinLocationX, "0");
		if (this.settings.getProperty(EzimConf.ezimfileinLocationY) == null)
			this.settings.setProperty(EzimConf.ezimfileinLocationY, "0");
		if (this.settings.getProperty(EzimConf.ezimfileinSizeH) == null)
			this.settings.setProperty(EzimConf.ezimfileinSizeH, "0");
		if (this.settings.getProperty(EzimConf.ezimfileinSizeW) == null)
			this.settings.setProperty(EzimConf.ezimfileinSizeW, "0");

		// sound
		if (this.settings.getProperty(EzimConf.ezimsoundEnabled) == null)
			this.settings.setProperty(EzimConf.ezimsoundEnabled, "true");

		return;
	}

	// P U B L I C   M E T H O D S -----------------------------------------
	/**
	 * return an EzimConf object
	 */
	public static EzimConf getInstance()
	{
		if (EzimConf.ezconf == null)
			EzimConf.ezconf = new EzimConf();

		return EzimConf.ezconf;
	}

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
		catch(FileNotFoundException fnfe)
		{
			// we can safely ignore this
			EzimLogger.getInstance().warning(fnfe.getMessage(), fnfe);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (fisTmp != null) fisTmp.close();
			}
			catch(Exception e)
			{
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}
		}

		return;
	}

	/**
	 * write configuration items to the file indicated by the given filename
	 */
	public synchronized void write()
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
			EzimLogger.getInstance().severe(e.getMessage(), e);
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
				EzimLogger.getInstance().severe(e.getMessage(), e);
			}
		}

		return;
	}
}
