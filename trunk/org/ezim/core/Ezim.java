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
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.ezim.core.EzimAckSemantics;
import org.ezim.core.EzimAckTaker;
import org.ezim.core.EzimConf;
import org.ezim.core.EzimDtxTaker;
import org.ezim.core.EzimLang;
import org.ezim.core.EzimLogger;
import org.ezim.core.EzimThreadPool;
import org.ezim.ui.EzimMain;

public class Ezim
{
	// D E F A U L T   C O N S T A N T S -----------------------------------
	// application name and version
	public final static String appName = "EZ Intranet Messenger";
	public final static String appAbbrev = "ezim";
	public final static String appVer = "1.2.5";

	// thread pool sizes and keep alive time (in minutes)
	public final static int thPoolSizeCore = 8;
	public final static int thPoolSizeMax = 16;
	public final static int thPoolKeepAlive = 30;

	// multicast group, port, TTL, and incoming buffer size
	// where group should be from 224.0.0.0 to 239.255.255.255
	public final static String mcGroupIPv4 = "229.0.0.1";
	public final static String mcGroupIPv6 = "ff15::657a:696d";
	public final static int mcPort = 5555;
	public final static int ttl = 1;
	public final static int inBuf = 4096;

	// maximum textfield lengths (for Ack messages)
	public final static int maxAckLength = inBuf / 4;

	// direct transmission port and timeout limit (in ms)
	public final static int dtxPort = 6666;
	public final static int dtxTimeout = 30000;

	// direct transmission message encoding
	public final static String dtxMsgEnc = "UTF-8";

	// direct transmission buffer length (in bytes)
	public final static int dtxBufLen = 1024;

	// time interval for the refresh button to be re-enabled after being
	// clicked (to avoid ACK flooding)
	public final static int rfhBtnTI = 5000;

	// self-entry background color on the contact list
	public final static int colorSelf = (int) 0xDEEFFF;

	// available locales
	public final static Locale[] locales =
	{
		Locale.US
		, Locale.JAPAN
		, Locale.SIMPLIFIED_CHINESE
		, Locale.TRADITIONAL_CHINESE
		, new Locale("es")			// Spanish
		, new Locale("pt", "BR")	// Portuguese (Brazil)
		, Locale.ITALY
		, Locale.FRANCE
	};

	// regexp for validating IPv4 address
	public static final String regexpIPv4
		= "\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)"
		+ "(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z";

	// regexp for validating IPv6 address
	public static final String regexpIPv6
		= "\\A("
		+ "(?:(?:[0-9A-Fa-f]{1,4}\\:){1,1}(?:\\:[0-9A-Fa-f]{1,4}){1,6})"
		+ "|(?:(?:[0-9A-Fa-f]{1,4}\\:){1,2}(?:\\:[0-9A-Fa-f]{1,4}){1,5})"
		+ "|(?:(?:[0-9A-Fa-f]{1,4}\\:){1,3}(?:\\:[0-9A-Fa-f]{1,4}){1,4})"
		+ "|(?:(?:[0-9A-Fa-f]{1,4}\\:){1,4}(?:\\:[0-9A-Fa-f]{1,4}){1,3})"
		+ "|(?:(?:[0-9A-Fa-f]{1,4}\\:){1,5}(?:\\:[0-9A-Fa-f]{1,4}){1,2})"
		+ "|(?:(?:[0-9A-Fa-f]{1,4}\\:){1,6}(?:\\:[0-9A-Fa-f]{1,4}){1,1})"
		+ "|(?:[0-9A-Fa-f]{1,4}(?:\\:[0-9A-Fa-f]{1,4}){7})"
		+ "|(?:(?:(?:[0-9A-Fa-f]{1,4}\\:){1,7}|\\:)\\:)"
		+ "|(?:\\:(?:\\:[0-9A-Fa-f]{1,4}){1,7})"
		+ "|(?:(?:(?:(?:[0-9A-Fa-f]{1,4}\\:){6})(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}))"
		+ "|(?:(?:(?:[0-9A-Fa-f]{1,4}\\:){5}[0-9A-Fa-f]{1,4}\\:(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}))"
		+ "|(?:(?:[0-9A-Fa-f]{1,4}\\:){5}\\:[0-9A-Fa-f]{1,4}\\:(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})"
		+ "|(?:(?:[0-9A-Fa-f]{1,4}\\:){1,1}(?:\\:[0-9A-Fa-f]{1,4}){1,4}\\:(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})"
		+ "|(?:(?:[0-9A-Fa-f]{1,4}\\:){1,2}(?:\\:[0-9A-Fa-f]{1,4}){1,3}\\:(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})"
		+ "|(?:(?:[0-9A-Fa-f]{1,4}\\:){1,3}(?:\\:[0-9A-Fa-f]{1,4}){1,2}\\:(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})"
		+ "|(?:(?:[0-9A-Fa-f]{1,4}\\:){1,4}(?:\\:[0-9A-Fa-f]{1,4}){1,1}\\:(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})"
		+ "|(?:(?:(?:[0-9A-Fa-f]{1,4}\\:){1,5}|\\:)\\:(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})"
		+ "|(?:\\:(?:\\:[0-9A-Fa-f]{1,4}){1,5}\\:(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})"
		+ ")(?:\\%\\d+)?\\z"
		;

