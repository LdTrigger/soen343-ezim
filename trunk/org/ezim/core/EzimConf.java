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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.IllegalAccessException;
import java.lang.NoSuchMethodException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileLock;
import java.util.Locale;
import java.util.Properties;

import org.ezim.core.Ezim;
import org.ezim.core.EzimLogger;
import org.ezim.core.EzimSetting;

public class EzimConf
{
	// configuration item name
	@EzimSetting(type=Integer.class, name="thpool.size.core", defaultValue="8")
	public static Integer THPOOL_SIZE_CORE;
	@EzimSetting(type=Integer.class, name="thpool.size.max", defaultValue="16")
	public static Integer THPOOL_SIZE_MAX;
	@EzimSetting(type=Integer.class, name="thpool.KeepAlive", defaultValue="30")
	public static Integer THPOOL_KEEPALIVE;

	@EzimSetting(type=String.class, name="net.multicast.group", defaultValue="")
	public static String NET_MC_GROUP;
	@EzimSetting(type=Integer.class, name="net.multicast.port", defaultValue="5555")
	public static Integer NET_MC_PORT;
	@EzimSetting(type=Integer.class, name="net.dtx.port", defaultValue="6666")
	public static Integer NET_DTX_PORT;

	@EzimSetting(type=String.class, name="net.localni", defaultValue="")
	public static String NET_LOCALNI;
	@EzimSetting(type=String.class, name="net.localaddress", defaultValue="")
	public static String NET_LOCALADDRESS;
	@EzimSetting(type=String.class, name="net.localname", defaultValue="")
	public static String NET_LOCALNAME;

	// default value 0xdeefff
	@EzimSetting(type=Integer.class, name="ui.color.self", defaultValue="14610431")
	public static Integer UI_COLOR_SELF;

	@EzimSetting(type=String.class, name="ui.user.locale", defaultValue="en_US")
	public static String UI_USER_LOCALE;

	@EzimSetting(type=Integer.class, name="ui.stateicon.size", defaultValue="24")
	public static Integer UI_STATEICON_SIZE;

	@EzimSetting(type=Boolean.class, name="ui.main.alwaysontop", defaultValue="false")
	public static Boolean UI_MAIN_ALWAYSONTOP;
	@EzimSetting(type=Boolean.class, name="ui.main.visible", defaultValue="true")
	public static Boolean UI_MAIN_VISIBLE;
	@EzimSetting(type=Integer.class, name="ui.main.location.x", defaultValue="0")
	public static Integer UI_MAIN_LOCATION_X;
	@EzimSetting(type=Integer.class, name="ui.main.location.y", defaultValue="0")
	public static Integer UI_MAIN_LOCATION_Y;
	@EzimSetting(type=Integer.class, name="ui.main.size.h", defaultValue="0")
	public static Integer UI_MAIN_SIZE_H;
	@EzimSetting(type=Integer.class, name="ui.main.size.w", defaultValue="0")
	public static Integer UI_MAIN_SIZE_W;

	@EzimSetting(type=Integer.class, name="ui.plaza.location.x", defaultValue="0")
	public static Integer UI_PLAZA_LOCATION_X;
	@EzimSetting(type=Integer.class, name="ui.plaza.location.y", defaultValue="0")
	public static Integer UI_PLAZA_LOCATION_Y;
	@EzimSetting(type=Integer.class, name="ui.plaza.size.h", defaultValue="0")
	public static Integer UI_PLAZA_SIZE_H;
	@EzimSetting(type=Integer.class, name="ui.plaza.size.w", defaultValue="0")
	public static Integer UI_PLAZA_SIZE_W;

	@EzimSetting(type=Integer.class, name="ui.msgout.location.x", defaultValue="0")
	public static Integer UI_MSGOUT_LOCATION_X;
	@EzimSetting(type=Integer.class, name="ui.msgout.location.y", defaultValue="0")
	public static Integer UI_MSGOUT_LOCATION_Y;
	@EzimSetting(type=Integer.class, name="ui.msgout.size.h", defaultValue="0")
	public static Integer UI_MSGOUT_SIZE_H;
	@EzimSetting(type=Integer.class, name="ui.msgout.size.w", defaultValue="0")
	public static Integer UI_MSGOUT_SIZE_W;

