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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.ezim.core.EzimAckSemantics;
import org.ezim.core.EzimAckSender;
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
	public final static String appVer = "1.2.27";

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

	// maximum textfield lengths (for ACK messages)
	public final static int maxAckLength = inBuf / 4;

	// maximum textarea lengths (for DTX messages)
	public final static int maxMsgLength = Integer.MAX_VALUE / 4;

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

	// thread pool termination timeout (in seconds) at exit
	public final static long exitTimeout = 15;

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
		, Locale.GERMANY
		, new Locale("nl", "NL")	// Dutch (Netherlands)
		, new Locale("el", "GR")	// Greek (Greece)
	};

	// regexp for validating IPv4 address
	public final static String regexpIPv4
		= "\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)"
		+ "(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z";

	// regexp for validating IPv6 address
	public final static String regexpIPv6
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
		+ ")(?:\\%[^\\%]+)?\\z"
		;

	// regexp for validating RGB color
	public final static String regexpRgb = "\\A[0-9A-Fa-f]{6}\\z";

	// regexp for validating boolean values
	public final static String regexpBool = "\\A(true|false)\\z";

	// regexp for validating integer values
	public final static String regexpInt = "\\A([1-9][0-9]*|0)\\z";

	// valid state icon sizes
	public final static Integer[] stateiconSizes = {16, 24, 32};

	// P R O P E R T I E S -------------------------------------------------
	private static Hashtable<NetworkInterface, List<InetAddress>>
		nifs = null;
	private static volatile boolean running = true;
	private static volatile boolean shutdown = false;

	public static NetworkInterface localNI = null;
	public static InetAddress localAddress = null;
	public static int localDtxPort = 0;
	public static String localName = null;

	public static Thread thAckTaker = null;
	public static Thread thDtxTaker = null;

	// P R I V A T E   M E T H O D S ---------------------------------------
	/**
	 * locale change has to be here in order to work properly
	 */
	private static void setDefaultLocale()
	{
		String strLocale = EzimConf.UI_USER_LOCALE;

		for(int iCnt = 0; iCnt < Ezim.locales.length; iCnt ++)
		{
			if (strLocale.equals(Ezim.locales[iCnt].toString()))
			{
				Locale.setDefault(Ezim.locales[iCnt]);
				break;
			}
		}
	}

	/**
	 * scan all network interfaces
	 */
	private static void scanNetworkInterfaces()
	{
		Enumeration<NetworkInterface> enumNI = null;
		NetworkInterface niTmp = null;
		Ezim.nifs = new Hashtable<NetworkInterface, List<InetAddress>>();

		try
		{
			enumNI = NetworkInterface.getNetworkInterfaces();

			while(enumNI.hasMoreElements())
			{
				niTmp = enumNI.nextElement();

				if (! niTmp.isUp() || ! niTmp.supportsMulticast()) continue;

				Ezim.nifs.put
				(
					niTmp
					, new ArrayList<InetAddress>
					(
						Collections.list(niTmp.getInetAddresses())
					)
				);
			}
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			Ezim.exit(1);
		}
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
	 * set local network interface and address
	 */
	private static void setLocalNiAddress()
	{
		String strLclNi = EzimConf.NET_LOCALNI;
		String strLclAdr = EzimConf.NET_LOCALADDRESS;

		if (strLclNi != null && strLclNi.length() > 0)
		{
			for(NetworkInterface niTmp: Ezim.nifs.keySet())
				if (strLclNi.equals(niTmp.getName()))
					Ezim.localNI = niTmp;

			if (null == Ezim.localNI)
			{
				EzimLogger.getInstance().warning
				(
					"Invalid network interface setting \"" + strLclNi
						+ "\"."
				);
			}
		}

		if
		(
			strLclAdr != null && strLclAdr.length() > 0
			&& Ezim.localNI != null
		)
		{
			List<InetAddress> lTmp = Ezim.nifs.get(Ezim.localNI);

			for(InetAddress iaTmp: lTmp)
				if (strLclAdr.equals(iaTmp.getHostAddress()))
					Ezim.localAddress = iaTmp;

			if (null == Ezim.localAddress)
			{
				EzimLogger.getInstance().warning
				(
					"Invalid local address setting \"" + strLclAdr
						+ "\"."
				);
			}
		}

		// confine our selectable network interfaces
		Collection<List<InetAddress>> cTmp = null;

		if (null == Ezim.localNI)
		{
			cTmp = Ezim.nifs.values();
		}
		else
		{
			cTmp = new ArrayList<List<InetAddress>>();

			((ArrayList<List<InetAddress>>) cTmp).add
			(
				Ezim.nifs.get(Ezim.localNI)
			);
		}

		if (Ezim.localAddress == null)
		{
			// try to pick an IPv6 non-loopback and non-link-locale address
			for(List<InetAddress> lTmp: cTmp)
			{
				for(InetAddress iaTmp: lTmp)
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
		}

		// try to pick a non-loopback and non-link-locale address
		if (Ezim.localAddress == null)
		{
			for(List<InetAddress> lTmp: cTmp)
			{
				for(InetAddress iaTmp: lTmp)
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
		}

		// try to pick an IPv6 non-loopback address
		if (Ezim.localAddress == null)
		{
			for(List<InetAddress> lTmp: cTmp)
			{
				for(InetAddress iaTmp: lTmp)
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
		}

		// try to pick a non-loopback address
		if (Ezim.localAddress == null)
		{
			for(List<InetAddress> lTmp: cTmp)
			{
				for(InetAddress iaTmp: lTmp)
				{
					if (! iaTmp.isLoopbackAddress())
					{
						Ezim.localAddress = iaTmp;
						break;
					}
				}
			}
		}

		// try to pick an IPv6 address
		if (Ezim.localAddress == null)
		{
			for(List<InetAddress> lTmp: cTmp)
			{
				for(InetAddress iaTmp: lTmp)
				{
					if (iaTmp instanceof Inet6Address)
					{
						Ezim.localAddress = iaTmp;
						break;
					}
				}
			}
		}

		// pick the first available address when all failed
		if (Ezim.localAddress == null)
		{
			Ezim.localAddress = Ezim.nifs.elements().nextElement().get(0);
		}

		if (null == Ezim.localNI)
			for(NetworkInterface niTmp: Ezim.nifs.keySet())
				if (Ezim.nifs.get(niTmp).contains(Ezim.localAddress))
					Ezim.localNI = niTmp;

		// save local network interface
		EzimConf.NET_LOCALNI = Ezim.localNI.getName();

		// save local address
		EzimConf.NET_LOCALADDRESS = Ezim.localAddress.getHostAddress();
	}

	/**
	 * set multicast group IP address
	 */
	private static void setMcGroup()
	{
		String strMcGroup = EzimConf.NET_MC_GROUP;

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
			EzimConf.NET_MC_GROUP = Ezim.mcGroupIPv6;
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
			EzimConf.NET_MC_GROUP = Ezim.mcGroupIPv4;
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
					EzimConf.NET_MC_GROUP = Ezim.mcGroupIPv6;
				else if (Ezim.localAddress instanceof Inet4Address)
					EzimConf.NET_MC_GROUP = Ezim.mcGroupIPv4;
			}
		}
	}

	/**
	 * set operating network interface
	 */
	private static void setOperatingNI()
	{
		try
		{
			Ezim.localNI = NetworkInterface.getByInetAddress
			(
				Ezim.localAddress
			);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
			Ezim.exit(1);
		}
	}

	/**
	 * set local DTX port
	 */
	private static void setLocalDtxPort()
	{
		Ezim.localDtxPort = EzimConf.NET_DTX_PORT;
	}

	/**
	 * set local name
	 */
	private static void setLocalName()
	{
		Ezim.localName = EzimConf.NET_LOCALNAME;

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
			EzimConf.NET_LOCALNAME = Ezim.localName;

			EzimConf.write();
		}
	}

	/**
	 * initialize network configurations
	 */
	private static void initNetConf()
	{
		Ezim.scanNetworkInterfaces();
		Ezim.setLocalNiAddress();
		Ezim.setMcGroup();
		Ezim.setOperatingNI();
		Ezim.setLocalDtxPort();
		Ezim.setLocalName();
	}

	// P U B L I C   M E T H O D S -----------------------------------------
	/**
	 * get all available network interfaces
	 */
	public static Collection<NetworkInterface> getLocalNIs()
	{
		return (Collection<NetworkInterface>)
			Ezim.nifs.keySet();
	}

	/**
	 * get all addresses associated with the specified network interface
	 * @param network interface to retrieve addresses
	 */
	public static List<InetAddress> getNIAddresses(NetworkInterface niIn)
	{
		return Ezim.nifs.get(niIn);
	}

	/**
	 * check if the address provided is local
	 * @param iaIn address to check against
	 */
	public static boolean isLocalAddress(InetAddress iaIn)
	{
		for(List<InetAddress> lAddrs: Ezim.nifs.values())
			for(InetAddress iaddr: lAddrs)
				if (iaIn.equals(iaddr)) return true;

		return false;
	}

	/**
	 * perform cleanup and exit
	 * @param iIn exit code
	 */
	public static void exit(final int iIn)
	{
		if (! Ezim.running) return;

		Ezim.running = false;

		Ezim.thAckTaker.interrupt();
		EzimAckTaker.getInstance().closeSocket();
		Ezim.thDtxTaker.interrupt();
		EzimDtxTaker.getInstance().closeSocket();

		EzimThreadPool etpTmp = EzimThreadPool.getInstance();

		// acknowledge other peers we're going offline
		EzimAckSender easOff = new EzimAckSender
		(
			EzimAckSemantics.offline()
		);
		etpTmp.execute(easOff);

		etpTmp.shutdown();

		try
		{
			etpTmp.awaitTermination(Ezim.exitTimeout, TimeUnit.SECONDS);
		}
		catch(InterruptedException ie)
		{
			EzimLogger.getInstance().warning(ie.getMessage(), ie);
		}

		if (! Ezim.shutdown) System.exit(iIn);
	}

	/**
	 * the main function which gets executed
	 * @param arrArgs command line arguments
	 */
	public static void main(String[] arrArgs)
	{
		Ezim.setDefaultLocale();

		UIManager.put("Button.defaultButtonFollowsFocus", true);

		EzimLang.init();
		EzimImage.init();

		Ezim.initNetConf();

		EzimMain emTmp = EzimMain.getInstance();

		EzimThreadPool etpTmp = EzimThreadPool.getInstance();

		EzimAckSender.prepareSocket();

		Ezim.thDtxTaker = new Thread(EzimDtxTaker.getInstance());
		Ezim.thDtxTaker.setDaemon(true);
		Ezim.thDtxTaker.start();

		Ezim.thAckTaker = new Thread(EzimAckTaker.getInstance());
		Ezim.thAckTaker.setDaemon(true);
		Ezim.thAckTaker.start();

		try
		{
			Thread.sleep(500);
		}
		catch(Exception e)
		{
			EzimLogger.getInstance().severe(e.getMessage(), e);
		}

		// FIXME:2013-11-17:Chun:This make EZIM halts on exit on Win7
/*
		// execute proper ending processes when JVM shuts down
		Runtime.getRuntime().addShutdownHook
		(
			new Thread()
			{
				public void run()
				{
					Ezim.shutdown = true;
					EzimMain.getInstance().panic(0);
				}
			}
		);
*/

		EzimAckSemantics.sendAllInfo();
		emTmp.freshPoll();
	}
}