	// P R O P E R T I E S -------------------------------------------------
	public static ArrayList<InetAddress> localAddresses = null;
	public static InetAddress localAddress = null;
	public static NetworkInterface operatingNI = null;
	public static int localDtxPort = 0;
	public static String localName = null;

	// P R I V A T E   M E T H O D S ---------------------------------------
	/**
	 * prepare the save data directory if not exist
	 */
	private static void mkSaveDataDir()
	{
		File fTmp = new File(EzimConf.getConfDir());

		try
		{
			if (! fTmp.isDirectory()) fTmp.mkdirs();
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			System.exit(1);
		}

		return;
	}

	/**
	 * locale change has to be here in order to work properly
	 */
	private static void setDefaultLocale()
	{
		EzimConf ecTmp = EzimConf.getInstance();

		String strLocale = ecTmp.settings.getProperty
		(
			EzimConf.ezimUserLocale
		);

		for(int iCnt = 0; iCnt < Ezim.locales.length; iCnt ++)
		{
			if (strLocale.equals(Ezim.locales[iCnt].toString()))
			{
				Locale.setDefault(Ezim.locales[iCnt]);
				break;
			}
		}

		return;
	}

	/**
	 * initialize local addresses
	 */
	private static void initLocalAddresses()
	{
		Enumeration<NetworkInterface> enumNI = null;
		NetworkInterface niTmp = null;
		Enumeration<InetAddress> enumIA = null;
		InetAddress iaTmp = null;
		Ezim.localAddresses = new ArrayList<InetAddress>();

		try
		{
			enumNI = NetworkInterface.getNetworkInterfaces();

			while(enumNI.hasMoreElements())
			{
				niTmp = enumNI.nextElement();

				if (! niTmp.isUp() || ! niTmp.supportsMulticast()) continue;

				enumIA = niTmp.getInetAddresses();

				while(enumIA.hasMoreElements())
				{
					iaTmp = enumIA.nextElement();

					if
					(
						! Ezim.localAddresses.contains(iaTmp)
					)
					{
						Ezim.localAddresses.add(iaTmp);
					}
				}
			}
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			System.exit(1);
		}

		Ezim.localAddresses.trimToSize();

		return;
	}