	@EzimSetting(type=Boolean.class, name="ui.msgin.autoopen", defaultValue="false")
	public static Boolean UI_MSGIN_AUTOOPEN;
	@EzimSetting(type=Integer.class, name="ui.msgin.location.x", defaultValue="0")
	public static Integer UI_MSGIN_LOCATION_X;
	@EzimSetting(type=Integer.class, name="ui.msgin.location.y", defaultValue="0")
	public static Integer UI_MSGIN_LOCATION_Y;
	@EzimSetting(type=Integer.class, name="ui.msgin.size.h", defaultValue="0")
	public static Integer UI_MSGIN_SIZE_H;
	@EzimSetting(type=Integer.class, name="ui.msgin.size.w", defaultValue="0")
	public static Integer UI_MSGIN_SIZE_W;

	@EzimSetting(type=Integer.class, name="ui.fileout.location.x", defaultValue="0")
	public static Integer UI_FILEOUT_LOCATION_X;
	@EzimSetting(type=Integer.class, name="ui.fileout.location.y", defaultValue="0")
	public static Integer UI_FILEOUT_LOCATION_Y;
	@EzimSetting(type=Integer.class, name="ui.fileout.size.h", defaultValue="0")
	public static Integer UI_FILEOUT_SIZE_H;
	@EzimSetting(type=Integer.class, name="ui.fileout.size.w", defaultValue="0")
	public static Integer UI_FILEOUT_SIZE_W;
	@EzimSetting(type=String.class, name="ui.fileout.directory", defaultValue="")
	public static String UI_FILEOUT_DIRECTORY;

	@EzimSetting(type=Integer.class, name="ui.filein.location.x", defaultValue="0")
	public static Integer UI_FILEIN_LOCATION_X;
	@EzimSetting(type=Integer.class, name="ui.filein.location.y", defaultValue="0")
	public static Integer UI_FILEIN_LOCATION_Y;
	@EzimSetting(type=Integer.class, name="ui.filein.size.h", defaultValue="0")
	public static Integer UI_FILEIN_SIZE_H;
	@EzimSetting(type=Integer.class, name="ui.filein.size.w", defaultValue="0")
	public static Integer UI_FILEIN_SIZE_W;
	@EzimSetting(type=String.class, name="ui.filein.directory", defaultValue="")
	public static String UI_FILEIN_DIRECTORY;

	@EzimSetting(type=Boolean.class, name="sound.enabled", defaultValue="true")
	public static Boolean SOUND_ENABLED;
	@EzimSetting(type=String.class, name="sound.statechg", defaultValue="")
	public static String SOUND_STATECHG;
	@EzimSetting(type=String.class, name="sound.statuschg", defaultValue="")
	public static String SOUND_STATUSCHG;
	@EzimSetting(type=String.class, name="sound.msgin", defaultValue="")
	public static String SOUND_MSGIN;
	@EzimSetting(type=String.class, name="sound.filein", defaultValue="")
	public static String SOUND_FILEIN;

	private static FileLock flock = null;

	// C O N S T R U C T O R -----------------------------------------------
	static
	{
		EzimConf.read();
		EzimConf.validate();
		EzimConf.prepareFile();
	}