	/**
	 * parse and instantiate an InetAddress
	 */
	private static InetAddress parseInetAddress(String strIn)
	{
		InetAddress iaOut = null;
		String[] strBytes = null;
		byte[] arrBytes = null;

		if
		(
			strIn.matches(Ezim.regexpIPv4)
			|| strIn.matches(Ezim.regexpIPv6)
		)
		{
			try
			{
				iaOut = InetAddress.getByName(strIn);
			}
			catch(Exception e)
			{
				// this should NEVER happen
				EzimLogger.getInstance().severe(e.getMessage(), e);
				iaOut = null;
			}
		}

		return iaOut;
	}

	/**
	 * set local address
	 */
	private static void setLocalAddress()
	{
		EzimConf ecTmp = EzimConf.getInstance();
		String strLclAdr = ecTmp.settings.getProperty
		(
			EzimConf.ezimLocaladdress
		);

		if (strLclAdr != null && strLclAdr.length() > 0)
		{
			Ezim.localAddress = Ezim.parseInetAddress(strLclAdr);

			if (! Ezim.localAddresses.contains(Ezim.localAddress))
			{
				EzimLogger.getInstance().warning
				(
					"Invalid local address setting \"" + strLclAdr
						+ "\"."
				);

				Ezim.localAddress = null;
			}
		}

		if (Ezim.localAddress == null)
		{
			// try to pick an IPv6 non-loopback and non-link-locale address
			for(InetAddress iaTmp: Ezim.localAddresses)
			{
				if
				(
					iaTmp instanceof Inet6Address
					&& ! iaTmp.isLoopbackAddress()
					&& ! iaTmp.isLinkLocalAddress()
				)
				{
					Ezim.localAddress = iaTmp;
					break;
				}
			}
		}

		// try to pick a non-loopback and non-link-locale address
		if (Ezim.localAddress == null)
		{
			for(InetAddress iaTmp: Ezim.localAddresses)
			{
				if
				(
					! iaTmp.isLoopbackAddress()
					&& ! iaTmp.isLinkLocalAddress()
				)
				{
					Ezim.localAddress = iaTmp;
					break;
				}
			}
		}

		// try to pick an IPv6 non-loopback address
		if (Ezim.localAddress == null)
		{
			for(InetAddress iaTmp: Ezim.localAddresses)
			{
				if
				(
					iaTmp instanceof Inet6Address
					&& ! iaTmp.isLoopbackAddress()
				)
				{
					Ezim.localAddress = iaTmp;
					break;
				}
			}
		}

		// try to pick a non-loopback address
		if (Ezim.localAddress == null)
		{
			for(InetAddress iaTmp: Ezim.localAddresses)
			{
				if (! iaTmp.isLoopbackAddress())
				{
					Ezim.localAddress = iaTmp;
					break;
				}
			}
		}

		// try to pick an IPv6 address
		if (Ezim.localAddress == null)
		{
			for(InetAddress iaTmp: Ezim.localAddresses)
			{
				if (iaTmp instanceof Inet6Address)
				{
					Ezim.localAddress = iaTmp;
					break;
				}
			}
		}

		// pick the first available address when all failed
		if (Ezim.localAddress == null)
		{
			Ezim.localAddress = Ezim.localAddresses.get(0);
		}

		ecTmp.settings.setProperty
		(
			EzimConf.ezimLocaladdress
			, Ezim.localAddress.getHostAddress()
		);

		return;
	}

	/**
	 * set multicast group IP address
	 */
	private static void setMcGroup()
	{
		EzimConf ecTmp = EzimConf.getInstance();
		String strMcGroup = ecTmp.settings.getProperty
		(
			EzimConf.ezimMcGroup
		);

		// set multicast group to default if not yet determined or
		// inappropriate
		if
		(
			Ezim.localAddress instanceof Inet6Address
			&& (
				strMcGroup == null
				|| ! strMcGroup.matches(Ezim.regexpIPv6)
			)
		)
		{
			ecTmp.settings.setProperty
			(
				EzimConf.ezimMcGroup
				, Ezim.mcGroupIPv6
			);
		}
		else if
		(
			Ezim.localAddress instanceof Inet4Address
			&& (
				strMcGroup == null
				|| ! strMcGroup.matches(Ezim.regexpIPv4)
			)
		)
		{
			ecTmp.settings.setProperty
			(
				EzimConf.ezimMcGroup
				, Ezim.mcGroupIPv4
			);
		}
		else
		{
			InetAddress iaTmp = null;

			try
			{
				iaTmp = InetAddress.getByName(strMcGroup);
			}
			catch(Exception e)
			{
				// this should NEVER happen
				EzimLogger.getInstance().severe(e.getMessage(), e);
				iaTmp = null;
			}

			if (iaTmp == null || ! iaTmp.isMulticastAddress())
			{
				if (Ezim.localAddress instanceof Inet6Address)
				{
					ecTmp.settings.setProperty
					(
						EzimConf.ezimMcGroup
						, Ezim.mcGroupIPv6
					);
				}
				else if (Ezim.localAddress instanceof Inet4Address)
				{
					ecTmp.settings.setProperty
					(
						EzimConf.ezimMcGroup
						, Ezim.mcGroupIPv4
					);
				}
			}
		}

		return;
	}

	/**
	 * set operating network interface
	 */
	private static void setOperatingNI()
	{
		try
		{
			Ezim.operatingNI = NetworkInterface.getByInetAddress
			(
				Ezim.localAddress
			);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			System.exit(1);
		}

		return;
	}

	/**
	 * set local DTX port
	 */
	private static void setLocalDtxPort()
	{
		EzimConf ecfTmp = EzimConf.getInstance();

		Ezim.localDtxPort = Integer.parseInt
		(
			ecfTmp.settings.getProperty(EzimConf.ezimDtxPort)
		);

		return;
	}

	/**
	 * set local name
	 */
	private static void setLocalName()
	{
		EzimConf ecTmp = EzimConf.getInstance();

		Ezim.localName = ecTmp.settings.getProperty
		(
			EzimConf.ezimLocalname
		);

		// query username if isn't set yet
		if (Ezim.localName == null || Ezim.localName.length() == 0)
		{
			String strTmp = null;

			// obtain user name
			while(strTmp == null || strTmp.length() == 0)
			{
				strTmp = JOptionPane.showInputDialog
				(
					EzimLang.PleaseInputYourName
				);
			}

			Ezim.localName = strTmp;

			// save username
			ecTmp.settings.setProperty
			(
				EzimConf.ezimLocalname
				, Ezim.localName
			);
		}

		return;
	}

	/**
	 * initialize network configurations
	 */
	private static void initNetConf()
	{
		Ezim.initLocalAddresses();
		Ezim.setLocalAddress();
		Ezim.setMcGroup();
		Ezim.setOperatingNI();
		Ezim.setLocalDtxPort();
		Ezim.setLocalName();

		return;
	}

	// P U B L I C   M E T H O D S -----------------------------------------
	/**
	 * the main function which gets executed
	 * @param arrArgs command line arguments
	 */
	public static void main(String[] arrArgs)
	{
		Ezim.mkSaveDataDir();
		Ezim.setDefaultLocale();

		UIManager.put("Button.defaultButtonFollowsFocus", true);

		EzimLang.init();
		EzimImage.init();

		Ezim.initNetConf();

		EzimMain emTmp = EzimMain.getInstance();

		EzimThreadPool etpTmp = EzimThreadPool.getInstance();

		EzimDtxTaker edtTmp = new EzimDtxTaker();
		etpTmp.execute(edtTmp);

		EzimAckTaker eatTmp = new EzimAckTaker();
		etpTmp.execute(eatTmp);

		try
		{
			Thread.sleep(500);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}

/*
		// execute proper ending processes when JVM shuts down
		Runtime.getRuntime().addShutdownHook
		(
			new Thread()
			{
				public void run()
				{
					EzimMain.getInstance().panic();
					return;
				}
			}
		);
*/

		EzimAckSemantics.sendAllInfo();
		emTmp.freshPoll();

		return;
	}
}