	/**
	 * instantiate an object from a given string
	 * @param c class of the object to instantiate
	 * @param s string to instantiate from
	 * @return instance of the specified class
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	private static <T> T castFromStr(Class<T> c, String s)
		throws NoSuchMethodException
			, IllegalAccessException
			, InvocationTargetException
	{
		if (String.class == c)
			return (T) s;

		Method m = c.getMethod("valueOf", String.class);

		if (null == m)
		{
			throw new NoSuchMethodException
			(
				"Class " + c.getName()
					+ " does not have the valueOf() method."
			);
		}

		return (T) m.invoke(null, s);
	}

	/**
	 * read configuration items from file indicated by the given filename
	 */
	private static void read()
	{
		FileInputStream fisTmp = null;
		Properties props = new Properties();

		try
		{
			fisTmp = new FileInputStream(EzimConf.getConfFilename());
			props.load(fisTmp);
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

		try
		{
			for(Field fld: EzimConf.class.getFields())
			{
				EzimSetting setting  = fld.getAnnotation(EzimSetting.class);

				if (null == setting) continue;

				try
				{
					fld.set
					(
						null
						, EzimConf.castFromStr
						(
							setting.type()
							, props.getProperty
							(
								setting.name()
								, setting.defaultValue()
							)
						)
					);
				}
				catch(Exception e)
				{
					EzimLogger.getInstance().warning
					(
						"Cannot set property \"" + fld.getName() + "\"."
						, e
					);
				}
			}
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}
	}

	/**
	 * validate settings and set default values where necessary
	 */
	private static void validate()
	{
		boolean blnTmp = false;
		int iTmp = 0;
		String strTmp = null;

		// thread pool settings
		if (4 > EzimConf.THPOOL_SIZE_CORE) EzimConf.THPOOL_SIZE_CORE = 4;
		if (64 < EzimConf.THPOOL_SIZE_MAX) EzimConf.THPOOL_SIZE_MAX = 64;
		if (1 > EzimConf.THPOOL_KEEPALIVE) EzimConf.THPOOL_KEEPALIVE = 1;

		// ACK port
		if (0 > EzimConf.NET_MC_PORT || 65535 < EzimConf.NET_MC_PORT)
			EzimConf.NET_MC_PORT = 5555;

		// DTX port
		if (0 > EzimConf.NET_DTX_PORT || 65535 < EzimConf.NET_DTX_PORT)
			EzimConf.NET_DTX_PORT = 6666;

		// self-color
		EzimConf.UI_COLOR_SELF &= 0xffffff;

		// locale
		boolean blnInvalidLocale = true;
		for(int iCnt = 0; iCnt < Ezim.locales.length; iCnt ++)
		{
			if
			(
				EzimConf.UI_USER_LOCALE.equals
				(
					Ezim.locales[iCnt].toString()
				)
			)
			{
				blnInvalidLocale = false;
				break;
			}
		}
		if (blnInvalidLocale)
			EzimConf.UI_USER_LOCALE = Ezim.locales[0].toString();

		// state icon size
		blnTmp = true;
		for(int iCnt = 0; iCnt < Ezim.stateiconSizes.length; iCnt ++)
		{
			if
			(
				Ezim.stateiconSizes[iCnt].intValue()
					== EzimConf.UI_STATEICON_SIZE
			)
			{
				blnTmp = false;
				break;
			}
		}
		if (blnTmp)
		{
			EzimConf.UI_STATEICON_SIZE = Ezim.stateiconSizes[0];
		}

		// main window geometry
		if (0 > EzimConf.UI_MAIN_LOCATION_X)
			EzimConf.UI_MAIN_LOCATION_X = 0;
		if (0 > EzimConf.UI_MAIN_LOCATION_Y)
			EzimConf.UI_MAIN_LOCATION_Y = 0;
		if (0 > EzimConf.UI_MAIN_SIZE_H)
			EzimConf.UI_MAIN_SIZE_H = 0;
		if (0 > EzimConf.UI_MAIN_SIZE_W)
			EzimConf.UI_MAIN_SIZE_W = 0;

		// plaza window geometry
		if (0 > EzimConf.UI_PLAZA_LOCATION_X)
			EzimConf.UI_PLAZA_LOCATION_X = 0;
		if (0 > EzimConf.UI_PLAZA_LOCATION_Y)
			EzimConf.UI_PLAZA_LOCATION_Y = 0;
		if (0 > EzimConf.UI_PLAZA_SIZE_H)
			EzimConf.UI_PLAZA_SIZE_H = 0;
		if (0 > EzimConf.UI_PLAZA_SIZE_W)
			EzimConf.UI_PLAZA_SIZE_W = 0;

		// outgoing message window geometry
		if (0 > EzimConf.UI_MSGOUT_LOCATION_X)
			EzimConf.UI_MSGOUT_LOCATION_X = 0;
		if (0 > EzimConf.UI_MSGOUT_LOCATION_Y)
			EzimConf.UI_MSGOUT_LOCATION_Y = 0;
		if (0 > EzimConf.UI_MSGOUT_SIZE_H)
			EzimConf.UI_MSGOUT_SIZE_H = 0;
		if (0 > EzimConf.UI_MSGOUT_SIZE_W)
			EzimConf.UI_MSGOUT_SIZE_W = 0;

		// incoming message window geometry
		if (0 > EzimConf.UI_MSGIN_LOCATION_X)
			EzimConf.UI_MSGIN_LOCATION_X = 0;
		if (0 > EzimConf.UI_MSGIN_LOCATION_Y)
			EzimConf.UI_MSGIN_LOCATION_Y = 0;
		if (0 > EzimConf.UI_MSGIN_SIZE_H)
			EzimConf.UI_MSGIN_SIZE_H = 0;
		if (0 > EzimConf.UI_MSGIN_SIZE_W)
			EzimConf.UI_MSGIN_SIZE_W = 0;

		// outgoing file window geometry
		if (0 > EzimConf.UI_FILEOUT_LOCATION_X)
			EzimConf.UI_FILEOUT_LOCATION_X = 0;
		if (0 > EzimConf.UI_FILEOUT_LOCATION_Y)
			EzimConf.UI_FILEOUT_LOCATION_Y = 0;
		if (0 > EzimConf.UI_FILEOUT_SIZE_H)
			EzimConf.UI_FILEOUT_SIZE_H = 0;
		if (0 > EzimConf.UI_FILEOUT_SIZE_W)
			EzimConf.UI_FILEOUT_SIZE_W = 0;

		// incoming file window geometry
		if (0 > EzimConf.UI_FILEIN_LOCATION_X)
			EzimConf.UI_FILEIN_LOCATION_X = 0;
		if (0 > EzimConf.UI_FILEIN_LOCATION_Y)
			EzimConf.UI_FILEIN_LOCATION_Y = 0;
		if (0 > EzimConf.UI_FILEIN_SIZE_H)
			EzimConf.UI_FILEIN_SIZE_H = 0;
		if (0 > EzimConf.UI_FILEIN_SIZE_W)
			EzimConf.UI_FILEIN_SIZE_W = 0;
	}

	/**
	 * prepare and lock the physical file
	 */
	private static void prepareFile()
	{
		try
		{
			// prepare directories if necessary
			File fTmp = new File(EzimConf.getConfDir());
			if (! fTmp.exists() || ! fTmp.isDirectory()) fTmp.mkdirs();

			// attempt lock
			EzimConf.flock = new FileOutputStream
			(
				EzimConf.getLockFilename()
			).getChannel().tryLock();

			if (EzimConf.flock == null)
			{
				throw new Exception
				(
					"Only one program instance can be run at a time."
				);
			}
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			Ezim.exit(1);
		}
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
	 * determine the appropriate lock filename
	 */
	public static String getLockFilename()
	{
		String strSep = System.getProperty("file.separator");
		StringBuffer sbFName = new StringBuffer();

		sbFName.append(EzimConf.getConfDir());
		sbFName.append(strSep);
		sbFName.append(Ezim.appAbbrev);
		sbFName.append(".lock");

		return sbFName.toString();
	}

	/**
	 * write configuration items to the file indicated by the given filename
	 */
	synchronized public static void write()
	{
		FileOutputStream fosTmp = null;
		Properties props = new Properties();

		try
		{
			for(Field fld: EzimConf.class.getFields())
			{
				EzimSetting setting  = fld.getAnnotation(EzimSetting.class);

				if (null == setting) continue;

				props.setProperty(setting.name(), fld.get(null).toString());
			}

			// output configuration to file
			fosTmp = new FileOutputStream(EzimConf.getConfFilename());
			props.store(fosTmp, "--- ezim Configuration File ---");
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
	}
}
